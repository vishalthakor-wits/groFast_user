package com.wits.grofastUser.Api.responseClasses;

import com.wits.grofastUser.Api.responseModels.CartModel;

public class AddToCartResponse {
    private String message;

    private CartModel cart_item;

    public String getMessage() {
        return message;
    }

    public CartModel getCart_item() {
        return cart_item;
    }
}
