package com.yen.springChatRoom.bean;

import java.util.List;

public class Channel {

    private String channelId;
    private List<User> users;

    public Channel() {
    }

    public Channel(String channelId, List<User> users) {
        this.channelId = channelId;
        this.users = users;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
