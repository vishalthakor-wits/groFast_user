package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.paginatedResponses.ProductPaginatedRes;

public class ProductResponse {
    private String message;

    private Integer status;

    @SerializedName("products")
    private ProductPaginatedRes paginatedProducts;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public ProductPaginatedRes getPaginatedProducts() {
        return paginatedProducts;
    }
}
