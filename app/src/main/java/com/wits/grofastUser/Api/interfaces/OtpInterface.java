package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.Api.responseClasses.OtpVerifyResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OtpInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("phone_no") String phone_no);

    @POST("verifyOtp")
    Call<OtpVerifyResponse> verifyOtp(@Query("phone_no") String phone_no, @Query("otp") Integer otp);

    @FormUrlEncoded
    @POST("send-phone-update-otp")
    Call<LoginResponse> sendPhoneUpdateOtp(@Field("phone_no") String phone_no);

    @POST("verifyOtp-Change-Phone")
    Call<LoginResponse> verifyPhoneUpdateOtp(@Query("phone_no") String phone_no, @Query("otp") Integer otp);
}
