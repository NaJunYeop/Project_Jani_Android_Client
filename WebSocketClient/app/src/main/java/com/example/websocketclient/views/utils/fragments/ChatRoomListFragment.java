package com.example.websocketclient.views.utils.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.FragmentChatRoomListBinding;
import com.example.websocketclient.viewmodels.ChatRoomListViewModel;
import com.example.websocketclient.views.ChatRoomActivity;
import com.example.websocketclient.views.utils.adapters.ChatRoomListAdapter;

public class ChatRoomListFragment extends Fragment {
    private FragmentChatRoomListBinding fragmentChatRoomListBinding;
    private ChatRoomListViewModel chatRoomListViewModel;
    private ChatRoomListAdapter chatRoomListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentChatRoomListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_room_list, container, false);
        fragmentChatRoomListBinding.setLifecycleOwner(getViewLifecycleOwner());

        chatRoomListViewModel = ViewModelProviders.of(this).get(ChatRoomListViewModel.class);
        fragmentChatRoomListBinding.setChatRoomListVieWModel(chatRoomListViewModel);

        chatRoomListAdapter = new ChatRoomListAdapter(chatRoomListViewModel);
        fragmentChatRoomListBinding.chatRoomListRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        fragmentChatRoomListBinding.chatRoomListRecyclerView.setAdapter(chatRoomListAdapter);

        getLiveDataEvent();

        return fragmentChatRoomListBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (chatRoomListAdapter != null) {
            chatRoomListAdapter.notifyDataSetChanged();
        }
    }

    public void getLiveDataEvent() {
        chatRoomListViewModel.getMessageEvent()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer flag) {
                        if (flag == 0) {
                            chatRoomListAdapter.notifyDataSetChanged();
                        }
                        else if (flag == 1) {
                            Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
