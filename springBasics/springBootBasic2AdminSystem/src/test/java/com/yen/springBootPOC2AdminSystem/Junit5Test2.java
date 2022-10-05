package com.yen.springBootPOC2AdminSystem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// https://www.youtube.com/watch?v=4lC90sbQ7a0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=73

/** Note :
 *
 *   1) if one unit test failed, then all of its following tests will NOT be executed
 *
 */

@DisplayName("my Junit5 Test 2")
public class Junit5Test2 {

    @DisplayName("test SimpleAssertions")
    @Test
    void testSimpleAssertions(){
        int res1 = cal(1,2);
        assertEquals(res1, 3);
        //assertEquals(res1, 10, "testSimpleAssertions asert failed !!!");
    }

    int cal(int i, int j){
        return i + j;
    }

    @Disabled
    @DisplayName("test SimpleAssertions 2")
    @Test
    void testSimpleAssertions2(){
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertSame(obj1, obj2, "two obj are different !!");
    }

    @DisplayName("test testArrayEquals")
    @Test
    void testArrayEquals(){
        assertArrayEquals(new int[]{1,2}, new int[]{1,2});
    }

    /**
     * CombinationTests
     * -> all sub tests must succeed
     */
    @DisplayName("test testCombinationTests")
    @Test
    void testCombinationTests(){
        assertAll("test",
                () -> assertTrue(true && true),
                () -> assertEquals(1, 1)
        );
    }

    @DisplayName("test testAssertThrows")
    @Test
    void testAssertThrows(){
        assertThrows(
                ArithmeticException.class,
                () -> {int i = 1/0;},
                "logic runs ok ??!");
    }

    @DisplayName("test testQuickFail")
    @Test
    void testQuickFail(){
        if (1==2){
            fail("quick fail");
        }
    }

}
