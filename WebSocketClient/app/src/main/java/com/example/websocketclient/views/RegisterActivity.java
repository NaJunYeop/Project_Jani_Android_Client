package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.websocketclient.controllers.NetworkTask;
import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.controllers.DBUserInformationTask;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.RegisterModel;
import com.example.websocketclient.models.ServerModel;
import com.example.websocketclient.R;
import com.google.gson.Gson;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Button register_btn;
    private EditText user_name_edit;

    private TelephonyManager telephonyManager;
    private ServerModel serverModel;
    private NetworkTask networkTask;
    private Gson gson;
    private String user_name;
    private String requestJson;
    private RegisterModel registerModel;
    private List<UserInformation> userInformation;
    private AppDatabase db;
    private DBUserInformationTask dbUserInformationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getInstance(this);
        dbUserInformationTask = new DBUserInformationTask.Builder(this, "isUserExist", db).build();
        dbUserInformationTask.execute();
    }

    public void registerAction() {
        setContentView(R.layout.activity_register);
        register_btn = (Button)findViewById(R.id.register_btn);
        user_name_edit = (EditText)findViewById(R.id.user_name_edit);

        serverModel = ServerModel.getInstance();
        gson = new Gson();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                user_name = user_name_edit.getText().toString();
                registerModel = new RegisterModel.Builder(user_name).build();
                requestJson = gson.toJson(registerModel, RegisterModel.class);
                networkTask = new NetworkTask(RegisterActivity.this.getApplicationContext(),
                        serverModel,
                        "POST",
                        "/user-registration",
                        requestJson);
                networkTask.execute();
            }
        });
    }
}
