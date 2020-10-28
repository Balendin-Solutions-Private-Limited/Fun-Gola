package com.thambola.fungola.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.thambola.fungola.LocalNotification.database.DatabaseHelper;
import com.thambola.fungola.LocalNotification.models.Reminder;
import com.thambola.fungola.LocalNotification.receivers.AlarmReceiver;
import com.thambola.fungola.LocalNotification.utils.AlarmUtil;
import com.thambola.fungola.LocalNotification.utils.DateAndTimeUtil;
import com.thambola.fungola.Model.BuyTicket;
import com.thambola.fungola.Model.GameList;
import com.thambola.fungola.Model.StartGame;
import com.thambola.fungola.MyThambolaActivity;
import com.thambola.fungola.R;
import com.thambola.fungola.Randomnumbers;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.SelectOptionActivity;
import com.thambola.fungola.Store_DB.SharedPreference;
import com.thambola.fungola.Store_DB.Store_GameResponse;
import com.thambola.fungola.Utils.AESUtils;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thambola.fungola.Utils.Constants.roundoffloat;
import static com.thambola.fungola.Utils.Constants.roundoffloat1;

/**
 * Created by Admin on 4/25/2019.
 */

public class GamesTimesAdapter extends RecyclerView.Adapter<GamesTimesAdapter.ViewHolder>{

    //private final Context context1;
    private final String access_token,userid;
    private final Context context;
    private final android.app.Activity Activity;
    //private RelativeLayout relativeLayout_main,relativeLayout;
    private RelativeLayout.LayoutParams params;
    private SharedPreference sharedPreference;
    private ArrayList<Store_GameResponse> getgameresponse;
    //private final Activity context;
    private ArrayList<GameList> gamelist;
   // private int[] mimageData;
    private LayoutInflater mInflater;
    private GamesTimesAdapter.ItemClickListener mClickListener;
    String gametype;
    private CustomDialog mCustomDialog;
    //private BuyTicket buyTicket;
   // private StartGame startGame;
    private ArrayList<String> myTextViewListvalues=new ArrayList<>();
    Map<Integer,GameList> deletedItems;

    private int positionval;
    private TextView numberofcoinstext;
    private int numberofticketwantstobuy;
    private Button proceed;
    private String convertedTime,convertedDate;
    private boolean gamerunning;
    private StartGame store_startgameresponse;
    private String store_encrypted;

    private Calendar calendar;
    private String icon,colour;
    private int repeatType;
    private int id;
    private int timesShown = 0;
    private int timesToShow = 1;
    private int interval = 1;
    private int minutes=23;
    private float wallet_money,free_coins;
    private float ticketbuyedprice;
   // private int position;


