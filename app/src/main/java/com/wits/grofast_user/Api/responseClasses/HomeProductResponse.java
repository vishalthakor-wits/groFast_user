package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.HomeProductModel;

import java.util.List;

public class HomeProductResponse {
    private String message;

    private Integer status;

    @SerializedName("products")
    List<HomeProductModel> productList;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<HomeProductModel> getProductList() {
        return productList;
    }
}
