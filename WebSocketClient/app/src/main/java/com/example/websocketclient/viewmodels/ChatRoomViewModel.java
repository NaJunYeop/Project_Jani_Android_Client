package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        modelRepository.setReferences(context);
    }

    public void sendButtonClicked() {
        if (sMessageModel == null) sMessageModel = new MessageModel();

        compositeDisposable.add(modelRepository.stompSendMessage("/app/end", gson.toJson(sMessageModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
