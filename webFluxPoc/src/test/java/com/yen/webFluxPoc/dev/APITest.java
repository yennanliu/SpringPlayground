package com.yen.webFluxPoc.dev;

// https://youtu.be/ripK1QwyOJ4?si=KhKvJ6cRsWmm-uZo

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class APITest {

    /**
     * Common Reactor API test (demo)
     */

    /** filter demo */
    @Test
    public void filter(){

        /**
         *  log:
         *
         * 20:35:16.469 [main] INFO reactor.Flux.FilterFuseable.1 -- | onSubscribe([Fuseable] FluxFilterFuseable.FilterFuseableSubscriber)
         * 20:35:16.471 [main] INFO reactor.Flux.FilterFuseable.1 -- | request(unbounded)
         * 20:35:16.471 [main] INFO reactor.Flux.FilterFuseable.1 -- | onNext(2)
         * 20:35:16.471 [main] INFO reactor.Flux.FilterFuseable.1 -- | onNext(4)
         * 20:35:16.471 [main] INFO reactor.Flux.FilterFuseable.1 -- | onComplete()
         *
         *
         *
         *  Explanation:
         *
         *  - onSubscribe : stream is subscribed
         *  - request(unbounded) : request un-limit records
         *  - onNext(2) : record arrived
         *  - onComplete() : stream completed
         */
        Flux.just(1,2,3,4)
                .filter(x -> x % 2 == 0)
                .log()
                .subscribe();
    }



}
