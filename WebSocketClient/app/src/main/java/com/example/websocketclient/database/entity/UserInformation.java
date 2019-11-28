package com.example.websocketclient.database.entity;
//데이터베이스 생성
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_information")
public class UserInformation {
    @PrimaryKey(autoGenerate = true)
    private int uid;
//데이터베이스를 여기서 객체화
    //유저의 이름 먼저 객체화, 저장
    @ColumnInfo(name = "user_name")//컬럼 이름
    private String userName;

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
}
