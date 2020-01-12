package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "chat_room_Model")
public class ChatRoomModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chat_room_id")
    private int chatRoomId;

    @ColumnInfo(name = "chat_room_count")
    private int chatRoomCount;

    @ColumnInfo(name = "chat_room_count")
    private String chatRoomOwner;

    @ColumnInfo(name = "chat_channel")
    private String chatChannel;

    @ColumnInfo(name = "chat_room_name")
    private String chatRoomName;

    public ChatRoomModel(int chatRoomCount, String chatRoomOwner, String chatChannel, String chatRoomName) {
        this.chatRoomCount = chatRoomCount;
        this.chatRoomOwner = chatRoomOwner;
        this.chatChannel = chatChannel;
        this.chatRoomName = chatRoomName;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public int getChatRoomCount() {
        return chatRoomCount;
    }

    public void setChatRoomCount(int chatRoomCount) {
        this.chatRoomCount = chatRoomCount;
    }

    public String getChatRoomOwner() {
        return chatRoomOwner;
    }

    public void setChatRoomOwner(String chatRoomOwner) {
        this.chatRoomOwner = chatRoomOwner;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
}
