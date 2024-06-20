package com.wits.grofast_user.Notification;

import com.wits.grofast_user.Api.responseClasses.NotificationResponse;
import com.wits.grofast_user.Api.responseModels.NotificationModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationInterface {
    @POST("add-fcm-token")
    Call<Void> storeFcmtoServer(@Query("fcm_token") String token);

    @GET("customer-notification-fetch")
    Call<NotificationResponse> notificationFetch();

    @GET("customer-notification-setting")
    Call<NotificationResponse> notificationsetting();

}
