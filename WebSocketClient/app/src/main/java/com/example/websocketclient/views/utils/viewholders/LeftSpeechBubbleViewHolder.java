package com.example.websocketclient.views.utils.viewholders;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.databinding.LeftSpeechBubbleBinding;
import com.example.websocketclient.models.MessageModel;
import com.example.websocketclient.viewmodels.ChatRoomViewModel;
import com.example.websocketclient.viewmodels.MainViewModel;

public class LeftSpeechBubbleViewHolder extends RecyclerView.ViewHolder {
    private LeftSpeechBubbleBinding leftSpeechBubbleBinding;

    public LeftSpeechBubbleViewHolder(@NonNull LeftSpeechBubbleBinding leftSpeechBubbleBinding) {
        super(leftSpeechBubbleBinding.getRoot());
        this.leftSpeechBubbleBinding = leftSpeechBubbleBinding;
    }

    public void setBinding(ChatRoomViewModel chatRoomViewModel, int position) {
        leftSpeechBubbleBinding.setChatRoomViewModel(chatRoomViewModel);
        //leftSpeechBubbleBinding.setMessageModel(messageModel);
        leftSpeechBubbleBinding.setPosition(position);
        //This forces the bindings to run immediately instead of delaying them until the next frame.
        leftSpeechBubbleBinding.executePendingBindings();
    }
}
