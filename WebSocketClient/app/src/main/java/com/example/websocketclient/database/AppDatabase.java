package com.example.websocketclient.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.websocketclient.database.dao.RegisterModelDao;
import com.example.websocketclient.database.dao.RequestModelDao;
import com.example.websocketclient.database.dao.UserDao;
import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.database.entity.RegisterModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.UserInformationModel;

@Database(entities = {UserInformationModel.class, RequestModel.class, RegisterModel.class, ParticipantModel.class, MessageModel.class, ChatRoomModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "jani_database_temp").build();
        }
        return instance;
    }
    public static AppDatabase newInstance(Context context) {
        instance = null;
        instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "jani_database_temp").build();
        return instance;
    }

    public static void freeInstance() {
        instance = null;
    }

    public abstract UserDao userDao();
    public abstract RegisterModelDao registerModelDao();
    public abstract RequestModelDao requestModelDao();
}
