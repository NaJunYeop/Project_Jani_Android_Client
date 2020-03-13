package com.example.websocketclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.FragmentSchedulerBinding;
import com.example.websocketclient.viewmodels.MainViewModel;

public class SchedulerFragment extends Fragment {

    private FragmentSchedulerBinding fragmentSchedulerBinding;
    private MainViewModel mainViewModel;

    public SchedulerFragment(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSchedulerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scheduler, container, false);
        fragmentSchedulerBinding.setMainViewModel(mainViewModel);
        return fragmentSchedulerBinding.getRoot();
    }
}
