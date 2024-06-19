package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.BannerModel;

public class BannerFetchResponse {
    private String message;

    private Integer status;

    @SerializedName("banners")
    private BannerModel banners;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public BannerModel getBanners() {
        return banners;
    }
}
