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

    public void setProductSearchIndicator(int value) {
        editor.putInt("productFetchIndicator", value).apply();
    }

    public int getProductSearchIndicator() {
        return sharedPreferences.getInt("productFetchIndicator", 0);
    }

    public void setSearchCategoryName(String value) {
        editor.putString("productSearchCategory", value).apply();
        editor.putString("productSearchName", null).apply();
    }

    public String getSearchCategoryName() {
        return sharedPreferences.getString("productSearchCategory", "");
    }

    public void setSearchProductName(String value) {
        editor.putString("productSearchName", value).apply();
        editor.putString("productSearchCategory", null).apply();
    }

    public String getetSearchProductName() {
        return sharedPreferences.getString("productSearchName", "");
    }

    public void resetSearchIndicator() {
        editor.putInt("productFetchIndicator", 0).apply();
        editor.putString("productSearchName", null).apply();
        editor.putString("productSearchCategory", null).apply();
    }

    public void setOrderHistoryFetchIndicator(int value) {
        editor.putInt("orderFetchIndicator", value).apply();
    }

    public int getOrderHistoryFetchIndicator() {
        return sharedPreferences.getInt("orderFetchIndicator", 0);
    }

    public void setOrderHistoryFetchName(String value) {
        editor.putString("orderHistorySearchName", value).apply();
    }

    public String getOrderHistoryFetchName() {
        return sharedPreferences.getString("orderHistorySearchName", "");
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
