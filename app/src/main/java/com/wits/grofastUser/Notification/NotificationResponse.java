package com.wits.grofastUser.Notification;


import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Notification.NotificationModel;

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
