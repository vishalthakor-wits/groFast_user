package com.wits.grofast_user.Api.responseModels;

public class BannerModel {
    private Integer id;
    private String uuid;
    private String image;
    private String start_date;
    private String end_date;
    private int status;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getImage() {
        return image;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getStatus() {
        return status;
    }
}
