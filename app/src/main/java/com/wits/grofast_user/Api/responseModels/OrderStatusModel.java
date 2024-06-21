package com.wits.grofast_user.Api.responseModels;

import java.io.Serializable;

public class OrderStatusModel implements Serializable {
    private String label;
    private String color;

    public String getStatus() {
        return label;
    }

    public String getColor() {
        return color;
    }

}
