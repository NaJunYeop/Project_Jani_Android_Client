package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityAddFriendBinding;
import com.example.websocketclient.viewmodels.AddFriendViewModel;
import com.example.websocketclient.views.utils.dialogs.AddFriendDialog;
import com.example.websocketclient.views.utils.dialogs.OneButtonDialog;
import com.google.gson.Gson;

public class AddFriendActivity extends AppCompatActivity {
    private ActivityAddFriendBinding activityAddFriendBinding;
    private AddFriendViewModel addFriendViewModel;
    private Gson gson = new Gson();

    private AddFriendDialog addFriendDialog;
    private OneButtonDialog oneButtonDialog;

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
                        if (s.equals("NOTEXIST") || s.equals("ALREADY")) {
                            showOneButtonDialog();
                        } else if (s.equals("EXIST") || s.equals("REQUEST_EXIST")) {
                            showAddDialog();
                        }
                    }
                });

        addFriendViewModel.getCommandEvent()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer command) {
                        if (command == 1) {
                            addFriendDialog.dismiss();
                        } else if (command == 2) {
                            oneButtonDialog.dismiss();
                        } else if (command == 3) {
                            Toast.makeText(AddFriendActivity.this, "요청을 완료했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void showAddDialog() {
        if (addFriendDialog == null) addFriendDialog = new AddFriendDialog(addFriendViewModel);
        addFriendDialog.show(getSupportFragmentManager(), "AddFriendDialog");
    }

    public void showOneButtonDialog() {
        if (oneButtonDialog == null) oneButtonDialog = new OneButtonDialog(addFriendViewModel);
        oneButtonDialog.show(getSupportFragmentManager(), "OneButtonDialog");
    }
}
