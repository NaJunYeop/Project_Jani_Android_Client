package com.example.websocketclient.views.adapters;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class CustomAdapters {

    @BindingAdapter("createChatChannelButtonVisibility")
    public static void setCreateChatChannelButtonVisibility(View view, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

}
