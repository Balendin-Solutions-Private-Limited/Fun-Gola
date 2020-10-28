package com.thambola.fungola;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.widgets.WheelView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Yogi on 23/07/2016.
 */
public class SpinWinActivity extends AppCompatActivity implements View.OnClickListener {

    private static double currentSpinRec;
    private static float lastX;
    private static float lastY;
    private static double theta;
    private static float width;

    public static boolean is_Spin;

    static {
        lastX = 0.0f;
        lastY = 0.0f;
        theta = 0.0d;
    }

    WheelView wheelView;
    private Button autoSpinButton;
    //private List<String> listItems;
    private String listTitle;

    String[] itemslist=new String[]{"25","SORRY","10","TRY AGAIN","100","SORRY","50","TRY AGAIN"};
    private boolean isspinstop;
    private String access_token,UserId;
    private CustomDialog mCustomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_win);
        Tools.setSystemBarColor(this, R.color.theme_color1);




        wheelView = (WheelView) findViewById(R.id.wheelView);
        autoSpinButton = (Button) findViewById(R.id.autoSpinButton);
     //   autoSpinButton.setOnClickListener(this);

        DisplayMetrics dm=getResources().getDisplayMetrics();
        width = dm.widthPixels;

      //  width = (float) getWindowManager().getDefaultDisplay().getWidth();
        listTitle = "List";
      //  listItems = new ArrayList<>();
      //        for (int i = 0; i < itemslist.length; i++) {
        //            listItems.add("Item " + i);
        //        }

       // wheelView.setItems(listItems);
        wheelView.setItems(itemslist);
        wheelView.setList(listTitle);
        updateWheel(0.0d);
        currentSpinRec = 0.0d;
        access_token = Util.getStringFromSP(SpinWinActivity.this, Constants.access_token);
        UserId = Util.getStringFromSP(SpinWinActivity.this, Constants.UserId);

        mCustomDialog = new CustomDialog(SpinWinActivity.this);

        autoSpinButton.setVisibility(View.INVISIBLE);

        if (Util.checkInternetConnection(SpinWinActivity.this)) {

            getspinAvailable();

        } else {
            Util.showAlert(SpinWinActivity.this, getResources().getString(R.string.check_internet_connection), false);
        }




        autoSpinButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                if(!is_Spin)
                {

                    if(!isspinstop)
                    {
                        autoSpinButton.setEnabled(true);
                        autoSpinButton.setBackgroundResource(R.drawable.wallet_image2);
                        is_Spin=false;
                        autoSpinButton.setText("STOP");
                        isspinstop=true;
                    }
                    else
                    {
                        autoSpinButton.setEnabled(false);
                        autoSpinButton.setBackgroundColor((getResources().getColor(R.color.deep_purple_300)));

                        is_Spin=true;
                        autoSpinButton.setText("SPIN");
                        isspinstop=false;
                    }

                }
                else {
                    autoSpinButton.setEnabled(true);
                    autoSpinButton.setBackgroundResource(R.drawable.wallet_image2);

                }

                wheelView.autoSpin(30.0f * (320.0f / width),isspinstop);
            }
        });
    }


    private void updateWheel(double rotationVal) {
        if (wheelView != null) {
            wheelView.setRotation(wheelView.getRotationNumber() + rotationVal);
            wheelView.invalidate();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.autoSpinButton:

                System.out.println("rrrrrrrrrr autoSpinButton");

                if(!is_Spin)
                {
                    autoSpinButton.setEnabled(true);
                    autoSpinButton.setBackgroundResource(R.drawable.wallet_image2);
                    if(!isspinstop)
                    {
                        is_Spin=false;
                        autoSpinButton.setText("SPIN");
                        isspinstop=true;
                    }
                    else
                    {
                        is_Spin=true;
                        autoSpinButton.setText("STOP");
                        isspinstop=false;
                    }

                }
                else {
                    autoSpinButton.setEnabled(false);
                    autoSpinButton.setBackgroundColor(getResources().getColor(R.color.deep_purple_300));
                }

                wheelView.autoSpin(30.0f * (320.0f / width),isspinstop);
                break;
        }
    }


   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void winnText(String winner) {

        if(winner.equals("TRY AGAIN"))
        {
            autoSpinButton.setText("SPIN");
            autoSpinButton.setEnabled(true);
            autoSpinButton.setBackgroundResource(R.drawable.wallet_image2);
            Toast.makeText(SpinWinActivity.this, "Please "+winner, Toast.LENGTH_SHORT).show();

        }
        else if(winner.equals("SORRY"))
        {

            autoSpinButton.setText("Come Back Tomorrow");
            autoSpinButton.setEnabled(false);
            autoSpinButton.setBackgroundColor(getResources().getColor(R.color.deep_purple_300));

            UpdateOffer(winner);

        }
        else {
            autoSpinButton.setText("Come Back Tomorrow");
            autoSpinButton.setEnabled(false);
            autoSpinButton.setBackgroundColor(getResources().getColor(R.color.deep_purple_300));


            UpdateOffer(winner);
        }


    }

    private void getspinAvailable() {


        if (mCustomDialog != null ) {
            mCustomDialog.show();
        }
        //System.out.println("rrrrrrrrrr enter"+UserId +" access_token "+access_token);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.IsSpinAvailable("Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }




                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    autoSpinButton.setVisibility(View.VISIBLE);
                    try {
                       // System.out.println("rrrrrrrrrrr response response.body().string()  "+response.body().string());

                        if(response.body().string().equals("true"))
                        {
                            autoSpinButton.setText("SPIN");
                            autoSpinButton.setEnabled(true);
                            autoSpinButton.setBackgroundResource(R.drawable.wallet_image2);
                        }
                        else {

                            autoSpinButton.setText("Come Back Tomorrow");
                            autoSpinButton.setEnabled(false);
                            autoSpinButton.setBackgroundColor((getResources().getColor(R.color.deep_purple_300)));

                            Toast.makeText(SpinWinActivity.this, "Come Back Tomorrow to Spin Again", Toast.LENGTH_SHORT).show();

                            //autoSpinButton.setVisibility(View.INVISIBLE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (response.code()==500) {

                    Util.showAlert(SpinWinActivity.this, ""+response.message(), false);

                }

                else {


                     Util.showAlert(SpinWinActivity.this, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("response","Getting response from server : "+t);
            }
        });

    }

    private void UpdateOffer(final String winamount) {


        if (mCustomDialog != null ) {
            mCustomDialog.show();
        }
        //System.out.println("rrrrrrrrrr enter"+UserId +" access_token "+access_token);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.UpdateOffer(""+winamount,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {


                    try {
                        System.out.println("rrrrrrrrrrr response UpdateOffer  "+response.body().string());

                        if(winamount.equals("SORRY"))
                        {
                            System.out.println("rrrrrrrrr enter SORRY");
                            if (mCustomDialog != null && mCustomDialog.isShowing()) {
                                mCustomDialog.dismiss();
                            }
                            Toast.makeText(SpinWinActivity.this, "Sorry You Missed, Try Again Tomorrow", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            System.out.println("rrrrrrrrr enter ProcessWalletqqq "+response.body().string());

                                System.out.println("rrrrrrrrr enter ProcessWallet");
                                ProcessWallet(winamount);

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {

                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    // recyclerView.setVisibility(View.GONE);
                    // tv_bankD.setVisibility(View.VISIBLE);
                    // Util.showAlert((Activity) context, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("response","Getting response from server : "+t);
            }
        });

    }


    private void processMovement(float _x, float _y, float _lastX, float _lastY) {
        int px = this.wheelView.getPX();
        int py = this.wheelView.getPY();
        float nX = _x - ((float) px);
        float nY = ((float) py) - _y;
        theta = calculateAngle(nX, nY) - calculateAngle(_lastX - ((float) px), ((float) py) - _lastY);
        updateWheel(-theta);
    }

    private double calculateAngle(float x, float y) {
        double angle;
        if (x == 0.0f) {
            if (y > 0.0f) {
                angle = 90.0d;
            } else {
                angle = 270.0d;
            }
        } else if (y == 0.0f) {
            if (x > 0.0f) {
                angle = 0.0d;
            } else {
                angle = 180.0d;
            }
        } else if (x == 0.0f && y == 0.0f) {
            return -1.0d;
        } else {
            angle = Math.toDegrees(Math.atan2((double) y, (double) x)) - 90.0d;
        }
        return angle;
    }
    private void ProcessWallet(final String amount) {

        /*if (mCustomDialog != null)
            mCustomDialog.show();*/

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", UserId);
        jsonObject.addProperty("Amount", amount);
        jsonObject.addProperty("IsCoin", true);
        jsonObject.addProperty("TransactionType", "Credit");
        jsonObject.addProperty("TransactionSource", "Free");
        jsonObject.addProperty("Information", "SpinWin");
        /*JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", userid);
        jsonObject.addProperty("Amount", amount);
        jsonObject.addProperty("IsCoin", false);
        jsonObject.addProperty("TransactionType", "Credit");
        jsonObject.addProperty("TransactionSource", "Bank");
        jsonObject.addProperty("Information", "Added to Wallet");*/

        System.out.println("rrrrrrrrrrrr amount "+amount);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.WalletProcess(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                System.out.println("rrrrrrrrrrr response process wallet  "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    Toast.makeText(SpinWinActivity.this, "Congratulations You Won "+amount+" Coins", Toast.LENGTH_SHORT).show();

                    /*Util.showAlerts(SpinWinActivity.this, ""+sucess, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });*/

                }
                else if (response.code()==500) {

                    Util.showAlert(SpinWinActivity.this, ""+response.message(), false);

                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    Util.showAlert(SpinWinActivity.this, "Unable to Process Request...", false);
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



}
