package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.paginatedResponses.InAppNotificationPaginatedRes;

public class InAppNotificationResponse {
    private String message;
    private Integer status;
    @SerializedName("data")
    private InAppNotificationPaginatedRes inAppNotificationPaginatedRes;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public InAppNotificationPaginatedRes getInAppNotificationPaginatedRes() {
        return inAppNotificationPaginatedRes;
    }
}
