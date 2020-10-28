package com.thambola.fungola.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anupamchugh on 05/01/17.
 */

public class ApiClient {


    /// Stage-Url fungola and procuction
  //  public static final String BASE_URL = "http://fungola-stage.balendin.in/";
    // Stage-Url Funtime
    public static final String BASE_URL = "http://funtime-stage.balendin.in/";

    // Production-Url Funtime
  //  public static final String BASE_URL = "http://funtime.balendingames.in/";


    public static final String SERVER_URL = "http://worldtimeapi.org";


    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;


    public static Retrofit getClient_serverTIme() {
        if (retrofit1==null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(300, TimeUnit.MINUTES)
                    .writeTimeout(300, TimeUnit.MINUTES)
                    .connectTimeout(300, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
