package com.example.websocketclient.models;

import java.util.ArrayList;

public class ChatRoomModel {
    private final int QUEUE = 0;
    private final int TOPIC = 1;
    private String senderChatChannel;
    private String chatRoomName;
    private int type;
    private ArrayList<String> participants = new ArrayList<>();
    private ArrayList<MessageModel> messageModels = new ArrayList<>();

    public String getSenderChatChannel() {
        return senderChatChannel;
    }

    public void setSenderChatChannel(String senderChatChannel) {
        this.senderChatChannel = senderChatChannel;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
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

    public MessageModel getMessageModelAt(int position) {
        return this.messageModels.get(position);
    }

    public MessageModel getMessageModelLast() {
        if (messageModels.size() == 0) {
            return new MessageModel();
        }
        else {
            return this.messageModels.get(messageModels.size() - 1);
        }
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
