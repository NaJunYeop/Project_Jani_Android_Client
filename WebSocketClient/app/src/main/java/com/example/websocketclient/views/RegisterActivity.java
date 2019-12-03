package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.retrofit.models.RegisterModel;
import com.example.websocketclient.models.ServerModel;
import com.example.websocketclient.R;
import com.example.websocketclient.retrofit.utils.RetrofitClient;
import com.example.websocketclient.retrofit.utils.RetrofitCommunicationService;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivityLog";

    private Button register_btn;
    private EditText user_name_edit;

    private TelephonyManager telephonyManager;
    private String user_name;
    private RegisterModel registerModel;
    private AppDatabase db;

    private CompositeDisposable compositeDisposable;
    private Disposable disposable;
    private Maybe<UserInformation> maybeUserInformation;
    private Maybe<Integer> maybeInteger;
    private Maybe<RegisterModel> maybeRegisterModel;
    private Completable completable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();
        db = AppDatabase.getInstance(this);

        /*
        completable = db.userDao().deleteAll();
        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onComplete() {
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        */

        maybeUserInformation = db.userDao().isUserExist();
        maybeUserInformation.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<UserInformation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserInformation userInformation) {
                        Log.i(TAG, userInformation.getUserName() + "is logged on");
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "Start Account Registration ...");
                        registerAction();
                    }
                });
    }

    public void registerAction() {
        setContentView(R.layout.activity_register);
        register_btn = (Button)findViewById(R.id.register_btn);
        user_name_edit = (EditText)findViewById(R.id.user_name_edit);

        register_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user_name = user_name_edit.getText().toString();
                if (registerModel == null) {
                    registerModel = new RegisterModel.Builder(user_name).build();
                }
                else {
                    registerModel.setUserName(user_name);
                }

                maybeRegisterModel = RetrofitClient.getInstance().getUserInformation(registerModel);
                maybeRegisterModel.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MaybeObserver<RegisterModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onSuccess(RegisterModel registerModel) {
                                if (!disposable.isDisposed()) {
                                    disposable.dispose();
                                }
                                if (registerModel.getUserName().equals("EXT")) {
                                    user_name_edit.setText("");
                                    Log.i(TAG, "From server : Already exist user name");
                                    Toast.makeText(RegisterActivity.this, "존재하는 이름입니다.\n다른 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Log.i(TAG, registerModel.getUserName() + " is logged on !!!");

                                    UserInformation userInformation = new UserInformation();
                                    userInformation.setUserName(registerModel.getUserName());

                                    completable = db.userDao().insertUserName(userInformation);
                                    completable.subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new CompletableObserver() {
                                                @Override
                                                public void onSubscribe(Disposable d) {
                                                    disposable = d;
                                                }

                                                @Override
                                                public void onComplete() {
                                                    if (!disposable.isDisposed()) {
                                                        disposable.dispose();
                                                    }

                                                    Log.i(TAG, "Register to SQLite");
                                                    Log.i(TAG, "Go to MainActivity");
                                                    Toast.makeText(RegisterActivity.this, "등록 완료.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, "From server : Error occured");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }
}
