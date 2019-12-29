package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.ChatRoomModel;
import com.example.websocketclient.models.MessageModel;
import com.example.websocketclient.models.ModelRepository;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public class ChatRoomViewModel extends AndroidViewModel {
    private final String TAG = "ChatRoomViewModelLog";
    private Context context;
    private ModelRepository modelRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MessageModel sMessageModel;
    private Gson gson = new Gson();

    public ObservableField<String> messageEdit = new ObservableField<>();

    private MutableLiveData<Boolean> messageEvent;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
    }

    public LiveData<Boolean> getMessageEvent() {
        return this.messageEvent = new MutableLiveData<>();
    }

    public ModelRepository getModelRepository() {
        return this.modelRepository;
    }

    public void sendButtonClicked() {
        if (sMessageModel == null) sMessageModel = new MessageModel();

        ChatRoomModel selectedChatRoomModel = modelRepository.getSelectedChatRoomModel();

        sMessageModel.setSenderName(modelRepository.getCurUserInformation().getUserName());
        sMessageModel.setChatRoomName(selectedChatRoomModel.getChatRoomName());
        sMessageModel.setContents(messageEdit.get());
        sMessageModel.setSenderSideDate("2019/12/16");

        compositeDisposable.add(modelRepository.stompSendMessage("/app" + modelRepository.getSelectedChatRoomModel().getChatRoomName(), gson.toJson(sMessageModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                    modelRepository.getSelectedChatRoomModel().getMessageModels().add(sMessageModel);
                    messageEdit.set("");
                    messageEvent.setValue(true);
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        String chatRoomName = modelRepository.getSelectedChatRoomModel().getChatRoomName();

        if (modelRepository.getChatRoomModelHashMap().containsKey(chatRoomName) == false) {
            modelRepository.getChatRoomModelHashMap().put(chatRoomName, modelRepository.getSelectedChatRoomModel());
            modelRepository.getChatRoomList().add(modelRepository.getSelectedChatRoomModel());
        }

        compositeDisposable.dispose();
    }
}
