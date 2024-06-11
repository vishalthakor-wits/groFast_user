package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.CategoryResponse;
import com.wits.grofast_user.Api.responseClasses.HomeCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {
    @GET("fetchAllCategories")
    Call<CategoryResponse> fetchCategories();

    @GET("fetchCategoriesHome")
    Call<HomeCategoryResponse> fetchHomeCategories();
}
