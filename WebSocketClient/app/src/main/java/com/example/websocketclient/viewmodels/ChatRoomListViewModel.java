package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.models.ChatModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.models.PlainTextModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class ChatRoomListViewModel extends AndroidViewModel {

    private final String TAG = "CRMLVMLOG";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ModelRepository modelRepository;
    private Context context;
    private Gson gson = new Gson();

    private MutableLiveData<Integer> messageEvent;

    public ChatRoomListViewModel(@NonNull Application application) {
        super(application);

        this.context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);

        subscribeToQueueChannel();
        subscribeToTopicChannels();
        subscribeToChatModelListEvent();

        Log.d("QueueChannelCheck", "chat room list num = " + modelRepository.getChatModels().size());
    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public LiveData<Integer> getMessageEvent() {
        return this.messageEvent = new MutableLiveData<>();
    }


    public void subscribeToQueueChannel() {
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/queue/" + modelRepository.getUserRegisterModel().getRegUserName())
                .subscribe(topicMessage -> {
                    Log.d("CHECK", "ChatRoomListViewModel");
                    messageEvent.setValue(0);
                }));
    }

    public void subscribeToTopicChannels() {
        for (ChatModel cm : modelRepository.getChatModels()) {
            if (cm.getChatRoomModel().getChatChannel().contains("/topic/")) {
                compositeDisposable.add(modelRepository.stompGetTopicMessage(cm.getChatRoomModel().getChatChannel())
                        .subscribe(topicMessage -> {
                            messageEvent.setValue(0);
                        })
                );
            }
        }
    }

    public void subscribeToChatModelListEvent() {
        modelRepository.getChatModelListEvent()
                .subscribe(new Observer<ChatModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ChatModel chatModel) {
                        Log.d("PublishSubjectEvent", "inChatRoomListViewModel\n" + chatModel.toString());
                        subscribeToTopicChannel(chatModel.getChatRoomModel().getChatChannel());
                        messageEvent.setValue(3);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void subscribeToTopicChannel(String topicChannel) {
        compositeDisposable.add(modelRepository.stompGetTopicMessage(topicChannel)
                .subscribe(topicMessage -> {
                    messageEvent.setValue(3);
                }));
    }

    public void chatListItemClicked(int position) {

        modelRepository.setSelectedChatModel(modelRepository.getChatModelAt(position));
        messageEvent.setValue(1);
    }

    public void createChatRoomButtonClicked() {
        /*modelRepository.setTopicChannel("/topic/" + 2);
        messageEvent.setValue(2);*/
        modelRepository.retrofitGetTopicChannel()
                .subscribe(new SingleObserver<PlainTextModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PlainTextModel plainTextModel) {
                        modelRepository.setTopicChannel("/topic/" + Integer.parseInt(plainTextModel.getText()));
                        messageEvent.setValue(2);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        Log.i(TAG, "onCleared() Method is called ......................");
        //modelRepository.stompDisconnectStart(); // 과연 Disconnect 해야 하는가? 에 대한 이해는 조금 더 해 봐야 알 듯.
    }
}
