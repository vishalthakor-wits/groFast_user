package com.wits.grofastUser.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastUser.Api.responseModels.UserModel;

public class EditProfileResponse {
    private int status;
    private String message;

    @SerializedName("user")
    private UserModel userProfile;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public UserModel getUserProfile() {
        return userProfile;
    }
}

