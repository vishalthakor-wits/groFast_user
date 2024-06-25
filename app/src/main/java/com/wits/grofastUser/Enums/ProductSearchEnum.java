package com.wits.grofastUser.Enums;

public enum ProductSearchEnum {

    searchAll(0), searchByName(1), searchByCategory(2);
    private final int value;

    ProductSearchEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
