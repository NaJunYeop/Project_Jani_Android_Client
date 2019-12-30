package com.example.websocketclient.models;

import java.util.StringTokenizer;

public class MessageModel {
    private String senderSideDate = "";
    private String senderName = "";
    private String senderChatChannel = "";
    private String contents = "";

    public MessageModel() {

    }

    public String getSenderSideDate() {
        String ret;
        StringTokenizer stringTokenizer = new StringTokenizer(senderSideDate, " ");

        ret = stringTokenizer.nextToken();

        String time = stringTokenizer.nextToken();

        stringTokenizer = new StringTokenizer(time, ":");

        String hourStr = stringTokenizer.nextToken();
        String minute = stringTokenizer.nextToken();

        int hourInt = Integer.parseInt(hourStr);

        if (hourInt < 12) {
            ret += " 오전 ";
        }
        else {
            hourInt -= 12;
            ret += " 오후 ";
        }

        ret += Integer.toString(hourInt) + ":" + minute;

        return ret;
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

    public String getSenderChatChannel() {
        return senderChatChannel;
    }

    public void setSenderChatChannel(String senderChatChannel) {
        this.senderChatChannel = senderChatChannel;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
