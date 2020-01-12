package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "participant_model")
public class ParticipantModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "participant_id")
    private int participantId;

    @ColumnInfo(name = "participant_user_name")
    private String participantUserName;

    @ColumnInfo(name = "chat_channel")
    private String chatChannel;

    @ColumnInfo(name = "participant_owner")
    private String participantOwner;

    public ParticipantModel(String participantUserName, String chatChannel, String participantOwner) {
        this.participantUserName = participantUserName;
        this.chatChannel = chatChannel;
        this.participantOwner = participantOwner;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String getParticipantUserName() {
        return participantUserName;
    }

    public void setParticipantUserName(String participantUserName) {
        this.participantUserName = participantUserName;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getParticipantOwner() {
        return participantOwner;
    }

    public void setParticipantOwner(String participantOwner) {
        this.participantOwner = participantOwner;
    }

}
