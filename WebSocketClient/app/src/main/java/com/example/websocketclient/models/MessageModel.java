package com.example.websocketclient.models;

<<<<<<< HEAD
public class MessageModel {
    private String userName;
    private String phoneNumber;
    private String email;

    public static class Builder implements Buildable<MessageModel> {
        private String userName;
        private String phoneNumber;
        private String email = "absent";

        public Builder(String userName, String phoneNumber) {
            this.userName = userName;
            this.phoneNumber = phoneNumber;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public MessageModel build() {
            return new MessageModel(this);
        }
    }
    public MessageModel(Builder builder) {
        this.userName = builder.userName;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
=======
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
>>>>>>> develop
    }
}
