package com.wits.grofastUser.Api.responseModels;

public class CustomSpinnerModel {
    private String name;

    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public CustomSpinnerModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public CustomSpinnerModel() {
    }
}
