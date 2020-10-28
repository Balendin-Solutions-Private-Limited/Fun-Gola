package com.thambola.fungola;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.thambola.fungola.Animation.Rotate;
import com.thambola.fungola.Model.WalletTransactionHistory;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.adapter.GameWalletHistoryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallet_Activity extends AppCompatActivity {


    private CustomDialog mCustomDialog;
    // private ProfileDetails profileDetails;
    private String access_token,userid;
    private boolean mRotated;
    // CircularImageView profile_image;
    TextView username,wallet_money,free_coins,wallet_addmoney,wallet_withdraw;
    private LinearLayout rootview,wallet_addmoney_lay,wallet_withdraw_lay;
    ImageView refresh;
    RecyclerView wallet_recyclerview;
    private int WALLET_REFRESH=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Tools.setSystemBarColor(this, R.color.theme_color1);


        //bannerdisplay();
        mCustomDialog = new CustomDialog(Wallet_Activity.this);


        wallet_recyclerview=findViewById(R.id.wallet_recyclerview);
        wallet_money=findViewById(R.id.wallet_money);
        free_coins=findViewById(R.id.free_coins);
        refresh=findViewById(R.id.refresh);
        rootview=findViewById(R.id.rootview);
        wallet_addmoney=findViewById(R.id.wallet_addmoney);
        wallet_addmoney_lay=findViewById(R.id.wallet_addmoney_lay);
        wallet_withdraw=findViewById(R.id.wallet_withdraw);
        wallet_withdraw_lay=findViewById(R.id.wallet_withdraw_lay);

        access_token = Util.getStringFromSP(Wallet_Activity.this, Constants.access_token);
        userid = Util.getStringFromSP(Wallet_Activity.this, Constants.UserId);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(Wallet_Activity.this,1);
        wallet_recyclerview.setLayoutManager(gridLayoutManager);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(rootview, new Rotate());

                mRotated = !mRotated;
                refresh.setRotation(mRotated ? 135 : 0);

                if (Util.checkInternetConnection(Wallet_Activity.this)) {

                    if (mCustomDialog != null)
                        mCustomDialog.show();
                    getWalletBalence();

                } else {
                    Util.showAlert(Wallet_Activity.this, getResources().getString(R.string.check_internet_connection), false);
                }

            }
        });

        wallet_addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Wallet_Activity.this, LoadAmount_Activity.class);
                intent.putExtra("type","addmoney");
                startActivityForResult(intent,WALLET_REFRESH);
            }
        });


        wallet_addmoney_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Wallet_Activity.this, LoadAmount_Activity.class);
                intent.putExtra("type","addmoney");
                startActivityForResult(intent,WALLET_REFRESH);
            }
        });

        wallet_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Wallet_Activity.this, LoadAmount_Activity.class);
                intent.putExtra("type","withdraw");
                startActivityForResult(intent,WALLET_REFRESH);
            }
        });


        wallet_withdraw_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Wallet_Activity.this, LoadAmount_Activity.class);
                intent.putExtra("type","withdraw");
                startActivityForResult(intent,WALLET_REFRESH);
            }
        });


        try{

            if (Util.checkInternetConnection(Wallet_Activity.this)) {

                getWalletBalence();
            } else {
                Toast.makeText(Wallet_Activity.this, ""+getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                //Util.showAlert(Update_Profile.this, getResources().getString(R.string.check_internet_connection), false);
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();

            // System.out.println("rrrrrrrrrrrrrr profileDetails" + profileDetails.getMobile());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WALLET_REFRESH) {
            if (resultCode == RESULT_OK) {
                try {

                    if (Util.checkInternetConnection(Wallet_Activity.this)) {

                        if (mCustomDialog != null)
                            mCustomDialog.show();
                        getWalletBalence();

                    } else {
                        Util.showAlert(Wallet_Activity.this, getResources().getString(R.string.check_internet_connection), false);
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }
    private void getWalletBalence() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetWalletbalance("Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {

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

                        wallet_money.setText("Wallet  "+jsonObject.getString("amountBalance"));
                        free_coins.setText("Free coins  "+jsonObject.getString("coinBalance"));

                        gwtwalletransactions();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {
                    Util.showAlert(Wallet_Activity.this, ""+response.message(), false);

                }
                else {
                    //psLrecyclesetVisibility(GONE);
                    // tv_LoanMR.setVisibility(VISIBLE);
                    Util.showAlert(Wallet_Activity.this, "Unable to Process Request...", false);
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

    private void gwtwalletransactions() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<WalletTransactionHistory>> call = apiService.GetWalletTransactionHistory(userid,"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<WalletTransactionHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<WalletTransactionHistory>> call, Response<ArrayList<WalletTransactionHistory>> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                if (response.isSuccessful()) {

                    ArrayList<WalletTransactionHistory> getWalletTransactionHistories = new ArrayList<>();

                    getWalletTransactionHistories.addAll(response.body());


                    GameWalletHistoryAdapter adapter = new GameWalletHistoryAdapter(Wallet_Activity.this, getWalletTransactionHistories);
                    wallet_recyclerview.setAdapter(adapter);




                }
                else {
                    // recyclersetVisibility(GONE);
                    // tv_bankD.setVisibility(VISIBLE);
                    // Util.showAlert((Activity) context, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ArrayList<WalletTransactionHistory>> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });

    }


}
