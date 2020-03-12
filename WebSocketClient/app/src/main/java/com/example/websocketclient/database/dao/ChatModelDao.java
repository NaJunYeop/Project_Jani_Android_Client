package com.example.websocketclient.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.database.entity.RegisterModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface ChatModelDao {
    @Query("SELECT * FROM chat_room_Model")
    Maybe<List<ChatRoomModel>> getChatRoomModels();

    @Query("SELECT * FROM message_model")
    Maybe<List<MessageModel>> getMessageModels();

    @Query("SELECT * FROM participant_model")
    Maybe<List<ParticipantModel>> getParticipantModels();

    @Insert
    Completable insertChatRoomModel(ChatRoomModel chatRoomModel);

    @Insert
    Completable insertMessageModel(MessageModel messageModel);

    @Insert
    Completable insertParticipantModel(ParticipantModel participantModel);
}
