package com.wits.grofast_user.session;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationSession {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public NotificationSession(Context context) {
        sharedPreferences = context.getSharedPreferences("Notification", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserId(Integer userId) {
        editor.putInt("userId", userId);
        editor.apply();
    }

    public Integer getUserId() {
        return sharedPreferences.getInt("userId", 0);
    }

    public void setId(Integer Id) {
        editor.putInt("userId", Id);
        editor.apply();
    }

    public Integer getId() {
        return sharedPreferences.getInt("Id", 0);
    }

    public void setEnableAll(Integer enableAll) {
        editor.putInt("enableAll", enableAll);
        editor.apply();
    }

    public Integer getEnableAll() {
        return sharedPreferences.getInt("enableAll", 0);
    }

    public void setPushNotification(Integer pushNotification) {
        editor.putInt("pushNotification", pushNotification);
        editor.apply();
    }

    public Integer getPushNotification() {
        return sharedPreferences.getInt("pushNotification", 0);
    }

    public void setNewsLetterEmailNotification(Integer newsLetterEmailNotification) {
        editor.putInt("newsLetterEmailNotification", newsLetterEmailNotification);
        editor.apply();
    }

    public Integer getNewsLetterEmailNotification() {
        return sharedPreferences.getInt("newsLetterEmailNotification", 0);
    }

    public void setPromoOfferEmail(Integer promoOfferEmail) {
        editor.putInt("promoOfferEmail", promoOfferEmail);
        editor.apply();
    }

    public Integer getPromoOfferEmail() {
        return sharedPreferences.getInt("promoOfferEmail", 0);
    }

    public void setPromoOfferPush(Integer promoOfferPush) {
        editor.putInt("promoOfferPush", promoOfferPush);
        editor.apply();
    }

    public Integer getpromoOfferPush() {
        return sharedPreferences.getInt("promoOfferPush", 0);
    }

    public void setsocialNotificationEmail(Integer socialNotificationEmail) {
        editor.putInt("socialNotificationEmail", socialNotificationEmail);
        editor.apply();
    }

    public Integer getsocialNotificationEmail() {
        return sharedPreferences.getInt("socialNotificationEmail", 0);
    }

    public void setsocialNotificationPush(Integer socialNotificationPush) {
        editor.putInt("socialNotificationPush", socialNotificationPush);
        editor.apply();
    }

    public Integer getsocialNotificationPush() {
        return sharedPreferences.getInt("socialNotificationPush", 0);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
