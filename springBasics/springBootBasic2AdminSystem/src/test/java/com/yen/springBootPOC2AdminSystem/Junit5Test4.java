package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=X_nRTn9NRck&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=76
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springboot2Course/boot-05-web-admin/src/test/java/com/atguigu/admin/TestingAStackDemo.java

/**
 *  Junit5 Test 4 : Nested test
 */

import org.junit.jupiter.api.*;

import java.util.EmptyStackException;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("my Junit5 Test 4: Nested test")
public class Junit5Test4 {

    Stack<Object> stack;

    @Test
    @DisplayName("new Stack()")
    void isInstantiatedWithNew() {
        new Stack<>();
        /**
         *  嵌套测试情况下，外层的Test不能驱动内层的Before(After)Each/All之类的方法提前/之后运行
         */
        assertNull(stack);
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {

        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            assertThrows(EmptyStackException.class, stack::pop);
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        void throwsExceptionWhenPeeked() {
            assertThrows(EmptyStackException.class, stack::peek);
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {

            String anElement = "an element";

            @BeforeEach
            void pushAnElement() {
                stack.push(anElement);
            }

            /**
             * 内层的Test可以驱动外层的Before(After)Each/All之类的方法提前/之后运行
             */
            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty() {
                assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                assertEquals(anElement, stack.pop());
                assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked() {
                assertEquals(anElement, stack.peek());
                assertFalse(stack.isEmpty());
            }
        }
    }


    //
//    Stack<Object> stack;
//
//    @Test
//    @DisplayName("is instantiated with new stack()")
//    void instantiatedWithNew(){
//        new Stack<>();
//    }
//
//    @Nested
//    @DisplayName("when new")
//    class WhenNew{
//
//        @BeforeEach
//        void createNewStack(){
//            stack =  new Stack<>();
//        }
//
//        @Test
//        @DisplayName("is empty")
//        void isEmpty(){
//            assertTrue(stack.isEmpty());
//        }
//
//        @Test
//        @DisplayName("throws EmptyStackException when popped")
//        void throwsExceptionWhenPopped(){
//            assertThrows(EmptyStackException.class, stack::pop);
//        }
//
//        @Test
//        @DisplayName("throws EmptyStackException when peeked")
//        void throwsExceptionWhenPeeked(){
//            assertThrows(EmptyStackException.class, stack::peek);
//        }
//
//        @Nested
//        @DisplayName("after pushing an element")
//        class AfterPushing{
//
//            String anElement = "an element";
//
//        }
//
//    }


}
