package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.RegisterModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.UserInformationModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface RequestModelDao {
    @Query("SELECT * FROM request_model")
    Maybe<List<RequestModel>> getRequestModels();

    @Insert
    Completable insertRequestModel(RequestModel requestModel);

    @Query("DELETE FROM request_model WHERE req_sender_name = :senderName")
    Completable deleteRequestModel(String senderName);
}
