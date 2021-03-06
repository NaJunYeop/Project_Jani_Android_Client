package com.example.websocketclient.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityChatRoomBinding;
import com.example.websocketclient.viewmodels.ChatRoomViewModel;
import com.example.websocketclient.views.adapters.ChatRoomAdapter;

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

        //activityChatRoomBinding.setChatRoomAdapter(chatRoomAdapter);
        activityChatRoomBinding.recyclerView.setAdapter(chatRoomAdapter);
    }

    public void getLiveDataEvent() {
        activityChatRoomBinding.recyclerView.scrollToPosition(chatRoomAdapter.getItemCount() - 1);
        chatRoomViewModel.getMessageEvent()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer flag) {
                        if (flag == 0) {
                            activityChatRoomBinding.recyclerView.scrollToPosition(chatRoomAdapter.getItemCount() - 1);
                        }
                    }
                });
    }
}
