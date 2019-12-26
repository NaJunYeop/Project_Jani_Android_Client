package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.ChatRoomModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.MessageModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.models.RequestModel;
import com.example.websocketclient.views.MainActivity;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class MainViewModel extends AndroidViewModel implements Serializable {
    private final String TAG = "MainViewModelLog";
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";
    public static final String ACK = "ACK";
    public static final String WAIT = "WAIT";

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StompClient mStompClient;
    private UserInformation userInformation = new UserInformation();
    private ModelRepository modelRepository;
    public MessageModel sMessageModel;
    public MessageModel rMessageModel;


    public ObservableField<String> messageEdit = new ObservableField<>();

    private MutableLiveData<LifecycleEvent.Type> stompHealthEvent;
    private MutableLiveData<Integer> positionEvent;
    private MutableLiveData<Integer> friendListFragmentButtonEvent;

    private MutableLiveData<Boolean> requestChannelEvent;
    private MutableLiveData<MessageModel> queueChannelEvent;
    private MutableLiveData<MessageModel> topicChannelEvent;

    private ArrayList<MessageModel> messageModels = new ArrayList<>();
    private Gson gson = new Gson();

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();

        Log.i(TAG, "MainViewModel Constructor");

        // getApplicationContext() : LiftCycle을 가지는 Context
        // getBaseContext() : 자신의 Context가 아니라 다른 Context를 Access할 때 사용한다.
        // View.getContext() : 현재 실행되고 있는 View의 Context를 Return, this와 같다.
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);

        createRequestChannel();
        stompConnect();
    }

    public ModelRepository getModelRepository() {
        return this.modelRepository;
    }

    public ArrayList<MessageModel> getChatAdapterArrayList() {
        return messageModels;
    }

    public LiveData<Integer> getFriendListFragmentButtonEvent() {
        return friendListFragmentButtonEvent = new MutableLiveData<>();
    }

    public LiveData<LifecycleEvent.Type> getStompHealthEvent() {
        return stompHealthEvent = new MutableLiveData<>();
    }

    public LiveData<Integer> getMessageEvent() {
        return positionEvent = new MutableLiveData<>();
    }

    public LiveData<Boolean> getRequestChannelEvent() {
        return requestChannelEvent = new MutableLiveData<>();
    }

    public void stompConnect() {
        Log.i(TAG, "2 : Second");
        compositeDisposable.add(modelRepository.stompGetStompClientLifecycle()
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
        modelRepository.stompConnectStart();
    }

    public void stompDisconnect() {

       mStompClient.disconnect();
    }

    public void createRequestChannel() {
        Log.i(TAG, "3 : Third");
        Toast.makeText(context, modelRepository.getCurUserInformation().getUserName(), Toast.LENGTH_LONG).show();
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/req/" + modelRepository.getCurUserInformation().getUserName())
                .subscribe(topicMessage -> {
                    // Json Parsing Needed.
                    RequestModel requestModel = gson.fromJson(topicMessage.getPayload(), RequestModel.class);
                    if (requestModel.getStatus().equals("REQ")) {
                        modelRepository.addRequestModel(gson.fromJson(topicMessage.getPayload(), RequestModel.class));
                    }
                    else if (requestModel.getStatus().equals("ACK")) {
                        FriendModel friendModel = new FriendModel(requestModel.getReceiverName());
                        modelRepository.addFriendList(friendModel);
                    }
                    /*rMessageModel = gson.fromJson(topicMessage.getPayload(), MessageModel.class);
                    messageModels.add(rMessageModel);
                    Log.d(TAG, "Received : " + rMessageModel.getContents());
                    positionEvent.setValue(messageModels.size() - 1)*/;
                })
        );
    }

    public void createQueueChannel() {
        createChatRoom("/queue/" + modelRepository.getCurUserInformation().getUserName());

    }

    public void createTopicChannel() {
        createChatRoom("/topic/" + modelRepository.getCurUserInformation().getUserName());
    }

    public void createChatRoom(String topic)
    {
        // /topic/greetings에 가입하면 해당 Chat Room에 대한 Event, 즉, Message들을 .subscribe에서 받아온다.
        // compositeDisposable.add(mStompClient.topic("/topic/greetings")
        compositeDisposable.add(modelRepository.stompGetTopicMessage(topic)
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

    public void sendButtonClikced() {
        if (sMessageModel == null) sMessageModel = new MessageModel();

        sMessageModel.setSenderName(MainActivity.intentUserInformation.getUserName());
        sMessageModel.setChatRoomName("topic/greetings");
        sMessageModel.setContents(messageEdit.get());
        sMessageModel.setSenderSideDate("2019/12/16");
        messageEdit.set("");

        compositeDisposable.add(modelRepository.stompSendMessage("/app/end", gson.toJson(sMessageModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
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
        Log.i(TAG, "onCleared() Method is called ......................");
        //modelRepository.stompDisconnectStart(); // 과연 Disconnect 해야 하는가? 에 대한 이해는 조금 더 해 봐야 알 듯.
    }
}


