package com.yen.ShoppingCart.exception;

public class OrderNotFoundException extends IllegalArgumentException{

    public OrderNotFoundException(String msg){

        super(msg);
    }

}
