package com.yen.webFluxPoc.dev;

/** Generate, Create sequence demo */
// https://youtu.be/yQvK2PvRuNM?si=IE9D2kDxttNRLtty
// https://youtu.be/yQvK2PvRuNM?si=fN0mXU4wIPzURQ0P

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class GenerateCreateSequence {
  public static void main(String[] args) throws InterruptedException {

    // new GenerateCreateSequence().generate();
    new GenerateCreateSequence().create();

    Thread.sleep(20000);
  }

  /**
   * generate : create sequence via programming way
   *
   * <p>NOTE !!! generate CAN'T be used in multi thread
   */
  // sink : accept record, source : data source
  public void generate() {

    /**
     * V1 (generate) : will cause "java.lang.IllegalStateException: More than one call to onNext"
     * error
     */
    //    Flux<Object> flux =
    //        Flux.generate(
    //            sink -> {
    //              for (int i = 0; i < 100; i++) {
    //                sink.next("record " + i); // send record, may throw run time exception
    //              }
    //            });

    /** V2 (generate) */
    Flux<Object> flux =
        Flux.generate( // NOTE !!! : Flux.generate
            () -> 0, // initial value
            (state, sink) -> { // NOTE !!! generate CAN'T be used in multi thread
              // ONLY send element when state <= 10
              if (state <= 10) {
                sink.next(sink); // send element out
              } else {
                sink.complete();
              }
              return state + 1; // return new val
            });

    // subscriber implements disposable (可取消)
    // Disposable disposable = flux.log().subscribe();
    flux.log().subscribe();
  }

  /**
   * create : create sequence via programming way
   *
   * <p>NOTE !!! create CAN be used in multi thread
   */
  public void create() throws InterruptedException {

    // MyListener listener = new MyListener();
    // AtomicReference<MyListener> listener  = null;

    Flux.create(
            fluxSink -> {
              MyListener listener = new MyListener(fluxSink);
              // listener.set(new MyListener(fluxSink));
              for (int i = 0; i < 100; i++) {
                listener.online("Iori Yagami " + i);
                //            int finalI = i;
                //            new Thread(
                //                    () -> {
                //                      //listener.get().online("user " + finalI);
                //                    })
                //                .start();
                //            try {
                //              Thread.sleep(1000);
                //            } catch (InterruptedException e) {
                //              throw new RuntimeException(e);
                //            }
              }
              fluxSink.next("record zz");
            })
        .log()
        .subscribe();
  }

  class MyListener {
    // use login, trigger online event
    FluxSink<Object> sink;

    // constructor
    public MyListener(FluxSink<Object> sink) {
      this.sink = sink;
    }

    public void online(Object userName) {
      System.out.println("user login : " + userName);
      sink.next(userName); // send userName out
    }
  }
}
