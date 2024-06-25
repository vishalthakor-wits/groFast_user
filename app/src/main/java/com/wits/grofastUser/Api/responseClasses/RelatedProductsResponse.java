package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.responseModels.ProductModel;

import java.util.List;

public class RelatedProductsResponse {

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
