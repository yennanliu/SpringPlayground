package com.yen.ShoppingCart.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    ApiResponse successResponse;
    ApiResponse failResponse;

    @BeforeEach
    public void setUp(){

        successResponse = new ApiResponse(true, "success msg");
        failResponse = new ApiResponse(false, "fail msg");
    }

    @Test
    public void shouldReturnTrueIfSuccess(){

        assertEquals(successResponse.isSuccess(), true);
    }

    @Test
    public void shouldReturnFalseIfFail(){

        assertEquals(failResponse.isSuccess(), false);
    }

}