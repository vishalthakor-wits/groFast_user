package com.wits.grofast_user.Api.responseModels;

import com.google.gson.annotations.SerializedName;

public class ProductModel {
    private String image;

    private Integer quantity;

    private Integer stock_status;

    private String product_detail;

    private String name;

    private Integer discount;

    private Integer product_status;

    private String product_code;

    private String uuid;

    private Integer tax_id;

    private Integer final_price;

    private Integer category_id;

    private Integer price;

    private Integer return_policy;

    private String commission;

    private Integer id;

    private Integer supplier_id;

    @SerializedName("unit_id")
    private String unitName;

    public String getImage() {
        return image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getStock_status() {
        return stock_status;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Integer getProduct_status() {
        return product_status;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getTax_id() {
        return tax_id;
    }

    public Integer getFinal_price() {
        return final_price;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getReturn_policy() {
        return return_policy;
    }

    public String getCommission() {
        return commission;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getName() {
        return name;
    }

}
