package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.BannerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BannerInterface {
    @GET("fetchBanners")
    Call<BannerResponse> fetchbanner();
}
