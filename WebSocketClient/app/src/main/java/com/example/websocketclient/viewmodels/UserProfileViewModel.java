package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.ChatRoomModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.MessageModel;
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

        queueRoomName += targetUser;
        userProfileName.set(targetUser.getFriendName());
    }

    public LiveData<Integer> getProfileButtonEvent() {
        return profileButtonEvent = new MutableLiveData<>();
    }

    public void startEndToEndChattingButtonClicked() {
        if (modelRepository.getChatRoomModelHashMap().containsKey(targetUser.getFriendName()) == true) {
            modelRepository.setSelectedChatRoomModel(modelRepository.getChatRoomModelHashMap().get(targetUser.getFriendName()));
        } else {
            ChatRoomModel newChatRoomModel = new ChatRoomModel();
            ArrayList<MessageModel> newMessageModels = new ArrayList<>();
            ArrayList<FriendModel> newParticipants = new ArrayList<>();

            String initialChatRoomName = "/queue/" + targetUser.getFriendName();

            newParticipants.add(new FriendModel(targetUser.getFriendName()));

            newChatRoomModel.setChatRoomName(initialChatRoomName);
            newChatRoomModel.setChatRoomNickName(initialChatRoomName);
            newChatRoomModel.setMessageModels(newMessageModels);
            newChatRoomModel.setParticipants(newParticipants);
            newChatRoomModel.setSenderName("/queue/" + modelRepository.getCurUserInformation().getUserName());
            newChatRoomModel.setType(QUEUE);

            // 이렇게 하고 ChatRoomViewModel에서 onCleared될 때, 해당 채팅방이 존재하지 않으면 Add.

            modelRepository.setSelectedChatRoomModel(newChatRoomModel);
        }

        profileButtonEvent.setValue(1);
    }
}
