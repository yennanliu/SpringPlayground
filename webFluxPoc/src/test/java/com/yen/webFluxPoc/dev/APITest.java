package com.yen.webFluxPoc.dev;

// https://youtu.be/ripK1QwyOJ4?si=KhKvJ6cRsWmm-uZo

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class APITest {

  /** Common Reactor API test (demo) */

  /** filter demo */
  @Test
  public void filter() {

    /**
     * log:
     *
     * <p>20:35:16.469 [main] INFO reactor.Flux.FilterFuseable.1 -- | onSubscribe([Fuseable]
     * FluxFilterFuseable.FilterFuseableSubscriber) 20:35:16.471 [main] INFO
     * reactor.Flux.FilterFuseable.1 -- | request(unbounded) 20:35:16.471 [main] INFO
     * reactor.Flux.FilterFuseable.1 -- | onNext(2) 20:35:16.471 [main] INFO
     * reactor.Flux.FilterFuseable.1 -- | onNext(4) 20:35:16.471 [main] INFO
     * reactor.Flux.FilterFuseable.1 -- | onComplete()
     *
     * <p>Explanation:
     *
     * <p>- onSubscribe : stream is subscribed - request(unbounded) : request un-limit records -
     * onNext(2) : record arrived - onComplete() : stream completed
     */
    Flux.just(1, 2, 3, 4).filter(x -> x % 2 == 0).log().subscribe();
  }

  // flatMap demo
  // https://youtu.be/ripK1QwyOJ4?si=Zu2m92D-awibq0bJ&t=696
  /**
   * - Function : functional interface - T : input element - R : output element public final <R>
   * Flux<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) { return
   * this.flatMap(mapper, Queues.SMALL_BUFFER_SIZE, Queues.XS_BUFFER_SIZE); }
   */
  @Test
  public void flatMap() {

    Flux.just("jack walker", "Jotaro Kujo")
        // split name with space, print all first name, and last name
        .flatMap(
            x -> {
              String[] y = x.split(" ");
              return Flux.fromArray(y); // transform array to a multi element stream
            })
        .log()
        .subscribe();
  }

  // concatMap
  /** Mono, Flux are publisher */
  @Test
  public void concatMap() {

    Flux.just(1, 2)
        .concatMap(
            x -> {
              return Flux.just(x + "--a");
            })
        .log()
        .subscribe();
  }

}
