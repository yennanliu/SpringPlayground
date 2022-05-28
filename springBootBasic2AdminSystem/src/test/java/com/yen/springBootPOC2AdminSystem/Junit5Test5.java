package com.yen.springBootPOC2AdminSystem;

// https://www.youtube.com/watch?v=mGf1M9VNDWs&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=77

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

/**
 *  Junit5 Test 5 : Parameterized Test
 */


@DisplayName("my Junit5 Test 6: Nested test")
public class Junit5Test5 {

    @ParameterizedTest
    @DisplayName("parameterized test")
    @ValueSource(ints = {1,2,3,4,5})
    void testParameterized(int i){
        System.out.println(i);
    }

    @ParameterizedTest
    @DisplayName("parameterized test")
    @MethodSource("stringProvider")
    void testParameterized2(String s){
        System.out.println(s);
    }

    static Stream<String> stringProvider(){
        return Stream.of("aaa","bbb");
    }

}
