package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.UserInformation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface UserDao {

    @Query("SELECT * FROM register_model")
    Maybe<UserInformation> isUserExist();

    /*
    @Query("SELECT * FROM user_information WHERE user_name = :userName")
    UserInformation findByName(String userName);
    */

    @Query("DELETE FROM user_information")
    Completable deleteAll();

    @Insert
    Completable insertUserName(UserInformation userInformation);

    @Delete
    void delete(UserInformation userInformation);
}
