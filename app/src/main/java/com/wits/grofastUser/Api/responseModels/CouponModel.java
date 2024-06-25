package com.wits.grofastUser.Api.responseModels;

public class CouponModel {
    private Integer id;
    private String uuid;
    private String name;
    private String description;
    private String code;
    private String from;
    private String to;
    private String discount;
    private String max_amount;
    private String status;
    private String image;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDiscount() {
        return discount;
    }

    public String getMax_amount() {
        return max_amount;
    }

    public String getStatus() {
        return status;
    }

    public Integer getType() {
        return type;
    }

    public String getImage() {
        return image;
    }
}
