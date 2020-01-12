package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_information")
public class UserInformation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_info_id")
    private int userInfoId;

    @ColumnInfo(name = "user_info_owner")
    private String userInfoOwner;

    @ColumnInfo(name = "user_info_user_name")
    private String userInfoUserName;

    public UserInformation(String userInfoOwner, String userInfoUserName) {
        this.userInfoOwner = userInfoOwner;
        this.userInfoUserName = userInfoUserName;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public String getUserInfoOwner() {
        return userInfoOwner;
    }

    public void setUserInfoOwner(String userInfoOwner) {
        this.userInfoOwner = userInfoOwner;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserInfoUserName() {
        return userInfoUserName;
    }

    public void setUserInfoUserName(String userInfoUserName) {
        this.userInfoUserName = userInfoUserName;
    }
}
