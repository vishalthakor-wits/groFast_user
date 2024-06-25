package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.AddToCartResponse;
import com.wits.grofastUser.Api.responseClasses.CartFetchResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartInterface {
    @POST("add-to-cart")
    Call<AddToCartResponse> addToCart(@Query("product_id") Integer product_id, @Query("quantity") Integer quantity);

    @POST("fetchCartDetails")
    Call<CartFetchResponse> fetchCartDetails(@Query("tip") Integer tip, @Query("coupon") String couponCode, @Query("aditional_note") String aditionalNote);

    @POST("remove-cart")
    Call<AddToCartResponse> removeCartItem(@Query("product_id") Integer product_id, @Query("quantity") Integer quantity);
}
