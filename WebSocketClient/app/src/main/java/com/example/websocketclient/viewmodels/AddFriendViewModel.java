package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.retrofit.models.RegisterModel;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddFriendViewModel extends AndroidViewModel {

    private Context context;
    private ModelRepository modelRepository;
    private UserInformation userInformation;
    private RegisterModel registerModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableField<String> userNameEdit = new ObservableField<>();

    public MutableLiveData<String> isUserExist;

    public AddFriendViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
    }

    public LiveData<String> getRetrofitEvent() {
        return isUserExist = new MutableLiveData<>();
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
                        isUserExist.setValue(registerModel.getUserName());
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
        //compositeDisposable.dispose();
    }
}
