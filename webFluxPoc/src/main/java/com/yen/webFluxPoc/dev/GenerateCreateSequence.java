package com.yen.webFluxPoc.dev;

/** Create sequence demo */
// https://youtu.be/yQvK2PvRuNM?si=IE9D2kDxttNRLtty

import reactor.core.publisher.Flux;

public class GenerateCreateSequence {
  public static void main(String[] args) throws InterruptedException {

      new GenerateCreateSequence().generate();

      Thread.sleep(20000);
  }

  // create sequence via programming way
  // sink : accept record, source : data source
  public void generate() {

    /** V1 : will cause "java.lang.IllegalStateException: More than one call to onNext" error */
    //    Flux<Object> flux =
    //        Flux.generate(
    //            sink -> {
    //              for (int i = 0; i < 100; i++) {
    //                sink.next("record " + i); // send record, may throw run time exception
    //              }
    //            });

    /** V2 */
    Flux<Object> flux =
        Flux.generate(
            () -> 0, // initial value
            (state, sink) -> {
              // ONLY send element when state <= 10
              if (state <= 10) {
                sink.next(sink); // send element out
              } else {
                sink.complete();
              }
              return state + 1; // return init value + 1
            });

    flux.log().subscribe();
  }

}
