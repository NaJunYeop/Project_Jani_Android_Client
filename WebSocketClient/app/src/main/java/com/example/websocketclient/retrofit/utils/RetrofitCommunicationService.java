package com.example.websocketclient.retrofit.utils;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.RegisterModel;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitCommunicationService {
    @POST("/user-registration")
    Maybe<RegisterModel> getUserInformation(@Body RegisterModel registerModel);

    @POST("/find-user")
    Maybe<RegisterModel> findUserInformation(@Body RegisterModel registerModel);

    @POST("/get-request-model")
    Observable<List<RequestModel>> getRequstModelList(@Body String userName);

    @POST("/get-friend-model")
    Observable<List<FriendModel>> getFriendModelList(@Body String userName);

    @POST("/get-chat-room-model")
    Observable<List<ChatRoomModel>> getChatRoomModelList(@Body String userName);

    @POST("/get-message-model")
    Observable<List<MessageModel>> getMessageModelList(@Body String chatChannel);

}
