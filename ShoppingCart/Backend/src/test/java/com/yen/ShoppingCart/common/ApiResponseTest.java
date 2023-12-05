package com.yen.ShoppingCart.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    ApiResponse sucessResponse;
    ApiResponse failResponse;

    @BeforeEach
    public void setUp(){

        sucessResponse = new ApiResponse(true, "success msg");
        failResponse = new ApiResponse(false, "fail msg");
    }

    @Test
    public void shouldReturnTrueIfSuccess(){

        assertEquals(sucessResponse.isSuccess(), true);
    }

    @Test
    public void shouldReturnFalseIfFail(){

        assertEquals(failResponse.isSuccess(), false);
    }

}