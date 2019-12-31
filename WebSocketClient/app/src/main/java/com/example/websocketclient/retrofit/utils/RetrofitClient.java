package com.example.websocketclient.retrofit.utils;

import com.example.websocketclient.models.ServerModel;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static class LazyHolder {
        public static final RetrofitCommunicationService instance = new Retrofit.Builder()
                .baseUrl(ServerModel.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitCommunicationService.class);
    }

    private RetrofitClient() {

    }

    public static RetrofitCommunicationService getInstance() {
        return LazyHolder.instance;
    }
}
