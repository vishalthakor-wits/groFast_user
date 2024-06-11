package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.Api.responseClasses.WalletResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WalletInterface {
    @GET("fetchWalletDetails")
    Call<WalletResponse> fetchWallet();

    @GET("activateWallet")
    Call<LoginResponse> activateWallet();
}
