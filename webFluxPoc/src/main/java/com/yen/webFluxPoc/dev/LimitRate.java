package com.yen.webFluxPoc.dev;

// https://youtu.be/8YYtCSOkHXU?si=PXfKvNBKC6DP43OZ

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/** Rate Limit (限流) demo */
public class LimitRate {

  // 限流
  /**
   *  NOTE !!!
   *
   *  request(100)
   *  request(75)
   *  request(75)
   *  ....
   *
   *  -> 75 % fetch strategy
   *  -> fetch again when 75% of records are processed
   *  https://youtu.be/8YYtCSOkHXU?si=Pw_qvWSfd2p1M-25&t=490
   *
   *
   */
  public void limit() {
      Flux.range(1, 1000)
              .log() // NOTE !!! need to put log here (before stream rate is limited)
              .limitRate(100) // pre fetch 30 elements
              //.log()
              .subscribe();
//              .subscribe(new BaseSubscriber<Integer>() {
//                  @Override
//                  protected void hookOnSubscribe(Subscription subscription) {
//                      //super.hookOnSubscribe(subscription);
//                      request(1); // fetch 1 time
//                  }
//
//                  @Override
//                  protected void hookOnNext(Integer value) {
//                      //super.hookOnNext(value);
//                      System.out.println("element = " + value);
//                      request(1);
//                  }
//              });
  }

  public static void main(String[] args) {

      // run
      new LimitRate().limit();
  }

}
