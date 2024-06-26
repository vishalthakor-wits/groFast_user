package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.responseModels.HomeCategoryModel;

import java.util.List;

public class HomeCategoryResponse {

    private String message;

    private Integer status;

    @SerializedName("categories")
    List<HomeCategoryModel> categoryList;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<HomeCategoryModel> getCategoryList() {
        return categoryList;
    }
}
