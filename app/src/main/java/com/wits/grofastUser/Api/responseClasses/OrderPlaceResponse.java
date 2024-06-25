package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.responseModels.OrderModel;

public class OrderPlaceResponse {
    private int status;
    private String message;

    @SerializedName("order")
    private OrderModel orderDetails;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OrderModel getOrderDetails() {
        return orderDetails;
    }
}
