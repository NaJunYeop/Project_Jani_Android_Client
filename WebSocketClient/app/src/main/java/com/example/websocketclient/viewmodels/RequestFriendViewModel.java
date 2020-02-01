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

import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RequestModel;
import com.google.gson.Gson;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

    public void listAcceptButtonClicked(int position) {
        flag = ACCEPT;
        dialogTextView.set("[ " + modelRepository.getRequestModelAt(position).getReqSenderName() + " ] 님을 친구로 추가 하시겠습니까?");
        list_position = position;
        buttonClickEvent.setValue(true);
    }

    public void listDenyButtonClicked(int position) {
        flag = DENY;
        dialogTextView.set("요청 목록에서 삭제 하시겠습니까?");
        list_position = position;
        buttonClickEvent.setValue(true);
    }

    public void dialogAcceptButtonClicked() {
        RequestModel requestModel = modelRepository.getRequestModelAt(list_position);
        String senderName = requestModel.getReqSenderName();

        // Dialog에서 [추가] 버튼을 눌렀을 경우.
        if (flag == ACCEPT) {

            requestModel.setReqType(2); // 1 : REQ, 2 : ACK, 3 : TOPIC_CHANNEL

            // 친구 추가
            addFriendUserInformation(senderName);
            // RequestModel Sender에게 ACK을 보내서 ACK을 보내는 Sender를 친구로 추가하게 함
            sendAckToSenderViaStomp(requestModel);
        }

        // RequestModel을 ACCEPT/DENY에 상관없이 삭제해야함.
        modelRepository.deleteRequestModel(list_position);
        modelRepository.deleteRequestModelFromClientDB(senderName);
        decisionEvent.setValue(true);
    }

    public void dialogDenyButtonClicked() {
        decisionEvent.setValue(false);
    }

    public void sendAckToSenderViaStomp(RequestModel requestModel) {

        compositeDisposable.add(modelRepository
                .stompSendMessage("/app/req/" + requestModel.getReqSenderName(), gson.toJson(requestModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    public void addFriendUserInformation(String friendUserName) {
        modelRepository.retrofitGetUserInformationModel(friendUserName)
                .subscribe(new SingleObserver<UserInformationModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserInformationModel userInformationModel) {
                        // Add To Database
                        insertClientDBUserInformationModel(userInformationModel);
                        // Add To ArrayList
                        modelRepository.addUserInformationModel(userInformationModel);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertClientDBUserInformationModel(UserInformationModel userInformationModel) {
        modelRepository.insertClientDBUserInformationModel(userInformationModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        // Insert To SQLite Successfully
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
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
