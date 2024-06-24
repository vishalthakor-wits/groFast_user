package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.paginatedResponses.WalletPaginatedRes;

public class WalletResponse {
    private String message;

    private Integer status;
    private Integer coins;

    @SerializedName("response")
    private WalletPaginatedRes walletPaginatedRes;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public WalletPaginatedRes getWalletPaginatedRes() {
        return walletPaginatedRes;
    }

    public Integer getCoins() {
        return coins;
    }
}
