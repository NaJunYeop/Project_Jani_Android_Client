package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RequestModel;
import com.google.gson.Gson;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class MainViewModel extends AndroidViewModel implements Serializable {
    private final String TAG = "MainViewModelLog";

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ModelRepository modelRepository;

    private MutableLiveData<LifecycleEvent.Type> stompHealthEvent;

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
        modelRepository.setMainViewModel(this);

        createRequestChannel();
        createQueueChannel();
        //createTopicChannel();
        stompConnect();
    }

    public ModelRepository getModelRepository() {
        return this.modelRepository;
    }

    public LiveData<LifecycleEvent.Type> getStompHealthEvent() {
        return stompHealthEvent = new MutableLiveData<>();
    }

    public void stompConnect() {
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

    public void createRequestChannel() {
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/req/" + modelRepository.getCurUserInformation().getUserName())
                .subscribe(topicMessage -> {
                    // Json Parsing Needed.
                    RequestModel requestModel = gson.fromJson(topicMessage.getPayload(), RequestModel.class);
                    if (requestModel.getStatus().equals("REQ")) {
                        modelRepository.addRequestModel(requestModel);
                    }
                    else if (requestModel.getStatus().equals("ACK")) {
                        FriendModel friendModel = new FriendModel(requestModel.getReceiverName());
                        modelRepository.addFriendModel(friendModel);
                    }
                })
        );
    }

    public void createQueueChannel() {
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/queue/" + modelRepository.getCurUserInformation().getUserName())
                .subscribe(topicMessage -> {
                    Log.d("CHECK", "MainViewModel");
                    MessageModel receivedMessage = gson.fromJson(topicMessage.getPayload(), MessageModel.class);
                    modelRepository.addChatRoomModelByMessageModel(receivedMessage);
                })
        );
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


