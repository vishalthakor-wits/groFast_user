package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.AddressAddResponse;
import com.wits.grofast_user.Api.responseClasses.AddressFetchResponse;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddressInterface {
    @GET("fetch-user-address")
    Call<AddressFetchResponse> fetchCustmerAddress();

    @POST("add-user-address")
    Call<AddressAddResponse> addCustomreAddress(@Query("address") String address, @Query("country") String country, @Query("state") String state, @Query("city") String city, @Query("pin_code") String pincode);

    @POST("fetch-user-address")
    Call<LoginResponse> deleteCustomerAddress(@Query("address_id") int addressId);
}