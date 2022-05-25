package com.yen.springBootPOC2AdminSystem;

import org.junit.jupiter.api.*;

// https://www.youtube.com/watch?v=ko75V2ql0Jg&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=72

@DisplayName("my Junit5 Test")
public class Junit5Test {
    @DisplayName("test DisplayName")
    @Test
    void testDisplayName(){
        System.out.println(123);
    }

    @DisplayName("test DisplayName 2")
    @Test
    void testDisplayName2(){
        System.out.println(456);
    }

    @BeforeEach
    void testBeforeEach(){
        System.out.println("test start ...");
    }

    @AfterEach
    void testAfterEach(){
        System.out.println("test end ...");
    }

    @BeforeAll
    static void testBeforeAll(){  // Note !!! we have to make testBeforeAll as static (since it's only called once)
        System.out.println("all tests start ...");
    }

    @AfterAll
    static void testAfterAll(){   // Note !!! we have to make testAfterAll as static (since it's only called once)
        System.out.println("all tests end ...");
    }

}
