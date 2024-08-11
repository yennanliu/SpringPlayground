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

    Flux<Object> flux =
        Flux.generate(
            sink -> {
              for (int i = 0; i < 100; i++) {
                sink.next("record " + i); // send record, may throw run time exception
              }
            });

    flux.log().subscribe();
  }

}
