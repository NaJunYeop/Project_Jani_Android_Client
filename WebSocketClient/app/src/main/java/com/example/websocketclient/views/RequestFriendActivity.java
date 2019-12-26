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

public class RequestFriendActivity extends AppCompatActivity {

    private ActivityRequestFriendBinding activityRequestFriendBinding;
    private RequestFriendViewModel requestFriendViewModel;
    private RequestFriendAdapter requestFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBindingInit();

        requestFriendViewModel.getDecisionEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean b) {
                        if (b) {
                            requestFriendAdapter.notifyDataSetChanged();
                        }
                    }
                });
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
}
