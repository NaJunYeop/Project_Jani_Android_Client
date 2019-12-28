package com.example.websocketclient.models;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.retrofit.models.RegisterModel;
import com.example.websocketclient.retrofit.utils.RetrofitClient;
import com.example.websocketclient.retrofit.utils.RetrofitCommunicationService;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.RequestFriendActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ModelRepository {
    public final String TAG = "ModelRepositoryLog";
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";

    private Context context;
    private AppDatabase db;
    private RetrofitCommunicationService retrofitCommunicationService;
    private UserInformation userInformation = new UserInformation();
    private StompClient mStompClient;

    private Map<String, ChatRoomModel> oldChatRoomModelMap = new HashMap<>();
    private Map<String, ChatRoomModel> newChatRoomModelMap = new HashMap<>();
    private HashMap<String, FriendModel> friendModelHashMap = new HashMap<>();
    private HashMap<String, RequestModel> requestModelHashMap = new HashMap<>();

    private ArrayList<ChatRoomModel> oldChatRoomModels = new ArrayList<>();
    private ArrayList<ChatRoomModel> newChatRoomModels = new ArrayList<>();
    private ArrayList<FriendModel> friendModels;
    private ArrayList<RequestModel> requestModels;

    private FriendModel selectedFriendModel;
    private ChatRoomModel selectedChatRoomModel;


    // Singleton Pattern
    private static class LazyHolder {
        private static final ModelRepository instance = new ModelRepository();
    }

    public static ModelRepository getInstance() {
        return LazyHolder.instance;
    }

    public ModelRepository() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ServerModel.SERVER_IP + ":" + ServerModel.SERVER_PORT + "/janiwss/websocket");

        requestModels = new ArrayList<>(requestModelHashMap.values());
        friendModels = new ArrayList<>(friendModelHashMap.values());

        // Some Data Initialization Here... Or Call Initializer Method
        /*oldFriends.add(new FriendModel("나준엽"));
        oldFriends.add(new FriendModel("김용재"));

        RequestModel requestModel = new RequestModel();
        requestModel.setSenderName("sender");
        requestModel.setReceiverName("receiver");
        requestModel.setStatus("REQ");
        requestModels.add(requestModel);*/
    }

    public void setReferences(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(this.context);
        retrofitCommunicationService = RetrofitClient.getInstance();
    }

    public void setSelectedFriendModel(FriendModel selectedFriendModel) {
        this.selectedFriendModel = selectedFriendModel;
    }

    public FriendModel getSelectedFriendModel() {
        return this.selectedFriendModel;
    }

    public void setSelectedChatRoomModel(ChatRoomModel selectedChatRoomModel) {
        this.selectedChatRoomModel = selectedChatRoomModel;
    }

    public ChatRoomModel getSelectedChatRoomModel() {
        return selectedChatRoomModel;
    }

    public HashMap<String, RequestModel> getRequestModelHashMap() {
        return this.requestModelHashMap;
    }

    public ArrayList<RequestModel> getRequestModelList() {
        return this.requestModels;
    }

    public RequestModel getRequestModelAt(int position) {
        return requestModels.get(position);
    }

    public void eraseRequestModelAt(int position) {
        requestModels.remove(position);
    }

    /*public void addRequestModel(RequestModel requestModel) {
        requestModels.add(requestModel);
    }*/

    public HashMap<String, FriendModel> getFriendModelHashMap() {
        return friendModelHashMap;
    }

    public ArrayList<FriendModel> getFriendModelList() {
        return this.friendModels;
    }

    public FriendModel getFriendModelAt(int position) {
        return friendModels.get(position);
    }

    /*public void addFriendList(FriendModel friendModel) {
        friendModels.add(friendModel);
    }*/

    public ArrayList<ChatRoomModel> getChatRoomList() {
        return this.oldChatRoomModels;
    }

    public ChatRoomModel getChatRoomModel(int position) {
        return oldChatRoomModels.get(position);
    }

    public void addChatRoomList(ChatRoomModel chatRoomModel) {
        oldChatRoomModels.add(chatRoomModel);
        newChatRoomModels.add(chatRoomModel);
    }

    public UserInformation getCurUserInformation() {
        return userInformation;
    }

    public void setCurUserInformation(UserInformation userInformation) {
        this.userInformation.setUserName(userInformation.getUserName());
    }

    // ============================= Room Databse ==================================================
    public Maybe<UserInformation> dbGetUserInformation() {
        return db.userDao().isUserExist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable dbInsertUserInformation(UserInformation userInformation) {
        return db.userDao().insertUserName(userInformation).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // =============================== Retrofit ====================================================
    public Maybe<RegisterModel> retrofitGetUserInformation(RegisterModel registerModel) {
        return retrofitCommunicationService.getUserInformation(registerModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<RegisterModel> retrofitFindUserInformation(RegisterModel registerModel) {
        return retrofitCommunicationService.findUserInformation(registerModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // ========================= STOMP over Websocket ==============================================

    public Flowable<LifecycleEvent> stompGetStompClientLifecycle() {
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        return mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<StompMessage> stompGetTopicMessage(String topic) {
        return mStompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable stompSendMessage(String destination, String contents) {
        return mStompClient.send(destination, contents)
                .compose(stompApplySchedulers());
    }

    public void stompConnectStart() {
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));
        mStompClient.connect(headers);
    }

    public void stompDisconnectStart() {
        mStompClient.disconnect();
    }

    // 아마 App이 다른 Thread에서 뭔가가 동작하고 있다고 해도 Message를 Send할 수 있게 조치를 취하는 것 인 듯.
    // .compose에 이 Method를 할당하는데 RxLifecycle을 적용하겠다는 의미이다.
    protected CompletableTransformer stompApplySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
