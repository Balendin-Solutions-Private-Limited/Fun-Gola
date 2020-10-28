package com.thambola.fungola;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.Model.GetGameWinners;
import com.thambola.fungola.Model.GetUsersPerGame;
import com.thambola.fungola.Model.GetWinnerPrice;
import com.thambola.fungola.Model.MyGameHistory;
import com.thambola.fungola.Model.StartGame;
import com.thambola.fungola.Model.WinningDetails;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.adapter.GameUsersAdapter;
import com.thambola.fungola.adapter.PreviousNumbersAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thambola.fungola.Utils.Constants.roundoffloat;
import static com.thambola.fungola.Utils.Constants.roundoffloat1;

public class View_Results extends AppCompatActivity {

    private StartGame startgameresponse;
    private int presentnumpos;
    private List<String> listofrandomnumbers;
    private PreviousNumbersAdapter previousNumbersAdapter;
    private String access_token;
    private CustomDialog mCustomDialog;
    private String historylayout;
    private String gameid;
   // private float admincharges_per,firstfive_per,lineone_per,linetwo_per,linethree_per,fullhousie_per;
    private MyGameHistory mygamehistory;
   // private CardView coinsboardcard;
    private RelativeLayout coinsboardcard;
    //private static DecimalFormat roundoffloat = new DecimalFormat("0.0");
    private String gametype;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__results);
        Tools.setSystemBarColor(this, R.color.theme_color1);

        coinsboardcard=findViewById(R.id.coinsboardcard);

        access_token = Util.getStringFromSP(View_Results.this, Constants.access_token);


        //Panel1.AddView(VV,10%x,10%y,80%x,80%y);

     //   final RelativeLayout videoview_layout = findViewById(R.id.videoview_layout);
        final VideoView winAnimationView = findViewById(R.id.win_animation);

      //  videoview_layout.setAlpha(1);
      //  final  VideoView winAnimationView=new VideoView(View_Results.this);
    //    videoview_layout.addView(winAnimationView);
      //  videoview_layout.addView(winAnimationView,10,10%y,80%x,80%y);
       // winAnimationView.setAlpha(0.5f);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win_video);
        winAnimationView.setVideoURI(video);

        winAnimationView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                winAnimationView.start();
            }
        });

        winAnimationView.start();
        /*PaintView fireworksView = (PaintView) findViewById(R.id.painview);
        fireworksView.setAlpha(0.5f);*/


        mCustomDialog = new CustomDialog(View_Results.this);


        historylayout= getIntent().getStringExtra("historylayout");

        if(historylayout.equals("true"))
        {

            mygamehistory= (MyGameHistory) getIntent().getSerializableExtra("Mygamehistory");

            gameid= mygamehistory.getGameId();
            gametype=mygamehistory.getGame().getGameType();

           /* admincharges_per= Float.parseFloat(mygamehistory.getGame().getCharge());
            firstfive_per= Float.parseFloat(mygamehistory.getGame().getFirstFive());
            lineone_per= Float.parseFloat(mygamehistory.getGame().getLineOne());
            linetwo_per= Float.parseFloat(mygamehistory.getGame().getLineTwo());
            linethree_per= Float.parseFloat(mygamehistory.getGame().getLineThree());
            fullhousie_per= Float.parseFloat(mygamehistory.getGame().getFullHousee());*/

            coinsboardcard.setVisibility(View.GONE);
        }
        else {
            startgameresponse= (StartGame) getIntent().getSerializableExtra("startgameresponse");
            listofrandomnumbers= (List<String>) getIntent().getSerializableExtra("listofrandomnumbers");
            presentnumpos= getIntent().getIntExtra("presentnumpos",0);
          //  System.out.println("rrrrrrrrrrrrrr presentnumpos viewresults"+presentnumpos);
            if(presentnumpos>89)
                presentnumpos=89;
         //   System.out.println("rrrrrrrrrrrrrr presentnumpos viewresults1"+presentnumpos);

            gameid=startgameresponse.getTicketPurchased().getGameId();
            gametype=startgameresponse.getTicketPurchased().getGame().getGameType();

            /* admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
             firstfive_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getFirstFive());
             lineone_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getLineOne());
             linetwo_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getLineTwo());
             linethree_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getLineThree());
             fullhousie_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getFullHousee());*/

            coinsboardcard.setVisibility(View.VISIBLE);
            /// numbers completed
            coinsboard();
        }




     //   total_money= getIntent().getStringExtra("total_money");




        if (Util.checkInternetConnection(View_Results.this)) {

            if (mCustomDialog != null)
                mCustomDialog.show();
            getGameWinners();

        } else {
            Util.showAlert(View_Results.this, getResources().getString(R.string.check_internet_connection), false);
        }



    }

    private void coinsboard() {

        RecyclerView childrecyclerview = (RecyclerView) findViewById(R.id.previous_numbers_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(View_Results.this,10);
        childrecyclerview.setLayoutManager(gridLayoutManager);
        previousNumbersAdapter = new PreviousNumbersAdapter(View_Results.this,90,presentnumpos,listofrandomnumbers);
        childrecyclerview.setAdapter(previousNumbersAdapter);

    }

    private void getGameWinners() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetGameWinners>> call = apiService.GetGameWinners(gameid,"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GetGameWinners>>() {
            @Override
            public void onResponse(Call<ArrayList<GetGameWinners>> call, Response<ArrayList<GetGameWinners>> response) {


                // if (response.code() == 200) {

                if (response.isSuccessful()) {


                    TextView winner_f5=(TextView) findViewById(R.id.winner_f5);
                    TextView winner_l1=(TextView) findViewById(R.id.winner_l1);
                    TextView winner_l2=(TextView) findViewById(R.id.winner_l2);
                    TextView winner_l3=(TextView) findViewById(R.id.winner_l3);
                    TextView winner_fh=(TextView) findViewById(R.id.winner_fh);


                    ArrayList<GetGameWinners> getGameWinners=new ArrayList<>();

                    getGameWinners.addAll(response.body());

                    try{

                      //  System.out.println("rrrrrrrrr FirstFive "+isListContains("LineOne",getGameWinners)+" :getGameWinners:: "+getGameWinners.size());
                        winner_f5.setText(" "+isListContains("FirstFive",getGameWinners));
                        winner_l1.setText(" "+isListContains("LineOne",getGameWinners));
                        winner_l2.setText(" "+isListContains("LineTwo",getGameWinners));
                        winner_l3.setText(" "+isListContains("LineThree",getGameWinners));
                        winner_fh.setText(" "+isListContains("FullHousee",getGameWinners));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }




                    ///getuserpergame
                    getUsersperGame();


                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                }

            }


            private String isListContains(String whichtype, ArrayList<GetGameWinners> getGameWinners) {

                for(GetGameWinners getGameWinners1: getGameWinners)
                {
                    if(getGameWinners1.getWinType().equals(whichtype))
                        return getGameWinners1.getUsername();

                }


                return "";
            }

            @Override
            public void onFailure(Call<ArrayList<GetGameWinners>> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void getUsersperGame() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetUsersPerGame>> call = apiService.GetUsersPerGame(gameid,"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GetUsersPerGame>>() {
            @Override
            public void onResponse(Call<ArrayList<GetUsersPerGame>> call, Response<ArrayList<GetUsersPerGame>> response) {



                // if (response.code() == 200) {

                if (response.isSuccessful()) {


                    RecyclerView childrecyclerview = (RecyclerView)findViewById(R.id.game_users_recyclerview);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(View_Results.this,2);
                    childrecyclerview.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

                    ArrayList<GetUsersPerGame> getUsersPerGames=new ArrayList<>();
                    getUsersPerGames.addAll(response.body());

                    GameUsersAdapter gameUsersAdapter = new GameUsersAdapter(View_Results.this,getUsersPerGames);
                    childrecyclerview.setAdapter(gameUsersAdapter);


                    /// calulatewinning money
                    calulategamewiningmoney();


                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<GetUsersPerGame>> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void calulategamewiningmoney() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetWinnerPrice>> call = apiService.GetWinnerPrice(gameid,"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GetWinnerPrice>>() {
            @Override
            public void onResponse(Call<ArrayList<GetWinnerPrice>> call, Response<ArrayList<GetWinnerPrice>> response) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
               // System.out.println("rrrrrrrrrrr response code  "+response);

                if (response.isSuccessful()) {



                    TextView win_type_heading = findViewById(R.id.win_type_heading);

                    TextView winner_f5=(TextView) findViewById(R.id.winamount_f5);
                    TextView winner_l1=(TextView) findViewById(R.id.winamount_l1);
                    TextView winner_l2=(TextView) findViewById(R.id.winamount_l2);
                    TextView winner_l3=(TextView) findViewById(R.id.winamount_l3);
                    TextView winner_fh=(TextView) findViewById(R.id.winamount_fh);


                    TextView winner_f5_user=(TextView) findViewById(R.id.winamount_f5_user);
                    TextView winner_l1_user=(TextView) findViewById(R.id.winamount_l1_user);
                    TextView winner_l2_user=(TextView) findViewById(R.id.winamount_l2_user);
                    TextView winner_l3_user=(TextView) findViewById(R.id.winamount_l3_user);
                    TextView winner_fh_user=(TextView) findViewById(R.id.winamount_fh_user);


                    ArrayList<GetWinnerPrice> getWinnerPrices=new ArrayList<>();

                    getWinnerPrices.addAll(response.body());



                    if(gametype.equals(Constants.gametype))
                    {
                        win_type_heading.setText("Win Amount");
                        for(int i=0;i<getWinnerPrices.size();i++)
                        {

                            try{
                                System.out.println("rrrrrrrrrr getWinnerPrices.size() "+getWinnerPrices.size()+" getWinnerPrices.get(i).getWinningDetails() "+getWinnerPrices.get(i).getWinningDetails());
                                if(getWinnerPrices.get(i).getWinningDetails()!=null || getWinnerPrices.get(i).getWinningDetails().size()<0)
                                {
                                    float totalmoney=getWinnerPrices.get(i).getGameTotalPrice();
                                    //   float totalmoney=Float.parseFloat("120");
                                    //  float admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
                                    float firstfive_per= Float.parseFloat(getWinnerPrices.get(i).getFirstFive());
                                    float lineone_per= Float.parseFloat(getWinnerPrices.get(i).getLineOne());
                                    float linetwo_per= Float.parseFloat(getWinnerPrices.get(i).getLineTwo());
                                    float linethree_per= Float.parseFloat(getWinnerPrices.get(i).getLineThree());
                                    float fullhousie_per= Float.parseFloat(getWinnerPrices.get(i).getFullHousee());


                                    //   float admincharges=totalmoney/100*admincharges_per;
                                    float firstfive=totalmoney/100*firstfive_per;
                                    float lineone=totalmoney/100*lineone_per;
                                    float linetwo=totalmoney/100*linetwo_per;
                                    float linethree=totalmoney/100*linethree_per;
                                    float fullhousie=totalmoney/100*fullhousie_per;

                                    System.out.println("rrrrrrrrrrrr totalmoney "+totalmoney);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive "+firstfive+" lineone "+lineone+" fullhousie "+fullhousie/*+" admincharges "+admincharges*/);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive_per "+firstfive_per+" lineone_per "+lineone_per+" fullhousie_per "+fullhousie_per/*+" admincharges_per "+admincharges_per*/);

                                    // RecyclerView childrecyclerview = (RecyclerView) dialog.findViewById(R.id.previous_numbers_recyclerview);
                                    winner_f5.setText(" "+roundoffloat.format(firstfive));
                                    winner_l1.setText(" "+roundoffloat.format(lineone));
                                    winner_l2.setText(" "+roundoffloat.format(linetwo));
                                    winner_l3.setText(" "+roundoffloat.format(linethree));
                                    winner_fh.setText(" "+roundoffloat.format(fullhousie));


                                    winner_f5_user.setText(" "+isListContainswinning("FirstFive",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_l1_user.setText(" "+isListContainswinning("LineOne",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_l2_user.setText(" "+isListContainswinning("LineTwo",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_l3_user.setText(" "+isListContainswinning("LineThree",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_fh_user.setText(" "+isListContainswinning("FullHousee",getWinnerPrices.get(i).getWinningDetails()));

                                }
                            }
                            catch (Exception e)
                            {

                            }



                        }
                    }
                    else
                    {
                        win_type_heading.setText("Win Coins");

                        for(int i=0;i<getWinnerPrices.size();i++)
                        {

                            try{
                                System.out.println("rrrrrrrrrr getWinnerPrices.size() "+getWinnerPrices.size()+" getWinnerPrices.get(i).getWinningDetails() "+getWinnerPrices.get(i).getWinningDetails());
                                if(getWinnerPrices.get(i).getWinningDetails()!=null || getWinnerPrices.get(i).getWinningDetails().size()<0)
                                {
                                    float totalmoney=getWinnerPrices.get(i).getGameTotalPrice();
                                    //   float totalmoney=Float.parseFloat("120");
                                    //  float admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
                                    float firstfive_per= Float.parseFloat(getWinnerPrices.get(i).getFirstFive());
                                    float lineone_per= Float.parseFloat(getWinnerPrices.get(i).getLineOne());
                                    float linetwo_per= Float.parseFloat(getWinnerPrices.get(i).getLineTwo());
                                    float linethree_per= Float.parseFloat(getWinnerPrices.get(i).getLineThree());
                                    float fullhousie_per= Float.parseFloat(getWinnerPrices.get(i).getFullHousee());


                                    System.out.println("rrrrrrrrrrrr totalmoney "+totalmoney);
                                    //    System.out.println("rrrrrrrrrrrrrrrrrr firstfive "+firstfive+" lineone "+lineone+" fullhousie "+fullhousie/*+" admincharges "+admincharges*/);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive_per "+firstfive_per+" lineone_per "+lineone_per+" fullhousie_per "+fullhousie_per/*+" admincharges_per "+admincharges_per*/);

                                    // RecyclerView childrecyclerview = (RecyclerView) dialog.findViewById(R.id.previous_numbers_recyclerview);



                                    winner_f5.setText(" "+roundoffloat1.format(firstfive_per));
                                    winner_l1.setText(" "+roundoffloat1.format(lineone_per));
                                    winner_l2.setText(" "+roundoffloat1.format(linetwo_per));
                                    winner_l3.setText(" "+roundoffloat1.format(linethree_per));
                                    winner_fh.setText(" "+roundoffloat1.format(fullhousie_per));


                                    winner_f5_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("FirstFive",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_l1_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("LineOne",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_l2_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("LineTwo",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_l3_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("LineThree",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_fh_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("FullHousee",getWinnerPrices.get(i).getWinningDetails()))));
                                }
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }



                }
                else {
                    // recyclerView.setVisibility(View.GONE);
                    // tv_bankD.setVisibility(View.VISIBLE);
                    // Util.showAlert((Activity) context, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ArrayList<GetWinnerPrice>> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });

    }



    private String isListContainswinning(String whichtype, ArrayList<WinningDetails> getGameWinners) {

        String winmoney="0";
        for(WinningDetails getGameWinners1: getGameWinners)
        {
            if(getGameWinners1.getWinType().equals(whichtype))
            {
                System.out.println("rrrrrrrrrrr userwin money "+""+getGameWinners1.getMoney());
                return ""+getGameWinners1.getMoney();
            }

        }


        return winmoney;
    }

    /*private void bannerdisplay() {
        adView = new AdView(getApplicationContext(), getResources().getString(R.string.banneraplacementid), AdSize.BANNER_HEIGHT_50);

        LinearLayout adContainer = findViewById(R.id.banner_container);

        adContainer.addView(adView);

        adView.loadAd();
    }*/

}
