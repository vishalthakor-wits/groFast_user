package com.wits.grofastUser.Api.responseClasses;


import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.responseModels.NotificationModel;

public class NotificationResponse {
    private String message;
    private Integer status;
    @SerializedName("data")
    private NotificationModel notificationModel;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

}
