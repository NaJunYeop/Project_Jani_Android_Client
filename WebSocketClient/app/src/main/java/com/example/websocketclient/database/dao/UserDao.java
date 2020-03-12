package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.UserInformationModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_information")
    Maybe<List<UserInformationModel>> getUserInformationModels();

    @Insert
    Completable insertUserInformationModel(UserInformationModel userInformationModel);

    @Delete
    void delete(UserInformationModel userInformationModel);
}
