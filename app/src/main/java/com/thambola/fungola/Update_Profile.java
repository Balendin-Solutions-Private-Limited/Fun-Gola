package com.thambola.fungola;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.thambola.fungola.CustomViews.MyRadioButton;
import com.thambola.fungola.Model.ProfileDetails;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.CustomDialog;
import com.thambola.fungola.Utils.ImageUtil;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thambola.fungola.Utils.Constants.UserId;

public class Update_Profile extends AppCompatActivity {

    private static final int READ_EXST = 0x4;
    TextView edit_profile,profile_save;
    CircularImageView profile_image;
    EditText profile_username,profile_mailid,profile_mobile,profile_dob,
            profile_gender,profile_referalcode,profile_name;
    private CustomDialog mCustomDialog;
    private String access_token,userid;
    private ProfileDetails profileDetails;
    private String Filepath;
    private MyRadioButton Female,Male;
   // private Bitmap appimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);
        Tools.setSystemBarColor(this, R.color.theme_color1);


        mCustomDialog = new CustomDialog(Update_Profile.this);

        edit_profile=(TextView)findViewById(R.id.edit_profile);
        profile_image=findViewById(R.id.profile_image);
        profile_username=findViewById(R.id.profile_username);
        profile_mailid=findViewById(R.id.profile_mailid);
        profile_mobile=findViewById(R.id.profile_mobile);
        profile_dob=findViewById(R.id.profile_dob);
        profile_gender=findViewById(R.id.profile_gender);
        profile_referalcode=findViewById(R.id.profile_referalcode);
        profile_name=findViewById(R.id.profile_name);
        profile_save=findViewById(R.id.profile_save);

        Female = findViewById(R.id.acrb_female);
        Male = findViewById(R.id.acrb_male);

        profileDetails= (ProfileDetails) getIntent().getSerializableExtra("profile_details");

        access_token = Util.getStringFromSP(Update_Profile.this, Constants.access_token);
        userid = Util.getStringFromSP(Update_Profile.this, Constants.UserId);

        setRadiosListener();

       // appimage=BitmapFactory.decodeResource(getResources(),R.drawable.game_play);
      //  System.out.println("rrrrrrrrrrrrr appimage::::: "+appimage);

