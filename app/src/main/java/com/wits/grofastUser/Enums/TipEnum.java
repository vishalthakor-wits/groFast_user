package com.wits.grofastUser.Enums;

public enum TipEnum {
    tip30("30RS"), tip20("20RS"), tipOther("CUSTOM_TIP");

    private String value;

    TipEnum(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
