package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.EditProfileResponse;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserInterface {
    @POST("editCustomerProfile")
    @Multipart
    Call<EditProfileResponse> updateProfile(
            @Part("phone_no") RequestBody phone_no,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part image
    );

    @GET("removeProfileImage")
    Call<Void> removeUserProfile();


    @POST("update-phone-no")
    Call<LoginResponse> updateuserPhoneNo(@Query("phone_no") String phone_no);

    @POST("delete-customer")
    Call<LoginResponse> deleteaccount();

    @POST("customer-logout")
    Call<LoginResponse> logout();
}