        boolean result = askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,3);
        if (result)
            try {
                getprofiledetails();
            }
            catch (Exception e)
            {

            }



        profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.checkInternetConnection(Update_Profile.this)) {

                    updateprofile();
                    //  new ProfileUpdateAsync().execute();
                } else {
                    Util.showAlert(Update_Profile.this, getResources().getString(R.string.check_internet_connection), false);
                }
            }
        });



        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,2);
                if (result)
                    galleryIntent();
            }
        });

      //  profile_dob.setEnabled(false);
        profile_dob.setFocusable(false);
        profile_dob.setClickable(true);
        profile_dob.setCursorVisible(false);
        profile_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Update_Profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                              //  profile_dob.setText(day + "/" + month + "/" + year);
                                profile_dob.setText(""+year+"-"+month+"-"+day);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

    }

    private boolean askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
            return false;
        } else {

            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       // System.out.println("rrrrrrrrrrr requestCode "+requestCode);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {

                case 1:
                    System.out.println("rrrrrrrrrr enter 11111");
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                        // saveImage(myBitmap1);
                        galleryIntent();



                    } else {
                        Toast.makeText(Update_Profile.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    ActivityCompat.requestPermissions(Update_Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    break;

                case 3:
                    System.out.println("rrrrrrrrrr enter 3333");
                    ActivityCompat.requestPermissions(Update_Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
                    break;
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, READ_EXST);
                    break;
                case 5:

                    System.out.println("rrrrrrrrrr enter 5555555");
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        try {
                            getprofiledetails();
                        }
                        catch (Exception e)
                        {

                        }

                    } else {
                        Toast.makeText(Update_Profile.this, "Permission denied. Please alow to continue Edit Profile", Toast.LENGTH_SHORT).show();
                        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,3);
                    }

                    break;

                //Accounts

            }

            // Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied. Please alow to continue Edit Profile", Toast.LENGTH_SHORT).show();

            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,3);
        }

    }

    private void galleryIntent() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, READ_EXST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == READ_EXST)
                ImageUtil.onSelectFromGalleryResult(data,profile_image,getApplicationContext());

        }
    }



    private void getprofiledetails() {

            profile_username.setEnabled(false);
            profile_mobile.setEnabled(false);
            profile_mobile.setText(""+profileDetails.getMobile());
            profile_mailid.setText(""+profileDetails.getEmail());
            profile_username.setText(""+profileDetails.getUserName());
      //  System.out.println("rrrrrrrrrrr rofileDetails.getProfilePic() "+profileDetails.getProfilePic());

            System.out.println("rrrrrrrr profileDetails.getReferralCodeApplied() "+profileDetails.getReferralCodeApplied());
          try {
              if(profileDetails.getReferralCodeApplied().length()>0)
              {
                  profile_referalcode.setEnabled(false);
                  profile_referalcode.setText(""+profileDetails.getReferralCodeApplied());


              }
              else {
                  profile_referalcode.setEnabled(true);
              }
          }
          catch (Exception e)
          {
              e.printStackTrace();

              profile_referalcode.setEnabled(true);
          }

            profile_name.setText(""+profileDetails.getFirstName());

           // profile_gender.setText(""+profileDetails.getGender());


                   try {
                       if(profileDetails.getGender().equals("Female"))
                       {
                           Female.setChecked(true);
                           profile_gender.setText("Female");
                       }
                       else
                       {
                           Male.setChecked(true);
                           profile_gender.setText("Male");
                       }
                   }
                   catch (Exception e)
                   {

                   }

      //  System.out.println("rrrrrrrrrrrrrrrrr profileDetails.getUserName() "+profileDetails.getUserName()+" profileDetails.getFirstName() "+profileDetails.getFirstName());
                   // profile_gender.setText(""+profileDetails.getGender());

                    if(profileDetails.getDateOfBirth()!=null)
                    {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date birthdate = null;
                        try {
                            birthdate = format.parse(profileDetails.getDateOfBirth());

                        } catch (ParseException e) {
                            e.printStackTrace();

                          //  System.out.println("rrrrrrrrrrr erroe "+e.getMessage());
                        }

                        SimpleDateFormat timeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                        String convertedbirthdate = timeFormatter1.format(birthdate);
                        profile_dob.setText(""+convertedbirthdate);
                    }




                    System.out.println("rrrrrrrrrrr rofileDetails.getProfilePic() "+profileDetails.getProfilePic());
                    if(profileDetails.getProfilePic()!=null)
                    {
                        new loadimage().execute();
                    }
                    else
                    {
                        ImageUtil.imagepath=null;
                        new loadimage().execute();

                    }



    }

    private class loadimage extends AsyncTask<Void, Void, Void>
    {

        private Bitmap imagebitmap;

        @Override
        protected Void doInBackground(Void... voids) {

            if(profileDetails.getProfilePic()!=null)
                imagebitmap = getBitmapFromURL(""+profileDetails.getProfilePic());
            else
            {
                imagebitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round);
             //   imagebitmap=appimage;
            //    imagebitmap=Bitmap.createScaledBitmap(imagebitmap,100,100,false);
               // imagebitmap = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
            }


            System.out.println("rrrrrrrrrrrrr imagebitmap::::: "+imagebitmap);
            saveImage(imagebitmap);
//            System.out.println("rrrrrrrrrrr imagebitmap"+profileDetails.getProfilePic());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //image.setImageBitmap(imagebitmap);

            ImageUtil.imagepath=GetPath();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher);
            try{
                Glide.with(Update_Profile.this).load(ImageUtil.imagepath)
                        .apply(options).into(profile_image);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



        }
    }

    private String GetPath(){


        File file = new File(Filepath);
        file = new File(file.getAbsolutePath());

        return String.valueOf(file);
    }
    private void saveImage(Bitmap finalBitmap) {
       // String root = Environment.getExternalStorageDirectory().getAbsolutePath();
       // File myDir = new File(root +"/"+getResources().getString(R.string.app_name));

        File myDir = new File((Update_Profile.this
                .getApplicationContext().getFileStreamPath(""+Constants.appname)
                .getPath()));

        System.out.println("rrrrrrrrrrr myDir "+myDir.getAbsolutePath());
        if (!myDir.exists()) {

            myDir.mkdirs();
        }
        String iname;
        if(myDir.listFiles()!=null)
        {
            iname = "Image-" + System.currentTimeMillis()+ ".jpg";

        }else{
            iname = "Image-" + System.currentTimeMillis() + ".jpg";
        }
        File file = new File(myDir, iname);

        //  File file = new File(myDir, iname);
        Filepath = file.toString();

        //System.out.println("rrrrrrrrrrr rrrrFilepath"+Filepath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            Log.v("SaveFile",file.toString());
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {

            e.printStackTrace();


        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private void setRadiosListener() {
        Female.setOwnOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    profile_gender.setText("Female");
                else
                    profile_gender.setText("Male");
            }
        });
        Male.setOwnOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    profile_gender.setText("Male");
                else
                    profile_gender.setText("Female");
            }
        });
    }


    private void updateprofile1() {

        if (mCustomDialog != null)
            mCustomDialog.show();



        System.out.println("rrrrrrrrrr ImageUtil.imagepath ImageUtil.imagepath "+ImageUtil.imagepath);
        //pass it like this
        File file = new File(""+ImageUtil.imagepath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("ProfilePic", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody id =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+userid);


        RequestBody Username =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_username.getText().toString());


        RequestBody FirstName =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_name.getText().toString());


        RequestBody LastName =
                RequestBody.create(MediaType.parse("multipart/form-data"), "");


        RequestBody Mobile =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_mobile.getText().toString());


        RequestBody Email =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_mailid.getText().toString());


        RequestBody Gender =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_gender.getText().toString());


        RequestBody DateOfBirth =
                RequestBody.create(MediaType.parse("multipart/form-data"), profile_dob.getText().toString());


        RequestBody ReferalCode =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_referalcode.getText().toString());


        RequestBody isEmailVerified =
                RequestBody.create(MediaType.parse("multipart/form-data"), "true");


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call;

