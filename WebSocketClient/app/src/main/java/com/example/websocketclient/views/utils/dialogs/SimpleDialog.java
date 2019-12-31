package com.example.websocketclient.views.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.DialogSimpleBinding;
import com.example.websocketclient.viewmodels.RequestFriendViewModel;

public class SimpleDialog extends DialogFragment {
    private DialogSimpleBinding dialogSimpleBinding;
    private RequestFriendViewModel requestFriendViewModel;

    public SimpleDialog(RequestFriendViewModel requestFriendViewModel) {
        this.requestFriendViewModel = requestFriendViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialogSimpleBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_simple, container, false);
        dialogSimpleBinding.setRequestFriendViewModel(requestFriendViewModel);

        return dialogSimpleBinding.getRoot();
    }
}
