package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityRequestFriendUnitBinding;
import com.example.websocketclient.databinding.ActivityUserProfileBinding;
import com.example.websocketclient.viewmodels.UserProfileViewModel;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding activityUserProfileBinding;
    private UserProfileViewModel userProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBinding();
        getLiveDataEvent();
    }

    public void initDataBinding() {
        activityUserProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        activityUserProfileBinding.setLifecycleOwner(this);

        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        activityUserProfileBinding.setUserProfileViewModel(userProfileViewModel);
    }

    public void getLiveDataEvent() {
        userProfileViewModel.getProfileButtonEvent()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer == 1) {
                            Intent intent = new Intent(UserProfileActivity.this, ChatRoomActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
