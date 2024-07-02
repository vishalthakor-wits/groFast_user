package com.wits.grofastUser.Notification;

public class NotificationModel {
    private int id;
    private int user_id;
    private int enable_all;
    private int push_notification;
    private int newsletter_email;
    private int promo_offer_email;
    private int promo_offer_push;
    private int social_notification_email;
    private int social_notification_push;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getEnable_all() {
        return enable_all;
    }

    public int getPush_notification() {
        return push_notification;
    }

    public int getNewsletter_email() {
        return newsletter_email;
    }

    public int getPromo_offer_email() {
        return promo_offer_email;
    }

    public int getPromo_offer_push() {
        return promo_offer_push;
    }

    public int getSocial_notification_email() {
        return social_notification_email;
    }

    public int getSocial_notification_push() {
        return social_notification_push;
    }

}
