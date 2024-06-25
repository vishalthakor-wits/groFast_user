package com.wits.grofastUser.Api.responseModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ProductModel implements Parcelable {
    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
    private final String image;
    private final Integer quantity;
    private final Integer stock_status;
    private final String product_detail;
    private final String name;
    private final Integer discount;
    @SerializedName("product_status")
    private OrderStatusModel productStatus;
    private final String product_code;
    private final String uuid;
    private final Integer tax_id;
    private final Integer final_price;
    private final Integer category_id;
    private final Integer price;
    private final Integer return_policy;
    private final String commission;
    private final Integer id;
    private final int per;
    private final Integer supplier_id;
    @SerializedName("unit_id")
    private final String unitName;

    protected ProductModel(Parcel in) {
        image = in.readString();
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        if (in.readByte() == 0) {
            stock_status = null;
        } else {
            stock_status = in.readInt();
        }
        product_detail = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            discount = null;
        } else {
            discount = in.readInt();
        }
        product_code = in.readString();
        uuid = in.readString();
        if (in.readByte() == 0) {
            tax_id = null;
        } else {
            tax_id = in.readInt();
        }
        if (in.readByte() == 0) {
            final_price = null;
        } else {
            final_price = in.readInt();
        }
        if (in.readByte() == 0) {
            category_id = null;
        } else {
            category_id = in.readInt();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        if (in.readByte() == 0) {
            return_policy = null;
        } else {
            return_policy = in.readInt();
        }
        commission = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        per = in.readInt();
        if (in.readByte() == 0) {
            supplier_id = null;
        } else {
            supplier_id = in.readInt();
        }
        unitName = in.readString();
    }

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

    public int getPer() {
        return per;
    }

    public OrderStatusModel getProductStatus() {
        return productStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(image);
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        if (stock_status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stock_status);
        }
        dest.writeString(product_detail);
        dest.writeString(name);
        if (discount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(discount);
        }
        dest.writeString(product_code);
        dest.writeString(uuid);
        if (tax_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(tax_id);
        }
        if (final_price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(final_price);
        }
        if (category_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(category_id);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        if (return_policy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(return_policy);
        }
        dest.writeString(commission);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeInt(per);
        if (supplier_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(supplier_id);
        }
        dest.writeString(unitName);
    }
}
