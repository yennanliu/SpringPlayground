package com.yen.ShoppingCart.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    class MyClass{};
    Helper helper = null;
    MyClass myClass = null;

    @BeforeEach
    public void setUp(){

        helper = new Helper();
        myClass = new MyClass();
    }

    @Test
    public void shouldReturnTrueIfNotNull(){

        assertSame(helper.notNull(myClass), true);
    }

    @Test
    public void shouldReturnFalseIfNull(){

        assertSame(helper.notNull(null), false);
    }

}