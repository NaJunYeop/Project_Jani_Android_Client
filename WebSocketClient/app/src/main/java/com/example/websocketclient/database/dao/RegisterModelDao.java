package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.RegisterModel;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface  RegisterModelDao {
    @Query("SELECT * FROM register_model")
    Maybe<RegisterModel> getUserRegisterModels();

    @Insert
    Completable insertRegisterModel(RegisterModel registerModel);
}
