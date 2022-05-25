package com.yen.springBootPOC2AdminSystem;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;


// https://www.youtube.com/watch?v=JoqUZqBSUMc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=75

@DisplayName("my Junit5 Test 3")
public class Junit5Test3 {

    /**
     *  test pre-condition
     */
    @DisplayName("test testAssumptions")
    @Test
    void testAssumptions(){
        Assumptions.assumeTrue(false, "not true");
        //Assumptions.assumeTrue(true, "not true");
        System.out.println("testAssumptions OK");
    }

}
