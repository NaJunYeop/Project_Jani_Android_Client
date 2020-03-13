package com.example.websocketclient.views.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.databinding.RightSpeechBubbleBinding;
import com.example.websocketclient.viewmodels.ChatRoomViewModel;

public class RightSpeechBubbleViewHolder extends RecyclerView.ViewHolder {
    private RightSpeechBubbleBinding rightSpeechBubbleBinding;

    public RightSpeechBubbleViewHolder(@NonNull RightSpeechBubbleBinding rightSpeechBubbleBinding) {
        super(rightSpeechBubbleBinding.getRoot());
        this.rightSpeechBubbleBinding = rightSpeechBubbleBinding;
    }

    public void setBinding(ChatRoomViewModel chatRoomViewModel, int position) {
        rightSpeechBubbleBinding.setChatRoomViewModel(chatRoomViewModel);
        rightSpeechBubbleBinding.setPosition(position);

        //This forces the bindings to run immediately instead of delaying them until the next frame.
        rightSpeechBubbleBinding.executePendingBindings();
    }
}
