package com.example.websocketclient.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatRoomModel {
    private String chatRoomName;
    private String chatRoomNickName;
    private ArrayList<String> participants;
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

    public ArrayList<MessageModel> getMessageModels() {
        return messageModels;
    }

    public void setMessageModels(ArrayList<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }
}
