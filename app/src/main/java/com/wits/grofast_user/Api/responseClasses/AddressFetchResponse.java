package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.AddressModel;

import java.util.List;

public class AddressFetchResponse {

    private Integer status;
    private String message;

    @SerializedName("address")
    private List<AddressModel> addressList;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<AddressModel> getAddressList() {
        return addressList;
    }
}
