package com.example.websocketclient.views.utils.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.FragmentFriendListUnitBinding;
import com.example.websocketclient.viewmodels.FriendListFragmentViewModel;
import com.example.websocketclient.viewmodels.MainViewModel;

public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "FriendListAdapterLog";
    private FragmentFriendListUnitBinding fragmentFriendListUnitBinding;
    private LayoutInflater inflater;
    private MainViewModel mainViewModel;
    private FriendListFragmentViewModel friendListFragmentViewModel;

    public FriendListAdapter(FriendListFragmentViewModel friendListFragmentViewModel) {
        //this.mainViewModel = mainViewModel;
        this.friendListFragmentViewModel = friendListFragmentViewModel;
        //Log.i(TAG, "" + mainViewModel.getModelRepository().getFriendList().size());
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        fragmentFriendListUnitBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list_unit, parent,false);

        //Log.i(TAG, "" + mainViewModel.getModelRepository().getFriendList().size());

        return new FriendListViewHolder(fragmentFriendListUnitBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FriendListViewHolder)holder).setBinding(friendListFragmentViewModel, position);
    }

    @Override
    public int getItemCount() {
        //Log.i(TAG, "" + friendListFragmentViewModel.getModelRepository().getFriendList().size());
        return friendListFragmentViewModel.getModelRepository().getFriendModelList().size();
    }

    // ViewHolder Class
    public class FriendListViewHolder extends RecyclerView.ViewHolder {

        private FragmentFriendListUnitBinding fragmentFriendListUnitBinding;
        public FriendListViewHolder(@NonNull FragmentFriendListUnitBinding fragmentFriendListUnitBinding) {
            super(fragmentFriendListUnitBinding.getRoot());
            this.fragmentFriendListUnitBinding = fragmentFriendListUnitBinding;
        }

        public void setBinding(FriendListFragmentViewModel friendListFragmentViewModel, int position) {
            //fragmentFriendListUnitBinding.setMainViewModel(mainViewModel);
            fragmentFriendListUnitBinding.setFriendListViewModel(friendListFragmentViewModel);
            fragmentFriendListUnitBinding.setPosition(position);
            fragmentFriendListUnitBinding.executePendingBindings();
        }
    }
}
