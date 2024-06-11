package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.AddressModel;

public class AddressAddResponse {
    private Integer status;
    private String message;

    @SerializedName("address")
    private AddressModel addressModel;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }
}