//        System.out.println("profileDetails.getReferralCodeApplied() "+profileDetails.getReferralCodeApplied().equalsIgnoreCase("null"));

        try {
            if(!profile_referalcode.getText().toString().isEmpty()&&profileDetails.getReferralCodeApplied().length()>0)
            {

                System.out.println("rrrrrrrrrr enterr");
                call=apiService.updateProfile_norefferal(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        isEmailVerified,body,"Bearer "+access_token);

            }
            else {

                System.out.println("rrrrrrrrrr not enterr");
                call=apiService.updateProfile(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        ReferalCode,isEmailVerified,body,"Bearer "+access_token);

            }
        }
        catch (Exception e)
        {

            if(!profile_referalcode.getText().toString().isEmpty())
            {
                System.out.println("rrrrrrrrrr enterr exception"+e.getMessage());
                call=apiService.updateProfile_norefferal(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        isEmailVerified,body,"Bearer "+access_token);

            }
            else {

                System.out.println("rrrrrrrrrr enterr exception"+e.getMessage());
                call=apiService.updateProfile(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        ReferalCode,isEmailVerified,body,"Bearer "+access_token);

            }
        }


        // Call<ResponseBody> call = apiService.UPDATEPROFILE(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }


                System.out.println("rrrrrrrrrrrrrrr update profile "+response);

                if (response.isSuccessful()) {


                    Util.showAlerts(Update_Profile.this, "Profile Updated Successfully", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  signIn();
                            //finish();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                }
                else if (response.code() == 500) {
                    Util.showAlert(Update_Profile.this, ""+response.message(), false);

                }else {
                    Util.showAlert(Update_Profile.this, "Unable to Process Request...", false);
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
        /*ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.SIGNUP(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                mCustomDialog.dismiss();

                System.out.println("rrrrrrrrrrrrrrr sucess "+response);
                Log.d("response","Getting response from server : "+response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCustomDialog.dismiss();
                Log.d("response","Getting response from server : "+t);
            }
        });*/

    }

    private void updateprofile() {

        if (mCustomDialog != null)
            mCustomDialog.show();



        System.out.println("rrrrrrrrrr ImageUtil.imagepath ImageUtil.imagepath "+ImageUtil.imagepath);
        //pass it like this
        File file = new File(""+ImageUtil.imagepath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("ProfilePic", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody id =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+userid);


        RequestBody Username =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_username.getText().toString());


        RequestBody FirstName =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_name.getText().toString());


        RequestBody LastName =
                RequestBody.create(MediaType.parse("multipart/form-data"), "");


        RequestBody Mobile =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_mobile.getText().toString());


        RequestBody Email =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_mailid.getText().toString());


        RequestBody Gender =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_gender.getText().toString());


        RequestBody DateOfBirth =
                RequestBody.create(MediaType.parse("multipart/form-data"), profile_dob.getText().toString());


        RequestBody ReferalCode =
                RequestBody.create(MediaType.parse("multipart/form-data"), ""+profile_referalcode.getText().toString());


        RequestBody isEmailVerified =
                RequestBody.create(MediaType.parse("multipart/form-data"), "true");


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call;

