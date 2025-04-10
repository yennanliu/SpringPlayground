package com.yen.springChatRoom.controller;

import com.yen.springChatRoom.bean.ChatMessage;
import com.yen.springChatRoom.bean.Message;
import com.yen.springChatRoom.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Slf4j
@Controller
public class ChatController {

  final String onlineUserKey = "websocket.onlineUsers";
  private final String USER_NAME = "username";
  @Value("${redis.channel.msgToAll}")
  private String msgToAll;
  @Value("${redis.set.onlineUsers}")
  private String onlineUsers;
  @Value("${redis.channel.userStatus}")
  private String userStatus;
  @Value("${redis.channel.private}")
  private String privateChannel;
  // private RedisTemplate redisTemplate;
  // TODO : check difference ? RedisTemplate VS RedisTemplate<String, String>
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private SimpMessagingTemplate simpMessagingTemplate;

  /** single mode : read msg from FE, and send to other users (@SendTo("/topic/public")) directly */
  //    @MessageMapping("/chat.sendMessage")
  //    @SendTo("/topic/public")
  //    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
  //
  //        return chatMessage;
  //    }

  /**
   * cluster mode : read msg from FE, but NOT send to other users, instead, send to Redis channel,
   * so the other service on cluster can read/digest the msg
   */
  @MessageMapping("/chat.sendMessage")
  public void sendMessage(@Payload ChatMessage chatMessage) {
    try {

      // test : save msg to redis
      redisTemplate.opsForSet().add(msgToAll, JsonUtil.parseObjToJson(chatMessage));
      // redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage)));
      redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
    } catch (Exception e) {
      log.error("send msg error : " + e.getMessage(), e);
    }
  }

  @MessageMapping("/chat.addUser")
  public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

    log.info("User added in Chatroom:" + chatMessage.getSender());
    try {
      headerAccessor.getSessionAttributes().put(USER_NAME, chatMessage.getSender());
      redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
      redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
      // TODO : show online user
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  // TODO : check @DestinationVariable ?
  @RequestMapping("/app/private/{username}")
  public void handlePrivateMessage(@DestinationVariable String username, Message message) {

    log.info("handlePrivateMessage : username = " + username + " message = " + message);
    // save to redis
    // redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
    redisTemplate
        .opsForSet()
        .add(privateChannel + "." + username, JsonUtil.parseObjToJson(message));
    simpMessagingTemplate.convertAndSendToUser(username, "/topic/private", message);
  }

  @MessageMapping("/chat.private")
  public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
    try {
      String recipient = chatMessage.getRecipient();
      if (recipient != null && !recipient.isEmpty()) {
        chatMessage.setType(ChatMessage.MessageType.PRIVATE);
        
        // Save private message to Redis
        String privateChannelKey = privateChannel + "." + recipient + "." + chatMessage.getSender();
        redisTemplate.opsForSet().add(privateChannelKey, JsonUtil.parseObjToJson(chatMessage));
        
        // Send to specific user
        simpMessagingTemplate.convertAndSendToUser(
            recipient,
            "/topic/private",
            chatMessage
        );
        
        // Send a copy to sender
        simpMessagingTemplate.convertAndSendToUser(
            chatMessage.getSender(),
            "/topic/private",
            chatMessage
        );
        
        log.info("Private message sent from {} to {}", chatMessage.getSender(), recipient);
      }
    } catch (Exception e) {
      log.error("Error sending private message: " + e.getMessage(), e);
    }
  }

  // Get private chat history
  @MessageMapping("/chat.getPrivateHistory")
  public void getPrivateMessageHistory(@Payload ChatMessage chatMessage) {
    try {
      String user1 = chatMessage.getSender();
      String user2 = chatMessage.getRecipient();
      
      // Get messages from both directions
      String channelKey1 = privateChannel + "." + user1 + "." + user2;
      String channelKey2 = privateChannel + "." + user2 + "." + user1;
      
      Set<String> messages1 = redisTemplate.opsForSet().members(channelKey1);
      Set<String> messages2 = redisTemplate.opsForSet().members(channelKey2);
      
      // Send history to requesting user
      if (messages1 != null) {
        for (String msg : messages1) {
          ChatMessage message = JsonUtil.parseJsonToObj(msg, ChatMessage.class);
          simpMessagingTemplate.convertAndSendToUser(
              user1,
              "/topic/private.history",
              message
          );
        }
      }
      
      if (messages2 != null) {
        for (String msg : messages2) {
          ChatMessage message = JsonUtil.parseJsonToObj(msg, ChatMessage.class);
          simpMessagingTemplate.convertAndSendToUser(
              user1,
              "/topic/private.history",
              message
          );
        }
      }
      
      log.info("Private chat history sent for users {} and {}", user1, user2);
    } catch (Exception e) {
      log.error("Error getting private message history: " + e.getMessage(), e);
    }
  }
}
