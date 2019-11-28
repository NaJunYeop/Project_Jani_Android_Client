package com.example.websocketclient.controllers;
//통신
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.controllers.DBUserInformationTask;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.views.MainActivity;
import com.example.websocketclient.models.ServerModel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NetworkTask extends AsyncTask<Void, Void, String> {//HTTP통신하는 곳
    private Context context;
    private ServerModel serverModel;
    private String requestType;
    private String requestAdder;
    private String requestJson;
    private AppDatabase db;
    private UserInformation userInformation;

    public NetworkTask(Context context, ServerModel serverModel, String requestType, String requestAdder, String requestJson) {
        this.context = context;
        this.serverModel = serverModel;
        this.requestType = requestType;
        this.requestAdder = requestAdder;
        this.requestJson = requestJson;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null) {
            Toast.makeText(context, "연결 실패.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
        }
        else if (s.equals("EXCEPTION")) {
            Toast.makeText(context, "오류가 발생하였습니다.\n다시 시도해 주세요.", Toast.LENGTH_LONG).show();
        }
        else if (s.equals("EXIST")) {
            Toast.makeText(context, "존재하는 이름입니다.\n다른 이름을 입력해 주세요.", Toast.LENGTH_LONG).show();
        }
        else {
            db = AppDatabase.getInstance(context);
            DBUserInformationTask dbUserInformationTask = new DBUserInformationTask.Builder(context, "insertUserName", db)
                    .setUserName(s)
                    .build();
            dbUserInformationTask.execute();
            Intent intent = new Intent(context, MainActivity.class);
            Toast.makeText(context, "등록이 완료 되었습니다.", Toast.LENGTH_LONG).show();
            context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (requestType.equals("POST")) {
            return serverModel.requestHttpPost(requestAdder, requestJson);
        }

        if (requestType.equals("GET")) {

        }
        return null;
    }
}
