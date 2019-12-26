package com.example.websocketclient.models;

public class FriendModel {
    private String friendName;

    public FriendModel(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
