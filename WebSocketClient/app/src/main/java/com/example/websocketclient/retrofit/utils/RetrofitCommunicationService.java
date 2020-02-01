package com.example.websocketclient.retrofit.utils;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.RegisterModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitCommunicationService {
    @POST("/duplication-check")
    Single<String> userNameDuplicationCheck(@Body String userName);

    @POST("/user-registration")
    Completable registerUserRegisterModelToServer(@Body RegisterModel registerModel);

    @POST("/find-user")
    Maybe<String> findUserInformationModel(@Body String userName);

    @POST("/get-user-info")
    Single<UserInformationModel> getUserInformationModel(@Body String userName);

    /*@POST("/find-user")
    Maybe<RegisterModel> findUserInformation(@Body RegisterModel registerModel);*/

    @POST("/get-request-model")
    Observable<List<RequestModel>> getRequstModelList(@Body String userName);

    @POST("/get-friend-model")
    Observable<List<FriendModel>> getFriendModelList(@Body String userName);

    @POST("/get-chat-room-model")
    Observable<List<ChatRoomModel>> getChatRoomModelList(@Body String userName);

    @POST("/get-message-model")
    Observable<List<MessageModel>> getMessageModelList(@Body String chatChannel);

}
