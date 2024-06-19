package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.BannerModel;

import java.util.List;

public class BannerResponse {
    private String message;

    private Integer status;

    @SerializedName("banners")
    private List<BannerModel> banners;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<BannerModel> getBanners() {
        return banners;
    }
}
