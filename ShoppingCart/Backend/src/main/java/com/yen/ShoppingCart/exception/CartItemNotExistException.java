package com.yen.ShoppingCart.exception;

public class CartItemNotExistException extends IllegalArgumentException{

    public CartItemNotExistException(String msg){
        super(msg);
    }

}
