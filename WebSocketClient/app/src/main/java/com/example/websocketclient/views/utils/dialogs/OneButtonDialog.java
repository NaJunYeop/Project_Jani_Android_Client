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
import com.example.websocketclient.databinding.DialogNotExistFriendBinding;
import com.example.websocketclient.viewmodels.AddFriendViewModel;

public class OneButtonDialog extends DialogFragment {
    private DialogNotExistFriendBinding dialogNotExistFriendBinding;
    private AddFriendViewModel addFriendViewModel;

    public OneButtonDialog(AddFriendViewModel addFriendViewModel) {
        this.addFriendViewModel = addFriendViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialogNotExistFriendBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_not_exist_friend, container, false);
        dialogNotExistFriendBinding.setAddFriendViewModel(addFriendViewModel);

        return dialogNotExistFriendBinding.getRoot();
    }
}
