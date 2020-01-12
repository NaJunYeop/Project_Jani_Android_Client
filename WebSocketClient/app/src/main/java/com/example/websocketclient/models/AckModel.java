package com.example.websocketclient.models;

public class AckModel {

    private int ackType;
    private int msgId;
    private String chatChannel;
    private String ackReceiver;

    public static class Builder implements Buildable {
        private int ackType;
        private int msgId = -1;
        private String chatChannel = "";
        private String ackReceiver = "";

        public Builder(int ackType) {
            this.ackType = ackType;
        }

        public Builder setMsgId(int msgId) {
            this.msgId = msgId;
            return this;
        }

        public Builder setChatChannel(String chatChannel) {
            this.chatChannel = chatChannel;
            return this;
        }

        public Builder setAckReceiver(String ackReceiver) {
            this.ackReceiver = ackReceiver;
            return this;
        }

        @Override
        public AckModel build() {
            return new AckModel(this);
        }
    }

    public AckModel(Builder builder) {
        this.ackType = builder.ackType;
        this.msgId = builder.msgId;
        this.chatChannel = builder.chatChannel;
        this.ackReceiver = builder.ackReceiver;
    }

    public int getAckType() {
        return ackType;
    }

    public void setAckType(int ackType) {
        this.ackType = ackType;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getAckReceiver() {
        return ackReceiver;
    }

    public void setAckReceiver(String ackReceiver) {
        this.ackReceiver = ackReceiver;
    }
}
