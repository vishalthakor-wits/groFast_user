package com.wits.grofast_user.address;

public class AddressSpinnerModel {
    private String name;

    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public AddressSpinnerModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public AddressSpinnerModel() {
    }
}
