package com.example.websocketclient.views.utils.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityAddChatRoomUnitBinding;
import com.example.websocketclient.viewmodels.AddChatRoomViewModel;

public class AddChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AddChatRoomViewModel addChatRoomViewModel;
    private LayoutInflater inflater;
    private ActivityAddChatRoomUnitBinding activityAddChatRoomUnitBinding;

    public AddChatRoomAdapter(AddChatRoomViewModel addChatRoomViewModel) {
        this.addChatRoomViewModel = addChatRoomViewModel;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        activityAddChatRoomUnitBinding = DataBindingUtil.inflate(inflater, R.layout.activity_add_chat_room_unit, parent,false);


        return new AddChatRoomViewHolder(activityAddChatRoomUnitBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AddChatRoomViewHolder)holder).setBinding(addChatRoomViewModel, position);
    }

    @Override
    public int getItemCount() {
        return addChatRoomViewModel.getModelRepository().getUserInformationModels().size();
    }

    public class AddChatRoomViewHolder extends RecyclerView.ViewHolder {

        private ActivityAddChatRoomUnitBinding activityAddChatRoomUnitBinding;

        public AddChatRoomViewHolder(@NonNull ActivityAddChatRoomUnitBinding activityAddChatRoomUnitBinding) {
            super(activityAddChatRoomUnitBinding.getRoot());
            this.activityAddChatRoomUnitBinding = activityAddChatRoomUnitBinding;
        }

        public void setBinding(AddChatRoomViewModel addChatRoomViewModel, int position) {
            activityAddChatRoomUnitBinding.setAddChatRoomViewModel(addChatRoomViewModel);
            activityAddChatRoomUnitBinding.setPosition(position);
            activityAddChatRoomUnitBinding.executePendingBindings();
        }
    }
}