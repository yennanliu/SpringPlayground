package springcloud.controller;

// https://www.youtube.com/watch?v=b-g8YJ4Z2-c&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=89

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {

    @Value("{server.port}")
    private String serverPort;  // 8803

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message){
        System.out.println(">>> consumer 1 ---> msg = " + message.getPayload() + " port = " + serverPort);
    }

}
