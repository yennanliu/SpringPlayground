package com.yen.springcloud.service.impl;

// https://www.youtube.com/watch?v=IcNEars4VfM&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=88

/** MessageChannel for RabbitMQ */

import com.yen.springcloud.service.IMessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import java.util.UUID;

/**
 *  NOTE : we don't need @Service annotation here,
 *  since this is (IMessageProvider) NOT a service called by controller
 */
@EnableBinding(Source.class) // define msg push (output) source
public class IMessageProviderImpl implements IMessageProvider {

    private MessageChannel output; // msg output channel

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build()); // create msg constructor
        System.out.println(">>> serial = " + serial);
        //return null;
        return serial;
    }

}
