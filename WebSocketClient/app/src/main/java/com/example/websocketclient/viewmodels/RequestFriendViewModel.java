package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.models.RequestModel;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public class RequestFriendViewModel extends AndroidViewModel {

    private final String TAG = "RFVMLOG";
    private Context context;
    private ModelRepository modelRepository;
    private MutableLiveData<RequestModel> requestModelEvent;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Gson gson = new Gson();

    private MutableLiveData<Boolean> decisionEvent;

    public RequestFriendViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
    }

    public LiveData<Boolean> getDecisionEvent() {
        return decisionEvent = new MutableLiveData<>();
    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public void acceptButtonClicked(int position) {
        RequestModel requestModel = modelRepository.getRequestModelAt(position);
        FriendModel friendModel = new FriendModel(requestModel.getSenderName());

        requestModel.setStatus("ACK");
        modelRepository.addFriendList(friendModel);

        compositeDisposable.add(modelRepository
                .stompSendMessage("/req/" + requestModel.getSenderName(), gson.toJson(requestModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                    denyButtonClicked(position);
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    public void denyButtonClicked(int position) {
        modelRepository.eraseRequestModelAt(position);
        decisionEvent.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //compositeDisposable.dispose();
    }
}
