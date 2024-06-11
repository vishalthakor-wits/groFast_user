package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.paginatedResponses.CouponPaginationRes;

public class CouponResponse {
    private String message;

    private Integer status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @SerializedName("coupons")
    private CouponPaginationRes couponPaginationRes;
    public CouponResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public CouponPaginationRes getCouponPaginationRes() {
        return couponPaginationRes;
    }
}
