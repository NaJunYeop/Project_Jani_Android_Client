package com.example.websocketclient.views;
//채팅방 친구목록, 일정,
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
//레지스터 버튼 기존 UI의 btnRegister -> register_btn 로 변경 yj
    private Button register_btn;
    private Button btnLogin;
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

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ServerModel.SERVER_IP + ":" + ServerModel.SERVER_PORT + "/janiwss/websocket");
        resetSubscriptions();



        ////////////////////////////
        //레지스터 버튼 코드 yj 시작
        register_btn = (Button)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendEchoViaStomp();             //이건 무엇일까 yj

                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(in);
                //registerActivity로 넘긴후에 chatroom으로 이동하게 코드 짤 예정 yj

            }
        });
        //레지스터 버튼 끝

        ///////////////////////////

        //로그인 버튼 시작 yj
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "LOGIN", Toast.LENGTH_SHORT).show();
                Intent ChatRoom = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(ChatRoom);
            }
        });

        //로그인 버튼 끝
        ////////////////////////////////////


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
