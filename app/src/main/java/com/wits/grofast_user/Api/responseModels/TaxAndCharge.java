package com.wits.grofast_user.Api.responseModels;

public class TaxAndCharge {

    private String text;
    private Float value;

    public String getText() {
        return text;
    }

    public Float getValue() {
        return value;
    }

    public TaxAndCharge(String text, Float value) {
        this.text = text;
        this.value = value;
    }
}
