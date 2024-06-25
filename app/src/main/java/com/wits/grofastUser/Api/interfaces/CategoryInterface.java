package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.CategoryResponse;
import com.wits.grofastUser.Api.responseClasses.HomeCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {
    @GET("fetchAllCategories")
    Call<CategoryResponse> fetchCategories();

    @GET("fetchCategoriesHome")
    Call<HomeCategoryResponse> fetchHomeCategories();
}
