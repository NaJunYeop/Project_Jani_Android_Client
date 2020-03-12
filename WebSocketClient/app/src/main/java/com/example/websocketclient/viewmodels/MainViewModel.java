package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.ChatModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RequestModel;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOperator;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
        createTopicChannels();

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
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/req/" + modelRepository.getUserRegisterModel().getRegUserName())
                .subscribe(topicMessage -> {
                    RequestModel requestModel = gson.fromJson(topicMessage.getPayload(), RequestModel.class);
                    int type = requestModel.getReqType();
                    // REQ
                    if (type == 1) {
                        addRequestModel(requestModel);
                    }
                    // ACK
                    else if (type == 2) {
                        // 해당 친구(사용자) 이름을 MySQL에서 조회한 후 UserInformation을 받아
                        // Database(SQLite)와 ArrayList에 저장한다.
                        addFriendUserInformation(requestModel.getReqReceiverName());
                    }
                    // TopicChannel
                    else if (type == 3) {

                    }

                    // ParticipantName
                    // Delimeter = ':'
                    else if (type == 4) {
                        Toast.makeText(context, requestModel.getChatChannel(), Toast.LENGTH_SHORT).show();
                        Log.d("topicChecking", requestModel.getChatChannel());
                        StringTokenizer st = new StringTokenizer(requestModel.getReqSenderName(), ":");

                        while (st.hasMoreTokens()) {
                            modelRepository.getTempParticipantModels().add(new ParticipantModel(st.nextToken(), requestModel.getChatChannel(), modelRepository.getUserRegisterModel().getRegUserName()));
                        }

                        createTopicChannel(requestModel.getChatChannel());
                    }
                })
        );
    }

    public void createQueueChannel() {
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/queue/" + modelRepository.getUserRegisterModel().getRegUserName())
                        .subscribe(topicMessage -> {
                            MessageModel receivedMessage = gson.fromJson(topicMessage.getPayload(), MessageModel.class);
                            receivedMessage.setMsgOwner(modelRepository.getUserRegisterModel().getRegUserName());
                            // 내가 보낸 Message를 중복으로 받지 않기 위한 조건.
                            if (!receivedMessage.getMsgSenderName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {

                                Toast.makeText(context, receivedMessage.getMsgContent(), Toast.LENGTH_SHORT).show();
                                Log.d("QueueChannelCheck", "In MainViewModel");
                                String chatChannel = "";

                                // MsgSender에 대한 ChatModel이 존재하지 않을 경우 생성해야한다.
                                if (modelRepository.getChatModel((chatChannel = receivedMessage.getChatChannel())) == null) {
                                    List<ParticipantModel> participantModels = new ArrayList<>();
                                    participantModels.add(new ParticipantModel(receivedMessage.getMsgSenderName(), chatChannel, modelRepository.getUserRegisterModel().getRegUserName()));
                                    participantModels.add(new ParticipantModel(modelRepository.getUserRegisterModel().getRegUserName(), chatChannel, modelRepository.getUserRegisterModel().getRegUserName()));

                                    // createChatModel을 호출하면 chatModels에 add한다.
                                    ChatModel chatModel = modelRepository.createChatModel(
                                            chatChannel,
                                            receivedMessage.getMsgSenderName(),
                                            participantModels
                                    );

                                    chatModel.getMessageModels().add(receivedMessage);

                                    insertClientDBChatRoomModel(chatModel.getChatRoomModel());
                                    insertClientDBMessageModel(receivedMessage);

                                    for (ParticipantModel pm : chatModel.getParticipantModels()) {
                                        insertClientDBParticipantModel(pm);
                                    }
                                }
                                else {
                                    modelRepository.addMessageModelToChatModels(receivedMessage);
                                    insertClientDBMessageModel(receivedMessage);
                                }
                            }
                })
        );
    }

    public void createTopicChannels() {
        for (ChatModel cm : modelRepository.getChatModels()) {
            if (cm.getChatRoomModel().getChatChannel().contains("/topic/")) {
                compositeDisposable.add(modelRepository.stompGetTopicMessage(cm.getChatRoomModel().getChatChannel())
                        .subscribe(topicMessage -> {
                            MessageModel receivedMessage = gson.fromJson(topicMessage.getPayload(), MessageModel.class);
                            receivedMessage.setMsgOwner(modelRepository.getUserRegisterModel().getRegUserName());

                            if (!receivedMessage.getMsgSenderName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {
                                String chatChannel = "";
                                String chatRoomName = "";

                                if (modelRepository.getChatModel((chatChannel = receivedMessage.getChatChannel())) == null) {
                                    List<ParticipantModel> participantModels = new ArrayList<>();
                                    ParticipantModel pm;
                                    List<ParticipantModel> tempParticipantModels = modelRepository.getTempParticipantModels();

                                    for (int i = 0; i < tempParticipantModels.size(); i++) {
                                        pm = tempParticipantModels.get(i);

                                        if (pm.getChatChannel().equals(receivedMessage.getChatChannel())) {
                                            participantModels.add(pm);
                                            insertClientDBParticipantModel(pm);

                                            if (!pm.getParticipantUserName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {
                                                chatRoomName += pm.getParticipantUserName() + ", ";
                                            }

                                            tempParticipantModels.remove(i);
                                            i--;
                                        }
                                    }

                                    chatRoomName = chatRoomName.substring(0, chatRoomName.length() - 2);

                                    ChatModel chatModel = modelRepository.createChatModel(
                                            chatChannel,
                                            chatRoomName,
                                            participantModels
                                    );

                                    chatModel.getMessageModels().add(receivedMessage);

                                    insertClientDBChatRoomModel(chatModel.getChatRoomModel());
                                    insertClientDBMessageModel(receivedMessage);
                                } else {
                                    modelRepository.addMessageModelToChatModels(receivedMessage);
                                    insertClientDBMessageModel(receivedMessage);
                                }
                            }
                        })
                );
            }
        }
    }

    public void createTopicChannel(String topicChannel) {

        compositeDisposable.add(modelRepository.stompGetTopicMessage(topicChannel)
                .subscribe(topicMessage -> {
                    MessageModel receivedMessage = gson.fromJson(topicMessage.getPayload(), MessageModel.class);
                    receivedMessage.setMsgOwner(modelRepository.getUserRegisterModel().getRegUserName());

                    Log.d("receivedMessageModelLog", "In MainViewModel : " + receivedMessage.toString());

                    if (!receivedMessage.getMsgSenderName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {
                        String chatChannel = "";
                        String chatRoomName = "";

                        if (modelRepository.getChatModel((chatChannel = receivedMessage.getChatChannel())) == null) {
                            List<ParticipantModel> participantModels = new ArrayList<>();
                            ParticipantModel pm;
                            List<ParticipantModel> tempParticipantModels = modelRepository.getTempParticipantModels();

                            for (int i = 0; i < tempParticipantModels.size(); i++) {
                                pm = tempParticipantModels.get(i);

                                Log.d("formatChecking", pm.getParticipantUserName());
                                Log.d("formatChecking", pm.getChatChannel() + ", " + receivedMessage.getChatChannel());

                                if (pm.getChatChannel().equals(receivedMessage.getChatChannel())) {
                                    participantModels.add(pm);
                                    insertClientDBParticipantModel(pm);

                                    if (!pm.getParticipantUserName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {
                                        Log.d("formatChecking", pm.getParticipantUserName());
                                        chatRoomName += pm.getParticipantUserName() + ", ";
                                    }

                                    tempParticipantModels.remove(i);
                                    i--;
                                }
                            }

                            chatRoomName = chatRoomName.substring(0, chatRoomName.length() - 2);

                            ChatModel chatModel = modelRepository.createChatModel(
                                    chatChannel,
                                    chatRoomName,
                                    participantModels
                            );

                            chatModel.getMessageModels().add(receivedMessage);

                            Log.d("ChatRoomCheckLog", "MainViewModel !!! : " + receivedMessage.toString());

                            insertClientDBChatRoomModel(chatModel.getChatRoomModel());
                            insertClientDBMessageModel(receivedMessage);
                        } else {
                            modelRepository.addMessageModelToChatModels(receivedMessage);
                            insertClientDBMessageModel(receivedMessage);
                        }
                    }
                })
        );
    }

    public void addRequestModel(RequestModel requestModel) {
        modelRepository.getRequestModel(requestModel.getReqSenderName())
                .subscribe(new MaybeObserver<RequestModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(RequestModel requestModel) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // Add to Database
                        insertClientDBRequestModel(requestModel);
                        // Add to ArrayList
                        modelRepository.addRequestModel(requestModel);
                    }
                });
    }

    public void insertClientDBChatRoomModel(ChatRoomModel chatRoomModel) {
        modelRepository.insertClientDBChatRoomModel(chatRoomModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertClientDBMessageModel(MessageModel messageModel) {
        modelRepository.insertClientDBMessageModel(messageModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertClientDBParticipantModel(ParticipantModel participantModel) {
        modelRepository.insertClientDBParticipantModel(participantModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertClientDBRequestModel(RequestModel requestModel) {
        modelRepository.insertClientDBRequestModel(requestModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void addFriendUserInformation(String friendUserName) {
        modelRepository.retrofitGetUserInformationModel(friendUserName)
                .subscribe(new SingleObserver<UserInformationModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserInformationModel userInformationModel) {
                        // Add To Database
                        insertClientDBUserInformationModel(userInformationModel);
                        // Add To ArrayList
                        modelRepository.addUserInformationModel(userInformationModel);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertClientDBUserInformationModel(UserInformationModel userInformationModel) {
        modelRepository.insertClientDBUserInformationModel(userInformationModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        // Insert To SQLite Successfully
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
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
        Log.i("QueueChannelCheck", "MainViewModel : onCleared()");
        modelRepository.stompDisconnectStart(); // 과연 Disconnect 해야 하는가? 에 대한 이해는 조금 더 해 봐야 알 듯.
    }
}


