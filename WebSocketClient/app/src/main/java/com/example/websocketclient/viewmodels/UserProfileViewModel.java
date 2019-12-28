package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;

public class UserProfileViewModel extends AndroidViewModel {

    private ModelRepository modelRepository;
    private Context context;

    public ObservableField<String> userProfileName = new ObservableField<>();

    private MutableLiveData<Integer> profileButtonEvent;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);

        userProfileName.set(modelRepository.getSelectedFriendModel().getFriendName());
    }

    public LiveData<Integer> getProfileButtonEvent() {
        return profileButtonEvent = new MutableLiveData<>();
    }

    public void startEndToEndChattingButtonClicked() {
        profileButtonEvent.setValue(1);
    }
}
