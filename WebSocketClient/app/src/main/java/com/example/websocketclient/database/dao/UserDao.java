package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.UserInformationModel;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface UserDao {

    /*@Query("SELECT * FROM register_model")
    Maybe<UserInformationModel> isUserExist();*/

    /*
    @Query("SELECT * FROM user_information WHERE user_name = :userName")
    UserInformationModel findByName(String userName);
    */

   /* @Query("DELETE FROM UserInformationModel")
    Completable deleteAll();*/

    @Insert
    Completable insertUserName(UserInformationModel userInformationModel);

    @Delete
    void delete(UserInformationModel userInformationModel);
}
