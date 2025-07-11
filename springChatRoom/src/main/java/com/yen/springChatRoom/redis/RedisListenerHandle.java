package com.yen.springChatRoom.redis;

import com.yen.springChatRoom.bean.ChatMessage;
import com.yen.springChatRoom.service.ChatService;
import com.yen.springChatRoom.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/** Class for Redis channel msg handling */
@Slf4j
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

  @Value("${redis.channel.msgToAll}")
  private String msgToAll;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  @Value("${redis.channel.private}")
  private String privateChannel;

  @Value("${server.port}")
  private String serverPort;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Autowired private ChatService chatService;

  @Override
  public void onMessage(Message message, byte[] bytes) {
    byte[] body = message.getBody();
    byte[] channel = message.getChannel();
    String rawMsg;
    String topic;
    try {
      rawMsg = redisTemplate.getStringSerializer().deserialize(body);
      topic = redisTemplate.getStringSerializer().deserialize(channel);
      log.info("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return;
    }

    if (msgToAll.equals(topic)) {
      log.info("Send message to all users:" + rawMsg);
      ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
      if (chatMessage != null) {
        chatService.sendMsg(chatMessage);
      }
    } else if (userStatus.equals(topic)) {
      ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
      if (chatMessage != null) {
        chatService.alertUserStatus(chatMessage);
      }
    } else if (topic != null && topic.startsWith(privateChannel)) {
      ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
      if (chatMessage != null && chatMessage.getType() == ChatMessage.MessageType.PRIVATE) {
        chatService.sendPrivateMessage(chatMessage);
      }
    } else {
      log.warn("No further operation with this topic!");
    }
  }

  //    @Override
  //    public void onMessage(Message message, byte[] pattern) {
  //
  //        // super.onMessage(message, pattern);
  //        byte[] body = message.getBody();
  //        byte[] channel = message.getChannel();
  //        String rawMsg;
  //        String topic;
  //
  //        try{
  //            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
  //            topic = redisTemplate.getStringSerializer().deserialize(channel);
  //            LOGGER.info("Receive raw msg from topic : " + topic + " , raw msg  : " + rawMsg);
  //        }catch (Exception e){
  //            LOGGER.error("Receive raw msg failed : " + e.getMessage() + e);
  //            return; // TODO : return custom error msg instead
  //        }
  //
  //        if (msgToAll.equals(topic)){
  //            LOGGER.info("Send msg to all users : " + rawMsg + ", topic = " + topic);
  //            ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
  //            // send msg to all online users
  //            chatService.sendMsg(chatMessage);
  //        }else{
  //            LOGGER.warn("Not sending msg to all user with topic : " + topic);
  //        }

}
