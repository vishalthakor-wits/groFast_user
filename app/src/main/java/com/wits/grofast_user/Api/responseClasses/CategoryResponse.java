package com.wits.grofast_user.Api.responseClasses;

import com.wits.grofast_user.Api.responseModels.CategoryModel;

import java.lang.Integer;
import java.lang.String;
import java.util.List;

public class CategoryResponse {

  private String message;

  private Integer status;

  private List<CategoryModel> categories;

  public String getMessage() {
    return message;
  }

  public Integer getStatus() {
    return status;
  }

  public List<CategoryModel> getCategories() {
    return categories;
  }
}
