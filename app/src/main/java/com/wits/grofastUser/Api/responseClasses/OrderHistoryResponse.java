package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.responseModels.OrderModel;

import java.util.List;

public class OrderHistoryResponse {

    private int status;
    private String message;

    @SerializedName("orders")
    private OrderPaginatedResponse paginatedOrders;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OrderPaginatedResponse getPaginatedOrders() {
        return paginatedOrders;
    }
}
