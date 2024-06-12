package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.ProductModel;

import java.util.List;

public class RelatedPeoductsResponse {

    private String message;

    private Integer status;

    @SerializedName("products")
    private List<ProductModel> productList;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<ProductModel> getProductList() {
        return productList;
    }
}
