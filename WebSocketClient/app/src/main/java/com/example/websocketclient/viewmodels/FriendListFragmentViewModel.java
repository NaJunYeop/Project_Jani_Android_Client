package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.models.RequestModel;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public class FriendListFragmentViewModel extends AndroidViewModel {

    private final String TAG = "FLFVMLOG";
    private final int ADD = 0;
    private final int REQ = 1;

    private Context context;
    private ModelRepository modelRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Gson gson = new Gson();

    public ObservableBoolean isLoading = new ObservableBoolean();

    private MutableLiveData<Boolean> refreshEvent;
    private MutableLiveData<Integer> buttonEvent;
    private MutableLiveData<Boolean> profileEvent;

    public FriendListFragmentViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
    }

    public LiveData<Boolean> getRefreshEvent() {
        return refreshEvent = new MutableLiveData<>();
    }

    public LiveData<Integer> getButtonClickEvent() {
        return buttonEvent = new MutableLiveData<>();
    }

    public LiveData<Boolean> getProfileEvent() {
        return profileEvent = new MutableLiveData<>();
    }

    public ModelRepository getModelRepository() {
        return this.modelRepository;
    }

    public void createRequestChannel() {
        Toast.makeText(context, modelRepository.getCurUserInformation().getUserName(), Toast.LENGTH_LONG).show();
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/req/" + modelRepository.getCurUserInformation().getUserName())
                .subscribe(topicMessage -> {
                    // Json Parsing Needed.
                    RequestModel requestModel = gson.fromJson(topicMessage.getPayload(), RequestModel.class);
                    if (requestModel.getStatus().equals("REQ")) {
                        modelRepository.getRequestModelHashMap().put(requestModel.getSenderName(), requestModel);
                        modelRepository.getRequestModelList().add(requestModel);
                        //modelRepository.addRequestModel(gson.fromJson(topicMessage.getPayload(), RequestModel.class));
                    }
                    else if (requestModel.getStatus().equals("ACK")) {
                        FriendModel friendModel = new FriendModel(requestModel.getReceiverName());

                        modelRepository.getFriendModelHashMap().put(friendModel.getFriendName(), friendModel);
                        modelRepository.getFriendModelList().add(friendModel);
                        //modelRepository.addFriendList(friendModel);
                    }
                })
        );
    }

    public void addFriendButtonClicked() {
        buttonEvent.setValue(ADD);
    }

    public void uncheckedFriendRequestButtonClicked() {
        buttonEvent.setValue(REQ);
    }

    public void listItemClicked(int position) {
        modelRepository.setSelectedFriendModel(modelRepository.getFriendModelAt(position));
        profileEvent.setValue(true);
    }

    public void onRefresh() {
        isLoading.set(true);
        refreshEvent.setValue(true);
    }

    public void onComplete() {
        isLoading.set(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
