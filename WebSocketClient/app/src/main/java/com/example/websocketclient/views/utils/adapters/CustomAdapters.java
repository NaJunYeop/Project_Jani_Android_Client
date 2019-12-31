package com.example.websocketclient.views.utils.adapters;

import android.widget.EditText;

import androidx.databinding.BindingAdapter;

public class CustomAdapters {

    @BindingAdapter("binding")
    public static void makeEditTextEmpty(EditText view, CharSequence input) {
        if (view.getText().toString().equals(input.toString())) {
            view.setText("");
        }
    }
}
