package com.example.websocketclient.views.fragments;


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
import com.example.websocketclient.databinding.FragmentFriendListBinding;
import com.example.websocketclient.viewmodels.FriendListFragmentViewModel;
import com.example.websocketclient.views.activities.AddFriendActivity;
import com.example.websocketclient.views.activities.RequestFriendActivity;
import com.example.websocketclient.views.activities.UserProfileActivity;
import com.example.websocketclient.views.adapters.FriendListAdapter;

public class FriendListFragment extends Fragment {

    private FragmentFriendListBinding fragmentFriendListBinding;
    private FriendListFragmentViewModel friendListFragmentViewModel;
    private FriendListAdapter friendListAdapter;

    private final int ADD = 0;
    private final int REQ = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentFriendListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, container, false);
        fragmentFriendListBinding.setLifecycleOwner(getViewLifecycleOwner());

        friendListFragmentViewModel = ViewModelProviders.of(this).get(FriendListFragmentViewModel.class);
        fragmentFriendListBinding.setFriendListViewModel(friendListFragmentViewModel);

        friendListAdapter = new FriendListAdapter(friendListFragmentViewModel);
        fragmentFriendListBinding.friendListRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        fragmentFriendListBinding.friendListRecyclerView.setAdapter(friendListAdapter);

        getLiveDataEvent();

        return fragmentFriendListBinding.getRoot();
    }

    public void getLiveDataEvent() {
        // getViewLifecycleOwner() or getViewLifecycleOwnerLiveData() : LiveData will remove observers every time the fragment's view is destroyed.
        // 당겨서 새로고침 하는 Event를  ViewModel로 부터 받는다.
        friendListFragmentViewModel.getRefreshEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            if (friendListAdapter != null) {
                                friendListAdapter.notifyDataSetChanged();
                                friendListFragmentViewModel.onComplete();
                            }
                        }
                    }
                });


        // Button Click Event를 ViewModel로 부터 받는다.
        friendListFragmentViewModel.getButtonClickEvent()
                .observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer command) {
                        if (command == ADD) {
                            // 친구추가 Activity로 Intent
                            Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                            startActivity(intent);
                        }
                        else if (command == REQ) {
                            // 친구요청 Activity로 Intent
                            Intent intent = new Intent(getActivity(), RequestFriendActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        friendListFragmentViewModel.getProfileEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean check) {
                        if (check) {
                            // 프로필 Activity로 Intent
                            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (friendListAdapter != null) {
            friendListAdapter.notifyDataSetChanged();
        }
    }
}
