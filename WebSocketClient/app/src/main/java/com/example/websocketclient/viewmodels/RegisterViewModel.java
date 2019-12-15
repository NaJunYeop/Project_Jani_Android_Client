package com.example.websocketclient.viewmodels;

import android.content.Context;
import android.util.Log;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.retrofit.models.RegisterModel;
import com.example.websocketclient.retrofit.utils.RetrofitClient;
import com.example.websocketclient.retrofit.utils.RetrofitCommunicationService;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {

    private final String TAG = "RegisterViewModelLog";

    private AppDatabase db;
    private RegisterModel registerModel;
    private UserInformation mUserInformation;
    private RetrofitCommunicationService retrofitCommunicationService;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    // MutableLiveData : setValue시 Main-Thread에서 동작하며 이를 Observe하고 있는 Observer에게 Notify
    private MutableLiveData<String> retMsgButtonClicked;
    private MutableLiveData<String> isDBInsertSuccess;
    private MutableLiveData<UserInformation> isDBUserExist;

    // data-binding을 위해 ObservableField 객체 생성.
    public ObservableField<String> userNameEdit = new ObservableField<>();

    public RegisterViewModel(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(context);
        retrofitCommunicationService = RetrofitClient.getInstance();
    }

    // 초기 Observe할 MutableLiveData를 LiveData로 반환
    public LiveData<String> getDBInsEvent() { return isDBInsertSuccess = new MutableLiveData<>(); }

    // 초기 Observe할 MutableLiveData를 LiveData로 반환
    public LiveData<String> getRetrofitEvent() { return retMsgButtonClicked = new MutableLiveData<>(); }

    // 초기 Observe할 MutableLiveData를 LiveData로 반환
    public LiveData<UserInformation> getDBSelEvent() { return isDBUserExist = new MutableLiveData<>(); }

    public void checkUserExist() {
        db.userDao().isUserExist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<UserInformation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserInformation userInformation) {
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

        db.userDao().insertUserName(mUserInformation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        isDBInsertSuccess.setValue("SUCC");
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

        retrofitCommunicationService.getUserInformation(registerModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
