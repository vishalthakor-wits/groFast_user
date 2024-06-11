package com.wits.grofast_user.Api.responseModels;

import com.google.gson.annotations.SerializedName;

public class HomeCategoryModel {

    private String updated_at;

    private Integer product_id;

    private String created_at;

    private Integer id;

    private String uuid;

    @SerializedName("home_category")
    private CategoryModel homeCategory;

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

    public CategoryModel getHomeCategory() {
        return homeCategory;
    }
}
