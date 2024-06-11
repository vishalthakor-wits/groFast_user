package com.wits.grofast_user.session;

import android.content.Context;
import android.content.SharedPreferences;

public class CartDetailSession {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public CartDetailSession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserCartDetail", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setTip(String tip) {
        editor.putString("userOrderTip", tip);
        editor.apply();
    }

    public String getTip() {
        String tip = sharedPreferences.getString("userOrderTip", "0");
        if (tip.equals("")) {
            tip = "0";
        }
        return tip;
    }

    public void setCoupon(String tip) {
        editor.putString("userOrderCoupon", tip.trim());
        editor.apply();
    }

    public String getCoupon() {
        return sharedPreferences.getString("userOrderCoupon", "");
    }

    public void storeAditionalNote(String note) {
        editor.putString("userAditionalNote", note.trim());
        editor.apply();
    }

    public String getAditionalNote() {
        return sharedPreferences.getString("userAditionalNote", "");
    }

    public void setTotalAmount(Float total) {
        editor.putFloat("userCartTotal", total);
    }

    public Float getTotalAmount() {
        return sharedPreferences.getFloat("userCartTotal", 0);
    }

    public void setDiscount(Float discount) {
        editor.putFloat("userCartDiscount", discount);
    }

    public Float getDiscount() {
        return sharedPreferences.getFloat("userCartDiscount", 0);
    }

    public void setDeleveryCharges(Float charges) {
        editor.putFloat("userCartDeleveryCharges", charges);
    }

    public Float getDeleveryCharges() {
        return sharedPreferences.getFloat("userCartDeleveryCharges", 0);
    }

    public void setSgst(Float sgst) {
        editor.putFloat("userCartSgst", sgst);
    }

    public Float getSgst() {
        return sharedPreferences.getFloat("userCartSgst", 0);
    }

    public void setCgst(float cgst) {
        editor.putFloat("userCartCgst", cgst);
    }

    public Float getCgst() {
        return sharedPreferences.getFloat("userCartCgst", 0);
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
