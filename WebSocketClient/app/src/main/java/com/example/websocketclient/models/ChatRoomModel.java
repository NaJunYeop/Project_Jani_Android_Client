package com.example.websocketclient.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatRoomModel {
    private final int QUEUE = 0;
    private final int TOPIC = 1;
    private String chatRoomName;
    private String chatRoomNickName;
    private String senderName;
    private int type;
    private ArrayList<FriendModel> participants;
    private ArrayList<MessageModel> messageModels;

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getChatRoomNickName() {
        return chatRoomNickName;
    }

    public void setChatRoomNickName(String chatRoomNickName) {
        this.chatRoomNickName = chatRoomNickName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<MessageModel> getMessageModels() {
        return messageModels;
    }

    public void setMessageModels(ArrayList<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    public ArrayList<FriendModel> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<FriendModel> participants) {
        this.participants = participants;
    }
}
