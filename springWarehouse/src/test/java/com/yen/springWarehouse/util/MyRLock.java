package com.yen.springWarehouse.util;

import org.junit.jupiter.api.Test;

import com.yen.MyRLock.dev.Util;

import com.yen.MyRLock.core.BusinessKeyProvider;

public class MyRLock {
    @Test
    public void test1(){

        Util util = new Util();
        System.out.println(util.add(1, 2));
        System.out.println(util.substract(1, 2));
        System.out.println(util.product(1, 2));
        System.out.println(util.division(1, 2));
        System.out.println(util.square(10));
        System.out.println(util.sin(10));

        BusinessKeyProvider businessKeyProvider = new BusinessKeyProvider();
        System.out.println(">>> businessKeyProvider = " + businessKeyProvider);
    }

}
