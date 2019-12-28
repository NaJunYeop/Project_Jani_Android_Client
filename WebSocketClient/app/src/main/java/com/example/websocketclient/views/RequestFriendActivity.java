package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityRequestFriendBinding;
import com.example.websocketclient.viewmodels.RequestFriendViewModel;
import com.example.websocketclient.views.utils.adapters.RequestFriendAdapter;
import com.example.websocketclient.views.utils.dialogs.SimpleDialog;

public class RequestFriendActivity extends AppCompatActivity {

    private ActivityRequestFriendBinding activityRequestFriendBinding;
    private RequestFriendViewModel requestFriendViewModel;
    private RequestFriendAdapter requestFriendAdapter;
    private SimpleDialog simpleDialog;

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
        requestFriendViewModel.getDecisionEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            requestFriendAdapter.notifyDataSetChanged();
                        }
                        if (simpleDialog != null) {
                            simpleDialog.dismiss();
                        }
                    }
                });

        requestFriendViewModel.getButtonClickEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            showSimpleDialog();
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

    public void showSimpleDialog() {
        if (simpleDialog == null) simpleDialog = new SimpleDialog(requestFriendViewModel);
        simpleDialog.show(getSupportFragmentManager(), "SimpleDialog");
    }
}
