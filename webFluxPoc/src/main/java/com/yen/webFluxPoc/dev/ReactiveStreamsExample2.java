package com.yen.webFluxPoc.dev;

// https://youtu.be/A3LGEsqtp88?si=tVIJJvEgjrtKf2y0

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;


public class ReactiveStreamsExample2 {

    /** define flow intermedia operation (only need to implement subscribe interface, since we extend SubmissionPublisher already */
    static class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String>{

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("--> processor subscribe OK");
            // request 1 new record from upstream
            this.subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            System.out.println("--> processor receive data");
            // modify received data
            item += " woo ";

            /** NOTE !!! submit modified data */
            submit(item);
            // request 1 new record from upstream
            this.subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }

  public static void main(String[] args) throws InterruptedException {

    /** Step 1) define publisher */
    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    /** Step 2) define intermedia operation (V1)
     *
     *
     *  Flow.Processor<subscribe data type, publish data type>
     */
//    Flow.Processor<String, String> processor = new Flow.Processor<String, String>() {
//        @Override
//        public void subscribe(Flow.Subscriber<? super String> subscriber) {
//
//        }
//
//        @Override
//        public void onSubscribe(Flow.Subscription subscription) {
//
//        }
//
//        @Override
//        public void onNext(String item) {
//
//        }
//
//        @Override
//        public void onError(Throwable throwable) {
//
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    };


      /** Step 2) define intermedia operation (V2)
       *
       */
      MyProcessor myProcessor = new MyProcessor();


    /** Step 3) define subscriber */
    Flow.Subscriber<String> subscriber =
        new Flow.Subscriber<String>() {

          /**
           * NOTE !!!
           *
           * <p>Subscription : 訂閱關係, 綁定發布者, 訂閱者
           */
          private Flow.Subscription subscription;

          @Override
          public void onSubscribe(Flow.Subscription subscription) {

            System.out.println(
                Thread.currentThread().getName() + "----> Subscribe start ..." + subscription);
            this.subscription = subscription;

            this.subscription.request(1);
          }

          @Override
          public void onNext(String item) {

            this.subscription.request(1);

            System.out.println(
                Thread.currentThread().getName() + "----> subscriber receive item ..." + item);
          }

          @Override
          public void onError(Throwable throwable) {

            System.out.println(
                Thread.currentThread().getName()
                    + " subscriber has error ..."
                    + throwable.getMessage());
          }

          @Override
          public void onComplete() {
            System.out.println(Thread.currentThread().getName() + "----> subscriber completed ...");
          }
        };

      /** Step 4) binding publisher, intermedia operator, and subscriber
       *
       *   e.g. :
       *    publisher <-- processor
       *    processor <-- subscriber
       *
       */
      publisher.subscribe(myProcessor);
      myProcessor.subscribe(subscriber);

      // run
      for (int i = 0; i < 10; i++){
          if (i==5){
        //publisher.closeExceptionally(new RuntimeException("some exception"));
          }else{
              publisher.submit("record --" + i);
          }
      }

      // delay main thread termination, so publisher, subscriber thread can work
      Thread.sleep(20000);

  }

}
