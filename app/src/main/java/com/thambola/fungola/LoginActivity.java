package com.thambola.fungola;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thambola.fungola.Model.LoginError;
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


public class LoginActivity extends AppCompatActivity {

    private CustomDialog mCustomDialog;
    private String token;
    private EditText userlogin,password;
    private String access_token,UserId,Firstname,Lasrname,Username,ReferralCodeToBeShared;
    EditText et_NPasrd,et_resetpassword,et_CNPasrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Tools.setSystemBarColor(this, R.color.theme_color1);


        mCustomDialog = new CustomDialog(this);

        userlogin = (EditText) findViewById(R.id.userlogin);
        password = (EditText) findViewById(R.id.password);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                          //  Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        token = task.getResult().getToken();

                        System.out.println("rrrrrrrrrrrrrrrrr genreae token "+token);
                    }
                });
       ((View) findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               login();
            }
        });


        ((View) findViewById(R.id.signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(LoginActivity.this,Sign_up.class);
               startActivity(intent);
            }
        });




        ((View) findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkInternetConnection(LoginActivity.this)) {

                    EmailDialog();

                    //new ForgotPasswordSyncTask().execute();
                } else {
                    Util.showAlert(LoginActivity.this, getResources().getString(R.string.check_internet_connection), false);
                }
            }
        });

        //Tools.setSystemBarColor(this);
    }

    private void login() {

        if (mCustomDialog != null)
            mCustomDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        System.out.println("rrrrrrrrrrrr password.getText().toString(): "+password.getText().toString().trim());
        Call<ResponseBody> call = apiService.SIGNIN(userlogin.getText().toString(), password.getText().toString().trim(),"password");
        //  Call<MoviesResponse> call = apiService.lo(API_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //List<Movie> movies = response.body().getResults();
                // Log.d(TAG, "Number of movies received: " + movies.size());

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                System.out.println("rrrrrrrrrrrr response login "+response);



                if (response.isSuccessful()) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());

                            if (jsonObject.has("access_token")) {
                                Util.saveStringInSP(LoginActivity.this, Constants.access_token, jsonObject.getString("access_token"));
                                Util.saveStringInSP(LoginActivity.this, Constants.UserId, jsonObject.getString("UserId"));
                                Util.saveBooleanInSP(LoginActivity.this, Constants.islogin, true);
                                Util.saveStringInSP(LoginActivity.this, Constants.FirstName, jsonObject.getString("Firstname"));
                                Util.saveStringInSP(LoginActivity.this, Constants.Lastname, jsonObject.getString("Lastname"));
                                Util.saveStringInSP(LoginActivity.this, Constants.Username, jsonObject.getString("Username"));
                                Util.saveStringInSP(LoginActivity.this, Constants.ReferralCodeToBeShared, jsonObject.getString("ReferralCodeToBeShared"));


                                access_token=jsonObject.getString("access_token");
                                UserId=jsonObject.getString("UserId");
                                Firstname=jsonObject.getString("Firstname");
                                Lasrname=jsonObject.getString("Lastname");
                                Username=jsonObject.getString("Username");
                                ReferralCodeToBeShared=jsonObject.getString("ReferralCodeToBeShared");

                                /*Intent i = new Intent(LoginActivity.this, MainScreen.class);
                                startActivity(i);*/
                                updatetoken();

                            }

                        }catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                        //  Toast.makeText(LoginActivity.this, "WALLET", Toast.LENGTH_LONG).show();
                        //ToDo we can handle here
                        //DO AS PER YOUR REQUIREMENT

                } else if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        LoginError mError = new LoginError();
                        try {
                            mError = gson.fromJson(response.errorBody().string(), LoginError.class);
                            Util.showAlert(LoginActivity.this, ""+mError.getError_description(), false);

                                //ToDo we can handle here

                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
                else if (response.code()==500) {
                    Util.showAlert(LoginActivity.this, ""+response.message(), false);

                }
                else {
                    //psLrecyclesetVisibility(GONE);
                    // tv_LoanMR.setVisibility(VISIBLE);
                    Util.showAlert(LoginActivity.this, "Unable to Process Request...", false);
                }



            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
            }


        });
    }

    private void updatetoken() {

       /* if (mCustomDialog != null)
            mCustomDialog.show();*/

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("FireBaseToken", ""+token);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.UpdateToken(jsonObject,"Bearer "+access_token );
        //  Call<MoviesResponse> call = apiService.lo(API_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //List<Movie> movies = response.body().getResults();
                // Log.d(TAG, "Number of movies received: " + movies.size());

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                if (response.isSuccessful()) {
                        try {
                            // jsonObject = new JSONObject(response.body().string());

                            Util.saveStringInSP(LoginActivity.this, Constants.access_token, access_token);
                            Util.saveStringInSP(LoginActivity.this, Constants.UserId, "" +UserId );
                            Util.saveStringInSP(LoginActivity.this, Constants.FirstName,Firstname);
                            Util.saveStringInSP(LoginActivity.this, Constants.Lastname, Lasrname);
                            Util.saveStringInSP(LoginActivity.this, Constants.Username,Username);
                            Util.saveStringInSP(LoginActivity.this, Constants.ReferralCodeToBeShared,ReferralCodeToBeShared);

                            // Util.saveStringInSP(LoginActivity.this, "email", "" + et_IuserName.getText().toString());
                            //  Util.saveStringInSP(LoginActivity.this, "mobile", "" + et_IuserName.getText().toString());
                            Util.saveBooleanInSP(LoginActivity.this, Constants.islogin, true);
                            // Util.saveBooleanInSP(LoginActivity.this, "IsLogged", true);

                            Intent i = new Intent(LoginActivity.this, MainScreen.class);
                            startActivity(i);
                            LoginActivity.this.finish();


                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        //  Toast.makeText(LoginActivity.this, "WALLET", Toast.LENGTH_LONG).show();
                        //ToDo we can handle here
                        //DO AS PER YOUR REQUIREMENT

                } else if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        LoginError mError = new LoginError();
                        try {
                            mError = gson.fromJson(response.errorBody().string(), LoginError.class);
                            Util.showAlert(LoginActivity.this, ""+mError.getError_description(), false);

                        } catch (IOException e) {
                            // handle failure to read error
                        }


                }
                else if (response.code()==500) {
                    Util.showAlert(LoginActivity.this, ""+response.message(), false);

                }
                else {
                    //psLrecyclesetVisibility(GONE);
                    // tv_LoanMR.setVisibility(VISIBLE);
                    Util.showAlert(LoginActivity.this, "Unable to Process Request...", false);
                }


            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
            }


        });
    }

    public void  EmailDialog(){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chpassworddialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("Please Enter Your Email id");
        TextView popup_otp = (TextView) dialog.findViewById(R.id.popup_otp);
        et_resetpassword  = (EditText) dialog.findViewById(R.id.et_resetpassword);
        et_NPasrd  = (EditText) dialog.findViewById(R.id.et_NPasrd);
        et_CNPasrd = (EditText) dialog.findViewById(R.id.et_CNPasrd);
        et_resetpassword.setVisibility(View.GONE);
        et_CNPasrd.setVisibility(View.GONE);

        RelativeLayout et_resetpassword_lay=dialog.findViewById(R.id.et_resetpassword_lay);
        et_resetpassword_lay.setVisibility(View.GONE);
        RelativeLayout et_CNPasrd_lay=dialog.findViewById(R.id.et_CNPasrd_lay);
        et_CNPasrd_lay.setVisibility(View.GONE);

        popup_otp.setText("Please Enter Your Email id");
        et_NPasrd.setInputType(InputType.TYPE_CLASS_TEXT);
        et_NPasrd.setHint(" Email id ");
        Button btn_PCHsubmit = dialog.findViewById(R.id.btn_PCHsubmit);
        ImageView btn_pss_cancel = dialog.findViewById(R.id.btn_pss_cancel);
        dialog.show();
        // Util.saveBooleanInSP(context, "DIALOG", false);
        // Util.saveBooleanInSP(context, "PASSWORDDIALOG", true);
        btn_PCHsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!et_NPasrd.getText().toString().isEmpty())
                {
                    dialog.dismiss();
                    forgotpassword();
                }
                else
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email id", Toast.LENGTH_SHORT).show();
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

    private void forgotpassword() {

        if (mCustomDialog != null)
            mCustomDialog.show();



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Email", ""+et_NPasrd.getText().toString());



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.ForgotPassword(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                // System.out.println("rrrrrrrrrrrrrrr apply loan "+response);


                if (response.isSuccessful()) {
                    Util.showAlerts(LoginActivity.this, "New Password is sent to Your Email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // signIn();
                            //finish();
                            //  PasswordDialog();
                        }
                    });

                }
                if (response.code()==404)
                {
                    Util.showAlert(LoginActivity.this, "Invalid Email Id", false);
                }
                else if (response.code()==500) {

                    Util.showAlert(LoginActivity.this, ""+response.message(), false);

                }

                else
                {
                    Util.showAlert(LoginActivity.this, "Unable to Process Request...", false);
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
