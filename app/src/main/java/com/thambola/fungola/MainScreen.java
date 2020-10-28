package com.thambola.fungola;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionManager;

import com.google.gson.JsonObject;
import com.startapp.sdk.adsbase.Ad;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.thambola.fungola.Animation.Rotate;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainScreen extends AppCompatActivity implements Animation.AnimationListener {

    ImageView profile;
    ImageView ic_public,ic_private,ic_free,refresh,info,ic_account,ic_wallet,ic_share,ic_rate;
    //private BottomNavigationView navigation;
    TextView username,wallet_money;/*,free_coins;*/
    private String access_token,UserId;
    private boolean mRotated;
    private LinearLayout rootview;
    private ImageView ic_spinwin,ic_reward_video;
    private SwipeRefreshLayout swipeRefreshLayout;
    
    public static int advalue = 1;
   // private StartAppAd rewardedVideo;
    private CustomDialog mCustomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsceen);
        Tools.setSystemBarColor(this, R.color.theme_color1);


        //bannerdisplay();
        //interstitialAddisplay();

        mCustomDialog = new CustomDialog(MainScreen.this);

        ic_private=findViewById(R.id.ic_private);
        ic_public=findViewById(R.id.ic_public);
        ic_free=findViewById(R.id.ic_free);
        profile=findViewById(R.id.profile);
        username=findViewById(R.id.username);

        ic_spinwin=findViewById(R.id.ic_spinwin);
        ic_reward_video=findViewById(R.id.ic_reward_video);


        wallet_money=findViewById(R.id.wallet_money);
        //free_coins=findViewById(R.id.free_coins);
        refresh=findViewById(R.id.refresh);
        rootview=(LinearLayout) findViewById(R.id.rootview);
        info=findViewById(R.id.info);
        ic_account=findViewById(R.id.ic_account);
        ic_wallet=findViewById(R.id.ic_wallet);
        ic_share=findViewById(R.id.ic_share);
        ic_rate=findViewById(R.id.ic_rate);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);


        final Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation);
        ic_spinwin.startAnimation(aniRotateClk);

        final Animation blink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        ic_reward_video.startAnimation(blink);

        ic_reward_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ic_reward_video.setEnabled(false);
                Toast.makeText(MainScreen.this, "Video is Loading...", Toast.LENGTH_SHORT).show();
                showRewardedVideo();
            }
        });


        username.setText("Hi  "+ Util.getStringFromSP(MainScreen.this,Constants.Username));
        ic_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                code(getString(R.string.Public),"Public");

            }
        });
        ic_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               code(getString(R.string.free),"Free");
            }
        });
        ic_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainScreen.this, "We are Comming Soon..", Toast.LENGTH_SHORT).show();
             //  code(getString(R.string.Private),"Private");
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(rootview, new Rotate());

                mRotated = !mRotated;
                refresh.setRotation(mRotated ? 135 : 0);

                if (Util.checkInternetConnection(MainScreen.this)) {

                    getWalletBalence();

                } else {
                    Util.showAlert(MainScreen.this, getResources().getString(R.string.check_internet_connection), false);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Util.checkInternetConnection(MainScreen.this)) {

                    getWalletBalence();

                } else {
                    Util.showAlert(MainScreen.this, getResources().getString(R.string.check_internet_connection), false);
                }

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showinfoDialog();
            }
        });

        ic_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advalue==Integer.parseInt(getString(R.string.adnumber))) {
                    advalue = 1;
                    //interstitialAddisplay();
                    StartAppAd.showAd(getApplicationContext());
                    Intent intent=new Intent(MainScreen.this,Account_Activity.class);
                    startActivity(intent);

                } else {
                    Intent intent=new Intent(MainScreen.this,Account_Activity.class);
                    startActivity(intent);
                    advalue++;
                }

            }
        });

        ic_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advalue==Integer.parseInt(getString(R.string.adnumber))) {
                    advalue = 1;
                    StartAppAd.showAd(getApplicationContext());

                    Intent intent=new Intent(MainScreen.this,Wallet_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent=new Intent(MainScreen.this,Wallet_Activity.class);
                    startActivity(intent);
                    advalue++;
                }

            }
        });
        ic_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this,Refer_Earn.class);
                startActivity(intent);
            }
        });
        ic_rate.setOnClickListener(new View.OnClickListener() {
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


        ic_spinwin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartAppAd.showAd(getApplicationContext());

                Intent intent=new Intent(MainScreen.this,SpinWinActivity.class);
                startActivity(intent);


            }
        });

        access_token = Util.getStringFromSP(MainScreen.this, Constants.access_token);
        UserId = Util.getStringFromSP(MainScreen.this, Constants.UserId);






    }



    private void code(String string, String gametype) {

        if(advalue==Integer.parseInt(getString(R.string.adnumber))) {
            advalue = 1;
            StartAppAd.showAd(getApplicationContext());

            SelectOptionActivityclass(string,gametype);

        } else {
            SelectOptionActivityclass(string, gametype);
            advalue++;
        }

    }

    private void SelectOptionActivityclass(String string, String gametype) {

        Intent learnactivity=new Intent(getApplicationContext(), SelectOptionActivity.class);
        learnactivity.putExtra("heading",string);
        learnactivity.putExtra("gametype", gametype);
        startActivity(learnactivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    /*private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/


    private void getWalletBalence() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetWalletbalance("Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
           // private JSONObject jsonObject;

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                /*if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }*/

                if (response.isSuccessful()) {

                    try {

                        String walletresponse=response.body().string();

                        JSONObject jsonObject = new JSONObject(walletresponse);

                        wallet_money.setText("Wallet  "+jsonObject.getString("amountBalance")+"    Free coins  "+jsonObject.getString("coinBalance"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {
                    Util.showAlert(MainScreen.this, ""+response.message(), false);

                }
                else {
                    //psLrecycleView.setVisibility(View.GONE);
                    // tv_LoanMR.setVisibility(View.VISIBLE);
                    Util.showAlert(MainScreen.this, "Unable to Process Request...", false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                /*if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }*/
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void showinfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wallet Info");
        builder.setMessage("Free Wallet coins are used to buy tickets only. 100 Free Coins = 5 Wallet Coins");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // Snackbar.make(parent_view, "Agree clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        //builder.setNegativeButton(R.string.DISAGREE, null);
        builder.show();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        code(getString(R.string.Public),"Public");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.checkInternetConnection(MainScreen.this)) {

            getWalletBalence();

        } else {
            Util.showAlert(MainScreen.this, getResources().getString(R.string.check_internet_connection), false);
        }
    }




    /*private void interstitialAddisplay() {
        interstitialAd = new InterstitialAd(getApplicationContext(), getResources().getString(R.string.intertitialaplacementid));
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {

                interstitialAd.show();
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {

            }

           *//* @Override
            public void onInterstitialDisplayed(Ad ad) {


            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed

                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }*//*
        });
        interstitialAd.loadAd();
    }

    private void bannerdisplay() {
        adView = new AdView(getApplicationContext(), getResources().getString(R.string.banneraplacementid), AdSize.BANNER_HEIGHT_50);

        LinearLayout adContainer = findViewById(R.id.banner_container);

        adContainer.addView(adView);

        adView.loadAd();
    }
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
*/

    public void showRewardedVideo() {
        final StartAppAd rewardedVideo = new StartAppAd(this);


        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                rewardedVideo.close();
                ic_reward_video.setEnabled(true);
                ProcessWallet(Constants.rewardedpoints);
              //  Toast.makeText(getApplicationContext(), "Grant the reward to user", Toast.LENGTH_LONG).show();
            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {


            @Override
            public void onReceiveAd(Ad ad) {
                rewardedVideo.showAd();
                ic_reward_video.setEnabled(true);
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                ic_reward_video.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Rewarded video not Loaded. Please Try Again Later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ProcessWallet(final String amount) {

        if (mCustomDialog != null)
            mCustomDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", UserId);
        jsonObject.addProperty("Amount", amount);
        jsonObject.addProperty("IsCoin", true);
        jsonObject.addProperty("TransactionType", "Credit");
        jsonObject.addProperty("TransactionSource", "Free");
        jsonObject.addProperty("Information", "RewardedVideo");
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

                    Toast.makeText(MainScreen.this, "Hey!! Congratulations You Earned "+amount+" Coins", Toast.LENGTH_SHORT).show();

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

                    Util.showAlert(MainScreen.this, ""+response.message(), false);

                }
                else {

                    Util.showAlert(MainScreen.this, "Unable to Process Request...", false);
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
