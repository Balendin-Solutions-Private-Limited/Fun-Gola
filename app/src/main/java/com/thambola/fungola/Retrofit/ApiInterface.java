package com.thambola.fungola.Retrofit;


import com.google.gson.JsonObject;
import com.thambola.fungola.Model.BuyTicket;
import com.thambola.fungola.Model.GameList;
import com.thambola.fungola.Model.GenerateCheckSum;
import com.thambola.fungola.Model.GetGameWinners;
import com.thambola.fungola.Model.GetUsersPerGame;
import com.thambola.fungola.Model.GetWinnerPrice;
import com.thambola.fungola.Model.MyGameHistory;
import com.thambola.fungola.Model.ProfileDetails;
import com.thambola.fungola.Model.SignupResult;
import com.thambola.fungola.Model.StartGame;
import com.thambola.fungola.Model.WalletTransactionHistory;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by anupamchugh on 09/01/17.
 */

public interface ApiInterface {


    @POST("token")
    @FormUrlEncoded
    Call<ResponseBody> SIGNIN_EMPLOYEE(@Field("username") String mobile,
                                       @Field("grant_type") String grant_type
    );

    @POST("token")
    @FormUrlEncoded
    Call<ResponseBody> SIGNIN(@Field("username") String mobile,
                              @Field("password") String password,
                              @Field("grant_type") String grant_type
    );


    @POST("api/account/register")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json;charset=utf-8",
            //"Cache-Control: max-age=640000"
    })
    Call<SignupResult> SIGNUP(@Body JsonObject jsonObject);


    //{{api_host}}/api/gameapi/GameListWithType?gameType=Public
   // {{api_host}}/api/gameapi/GameListWithType?gameType=Free
    //{{api_host}}/api/gameapi/GameListWithType?gameType=Private
   // @GET("api/gameapi/GameListWithType?gameType={gameType}")
   // Call<ArrayList<GameList>> ALLGAMELIST(String gameType,@Header("Authorization") String authHeader);

    @GET("api/gameapi/GameListWithType")
    Call<ArrayList<GameList>> ALLGAMELIST(@Query("gameType") String gameType,
                                          @Header("Authorization") String authHeader);

    @POST("api/ticket/buyticket")
    Call<BuyTicket> BuyTicket(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    @POST("api/gameapi/Claim")
    Call<ResponseBody> ClaimGame(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    @POST("api/gameapi/startgame")
    Call<StartGame> StartGame(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);



    @GET("api/ticket/GetUsersPerGame/{userId}")
    Call<ArrayList<GetUsersPerGame>> GetUsersPerGame(@Path("userId") String userId, @Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);

    @GET("api/gameapi/GetWinner/{userId}")
    Call<ArrayList<GetGameWinners>> GetGameWinners(@Path("userId") String userId, @Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);

    @GET("api/gameapi/MyGameHistory")
    Call<ArrayList<MyGameHistory>> GetGameHistory(@Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);

    @GET("api/gameapi/GetWinnerPrice/{userId}")
    Call<ArrayList<GetWinnerPrice>> GetWinnerPrice(@Path("userId") String userId, @Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);


    @GET("api/walletapi/getwalletbalance")
    Call<ResponseBody> GetWalletbalance(@Header("Authorization") String authHeader);

    @GET("/api/configapi/GetConfig")
    Call<ResponseBody> GetPlastoreVersion();

    ///servertime
    @GET("/api/timezone/Asia/kolkata.json")
    Call<ResponseBody> GetServerTime();

    @GET("api/walletapi/GetWalletTransactionHistory/{userId}")
    Call<ArrayList<WalletTransactionHistory>> GetWalletTransactionHistory(@Path("userId") String userId, @Header("Authorization") String authHeader);


    @Multipart
    @POST("api/account/UpdateProfile")
    Call<ResponseBody> updateProfile(@Part("Id") RequestBody Id,
                                     @Part("Username") RequestBody Username,
                                     @Part("firstName") RequestBody FirstName,
                                     @Part("lastName") RequestBody LastName,
                                     @Part("Mobile") RequestBody Mobile,
                                     @Part("Email") RequestBody Email,
                                     @Part("Gender") RequestBody Gender,
                                     @Part("DateOfBirth") RequestBody DateOfBirth,
                                     @Part("ReferralCodeToBeApplied") RequestBody ReferalCode,
                                     @Part("isEmailVerified") RequestBody isEmailVerified,
                                     @Part MultipartBody.Part image,
                                     @Header("Authorization") String authHeader);


    @Multipart
    @POST("api/account/UpdateProfile")
    Call<ResponseBody> updateProfile_image(
            @Part MultipartBody.Part image,
            @Header("Authorization") String authHeader);


    @Multipart
    @POST("api/account/UpdateProfile")
    Call<ResponseBody> updateProfile_norefferal(@Part("Id") RequestBody Id,
                                                @Part("Username") RequestBody Username,
                                                @Part("firstName") RequestBody FirstName,
                                                @Part("lastName") RequestBody LastName,
                                                @Part("Mobile") RequestBody Mobile,
                                                @Part("Email") RequestBody Email,
                                                @Part("Gender") RequestBody Gender,
                                                @Part("DateOfBirth") RequestBody DateOfBirth,
                                                @Part("isEmailVerified") RequestBody isEmailVerified,
                                                // @Part("ReferalCode") RequestBody ReferalCode,
                                                @Part MultipartBody.Part image,
                                                @Header("Authorization") String authHeader);





    @POST("api/account/updateprofile")
    Call<ResponseBody> UPDATEPROFILE(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    @GET("api/account/GetProfile/{userId}")
    Call<ProfileDetails> GetPROFILE(@Path("userId") String userId, @Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);

    @POST("api/LeadsApi/Addbankdetails")
    Call<ResponseBody> AddBankAccount(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);


    @POST("api/configapi/SaveConfig")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json;charset=utf-8",
            //"Cache-Control: max-age=640000"
    })
    //Call<ResponseBody> UpdateToken(@Field("FireBaseToken") String regKey, @Header("Authorization") String authHeader);
    Call<ResponseBody> UpdateToken(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);


    @POST("api/Account/ForgetPassword")
    Call<ResponseBody> ForgotPassword(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    @POST("api/Account/ChangePassword")
    Call<ResponseBody> ChangePassword(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);


    @POST("api/walletapi/ProcessWalletTransaction")
    Call<ResponseBody> WalletProcess(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);


    @POST("api/WalletApi/GenerateCheckSum")
    Call<GenerateCheckSum> GenerateCheckSum(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    @POST("api/WalletApi/SendPayoutRequest")
    Call<ResponseBody> Withdrawprocess(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    @POST("api/WalletApi/UpdateOrderStatus/{orderId}")
    Call<ResponseBody> UpdateOrderStatus(@Path("orderId") String orderId, @Body JsonObject jsonObject, @Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);

    @GET("api/gameapi/IsSpinAvailable")
    Call<ResponseBody> IsSpinAvailable(@Header("Authorization") String authHeader);//Call<Post> savePost(@Body User user);

    @POST("api/gameapi/UpdateOffer")
    Call<ResponseBody> UpdateOffer(@Query("offer") String gameType,
                                   @Header("Authorization") String authHeader);

}
