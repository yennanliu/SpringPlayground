package com.yen.springChatRoom.bean;

import java.util.List;

public class OnlineUser {

    private List<String> users;

    public OnlineUser() {
    }

    public OnlineUser(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

}