//        System.out.println("profileDetails.getReferralCodeApplied() "+profileDetails.getReferralCodeApplied().equalsIgnoreCase("null"));

        try {
            if(!profile_referalcode.getText().toString().isEmpty()&&profileDetails.getReferralCodeApplied().length()>0)
            {

                System.out.println("rrrrrrrrrr enterr");
                call=apiService.updateProfile_norefferal(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        isEmailVerified,body,"Bearer "+access_token);
              //  call=apiService.updateProfile_image(body,"Bearer "+access_token);
            }
            else {

                System.out.println("rrrrrrrrrr not enterr");
                call=apiService.updateProfile(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        ReferalCode,isEmailVerified,body,"Bearer "+access_token);
              //  call=apiService.updateProfile_image(body,"Bearer "+access_token);


            }
        }
        catch (Exception e)
        {

            if(!profile_referalcode.getText().toString().isEmpty())
            {
                System.out.println("rrrrrrrrrr enterr exception"+e.getMessage());
                call=apiService.updateProfile_norefferal(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        isEmailVerified,body,"Bearer "+access_token);

            }
            else {

                System.out.println("rrrrrrrrrr enterr exception"+e.getMessage());
                call=apiService.updateProfile(id,Username,FirstName,LastName,Mobile , Email,Gender,DateOfBirth,
                        ReferalCode,isEmailVerified,body,"Bearer "+access_token);

            }
        }


        // Call<ResponseBody> call = apiService.UPDATEPROFILE(jsonObject,"Bearer "+access_token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (mCustomDialog != null && mCustomDialog.isShowing()) {
                    mCustomDialog.dismiss();
                }


                System.out.println("rrrrrrrrrrrrrrr update profile "+response);

                if (response.isSuccessful()) {


                    Util.showAlerts(Update_Profile.this, "Profile Updated Successfully", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  signIn();
                            //finish();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                }
                else if (response.code() == 500) {
                    Util.showAlert(Update_Profile.this, ""+response.message(), false);

                }else {
                    Util.showAlert(Update_Profile.this, "Unable to Process Request...", false);
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
        /*ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.SIGNUP(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                mCustomDialog.dismiss();

                System.out.println("rrrrrrrrrrrrrrr sucess "+response);
                Log.d("response","Getting response from server : "+response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCustomDialog.dismiss();
                Log.d("response","Getting response from server : "+t);
            }
        });*/

    }

}
