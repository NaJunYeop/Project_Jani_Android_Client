package com.example.websocketclient.views.utils.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.websocketclient.R;
import com.example.websocketclient.databinding.LeftSpeechBubbleBinding;
import com.example.websocketclient.databinding.RightSpeechBubbleBinding;
import com.example.websocketclient.models.MessageModel;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.MainActivity;
import com.example.websocketclient.views.utils.viewholders.LeftSpeechBubbleViewHolder;
import com.example.websocketclient.views.utils.viewholders.RightSpeechBubbleViewHolder;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "ChatRoomAdapterLog";
    private final int SENDER = 0;
    private final int RECEIVER = 1;
    private MainViewModel mainViewModel;
    private LayoutInflater inflater;

    public ChatRoomAdapter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    // onCreateViewHolder를 실행하기 전에 Scarpeed View Pool에 현재 ViewType과 같은 ViewHolder가 있으면 새로운 ViewHolder 객체를 생성하지 않고
    // 존재하는 ViewHolder를 Return한다.
    // Scarped View란 현재 RecyclerView에 붙어있지만 곧 제거되거나 재사용될 것으로 지정된 ViewHolder이다.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        Log.i(TAG, "onCreateViewHolder is called");

        // 왼쪽 말풍선(상대방)
        LeftSpeechBubbleBinding leftSpeechBubbleBinding = DataBindingUtil.inflate(inflater, R.layout.left_speech_bubble, parent, false);

        // 오른쪽 말풍선(자기 자신)
        RightSpeechBubbleBinding rightSpeechBubbleBinding = DataBindingUtil.inflate(inflater, R.layout.right_speech_bubble, parent, false);

        // 보내는 사람 즉, 자기 자신 일 경우 ViewHolder 객체 Return
        if (viewType == SENDER) {
            return new RightSpeechBubbleViewHolder(rightSpeechBubbleBinding);
        }
        // 받는 사람 즉, 다른 사람이 보낸 Message를 받을 경우 ViewHolder 객체 Return
        else {
            return new LeftSpeechBubbleViewHolder(leftSpeechBubbleBinding);
        }
    }

    // onCreateViewHolder에서 생성된 ViewHolder를 가져와서 현재 Position에 맞는 Data를 ViewHolder안의 View들에게 Binding해준다.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (userDivider(mainViewModel.getMessageModelAt(position).getSenderName()) == SENDER) {
            ((RightSpeechBubbleViewHolder)holder).setBinding(mainViewModel, position);
        }
        else {
            ((LeftSpeechBubbleViewHolder)holder).setBinding(mainViewModel, position);
        }

    }

    // 현재 Position에 해당하는 ViewType을 판단한다.
    @Override
    public int getItemViewType(int position) {
        return userDivider(mainViewModel.getMessageModelAt(position).getSenderName());
    }

    // ItemCount 수만큼 getItemViewType(), onCreateViewHolder(), onBindViewHolder가 연속적으로 호출된다.
    @Override
    public int getItemCount() {
        return mainViewModel.getChatAdapterArrayList().size();
    }

    public int userDivider(String userName) {
        if (MainActivity.intentUserInformation.getUserName().equals(userName)) {
            return SENDER;
        }
        else {
            return RECEIVER;
        }
    }
}
