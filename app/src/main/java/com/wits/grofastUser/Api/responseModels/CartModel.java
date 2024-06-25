package com.wits.grofastUser.Api.responseModels;

public class CartModel {
    private Integer amount;

    private Integer quantity;

    private String updated_at;

    private Integer product_id;

    private String created_at;

    private Integer id;

    private Integer customer_id;

    private String uuid;

    private ProductModel product;

    public Integer getAmount() {
        return amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public String getUuid() {
        return uuid;
    }

    public ProductModel getProduct() {
        return product;
    }
}
