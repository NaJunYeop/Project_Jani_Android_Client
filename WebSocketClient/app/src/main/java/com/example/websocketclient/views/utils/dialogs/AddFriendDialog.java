package com.example.websocketclient.views.utils.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.DialogAddFriendBinding;
import com.example.websocketclient.viewmodels.AddFriendViewModel;

public class AddFriendDialog extends DialogFragment {
    private DialogAddFriendBinding dialogAddFriendBinding;
    private AddFriendViewModel addFriendViewModel;

    public AddFriendDialog(AddFriendViewModel addFriendViewModel) {
        this.addFriendViewModel = addFriendViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialogAddFriendBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_friend, container, false);
        dialogAddFriendBinding.setAddFriendViewModel(addFriendViewModel);

        return dialogAddFriendBinding.getRoot();
    }
}
