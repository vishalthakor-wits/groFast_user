// OtpVerifyResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.wits.grofast_user.Api.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_user.Api.responseModels.UserModel;

public class OtpVerifyResponse {
   @SerializedName("access_token")
    private String accessToken;
    private String message;
    private UserModel user;
    private long status;

    public String getAccessToken() {
        return accessToken;
    }

    public String getMessage() {
        return message;
    }

    public UserModel getUser() {
        return user;
    }

    public long getStatus() {
        return status;
    }
}

