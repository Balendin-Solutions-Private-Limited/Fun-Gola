package com.thambola.fungola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.Model.WalletTransactionHistory;
import com.thambola.fungola.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 4/25/2019.
 */

public class GameWalletHistoryAdapter extends RecyclerView.Adapter<GameWalletHistoryAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<WalletTransactionHistory> gwtwalletransactions;
    private final LayoutInflater mInflater;
    private final SimpleDateFormat format,dateformate,timeformate;


    public GameWalletHistoryAdapter(Context context, ArrayList<WalletTransactionHistory> gwtwalletransactions)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.gwtwalletransactions=gwtwalletransactions;
         format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
         dateformate = new SimpleDateFormat("yyyy-MM-dd");
         timeformate = new SimpleDateFormat("hh:mm:ss aa");

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public GameWalletHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gameswallethistoryitem, parent, false);
        return new GameWalletHistoryAdapter.ViewHolder(view);
    }

    // binds the data_maths to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final GameWalletHistoryAdapter.ViewHolder holder, final int position) {

        
        holder.wallet_amount.setText(gwtwalletransactions.get(position).getAmount());

        holder.wallet_deb_cre.setText(""+gwtwalletransactions.get(position).getTransactionType());


        try {
            Date transactondate = format.parse(gwtwalletransactions.get(position).getAddedOn());

            String convertedbirthdate = dateformate.format(transactondate);

            String convertedTime = timeformate.format(transactondate);

            holder.wallet_date.setText(""+convertedbirthdate+" "+convertedTime);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.wallet_date.setText(""+gwtwalletransactions.get(position).getAddedOn());
            //  System.out.println("rrrrrrrrrrr erroe "+e.getMessage());
        }


        if(gwtwalletransactions.get(position).getTransactionType().equals("Credit"))
        {
            holder.wallet_deb_cre_image.setRotation(0);
            //holder.wallet_deb_cre.setTextColor(context.getResources().getColor(R.color.green_700));
        }
        else
        {
            holder.wallet_deb_cre_image.setRotation(180);
           // holder.wallet_deb_cre.setTextColor(context.getResources().getColor(R.color.yellow_800));
        }



        if(gwtwalletransactions.get(position).getIsCoin()==false
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Game"))
            holder.wallet_type.setText("Game Won");

        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Debit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Game"))
            holder.wallet_type.setText("Coins Withdraw");

        else if(gwtwalletransactions.get(position).getIsCoin()==false
                && gwtwalletransactions.get(position).getTransactionType().equals("Debit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Bank"))
            holder.wallet_type.setText("Money Withdraw");


        else if(gwtwalletransactions.get(position).getIsCoin()==false
                && gwtwalletransactions.get(position).getTransactionType().equals("Debit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Game"))
            holder.wallet_type.setText("Money Withdraw");


        else if(gwtwalletransactions.get(position).getIsCoin()==false
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Bank"))
            holder.wallet_type.setText("Added to Wallet");


        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Game"))
            holder.wallet_type.setText("Earned Free Coins");

        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Free")
                &&gwtwalletransactions.get(position).getInformation().contains("Referral"))
            holder.wallet_type.setText("Earn By Referral");

        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Free")
                &&gwtwalletransactions.get(position).getInformation().contains("registration"))
            holder.wallet_type.setText("Successfull Registration");

        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Free")
                &&gwtwalletransactions.get(position).getInformation().contains("update"))
            holder.wallet_type.setText("Earn By Referral");

        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Free")
                &&gwtwalletransactions.get(position).getInformation().contains("SpinWin"))
            holder.wallet_type.setText("Spin Win");
        else if(gwtwalletransactions.get(position).getIsCoin()==true
                && gwtwalletransactions.get(position).getTransactionType().equals("Credit")
                &&gwtwalletransactions.get(position).getTransactionSource().equals("Free")
                &&gwtwalletransactions.get(position).getInformation().contains("RewardedVideo"))
            holder.wallet_type.setText("Rewarded Video");







    }




    // total number of cells
    @Override
    public int getItemCount() {

        return gwtwalletransactions.size();
    }






    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        
        TextView wallet_date,wallet_type,wallet_amount,wallet_deb_cre;
        ImageView wallet_deb_cre_image;


        ViewHolder(View itemView) {
            super(itemView);

            wallet_date = itemView.findViewById(R.id.wallet_date);
            wallet_type = itemView.findViewById(R.id.wallet_type);
            wallet_amount = itemView.findViewById(R.id.wallet_amount);
            wallet_deb_cre = itemView.findViewById(R.id.wallet_deb_cre);
            wallet_deb_cre_image = itemView.findViewById(R.id.wallet_deb_cre_image);
       
        }




    }







}