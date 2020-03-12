package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.ChatModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class UserProfileViewModel extends AndroidViewModel {

    private final int QUEUE = 0;
    private final int TOPIC = 1;

    private ModelRepository modelRepository;
    private Context context;
    private UserInformationModel selectedUserInformationModel;
    private String queueChannel = "/queue/";
    private String receiverName;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableField<String> userProfileName = new ObservableField<>();

    private MutableLiveData<Integer> profileButtonEvent;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);

        selectedUserInformationModel = modelRepository.getSelectedUserInformationModel();

        receiverName = selectedUserInformationModel.getUserInfoUserName();

        queueChannel += receiverName;
        userProfileName.set(receiverName);
}

    public LiveData<Integer> getProfileButtonEvent() {
        return profileButtonEvent = new MutableLiveData<>();
    }

    public void startEndToEndChattingButtonClicked() {
        String userName = modelRepository.getUserRegisterModel().getRegUserName();

        List<ParticipantModel> participantModels = new ArrayList<>();

        participantModels.add(new ParticipantModel(userName, queueChannel, userName));
        participantModels.add(new ParticipantModel(receiverName, queueChannel, userName));

        ChatModel chatModel = modelRepository.getChatModel(queueChannel);

        if (chatModel == null) {
            // createChatModel을 호출하면 chatModels에 add한다.
            chatModel = modelRepository.createChatModel(queueChannel, receiverName, participantModels);

            insertClientDBChatRoomModel(chatModel.getChatRoomModel());
            for (ParticipantModel pm : chatModel.getParticipantModels()) {
                insertClientDBParticipantModel(pm);
            }
        }

        modelRepository.setSelectedChatModel(chatModel);
        profileButtonEvent.setValue(1);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
