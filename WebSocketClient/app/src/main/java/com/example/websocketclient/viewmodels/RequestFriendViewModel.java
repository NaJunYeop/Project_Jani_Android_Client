package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RequestModel;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public class RequestFriendViewModel extends AndroidViewModel {

    private final String TAG = "RFVMLOG";
    private int flag = 0;
    private final int ACCEPT = 1;
    private final int DENY = 2;
    private int list_position;

    private Context context;
    private ModelRepository modelRepository;

    public ObservableBoolean isLoading = new ObservableBoolean();

    private MutableLiveData<Boolean> buttonClickEvent;
    private MutableLiveData<Boolean> decisionEvent;
    private MutableLiveData<Boolean> refreshEvent;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Gson gson = new Gson();

    public ObservableField<String> dialogTextView = new ObservableField<>();

    public RequestFriendViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
    }

    public LiveData<Boolean> getDecisionEvent() {
        return decisionEvent = new MutableLiveData<>();
    }

    public LiveData<Boolean> getButtonClickEvent() {
        return buttonClickEvent = new MutableLiveData<>();
    }

    public LiveData<Boolean> getRefreshEvent() {
        return refreshEvent = new MutableLiveData<>();
    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public void acceptButtonClicked(int position) {
        flag = ACCEPT;
        dialogTextView.set("[ " + modelRepository.getRequestModelAt(position).getSenderName() + " ] 님을 친구로 추가 하시겠습니까?");
        list_position = position;
        buttonClickEvent.setValue(true);
    }

    public void denyButtonClicked(int position) {
        flag = DENY;
        dialogTextView.set("요청 목록에서 삭제 하시겠습니까?");
        list_position = position;
        buttonClickEvent.setValue(true);
    }

    public void duplicateAcceptButtonClicked() {
        if (flag == ACCEPT) {
            RequestModel requestModel = modelRepository.getRequestModelAt(list_position);
            FriendModel friendModel = new FriendModel(requestModel.getSenderName());

            requestModel.setStatus("ACK");

            modelRepository.addFriendModel(friendModel);

            compositeDisposable.add(modelRepository
                    .stompSendMessage("/app/req/" + requestModel.getSenderName(), gson.toJson(requestModel))
                    .subscribe(() -> {
                        Log.d(TAG, "STOMP echo send successfully");
                    }, throwable -> {
                        Log.e(TAG, "Error send STOMP echo", throwable);
                    }));
        }

        modelRepository.eraseRequestModelByPosition(list_position);

        decisionEvent.setValue(true);
        flag = 0;
    }

    public void duplicateDenyButtonClicked() {
        decisionEvent.setValue(false);
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
