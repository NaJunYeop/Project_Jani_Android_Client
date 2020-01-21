package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;

import java.util.ArrayList;

public class UserProfileViewModel extends AndroidViewModel {

    private final int QUEUE = 0;
    private final int TOPIC = 1;

    private ModelRepository modelRepository;
    private Context context;
    private FriendModel targetUser;
    private String queueRoomName = "/queue/";

    public ObservableField<String> userProfileName = new ObservableField<>();

    private MutableLiveData<Integer> profileButtonEvent;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);

        targetUser = modelRepository.getSelectedFriendModel();

        queueRoomName += targetUser.getFriendName();
        userProfileName.set(targetUser.getFriendName());
}

    public LiveData<Integer> getProfileButtonEvent() {
        return profileButtonEvent = new MutableLiveData<>();
    }

    public void startEndToEndChattingButtonClicked() {
        //modelRepository.addChatRoomModelByName(targetUser.getFriendName());

        Log.d("TesTest", "First 1:1 ButtonClicked" + modelRepository.getChatRoomList().size());

        modelRepository.setSelectedChatRoomModel(modelRepository.getChatRoomModelHashMap().get(queueRoomName));
        profileButtonEvent.setValue(1);
    }
}
