package com.yen.webFluxPoc.dev;

// https://youtu.be/H-ijsS-pfgQ?si=xwGf7fjAsxpSp1J0

import java.util.UUID;
import java.util.function.*;

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
    // NOTE !!! function.apply
    System.out.println(function.apply("1"));
    // System.out.println(function.apply("1abc"));

    /**
     * Example 3
     *
     * <p>no input param, no output param
     *
     * <p>runnable
     */
    Runnable runnable = () -> System.out.println(123);
    // runnable.run(); // NOTE !!! DON'T use this, use "new Thread(runnable).start();" instead

    new Thread(runnable).start();

    /**
     * Example 4
     *
     * <p>no input param, has output param
     *
     * <p>supplier
     */
    Supplier<String> supplier = () -> UUID.randomUUID().toString();
    System.out.println(supplier.get());

    /** Example 5 : define custom bi function */
    BiFunction<String, Integer, Long> biFunc = (a, b) -> 8L;

    /** Example 6 : predicate */
    Predicate<Integer> even = (t) -> t % 2 == 0;
    System.out.println(even.test(3));
    System.out.println(even.test(2));
    System.out.println(even.negate().test(2)); // negate : inverse

    /** Example 7 : sequence op */
    myMethod(
        () -> "777",
        str -> str.matches("-?\\d+(\\.\\d+)?"),
        Integer::parseInt,
        integer -> System.out.println(integer));
  }

  private static void myMethod(
      Supplier<String> supplier,
      Predicate<String> isNumber,
      Function<String, Integer> change,
      Consumer<Integer> consumer) {
    if (isNumber.test(supplier.get())) {
      consumer.accept(change.apply(supplier.get()));
    } else {
      System.out.println("Not a valid number !!");
    }
  }
}
