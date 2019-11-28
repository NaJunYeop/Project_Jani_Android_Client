package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.UserInformation;

import java.util.List;
//User 쿼리
@Dao
public interface UserDao {

    @Query("SELECT * FROM user_information")
    List<UserInformation> isUserExist();

    @Query("SELECT * FROM user_information WHERE user_name = :userName")
    UserInformation findByName(String userName);

    @Insert
    void insertUserName(UserInformation userInformation);

    @Delete
    void delete(UserInformation userInformation);
}
