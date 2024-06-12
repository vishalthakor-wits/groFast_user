package com.wits.grofast_user.Api.interfaces;

import com.wits.grofast_user.Api.responseClasses.HomeProductResponse;
import com.wits.grofast_user.Api.responseClasses.ProductResponse;
import com.wits.grofast_user.Api.responseClasses.RelatedPeoductsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductInerface {
    @GET("fetchProducts")
    Call<ProductResponse> fetchProducts(@Query("page") int page);

    @POST("fetchProductsByCategory")
    Call<ProductResponse> fetchProductsByCategory(@Query("page") int page, @Query("category") String category);

    @GET("fetchProductsHome")
    Call<HomeProductResponse> fetchHomeProducts();

    @POST("fetch-related-products")
    Call<RelatedPeoductsResponse> fetchRelatedProducts(@Query("categoryId") int categoryId);
}
