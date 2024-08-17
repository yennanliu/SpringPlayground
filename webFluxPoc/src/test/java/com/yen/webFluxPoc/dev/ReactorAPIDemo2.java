package com.yen.webFluxPoc.dev;

// https://youtu.be/E-9UjhOu8Ps?si=_r5uf7guFVxGWm8s

import java.time.Duration;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class ReactorAPIDemo2 {

  @Test
  public void retryAndTimeOut() throws InterruptedException {

    // exception : java.util.concurrent.TimeoutException: Did not observe any item or terminal
    // signal within 1000ms in 'concatMapNoPrefetch' (and no fallback has been configured)

    Flux.just(1, 2, 3)
        .delayElements(Duration.ofSeconds(3))
        .log()
        .timeout(Duration.ofSeconds(1)) // timeout
        .retry(3) // retry
        .map(x -> " !!!")
        // .log()
        .subscribe();

    Thread.sleep(20000);
  }

  /**
   * Sinks : stream pipeline (tunnel) data flows with sink
   *
   * <p>Sinks.many(); // send a flux data Sinks.one(); // send a Mono data
   */
  @Test
  public void sinkDemo() throws InterruptedException {

    //    Sinks.many(); // send a flux data
    //    Sinks.one(); // send a Mono data

    //    Sinks.many().unicast(); // 單播 : can only have one subscriber
    //    Sinks.many().multicast(); // 多播 : can only have multiple subscribers
    //    Sinks.many().replay(); // 重放 : can replay (resend) elements

    Sinks.Many<Object> many =
        Sinks.many()
            .unicast() // 單播
            /**
             * 背壓隊列:
             *
             * <p>(onBackpressureBuffer) : max can have 5 elements (similar as limit)
             */
            .onBackpressureBuffer(new LinkedBlockingDeque<>(5));

    // many.asFlux().delayElements(Duration.ofSeconds(1)).subscribe();

    for (int i = 0; i < 10; i++) {
      many.tryEmitNext("record = " + i);
      Thread.sleep(1000);
    }
  }
}
