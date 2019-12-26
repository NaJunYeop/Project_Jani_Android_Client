package com.example.websocketclient.views.utils.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.FragmentChatRoomListUnitBinding;
import com.example.websocketclient.viewmodels.MainViewModel;

public class ChatRoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainViewModel mainViewModel;
    private LayoutInflater inflater;
    private FragmentChatRoomListUnitBinding fragmentChatRoomListUnitBinding;

    public ChatRoomListAdapter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());

        fragmentChatRoomListUnitBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_room_list_unit, parent, false);
        return new ChatRoomListViewHolder(fragmentChatRoomListUnitBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatRoomListViewHolder)holder).setBinding(mainViewModel, position);
    }

    @Override
    public int getItemCount() {
        return mainViewModel.getModelRepository().getChatRoomList().size();
    }

    public class ChatRoomListViewHolder extends RecyclerView.ViewHolder {
        private FragmentChatRoomListUnitBinding fragmentChatRoomListUnitBinding;
        public ChatRoomListViewHolder(@NonNull FragmentChatRoomListUnitBinding fragmentChatRoomListUnitBinding) {
            super(fragmentChatRoomListUnitBinding.getRoot());
            this.fragmentChatRoomListUnitBinding = fragmentChatRoomListUnitBinding;
        }

        public void setBinding(MainViewModel mainViewModel, int position) {
            fragmentChatRoomListUnitBinding.setMainViewModel(mainViewModel);
            fragmentChatRoomListUnitBinding.setPosition(position);
            fragmentChatRoomListUnitBinding.executePendingBindings();
        }
    }
}
