package com.wits.grofastUser.Api.responseClasses;

import java.lang.Integer;
import java.lang.String;

public class LoginResponse  {

  private String phone_no;

  private String message;

  private Integer status;

  public String getPhone_no() {
    return phone_no;
  }

  public String getMessage() {
    return message;
  }

  public Integer getStatus() {
    return status;
  }
}
