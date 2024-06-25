package com.wits.grofastUser.Api.responseModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AddressModel implements Parcelable {

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };
    private final Integer id;
    private final Integer customer_id;
    private final String country;
    private final String state;
    private final String city;
    private final String pin_code;
    private final String address;
    private final String latitude;
    private final String longitude;

    protected AddressModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            customer_id = null;
        } else {
            customer_id = in.readInt();
        }
        country = in.readString();
        state = in.readString();
        city = in.readString();
        pin_code = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPin_code() {
        return pin_code;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (customer_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(customer_id);
        }
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(pin_code);
        dest.writeString(address);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
