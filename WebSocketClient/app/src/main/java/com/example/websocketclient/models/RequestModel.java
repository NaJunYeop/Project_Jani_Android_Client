package com.example.websocketclient.models;

import java.util.ArrayList;

public class RequestModel {
    private String senderName;
    private String receiverName;
    private ArrayList<FriendModel> participants;
    private String status;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<FriendModel> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<FriendModel> participants) {
        this.participants = participants;
    }
}
