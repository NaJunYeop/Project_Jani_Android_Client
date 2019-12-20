package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.MessageModel;
import com.example.websocketclient.models.ServerModel;
import com.example.websocketclient.views.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;

public class MainViewModel extends AndroidViewModel {
    private final String TAG = "MainViewModelLog";
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StompClient mStompClient;
    private UserInformation userInformation = new UserInformation();
    public MessageModel sMessageModel;
    public MessageModel rMessageModel;

    public ObservableField<String> messageEdit = new ObservableField<>();

    private MutableLiveData<LifecycleEvent.Type> stompHealthEvent;
    private MutableLiveData<Integer> positionEvent;

    private ArrayList<MessageModel> messageModels = new ArrayList<>();

    private Gson gson = new Gson();

    public MainViewModel(@NonNull Application application) {
        super(application);

        // getApplicationContext() : LiftCycle을 가지는 Context
        // getBaseContext() : 자신의 Context가 아니라 다른 Context를 Access할 때 사용한다.
        // View.getContext() : 현재 실행되고 있는 View의 Context를 Return, this와 같다.
        this.context = application.getApplicationContext();
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ServerModel.SERVER_IP + ":" + ServerModel.SERVER_PORT + "/janiwss/websocket");
        stompConnect();
    }

    public ArrayList<MessageModel> getChatAdapterArrayList() {
        return messageModels;
    }

    public LiveData<LifecycleEvent.Type> getStompHealthEvent() {
        return stompHealthEvent = new MutableLiveData<>();
    }

    public LiveData<Integer> getMessageEvent() {
        return positionEvent = new MutableLiveData<>();
    }

    public void stompConnect() {
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        compositeDisposable.add(mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i(TAG, "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            break;
                    }
                    stompHealthEvent.setValue(lifecycleEvent.getType());
                })
        );
        mStompClient.connect(headers);
    }

    public void stompDisconnect() {

       mStompClient.disconnect();
    }

    public void createChatRoom(String topic)
    {
        // /topic/greetings에 가입하면 해당 Chat Room에 대한 Event, 즉, Message들을 .subscribe에서 받아온다.
        // compositeDisposable.add(mStompClient.topic("/topic/greetings")
        compositeDisposable.add(mStompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    // Json Parsing Needed.
                    rMessageModel = gson.fromJson(topicMessage.getPayload(), MessageModel.class);
                    messageModels.add(rMessageModel);
                    Log.d(TAG, "Received : " + rMessageModel.getContents());
                    positionEvent.setValue(messageModels.size() - 1);
                })
        );
    }

    public MessageModel getMessageModelAt(int position) {
        return messageModels.get(position);
    }

    public void sendButtonClikced()
    {
        if (sMessageModel == null) sMessageModel = new MessageModel();

        sMessageModel.setSenderName(MainActivity.intentUserInformation.getUserName());
        sMessageModel.setChatRoomName("topic/greetings");
        sMessageModel.setContents(messageEdit.get());
        sMessageModel.setSenderSideDate("2019/12/16");
        messageEdit.set("");

        compositeDisposable.add(mStompClient.send("/app/end", gson.toJson(sMessageModel))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    // 아마 App이 다른 Thread에서 뭔가가 동작하고 있다고 해도 Message를 Send할 수 있게 조치를 취하는 것 인 듯.
    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        stompDisconnect(); // 과연 Disconnect 해야 하는가? 에 대한 이해는 조금 더 해 봐야 알 듯.
    }
}