    // data_maths is passed into the constructor
    public GamesTimesAdapter(android.app.Activity selectOptionActivity, Context context, ArrayList<GameList> data, String gamtype, String access_token, int id) {
        this.mInflater = LayoutInflater.from(context);
        this.gamelist = data;
        this.id=id;
        //mimageData=imgedata;
       //context1=context;
        this.context=context;
        gametype=gamtype;
        this.Activity=selectOptionActivity;
        this.access_token=access_token;

      //  setHasStableIds(true);

        userid = Util.getStringFromSP(context, Constants.UserId);

        mCustomDialog=new CustomDialog(context);

        sharedPreference = new SharedPreference();

        getgameresponse = sharedPreference.getGames(context);

        calendar = Calendar.getInstance();
        icon = context.getString(R.string.default_icon_value);
        colour = context.getString(R.string.default_colour_value);
        repeatType = Reminder.DOES_NOT_REPEAT;
       // this.id = getIntent().getIntExtra("NOTIFICATION_ID", 0);

        // Check whether to edit or create a new notification
        if (this.id == 0) {
            DatabaseHelper database = DatabaseHelper.getInstance(context);
            this.id = database.getLastNotificationId() + 1;
            database.close();
        } /*else {
            assignReminderValues();
        }*/

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public GamesTimesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gamestimes, parent, false);
        return new GamesTimesAdapter.ViewHolder(view);
    }

    // binds the data_maths to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final GamesTimesAdapter.ViewHolder holder, final int position) {


        //this.position=position;
        /*if(convertDate(gamelist.get(position).getGameStartTime()))
        {*/
            if(gamelist.get(position).getGameType().equals(gametype))
            {
                holder.relativeLayout.setVisibility(View.VISIBLE);
                holder.relativeLayout_main.setVisibility(View.VISIBLE);
                holder.textView_particpents.setVisibility(View.VISIBLE);
                holder.textView_ticketcost.setVisibility(View.VISIBLE);
                holder.textView_ticketsbuy.setVisibility(View.VISIBLE);
                holder.textView_totalcost.setVisibility(View.VISIBLE);
                holder.textView_buytickets.setVisibility(View.VISIBLE);
                holder.gamestatingtime.setVisibility(View.VISIBLE);
                holder.gamestatus.setVisibility(View.VISIBLE);
                holder.calulatemoney.setVisibility(View.VISIBLE);
              //  holder.lineralll.setVisibility(View.VISIBLE);
                holder.ic_buy_ticket.setVisibility(View.VISIBLE);
                holder.textView_startgame.setVisibility(View.VISIBLE);
              //  holder.relativeLayout.setVisibility(View.VISIBLE);
              //  holder.relativeLayout_main.setVisibility(View.VISIBLE);

                holder.relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.relativeLayout_main.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                holder.textView_particpents.setText(""+gamelist.get(position).getNumberOfUsersPurchased());

                if(gamelist.get(position).getGameType().equals(Constants.gametype)) {
                    holder.textView_ticketcost.setText("" + gamelist.get(position).getPrice());
                    holder.textView_totalcost.setText(""+gamelist.get(position).getTotalPrice());
                }
                else {
                    holder.textView_ticketcost.setText("Free");
                    holder.textView_totalcost.setText("0");
                }
                holder.textView_ticketsbuy.setText("0");
                // holder.gamestatingtime.setText("Game Will Starts At "+gamelist.get(position).getGameStartTime());


                //  String convertedtime=convertDate(gamelist.get(position).getGameStartTime());
                // convertDate(gamelist.get(position).getGameStartTime());

                //if(gamerunning)
                if(gamelist.get(position).gamerunning)
                    holder.gamestatus.setVisibility(View.VISIBLE);
                else
                    holder.gamestatus.setVisibility(View.INVISIBLE);

                holder.gamestatingtime.setText(""+gamelist.get(position).getConvertedTime());
                holder.gamestatingdate.setText(""+gamelist.get(position).getConvertedDate());
                //getCurrentDate(gamelist.get(position).getGameStartTime());

                // holder.textView_ticketsbuy.setText(""+buyTicket.getNumberOfTickets());
                if(gamelist.get(position).getNumberOfTicketsPurchasedByUser()>0)
                {
                    holder.ic_buy_ticket.setVisibility(View.VISIBLE);
                    holder.textView_buytickets.setBackgroundResource(R.drawable.game_buyed);
                  //  holder.relativeLayout.setCardBackgroundColor(context.getResources().getColor(R.color.red_400));
                }
                else {
                    holder.ic_buy_ticket.setVisibility(View.INVISIBLE);
                    holder.textView_buytickets.setBackgroundResource(R.drawable.game_buy);
                   // holder.relativeLayout.setCardBackgroundColor(Color.TRANSPARENT);
                }
                holder.textView_ticketsbuy.setText(""+gamelist.get(position).getNumberOfTicketsPurchasedByUser());
            }
            else {

             //   holder.relativeLayout.setVisibility(View.INVISIBLE);
             //   holder.relativeLayout_main.setVisibility(View.INVISIBLE);
            //    holder.relativeLayout.setVisibility(View.GONE);
              //  holder.relativeLayout_main.setVisibility(View.GONE);

                holder.relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));

                holder.relativeLayout.setVisibility(View.GONE);
                holder.relativeLayout_main.setVisibility(View.GONE);
                holder.textView_particpents.setVisibility(View.GONE);
                holder.textView_ticketcost.setVisibility(View.GONE);
                holder.textView_ticketsbuy.setVisibility(View.GONE);
                holder.textView_totalcost.setVisibility(View.GONE);
                holder.textView_buytickets.setVisibility(View.GONE);
                holder.gamestatingtime.setVisibility(View.GONE);
                holder.gamestatus.setVisibility(View.GONE);
                holder.calulatemoney.setVisibility(View.GONE);
             //   holder.lineralll.setVisibility(View.GONE);
                holder.ic_buy_ticket.setVisibility(View.GONE);
                holder.textView_startgame.setVisibility(View.GONE);
                holder.Layout_hide();
                holder.relativeLayout_main.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));

                return;
              //  hideItem(position);
               // gamelist.remove(position);
              //  notifyItemRemoved(position);
            }
       // }
       /* else {

          //  holder.relativeLayout.setVisibility(View.INVISIBLE);
         //   holder.relativeLayout_main.setVisibility(View.INVISIBLE);
         //   holder.relativeLayout.setVisibility(View.GONE);
          //  holder.relativeLayout_main.setVisibility(View.GONE);

            holder.relativeLayout.setVisibility(View.GONE);
            holder.relativeLayout_main.setVisibility(View.GONE);
            holder.textView_particpents.setVisibility(View.GONE);
            holder.textView_ticketcost.setVisibility(View.GONE);
            holder.textView_ticketsbuy.setVisibility(View.GONE);
            holder.textView_totalcost.setVisibility(View.GONE);
            holder.textView_buytickets.setVisibility(View.GONE);
            holder.gamestatingtime.setVisibility(View.GONE);
            holder.gamestatus.setVisibility(View.GONE);
            holder.calulatemoney.setVisibility(View.GONE);
          //  holder.lineralll.setVisibility(View.GONE);
            holder.ic_buy_ticket.setVisibility(View.GONE);
            holder.textView_startgame.setVisibility(View.GONE);

            holder.relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));

            holder.Layout_hide();
            holder.relativeLayout_main.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));

            return;
         //   hideItem(position);
          //  gamelist.remove(position);
           // notifyItemRemoved(position);

        }*/



        holder.calulatemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calulategamewiningmoney(gamelist.get(position));
            }
        });

       holder.textView_startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context1, "Clicked"+position, Toast.LENGTH_SHORT).show();

              //  new generatetickets(buyTicket.getNumberOfTickets()).execute();

                System.out.println("rrrrrrrr gamelist.get(position).getGameStartTime() "+gamelist.get(position).getGameStartTime()+" : : ");

                    if(gamelist.get(position).getNumberOfTicketsPurchasedByUser()>0) {

                        GetServerTime(gamelist.get(position).getGameStartTime(),position);


                    }

                    else {
                        Toast.makeText(context, "Please Buy Atleast one Ticket", Toast.LENGTH_SHORT).show();
                    }
               // }

            }
        });

        holder.textView_buytickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numberofticketwantstobuy=0;
               //
                try{
                 //   System.out.println("rrrrrrrrr  proceed "+proceed);
                    proceed.setEnabled(false);
                    proceed.setBackgroundResource(R.color.grey_300);
                }
                catch (Exception e)
                {

                }



                    if(gamelist.get(position).getGameType().equals(Constants.gametype))
                    {
                        if(holder.gamestatus.getVisibility()== View.VISIBLE)
                         alreadygamestatred();
                        else
                         ticketbuydailog(gamelist.get(position));

                    }
                    else
                 //   ticketbuydailog(gamelist.get(position).getGameId(),gamelist.get(position).getMaxTicketsPerGamer(),gamelist.get(position).getPrice(),gamelist.get(position).getGameStartTime(),gamelist.get(position).getGameType());
                    ticketbuydailog(gamelist.get(position));

              // }
            }
        });
    }



    public void ticketbuydailog(GameList gameList){

        final int gameId=gameList.getGameId();
        final int maxTicketsPerGamer=gameList.getMaxTicketsPerGamer();
        final String ticketprice=gameList.getPrice();
        final String gameStartTime=gameList.getGameStartTime();
        final String gameType=gameList.getGameType();
        final int numberOfTicketsPurchasedByUser=gameList.getNumberOfTicketsPurchasedByUser();

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ticketsbuydailog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // dialog.setTitle(" Please check you Email for OTP. And use it within 10 Minutes.");

        ImageView ic_close =   dialog.findViewById(R.id.ic_close);
        numberofcoinstext =  (TextView) dialog.findViewById(R.id.numberofcoinstext);
        proceed = dialog.findViewById(R.id.proceed);

        if(numberofticketwantstobuy!=0)
        {
            proceed.setEnabled(true);
            proceed.setBackgroundResource(R.drawable.request_button);
        }
        else
        {
            proceed.setEnabled(false);
           // Toast.makeText(context, "Please Select Atleast One Ticket to Proceed", Toast.LENGTH_SHORT).show();
        }

        //android:background="@drawable/request_button"
        RecyclerView recyclerView = dialog. findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));


        //System.out.println("rrrrrrrrrrrr maxTicketsPerGamer "+maxTicketsPerGamer+" ticketprice "+ticketprice);


        recyclerView.setAdapter(new ImagesAdapter(context,maxTicketsPerGamer,ticketprice,gameType,numberOfTicketsPurchasedByUser));

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numberofticketwantstobuy!=0)
                {
                    dialog.dismiss();

                    if(gameType.equals(Constants.gametype))
                    {

                        if(((free_coins/100)*Constants.freecoinsvalue)>=ticketbuyedprice)
                        {
                            if (mCustomDialog != null)
                                mCustomDialog.show();
                            ProcessWallet(""+(numberofticketwantstobuy*((Float.parseFloat(ticketprice)/Constants.freecoinsvalue)*100)),true,"Debit","Game","Withdraw Coins"
                                    ,gameId,numberofticketwantstobuy,gameStartTime,gameType);


                        }
                        else if(wallet_money>=ticketbuyedprice)
                        {

                            if (mCustomDialog != null)
                                mCustomDialog.show();
                            ProcessWallet(""+ticketbuyedprice,false,"Debit","Game","Withdraw Money"
                                    ,gameId,numberofticketwantstobuy,gameStartTime,gameType);
                        }

                        else {

                            showinfoDialog();
                        }
                    }
                    else {

                        if (mCustomDialog != null)
                            mCustomDialog.show();
                        BuyTickets(gameId, numberofticketwantstobuy,gameStartTime,gameType);
                    }
                }
                else
                {

                    Toast.makeText(context, "Please Select Atleast One Ticket to Proceed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();

        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return gamelist.size();
    }



    public void onMethodCallback(int ticketbuyedprice, int position, String gameType) {

        this.ticketbuyedprice=ticketbuyedprice;
        numberofticketwantstobuy=position;
        if(gameType.equals(Constants.gametype))
            numberofcoinstext.setText(""+ticketbuyedprice+" Coins Will be charged for "+position+" Tickets");
        else
            numberofcoinstext.setText("Selected "+position+" Tickets are for FREE");
        proceed.setEnabled(true);
        proceed.setBackgroundResource(R.drawable.request_button);
    }

    public void setNewGamelist(ArrayList<GameList> allgamelist, int id, float wallet_money, float free_coins) {

        this.wallet_money=wallet_money;
        this.free_coins=free_coins;
        this.id=id;
        try {

            getgameresponse = sharedPreference.getGames(context);
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        this.gamelist=allgamelist;
        notifyDataSetChanged();

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_startgame,textView_particpents,textView_ticketcost,textView_ticketsbuy,
                textView_totalcost,textView_buytickets,gamestatingtime,gamestatingdate,gamestatus;
        ImageView ic_buy_ticket,calulatemoney;
        //RelativeLayout lineralll;
        //CardView relativeLayout;
        RelativeLayout relativeLayout,relativeLayout_main;


        ViewHolder(View itemView) {
            super(itemView);
            textView_startgame = itemView.findViewById(R.id.textView_startgame);
            textView_particpents = itemView.findViewById(R.id.textView_particpents);
            textView_ticketcost = itemView.findViewById(R.id.textView_ticketcost);
            textView_ticketsbuy = itemView.findViewById(R.id.textView_ticketsbuy);
            textView_totalcost = itemView.findViewById(R.id.textView_totalcost);
            textView_buytickets = itemView.findViewById(R.id.textView_buytickets);
            gamestatingtime = itemView.findViewById(R.id.gamestatingtime);
            gamestatingdate = itemView.findViewById(R.id.gamestatingdate);
            gamestatus = itemView.findViewById(R.id.gamestatus);
            calulatemoney = itemView.findViewById(R.id.calulatemoney);
         //   lineralll = itemView.findViewById(R.id.lineralll);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            relativeLayout_main = itemView.findViewById(R.id.relativeLayout_main);
            ic_buy_ticket = itemView.findViewById(R.id.ic_buy_ticket);

            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
           // imagevieww = itemView.findViewById(R.id.imageView);

            /*if(gamelist.get(position).getGameType().equals(gametype))
            {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout_main.setVisibility(View.VISIBLE);
                textView_particpents.setVisibility(View.VISIBLE);
                textView_ticketcost.setVisibility(View.VISIBLE);
                textView_ticketsbuy.setVisibility(View.VISIBLE);
                textView_totalcost.setVisibility(View.VISIBLE);
                textView_buytickets.setVisibility(View.VISIBLE);
                gamestatingtime.setVisibility(View.VISIBLE);
                gamestatus.setVisibility(View.VISIBLE);
                calulatemoney.setVisibility(View.VISIBLE);
                //  lineralll.setVisibility(View.VISIBLE);
                ic_buy_ticket.setVisibility(View.VISIBLE);
                textView_startgame.setVisibility(View.VISIBLE);
            }
            else {
                relativeLayout.setVisibility(View.GONE);
                relativeLayout_main.setVisibility(View.GONE);
                textView_particpents.setVisibility(View.GONE);
                textView_ticketcost.setVisibility(View.GONE);
                textView_ticketsbuy.setVisibility(View.GONE);
                textView_totalcost.setVisibility(View.GONE);
                textView_buytickets.setVisibility(View.GONE);
                gamestatingtime.setVisibility(View.GONE);
                gamestatus.setVisibility(View.GONE);
                calulatemoney.setVisibility(View.GONE);
//            lineralll.setVisibility(View.GONE);
                ic_buy_ticket.setVisibility(View.GONE);
                textView_startgame.setVisibility(View.GONE);
            }*/


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }

        public void Layout_hide() {
            params.height = 0;
            params.width=0;
            //itemView.setLayoutParams(params); //This One.
         //   relativeLayout_main.setLayoutParams(params);   //Or This one.
            relativeLayout.setLayoutParams(params);   //Or This one.

        }


    }

    public void hideItem(final int position) {
        //deletedItems.add(position, gamelist.get(position));
        gamelist.remove(position);
        notifyItemRemoved(position);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    private void BuyTickets(int gameId, int numbfotickets, final String gameStartTime, final String gameType) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("GameId", gameId);
        jsonObject.addProperty("NumberOfTickets", numbfotickets);

        System.out.println("rrrrrrrrrr gameId "+gameId+" numbfotickets "+numbfotickets);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicket> call = apiService.BuyTicket(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<BuyTicket>() {
            @Override
            public void onResponse(Call<BuyTicket> call, Response<BuyTicket> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }


                System.out.println("rrrrrrrrrrrrrrr BuyTickets "+response);
                System.out.println("rrrrrrrrrrrrrrr BuyTickets response "+response.isSuccessful());

                if (response.isSuccessful()) {

                 //   buyTicket=response.body();

                    Toast.makeText(context, "HEY!! You Have Buyed Tickets", Toast.LENGTH_SHORT).show();

                  //  ((SelectOptionActivity)context).GetAllGamesList();
                    ((SelectOptionActivity)context).getWalletBalence();

                    saveNotification(gameStartTime,gameType);

                }
               else if (response.code() == 400) {

                    String eresponse= null;
                    try {
                        eresponse = response.errorBody().string();

                            JSONObject jsonObject = new JSONObject(eresponse);

                        JSONObject modelState = jsonObject.getJSONObject("modelState");

                        JSONArray error = modelState.getJSONArray("error");

                        Util.showAlert(Activity, ""+error.get(0), false);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {
                    Util.showAlert(Activity, ""+response.message(), false);

                }
                else {
                    Util.showAlert(Activity, "Unable to Process Request...", false);
                }

            }

            @Override
            public void onFailure(Call<BuyTicket> call, Throwable t) {
                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void Startgame(final GameList gamelist, final String encrypted) {

        if (mCustomDialog != null)
            mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gameId", gamelist.getGameId());
        jsonObject.addProperty("ticketInfo", encrypted);


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<StartGame> call = apiService.StartGame(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<StartGame>() {
            @Override
            public void onResponse(Call<StartGame> call, Response<StartGame> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }



                if (response.isSuccessful()) {

                    Util.saveStringInSP(context, Constants.claimmessage,"null");

                    StartGame startGame1=response.body();

                    Intent intent = new Intent(context, MyThambolaActivity.class);
                    intent.putExtra("startgameresponse",(Serializable)startGame1);
                    intent.putExtra("encrypted",encrypted);
                    intent.putExtra(""+Constants.isgamestored,false);
                    context.startActivity(intent);


                }
                else {
                    Util.showAlert(Activity, "Unable to Process Request...", false);
                }

            }

            @Override
            public void onFailure(Call<StartGame> call, Throwable t) {
                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }





    public boolean dateexceed(String date, String currentdate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");



      /*  //////////// server time
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String currentdate = dateFormat.format(calendar1.getTime());*/


        System.out.println("rrrrrrrrrr server time :::  "+currentdate);


        SimpleDateFormat dateFormatcurrent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            Date currentdatetime = dateFormatcurrent.parse(""+currentdate);
          //  Date currentdatetime=format.parse(currentdate);
            Date extra_datetime=format.parse(date);

              System.out.println("rrrrrrrrrrr currentdatetime : "+currentdatetime+" currentdate : "+currentdate);
              System.out.println("rrrrrrrrrrr extra_datetime : "+extra_datetime+" date : "+date);

            if(currentdatetime.after(extra_datetime))
                return true;
            else
                return false;


        } catch (ParseException e) {
            e.printStackTrace();
        }



        return true;
    }


    public boolean convertDate_main(String date) {

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

        try {

            Date currentdatetime=format.parse(currentdate);
            Date extra_datetime=format.parse(extra_date);

          //  Date currentdatetime = format.parse("2020-04-17T15:11:00");
         //   Date currentdatetime = format.parse("2020-04-17T17:05:00");


            /*System.out.println("rrrrrrrrrrrrrr gamedate "+gamedate);
            System.out.println("rrrrrrrrrrrrrr extra_datetime "+extra_datetime);
            System.out.println("rrrrrrrrrrrrrr currentdatetime "+currentdatetime);*/



            if(currentdate.contains(convertedDte))
            {
               // System.out.println("rrrrrrrrrrrrr enter");
                this.convertedTime=convertedTime;
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




        if(currentdate.contains(convertedDte)) {
            System.out.println("rrrrrrrrrrrrr enter");
            this.convertedTime = convertedTime;
            this.convertedDate = "Today";
        }
        else
        {
            // this.convertedTime=(convertedDte+" "+convertedTime);
            this.convertedTime=convertedTime;
            this.convertedDate=convertedDte;
        }

        return true;
    }



    public class generatetickets extends AsyncTask<Void, Void, Void> {

        private final int numberOfTickets;
        private final GameList gameId;
       // private final String totalPrice;
        private String listString="";
        public generatetickets(int numberOfTickets, GameList gameList) {

            this.numberOfTickets=numberOfTickets;
            this.gameId=gameList;
          //  this.totalPrice=totalPrice;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i=0;i<numberOfTickets;i++)
            {
                int i4 = 0;

                myTextViewListvalues.clear();
                for(int listval=0;listval<27;listval++)
                {
                    myTextViewListvalues.add("0");
                }

                ArrayList<Randomnumbers> randomnumlist=new ArrayList<>();
                List<Integer> asList = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(5)});
                Collections.shuffle(asList);

                while (i4 < 9) {
                    ArrayList randomNumbers = getRandomNumbers(Integer.valueOf(i4 == 0 ? 1 : i4 * 10), Integer.valueOf((i4 * 10) + 9), (Integer) asList.get(i4));

                    randomnumlist.add(new Randomnumbers(i,randomNumbers,((Integer) asList.get(i4)).intValue()));

                  //  System.out.println("rrrrrrrrrrrrrrrr randomnumlist.size() "+randomnumlist.size()+" iiiiiiiii "+i+" randomNumbers "+randomNumbers.size()+" asList.get(i4): "+((Integer) asList.get(i4)).intValue());


                    for (int i7 = 0; i7 < randomNumbers.size(); i7++) {
                        if (i4 == 0) {
                            if (((Integer) asList.get(i4)).intValue() == 4) {

                                myTextViewListvalues.set(((i7 * 9) + 18),""+randomNumbers.get(i7).toString());

                            } else if (((Integer) asList.get(i4)).intValue() == 5) {

                                myTextViewListvalues.set(((i7 * 9) + 9),""+randomNumbers.get(i7).toString());
                            } else {
                                myTextViewListvalues.set(((i7 * 9)),""+randomNumbers.get(i7).toString());
                            }
                        }
                        else if (((Integer) asList.get(i4)).intValue() == 4) {
                            myTextViewListvalues.set(((i7 * 9) + i4 + 18),""+randomNumbers.get(i7).toString());

                        } else if (((Integer) asList.get(i4)).intValue() == 5) {

                            myTextViewListvalues.set(((i7 * 9) + i4 + 9),""+randomNumbers.get(i7).toString());
                        } else {
                            myTextViewListvalues.set(((i7 * 9) + i4 ),""+randomNumbers.get(i7).toString());

                        }

                    }
                    i4++;

                }


                String listString1="";
                for (String s : myTextViewListvalues)
                {

                    listString1 += s + ",";
                }

                listString1 = listString1.substring(0, listString1.length() - 1);

                listString=listString+"Ticket"+(i+1)+":"+listString1+" ";



            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {

                String encrypted = "";
                encrypted = AESUtils.encrypt(listString);

                Startgame(gameId,encrypted);

                //  Log.d("TEST", "encrypted:" + encrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }




        }
    }

    public ArrayList<Integer> getRandomNumbers(Integer num, Integer num2, Integer num3) {
        if (num3.intValue() == 4) {
            num3 = Integer.valueOf(1);
        }
        if (num3.intValue() == 5) {
            num3 = Integer.valueOf(2);
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        for (int intValue = num.intValue(); intValue <= num2.intValue(); intValue++) {
            arrayList.add(new Integer(intValue));
        }
        Collections.shuffle(arrayList);
        for (int i = 0; i < num3.intValue(); i++) {
            arrayList2.add((Integer) arrayList.get(i));
        }
        Collections.sort(arrayList2);
        return arrayList2;
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }


    private boolean isgamestored(String gameid, ArrayList<Store_GameResponse> store_gameResponses) {

        for(Store_GameResponse gameResponse: store_gameResponses)
        {
            if(gameResponse.getStore_gameResponse().getTicketPurchased().getGame().getId().equals(gameid))
            {

                store_startgameresponse=gameResponse.getStore_gameResponse();
                store_encrypted=gameResponse.getEncrypttickets();

                return true;
            }

        }


        return false;
    }



    public void saveNotification(String gameStartTime, String gameType) {



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            calendar.setTime(format.parse(gameStartTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Set calendar before 10 minutes
        calendar.add(Calendar.MINUTE, Constants.Gamebefore10min);


        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        System.out.println("rrrrrrrrrr DATEANDTIME 222 "+ DateAndTimeUtil.toStringDateAndTime(calendar)+"current date "+DateAndTimeUtil.toStringDateAndTime(calendar1));

        if (DateAndTimeUtil.toLongDateAndTime(calendar) < DateAndTimeUtil.toLongDateAndTime(calendar1)) {

            addcalender(calendar,1,gameType);
        }
        else {
            addcalender(calendar,10, gameType);
        }

        //Set calendar before 5 minutes
        calendar.add(Calendar.MINUTE, Constants.Gamebefore5min);

        if (DateAndTimeUtil.toLongDateAndTime(calendar) < DateAndTimeUtil.toLongDateAndTime(calendar1)) {

            addcalender(calendar,1, gameType);
        }
        else {
            DatabaseHelper database = DatabaseHelper.getInstance(context);
            id = database.getLastNotificationId() + 1;
            database.close();

            addcalender(calendar, 5, gameType);

        }

    }



    private void addcalender(Calendar calendar, int minutes, String gameType) {

        String game_titile,game_content;
        game_content="You Purchased a ticket in "+gameType+" Game and its started now, please login and Participate.";
        if(minutes==10)
            game_titile=gameType+" Game Starts in 10 Minutes";
        else if(minutes==5)
            game_titile=gameType+" Game Starts in 5 Minutes";
        else
            game_titile = gameType+" Game Starts Soon";
        System.out.println("rrrrrrrrrr DATEANDTIME 222 game_titile"+ DateAndTimeUtil.toStringDateAndTime(calendar)+" game_titile "+game_titile+" minutes "+minutes);

        DatabaseHelper database = DatabaseHelper.getInstance(context);
        Reminder reminder = new Reminder()
                .setId(id)
                .setTitle(""+game_titile)
                //.setContent("Your Game Will Starts in "+minutes+" Minutes")
                .setContent(""+game_content)
                .setDateAndTime(DateAndTimeUtil.toStringDateAndTime(calendar))
                // .setDateAndTime(DateAndTimeUtil.toStringDateAndTime(calendar))
                .setRepeatType(repeatType)
                .setForeverState(Boolean.toString(false))
                .setNumberToShow(timesToShow)
                .setNumberShown(timesShown)
                .setIcon(icon)
                .setColour(colour)
                .setInterval(interval);

        database.addNotification(reminder);

        database.close();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        calendar.set(Calendar.SECOND, 0);
        AlarmUtil.setAlarm(context, alarmIntent, reminder.getId(), calendar);
    }




    private void ProcessWallet(String ticketprice, boolean iscoin, String ttype, String tsource, String info,
                               final int gameId, final int numberofticketwantstobuy, final String gameStartTime, final String gameType) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", userid);
        jsonObject.addProperty("Amount", ticketprice);
        jsonObject.addProperty("IsCoin", iscoin);
        jsonObject.addProperty("TransactionType", ttype);
        jsonObject.addProperty("TransactionSource", tsource);
        jsonObject.addProperty("Information", info);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.WalletProcess(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                /*if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }*/
                System.out.println("rrrrrrrrrrr response process wallet  "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    BuyTickets(gameId, numberofticketwantstobuy,gameStartTime,gameType);


                }
                else if (response.code()==500) {
                   // Util.showAlert(context, ""+response.message(), false);

                }
                else {

                }

            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void showinfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage("You Don't Have Enough Balance in Wallet. Please Add Money To Proceed");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Snackbar.make(parent_view, "Agree clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        //builder.setNegativeButton(R.string.DISAGREE, null);
        builder.show();
    }

    private void alreadygamestatred() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sorry!!");
        builder.setMessage("Already the Game has been Started. Please buy tickets for another Game.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Snackbar.make(parent_view, "Agree clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        //builder.setNegativeButton(R.string.DISAGREE, null);
        builder.show();
    }


    private void calulategamewiningmoney(final GameList object) {

                    Dialog dialog=new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.game_win_money);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView win_type_heading = dialog.findViewById(R.id.win_type_heading);

                    TextView winner_f5=(TextView) dialog.findViewById(R.id.winamount_f5);
                    TextView winner_l1=(TextView) dialog.findViewById(R.id.winamount_l1);
                    TextView winner_l2=(TextView) dialog.findViewById(R.id.winamount_l2);
                    TextView winner_l3=(TextView) dialog.findViewById(R.id.winamount_l3);
                    TextView winner_fh=(TextView) dialog.findViewById(R.id.winamount_fh);


                    TextView winner_f5_user=(TextView) dialog.findViewById(R.id.winamount_f5_user);
                    TextView winner_l1_user=(TextView) dialog.findViewById(R.id.winamount_l1_user);
                    TextView winner_l2_user=(TextView) dialog.findViewById(R.id.winamount_l2_user);
                    TextView winner_l3_user=(TextView) dialog.findViewById(R.id.winamount_l3_user);
                    TextView winner_fh_user=(TextView) dialog.findViewById(R.id.winamount_fh_user);


                    TextView youearn=(TextView) dialog.findViewById(R.id.youearn);
                    youearn.setVisibility(View.GONE);

                    winner_f5_user.setVisibility(View.GONE);
                    winner_l1_user.setVisibility(View.GONE);
                    winner_l2_user.setVisibility(View.GONE);
                    winner_l3_user.setVisibility(View.GONE);
                    winner_fh_user.setVisibility(View.GONE);

                   // ArrayList<GetWinnerPrice> getWinnerPrices=new ArrayList<>();

                   // getWinnerPrices.addAll(response.body());


                    if(object.getGameType().equals(Constants.gametype))
                    {
                        win_type_heading.setText("Win Amount");



                            try{
                                //System.out.println("rrrrrrrrrr getWinnerPrices.size() "+object..size()+" getWinnerPrices.get(i).getWinningDetails() "+getWinnerPrices.get(i).getWinningDetails());

                                    float totalmoney= Float.parseFloat(""+object.getTotalPrice());
                                    //   float totalmoney=Float.parseFloat("120");
                                    //  float admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
                                    float firstfive_per= Float.parseFloat(object.getFirstFive());
                                    float lineone_per= Float.parseFloat(object.getLineOne());
                                    float linetwo_per= Float.parseFloat(object.getLineTwo());
                                    float linethree_per= Float.parseFloat(object.getLineThree());
                                    float fullhousie_per= Float.parseFloat(object.getFullHousee());


                                    //   float admincharges=totalmoney/100*admincharges_per;
                                    float firstfive=totalmoney/100*firstfive_per;
                                    float lineone=totalmoney/100*lineone_per;
                                    float linetwo=totalmoney/100*linetwo_per;
                                    float linethree=totalmoney/100*linethree_per;
                                    float fullhousie=totalmoney/100*fullhousie_per;

                                    System.out.println("rrrrrrrrrrrr totalmoney "+totalmoney);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive "+firstfive+" lineone "+lineone+" fullhousie "+fullhousie/*+" admincharges "+admincharges*/);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive_per "+firstfive_per+" lineone_per "+lineone_per+" fullhousie_per "+fullhousie_per/*+" admincharges_per "+admincharges_per*/);

                                    winner_f5.setText(" "+roundoffloat.format(firstfive));
                                    winner_l1.setText(" "+roundoffloat.format(lineone));
                                    winner_l2.setText(" "+roundoffloat.format(linetwo));
                                    winner_l3.setText(" "+roundoffloat.format(linethree));
                                    winner_fh.setText(" "+roundoffloat.format(fullhousie));



                            }
                            catch (Exception e)
                            {

                            }

                    }
                    else {

                        win_type_heading.setText("Win Coins");


                            try{

                                    float totalmoney= Float.parseFloat(""+object.getTotalPrice());
                                    //   float totalmoney=Float.parseFloat("120");
                                    //  float admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
                                    float firstfive_per= Float.parseFloat(object.getFirstFive());
                                    float lineone_per= Float.parseFloat(object.getLineOne());
                                    float linetwo_per= Float.parseFloat(object.getLineTwo());
                                    float linethree_per= Float.parseFloat(object.getLineThree());
                                    float fullhousie_per= Float.parseFloat(object.getFullHousee());


                                    System.out.println("rrrrrrrrrrrr totalmoney "+totalmoney);
                                    //   System.out.println("rrrrrrrrrrrrrrrrrr firstfive "+firstfive+" lineone "+lineone+" fullhousie "+fullhousie/*+" admincharges "+admincharges*/);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive_per "+firstfive_per+" lineone_per "+lineone_per+" fullhousie_per "+fullhousie_per/*+" admincharges_per "+admincharges_per*/);

                                    winner_f5.setText(" "+roundoffloat1.format(firstfive_per));
                                    winner_l1.setText(" "+roundoffloat1.format(lineone_per));
                                    winner_l2.setText(" "+roundoffloat1.format(linetwo_per));
                                    winner_l3.setText(" "+roundoffloat1.format(linethree_per));
                                    winner_fh.setText(" "+roundoffloat1.format(fullhousie_per));


                            }
                            catch (Exception e)
                            {

                            }

                    }



                    dialog.show();




            }



    private void GetServerTime(final String gameStartTime, final int position) {

        //final String[] currenttime = new String[1];

        final ApiInterface apiService =
                ApiClient.getClient_serverTIme().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetServerTime();
        call.enqueue(new Callback<ResponseBody>() {



            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    try {

                        String servertime=response.body().string();

                        JSONObject jsonObject = new JSONObject(servertime);

                          String currenttime =jsonObject.getString("datetime");

                                if (dateexceed(gameStartTime,currenttime)) {
                                    positionval = position;

                                    try {
                                        boolean issoted = isgamestored("" + gamelist.get(position).getGameId(), getgameresponse);
                                        if (issoted) {

                                            Intent intent = new Intent(context, MyThambolaActivity.class);
                                            intent.putExtra("startgameresponse", (Serializable) store_startgameresponse);
                                            intent.putExtra("encrypted", store_encrypted);
                                            intent.putExtra("" + Constants.isgamestored, true);
                                            context.startActivity(intent);
                                        } else {
                                            new generatetickets(gamelist.get(position).getNumberOfTicketsPurchasedByUser()
                                                    , gamelist.get(position)).execute();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        new generatetickets(gamelist.get(position).getNumberOfTicketsPurchasedByUser()
                                                , gamelist.get(position)).execute();
                                    }


                                } else {
                                    if(!gamelist.get(position).getConvertedDate().contains("Today"))
                                        Toast.makeText(context, "Game Will Start At " + gamelist.get(position).getConvertedDate() + " " + gamelist.get(position).getConvertedTime(), Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(context, "Game Will Start At " + gamelist.get(position).getConvertedTime() + " " + gamelist.get(position).getConvertedDate() , Toast.LENGTH_SHORT).show();
                                }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {

                    Util.showAlert(Activity, ""+response.message(), false);

                }
                else {

                    Util.showAlert(Activity, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Log.d("response","Getting response from server : "+t);
            }
        });

      //  return currenttime[0];

    }


}