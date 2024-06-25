package com.wits.grofast_user.Api.paginatedResponses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.OrderModel;

import java.util.List;

public class OrderPaginatedResponse {

    @SerializedName("data")
    private List<OrderModel> orderList;

    private int current_page;

    private int last_page;

    private int from;

    private int to;

    private int total;

    private int per_page;

    public List<OrderModel> getOrderList() {
        return orderList;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getTotal() {
        return total;
    }

    public int getPer_page() {
        return per_page;
    }
}
