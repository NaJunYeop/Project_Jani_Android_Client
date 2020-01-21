package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.ModelRepository;

import io.reactivex.disposables.CompositeDisposable;

public class ChatRoomListViewModel extends AndroidViewModel {

    private final String TAG = "CRMLVMLOG";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ModelRepository modelRepository;
    private Context context;

    private MutableLiveData<Integer> messageEvent;

    public ChatRoomListViewModel(@NonNull Application application) {
        super(application);

        this.context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);

        //subscribeToQueueChannel();
    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public LiveData<Integer> getMessageEvent() {
        return this.messageEvent = new MutableLiveData<>();
    }


    /*public void subscribeToQueueChannel() {
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/queue/" + modelRepository.getCurUserInformation().getUserName())
                .subscribe(topicMessage -> {
                    Log.d("CHECK", "ChatRoomListViewModel");
                    messageEvent.setValue(0);
                }));
    }*/

    public void chatListItemClicked(int position) {
        modelRepository.setSelectedChatRoomModel(modelRepository.getChatRoomList().get(position));
        messageEvent.setValue(1);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        Log.i(TAG, "onCleared() Method is called ......................");
        //modelRepository.stompDisconnectStart(); // 과연 Disconnect 해야 하는가? 에 대한 이해는 조금 더 해 봐야 알 듯.
    }
}
