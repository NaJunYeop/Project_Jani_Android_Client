package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityChatRoomBinding;
import com.example.websocketclient.viewmodels.ChatRoomViewModel;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.utils.adapters.ChatRoomAdapter;

import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class ChatRoomActivity extends AppCompatActivity {

    private final String TAG = "ChatRoomActivityLog";
    private ActivityChatRoomBinding activityChatRoomBinding;
    private ChatRoomAdapter chatRoomAdapter;
    private ChatRoomViewModel chatRoomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBindingInit();
        getLiveDataEvent();
    }

    public void dataBindingInit() {
        activityChatRoomBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);
        activityChatRoomBinding.setLifecycleOwner(this);

        chatRoomViewModel = ViewModelProviders.of(this).get(ChatRoomViewModel.class);
        activityChatRoomBinding.setChatRoomViewModel(chatRoomViewModel);

        activityChatRoomBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Binds ChatRoomAdapter.class
        chatRoomAdapter = new ChatRoomAdapter(chatRoomViewModel);
        activityChatRoomBinding.setChatRoomAdapter(chatRoomAdapter);
        activityChatRoomBinding.recyclerView.setAdapter(chatRoomAdapter);
    }

    public void getLiveDataEvent() {
        chatRoomViewModel.getMessageEvent()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {

                    }
                });
    }
}
