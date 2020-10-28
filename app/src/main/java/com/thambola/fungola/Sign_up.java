package com.thambola.fungola;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.thambola.fungola.Model.SignupResult;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Sign_up extends Activity {


    private EditText name,mobile,email,password,confirm_password,/*gender,dob,*/referalcode;
    private CustomDialog mCustomDialog;
    private FrameLayout clear;

    public Sign_up() {
        // Required empty public constructor
    }

    String transitionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        setContentView(R.layout.fragment_registration);


        mCustomDialog = new CustomDialog(this);

        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
      //  gender = (EditText) findViewById(R.id.gender);
      //  dob = (EditText) findViewById(R.id.dob);
        referalcode = (EditText) findViewById(R.id.referalcode);

        final Button siginup = (Button) findViewById(R.id.siginup);

        


         /*clear = (FrameLayout) findViewById(R.id.clear);

        //set transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clear.setTransitionName(transitionName);
        }

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //fab.setTransitionName("trans_clear");
                    Login endFragment = new Login();
                    setSharedElementReturnTransition(TransitionInflater.from(
                            Sign_up.this).inflateTransition(R.transition.change_image_trans));
                    setExitTransition(TransitionInflater.from(
                            Sign_up.this).inflateTransition(android.R.transition.fade));

                    endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                            Sign_up.this).inflateTransition(R.transition.change_image_trans));
                    endFragment.setEnterTransition(TransitionInflater.from(
                            Sign_up.this).inflateTransition(android.R.transition.fade));

                   // TransitionName = fab.getTransitionName();


                    Bundle bundle = new Bundle();
                    bundle.putString("TRANS_NAME", transitionName);
                    endFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, endFragment)
                            .addSharedElement(clear, transitionName)
                            .commit();
                }
            }
        });*/

        /*dob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_up.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dob.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
               // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });*/

        siginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // siginup();

                if (!signUpvalidate()) {
                    Toast.makeText(Sign_up.this, "Fill All The Fileds", Toast.LENGTH_SHORT).show();
                } else {
                    if (Util.checkInternetConnection(Sign_up.this)) {
                        siginup();
                    } else {
                        Util.showAlert(Sign_up.this, getResources().getString(R.string.check_internet_connection), false);
                    }
                }
        }
        });

    }

    public boolean signUpvalidate() {

        boolean valid = true;
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Pattern ps = Pattern.compile("^[a-zA-Z ]*$");

        /*if (et_UuserName.getText().toString().isEmpty() || !ps.matcher(et_UuserName.getText().toString()).matches()) {
            et_UuserName.setError("Enter Valid Username");
            valid = false;
        }*/

        if (email.getText().toString().isEmpty() || !emailPattern.matcher(email.getText().toString()).matches()) {
            email.setError("Enter Valid MailId");
            valid = false;
        }

        if (mobile.getText().toString().contains(" ") || mobile.getText().toString().isEmpty() || mobile.getText().toString().length() != 10) {
            mobile.setError("Enter valid Mobile Number");
            valid = false;
        }

        if (name.getText().toString().contains(" ") || name.getText().toString().isEmpty()) {
            name.setError("Enter Username");
            valid = false;
        }




        if (password.getText().toString().contains(" ") || password.getText().toString().isEmpty()) {
            password.setError("Enter valid Password");
            valid = false;
        }
        if (password.getText().toString().trim().length()<7) {
            password.setError("Must Be 8 Characters");
            valid = false;
        }
        if(!confirm_password.getText().toString().trim().equals(password.getText().toString().trim()))
        {
            password.setError("Must Be Same");
            confirm_password.setError("Must Be Same");
            valid = false;
        }
        if (confirm_password.getText().toString().contains(" ") || confirm_password.getText().toString().isEmpty()) {
            confirm_password.setError("Enter valid Confirmpassword");
            valid = false;
        }

        /*if (sp_Ugender.getSelectedItem().toString().contains("Gender") || sp_Ugender.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(SignupActivity.this, "Select the Gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }*/


        return valid;
    }



    private void siginup() {

        if (mCustomDialog != null)
            mCustomDialog.show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", ""+name.getText().toString());
        jsonObject.addProperty("email", ""+email.getText().toString());
        jsonObject.addProperty("password", ""+password.getText().toString());
        jsonObject.addProperty("confirmpassword", ""+confirm_password.getText().toString());
        jsonObject.addProperty("mobile", ""+mobile.getText().toString());
        jsonObject.addProperty("firstName", ""+name.getText().toString());
        jsonObject.addProperty("lastName", "");
        jsonObject.addProperty("ReferralCodeApplied", ""+referalcode.getText().toString());



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<SignupResult> call = apiService.SIGNUP(jsonObject);
        call.enqueue(new Callback<SignupResult>() {
            @Override
            public void onResponse(Call<SignupResult> call, Response<SignupResult> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }

                try{
                    System.out.println("rrrrrrrrrrrrrrr sucess registration "+response+ "ress "+response.isSuccessful()
                            +"response.code()"+response.code()+"response.raw()"+response.raw()+"getDateOfBirth "+response.body().getDateOfBirth());

                }
                catch (Exception e)
                {
                    System.out.println("rrrrrrrrrrrrrrr sucess registration "+response+ "ress "+response.isSuccessful()
                            +"response.code()"+response.code()+"response.raw()"+response.raw());

                }

                if (response.isSuccessful()) {
                    Util.showAlerts(Sign_up.this, "Successfully Registered", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  signIn();
                            Sign_up.this.finish();

                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                //fab.setTransitionName("trans_clear");
                                Login endFragment = new Login();
                                setSharedElementReturnTransition(TransitionInflater.from(
                                        Sign_up.this).inflateTransition(R.transition.change_image_trans));
                                setExitTransition(TransitionInflater.from(
                                        Sign_up.this).inflateTransition(android.R.transition.fade));

                                endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                                        Sign_up.this).inflateTransition(R.transition.change_image_trans));
                                endFragment.setEnterTransition(TransitionInflater.from(
                                        Sign_up.this).inflateTransition(android.R.transition.fade));

                                // TransitionName = fab.getTransitionName();


                                Bundle bundle = new Bundle();
                                bundle.putString("TRANS_NAME", transitionName);
                                endFragment.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, endFragment)
                                        .addSharedElement(clear, transitionName)
                                        .commit();*/
                            //}
                        }
                    });
                } else if (response.code()==500) {
                 //   Util.showAlert(Sign_up.this, "User Already Registered"+, false);
                    Util.showAlert(Sign_up.this, ""+response.message(), false);
                }
                else if (response.code()==400) {
                    String eresponse= null;
                    try {
                        eresponse = response.errorBody().string();

                        JSONObject jsonObject = new JSONObject(eresponse);

                        JSONObject modelState = jsonObject.getJSONObject("modelState");

                        JSONArray error = modelState.getJSONArray("error");

                        Util.showAlert(Sign_up.this, ""+error.get(0), false);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Util.showAlert(Sign_up.this, "Unable to Process Request...", false);
                }


            }

            @Override
            public void onFailure(Call<SignupResult> call, Throwable t) {
                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }
                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }
}
