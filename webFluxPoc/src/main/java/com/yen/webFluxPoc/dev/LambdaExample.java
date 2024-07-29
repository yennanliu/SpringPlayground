package com.yen.webFluxPoc.dev;

// https://youtu.be/H-ijsS-pfgQ?si=xwGf7fjAsxpSp1J0

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaExample {
  public static void main(String[] args) {

    /**
     * Example 1
     *
     * <p>has input param, no output param
     *
     * <p>function.accept
     */
    // NOTE !!! java.util.function.BiConsumer
    // BiConsumer : accept 2 param
    BiConsumer<String, String> consumer =
        (a, b) -> {
          System.out.println("input =  " + a + ", " + b);
        };
    // NOTE !!! consumer.accept
    consumer.accept("1", "2");

    /**
     * Example 2
     *
     * <p>has input param, has output param
     *
     * <p>function.apply
     */
    Function<String, Integer> function = (String x) -> Integer.parseInt(x);
    // NOTE !!! funciton.apply
    System.out.println(function.apply("1"));
    // System.out.println(function.apply("1abc"));

    /**
     * Example 3
     *
     * <p>no input param, no output param
     *
     * <p> runnable
     */
    Runnable runnable = () -> System.out.println(123);
    // runnable.run(); // NOTE !!! DON'T use this, use "new Thread(runnable).start();" instead

    new Thread(runnable).start();

    /**
     * Example 4
     *
     * <p>no no param, has output param
     *
     * <p> supplier
     */
    Supplier<String> supplier = () -> UUID.randomUUID().toString();
    System.out.println(supplier.get());
  }

}
