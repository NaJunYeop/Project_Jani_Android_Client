package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RequestModel;
import com.google.gson.Gson;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddFriendViewModel extends AndroidViewModel {

    private final String TAG = "AddFriendLog";
    private final int OK = 0;
    private final int NO = 1;

    private Context context;
    private ModelRepository modelRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableField<String> userNameEdit = new ObservableField<>();
    public ObservableField<String> textViewString = new ObservableField<>();

    public ObservableField<String> oneButtonTextView = new ObservableField<>();
    public ObservableField<String> twoButtonTextView = new ObservableField<>();

    private MutableLiveData<String> isUserExist;
    private MutableLiveData<Integer> commandEvent;

    private Gson gson = new Gson();

    public AddFriendViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
    }

    public LiveData<String> getRetrofitEvent() {
        return isUserExist = new MutableLiveData<>();
    }

    public LiveData<Integer> getCommandEvent() {
        return commandEvent = new MutableLiveData<>();
    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public void findUserButtonClicked() {

        String targetUser = userNameEdit.get();

        // 이미 등록되어 있는 친구일 경우에 대한 처리
        for (UserInformationModel item : modelRepository.getUserInformationModels()) {
            if (item.getUserInfoUserName().equals(userNameEdit.get())) {
                isUserExist.setValue("ALREADY_EXIST");
                oneButtonTextView.set("[ " + targetUser + " ] 님은 이미 친구로 등록되어 있습니다.");
                return;
            }
        }

        // 이미 해당 친구에게 요청이 와 있을 경우에 대한 처리
        for (RequestModel item : modelRepository.getRequestModels()) {
            if (item.getReqSenderName().equals(userNameEdit.get())) {
                isUserExist.setValue("REQUEST_EXIST");
                twoButtonTextView.set("[ " + targetUser + " ] 님으로 부터 친구요청이 존재합니다. 친구로 추가 하시겠습니까?");
                return;
            }
        }

        modelRepository.retrofitFindUserInformationModel(userNameEdit.get())
                .subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("NOT_EXIST")) {
                            // 해당 사용자가 존재하지 않습니다 라는 Dialog 띄워주기
                            isUserExist.setValue(result);
                            oneButtonTextView.set("해당 사용자는 존재하지 않습니다.");
                        }
                        else if (result.equals("FOUND")) {
                            // 해당 사용자에게 친구 요청을 하시겠습니까 라는 Dialog 띄워주기
                            isUserExist.setValue(result);
                            twoButtonTextView.set("[ " + targetUser + " ] 님에게 친구요청을 하시겠습니까?");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isUserExist.setValue("ERROR");
                    }

                    @Override
                    public void onComplete() {
                        isUserExist.setValue("NOTHING");
                    }
                });
    }

    public void noButtonClicked() {
        commandEvent.setValue(1);
        userNameEdit.set("");
    }

    public void acceptButtonClicked() {
        String friendUserName = userNameEdit.get();
        if (isUserExist.getValue().equals("REQUEST_EXIST")) {
            // 해당 사용자로부터 요청이 존재할 경우

            // 해당 사용자를 친구로 추가한다.
            addFriendUserInformation(friendUserName);
            // 해당 RequestModel을 ArrayList에서 삭제한다.
            modelRepository.deleteRequestModel(friendUserName);
            // 해당 RequestModel을 SQLite에서 삭제한다.
            deleteRequestModelFromClientDB(friendUserName) ;
        }
        else {
            // 해당 사용자에게 요청을 할 경우
            // RequestModel을 해당 사용자에게 전송한다.
            requestFriendToUser(friendUserName);
        }
    }

    public void deleteRequestModelFromClientDB(String receiverName) {
        modelRepository.deleteRequestModelFromClientDB(receiverName)
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

    public void requestFriendToUser(String friendUserName) {
        RequestModel requestModel = new RequestModel(1, modelRepository.getUserRegisterModel().getRegUserName(), friendUserName);

        compositeDisposable.add(modelRepository.stompSendMessage("/app/req/" + friendUserName, gson.toJson(requestModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                    commandEvent.setValue(2);

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
                        insertClientDBUserInformationModel(userInformationModel);
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

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void dismissButtonClicked() {
        commandEvent.setValue(1);
        userNameEdit.set("");
    }

    public void notExistButtonClicked() {
        commandEvent.setValue(2);
        userNameEdit.set("");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
