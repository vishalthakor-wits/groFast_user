package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.CouponResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CouponInterface {
    @GET("fetchCoupons")
    Call<CouponResponse> fetchCoupon(@Query("page") int page);
}
