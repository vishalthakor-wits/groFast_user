package com.wits.grofastUser.session;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetailSession {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public UserDetailSession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setPhoneNo(String phone) {
        editor.putString("userPhone", phone);
        editor.apply();
    }

    public String getPhoneNo() {
        return sharedPreferences.getString("userPhone", "1234567890");
    }


    public void setName(String name) {
        editor.putString("userName", name);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString("userName", "");
    }

    public void setImage(String image) {
        editor.putString("userImage", image);
        editor.apply();
    }

    public String getImage() {
        return sharedPreferences.getString("userImage", null);
    }

    public void setEmail(String email) {
        editor.putString("userEmail", email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString("userEmail", "");
    }

    public void setGender(String gender) {
        editor.putString("userGender", gender);
        editor.apply();
    }

    public String getGender() {
        return sharedPreferences.getString("userGender", "");
    }
    public void setUuid(String uuid) {
        editor.putString("userUUID", uuid);
        editor.apply();
    }

    public String getUuid() {
        return sharedPreferences.getString("userUUID", "");
    }
    public void setUserId(Integer userId) {
        editor.putInt("userId", userId);
        editor.apply();
    }

    public Integer getUserId() {
        return sharedPreferences.getInt("userId", 0);
    }

    public void setWalletStatus(int status) {
        boolean value = false;
        if (status == 1) {
            value = true;
        }
        editor.putBoolean("userWalletStatus", value);
        editor.apply();
    }

    public boolean isWalletActivated() {
        return sharedPreferences.getBoolean("userWalletStatus", false);
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
