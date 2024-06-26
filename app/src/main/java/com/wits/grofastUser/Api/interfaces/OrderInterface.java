package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.Api.responseClasses.OrderHistoryResponse;
import com.wits.grofastUser.Api.responseClasses.OrderPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderInterface {
    @POST("place-user-order")
    Call<OrderPlaceResponse> placeOrder(@Query("coupon") String couponCode, @Query("tip") int tip, @Query("additional_note") String aditionalNote, @Query("address_id") int addressId, @Query("receiver_name") String receiverName, @Query("receiver_phone_no") Long receiverPhone, @Query("payment_metod") int paymentMethod);

    @GET("fetch-user-order-histroy")
    Call<OrderHistoryResponse> fetchOrderHistory(@Query("page") int page);

    @POST("search-user-order")
    Call<OrderHistoryResponse> searchOrders(@Query("name") String couponCode, @Query("page") int page);

    @POST("user-reorder")
    Call<OrderPlaceResponse> reOrder(@Query("orderId") int orderId);

    @POST("cancel-user-order")
    Call<LoginResponse> cancelOrder(@Query("order_id") int order_id, @Query("reason") String reason);
}
