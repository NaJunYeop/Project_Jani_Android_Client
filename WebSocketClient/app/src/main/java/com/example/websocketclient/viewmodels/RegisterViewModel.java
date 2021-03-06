package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.database.entity.RegisterModel;
import com.example.websocketclient.models.PlainTextModel;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegisterViewModel extends AndroidViewModel {

    private final String TAG = "RegisterViewModelLog";

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ModelRepository modelRepository;

    // MutableLiveData : setValue시 Main-Thread에서 동작하며 이를 Observe하고 있는 Observer에게 Notify
    private MutableLiveData<String> clientDBEvent;
    private MutableLiveData<String> serverDBEvent;
    private MutableLiveData<String> retrofitEvent;
    private MutableLiveData<String> loadEvent;

    // data-binding을 위해 ObservableField 객체 생성.
    public ObservableField<String> userNameEdit = new ObservableField<>();
    public ObservableField<String> userPasswordEdit = new ObservableField<>();

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
    public LiveData<String> getRetrofitEvent() { return retrofitEvent = new MutableLiveData<>(); }

    public LiveData<String> getClientDBEvent() {
        return clientDBEvent = new MutableLiveData<>();
    }

    public LiveData<String> getServerDBEvent() {
        return serverDBEvent = new MutableLiveData<>();
    }

    public LiveData<String> getLoadEvent() {
        return loadEvent = new MutableLiveData<>();
    }

    public void prefetchingDataFromClientDB() {
        modelRepository.loadData()
                .subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // result == "FIN_LOAD"
                        loadEvent.setValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadEvent.setValue("LOAD_ERROR");
                    }

                    @Override
                    public void onComplete() {
                        loadEvent.setValue("LOAD_EMPTY");
                    }
                });
    }

    public void checkRegisterModelExist() {
        modelRepository.getClientDBRegisterModel()
                .subscribe(new MaybeObserver<RegisterModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    // SQLite에 RegisterModel이 등록되어 있을 경우
                    @Override
                    public void onSuccess(RegisterModel registerModel) {
                        modelRepository.setUserRegisterModel(registerModel);
                        clientDBEvent.setValue("EXIST");
                        prefetchingDataFromClientDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    // SQLite에 RegisterModel이 등록되어 있지 않은 경우
                    @Override
                    public void onComplete() {
                        clientDBEvent.setValue("N_EXIST");
                    }
                });
    }

    public void storeUserRegisterModelToClient(RegisterModel registerModel) {
        modelRepository.insertClientDBRegisterModel(registerModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        modelRepository.setUserRegisterModel(registerModel);
                        clientDBEvent.setValue("SUCCESS");

                        userNameEdit.set("");
                        userPasswordEdit.set("");

                        prefetchingDataFromClientDB();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void storeUserRegisterModelToServer() {
        RegisterModel registerModel = new RegisterModel(userNameEdit.get(), userPasswordEdit.get());

        modelRepository.insertServerDBRegisterModel(registerModel)
                .subscribe(new SingleObserver<PlainTextModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PlainTextModel plainTextModel) {
                        storeUserRegisterModelToClient(registerModel);
                        serverDBEvent.setValue("REG_DONE");

                    }

                    @Override
                    public void onError(Throwable e) {
                        serverDBEvent.setValue("FAIL");
                    }
                });
    }

    public void registerButtonClicked() {
        Log.d("registerChecking", "registerButtonClicked !!!");
        modelRepository.retrofitCheckDuplicateUserName(userNameEdit.get())
                .subscribe(new SingleObserver<PlainTextModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PlainTextModel res) {
                        retrofitEvent.setValue(res.getText());

                        Log.d("registerChecking", res.getText());

                        userNameEdit.set("");
                        userPasswordEdit.set("");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
