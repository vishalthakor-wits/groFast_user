package com.wits.grofastUser.Api.interfaces;

import com.wits.grofastUser.Api.responseClasses.HomeProductResponse;
import com.wits.grofastUser.Api.responseClasses.ProductResponse;
import com.wits.grofastUser.Api.responseClasses.RelatedProductsResponse;

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
    Call<RelatedProductsResponse> fetchRelatedProducts(@Query("categoryId") int categoryId,@Query("productId") int productId);

    @POST("search-products")
    Call<ProductResponse> searchProduct(@Query("product_name") String ProductName,@Query("page") int page);
}
