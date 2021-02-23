package com.mobile.agri10x.retrofit;

import com.mobile.agri10x.models.CheckPhoneExits;
import com.mobile.agri10x.models.GetAddMoney;
import com.mobile.agri10x.models.GetLogin;
import com.mobile.agri10x.models.GetOTP;
import com.mobile.agri10x.models.GetProfile;
import com.mobile.agri10x.models.GetRequestedCommodity;
import com.mobile.agri10x.models.GetRequestedVariety;
import com.mobile.agri10x.models.GetTradeCommodity;
import com.mobile.agri10x.models.GetTradeVariety;
import com.mobile.agri10x.models.GetUserInfo;
import com.mobile.agri10x.models.SetUserO;
import com.mobile.agri10x.models.resendOTP;
import com.mobile.agri10x.models.verfyOTP;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AgriInvestor {


    @GET("getTradeCommodity")
    Call<List<GetTradeCommodity>> wsgetTradeCommodity(@Header("x-auth-token") String token);


    @POST("getTradeVariety")
    Call<List<GetTradeVariety>> wsgetTradeVariety(@Header("x-auth-token") String token, @Body RequestBody params);

    @GET("getRequestedCommodity")
    Call<List<GetRequestedCommodity>> wsgetRequestedCommodity(@Header("x-auth-token") String token);

    @POST("getRequestedVariety")
    Call<List<GetRequestedVariety>> wsgetRequestedVariety(@Header("x-auth-token") String token, @Body RequestBody params);

    @POST("getOTP")
    Call<GetOTP> wsgetOTP(@Header("x-auth-token") String token, @Body RequestBody params);


    @POST("verifyOTP")
    Call<verfyOTP> wsverifyOTP(@Header("x-auth-token") String token, @Body RequestBody params);

    @POST("resendOTP")
    Call<resendOTP> wsResendOTP(@Header("x-auth-token") String token, @Body RequestBody params);

    @POST("checkPhone")
    Call<CheckPhoneExits> wsCheckPhoneExits(@Header("x-auth-token") String token, @Body RequestBody params);


    @POST("login")
    Call<GetLogin> wsLoginCheck(@Header("x-auth-token") String token, @Body RequestBody params);

    @POST("getUser")
    Call<GetProfile> wsGetProfileData(@Header("x-auth-token") String token, @Body RequestBody params);


    @POST("SetUser")
    Call<GetUserInfo> wssetuserinfo(@Header("x-auth-token") String token, @Body SetUserO setUserO);

    @POST("AddMoney")
    Call<GetAddMoney> wsGetAddMoney(@Header("x-auth-token") String token,@Body RequestBody params);
}
