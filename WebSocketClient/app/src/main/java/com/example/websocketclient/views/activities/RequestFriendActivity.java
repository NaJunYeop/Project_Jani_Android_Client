package com.example.websocketclient.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityRequestFriendBinding;
import com.example.websocketclient.viewmodels.RequestFriendViewModel;
import com.example.websocketclient.views.adapters.RequestFriendAdapter;
import com.example.websocketclient.views.dialogs.RequestFriendDialog;

public class RequestFriendActivity extends AppCompatActivity {

    private ActivityRequestFriendBinding activityRequestFriendBinding;
    private RequestFriendViewModel requestFriendViewModel;
    private RequestFriendAdapter requestFriendAdapter;
    private RequestFriendDialog requestFriendDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBindingInit();
        getLiveDataEvent();
    }

    public void dataBindingInit() {
        activityRequestFriendBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_friend);
        activityRequestFriendBinding.setLifecycleOwner(this);

        requestFriendViewModel = ViewModelProviders.of(this).get(RequestFriendViewModel.class);
        activityRequestFriendBinding.setRequestFriendViewModel(requestFriendViewModel);

        activityRequestFriendBinding.requestFriendRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        requestFriendAdapter = new RequestFriendAdapter(requestFriendViewModel);
        activityRequestFriendBinding.requestFriendRecyclerview.setAdapter(requestFriendAdapter);
    }

    public void getLiveDataEvent() {

        requestFriendViewModel.getButtonClickEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            showTwoButtonDialog();
                        }
                    }
                });

        requestFriendViewModel.getDecisionEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            requestFriendAdapter.notifyDataSetChanged();
                        }
                        if (requestFriendDialog != null) {
                            requestFriendDialog.dismiss();
                        }
                    }
                });

        requestFriendViewModel.getRefreshEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            if (requestFriendAdapter != null) {
                                requestFriendAdapter.notifyDataSetChanged();
                                requestFriendViewModel.onComplete();
                            }
                        }
                    }
                });
    }

    public void showTwoButtonDialog() {
        if (requestFriendDialog == null) requestFriendDialog = new RequestFriendDialog(requestFriendViewModel);
        requestFriendDialog.show(getSupportFragmentManager(), "RequestFriendDialog");
    }
}
