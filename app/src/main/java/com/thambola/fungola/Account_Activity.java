package com.thambola.fungola;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.startapp.sdk.adsbase.StartAppAd;
import com.thambola.fungola.Model.ProfileDetails;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Store_DB.SharedPreference;
import com.thambola.fungola.Store_DB.SharedPreference_TapedClaim;
import com.thambola.fungola.Store_DB.SharedPreference_TapedCoins;
import com.thambola.fungola.Store_DB.Store_GameResponse;
import com.thambola.fungola.Store_DB.TicketTapedClaim;
import com.thambola.fungola.Store_DB.TicketTapedNumbers;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thambola.fungola.MainScreen.advalue;


public class Account_Activity extends Activity {

    private static final int PROFILE_REFRESH = 1;
    private LinearLayout profile,logoutlayout,gamehistory,change_password,app_rate,app_share
            ,aboutUs,privacypolicy,checkout_widraw_process,refund_cancellation,terms_condition;
    TextView edit_profile,profile_name;
   // TextView edit_profile,profile_name;
    private CustomDialog mCustomDialog;
    private ProfileDetails profileDetails;
    private String access_token,userid;
    CircularImageView profile_image;
    private EditText et_resetpassword,et_NPasrd,et_CNPasrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_light);
        Tools.setSystemBarColor(this, R.color.theme_color1);


        mCustomDialog = new CustomDialog(Account_Activity.this);
        change_password = findViewById(R.id.change_password);
        logoutlayout = findViewById(R.id.logoutlayout);
        profile_name = findViewById(R.id.profile_name);
        profile = findViewById(R.id.profile);
        profile_image = findViewById(R.id.profile_image);
        gamehistory = findViewById(R.id.gamehistory);
        privacypolicy = findViewById(R.id.privacypolicy);
        app_rate = findViewById(R.id.app_rate);
        app_share = findViewById(R.id.app_share);
       // checkout_widraw_process = findViewById(R.id.checkout_widraw_process);
       // refund_cancellation = findViewById(R.id.refund_cancellation);
        terms_condition = findViewById(R.id.terms_condition);
        aboutUs = findViewById(R.id.aboutUs);
        access_token = Util.getStringFromSP(Account_Activity.this, Constants.access_token);
        userid = Util.getStringFromSP(Account_Activity.this, Constants.UserId);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Account_Activity.this, Update_Profile.class);
                intent.putExtra("profile_details",profileDetails);
                startActivityForResult(intent, PROFILE_REFRESH);
            }
        });
        logoutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutmess();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordDialog();
            }
        });

        gamehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advalue==Integer.parseInt(getString(R.string.adnumber))) {
                    advalue = 1;
                   //interstitialAddisplay();

                    StartAppAd.showAd(getApplicationContext());
                    Intent intent=new Intent(Account_Activity.this, MyGameHistoryActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent=new Intent(Account_Activity.this, MyGameHistoryActivity.class);
                    startActivity(intent);
                    advalue++;
                }

            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdf=new Intent(Account_Activity.this, PDFViewActivity.class);
                pdf.putExtra("value","About_us");
                startActivity(pdf);


            }
        });


        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdf=new Intent(Account_Activity.this, PDFViewActivity.class);
                pdf.putExtra("value","Privacy_Policy");
                startActivity(pdf);


            }
        });

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdf=new Intent(Account_Activity.this, PDFViewActivity.class);
                pdf.putExtra("value","Terms_and_Conditions");
                startActivity(pdf);


            }
        });

        app_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        app_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Account_Activity.this,Refer_Earn.class);
                startActivity(intent);
               /* try {
                    String shareBody = "https://play.google.com/store/apps/details?id=" + getPackageName();

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });


        try{


            if (Util.checkInternetConnection(Account_Activity.this)) {

                getprofiledetails();
            } else {
                Toast.makeText(Account_Activity.this, ""+getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                //Util.showAlert(Update_Profile.Account_Activity.this, getResources().getString(R.string.check_internet_connection), false);
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();

            // System.out.println("rrrrrrrrrrrrrr profileDetails" + profileDetails.getMobile());
        }
       // return view;
    }

    public  void logoutmess(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Account_Activity.this);
        //Set a title
        builder.setTitle(""+getResources().getString(R.string.app_name));
        //Set a message
        builder.setMessage("Are you sure you want to logout your session..?");
        ////Here Set a listener to be called when the positive button of the dialog is pressed.
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
               

                Util.saveBooleanInSP(Account_Activity.this,Constants.islogin, false);
                Util.saveStringInSP(Account_Activity.this, Constants.access_token, "");
                Util.saveStringInSP(Account_Activity.this, Constants.UserId, "");
                Util.saveStringInSP(Account_Activity.this,Constants.IsSpeechon,"On");
                Util.saveStringInSP(Account_Activity.this, Constants.FirstName,"");
                Util.saveStringInSP(Account_Activity.this, Constants.Lastname, "");
                Util.saveStringInSP(Account_Activity.this, Constants.Username,"");

                SharedPreference sharedPreference = new SharedPreference();
                SharedPreference_TapedCoins sharedPreferenceTapedCoins = new SharedPreference_TapedCoins();
                SharedPreference_TapedClaim sharedPreference_tapedClaim = new SharedPreference_TapedClaim();

                ArrayList<Store_GameResponse> savedgames = sharedPreference.getGames(Account_Activity.this);

                try {
                    for(int i=0;i<savedgames.size();i++)
                    {
                        try {
                            //remove taped postitions from sharepreference
                            ArrayList<TicketTapedNumbers> ticketTapedNumbers = sharedPreferenceTapedCoins.getTapedCoins(Account_Activity.this,savedgames.get(i).getStore_gameResponse().getTicketPurchased().getGame().getId());
                            sharedPreferenceTapedCoins.removeTapedGame(Account_Activity.this,ticketTapedNumbers,savedgames.get(i).getStore_gameResponse().getTicketPurchased().getGame().getId());

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        try {
                            //remove taped claims from sharepreference
                            ArrayList<TicketTapedClaim> ticketTapedClaims = sharedPreference_tapedClaim.getTapedClaim(Account_Activity.this,savedgames.get(i).getStore_gameResponse().getTicketPurchased().getGame().getId());
                            sharedPreference_tapedClaim.removeTapedGame(Account_Activity.this,ticketTapedClaims,savedgames.get(i).getStore_gameResponse().getTicketPurchased().getGame().getId());

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                          //  System.out.println("rrrrrrrrrrrrr error TicketTapedClaim "+e.getMessage());
                        }


                        sharedPreference.removeItempos(Account_Activity.this,i);

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                   // System.out.println("rrrrrrrrrrr errorrr "+e.getMessage());
                }


                startActivity(new Intent(Account_Activity.this, LoginActivity.class));
                Account_Activity.this.finish();
            }

        });
        //Here Set a listener to be called when the negative button of the dialog is pressed.
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        //Create the dialog
        AlertDialog alertdialog=builder.create();

        //show the alertdialog
        alertdialog.show();
    }

    private void getprofiledetails() {

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

                    profileDetails = response.body();


                    profile_name.setText(""+profileDetails.getFirstName());



                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(Account_Activity.this).load(profileDetails.getProfilePic())
                            .apply(options).into(profile_image);



                }

                else {
                   // Util.showAlert(Update_Profile.Account_Activity.this, "Unable to Process Request...", false);
                }

            }

            @Override
            public void onFailure(Call<ProfileDetails> call, Throwable t) {
                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_REFRESH) {
            if (resultCode == RESULT_OK) {
                try {

                    if (Util.checkInternetConnection(Account_Activity.this)) {

                        getprofiledetails();
                    } else {
                        Toast.makeText(Account_Activity.this, "" + getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }

    public void  PasswordDialog(){
        final Dialog dialog = new Dialog(Account_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chpassworddialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");
        et_resetpassword  = (EditText) dialog.findViewById(R.id.et_resetpassword);
        et_NPasrd  = (EditText) dialog.findViewById(R.id.et_NPasrd);
        et_CNPasrd = (EditText) dialog.findViewById(R.id.et_CNPasrd);
        Button btn_PCHsubmit = dialog.findViewById(R.id.btn_PCHsubmit);
        ImageView btn_pss_cancel = dialog.findViewById(R.id.btn_pss_cancel);
        dialog.show();

        //  Util.saveBooleanInSP(context, "DIALOG", false);
        // Util.saveBooleanInSP(context, "PASSWORDDIALOG", true);
        btn_PCHsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!samepassword())
                {
                    return;
                }

                if (Util.checkInternetConnection(Account_Activity.this)) {
                    dialog.dismiss();
                    changepassword();
                } else {
                    Util.showAlert(Account_Activity.this, getResources().getString(R.string.check_internet_connection), false);
                }

            }
        });
        btn_pss_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Util.saveBooleanInSP(context, "DIALOG", false);
                //  Util.saveBooleanInSP(context, "PASSWORDDIALOG", false);
                dialog.dismiss();
            }
        });
    }

    private boolean samepassword() {
        boolean result = true;

        if (et_NPasrd.getText().toString().trim().length()<7) {
            et_NPasrd.setError("Must Be 8 Characters");
            result = false;
        }
        if (!(et_NPasrd.getText().toString().equals(et_CNPasrd.getText().toString()))) {
            et_NPasrd.setError("Must Be Same");
            et_CNPasrd.setError("Must Be Same");
            result = false;
        }
        return result;
    }

    private void changepassword() {

        if (mCustomDialog != null)
            mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("oldPassword", ""+et_resetpassword.getText().toString());
        jsonObject.addProperty("newPassword", ""+et_NPasrd.getText().toString());
        jsonObject.addProperty("confirmNewPassword", ""+et_CNPasrd.getText().toString());

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.ChangePassword(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    Util.showAlerts(Account_Activity.this, "Password Changed Successfully", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // signIn();
                            //finish();
                            //PasswordDialog();
                        }
                    });

                }
                else if (response.code()==500) {
                    Util.showAlert(Account_Activity.this, ""+response.message(), false);

                }
                else if (response.code()==406) {
                    Util.showAlert(Account_Activity.this, "Old Password Does Not Match", false);

                }
                else {
                    Util.showAlert(Account_Activity.this, "Unable to Process Request...", false);
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
