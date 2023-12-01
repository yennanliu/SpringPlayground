package com.yen.ShoppingCart.exception;

public class ProductNotExistException extends IllegalArgumentException  {

    public ProductNotExistException(String msg){
        super(msg);
    }

}
