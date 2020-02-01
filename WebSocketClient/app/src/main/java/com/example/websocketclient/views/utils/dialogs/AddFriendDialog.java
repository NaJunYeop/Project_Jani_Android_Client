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
import com.example.websocketclient.databinding.AddFriendOneButtonDialogBinding;
import com.example.websocketclient.databinding.AddFriendTwoButtonDialogBinding;
import com.example.websocketclient.viewmodels.AddFriendViewModel;

public class AddFriendDialog extends DialogFragment {
    //private DialogAddFriendBinding dialogAddFriendBinding;
    private AddFriendViewModel addFriendViewModel;
    private AddFriendOneButtonDialogBinding addFriendOneButtonDialogBinding;
    private AddFriendTwoButtonDialogBinding addFriendTwoButtonDialogBinding;
    private final int ONE_BUTTON = 1;
    private final int TWO_BUTTON = 2;
    private int type;

    public AddFriendDialog(AddFriendViewModel addFriendViewModel, int type) {
        this.addFriendViewModel = addFriendViewModel;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (type == ONE_BUTTON) {
            addFriendOneButtonDialogBinding = DataBindingUtil.inflate(inflater, R.layout.add_friend_one_button_dialog, container, false);
            addFriendOneButtonDialogBinding.setAddFriendViewModel(addFriendViewModel);
            return addFriendOneButtonDialogBinding.getRoot();
        }
        else {
            addFriendTwoButtonDialogBinding = DataBindingUtil.inflate(inflater, R.layout.add_friend_two_button_dialog, container, false);
            addFriendTwoButtonDialogBinding.setAddFriendViewModel(addFriendViewModel);
            return addFriendTwoButtonDialogBinding.getRoot();
        }
    }
}
