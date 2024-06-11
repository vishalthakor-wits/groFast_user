package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.OrderModel;

import java.util.List;

public class OrderHistoryResponse {

    private int status;
    private String message;

    @SerializedName("orders")
    private List<OrderModel> orderList;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderModel> getOrderList() {
        return orderList;
    }
}
