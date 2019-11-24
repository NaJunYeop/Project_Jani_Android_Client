package com.example.websocketclient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.websocketclient.Models.RegisterModel;
import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewInitializer();
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
    private void viewInitializer() {
        register_btn = (Button)findViewById(R.id.register_btn);
        user_name_edit = (EditText)findViewById(R.id.user_name_edit);
    }
}
