package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.Api.responseClasses.WalletResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WalletInterface {
    @GET("fetchWalletDetails")
    Call<WalletResponse> fetchWallet(@Query("page") int page);

    @GET("activateWallet")
    Call<LoginResponse> activateWallet();

    @GET("get-customer-total-coins")
    Call<WalletResponse> getWalletCoin();
}
