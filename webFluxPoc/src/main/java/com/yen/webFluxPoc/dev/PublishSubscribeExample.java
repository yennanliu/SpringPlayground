package com.yen.webFluxPoc.dev;

/**
 * Publisher, subscriber example (觀察者模式)
 *
 * <p>ReactiveStream
 */

// https://youtu.be/Z_P08XLnQ8E?si=PVRkHWCrzdMAl7Vg
// https://youtu.be/_3oFXO7crcE?si=G10cGW-J3bvc22WE

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class PublishSubscribeExample {
  public static void main(String[] args) throws InterruptedException {

    /** Define a publisher that publishes data (V1) */
    //      Flow.Publisher<String> publisher = new Flow.Publisher<String>() {
    //
    //          // TODO : check difference between below and private Flow.Subscriber<String>
    // subscriber;
    //          private Flow.Subscriber<? super String> subscriber;
    //
    //          // subscriber will subscribe this method, NOTE !!! subscriber as input parameter
    //          @Override
    //          public void subscribe(Flow.Subscriber<? super String> subscriber) {
    //              this.subscriber = subscriber;
    //              //System.out.println("123");
    //          }
    //      };

    /** Define a publisher that publishes data (V2) */
    SubmissionPublisher publisher = new SubmissionPublisher();

    /** Define a subscriber that receives data */
    Flow.Subscriber<String> subscriber =
        new Flow.Processor<>() {

          /**
           * NOTE !!!
           *
           * <p>Subscription : 訂閱關係, 綁定發布者, 訂閱者
           */
          private Flow.Subscription subscription;

          /**
           * NOTE !!!
           *
           * <p>OnXXX : do recall (run something) when XXX happened
           */
          @Override
          public void onSubscribe(Flow.Subscription subscription) {
            System.out.println(
                Thread.currentThread().getName() + "----> Subscribe start ..." + subscription);
            this.subscription = subscription;
            /**
             * NOTE !!!
             *
             * <p>NEED to make subscription can request item, so subscriber can read data (request
             * data from upstream) https://youtu.be/_3oFXO7crcE?si=ODmQ6oS17RLGdFun&t=566
             */
            this.subscription.request(1);
          }

          // when next item comes (e.g. receive new data)
          @Override
          public void onNext(String item) {
            /**
             * NOTE !!!
             *
             * <p>WE NEED TO REQUEST 1 NEW RECORD when every onNext happened, so subscriber has new
             * data to read
             *
             * <p>SO, we need to add "this.subscription.request(1);" on "onSubscribe" and "onNext"
             */
            this.subscription.request(1);

            System.out.println(
                Thread.currentThread().getName() + "----> subscriber receive item ..." + item);
          }

          // when has error
          @Override
          public void onError(Throwable throwable) {
            System.out.println(
                Thread.currentThread().getName()
                    + " subscriber has error ..."
                    + throwable.getMessage());
          }

          // when completed
          @Override
          public void onComplete() {
            System.out.println(Thread.currentThread().getName() + "----> subscriber completed ...");
          }

          @Override
          public void subscribe(Flow.Subscriber<? super Object> subscriber) {}
        };

    /**
     * NOTE !!! binding publisher and subscriber
     *
     * <p>(NEED TO bind publisher, subscriber first, then publish data
     *
     * <p>記憶法: publisher <---subscribe-- subscriber
     */
    publisher.subscribe(subscriber);

    // publisher 10 records
    // NOTE !!! data is saved to buffer (緩衝區)
    for (int i = 0; i < 10; i++) {
      System.out.println(Thread.currentThread().getName() + " ---> publish data");
      publisher.submit("data = " + i);
    }

    // delay main thread termination, so publisher, subscriber thread can work
    Thread.sleep(20000);
  }
}
