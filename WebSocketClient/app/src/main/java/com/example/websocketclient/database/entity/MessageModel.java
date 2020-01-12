package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.StringTokenizer;

@Entity(tableName = "message_model")
public class MessageModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "msg_id")
    private int msgId;

    @ColumnInfo(name = "msg_is_read")
    private int msgIsRead;

    @ColumnInfo(name = "msg_count")
    private int msgCount;

    @ColumnInfo(name = "msg_owner")
    private String msgOwner;

    @ColumnInfo(name = "chat_channel")
    private String chatChannel;

    @ColumnInfo(name = "msg_sender_name")
    private String msgSenderName;

    @ColumnInfo(name = "msg_sender_side_date")
    private String msgSenderSideDate;

    @ColumnInfo(name = "msg_content")
    private String msgContent;

    public MessageModel(int msgIsRead, int msgCount, String msgOwner, String chatChannel, String msgSenderName, String msgSenderSideDate, String msgContent) {
        this.msgIsRead = msgIsRead;
        this.msgCount = msgCount;
        this.msgOwner = msgOwner;
        this.chatChannel = chatChannel;
        this.msgSenderName = msgSenderName;
        this.msgSenderSideDate = msgSenderSideDate;
        this.msgContent = msgContent;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgIsRead() {
        return msgIsRead;
    }

    public void setMsgIsRead(int msgIsRead) {
        this.msgIsRead = msgIsRead;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public String getMsgOwner() {
        return msgOwner;
    }

    public void setMsgOwner(String msgOwner) {
        this.msgOwner = msgOwner;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getMsgSenderName() {
        return msgSenderName;
    }

    public void setMsgSenderName(String msgSenderName) {
        this.msgSenderName = msgSenderName;
    }

    public String getMsgSenderSideDate() {
        return msgSenderSideDate;
    }

    public void setMsgSenderSideDate(String msgSenderSideDate) {
        this.msgSenderSideDate = msgSenderSideDate;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSenderSideDate() {
        String ret;
        StringTokenizer stringTokenizer = new StringTokenizer(msgSenderSideDate, " ");

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
}
