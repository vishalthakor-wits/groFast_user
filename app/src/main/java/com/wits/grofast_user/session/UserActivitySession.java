package com.wits.grofast_user.session;

import android.content.Context;
import android.content.SharedPreferences;

public class UserActivitySession {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public UserActivitySession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserActivity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginStaus(boolean status) {
        editor.putBoolean("loginStatus", status);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("loginStatus", false);
    }
    public void setToken(String token) {
        editor.putString("userAccessToken", token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("userAccessToken", "");
    }

    public void setProductFetchIndicator(int value) {
        editor.putInt("productFetchIndicator", value).apply();

//        0 -> fetchAllProducts
//        1 -> fetchProductsByCategory
//        2 -> searchProducts
    }

    public int getProductFetchIndicator() {
        return sharedPreferences.getInt("productFetchIndicator", 0);
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
