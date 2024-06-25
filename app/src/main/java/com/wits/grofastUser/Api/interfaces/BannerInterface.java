package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.BannerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BannerInterface {
    @GET("fetchBanners")
    Call<BannerResponse> fetchbanner();
}
