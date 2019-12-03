package com.example.websocketclient.retrofit.utils;

import com.example.websocketclient.retrofit.models.RegisterModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitCommunicationService {
    @POST("/user-registration")
    Maybe<RegisterModel> getUserInformation(@Body RegisterModel registerModel);

}
