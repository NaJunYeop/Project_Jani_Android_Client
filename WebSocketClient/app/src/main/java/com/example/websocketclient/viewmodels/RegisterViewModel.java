package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.retrofit.models.RegisterModel;
import com.example.websocketclient.retrofit.utils.RetrofitCommunicationService;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegisterViewModel extends AndroidViewModel {

    private final String TAG = "RegisterViewModelLog";

    private AppDatabase db;
    private RegisterModel registerModel;
    private UserInformation mUserInformation;
    private RetrofitCommunicationService retrofitCommunicationService;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ModelRepository modelRepository;

    // MutableLiveData : setValue시 Main-Thread에서 동작하며 이를 Observe하고 있는 Observer에게 Notify
    private MutableLiveData<String> retMsgButtonClicked;
    private MutableLiveData<UserInformation> isDBInsertSuccess;
    private MutableLiveData<UserInformation> isDBUserExist;

    // data-binding을 위해 ObservableField 객체 생성.
    public ObservableField<String> userNameEdit = new ObservableField<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);

        // getApplicationContext() : LiftCycle을 가지는 Context
        // getBaseContext() : 자신의 Context가 아니라 다른 Context를 Access할 때 사용한다.
        // View.getContext() : 현재 실행되고 있는 View의 Context를 Return, this와 같다.
        this.context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(this.context);
    }

    // 초기 Observe할 MutableLiveData를 LiveData로 반환
    public LiveData<UserInformation> getDBInsEvent() { return isDBInsertSuccess = new MutableLiveData<>(); }

    // 초기 Observe할 MutableLiveData를 LiveData로 반환
    public LiveData<String> getRetrofitEvent() { return retMsgButtonClicked = new MutableLiveData<>(); }

    // 초기 Observe할 MutableLiveData를 LiveData로 반환
    public LiveData<UserInformation> getDBSelEvent() { return isDBUserExist = new MutableLiveData<>(); }

    public void checkUserExist() {
        modelRepository.dbGetUserInformation()
                .subscribe(new MaybeObserver<UserInformation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserInformation userInformation) {
                        modelRepository.setCurUserInformation(userInformation);
                        isDBUserExist.setValue(userInformation);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        UserInformation absent = new UserInformation();
                        absent.setUserName("ABS");
                        isDBUserExist.setValue(absent);
                    }
                });
    }

    public void storeUserInformation() {
        if (mUserInformation == null) mUserInformation = new UserInformation();
        mUserInformation.setUserName(userNameEdit.get());
        mUserInformation.setTopicIdentifier(0);

        modelRepository.dbInsertUserInformation(mUserInformation)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        modelRepository.setCurUserInformation(mUserInformation);
                        isDBInsertSuccess.setValue(mUserInformation);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void registerButtonClicked() {
        if (registerModel == null) {
            registerModel = new RegisterModel.Builder(userNameEdit.get()).build();
        } else {
            registerModel.setUserName(userNameEdit.get());
        }

        modelRepository.retrofitGetUserInformation(registerModel)
                .subscribe(new MaybeObserver<RegisterModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(RegisterModel registerModel) {
                        retMsgButtonClicked.setValue(registerModel.getUserName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
