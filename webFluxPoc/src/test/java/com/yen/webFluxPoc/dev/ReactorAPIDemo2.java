package com.yen.webFluxPoc.dev;

// https://youtu.be/E-9UjhOu8Ps?si=_r5uf7guFVxGWm8s

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

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

  @Test
  public void sinkDemo(){
    
  }

}
