package com.wits.grofastUser.Notification;

import com.wits.grofastUser.Api.responseClasses.InAppNotificationResponse;
import com.wits.grofastUser.Api.responseClasses.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationInterface {
    @POST("add-fcm-token")
    Call<Void> storeFcmtoServer(@Query("fcm_token") String token);

    @GET("customer-notification-fetch")
    Call<NotificationResponse> notificationFetch();

    @POST("customer-notification-setting")
    Call<NotificationResponse> notificationsetting(
            @Query("enable_all") Integer enable_all,
            @Query("push_notification") Integer push_notification,
            @Query("newsletter_email") Integer newsletter_email,
            @Query("promo_offer_email") Integer promo_offer_email,
            @Query("promo_offer_push") Integer promo_offer_push,
            @Query("social_notification_email") Integer social_notification_email,
            @Query("social_notification_email") Integer social_notification_push
    );

    @GET("customer-inapp-notification")
    Call<InAppNotificationResponse> inappnotification(@Query("page") int page);

}
