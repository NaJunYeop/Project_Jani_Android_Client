package com.example.websocketclient.views.utils.adapters;

import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

public class CustomAdapters {

    @BindingAdapter("createChatChannelButtonVisibility")
    public static void setCreateChatChannelButtonVisibility(View view, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

}
