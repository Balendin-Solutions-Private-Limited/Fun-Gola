package com.thambola.fungola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.R;
import com.thambola.fungola.SelectOptionActivity;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private final int ticketscount;
    private final String ticketprice;
    private final Context context;
    private final String gameType;
    private final int numberOfTicketsPurchasedByUser;
    //  private final int numberOfTicketsPurchasedByUser;
    private int selectedpos=-1;

    //private GetData getDatainterface;

    public ImagesAdapter(Context context, int ticketscount, String ticketprice, String gameType, int numberOfTicketsPurchasedByUser) {

        this.ticketscount=ticketscount;
        this.ticketprice=ticketprice;
        this.context=context;
        this.gameType=gameType;
        this.numberOfTicketsPurchasedByUser=numberOfTicketsPurchasedByUser;
        if(numberOfTicketsPurchasedByUser>0)
        this.selectedpos=numberOfTicketsPurchasedByUser-1;


        //getDatainterface=((GetData)context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try{
            if((numberOfTicketsPurchasedByUser-1)>=position)
                holder.image.setEnabled(false);
            else
                holder.image.setEnabled(true);
        }
        catch (Exception e)
        {
            holder.image.setEnabled(true);
        }


        if(selectedpos>=position)
        {
            holder.image.setImageResource(R.drawable.game_tickets_buy);

        }
        else
        {
            holder.image.setImageResource(R.drawable.ic_image_tickets);

        }

        //handling item click event
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(holder.textView.getContext(), label, Toast.LENGTH_SHORT).show();

                selectedpos=position;

                int selectedpos1;
                if(numberOfTicketsPurchasedByUser>0)
                     selectedpos1=((selectedpos+1)-numberOfTicketsPurchasedByUser);
                else
                    selectedpos1=selectedpos+1;

                int ticketbuyedprice= Integer.parseInt(ticketprice) * selectedpos1;

                ((SelectOptionActivity)context).onTicketsClick(ticketbuyedprice,selectedpos1,gameType);


                notifyDataSetChanged();


            }
        });

    }


    @Override
    public int getItemCount() {
        return ticketscount;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
       // public TextView textView;
        ImageView image;
        //Button proceed;

        public ViewHolder(View itemView) {
            super(itemView);
            image =  itemView.findViewById(R.id.image);

        }
    }
}
