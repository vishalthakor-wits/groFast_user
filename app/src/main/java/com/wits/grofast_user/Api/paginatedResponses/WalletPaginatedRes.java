package com.wits.grofast_user.Api.paginatedResponses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.WalletModel;

import java.util.List;

public class WalletPaginatedRes {
    private Integer current_page;

    private Integer last_page;

    private Integer from;

    private Integer to;

    private Integer total;

    private Integer per_page;

    @SerializedName("data")
    private List<WalletModel> walletList;

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

    public List<WalletModel> getWalletList() {
        return walletList;
    }
}
