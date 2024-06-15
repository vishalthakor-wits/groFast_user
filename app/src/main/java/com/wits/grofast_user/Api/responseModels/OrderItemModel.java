package com.wits.grofast_user.Api.responseModels;

import com.google.gson.annotations.SerializedName;

public class OrderItemModel {

    private Integer quantity;

    private String updated_at;

    private Integer price;

    private Integer subtotal;

    private Integer product_id;

    private Integer customer_order_id;

    private String created_at;

    private Integer id;

    private String uuid;

    @SerializedName("product")
    private ProductModel product;

    public Integer getQuantity() {
        return quantity;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Integer getCustomer_order_id() {
        return customer_order_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public ProductModel getProduct() {
        return product;
    }
}
