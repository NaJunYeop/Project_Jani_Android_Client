package com.example.websocketclient.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.websocketclient.database.dao.FriendDao;
import com.example.websocketclient.database.dao.UserDao;
import com.example.websocketclient.database.entity.FriendInformation;
import com.example.websocketclient.database.entity.UserInformation;

@Database(entities = {UserInformation.class/*, FriendInformation.class*/}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "jani_database_test").build();
        }
        return instance;
    }
    public static AppDatabase newInstance(Context context) {
        instance = null;
        instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "jani_database_test").build();
        return instance;
    }

    public static void freeInstance() {
        instance = null;
    }

    public abstract UserDao userDao();
    //public abstract FriendDao friendDao();
}
