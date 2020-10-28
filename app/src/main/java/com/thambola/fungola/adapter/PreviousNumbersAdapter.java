package com.thambola.fungola.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.R;

import java.util.List;

public class PreviousNumbersAdapter extends RecyclerView.Adapter<PreviousNumbersAdapter.MyViewHolder> {

    private final Context context;
    private final List<String> listofrandomnumbers;
    //List asList;
    private int previousnumlist;
    private int presentnumpos;

    public PreviousNumbersAdapter(Context context, int previousnumlist, int presentnumpos, List<String> listofrandomnumbers) {
        this.context =context;

        this.presentnumpos=presentnumpos;
        this.previousnumlist=previousnumlist;
        this.listofrandomnumbers=listofrandomnumbers;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowpreviouslayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

       // int i4 = position;

        holder.number.setText(""+(position+1));


        try {
            if(containsString(holder.number.getText().toString(),listofrandomnumbers.subList(0,presentnumpos)))
            {
                holder.number.setBackgroundResource(R.drawable.game_previous_numb_done);
                holder.number.setTextColor(context.getResources().getColor(R.color.white));
              //  holder.number.setTextColor(Color.RED);
                holder.number.setTypeface(holder.number.getTypeface(), Typeface.BOLD);
                // holder.number.setBackgroundColor(context.getResources().getColor(R.color.grey_200));
            }
            else
            {
                holder.number.setBackgroundResource(R.drawable.game_previous_numb);
                holder.number.setTextColor(context.getResources().getColor(R.color.white));
                holder.number.setTypeface(holder.number.getTypeface(), Typeface.BOLD);
               // holder.number.setTypeface(holder.number.getTypeface(), Typeface.NORMAL);

            }
        }
        catch (Exception e)
        {

        }




    }
    @Override
    public int getItemCount() {

        return previousnumlist;
    }


    public void setnumpostion(int presentnum) {

        this.presentnumpos=presentnum;
        notifyDataSetChanged();
    }

    private boolean containsString(String testString, List<String> list)
    {

        return list.contains(testString);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number,number2,number3;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);

            DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
            int width=displayMetrics.widthPixels;
            number = (TextView) itemView.findViewById(R.id.number);
            RelativeLayout previous_layout = (RelativeLayout) itemView.findViewById(R.id.previous_layout);

           // CardView.LayoutParams layoutParams=new CardView.LayoutParams(width/10,width/10);
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width/10,width/10);
            previous_layout.setLayoutParams(layoutParams);


        }
    }

}