package com.thambola.fungola;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionManager;

import com.thambola.fungola.Animation.Rotate;
import com.thambola.fungola.Model.GameList;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.adapter.GamesTimesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectOptionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GamesTimesAdapter adapter;
    private RelativeLayout rootview;
    ImageView refresh;
    TextView heading,nogametext;
    //ImageView imageView1;
    RelativeLayout backgroundcolor;
    private CustomDialog mCustomDialog;
    ArrayList<GameList> allgamelist;
    private String access_token;
    private String gametype;
    private Context context;
    private int id;
    private boolean mRotated;
    private float free_coins,wallet_money;
    private String convertedTime,convertedDate;
    private boolean gamerunning;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        Tools.setSystemBarColor(this, R.color.theme_color1);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);


        nogametext=(TextView)findViewById(R.id.nogametext);
        heading=(TextView)findViewById(R.id.heading);
        backgroundcolor=(RelativeLayout) findViewById(R.id.backgroundcolor);
        refresh=findViewById(R.id.refresh);
        rootview= findViewById(R.id.rootview);


        String head=getIntent().getStringExtra("heading");
        gametype=getIntent().getStringExtra("gametype");
        heading.setText(""+head);

        context=this;
        mCustomDialog = new CustomDialog(SelectOptionActivity.this);
        access_token = Util.getStringFromSP(SelectOptionActivity.this, Constants.access_token);

        allgamelist=new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        int numberOfColumns = 2;
      //  recyclerView.setHasFixedSize(true);
        //recyclerView.setPreserveFocusAfterLayout(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        id = getIntent().getIntExtra("NOTIFICATION_ID", 0);

        adapter = new GamesTimesAdapter(SelectOptionActivity.this,context, allgamelist,  gametype ,access_token,id);
        recyclerView.setAdapter(adapter);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(rootview, new Rotate());

                mRotated = !mRotated;
                refresh.setRotation(mRotated ? 135 : 0);


            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Util.checkInternetConnection(SelectOptionActivity.this)) {

                    getWalletBalence();

                } else {
                    Util.showAlert(SelectOptionActivity.this, getResources().getString(R.string.check_internet_connection), false);
                }

            }
        });


    }



    public void GetAllGamesList() {

        allgamelist.clear();

        System.out.println("rrrrrrrrrrr gametype "+gametype);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GameList>> call = apiService.ALLGAMELIST(gametype,"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GameList>>() {
            @Override
            public void onResponse(Call<ArrayList<GameList>> call, Response<ArrayList<GameList>> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                System.out.println("rrrrrrrrrrr response code  "+response.code()+" response:: "+response);

                if (response.isSuccessful()) {

                    id = getIntent().getIntExtra("NOTIFICATION_ID", 0);

                    ArrayList<GameList> gameLists=new ArrayList<>();

                   // allgamelist.addAll(response.body());

                    gameLists.addAll(response.body());

                    System.out.println("rrrrrrrrr response.body().size() "+response.body().size()+" gameLists.size() "+gameLists.size());


                        if (response.body().size() > 0) {

                            nogametext.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);


                            GetServerTime(gameLists);

                    }
                    else {
                        nogametext.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
                //  }
                 else if (response.code()==500) {
                    Util.showAlert(SelectOptionActivity.this, ""+response.message(), false);

                    }
                else {
                    //psLrecycleView.setVisibility(View.GONE);
                   // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(SelectOptionActivity.this, "Unable to Process Request...", false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<GameList>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });

    }

    public void onTicketsClick(int ticketbuyedprice, int position, String gameType) {

        adapter.onMethodCallback(ticketbuyedprice,position,gameType);

    }

    @Override
    protected void onResume() {

       // System.out.println("rrrrrrrrr onresume games list called");
        if (Util.checkInternetConnection(SelectOptionActivity.this)) {


            getWalletBalence();


        } else {
            Util.showAlert(SelectOptionActivity.this, getResources().getString(R.string.check_internet_connection), false);
        }
        super.onResume();
    }

    public void getWalletBalence() {

        if (mCustomDialog != null)
            mCustomDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetWalletbalance("Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            // private JSONObject jsonObject;

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                /*if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }*/
                System.out.println("rrrrrrrrrrr response code wallet balence  "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    try {

                        String walletresponse=response.body().string();

                        System.out.println("rrrrrrrrrrrrr walletresponse "+walletresponse);

                        JSONObject jsonObject = new JSONObject(walletresponse);

                        System.out.println("rrrrrrrrrrrr amountbalance: "+jsonObject.getString("amountBalance"));

                        String wallet_money1 = jsonObject.getString("amountBalance");
                        String free_coins1 = jsonObject.getString("coinBalance");
                        free_coins= Float.parseFloat(free_coins1);
                        wallet_money= Float.parseFloat(wallet_money1);

                        if (Util.checkInternetConnection(SelectOptionActivity.this)) {
                            GetAllGamesList();
                        } else {
                            Util.showAlert(SelectOptionActivity.this, getResources().getString(R.string.check_internet_connection), false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {
                     Util.showAlert(SelectOptionActivity.this, ""+response.message(), false);

                }
                else {
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    // Util.showAlert(MainScreen.this, "Unable to Process Request...", false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void GetServerTime(final ArrayList<GameList> gamelist) {

        //final String[] currenttime = new String[1];

        final ApiInterface apiService =
                ApiClient.getClient_serverTIme().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetServerTime();
        call.enqueue(new Callback<ResponseBody>() {

           @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("rrrrrrrrrrr response code GetVersionCode servertime "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    try {

                        String servertime=response.body().string();

                      //  System.out.println("rrrrrrrrrrrrr servertime "+servertime);

                        JSONObject jsonObject = new JSONObject(servertime);

                        String currenttime =jsonObject.getString("datetime");
                      //  System.out.println("rrrrrrrrrrrrr currenttime "+ currenttime);

                        for(int i=0;i<gamelist.size();i++)
                        {

                            if(convertDate_server(gamelist.get(i).getGameStartTime(),currenttime))
                            {
                                System.out.println("rrrrrrrrr gamerunning "+gamerunning);
                                System.out.println("rrrrrrrrr convertedDate "+convertedDate);
                                System.out.println("rrrrrrrrr convertedTime "+convertedTime);

                                gamelist.get(i).setGamerunning(gamerunning);
                                gamelist.get(i).setConvertedDate(convertedDate);
                                gamelist.get(i).setConvertedTime(convertedTime);

                                allgamelist.add(gamelist.get(i));
                            }
                        }
                        if(allgamelist.size()>0)
                        {
                            adapter.setNewGamelist(allgamelist,id,wallet_money,free_coins);


                        } else {
                            nogametext.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {

                    Util.showAlert(SelectOptionActivity.this, ""+response.message(), false);

                }
                else {

                    Util.showAlert(SelectOptionActivity.this, "Unable to Process Request...", false);
                }
               swipeRefreshLayout.setRefreshing(false);
            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

                Log.d("response","Getting response from server : "+t);
            }
        });

        //  return currenttime[0];

    }


    public boolean convertDate_server(String date, String currentdate) {

        try {
        System.out.println("rrrrrrrrrrr date:: "+date+" ::: "+currentdate);
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss aa");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date gamedate = null;
        try {
            gamedate = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();

            System.out.println("rrrrrrrrrrr erroe "+e.getMessage());
        }


        SimpleDateFormat timeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        String convertedDte = timeFormatter1.format(gamedate);


        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        String convertedTime = sdf1.format(gamedate);
        // System.out.println("rrrrrrrrrr Datetime1 "+convertedTime);

        /*Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String currentdate = dateFormat.format(calendar1.getTime());*/


        SimpleDateFormat extra_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gamedate);
        calendar.add(Calendar.MINUTE, 10);
        String extra_date = extra_format.format(calendar.getTime());


     //   System.out.println("rrrrrrrrrr currentdate "+currentdate+" convertedDte "+convertedDte);



            Date currentdatetime=format.parse(currentdate);
            Date extra_datetime=format.parse(extra_date);

          //  System.out.println("rrrrrrrrr currentdatetime: "+currentdatetime+" extra_datetime: "+extra_datetime);
            //  Date currentdatetime = format.parse("2020-04-17T15:11:00");
            //   Date currentdatetime = format.parse("2020-04-17T17:05:00");


            if(currentdate.contains(convertedDte))
            {
                // System.out.println("rrrrrrrrrrrrr enter");

                this.convertedTime=convertedTime;
                this.convertedDate="Today";
                if(currentdatetime.after(gamedate))
                {
                    //  System.out.println("rrrrrrrrrrrrr enter 1111111");
                    if(currentdatetime.before(extra_datetime))
                    {
                        //  System.out.println("rrrrrrrrrrrrr enter 22222222");
                        gamerunning=true;
                        return true;
                    }
                    else {
                        //  System.out.println("rrrrrrrrrrrrr enter 333333");
                        gamerunning=false;
                        return false;
                    }
                }
                else {
                    // System.out.println("rrrrrrrrrrrrr enter 444444");
                    gamerunning=false;
                    return true;
                }

            }
            else if(gamedate.before(currentdatetime)){
                this.convertedTime=convertedTime;
                this.convertedDate=convertedDte;
                gamerunning=false;
                return false;
            }
            else
            {
                // this.convertedTime=(convertedDte+" "+convertedTime);
                this.convertedTime=convertedTime;
                this.convertedDate=convertedDte;
                gamerunning=false;
                return true;

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }



        return true;
    }

    public boolean convertDate(String date) {

        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss aa");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date gamedate = null;
        try {
            gamedate = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();

            System.out.println("rrrrrrrrrrr erroe "+e.getMessage());
        }


        SimpleDateFormat timeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        String convertedDte = timeFormatter1.format(gamedate);


        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        String convertedTime = sdf1.format(gamedate);
        // System.out.println("rrrrrrrrrr Datetime1 "+convertedTime);

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String currentdate = dateFormat.format(calendar1.getTime());


        SimpleDateFormat extra_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gamedate);
        calendar.add(Calendar.MINUTE, 10);
        String extra_date = extra_format.format(calendar.getTime());


        System.out.println("rrrrrrrrrr currentdate "+currentdate+" convertedDte "+convertedDte);

        try {

            Date currentdatetime=format.parse(currentdate);
            Date extra_datetime=format.parse(extra_date);

            System.out.println("rrrrrrrrr currentdatetime: "+currentdatetime+" extra_datetime: "+extra_datetime);
            //  Date currentdatetime = format.parse("2020-04-17T15:11:00");
            //   Date currentdatetime = format.parse("2020-04-17T17:05:00");


            if(currentdate.contains(convertedDte))
            {
                // System.out.println("rrrrrrrrrrrrr enter");

                this.convertedTime=convertedTime;
                this.convertedDate="Today";
                if(currentdatetime.after(gamedate))
                {
                    //  System.out.println("rrrrrrrrrrrrr enter 1111111");
                    if(currentdatetime.before(extra_datetime))
                    {
                        //  System.out.println("rrrrrrrrrrrrr enter 22222222");
                        gamerunning=true;
                        return true;
                    }
                    else {
                        //  System.out.println("rrrrrrrrrrrrr enter 333333");
                        gamerunning=false;
                        return false;
                    }
                }
                else {
                    // System.out.println("rrrrrrrrrrrrr enter 444444");
                    gamerunning=false;
                    return true;
                }

            }
            else if(gamedate.before(currentdatetime)){
                this.convertedTime=convertedTime;
                this.convertedDate=convertedDte;
                gamerunning=false;
                return false;
            }
            else
            {
                // this.convertedTime=(convertedDte+" "+convertedTime);
                this.convertedTime=convertedTime;
                this.convertedDate=convertedDte;
                gamerunning=false;
                return true;

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }



        return true;
    }

}
