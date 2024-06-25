package com.wits.grofastUser.Api.responseModels;

public class SpinnerItemModel {

  private int id;

  private int countryId;

  private int stateId;

  private int city_id;

  private String code;

  private String name;

  private String created_at;

  private String updated_at;

  public int getId() {
    return id;
  }

  public int getCountryId() {
    return countryId;
  }

  public int getStateId() {
    return stateId;
  }

  public int getCity_id() {
    return city_id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getCreated_at() {
    return created_at;
  }

  public String getUpdated_at() {
    return updated_at;
  }
}
