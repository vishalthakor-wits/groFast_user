package com.wits.grofast_user.Api.responseModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderModel implements Parcelable {

    private float sgst;

    private String country;

    private String city;

    private String latitude;

    private float discount;

    private String created_at;

    private String uuid;

    @SerializedName("order_status")
    private OrderStatusModel orderStatus;

    private float delivery_charges;

    private String updated_at;

    private Integer payment_metod;

    private String receiver_name;

    private float tip;

    private Integer id;

    private String state;

    private String longitude;

    private String pincode;

    private String address;

    private String coupon;

    private float cgst;

    private String receiver_phone_no;

    private float total_amount;

    private Integer customer_id;

    private String additional_note;

    @SerializedName("customer_order_items")
    private List<OrderItemModel> orderItems;

    @SerializedName("is_cancel_allow")
    private int isCancelAllow;
    @SerializedName("is_return_allow")
    private int isReturnAllow;
    @SerializedName("is_reorder_allow")
    private int isReorderAllow;

    protected OrderModel(Parcel in) {
        sgst = in.readFloat();
        country = in.readString();
        city = in.readString();
        latitude = in.readString();
        discount = in.readFloat();
        created_at = in.readString();
        uuid = in.readString();
        delivery_charges = in.readFloat();
        updated_at = in.readString();
        if (in.readByte() == 0) {
            payment_metod = null;
        } else {
            payment_metod = in.readInt();
        }
        receiver_name = in.readString();
        tip = in.readFloat();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        state = in.readString();
        longitude = in.readString();
        pincode = in.readString();
        address = in.readString();
        coupon = in.readString();
        cgst = in.readFloat();
        receiver_phone_no = in.readString();
        total_amount = in.readFloat();
        if (in.readByte() == 0) {
            customer_id = null;
        } else {
            customer_id = in.readInt();
        }
        additional_note = in.readString();
        isCancelAllow = in.readInt();
        isReturnAllow = in.readInt();
        isReorderAllow = in.readInt();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public int getIsCancelAllow() {
        return isCancelAllow;
    }

    public int getIsReturnAllow() {
        return isReturnAllow;
    }

    public int getIsReorderAllow() {
        return isReorderAllow;
    }

    public float getSgst() {
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

    public float getDiscount() {
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

    public float getDelivery_charges() {
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

    public float getTip() {
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

    public float getCgst() {
        return cgst;
    }

    public String getReceiver_phone_no() {
        return receiver_phone_no;
    }

    public float getTotal_amount() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(sgst);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(latitude);
        dest.writeFloat(discount);
        dest.writeString(created_at);
        dest.writeString(uuid);
        dest.writeFloat(delivery_charges);
        dest.writeString(updated_at);
        if (payment_metod == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(payment_metod);
        }
        dest.writeString(receiver_name);
        dest.writeFloat(tip);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(state);
        dest.writeString(longitude);
        dest.writeString(pincode);
        dest.writeString(address);
        dest.writeString(coupon);
        dest.writeFloat(cgst);
        dest.writeString(receiver_phone_no);
        dest.writeFloat(total_amount);
        if (customer_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(customer_id);
        }
        dest.writeString(additional_note);
        dest.writeInt(isCancelAllow);
        dest.writeInt(isReturnAllow);
        dest.writeInt(isReorderAllow);
    }
}
