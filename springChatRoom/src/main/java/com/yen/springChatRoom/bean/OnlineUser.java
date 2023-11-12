package com.yen.springChatRoom.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OnlineUser {

    public OnlineUser(){
    }

    public OnlineUser(List<String> users){
        this.users = users;
    }

    private List<String> users;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

}
