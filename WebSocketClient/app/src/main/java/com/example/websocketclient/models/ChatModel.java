package com.example.websocketclient.models;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;

import java.util.List;

public class ChatModel {

    private ChatRoomModel chatRoomModel;
    private List<MessageModel> messageModels;
    private List<ParticipantModel> participantModels;

    public ChatModel(ChatRoomModel chatRoomModel, List<MessageModel> messageModels, List<ParticipantModel> participantModels) {
        this.chatRoomModel = chatRoomModel;
        this.messageModels = messageModels;
        this.participantModels = participantModels;
    }

    public ChatRoomModel getChatRoomModel() {
        return chatRoomModel;
    }

    public void setChatRoomModel(ChatRoomModel chatRoomModel) {
        this.chatRoomModel = chatRoomModel;
    }

    public List<MessageModel> getMessageModels() {
        return messageModels;
    }

    public void setMessageModels(List<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    public List<ParticipantModel> getParticipantModels() {
        return participantModels;
    }

    public void setParticipantModels(List<ParticipantModel> participantModels) {
        this.participantModels = participantModels;
    }

    public MessageModel getMessageModelAt(int position) {
        return messageModels.get(position);
    }

    public ParticipantModel getParticipantModelAt(int position) {
        return participantModels.get(position);
    }

    public MessageModel getLastMessageModel() {
        return getMessageModelAt(messageModels.size() - 1);
    }

    public String getLastMessage() {
        if (messageModels.size() == 0) {
            return "대화 내용이 없습니다.";
        }
        else {
            return messageModels.get(messageModels.size() - 1).getMsgContent();
        }
    }

    public String getLastSenderSideDate() {
        if (messageModels.size() == 0) {
            return "";
        }
        else {
            return messageModels.get(messageModels.size() - 1).getMsgSenderSideDate();
        }
    }
}
