package com.wits.grofast_user.Api.responseModels;

public class WalletModel {
    private Integer id;
    private String uuid;
    private Integer customer_id;
    private Integer order_id;
    private Integer points;
    private String status;
    private String created_at;
    private String updated_at;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public Integer getPoints() {
        return points;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
