package com.example.websocketclient.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.websocketclient.models.Buildable;

@Entity(tableName = "register_model")
public class RegisterModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reg_id")
    private int regId;

    @ColumnInfo(name = "reg_user_name")
    private String regUserName;

    @ColumnInfo(name = "reg_password")
    private String regPassword;

    public RegisterModel(String regUserName, String regPassword) {
        this.regUserName = regUserName;
        this.regPassword = regPassword;
    }

    public int getRegId() {
        return regId;
    }

    public void setRegId(int regId) {
        this.regId = regId;
    }

    public String getRegUserName() {
        return regUserName;
    }

    public void setRegUserName(String regUserName) {
        this.regUserName = regUserName;
    }

    public String getRegPassword() {
        return regPassword;
    }

    public void setRegPassword(String regPassword) {
        this.regPassword = regPassword;
    }
}
