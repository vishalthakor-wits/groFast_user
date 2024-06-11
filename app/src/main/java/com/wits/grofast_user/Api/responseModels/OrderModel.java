package com.wits.grofast_user.Api.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderModel {

    private double sgst;

    private String country;

    private String city;

    private String latitude;

    private Integer discount;

    private String created_at;

    private String uuid;

    @SerializedName("order_status")
    private OrderStatusModel orderStatus;

    private Integer delivery_charges;

    private String updated_at;

    private Integer payment_metod;

    private String receiver_name;

    private Integer tip;

    private Integer id;

    private String state;

    private String longitude;

    private String pincode;

    private String address;

    private String coupon;

    private double cgst;

    private String receiver_phone_no;

    private Integer total_amount;

    private Integer customer_id;

    private String additional_note;

    @SerializedName("customer_order_items")
    private List<OrderItemModel> orderItems;

    public double getSgst() {
        return sgst;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getLatitude() {
        return latitude;
    }

    public Integer getDiscount() {
        return discount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUuid() {
        return uuid;
    }

    public OrderStatusModel getOrderStatus() {
        return orderStatus;
    }

    public Integer getDelivery_charges() {
        return delivery_charges;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Integer getPayment_metod() {
        return payment_metod;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public Integer getTip() {
        return tip;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPincode() {
        return pincode;
    }

    public String getAddress() {
        return address;
    }

    public String getCoupon() {
        return coupon;
    }

    public double getCgst() {
        return cgst;
    }

    public String getReceiver_phone_no() {
        return receiver_phone_no;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public String getAdditional_note() {
        return additional_note;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }
}
