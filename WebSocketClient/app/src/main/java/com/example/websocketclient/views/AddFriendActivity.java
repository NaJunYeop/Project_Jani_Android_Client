package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.websocketclient.R;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.databinding.ActivityAddFriendBinding;
import com.example.websocketclient.models.RequestModel;
import com.example.websocketclient.viewmodels.AddFriendViewModel;
import com.google.gson.Gson;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddFriendActivity extends AppCompatActivity {

    private final String TAG = "AddFriendLog";
    private ActivityAddFriendBinding activityAddFriendBinding;
    private AddFriendViewModel addFriendViewModel;
    private RequestModel requestModel;
    private Gson gson = new Gson();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBindingInit();

        addFriendViewModel.getRetrofitEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("NOTEXIST")) {
                            Toast.makeText(AddFriendActivity.this, "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddFriendActivity.this, "추가 하시겠습니까?", Toast.LENGTH_SHORT).show();
                            if (requestModel == null) requestModel = new RequestModel();

                            requestModel.setSenderName(addFriendViewModel.getModelRepository().getCurUserInformation().getUserName());
                            requestModel.setReceiverName(s);
                            requestModel.setStatus("REQ");
                            Log.d(TAG, s);
                            compositeDisposable.add(addFriendViewModel
                                    .getModelRepository()
                                    .stompSendMessage("/app/req/" + s, gson.toJson(requestModel))
                                    .subscribe(() -> {
                                        Log.d(TAG, "STOMP echo send successfully");
                                    }, throwable -> {
                                        Log.e(TAG, "Error send STOMP echo", throwable);
                                    }));
                        }
                    }
                });
    }

    public void dataBindingInit() {
        activityAddFriendBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        activityAddFriendBinding.setLifecycleOwner(this);

        addFriendViewModel = ViewModelProviders.of(this).get(AddFriendViewModel.class);
        activityAddFriendBinding.setAddFriendViewModel(addFriendViewModel);
    }

    public void showAddDialog() {

    }

    public void showNotExistDialog() {

    }
}
