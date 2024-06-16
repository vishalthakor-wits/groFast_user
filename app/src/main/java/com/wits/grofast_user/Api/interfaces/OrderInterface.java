package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.OrderHistoryResponse;
import com.wits.grofast_user.Api.responseClasses.OrderPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderInterface {
    @POST("place-user-order")
    Call<OrderPlaceResponse> placeOrder( @Query("coupon") String couponCode, @Query("tip") int tip, @Query("additional_note") String aditionalNote, @Query("address_id") int addressId, @Query("receiver_name") String receiverName, @Query("receiver_phone_no") Long receiverPhone, @Query("payment_metod") int paymentMethod);

    @GET("fetch-user-order-histroy")
    Call<OrderHistoryResponse> fetchOrderHistory();

    @POST("search-user-order")
    Call<OrderHistoryResponse> searchOrders(@Query("name") String couponCode);
}
