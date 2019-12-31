package com.example.websocketclient.models;

import android.content.Context;
import android.util.Log;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.retrofit.models.RegisterModel;
import com.example.websocketclient.retrofit.utils.RetrofitClient;
import com.example.websocketclient.retrofit.utils.RetrofitCommunicationService;
import com.example.websocketclient.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    private HashMap<String, ChatRoomModel> chatRoomModelHashMap = new HashMap<>();
    private HashMap<String, FriendModel> friendModelHashMap = new HashMap<>();
    private HashMap<String, RequestModel> requestModelHashMap = new HashMap<>();

    private ArrayList<ChatRoomModel> chatRoomModels;
    private ArrayList<FriendModel> friendModels;
    private ArrayList<RequestModel> requestModels;

    //private Observable<Boolean> doneInitiation;
    private Observable<Integer> doneInitiation;

    private FriendModel selectedFriendModel;
    private ChatRoomModel selectedChatRoomModel;

    private MainViewModel mainViewModel;

    private Disposable disposable;

    private int count = 0;


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
        chatRoomModels = new ArrayList<>(chatRoomModelHashMap.values());
    }

    public Observable<Boolean> setInitialModels() {

        setInitialRequestModelHashMap();
        setInitialFriendModelHashMap();
        setInitialChatRoomModelHashMap();

        while (true) {
            if (count == 4) {
                count = 0;
                return Observable.just(true);
            }
        }
    }

    public void setReferences(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(this.context);
        retrofitCommunicationService = RetrofitClient.getInstance();
    }

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    public MainViewModel getMainViewModel() {
        return this.mainViewModel;
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

    // ================================== RequestModel =============================================

    public Observable<List<RequestModel>> setInitialRequestModelHashMap() {
        return retrofitCommunicationService.getRequstModelList(userInformation.getUserName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public void addRequestModel(RequestModel requestModel) {
        if (requestModelHashMap.containsKey(requestModel.getSenderName()) == false) {
            requestModelHashMap.put(requestModel.getSenderName(), requestModel);
            requestModels.add(requestModelHashMap.get(requestModel.getSenderName()));
        }
    }

    public void eraseRequestModelByPosition(int position) {
        requestModelHashMap.remove(requestModels.get(position).getSenderName());
        requestModels.remove(position);
    }

    // ================================== FriendModel ==============================================

    public Observable<List<FriendModel>> setInitialFriendModelHashMap() {
        return retrofitCommunicationService.getFriendModelList(userInformation.getUserName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public HashMap<String, FriendModel> getFriendModelHashMap() {
        return friendModelHashMap;
    }

    public ArrayList<FriendModel> getFriendModelList() {
        return this.friendModels;
    }

    public FriendModel getFriendModelAt(int position) {
        return friendModels.get(position);
    }

    public void addFriendModel(FriendModel friendModel) {
        if (friendModelHashMap.containsKey(friendModel.getFriendName()) == false) {
            friendModelHashMap.put(friendModel.getFriendName(), friendModel);
            friendModels.add(friendModelHashMap.get(friendModel.getFriendName()));
        }
    }


    // ================================== ChatRoomModel ============================================

    public Observable<List<ChatRoomModel>> setInitialChatRoomModelHashMap() {
        return retrofitCommunicationService.getChatRoomModelList(userInformation.getUserName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void setInitialMessageModelHashMap() {

        if (chatRoomModels.size() > 0) {
            for (ChatRoomModel crm : chatRoomModels) {
                retrofitCommunicationService.getMessageModelList(crm.getSenderChatChannel())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }
    }

    public HashMap<String, ChatRoomModel> getChatRoomModelHashMap() {
        return this.chatRoomModelHashMap;
    }

    public ArrayList<ChatRoomModel> getChatRoomList() {
        return this.chatRoomModels;
    }

    public ChatRoomModel getChatRoomModelAt(int position) {
        return chatRoomModels.get(position);
    }

    public void addChatRoomModelByName(String targetUser) {

        String chatChannel = "/queue/" + targetUser;

        if (chatRoomModelHashMap.containsKey(chatChannel) == false) {
            ChatRoomModel chatRoomModel = new ChatRoomModel();

            chatRoomModel.setSenderChatChannel(chatChannel);
            chatRoomModel.setChatRoomName(targetUser);
            chatRoomModel.getParticipants().add(targetUser);
            chatRoomModel.getParticipants().add(userInformation.getUserName());
            chatRoomModel.setType(0);

            chatRoomModelHashMap.put(chatChannel, chatRoomModel);
            chatRoomModels.add(chatRoomModelHashMap.get(chatChannel));
        }
    }

    public void addChatRoomModelByMessageModel(MessageModel messageModel) {
        String chatChannel = messageModel.getSenderChatChannel();

        if (chatRoomModelHashMap.containsKey(chatChannel) == true) {
            chatRoomModelHashMap.get(chatChannel).getMessageModels().add(messageModel);
        }
        else {
            ChatRoomModel newChatRoomModel = new ChatRoomModel();

            newChatRoomModel.setSenderChatChannel(chatChannel);
            newChatRoomModel.setChatRoomName(messageModel.getSenderName());
            newChatRoomModel.getMessageModels().add(messageModel);
            newChatRoomModel.getParticipants().add(messageModel.getSenderName());
            newChatRoomModel.getParticipants().add(userInformation.getUserName());
            newChatRoomModel.setType(0);

            chatRoomModelHashMap.put(chatChannel, newChatRoomModel);
            chatRoomModels.add(chatRoomModelHashMap.get(chatChannel));

            Log.d("TestTest", "HashMapSize : " + chatRoomModelHashMap.size() + ", ListSize : " + chatRoomModels.size());
        }
    }

    // ================================== MessageModel ==============================================

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
        return db.userDao().insertUserName(userInformation)
                .subscribeOn(Schedulers.io())
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
