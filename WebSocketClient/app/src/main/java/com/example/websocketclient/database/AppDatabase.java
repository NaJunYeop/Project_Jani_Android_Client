package com.example.websocketclient.database;
/////
//RoomDB 사용
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.websocketclient.database.dao.FriendDao;
import com.example.websocketclient.database.dao.UserDao;
import com.example.websocketclient.database.entity.FriendInformation;
import com.example.websocketclient.database.entity.UserInformation;

@Database(entities = {UserInformation.class/*, FriendInformation.class*/}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {    //Primary Key 각각 User,Friend InFo 클래스에서 선언

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {//jani_database 파일 생성, 데이터베이스 객체만들 수 있다.
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "jani_database").build();
        }
        return instance;
    }
    public static AppDatabase newInstance(Context context) {
        instance = null;
        instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "jani_database").build();
        return instance;
    }

    public static void freeInstance() {
        instance = null;
    }

    public abstract UserDao userDao();
    //public abstract FriendDao friendDao();
    //16라인 data access object Dao 필요
}
