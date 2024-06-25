package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.paginatedResponses.ProductPaginatedRes;

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
