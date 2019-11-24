package com.example.websocketclient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private ServerModel serverModel;
    private String requestType;
    private String requestAdder;
    private String requestJson;

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
        else if (s.equals("OK")){
            Intent intent = new Intent(context, MainActivity.class);
            Toast.makeText(context, "등록이 완료 되었습니다.", Toast.LENGTH_LONG).show();
            context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        }
        else if (s.equals("EXIST")) {
            Toast.makeText(context, "존재하는 이름입니다.\n다른 이름을 입력해 주세요.", Toast.LENGTH_LONG).show();
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
