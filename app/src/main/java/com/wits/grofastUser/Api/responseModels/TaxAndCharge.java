package com.wits.grofastUser.Api.responseModels;

public class TaxAndCharge {

    private final String text;
    private final Float value;

    public TaxAndCharge(String text, Float value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public Float getValue() {
        return value;
    }
}
