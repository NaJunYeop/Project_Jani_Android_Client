package com.example.websocketclient.retrofit.utils;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.RegisterModel;
import com.example.websocketclient.models.PlainTextModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitCommunicationService {
    @POST("/duplication-check")
    Single<PlainTextModel> userNameDuplicationCheck(@Body String userName);

    @POST("/user-registration")
    Single<PlainTextModel> registerUserRegisterModelToServer(@Body RegisterModel registerModel);

    @POST("/find-user")
    Single<PlainTextModel> findUserInformationModel(@Body String userName);

    @POST("/get-user-info")
    Single<UserInformationModel> getUserInformationModel(@Body String userName);

    @POST("/get-topic-channel")
    Single<PlainTextModel> getTopicChannel();
}
