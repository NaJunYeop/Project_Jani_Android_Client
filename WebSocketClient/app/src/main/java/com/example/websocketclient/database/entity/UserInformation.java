package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_information")
public class UserInformation implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "topic_identifier")
    private int topicIdentifier;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public int getTopicIdentifier() {
        return topicIdentifier;
    }

    public void setTopicIdentifier(int topicIdentifier) {
        this.topicIdentifier = topicIdentifier;
    }
}
