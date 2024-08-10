package com.yen.webFluxPoc.dev;

// https://youtu.be/dT72MakO5Ig?si=iFEIBI2_qeIyJyYG

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReformRequestAndBuffer {

  public void buffer(){

      Flux<List<Integer>> flux = Flux.range(1, 10)
              /** NOTE !!! set buffer can only buffer 2 elements  */
              .buffer(2);
              //.subscribe(System.out::println);

      flux.subscribe(x -> System.out.println(x + ", data type = " + x.getClass()));

      flux.subscribe(new BaseSubscriber<List<Integer>>() {
          @Override
          protected void hookOnSubscribe(Subscription subscription) {
              //super.hookOnSubscribe(subscription);
              System.out.println("binding OK");
              request(1);
          }

          @Override
          protected void hookOnNext(List<Integer> value) {
              //super.hookOnNext(value);
              System.out.println("element = " + value);
          }
      });

    }

  public static void main(String[] args) {

      /**
       *  Result from below code: (buffer = 2)
       *
       *  [1, 2]
       *  [3, 4]
       *  [5, 6]
       *  [7, 8]
       *  [9, 10]
       */
      new ReformRequestAndBuffer().buffer();
      //request(N); // request data with N times, total record count = N * bufferSize
  }

}
