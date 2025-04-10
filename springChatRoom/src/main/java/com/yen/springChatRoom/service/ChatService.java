package com.yen.springChatRoom.service;

import com.yen.springChatRoom.bean.ChatMessage;
import com.yen.springChatRoom.controller.ChatController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

  private final String PUBLIC_TOPIC = "/topic/public";
  private final String PRIVATE_TOPIC = "/topic/private";

  @Autowired private SimpMessageSendingOperations simpMessageSendingOperations;

  public void sendMsg(@Payload ChatMessage chatMessage) {

    log.info("Send msg by simpMessageSendingOperations:" + chatMessage.toString());
    simpMessageSendingOperations.convertAndSend(PUBLIC_TOPIC, chatMessage);
  }

  public void alertUserStatus(@Payload ChatMessage chatMessage) {

    log.info("Alert user online by simpMessageSendingOperations:" + chatMessage.toString());
    simpMessageSendingOperations.convertAndSend(PUBLIC_TOPIC, chatMessage);
  }

  public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
    log.info("Send private message by simpMessageSendingOperations:" + chatMessage.toString());
    // Send to recipient
    simpMessageSendingOperations.convertAndSendToUser(
        chatMessage.getRecipient(),
        PRIVATE_TOPIC,
        chatMessage
    );
    // Send copy to sender
    simpMessageSendingOperations.convertAndSendToUser(
        chatMessage.getSender(),
        PRIVATE_TOPIC,
        chatMessage
    );
  }
}
