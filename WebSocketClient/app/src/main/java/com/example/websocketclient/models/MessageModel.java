package com.example.websocketclient.models;

public class MessageModel {
    private String senderSideDate;
    private String senderName;
    private String chatRoomName;
    private String contents;

    public MessageModel() {

    }

    public String getSenderSideDate() {
        // 날짜를 원하는 형태로 변환해서 Return 해야한다.
        return senderSideDate;
    }

    public void setSenderSideDate(String senderSideDate) {
        this.senderSideDate = senderSideDate;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
