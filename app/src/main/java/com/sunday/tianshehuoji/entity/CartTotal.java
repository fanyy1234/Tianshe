package com.sunday.tianshehuoji.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/9/17.
 */

public class CartTotal {

    private List<CartItem> cartItem;

    private Object cart;

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public Object getCart() {
        return cart;
    }

    public void setCart(Object cart) {
        this.cart = cart;
    }
}
