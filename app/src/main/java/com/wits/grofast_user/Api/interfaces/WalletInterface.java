package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.Api.responseClasses.WalletResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WalletInterface {
    @GET("fetchWalletDetails")
    Call<WalletResponse> fetchWallet(@Query("page") int page);

    @GET("activateWallet")
    Call<LoginResponse> activateWallet();

    @GET("get-customer-total-coins")
    Call<WalletResponse> getWalletCoin();
}
