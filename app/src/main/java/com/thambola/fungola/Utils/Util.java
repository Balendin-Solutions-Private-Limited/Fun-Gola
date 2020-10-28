package com.thambola.fungola.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.thambola.fungola.R;

public class Util {


    public static final String SHARED_PREFERENCES_NAME = ""+Constants.appname;


    public static void showAlerts(final Activity _activity,
                                  String alertMsg, DialogInterface.OnClickListener listener) {
        if (_activity == null || _activity.isFinishing())
            return;
        final AlertDialog.Builder alert = new AlertDialog.Builder(_activity);
        alert.setTitle(_activity.getResources().getString(R.string.app_name));
        alert.setCancelable(false);
        alert.setMessage(alertMsg);
        alert.setNegativeButton("CANCEL",listener);
        alert.setPositiveButton("Ok",listener);


        alert.show();

    }

    public static void showAlert(final Activity _activity,
                                 String alertMsg, final boolean goBack) {
        if (_activity == null || _activity.isFinishing())
            return;
        AlertDialog.Builder alert = new AlertDialog.Builder(_activity);
        alert.setTitle(_activity.getResources().getString(R.string.app_name));

        alert.setCancelable(false);
        alert.setMessage(alertMsg);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (goBack)
                    _activity.finish();
            }
        });
        alert.show();

    }

    public static boolean checkInternetConnection(Context _activity) {
        ConnectivityManager conMgr = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
    public static boolean checkInternetConnection1(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();


    }

    public static void saveBooleanInSP(Context _activity, String key, boolean value){
        SharedPreferences preferences = _activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveStringInSP(Context _activity, String key, String value){
        SharedPreferences preferences = _activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromSP(Context _activity, String key) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = _activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
    /**
     * Retrieve boolean value from SharedPreference for the given key
     */
    public static boolean getBooleanFromSP(Context _activity, String key) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = _activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

}
