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
import com.example.websocketclient.databinding.FragmentConfigurationBinding;
import com.example.websocketclient.viewmodels.MainViewModel;

public class ConfigurationFragment extends Fragment {

    private FragmentConfigurationBinding fragmentConfigurationBinding;
    private MainViewModel mainViewModel;

    public ConfigurationFragment(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentConfigurationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_configuration, container, false);
        fragmentConfigurationBinding.setMainViewModel(mainViewModel);
        return fragmentConfigurationBinding.getRoot();
    }
}
