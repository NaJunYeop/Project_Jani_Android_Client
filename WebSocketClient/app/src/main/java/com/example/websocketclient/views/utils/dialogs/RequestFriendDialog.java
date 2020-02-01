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
import com.example.websocketclient.databinding.RequestFriendTwoButtonDialogBinding;
import com.example.websocketclient.viewmodels.RequestFriendViewModel;

public class RequestFriendDialog extends DialogFragment {
    private RequestFriendTwoButtonDialogBinding requestFriendTwoButtonDialogBinding;
    private RequestFriendViewModel requestFriendViewModel;

    public RequestFriendDialog(RequestFriendViewModel requestFriendViewModel) {
        this.requestFriendViewModel = requestFriendViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requestFriendTwoButtonDialogBinding = DataBindingUtil.inflate(inflater, R.layout.request_friend_two_button_dialog, container, false);
        requestFriendTwoButtonDialogBinding.setRequestFriendViewModel(requestFriendViewModel);
        return requestFriendTwoButtonDialogBinding.getRoot();
    }
}
