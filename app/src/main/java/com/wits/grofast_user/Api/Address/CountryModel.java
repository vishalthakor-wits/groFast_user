package com.wits.grofast_user.Api.Address;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;

public class CountryModel {
  private String code;

  private String updated_at;

  private String name;

  private String created_at;

  private int id;

  public String getCode() {
    return code;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public String getName() {
    return name;
  }

  public String getCreated_at() {
    return created_at;
  }

  public int getId() {
    return id;
  }
}
