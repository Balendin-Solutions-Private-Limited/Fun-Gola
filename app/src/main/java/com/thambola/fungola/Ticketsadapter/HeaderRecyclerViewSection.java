package com.thambola.fungola.Ticketsadapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.plattysoft.leonids.ParticleSystem;
import com.thambola.fungola.Model.StartGame;
import com.thambola.fungola.MyThambolaActivity;
import com.thambola.fungola.R;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Store_DB.SharedPreference_TapedClaim;
import com.thambola.fungola.Store_DB.SharedPreference_TapedCoins;
import com.thambola.fungola.Store_DB.TicketTapedClaim;
import com.thambola.fungola.Store_DB.TicketTapedNumbers;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thambola.fungola.MyThambolaActivity.contextactivity;

public class HeaderRecyclerViewSection extends StatelessSection implements FilterableSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private final String access_token;
    private final StartGame startgameresponse;
    private final boolean isgamestored;
    private final List<String> listofrandomnumbers;
    private final int pos;
    private ArrayList<TicketTapedNumbers> tapedcoins;
    private ArrayList<TicketTapedClaim> tapedclaim;
    private SharedPreference_TapedCoins sharedPreferenceTapedCoins;
    private SharedPreference_TapedClaim sharedPreferenceTapedClaim;
    private Context context;
    private String title;
    private List<String> randomnumlist;
    private boolean firstfive;
    private boolean fullhousie;
    //private int firstfivecount;
    private ArrayList<Integer> selectedfivelist=new ArrayList<>();
    private String numberinfo="";
    private int posfirstf5;
    private int ticketnumber;

    public HeaderRecyclerViewSection(Context context, String title, int pos, List<String> list, StartGame startgameresponse, boolean isgamestored, List<String> listofrandomnumbers) {
        super(R.layout.header_layout, R.layout.tickets);
        this.context=context;
        this.title = title;
        this.pos=pos;
        this.randomnumlist = list;
        this.startgameresponse=startgameresponse;
        access_token = Util.getStringFromSP(context, Constants.access_token);

        this.listofrandomnumbers=listofrandomnumbers;
        this.isgamestored=isgamestored;
        sharedPreferenceTapedCoins=new SharedPreference_TapedCoins();
        sharedPreferenceTapedClaim=new SharedPreference_TapedClaim();


        try {
            tapedcoins=sharedPreferenceTapedCoins.getTapedCoins(context,startgameresponse.getTicketPurchased().getGame().getId());
            tapedclaim=sharedPreferenceTapedClaim.getTapedClaim(context,startgameresponse.getTicketPurchased().getGame().getId());
        }
        catch (Exception e)
        {

        }

    }
    @Override
    public int getContentItemsTotal() {

        return 1;
      //  return 1;
    }
    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holderp, int position) {
        final ItemViewHolder holder = (ItemViewHolder)holderp;

        if(pos%2==0)
            holder.relativeLayoutmain.setBackgroundResource(R.drawable.background_ticket);
        else
            holder.relativeLayoutmain.setBackgroundResource(R.drawable.background_ticket1);

      holder.tktnumber.setText(title);

      if(isgamestored)
      {
          try {
              int i4 = 0;
              while (i4 < 9) {

                  if(!randomnumlist.get(i4).equals("0"))
                  {
                      holder.myTextViewList.get(i4).setText("" + randomnumlist.get(i4));
                      int color=containsTapedNumber(""+randomnumlist.get(i4),tapedcoins);
                          ((TextView) holder.myTextViewList.get(i4)).setBackgroundColor(color);


                  }
                  else {
                      holder.myTextViewList.get(i4).setBackgroundResource(R.color.background_numno);
                  }
                  if(!randomnumlist.get(i4+9).equals("0"))
                  {
                      holder.myTextViewList.get(i4+9).setText("" + randomnumlist.get(i4+9));
                      int color=containsTapedNumber(""+randomnumlist.get(i4+9), tapedcoins);
                      ((TextView) holder.myTextViewList.get(i4+9)).setBackgroundColor(color);
                  }
                  else {
                      holder.myTextViewList.get(i4+9).setBackgroundResource(R.color.background_numno);
                  }
                  if(!randomnumlist.get(i4+18).equals("0"))
                  {
                      //   System.out.println("rrrrrrrrrrr i4+18 "+(i4+18));
                      holder.myTextViewList.get(i4+18).setText("" + randomnumlist.get(i4+18));
                      int color=containsTapedNumber(""+randomnumlist.get(i4+18), tapedcoins);
                      ((TextView) holder.myTextViewList.get(i4+18)).setBackgroundColor(color);
                  }
                  else {
                      holder.myTextViewList.get(i4+18).setBackgroundResource(R.color.background_numno);
                  }

                  i4++;

              }


              try {

                  if(containsTapedClaim("0",tapedclaim))
                  {
                      holder.game_firstfive_done.setVisibility(View.VISIBLE);
                      holder.game_firstfive.setImageResource(R.drawable.game_type_done_f5);

                  }
                  if(containsTapedClaim("1",tapedclaim))
                      commonOnClickstored(holder,0,0,9, 5,"LineOne",1);

                  if(containsTapedClaim("2",tapedclaim))
                      commonOnClickstored(holder,0,9,18, 5,"LineTwo",2);

                  if(containsTapedClaim("3",tapedclaim))
                      commonOnClickstored(holder,0,18,27, 5,"LineThree",3);

                  if(containsTapedClaim("4",tapedclaim))
                      commonOnClickstored(holder,0,0,27, 15,"FullHousee",4);


              }
              catch (Exception e)
              {

              }


          }catch (Exception e)
          {
              int i4 = 0;
              while (i4 < 9) {

                  if(!randomnumlist.get(i4).equals("0"))
                  {
                      holder.myTextViewList.get(i4).setText("" + randomnumlist.get(i4));
                    //  holder.myTextViewList.get(i4).setBackgroundResource(R.color.background_numyes);
                     // System.out.println("rrrrrrrrrrr getcolor "+holder.myTextViewList.get(i4).getColo);
                  }
                  else {
                      holder.myTextViewList.get(i4).setBackgroundResource(R.color.background_numno);
                  }
                  if(!randomnumlist.get(i4+9).equals("0"))
                  {
                      holder.myTextViewList.get(i4+9).setText("" + randomnumlist.get(i4+9));
                   //   holder.myTextViewList.get(i4+9).setBackgroundResource(R.color.background_numyes);
                  }
                  else {
                      holder.myTextViewList.get(i4+9).setBackgroundResource(R.color.background_numno);
                  }
                  if(!randomnumlist.get(i4+18).equals("0"))
                  {
                      holder.myTextViewList.get(i4+18).setText("" + randomnumlist.get(i4+18));
                   //   holder.myTextViewList.get(i4+18).setBackgroundResource(R.color.background_numyes);
                  }
                  else {
                      holder.myTextViewList.get(i4+18).setBackgroundResource(R.color.background_numno);
                  }


                  i4++;


              }
          }

      }
      else {

       //   System.out.println("rrrrrrrrrr enterrrr");
          int i4 = 0;
          while (i4 < 9) {

              if(!randomnumlist.get(i4).equals("0"))
              {
                  holder.myTextViewList.get(i4).setText("" + randomnumlist.get(i4));
            //      holder.myTextViewList.get(i4).setBackgroundResource(R.color.background_numyes);
              }
              else {
                  holder.myTextViewList.get(i4).setBackgroundResource(R.color.background_numno);
              }
              if(!randomnumlist.get(i4+9).equals("0"))
              {
                  holder.myTextViewList.get(i4+9).setText("" + randomnumlist.get(i4+9));
            //      holder.myTextViewList.get(i4+9).setBackgroundResource(R.color.background_numyes);
              }
              else {
                  holder.myTextViewList.get(i4+9).setBackgroundResource(R.color.background_numno);
              }
              if(!randomnumlist.get(i4+18).equals("0"))
              {
                  holder.myTextViewList.get(i4+18).setText("" + randomnumlist.get(i4+18));
            //      holder.myTextViewList.get(i4+18).setBackgroundResource(R.color.background_numyes);
              }
              else {
                  holder.myTextViewList.get(i4+18).setBackgroundResource(R.color.background_numno);
              }

              i4++;


          }
      }



        commonOnClick(holder);

        holder.game_firstfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ClaimGame(startgameresponse.getGameUserId(),"4,6,7,7,7,78","FirstFive", holder, 0);
                //ClaimGame(startgameresponse.getGameUserId(),"4,6,7,7,7,78","FirstFive");

                commonOnClick(holder,0,0,27,5,"FirstFive",0);

            }
        });
        holder.game_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ClaimGame(startgameresponse.getGameUserId(),"4,6,7,7,7,78","LineOne", holder, 1);
                commonOnClick(holder,0,0,9, 5,"LineOne",1);
            }
        });
        holder.game_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ClaimGame(startgameresponse.getGameUserId(),"4,6,7,7,7,78","LineTwo", holder, 2);
                commonOnClick(holder,0,9,18, 5,"LineTwo",2);

            }
        });
        holder.game_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ClaimGame(startgameresponse.getGameUserId(),"4,6,7,7,7,78","LineThree", holder, 3);
                commonOnClick(holder,0,18,27, 5,"LineThree",3);

            }
        });
        holder.game_fullhousie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ((MyThambolaActivity)context).CloseGame();
              //  ClaimGame(startgameresponse.getGameUserId(),"4,6,7,7,7,78","FullHousee", holder, 4);
                commonOnClick(holder,0,0,27, 15,"FullHousee",4);

            }
        });
    }



    private int containsTapedNumber(String testString, ArrayList<TicketTapedNumbers> ticketTapedNumbers)
    {
        try {
            if(containsTapedClaim("0",tapedclaim))
            {

                //System.out.println("rrrrrrrrrrrr enter posfirstf5 "+posfirstf5);
                for(int i=0;i<ticketTapedNumbers.size();i++)
                {
                   // System.out.println("rrrrrrrrrrrr enter pos i "+i+"title "+title);
                    if(ticketTapedNumbers.get(i).getGameid().equals(""+startgameresponse.getTicketPurchased().getGame().getId()))
                        if(ticketTapedNumbers.get(i).getTicketnumber().equals(""+title))
                            if(ticketTapedNumbers.get(i).getTapednumber().equals(testString))
                            {
                              // System.out.println("rrrrrrrrrrrr tapedclaim.get(1).getTapednumberslist() "+tapedclaim.get(ticketnumber).getTapednumberslist()+" testString "+testString);
                                if(tapedclaim.get(ticketnumber).getTapednumberslist().contains(testString))
                                    return context.getResources().getColor(R.color.gametapcolorclaim);
                                   // return Color.RED;

                                else
                                    return context.getResources().getColor(R.color.gametapcolor);
                                  //  return -16711936;
                            }
                }
            }
            else {
                for(TicketTapedNumbers ticketTapedNumbers1:ticketTapedNumbers)
                {
                    if(ticketTapedNumbers1.getGameid().equals(""+startgameresponse.getTicketPurchased().getGame().getId()))
                        if(ticketTapedNumbers1.getTicketnumber().equals(""+title))
                            if(ticketTapedNumbers1.getTapednumber().equals(testString))
                                return context.getResources().getColor(R.color.gametapcolor);
                                //return -16711936;
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("rrrrrrrrrrrrrr error taped rama "+e.getMessage());
            for(TicketTapedNumbers ticketTapedNumbers1:ticketTapedNumbers)
            {
                if(ticketTapedNumbers1.getGameid().equals(""+startgameresponse.getTicketPurchased().getGame().getId()))
                    if(ticketTapedNumbers1.getTicketnumber().equals(""+title))
                        if(ticketTapedNumbers1.getTapednumber().equals(testString))
                            return context.getResources().getColor(R.color.gametapcolor);
                           // return -16711936;
            }
        }
        return context.getResources().getColor(R.color.background_numyes);
    }

    private boolean containsTapedClaim(String testString, ArrayList<TicketTapedClaim> ticketTapedNumbers)
        {
                 for(int i=0;i<ticketTapedNumbers.size();i++)
                {
                    if(ticketTapedNumbers.get(i).getGameid().equals(""+startgameresponse.getTicketPurchased().getGame().getId()))
                        if(ticketTapedNumbers.get(i).getTicketnumber().equals(""+title))
                            if(ticketTapedNumbers.get(i).getTapedclaim().equals(testString))
                            {
                                this.ticketnumber=i;
                                return true;
                            }
                }


            return false;
        }



    public void commonOnClickstored(final ItemViewHolder holder, int firstfivecount, int startvalue, int endvalue, int numbercount, String whichtype, int whichvalue) {

       //  firstfivecount=firstfivecount1;
        selectedfivelist.clear();
        numberinfo="";
        for (int i = startvalue; i < endvalue; i++) {


                if (!((TextView) holder.myTextViewList.get(i)).getText().toString().isEmpty()) {
                    final int finalI = i;

                    if (((ColorDrawable) ((TextView) holder.myTextViewList.get(finalI)).getBackground()).getColor() == context.getResources().getColor(R.color.gametapcolor)
                            || ((ColorDrawable) ((TextView) holder.myTextViewList.get(finalI)).getBackground()).getColor() == context.getResources().getColor(R.color.gametapcolorclaim) )
                    {
                            selectedfivelist.add(finalI);
                            firstfivecount=firstfivecount+1;

                            numberinfo=numberinfo+""+((TextView) holder.myTextViewList.get(i)).getText().toString()+",";
                            System.out.println("rrrrrrrrrrrr numberinfo "+numberinfo);
                          //  System.out.println("rrrrrrrrrrrrrrrr fullhousie"+ holder.myTextViewList.size()+" i::: "+i+" firstfivecount"+ firstfivecount);
                        }

                }
            }

            if(firstfivecount == numbercount)
            {
             //   ClaimGame(startgameresponse.getGameUserId(),numberinfo,whichtype,holder,whichvalue);

                if(whichvalue==0)
                {
                    holder.game_firstfive_done.setVisibility(View.VISIBLE);

                    holder.game_firstfive.setImageResource(R.drawable.game_type_done_f5);
                }
                else if(whichvalue==1)
                {
                    holder.game_line1_done.setVisibility(View.VISIBLE);
                    holder.game_line1.setImageResource(R.drawable.game_type_done_l1);
                }
                else if(whichvalue==2)
                {
                    holder.game_line2_done.setVisibility(View.VISIBLE);
                    holder.game_line2.setImageResource(R.drawable.game_type_done_l2);
                }
                else if(whichvalue==3)
                {
                    holder.game_line3_done.setVisibility(View.VISIBLE);
                    holder.game_line3.setImageResource(R.drawable.game_type_done_l3);
                }
                else if(whichvalue==4)
                {
                    holder.game_fullhousie_done.setVisibility(View.VISIBLE);
                    holder.game_fullhousie.setImageResource(R.drawable.game_type_done_fh);
                    ((MyThambolaActivity)context).CloseGame();
                }

                for(int i=0;i<selectedfivelist.size();i++) {
                    ((TextView) holder.myTextViewList.get(selectedfivelist.get(i))).setBackgroundColor(context.getResources().getColor(R.color.gametapcolorclaim));
                }
            }


    }


    public void commonOnClick(final ItemViewHolder holder, int firstfivecount, int startvalue, int endvalue, int numbercount, String whichtype, int whichvalue) {


       //  firstfivecount=firstfivecount1;
        selectedfivelist.clear();
        numberinfo="";
        for (int i = startvalue; i < endvalue; i++) {

                if (!((TextView) holder.myTextViewList.get(i)).getText().toString().isEmpty()) {
                    final int finalI = i;
                            if (( (ColorDrawable)((TextView) holder.myTextViewList.get(finalI)).getBackground()).getColor() == context.getResources().getColor(R.color.gametapcolor)
                                    || ((ColorDrawable) ((TextView) holder.myTextViewList.get(finalI)).getBackground()).getColor() == context.getResources().getColor(R.color.gametapcolorclaim) )
                            {
                                selectedfivelist.add(finalI);
                                firstfivecount=firstfivecount+1;

                                numberinfo=numberinfo+""+((TextView) holder.myTextViewList.get(i)).getText().toString()+",";
                                System.out.println("rrrrrrrrrrrr numberinfo "+numberinfo);
                            }
                }
            }

            if(firstfivecount == numbercount)
            {
                ClaimGame(startgameresponse.getGameUserId(),numberinfo,whichtype,holder,whichvalue);
            }


    }



    public void commonOnClick(final ItemViewHolder holder) {
        for (int i = 0; i < holder.myTextViewList.size(); i++) {
            if (!((TextView) holder.myTextViewList.get(i)).getText().toString().isEmpty()) {
                final int finalI = i;
                ((TextView) holder.myTextViewList.get(i)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (((ColorDrawable) ((TextView) holder.myTextViewList.get(finalI)).getBackground()).getColor() == context.getResources().getColor(R.color.background_numyes)) {
                            try {
                                    if(containsMissedCoins(((TextView) holder.myTextViewList.get(finalI)).getText().toString(),listofrandomnumbers.subList(0,(Constants.Presentnumberpos+1))))
                                    {

                                ((TextView) holder.myTextViewList.get(finalI)).setBackgroundColor(context.getResources().getColor(R.color.gametapcolor));
                                sharedPreferenceTapedCoins.addTapedCoins(context, new TicketTapedNumbers(startgameresponse.getTicketPurchased().getGame().getId(), "" + title, "" + ((TextView) holder.myTextViewList.get(finalI)).getText().toString()), startgameresponse.getTicketPurchased().getGame().getId());
                                tapedcoins = sharedPreferenceTapedCoins.getTapedCoins(context, startgameresponse.getTicketPurchased().getGame().getId());
                            }

                            }catch (Exception e)
                            {

                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        }
    }

    private boolean containsMissedCoins(String presentcoin, List<String> list) {

        return list.contains(presentcoin);
    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder)holder;
        hHolder.headerTitle.setText(title);
    }



    @Override
    public void firstfive(boolean query) {
        this.firstfive=query;
    }



    private void ClaimGame(String gameId, final String numberinfo, final String claimtype, final ItemViewHolder holder, final int whichvalue) {

        //if (mCustomDialog != null)
        //    mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("GameUserId", gameId);
        jsonObject.addProperty("NumberInfo", numberinfo);
        jsonObject.addProperty("ClaimType", claimtype);


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.ClaimGame(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    //   buyTicket=response.body();

                    Toast.makeText(context, "HEY!! You Have Claimed "+claimtype, Toast.LENGTH_SHORT).show();

                    sharedPreferenceTapedClaim.addTapedCoins(context,new TicketTapedClaim(startgameresponse.getTicketPurchased().getGame().getId(),""+title,""+whichvalue, numberinfo),startgameresponse.getTicketPurchased().getGame().getId());

                    tapedclaim=sharedPreferenceTapedClaim.getTapedClaim(context,startgameresponse.getTicketPurchased().getGame().getId());

                    if(whichvalue==0)
                    {
                        holder.game_firstfive_done.setVisibility(View.VISIBLE);
                        holder.game_firstfive.setImageResource(R.drawable.game_type_done_f5);
                        //set animation
                        doAnimation(holder.game_firstfive);
                    }
                    else if(whichvalue==1)
                    {
                        holder.game_line1_done.setVisibility(View.VISIBLE);
                        holder.game_line1.setImageResource(R.drawable.game_type_done_l1);

                        //set animation
                        doAnimation(holder.game_line1);
                    }
                    else if(whichvalue==2)
                    {
                        holder.game_line2_done.setVisibility(View.VISIBLE);
                        holder.game_line2.setImageResource(R.drawable.game_type_done_l2);

                        //set animation
                        doAnimation(holder.game_line2);
                    }
                    else if(whichvalue==3)
                    {
                        holder.game_line3_done.setVisibility(View.VISIBLE);
                        holder.game_line3.setImageResource(R.drawable.game_type_done_l3);

                        //set animation
                        doAnimation(holder.game_line3);
                    }
                    else if(whichvalue==4)
                    {
                        holder.game_fullhousie_done.setVisibility(View.VISIBLE);
                        holder.game_fullhousie.setImageResource(R.drawable.game_type_done_fh);

                        //set animation

                        ((MyThambolaActivity)context).CloseGame();
                    }

                    for(int i=0;i<selectedfivelist.size();i++) {
                        ((TextView) holder.myTextViewList.get(selectedfivelist.get(i))).setBackgroundColor(context.getResources().getColor(R.color.gametapcolorclaim));

                    }


                }
                else {

                    if (response.code() == 400) {

                       // Toast.makeText(context, "Another user has already claimed"+claimtype, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "This has already been claimed", Toast.LENGTH_SHORT).show();


                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               // if (mCustomDialog != null && mCustomDialog.isShowing()) {
               //     mCustomDialog.dismiss();
               // }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    public void doAnimation(View arg0){
        ParticleSystem ps = new ParticleSystem(contextactivity, 100, R.drawable.yellow_spark, 800);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(arg0, 70);

        ParticleSystem ps2 = new ParticleSystem(contextactivity, 100, R.drawable.red_spark, 800);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(arg0, 70);
    }



}