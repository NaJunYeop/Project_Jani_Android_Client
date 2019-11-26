package com.example.websocketclient.models;

public class RegisterModel {
    private String userName;
    private String phoneNumber;
    private String email;

    public static class Builder implements Buildable {
        private String userName;
        private String phoneNumber = "";
        private String email = "";

        public Builder(String userName) {
            this.userName = userName;
        }

        public Builder setPhoneNumber() {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setEmail() {
            this.email = email;
            return this;
        }

        @Override
        public RegisterModel build() {
            return new RegisterModel(this);
        }
    }
    public RegisterModel(Builder builder) {
        this.userName = builder.userName;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
