package com.thambola.fungola;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.thambola.fungola.Model.GenerateCheckSum;
import com.thambola.fungola.Model.ProfileDetails;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadAmount_Activity extends AppCompatActivity implements PaytmPaymentTransactionCallback{

    EditText amounttoload,amounttowithdraw,userupiid;
    Button prodeed,prodeed_withdraw;
    private CustomDialog mCustomDialog;
    private String access_token,userid;

    private static final int CALL_TARNASATIN = 1;
    //private TransactionManager transactionManager;
    public DecimalFormat roundoffloat = new DecimalFormat("0.00");
    private String orderId;
    private String type;
    LinearLayout main_addmoney_layout,main_withdraw_layout;
    private String amountBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_amount);
        Tools.setSystemBarColor(this, R.color.theme_color1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
         amounttoload=findViewById(R.id.amounttoload);
        prodeed=findViewById(R.id.prodeed);
        main_addmoney_layout=findViewById(R.id.main_addmoney_layout);
        main_withdraw_layout=findViewById(R.id.main_withdraw_layout);

        amounttowithdraw=findViewById(R.id.amounttowithdraw);
        userupiid=findViewById(R.id.userupiid);
        prodeed_withdraw=findViewById(R.id.prodeed_withdraw);

        access_token = Util.getStringFromSP(LoadAmount_Activity.this, Constants.access_token);
        userid = Util.getStringFromSP(LoadAmount_Activity.this, Constants.UserId);
        mCustomDialog = new CustomDialog(LoadAmount_Activity.this);

        type=getIntent().getStringExtra("type");

        if(type.equals("addmoney"))
        {
            main_addmoney_layout.setVisibility(View.VISIBLE);
            main_withdraw_layout.setVisibility(View.GONE);

            if (ContextCompat.checkSelfPermission(LoadAmount_Activity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoadAmount_Activity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
            }
        }
        else {
            main_addmoney_layout.setVisibility(View.GONE);
            main_withdraw_layout.setVisibility(View.VISIBLE);

        }
        prodeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(Integer.parseInt(amounttoload.getText().toString())>9)
                    {

                        if (ContextCompat.checkSelfPermission(LoadAmount_Activity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LoadAmount_Activity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                        }

                        if (Util.checkInternetConnection(LoadAmount_Activity.this)) {



                            getprofiledetails_charges();
                            //Processpayment();
                            //ProcessWalletToken();
                           // ProcessWallet();

                        } else {
                            Util.showAlert(LoadAmount_Activity.this, getResources().getString(R.string.check_internet_connection), false);
                        }
                    }
                    else {
                        Util.showAlert(LoadAmount_Activity.this, "Minimum 10 Coins Required To Process", false);

                    }
                }
                catch (Exception e)
                {

                }
            }
        });

        prodeed_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    System.out.println("rrrrrrrrr userupiid.getText().toString().trim() "+userupiid.getText().toString().trim());
                    if(!userupiid.getText().toString().trim().isEmpty())
                    {
                        if(Integer.parseInt(amounttowithdraw.getText().toString())>=200)
                        {


                            if (Util.checkInternetConnection(LoadAmount_Activity.this)) {



                                getWalletBalence();


                            } else {
                                Util.showAlert(LoadAmount_Activity.this, getResources().getString(R.string.check_internet_connection), false);
                            }
                        }
                        else {
                            Util.showAlert(LoadAmount_Activity.this, "Minimum 200 Coins Required To Process", false);

                        }
                    }
                    else {
                        Util.showAlert(LoadAmount_Activity.this, "Please Enter UPI ID/ Paytm Number", false);

                    }
                }
                catch (Exception e)
                {

                }
            }
        });


      
    }

    private void getWalletBalence() {

        if (mCustomDialog != null)
            mCustomDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetWalletbalance("Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                System.out.println("rrrrrrrrrrr response code wallet balence  "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    try {

                        String walletresponse=response.body().string();

                        JSONObject jsonObject = new JSONObject(walletresponse);

                        amountBalance=jsonObject.getString("amountBalance");


                        float walwtbalence= Float.parseFloat(amountBalance);
                        float amountwithdraw= Float.parseFloat(amounttowithdraw.getText().toString());

                        if(Float.parseFloat(amounttowithdraw.getText().toString())<= Float.parseFloat(amountBalance))
                        {
                           AmountWithdraw();
                        }
                        else {
                            Util.showAlert(LoadAmount_Activity.this, "You Don't Have Sufficient Balance to Withdraw", false);

                        }
                        //wallet_money.setText("Wallet  "+jsonObject.getString("amountBalance"));
                       // free_coins.setText("Free coins  "+jsonObject.getString("coinBalance"));

                        //gwtwalletransactions();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    Util.showAlert(LoadAmount_Activity.this, ""+response.message(), false);

                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    Util.showAlert(LoadAmount_Activity.this, "Unable to Process Request...", false);
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

    private void AmountWithdraw() {

        if (mCustomDialog != null)
            mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        // jsonObject.addProperty("TxnAmount", amounttoload.getText().toString());
        //  jsonObject.addProperty("TxnAmount",amounttoload.getText().toString()+".00");
        jsonObject.addProperty("requestAmount",""+amounttowithdraw.getText().toString().trim());
        jsonObject.addProperty("upi",""+userupiid.getText().toString().trim());

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.Withdrawprocess(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                if (response.isSuccessful()) {


                    ProcessWallet(""+amounttowithdraw.getText().toString().trim(),"Debit","Money Withdraw","Withdraw Transaction Has Been Initiated Successfully. Funds Will be Credit to your UPI in 2 Working Days.");
                }
                else if (response.code()==500) {

                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    Util.showAlert(LoadAmount_Activity.this, ""+response.message(), false);

                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(LoadAmount_Activity.this, "Unable to Process Request...", false);
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

    private void Processpayment(final Number amount) {

        if (mCustomDialog != null)
            mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TxnAmount",""+amount);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<GenerateCheckSum> call = apiService.GenerateCheckSum(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<GenerateCheckSum>() {

            @Override
            public void onResponse(Call<GenerateCheckSum> call, Response<GenerateCheckSum> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                System.out.println("rrrrrrrrrrrr response "+response);
                if (response.isSuccessful()) {

                    System.out.println("rrrrrrrrrr response.body():: "+response.body());
                    GenerateCheckSum getGenerateCheckSum=response.body();

                    String cashbackurl=getGenerateCheckSum.getPaytmConfig().getCallBackUrl().replace("{orderId}",getGenerateCheckSum.getOrderId());

                    orderId=getGenerateCheckSum.getOrderId().replace("ORD","");


                    HashMap<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("MID", getGenerateCheckSum.getPaytmConfig().getMid());
                    // Key in your staging and production MID available in your dashboard
                    paramMap.put("ORDER_ID", getGenerateCheckSum.getOrderId());
                    paramMap.put( "CUST_ID" ,userid);
                 //   paramMap.put( "MOBILE_NO" , "7777777777");
                //    paramMap.put( "EMAIL" , "username@emailprovider.com");
                    paramMap.put("CHANNEL_ID", "WAP");
                  //  paramMap.put("TXN_AMOUNT", amounttoload.getText().toString()+".00");
                    paramMap.put("TXN_AMOUNT", ""+amount);
              //      paramMap.put("TXN_AMOUNT", amounttoload.getText().toString());
                    paramMap.put("WEBSITE", getGenerateCheckSum.getPaytmConfig().getWebsiteName());
                    // This is the staging value. Production value is available in your dashboard
                    paramMap.put("INDUSTRY_TYPE_ID", getGenerateCheckSum.getPaytmConfig().getIndustryType());
                    // This is the staging value. Production value is available in your dashboard
                    paramMap.put( "CALLBACK_URL", cashbackurl);
                    paramMap.put( "CHECKSUMHASH" , getGenerateCheckSum.getCheckSum());



                    Log.d("rrrtest",":"+paramMap);

                    paytmTransaction(paramMap);


                }
                else if (response.code()==500) {

                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    Util.showAlert(LoadAmount_Activity.this, ""+response.message(), false);

                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(LoadAmount_Activity.this, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<GenerateCheckSum> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void ProcessWallet(String amount, String TransactionType, String Information, final String sucess) {

        if (mCustomDialog != null)
            mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", userid);
        jsonObject.addProperty("Amount", amount);
        jsonObject.addProperty("IsCoin", false);
        jsonObject.addProperty("TransactionType", TransactionType);
        jsonObject.addProperty("TransactionSource", "Bank");
        jsonObject.addProperty("Information", Information);

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

                    Util.showAlerts(LoadAmount_Activity.this, ""+sucess, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                }
                else if (response.code()==500) {
                    Util.showAlert(LoadAmount_Activity.this, ""+response.message(), false);

                }
                else {
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(LoadAmount_Activity.this, "Unable to Process Request...", false);
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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CALL_TARNASATIN && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

       // order
        //unregisterReceiver(transactionManager);
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  SmartLocation.with(MainActivity3.this).location().stop();
      //  SmartLocation.with(MainActivity3.this).geocoding().stop();
        Log.d("MainActivity", "Stopping the location service");

    }

    private void paytmTransaction(HashMap<String, String> map){
     // PaytmPGService service = PaytmPGService.getStagingService();
        PaytmPGService service = PaytmPGService.getProductionService();
        PaytmOrder Order = new PaytmOrder(map);
        service.initialize(Order, null);
        service.startPaymentTransaction(this,true,true,this);
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        System.out.println("rrrrrrrrrr inResponse "+inResponse);
        if(inResponse.getString("STATUS").equals("TXN_SUCCESS"))
        {
            ProcessStatus("Success","Payment Success");
           // ProcessWallet();
        }
        //startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","success"));

    }


    @Override
    public void networkNotAvailable() {

        ProcessStatus("NetworkNotAvailable", "Network Not Available");
        /*Toast.makeText(this, "network not available", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","network"));
*/

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

        ProcessStatus("ClientAuthFailed", ""+inErrorMessage);
       /* Toast.makeText(this, "authentication failed", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","client"));
*/
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        ProcessStatus("Failure", ""+inErrorMessage);

        /*Toast.makeText(this, "ui-error", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","ui"));
*/
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

        ProcessStatus("ErrorLoadingWebPage", ""+inErrorMessage);
       /* Toast.makeText(this, "error loading web page", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","webpage"));
*/

    }

    @Override
    public void onBackPressedCancelTransaction() {

        ProcessStatus("BackPressTransactionCancelled", "Transaction Cancelled OnBackPressed");
       /* Toast.makeText(this, "Transaction Canceld", Toast.LENGTH_SHORT).show();
        finish();*/
        /*Toast.makeText(this, "error  on backpress", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","backpress" +
                ""));*/


    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

        ProcessStatus("TransactionCancelled", ""+inErrorMessage);
       /* Toast.makeText(this, "transaction canecl", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,PaymentResponseActivity.class).putExtra("response","cancel"));
*/

    }


    private void ProcessStatus(final String status, final String payment_message) {

        if (mCustomDialog != null)
            mCustomDialog.show();

        System.out.println("rrrrrrrrrr orderId "+orderId +" OrderStatus "+status);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderStatus",status);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.UpdateOrderStatus(orderId,jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                if (response.isSuccessful()) {

                    if (status.equals("Success"))
                    {

                        ProcessWallet(""+amounttoload.getText().toString().trim(),"Credit","Added to Wallet","Successfully Added money to Wallet");

                       // status.setText("Payment Sucess");
                    }
                    else {

                        Toast.makeText(LoadAmount_Activity.this, ""+payment_message, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else if (response.code()==500) {

                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    Util.showAlert(LoadAmount_Activity.this, ""+response.message(), false);

                }
                else {
                    if (mCustomDialog != null && mCustomDialog.isShowing()) {
                        mCustomDialog.dismiss();
                    }
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(LoadAmount_Activity.this, "Unable to Process Request...", false);
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



    private void getprofiledetails_charges() {

        if (mCustomDialog != null)
            mCustomDialog.show();

        //System.out.println("rrrrrrrrrr enter"+UserId +" access_token "+access_token);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileDetails> call = apiService.GetPROFILE(userid,"Bearer "+access_token);
        call.enqueue(new Callback<ProfileDetails>() {
            @Override
            public void onResponse(Call<ProfileDetails> call, Response<ProfileDetails> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                if (response.isSuccessful()) {

                    ProfileDetails profileDetails = response.body();


                    final Dialog dialog=new Dialog(LoadAmount_Activity.this,android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.payment_charges_details);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                    TextView payment_name=(TextView) dialog.findViewById(R.id.payment_name);
                    TextView payment_email=(TextView) dialog.findViewById(R.id.payment_email);
                    TextView payment_mobile=(TextView) dialog.findViewById(R.id.payment_mobile);
                    TextView payment_amount=(TextView) dialog.findViewById(R.id.payment_amount);
                    TextView payment_charges=(TextView) dialog.findViewById(R.id.payment_charges);
                    final TextView payment_total_amount=(TextView) dialog.findViewById(R.id.payment_total_amount);

                    Button payment_prodeed=dialog.findViewById(R.id.payment_prodeed);


                    payment_name.setText(""+profileDetails.getFirstName());
                    payment_email.setText(""+profileDetails.getEmail());
                    payment_mobile.setText(""+profileDetails.getMobile());
                    payment_amount.setText(""+amounttoload.getText().toString()+".00");

                    float totalmoney= Float.parseFloat(payment_amount.getText().toString().trim());

                    float charges=totalmoney/100* Constants.paytmcharges;

                    payment_charges.setText(""+charges);

                    payment_total_amount.setText(""+(totalmoney+charges));


                    payment_prodeed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            try {
                                Processpayment(roundoffloat.parse(payment_total_amount.getText().toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    });



                    dialog.show();



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
            public void onFailure(Call<ProfileDetails> call, Throwable t) {

                Log.d("response","Getting response from server : "+t);
            }
        });

    }


}
