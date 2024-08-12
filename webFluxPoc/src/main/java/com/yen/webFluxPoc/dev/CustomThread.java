package com.yen.webFluxPoc.dev;

// https://youtu.be/dDH8Q9lK6fs?si=ljbE_d4Ox2_4Nxy2

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class CustomThread {
  public static void main(String[] args) throws InterruptedException {

      new CustomThread().thread();

      Thread.sleep(20000);
  }

  public void thread(){

      // by default, main thread is used when publishing, modifying stream (records)
      Flux.range(1,10)
              //.publishOn(Schedulers.immediate()) // NOTE !!! for publisher, execute on a specific thread (subscriber is not affected)
              .publishOn(Schedulers.single())
              .log()
              .map(x -> x + 1)
              .log()
              .subscribe(x ->System.out.println(x));

      // Schedulers (調度器)
      /** NOTE !!! Schedulers (調度器) is a thread pool
       *
       */
//      Schedulers.boundedElastic(); // bounded, 有界, 彈性調度
//
//      Schedulers.immediate(); // no execution context (上下文), current thread does all operations
//
//      Schedulers.single(); // use a fixed single thread does op
  }

}
