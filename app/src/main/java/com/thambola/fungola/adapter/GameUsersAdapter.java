package com.thambola.fungola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.Model.GetUsersPerGame;
import com.thambola.fungola.R;

import java.util.ArrayList;

public class GameUsersAdapter extends RecyclerView.Adapter<GameUsersAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<GetUsersPerGame> getUsersPerGames;



    public GameUsersAdapter(Context context, ArrayList<GetUsersPerGame> getUsersPerGames) {
        this.context =context;
        this.getUsersPerGames=getUsersPerGames;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowgameuserlayout, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

       // int i4 = position;

        holder.game_user.setText("    "+getUsersPerGames.get(position).getUserUserName());
        holder.ticket_buy.setText(""+getUsersPerGames.get(position).getNumberOfTickets());





    }
    @Override
    public int getItemCount() {

        return getUsersPerGames.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView game_user,ticket_buy;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);


            game_user = itemView.findViewById(R.id.game_user);
            ticket_buy = itemView.findViewById(R.id.ticket_buy);


        }
    }

}