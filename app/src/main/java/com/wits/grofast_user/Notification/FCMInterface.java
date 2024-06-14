package com.wits.grofast_user.Notification;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FCMInterface {
    @POST("add-fcm-token")
    Call<Void> storeFcmtoServer(@Query("fcm_token") String token);
}
