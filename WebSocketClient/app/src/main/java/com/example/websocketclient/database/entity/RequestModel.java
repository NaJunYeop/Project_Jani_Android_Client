package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "request_model")
public class RequestModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "req_id")
    private int reqId;

    @ColumnInfo(name = "req_type")
    private int reqType;

    private String chatChannel;

    @ColumnInfo(name = "req_sender_name")
    private String reqSenderName;

    @ColumnInfo(name = "req_receiver_name")
    private String reqReceiverName;

    public RequestModel(int reqType, String reqSenderName, String reqReceiverName) {
        this.reqType = reqType;
        this.reqSenderName = reqSenderName;
        this.reqReceiverName = reqReceiverName;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public String getReqSenderName() {
        return reqSenderName;
    }

    public void setReqSenderName(String reqSenderName) {
        this.reqSenderName = reqSenderName;
    }

    public String getReqReceiverName() {
        return reqReceiverName;
    }

    public void setReqReceiverName(String reqReceiverName) {
        this.reqReceiverName = reqReceiverName;
    }
}
