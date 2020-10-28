package com.thambola.fungola;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.Utils.ViewAnimation;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

   // private Handler mWaitHandler = new Handler();
    private String currentVersion;
    private String token;
    private String TAG="rrrrrrrrrr";
    private final static int LOADING_DURATION = 4000;
    private Context context;
    private VideoView splash_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Tools.setSystemBarColor(this, R.color.theme_color1);

        splash_animation =  findViewById(R.id.splash_animation);


        StartAppSDK.init(this, getResources().getString(R.string.startapp_id), false);
        StartAppAd.disableSplash();
        //StartAppSDK.setTestAdsEnabled(true);
        /*splash_animation =  findViewById(R.id.splash_animation);
        // winAnimationView.setAlpha(0.5f);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
        splash_animation.setVideoURI(video);
        splash_animation.start();*/


        context=this;

        try {
            //String currentVersion="";
            currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            System.out.println("rrrrrrrr Current Version "+currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Util.checkInternetConnection(SplashActivity.this)) {

            GetVersionCode();
        } else {
            Util.showAlert(SplashActivity.this, getResources().getString(R.string.check_internet_connection), false);
        }

     //   GetVersionCode();
      //  new GetVersionCode().execute();

     //   loadingAndDisplayContent();
       // initComponent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
      //  mWaitHandler.removeCallbacksAndMessages(null);
    }

    private void loadingAndDisplayContent() {
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
        splash_animation.setVideoURI(video);
        splash_animation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initComponent();
            }
        }, LOADING_DURATION+300 );

        /*final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initComponent();
            }
        }, LOADING_DURATION + 400);*/
    }

    private void initComponent() {
        boolean islogin = Util.getBooleanFromSP(SplashActivity.this, Constants.islogin);
        if(islogin)
        {
            Intent i = new Intent(SplashActivity.this, MainScreen.class);

            startActivity(i);
            finish();
        }
        else {
            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);

            startActivity((intent));
            finish();
        }

    }

   private void GetVersionCode() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.GetPlastoreVersion();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                //System.out.println("rrrrrrrrrrr response code GetVersionCode  "+response);
                // if (response.code() == 200) {

                if (response.isSuccessful()) {

                    try {

                        String walletresponse=response.body().string();

                        //    System.out.println("rrrrrrrrrrrrr walletresponse "+walletresponse);

                        JSONObject jsonObject = new JSONObject(walletresponse);

                        String onlineVersion=jsonObject.getString("versionNumber");
                        //    System.out.println("rrrrrrrrrrrrr walletresponse "+onlineVersion);

                        if (onlineVersion != null && !onlineVersion.isEmpty()) {

                            if (onlineVersion.equals(currentVersion)) {

                                loadingAndDisplayContent();
                                // initComponent();

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setTitle("Mandatory Update Available");
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setIcon(R.mipmap.ic_launcher);

                                alertDialog.setMessage("Update is available in Play Store, Please must update your app to continue.");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                        }
                                    }
                                });

                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        GetVersionCode();
                                        //  new GetVersionCode().execute();
                                    }
                                });

                                alertDialog.show();
                            }

                        }
                        else {
                            loadingAndDisplayContent();
                        }

                        System.out.println("rrrrrrrrrr update Current version " + currentVersion + "playstore version " + onlineVersion);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (response.code()==500) {

                    Util.showAlert(SplashActivity.this, ""+response.message(), false);

                }
                else {

                    Util.showAlert(SplashActivity.this, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
              //  Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=com.photo.motioneffects&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (onlineVersion.equals(currentVersion)) {

                    initComponent();

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Mandatory Update Available");
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setIcon(R.mipmap.ic_launcher);

                    alertDialog.setMessage("Update is available in Play Store, Please must update your app to continue.");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            }
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            new GetVersionCode().execute();
                        }
                    });

                    alertDialog.show();
                }

            }
            else {
                loadingAndDisplayContent();
            }

            System.out.println("rrrrrrrrrr update Current version " + currentVersion + "playstore version " + onlineVersion);

        }
    }


}