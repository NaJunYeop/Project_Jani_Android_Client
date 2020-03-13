package com.example.websocketclient.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityAddFriendBinding;
import com.example.websocketclient.viewmodels.AddFriendViewModel;
import com.example.websocketclient.views.dialogs.AddFriendDialog;
import com.google.gson.Gson;

public class AddFriendActivity extends AppCompatActivity {
    private ActivityAddFriendBinding activityAddFriendBinding;
    private AddFriendViewModel addFriendViewModel;
    private Gson gson = new Gson();

    private AddFriendDialog addFriendDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBindingInit();
        getLiveDataEvent();
    }

    public void dataBindingInit() {
        activityAddFriendBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        activityAddFriendBinding.setLifecycleOwner(this);

        addFriendViewModel = ViewModelProviders.of(this).get(AddFriendViewModel.class);
        activityAddFriendBinding.setAddFriendViewModel(addFriendViewModel);
    }

    public void getLiveDataEvent() {
        addFriendViewModel.getRetrofitEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("NOT_EXIST") || s.equals("ALREADY_EXIST")) {
                            showOneButtonDialog();
                        } else if (s.equals("FOUND") || s.equals("REQUEST_EXIST")) {
                            showTwoButtonDialog();
                        }
                    }
                });

        addFriendViewModel.getCommandEvent()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String userName) {
                        if (userName.equals("DONE")) {
                            Toast.makeText(AddFriendActivity.this, "요청을 완료했습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if (userName.equals("DENY")) {
                        }
                        else {
                            Toast.makeText(AddFriendActivity.this, userName + "님을 친구로 추가하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        addFriendDialog.dismiss();
                    }
                });

    }

    public void showOneButtonDialog() {
        addFriendDialog = new AddFriendDialog(addFriendViewModel, 1);
        addFriendDialog.show(getSupportFragmentManager(), "AddFriendOneButtonDialog");
    }

    public void showTwoButtonDialog() {
        addFriendDialog = new AddFriendDialog(addFriendViewModel, 2);
        addFriendDialog.show(getSupportFragmentManager(), "AddFriendTwoButtonDialog");
    }
}
