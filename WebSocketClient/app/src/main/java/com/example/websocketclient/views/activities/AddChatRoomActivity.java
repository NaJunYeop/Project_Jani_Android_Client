package com.example.websocketclient.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityAddChatRoomBinding;
import com.example.websocketclient.viewmodels.AddChatRoomViewModel;
import com.example.websocketclient.views.adapters.AddChatRoomAdapter;

public class AddChatRoomActivity extends AppCompatActivity {

    private ActivityAddChatRoomBinding activityAddChatRoomBinding;
    private AddChatRoomViewModel addChatRoomViewModel;
    private AddChatRoomAdapter addChatRoomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBindingInit();
        stompLiveDataInit();
    }

    public void dataBindingInit() {


        activityAddChatRoomBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_chat_room);

        // set LifecycleOwner
        activityAddChatRoomBinding.setLifecycleOwner(this);

        // Binds AddChatRoomViewModel.class

        addChatRoomViewModel = ViewModelProviders.of(this).get(AddChatRoomViewModel.class);
        activityAddChatRoomBinding.setAddChatRoomViewModel(addChatRoomViewModel);

        activityAddChatRoomBinding.activityAddChatRoomRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        addChatRoomAdapter = new AddChatRoomAdapter(activityAddChatRoomBinding.getAddChatRoomViewModel());

        activityAddChatRoomBinding.activityAddChatRoomRecyclerview.setAdapter(addChatRoomAdapter);
    }

    public void stompLiveDataInit() {
        addChatRoomViewModel.getCommandEvent()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer == 1) {
                            Intent intent = new Intent(AddChatRoomActivity.this, ChatRoomActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
