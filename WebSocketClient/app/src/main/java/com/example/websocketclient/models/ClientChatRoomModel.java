package com.example.websocketclient.models;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;

public class ClientChatRoomModel {

    private ChatRoomModel chatRoomModel;
    private MessageModel messageModel;
    private ParticipantModel participantModel;

    public ClientChatRoomModel(ChatRoomModel chatRoomModel, MessageModel messageModel, ParticipantModel participantModel) {
        this.chatRoomModel = chatRoomModel;
        this.messageModel = messageModel;
        this.participantModel = participantModel;
    }

    public ChatRoomModel getChatRoomModel() {
        return chatRoomModel;
    }

    public void setChatRoomModel(ChatRoomModel chatRoomModel) {
        this.chatRoomModel = chatRoomModel;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public ParticipantModel getParticipantModel() {
        return participantModel;
    }

    public void setParticipantModel(ParticipantModel participantModel) {
        this.participantModel = participantModel;
    }
}
