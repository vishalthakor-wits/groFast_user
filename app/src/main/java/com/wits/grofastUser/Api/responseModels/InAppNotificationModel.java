package com.wits.grofastUser.Api.responseModels;

public class InAppNotificationModel {
    private int id;
    private int to;
    private int from;
    private String title;
    private String body;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getTo() {
        return to;
    }

    public int getFrom() {
        return from;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
