package com.wits.grofastUser.Api.responseModels;

import com.google.gson.annotations.SerializedName;

public class HomeProductModel {

    private String updated_at;

    private Integer product_id;

    private String created_at;

    private Integer id;

    private String uuid;

    @SerializedName("home_product")
    private ProductModel product;

    public String getUpdated_at() {
        return updated_at;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public ProductModel getProduct() {
        return product;
    }
}
