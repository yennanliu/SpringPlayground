package com.yen.springChatRoom.bean;

public class ChatMessage {
  private MessageType type;
  private String content;
  private String sender;
  private String recipient;

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  @Override
  public String toString() {
    return "ChatMessage{"
        + "type="
        + type
        + ", content='"
        + content
        + '\''
        + ", sender='"
        + sender
        + '\''
        + ", recipient='"
        + recipient
        + '\''
        + '}';
  }

  public enum MessageType {
    CHAT,
    JOIN,
    LEAVE,
    PRIVATE
  }
}
