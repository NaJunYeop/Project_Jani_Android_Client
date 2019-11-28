package com.example.websocketclient.database.controllers;
//main 쓰레드에서 DB접근 connection x 다른 thread에서 건드려야한다
//Thread
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.Buildable;
import com.example.websocketclient.views.MainActivity;
import com.example.websocketclient.views.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DBUserInformationTask extends AsyncTask<Void, Void, List<UserInformation>> {

    private Context context;
    private String command;
    private AppDatabase db;
    private String userName;

    public static class Builder implements Buildable {
        private Context context;
        private String command;
        private AppDatabase db;
        private String userName = "";

        public Builder(Context context, String command, AppDatabase db) {
            this.context = context;
            this.command = command;
            this.db = db;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        @Override
        public DBUserInformationTask build() {
            return new DBUserInformationTask(this);
        }
    }

    public DBUserInformationTask(Builder builder) {
        this.context = builder.context;
        this.command = builder.command;
        this.db = builder.db;
        this.userName = builder.userName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }//main thread에서 동작
    //UI 에서 동작

    @Override
    protected void onPostExecute(List<UserInformation> userInformations) {//Dinbackground에서 처리된게
        //mainthread에서 동작
        super.onPostExecute(userInformations);
        if (command.equals("isUserExist")) {
            if (userInformations.size() == 0) {
                ((RegisterActivity) context).registerAction();
            }
            else {
                Toast.makeText(context, userInformations.get(0).getUserName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    @Override
        protected List<UserInformation> doInBackground(Void... voids) {//background에서 실행할 것들
            if (command.equals("isUserExist")) {
                return db.userDao().isUserExist();
            }
            if (command.equals("insertUserName")) {
                UserInformation userinformation = new UserInformation();//onPostExecute로 보낸다.
                List<UserInformation> retList = new ArrayList<>();

                userinformation.setUserName(userName);
                db.userDao().insertUserName(userinformation);
                retList.add(userinformation);
                return retList;
            }
        return null;
    }
}
