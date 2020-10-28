package com.thambola.fungola.Ticketsadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.MyThambolaActivity;
import com.thambola.fungola.R;

import java.util.ArrayList;

public class ItemViewHolder extends RecyclerView.ViewHolder{
   TextView number,number2,number3,tktnumber;
   ImageView game_firstfive,game_line1,game_line2,game_line3,game_fullhousie;

   ImageView game_firstfive_done,game_line1_done,game_line2_done,game_line3_done,game_fullhousie_done;

   RelativeLayout relativeLayoutmain,ticket_mainlayout;

    ArrayList<TextView> myTextViewList = new ArrayList<>();
    public ItemViewHolder(View itemView) {
        super(itemView);

        TableRow tableRow = (TableRow) itemView.findViewById(R.id.tableRow1);
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            if (tableRow.getChildAt(i) instanceof TextView) {
                myTextViewList.add((TextView) tableRow.getChildAt(i));
            }
        }
        TableRow tableRow2 = (TableRow) itemView.findViewById(R.id.tableRow2);
        for (int i2 = 0; i2 < tableRow2.getChildCount(); i2++) {
            if (tableRow2.getChildAt(i2) instanceof TextView) {
                myTextViewList.add((TextView) tableRow2.getChildAt(i2));
            }
        }
        TableRow tableRow3 = (TableRow) itemView.findViewById(R.id.tableRow3);
        for (int i3 = 0; i3 < tableRow3.getChildCount(); i3++) {
            if (tableRow3.getChildAt(i3) instanceof TextView) {
                myTextViewList.add((TextView) tableRow3.getChildAt(i3));
            }
        }

        tktnumber=itemView.findViewById(R.id.tktnumber);
        relativeLayoutmain=itemView.findViewById(R.id.relativeLayoutmain);
        ticket_mainlayout=itemView.findViewById(R.id.ticket_mainlayout);

        game_firstfive_done=itemView.findViewById(R.id.game_firstfive_done);
        game_line1_done=itemView.findViewById(R.id.game_line1_done);
        game_line2_done=itemView.findViewById(R.id.game_line2_done);
        game_line3_done=itemView.findViewById(R.id.game_line3_done);
        game_fullhousie_done=itemView.findViewById(R.id.game_fullhousie_done);


        game_firstfive=itemView.findViewById(R.id.game_firstfive);
        game_line1=itemView.findViewById(R.id.game_line1);
        game_line2=itemView.findViewById(R.id.game_line2);
        game_line3=itemView.findViewById(R.id.game_line3);
        game_fullhousie=itemView.findViewById(R.id.game_fullhousie);

       // DisplayMetrics displayMetrics=
        try {
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(MyThambolaActivity.width,MyThambolaActivity.width/2);
           // ticket_mainlayout.setLayoutParams(layoutParams);
        }
        catch (Exception e)
        {

        }

    }
}
