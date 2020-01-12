package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.FriendModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.RegisterModel;
import com.google.gson.Gson;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddFriendViewModel extends AndroidViewModel {

    private final String TAG = "AddFriendLog";
    private final int OK = 0;
    private final int NO = 1;

    private Context context;
    private ModelRepository modelRepository;
    private UserInformation userInformation;
    private RegisterModel registerModel;
    private RequestModel requestModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableField<String> userNameEdit = new ObservableField<>();
    public ObservableField<String> textViewString = new ObservableField<>();
    public ObservableField<String> oneButtonTextView = new ObservableField<>();

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
        if (registerModel == null) registerModel = new RegisterModel.Builder(userNameEdit.get()).build();
        else registerModel.setUserName(userNameEdit.get());

        modelRepository.retrofitFindUserInformation(registerModel)
                .subscribe(new MaybeObserver<RegisterModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(RegisterModel registerModel) {

                        String targetUser = registerModel.getUserName();

                        if (targetUser.equals("NOTEXIST")) {
                            oneButtonTextView.set("해당 사용자는 존재하지 않습니다.");
                            isUserExist.setValue("NOTEXIST");
                        }
                        else {
                            if(modelRepository.getFriendModelHashMap().containsKey(targetUser) == true) {
                                oneButtonTextView.set("[ " + targetUser + " ] 님은 이미 친구로 등록되어 있습니다.");
                                isUserExist.setValue("ALREADY");
                            } else if (modelRepository.getRequestModelHashMap().containsKey(targetUser) == true) {
                                textViewString.set("[ " + targetUser + " ] 님으로 부터 친구요청이 존재합니다. 친구로 추가 하시겠습니까?");
                                isUserExist.setValue("REQUEST_EXIST");
                            } else {
                                textViewString.set("[ " + targetUser + " ] 님에게 친구요청을 하시겠습니까?");
                                isUserExist.setValue("EXIST");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void okButtonClicked() {
        if (requestModel == null) requestModel = new RequestModel();

        // 이미 해당 사용자로부터 친구 요청이 존재한다면
        if (isUserExist.getValue().equals("REQUEST_EXIST")) {

            requestModel.setSenderName(userNameEdit.get());
            requestModel.setReceiverName(modelRepository.getCurUserInformation().getUserName());

            FriendModel friendModel = new FriendModel(userNameEdit.get());

            // 나의 친구 리스트에 추가한다.
            modelRepository.getFriendModelHashMap().put(userNameEdit.get(), friendModel);
            modelRepository.getFriendModelList().add(friendModel);

            // 해당 친구 요청을 삭제한다.
            modelRepository.getRequestModelList().remove(modelRepository.getRequestModelHashMap().get(userNameEdit.get()));
            modelRepository.getRequestModelHashMap().remove(userNameEdit.get());

            requestModel.setStatus("ACK");
        }
        else {
            requestModel.setSenderName(modelRepository.getCurUserInformation().getUserName());
            requestModel.setReceiverName(userNameEdit.get());

            requestModel.setStatus("REQ");
        }
        compositeDisposable.add(modelRepository.stompSendMessage("/app/req/" + userNameEdit.get(), gson.toJson(requestModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                    commandEvent.setValue(3);

                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));

        commandEvent.setValue(1);
        userNameEdit.set("");
    }

    public void noButtonClicked() {
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
