package com.example.websocketclient.views.utils.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.ActivityRequestFriendUnitBinding;
import com.example.websocketclient.viewmodels.RequestFriendViewModel;

public class RequestFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RequestFriendViewModel requestFriendViewModel;
    private LayoutInflater inflater;
    private ActivityRequestFriendUnitBinding activityRequestFriendUnitBinding;

    public RequestFriendAdapter(RequestFriendViewModel requestFriendViewModel) {
        this.requestFriendViewModel = requestFriendViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        activityRequestFriendUnitBinding = DataBindingUtil.inflate(inflater, R.layout.activity_request_friend_unit, parent,false);

        return new RequestFriendViewHolder(activityRequestFriendUnitBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RequestFriendViewHolder)holder).setBinding(requestFriendViewModel, position);
    }

    @Override
    public int getItemCount() {
        return requestFriendViewModel.getModelRepository().getRequestModelList().size();
    }

    public class RequestFriendViewHolder extends RecyclerView.ViewHolder {

        private ActivityRequestFriendUnitBinding activityRequestFriendUnitBinding;

        public RequestFriendViewHolder(@NonNull ActivityRequestFriendUnitBinding activityRequestFriendUnitBinding) {
            super(activityRequestFriendUnitBinding.getRoot());
            this.activityRequestFriendUnitBinding = activityRequestFriendUnitBinding;
        }

        public void setBinding(RequestFriendViewModel requestFriendViewModel, int position) {
            activityRequestFriendUnitBinding.setRequestFriendViewModel(requestFriendViewModel);
            activityRequestFriendUnitBinding.setPosition(position);
            activityRequestFriendUnitBinding.executePendingBindings();
        }
    }
}
