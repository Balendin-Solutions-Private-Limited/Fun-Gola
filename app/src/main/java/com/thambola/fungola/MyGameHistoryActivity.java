package com.thambola.fungola;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thambola.fungola.Model.MyGameHistory;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.adapter.GameHistoryAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyGameHistoryActivity extends AppCompatActivity {
    private String access_token;
    private CustomDialog mCustomDialog;
    private RecyclerView gamehistory_recyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_history);
        Tools.setSystemBarColor(this, R.color.theme_color1);
         gamehistory_recyclerview=findViewById(R.id.gamehistory_recyclerview);

        mCustomDialog=new CustomDialog(MyGameHistoryActivity.this);

        access_token = Util.getStringFromSP(MyGameHistoryActivity.this, Constants.access_token);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyGameHistoryActivity.this,1);
        gamehistory_recyclerview.setLayoutManager(gridLayoutManager);

        if (Util.checkInternetConnection(MyGameHistoryActivity.this)) {

            if (mCustomDialog != null)
                mCustomDialog.show();

                getGameHistory();

        } else {
            Util.showAlert(MyGameHistoryActivity.this, getResources().getString(R.string.check_internet_connection), false);
        }
        /*GameHistoryAdapter adapter = new GameHistoryAdapter(MyGameHistory.this, context, allgamelist, gametype, access_token);
        gamehistory_recyclerview.setAdapter(adapter);*/

    }

    private void getGameHistory() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<MyGameHistory>> call = apiService.GetGameHistory("Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<MyGameHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<MyGameHistory>> call, Response<ArrayList<MyGameHistory>> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                System.out.println("rrrrrrrrrrr response code mygamehistory  "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    ArrayList<MyGameHistory> getMyGameHistories=new ArrayList<>();

                    getMyGameHistories.addAll(response.body());

                    System.out.println("rrrrrrrrrr getMyGameHistories "+getMyGameHistories.size());

                    GameHistoryAdapter adapter = new GameHistoryAdapter(MyGameHistoryActivity.this, getMyGameHistories);
                    gamehistory_recyclerview.setAdapter(adapter);



                }
                else if (response.code()==500) {
                    Util.showAlert(MyGameHistoryActivity.this, ""+response.message(), false);

                }
                else {
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(MyGameHistoryActivity.this, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ArrayList<MyGameHistory>> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

}
