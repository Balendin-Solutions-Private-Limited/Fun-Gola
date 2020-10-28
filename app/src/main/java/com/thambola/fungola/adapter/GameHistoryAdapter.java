package com.thambola.fungola.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.startapp.sdk.adsbase.StartAppAd;
import com.thambola.fungola.Account_Activity;
import com.thambola.fungola.MainScreen;
import com.thambola.fungola.Model.MyGameHistory;
import com.thambola.fungola.Model.UserWins;
import com.thambola.fungola.R;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.ViewAnimation;
import com.thambola.fungola.View_Results;

import java.util.ArrayList;

import static com.thambola.fungola.MainScreen.advalue;

/**
 * Created by Admin on 4/25/2019.
 */

public class GameHistoryAdapter extends RecyclerView.Adapter<GameHistoryAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<MyGameHistory> getMyGameHistories;
    private final LayoutInflater mInflater;



    public GameHistoryAdapter(Context context, ArrayList<MyGameHistory> getMyGameHistories)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.getMyGameHistories=getMyGameHistories;

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public GameHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gameshistoryitem, parent, false);
        return new GameHistoryAdapter.ViewHolder(view);
    }

    // binds the data_maths to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final GameHistoryAdapter.ViewHolder holder, final int position) {


        holder.gamestatingtime.setText(" Game Start Time "+getMyGameHistories.get(position).getGame().getStartDateTime());

        if(getMyGameHistories.get(position).getGame().getGameType().equals(Constants.gametype)) {

            holder.game_ticketprice.setText("   " + getMyGameHistories.get(position).getGame().getTicketPrice() + "   ");
        }
        else {
            holder.game_ticketprice.setText("   Free   ");

        }
        holder.game_type.setText("   "+getMyGameHistories.get(position).getGame().getGameType()+"   ");


        if(getMyGameHistories.get(position).getUserWins()!=null)
        try{


            if(isListContains("FirstFive",getMyGameHistories.get(position).getUserWins()))
                holder.claim_f5.setVisibility(View.VISIBLE);
            else
                holder.claim_f5.setVisibility(View.GONE);

            if(isListContains("LineOne",getMyGameHistories.get(position).getUserWins()))
                holder.claim_l1.setVisibility(View.VISIBLE);
            else
                holder.claim_l1.setVisibility(View.GONE);

            if(isListContains("LineTwo",getMyGameHistories.get(position).getUserWins()))
                holder.claim_l2.setVisibility(View.VISIBLE);
            else
                holder.claim_l2.setVisibility(View.GONE);

            if(isListContains("LineThree",getMyGameHistories.get(position).getUserWins()))
                holder.claim_l3.setVisibility(View.VISIBLE);
            else
                holder.claim_l3.setVisibility(View.GONE);

            if(isListContains("FullHousee",getMyGameHistories.get(position).getUserWins()))
                holder.claim_fh.setVisibility(View.VISIBLE);
            else
                holder.claim_fh.setVisibility(View.GONE);

            }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        holder.gamedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advalue==Integer.parseInt(context.getResources().getString(R.string.adnumber))) {
                    advalue = 1;
                    //interstitialAddisplay();
                    StartAppAd.showAd(context);

                    Intent intent=new Intent(context, View_Results.class);
                    intent.putExtra("Mygamehistory",getMyGameHistories.get(position));
                    intent.putExtra("historylayout","true");

                    context.startActivity(intent);

                } else {
                    Intent intent=new Intent(context, View_Results.class);
                    intent.putExtra("Mygamehistory",getMyGameHistories.get(position));
                    intent.putExtra("historylayout","true");

                    context.startActivity(intent);
                    advalue++;
                }

                //finish();
            }
        });

        holder.bt_toggle_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(holder.bt_toggle_input, holder);
            }
        });

        holder.bt_hide_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(holder.bt_toggle_input,holder);
            }
        });

    }

   /* private void interstitialAddisplay() {
        interstitialAd = new InterstitialAd(context, context.getResources().getString(R.string.intertitialaplacementid));
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {


            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed

                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });
        interstitialAd.loadAd();
    }*/

    private boolean isListContains(String whichtype, ArrayList<UserWins> getGameWinners) {

        for(UserWins userWins: getGameWinners)
        {
            if(userWins.getClaimType().equals(whichtype))
                return true;

        }


        return false;
    }

    private void toggleSectionInput(View view, final ViewHolder holder) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(holder.lyt_expand_input, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo1(holder.nested_scroll_view, holder.lyt_expand_input);
                }
            });
        } else {
            ViewAnimation.collapse(holder.lyt_expand_input);
        }
    }
    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


    // total number of cells
    @Override
    public int getItemCount() {

        return getMyGameHistories.size();
    }






    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_startgame,textView_particpents,textView_ticketcost,textView_ticketsbuy,
                textView_totalcost,textView_buytickets,gamestatingtime,gamedetails,game_ticketprice,
                game_type;
        ImageView ic_buy_ticket;
        RelativeLayout lineralll;
        CardView relativeLayout;
        private ImageButton bt_toggle_input;
        private Button bt_hide_input;
        private View lyt_expand_input;
        private NestedScrollView nested_scroll_view;
        TextView claim_f5,claim_l1,claim_l2,claim_l3,claim_fh;


        ViewHolder(View itemView) {
            super(itemView);
            textView_startgame = itemView.findViewById(R.id.textView_startgame);
            textView_particpents = itemView.findViewById(R.id.textView_particpents);
            textView_ticketcost = itemView.findViewById(R.id.textView_ticketcost);
            textView_ticketsbuy = itemView.findViewById(R.id.textView_ticketsbuy);
            textView_totalcost = itemView.findViewById(R.id.textView_totalcost);
            textView_buytickets = itemView.findViewById(R.id.textView_buytickets);
            gamestatingtime = itemView.findViewById(R.id.gamestatingtime);
            gamedetails = itemView.findViewById(R.id.gamedetails);
            lineralll = itemView.findViewById(R.id.lineralll);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            ic_buy_ticket = itemView.findViewById(R.id.ic_buy_ticket);
            game_ticketprice = itemView.findViewById(R.id.game_ticketprice);
            game_type = itemView.findViewById(R.id.game_type);

            claim_f5 = itemView.findViewById(R.id.claim_f5);
            claim_l1 = itemView.findViewById(R.id.claim_l1);
            claim_l2 = itemView.findViewById(R.id.claim_l2);
            claim_l3 = itemView.findViewById(R.id.claim_l3);
            claim_fh = itemView.findViewById(R.id.claim_fh);


           // imagevieww = itemView.findViewById(R.id.imageView);

            initComponent();

        }

        private void initComponent() {



            // input section
            bt_toggle_input = (ImageButton) itemView.findViewById(R.id.bt_toggle_input);
            bt_hide_input = (Button) itemView.findViewById(R.id.bt_hide_input);
            lyt_expand_input = (View) itemView.findViewById(R.id.lyt_expand_input);
            lyt_expand_input.setVisibility(View.GONE);





            // nested scrollview
            nested_scroll_view = (NestedScrollView) itemView.findViewById(R.id.nested_scroll_view);
        }



    }







}