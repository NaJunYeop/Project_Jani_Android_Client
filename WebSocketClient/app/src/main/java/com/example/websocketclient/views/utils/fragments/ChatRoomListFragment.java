package com.example.websocketclient.views.utils.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.FragmentChatRoomListBinding;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.utils.adapters.ChatRoomListAdapter;

public class ChatRoomListFragment extends Fragment {
    private FragmentChatRoomListBinding fragmentChatRoomListBinding;
    private MainViewModel mainViewModel;


    public ChatRoomListFragment(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentChatRoomListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_room_list, container, false);
        fragmentChatRoomListBinding.setMainViewModel(mainViewModel);

        ChatRoomListAdapter chatRoomListAdapter = new ChatRoomListAdapter(fragmentChatRoomListBinding.getMainViewModel());
        fragmentChatRoomListBinding.chatRoomListRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        fragmentChatRoomListBinding.chatRoomListRecyclerView.setAdapter(chatRoomListAdapter);

        return fragmentChatRoomListBinding.getRoot();
    }
}
