package com.yen.webFluxPoc.dev;

// https://youtu.be/ripK1QwyOJ4?si=KhKvJ6cRsWmm-uZo

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorAPIDemo {

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

  /** concatMap : elements can concat with other elements */
  /** Mono, Flux are publisher */
  @Test
  public void concatMap() {

    // V1
    Flux.just(1, 2)
        .concatMap(
            x -> {
              return Flux.just(x + "--a", 1, 2);
            })
        .log()
        .subscribe();

    // V2
    Flux.concat(Flux.just(1, 2), Flux.just("a", "b"), Flux.just("c", "d")).log().subscribe();

    // V3
    Flux.just(1, 2)
        .concatWith(Flux.just(4, 5, 6)) // NOTE ! needs to use same type (within two streams)
        .log()
        .subscribe();
  }

  /**
   * transform VS transformDeferred
   *
   *  -> external val is shared or not
   *
   *  -> transform : NO status transformed
   *  -> transformDeferred : has status transformed
   */
  @Test
  public void transform() {

      AtomicInteger atomic = new AtomicInteger(0);

    Flux<String> flux =
        Flux.just("a", "b", "c")
            // transform
            .transformDeferred(
                x -> {
                  // ++ atomic
                  // if 1st call, transform elements in stream to upper case
                  if (atomic.incrementAndGet() == 1) {
                    return x.map(String::toUpperCase);
                  } else {
                    return x;
                  }
                });

    /**
     * NOTE !!!
     *
     * <p>transform (without defer (延遲)) -> external val (atomic in this example) will NOT be shared
     *
     * <p>Sub 1 A Sub 1 B Sub 1 C Sub 2 a Sub 2 b Sub 2 c
     *
     * <p>VS
     *
     * <p>Sub 1 A Sub 1 B Sub 1 C Sub 2 A Sub 2 B Sub 2 C
     */
    flux.subscribe(y -> System.out.println("Sub 1 " + y));
    flux.subscribe(y -> System.out.println("Sub 2 " + y));
  }

  /**
   * defaultIfEmpty VS switchIfEmpty
   *
   *  defaultIfEmpty : static default val
   *  switchIfEmpty : dynamic default val (return new data)
   *
   *
   * <p>NOTE !!
   *
   * <p>- Mono.just(null) : NOT null stream, means stream has a "null" element - Mono.empty() : null
   * stream
   */
  @Test
  public void empty() {

    //    Mono.just("a")
    //            //.log()
    //            .subscribe(v -> System.out.println("v = " + v));

    // if subscribed elements is null, use default val
    MyMethod().defaultIfEmpty("default val").subscribe(v -> System.out.println("v = " + v));

    MyMethod()
        .switchIfEmpty(Mono.just("default val V2"))
        .subscribe(v -> System.out.println("v = " + v));
  }

  Mono<String> MyMethod() {
    // return Mono.just("yooooo");
    // return Mono.just("");
    return Mono.empty(); // NOTE !!! use this way define null stream
  }


  /**
   *  merge
   *
   *
   *  merge VS mergeWith
   */
  @Test
  public void merge() {

      Flux.just(1,2,3)
              .merge(Flux.just(4,5,6), Flux.just(9,10,11))
              .log()
              .subscribe();

      Flux.just(1,2,3)
              .mergeWith(Flux.just(4,5,6))
              .log()
              .subscribe();

  }

  @Test
  public void zip() {}
}
