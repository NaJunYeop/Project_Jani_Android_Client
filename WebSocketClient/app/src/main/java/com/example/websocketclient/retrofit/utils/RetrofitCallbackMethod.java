package com.example.websocketclient.retrofit.utils;

import com.example.websocketclient.retrofit.models.RegisterModel;

public interface RetrofitCallbackMethod<T> {
    void onError(Throwable t);
    void onSuccess(RegisterModel registerModel);
}
