package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.databinding.ActivityRegisterBinding;
import com.example.websocketclient.R;
import com.example.websocketclient.viewmodels.RegisterViewModel;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivityLog";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RegisterViewModel registerViewModel;
    private AppDatabase db;
    private Completable completable;
    private ActivityRegisterBinding activityRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Test 하면서 SQLite User Information 삭제 할 일 있으면 밑의 주석을 없애고 실행하시오.
        /*db = AppDatabase.getInstance(this);
        completable = db.userDao().deleteAll();
        completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                });*/

        // 이 Activity(RegisterActivity)를 띄워주기 전에 먼저 SQLite DB에서 사용자를 조회한 후
        // 사용자가 존재한다면 바로 Main Activity로 전환
        // 사용자가 존재하지 않는다면 계정을 등록하는 Register Acitivty를 띄워준다.
        beforeShowRegisterView();

        // SQLite DB를 조회하여 사용자가 존재하는지에 대한 Event를 ViewModel로 부터 받는다.
        registerViewModel.getClientDBEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String result) {
                        if (result.equals("N_EXIST")) {
                            showRegisterView();
                        }
                        /*else {
                            // ["EXIST" : 이미 가입된 사용자]
                            // ["SUCCESS" : 가입 성공 시]
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }*/
                    }
                });

        registerViewModel.getLoadEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String result) {
                        if (result.equals("FIN_LOAD")) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void dataBindingInit() {
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        activityRegisterBinding.setLifecycleOwner(this);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        activityRegisterBinding.setRegisterViewModel(registerViewModel);
    }

    public void beforeShowRegisterView() {
        dataBindingInit();

        registerViewModel.checkRegisterModelExist();
    }

    public void showRegisterView() {

        // SpringBoot Tomcat WAS(이하 Server)와 통신하여 사용자가 입력한 User Name이 사용가능한지 검사한다.
        // DUP : Server와 연결된 MySQL에 이미 존재하는 User Name
        registerViewModel.getRetrofitEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                        if (s.equals("DUP")) {
                            Log.i(TAG, "From server : User name already exist");
                            Toast.makeText(RegisterActivity.this, "존재하는 이름입니다.\n다른 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (s.equals("CHK")){
                            // 아이디를 사용할 것인지 Dialog로 물어봐야한다.
                            // 중복된 User Name이 아닌 경우 MySQL에 저장.
                            Toast.makeText(RegisterActivity.this, "계정을 생성합니다.", Toast.LENGTH_LONG).show();
                            registerViewModel.storeUserRegisterModelToServer();
                        }
                    }
                });

        registerViewModel.getServerDBEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("FAIL")) {
                            // Some Business Logic Here ...
                        }
                    }
                });
    }

    // Main Activity로 화면을 저장 할 경우 더 이상 이 Activity는 필요 없으므로 종료시켜 준다.
    // finish() Method를 사용하지 않으면 Main Activity에서 뒤로가기 버튼을 눌렀을 때 바로 종료되지 않고 이 Activity가 다시 실행된다.
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    // 이 Activity가 onDestroy되어서 완전히 종료 될 경우 Observable, Maybe, Completable, Single 등을 구독하고 있는 Observer들의 구독을 해지한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
