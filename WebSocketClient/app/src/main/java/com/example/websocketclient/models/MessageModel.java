package com.example.websocketclient.models;

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
    }
}
