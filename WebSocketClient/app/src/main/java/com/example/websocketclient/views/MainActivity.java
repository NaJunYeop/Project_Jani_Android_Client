package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.websocketclient.models.ServerModel;
import com.example.websocketclient.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class MainActivity extends AppCompatActivity {

    private Button register_btn;
    EditText etID;
    EditText etPassword;
    String stID;
    String stPassword;

    private List<String> mDataSet = new ArrayList<>();

    private static final String TAG = "MainActivity";
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";

    private StompClient mStompClient;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /////// /////Yj register
        etID = (EditText)findViewById(R.id.etID);
        etPassword = (EditText)findViewById(R.id.etPassword);


        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//btnRegister 클릭시 발생

                stID = etID.getText().toString();
                stPassword = etPassword.getText().toString();

                Toast.makeText(MainActivity.this,"Register",Toast.LENGTH_SHORT).show();

                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(in);

            }
        });
        ////////
        /////// /////Yj Login

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//btnRegister 클릭시 발생

                Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_SHORT).show();

                Intent in = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(in);

            }
        });
        ////////

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ServerModel.SERVER_IP + ":" + ServerModel.SERVER_PORT + "/janiwss/websocket");
        resetSubscriptions();
        stompConnect();
        /*
        register_btn = (Button)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEchoViaStomp();
            }
        });
        */
        //stompDisconnect();
        //toast("After STOMP Disconnection!");
    }

    public void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void stompConnect() {

        toast("Try Stomp Connection");
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                   switch (lifecycleEvent.getType()) {
                       case OPENED:
                           toast("Stomp connection opened");
                           break;
                       case ERROR:
                           Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                           toast("Stomp connection closed due to error");
                           break;
                       case CLOSED:
                           toast("Stomp connection closed");
                           break;
                       case FAILED_SERVER_HEARTBEAT:
                           toast("Stomp failed server heartbeat");
                           break;
                   }
                });
        mCompositeDisposable.add(dispLifecycle);

        Disposable dispTopic = mStompClient.topic("/topic/greetings")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received" + topicMessage.getPayload());
                    toast(topicMessage.getPayload());
                });
        mCompositeDisposable.add(dispTopic);
        mStompClient.connect(headers);
    }
    public void stompDisconnect() {
        mStompClient.disconnect();
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void sendEchoViaStomp() {
        mCompositeDisposable.add(mStompClient.send("/app/end", "Hello From Android")
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                }));
    }

    private void resetSubscriptions() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();

        //if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
