package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.OrderModel;

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
