package com.example.websocketclient.database.entity;

import androidx.databinding.ObservableBoolean;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_information")
public class UserInformationModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_info_id")
    private int userInfoId;

    @ColumnInfo(name = "user_info_owner")
    private String userInfoOwner;

    @ColumnInfo(name = "user_info_user_name")
    private String userInfoUserName;

    @Ignore
    private ObservableBoolean checked;

    public UserInformationModel(String userInfoOwner, String userInfoUserName) {
        this.userInfoOwner = userInfoOwner;
        this.userInfoUserName = userInfoUserName;
        checked = new ObservableBoolean();
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

    public ObservableBoolean getChecked() {
        if (checked == null) {
            return checked = new ObservableBoolean();
        }
        else {
            return checked;
        }
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }
}
