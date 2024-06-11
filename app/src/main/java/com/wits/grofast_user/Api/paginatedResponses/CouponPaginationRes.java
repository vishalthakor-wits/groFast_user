package com.wits.grofast_user.Api.paginatedResponses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.CouponModel;

import java.util.List;

public class CouponPaginationRes {

    @SerializedName("data")
    private List<CouponModel> couponList;

    private Integer current_page;

    private Integer last_page;

    private Integer from;

    private Integer to;

    private Integer total;

    private Integer per_page;

    public List<CouponModel> getCouponList() {
        return couponList;
    }

    public Integer getCurrent_page() {
        return current_page;
    }

    public Integer getLast_page() {
        return last_page;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPer_page() {
        return per_page;
    }

}
