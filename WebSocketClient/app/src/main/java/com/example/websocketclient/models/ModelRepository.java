package com.example.websocketclient.models;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.example.websocketclient.database.AppDatabase;
import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.database.entity.RegisterModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.retrofit.utils.RetrofitClient;
import com.example.websocketclient.retrofit.utils.RetrofitCommunicationService;
import com.example.websocketclient.viewmodels.MainViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function5;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kotlin.jvm.functions.Function0;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ModelRepository {
    public final String TAG = "ModelRepositoryLog";
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";
    private String topicChannel;

    private Context context;
    private AppDatabase db;
    private RetrofitCommunicationService retrofitCommunicationService;
    private RegisterModel userRegisterModel;
    private StompClient mStompClient;

    private List<UserInformationModel> userInformationModels;
    private List<RequestModel> requestModels;
    private List<ChatRoomModel> chatRoomModels;
    private List<MessageModel> messageModels;
    private List<ParticipantModel> participantModels;
    private List<ParticipantModel> tempParticipantModels;
    private List<ChatModel> chatModels;
    private List<ChatModel> tempChatModels;

    private PublishSubject<ChatModel> chatModelListEvent;

    private UserInformationModel selectedUserInformationModel;
    private ChatModel selectedChatModel;

    private MainViewModel mainViewModel;



    // Singleton Pattern
    private static class LazyHolder {
        private static final ModelRepository instance = new ModelRepository();
    }

    public static ModelRepository getInstance() {
        return LazyHolder.instance;
    }

    public ModelRepository() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ServerModel.SERVER_IP + ":" + ServerModel.SERVER_PORT + "/janiwss/websocket");
        chatModelListEvent = PublishSubject.create();
    }

    public void setUserRegisterModel(RegisterModel registerModel) {
        this.userRegisterModel = registerModel;
    }

    public Observable<ChatModel> getChatModelListEvent() {
        return chatModelListEvent;
    }

    public RegisterModel getUserRegisterModel() {
        return this.userRegisterModel;
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

    public UserInformationModel getSelectedUserInformationModel() {
        return selectedUserInformationModel;
    }

    public void setSelectedUserInformationModel(UserInformationModel selectedUserInformationModel) {
        this.selectedUserInformationModel = selectedUserInformationModel;
    }

    public ChatModel getSelectedChatModel() {
        return selectedChatModel;
    }

    public void setSelectedChatModel(ChatModel selectedChatModel) {
        this.selectedChatModel = selectedChatModel;
    }

    public List<ParticipantModel> getTempParticipantModels() {
        return tempParticipantModels;
    }

    public List<ChatModel> getTempChatModels() {
        return tempChatModels;
    }

    // ================================== RequestModel =============================================

    public Maybe<List<RequestModel>> loadRequestModels() {
        return db.requestModelDao().getRequestModels()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<RequestModel> getRequestModel(String senderName) {
        return db.requestModelDao().getRequestModel(senderName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public RequestModel getRequestModelAt(int position) {
        return requestModels.get(position);
    }

    public void addRequestModel(RequestModel requestModel) {
        this.requestModels.add(requestModel);
    }

    public void deleteRequestModel(String senderName) {
        for (int i = 0; i < this.requestModels.size(); i++) {
            if (this.requestModels.get(i).getReqSenderName().equals(senderName)) {
                requestModels.remove(i);
                return;
            }
        }
    }

    public void deleteRequestModel(int position) {
        requestModels.remove(position);
    }

    public Completable deleteRequestModelFromClientDB(String senderName) {
        return db.requestModelDao().deleteRequestModel(senderName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<RequestModel> getRequestModels() {
        return requestModels;
    }

    public void setRequestModels(List<RequestModel> requestModels) {
        this.requestModels = requestModels;
    }

    public List<UserInformationModel> getUserInformationModels() {
        return userInformationModels;
    }

    public void setUserInformationModels(List<UserInformationModel> userInformationModels) {
        this.userInformationModels = userInformationModels;
    }

    public List<ChatRoomModel> getChatRoomModels() {
        return this.chatRoomModels;
    }

    public void setChatRoomModels(List<ChatRoomModel> chatRoomModels) {
        this.chatRoomModels = chatRoomModels;
    }

    public List<MessageModel> getMessageModels() {
        return this.messageModels;
    }

    public void setMessageModels(List<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    public List<ParticipantModel> getParticipantModels() {
        return this.participantModels;
    }

    public void setParticipantModels(List<ParticipantModel> participantModels) {
        this.participantModels = participantModels;
    }

    public UserInformationModel getUserInformationModelAt(int position) {
        return userInformationModels.get(position);
    }

    public void addUserInformationModel(UserInformationModel userInformationModel) {
        this.userInformationModels.add(userInformationModel);
    }

    public void deleteUserInformationModel(String userName) {
        for (int i = 0; i < this.userInformationModels.size(); i++) {
            if (this.userInformationModels.get(i).getUserInfoUserName().equals(userName)) {
                this.userInformationModels.remove(i);
                return;
            }
        }
    }

    public void addMessageModelToChatModels(MessageModel messageModel) {
        //String chatChannel = "/queue/" + messageModel.getMsgSenderName();
        String chatChannel = messageModel.getChatChannel();
        Log.d("QueueChannelCheck", "ChatChannel : " + chatChannel);

        for (ChatModel cm : this.chatModels) {
            if (cm.getChatRoomModel().getChatChannel().equals(chatChannel)) {
                cm.getMessageModels().add(messageModel);
            }
        }
    }

    public void addToChatModels(ChatRoomModel chatRoomModel) {
        String chatChannel = chatRoomModel.getChatChannel();

        List<MessageModel> mMessageModels = new ArrayList<>();
        List<ParticipantModel> mParticipantModels = new ArrayList<>();

        for (MessageModel mm : this.messageModels) {
            if (mm.getChatChannel().equals(chatChannel)) {
                mMessageModels.add(mm);
            }
        }

        for (ParticipantModel pm : this.participantModels) {
            if (pm.getChatChannel().equals(chatChannel)) {
                mParticipantModels.add(pm);
            }
        }

        chatModels.add(new ChatModel(chatRoomModel, mMessageModels, mParticipantModels));
    }

    public ChatModel getChatModel(String queueChannel) {
        for (ChatModel cm : this.chatModels) {
            if (cm.getChatRoomModel().getChatChannel().equals(queueChannel)) {
                return cm;
            }
        }

        return null;
    }

    public ChatModel createChatModel(String chatChannel, String chatRoomName, List<ParticipantModel> participants) {
        String userName = getUserRegisterModel().getRegUserName();
        ChatModel chatModel = null;

        ChatRoomModel mChatRoomModel = new ChatRoomModel(0, userName, chatChannel, chatRoomName);
        chatModel = new ChatModel(mChatRoomModel, new ArrayList<MessageModel>(), participants);
        chatModels.add(chatModel);

        chatModelListEvent.onNext(chatModel);

        return chatModel;
    }

    public List<ChatModel> getChatModels() {
        return chatModels;
    }

    public ChatModel getChatModelAt(int position) {
        return chatModels.get(position);
    }

    public Maybe<List<UserInformationModel>> loadUserInformationModels() {
        return db.userDao().getUserInformationModels()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<List<ChatRoomModel>> loadChatRoomModels() {
        return db.chatModelDao().getChatRoomModels()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<List<MessageModel>> loadMessageModels()  {
        return db.chatModelDao().getMessageModels()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<List<ParticipantModel>> loadParticipantModels() {
        return db.chatModelDao().getParticipantModels()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<String> loadData() {
        userInformationModels = new ArrayList<>();
        requestModels = new ArrayList<>();
        chatRoomModels = new ArrayList<>();
        messageModels = new ArrayList<>();
        participantModels = new ArrayList<>();
        chatModels = new ArrayList<>();
        tempParticipantModels = new ArrayList<>();
        tempChatModels = new ArrayList<>();

        return Maybe.zip(
                loadUserInformationModels(),
                loadRequestModels(),
                loadChatRoomModels(),
                loadMessageModels(),
                loadParticipantModels(),
                new Function5<List<UserInformationModel>, List<RequestModel>, List<ChatRoomModel>, List<MessageModel>, List<ParticipantModel>, String>() {
                    @Override
                    public String apply(List<UserInformationModel> userInformationModels, List<RequestModel> requestModels, List<ChatRoomModel> chatRoomModels, List<MessageModel> messageModels, List<ParticipantModel> participantModels) throws Exception {
                        setUserInformationModels(userInformationModels);
                        setRequestModels(requestModels);
                        setChatRoomModels(chatRoomModels);
                        setMessageModels(messageModels);
                        setParticipantModels(participantModels);

                        for (ChatRoomModel crm : getChatRoomModels()) {
                            addToChatModels(crm);
                        }

                        Log.d("loadDataCheck", "chatModelSize = " + getChatModels().size());

                        return "FIN_LOAD";
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    /*public Single<String> insertClientDBChatModel(ChatRoomModel chatRoomModel, MessageModel messageModel, ParticipantModel participantModel) {
        return Single.zip(
                insertClientDBChatRoomModel(chatRoomModel),
                insertClientDBMessageModel(messageModel),
                insertClientDBParticipantModel(participantModel),
                new Function3<Integer, Integer, Integer, String>() {
                    @Override
                    public String apply(Integer integer, Integer integer2, Integer integer3) throws Exception {
                        return "CHAT_MODEL_FIN";
                    }
                }
        ).observeOn(AndroidSchedulers.mainThread());
    }*/

    // ============================= Room Databse ==================================================
    public Maybe<RegisterModel> getClientDBRegisterModel() {
        return db.registerModelDao().getUserRegisterModels()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertClientDBRegisterModel(RegisterModel registerModel) {
        return db.registerModelDao().insertRegisterModel(registerModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PlainTextModel> insertServerDBRegisterModel(RegisterModel userRegisterModel) {
        return retrofitCommunicationService.registerUserRegisterModelToServer(userRegisterModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertClientDBUserInformationModel(UserInformationModel userInformationModel) {
        return db.userDao().insertUserInformationModel(userInformationModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertClientDBRequestModel(RequestModel requestModel) {
        return db.requestModelDao().insertRequestModel(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertClientDBChatRoomModel(ChatRoomModel chatRoomModel) {
        return db.chatModelDao().insertChatRoomModel(chatRoomModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertClientDBMessageModel(MessageModel messageModel) {
        return db.chatModelDao().insertMessageModel(messageModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertClientDBParticipantModel(ParticipantModel participantModel) {
        return db.chatModelDao().insertParticipantModel(participantModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // =============================== Retrofit ====================================================
    public Single<PlainTextModel> retrofitCheckDuplicateUserName(String userName) {
        return retrofitCommunicationService.userNameDuplicationCheck(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PlainTextModel> retrofitFindUserInformationModel(String userName) {
        return retrofitCommunicationService.findUserInformationModel(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserInformationModel> retrofitGetUserInformationModel(String userName) {
        return retrofitCommunicationService.getUserInformationModel(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PlainTextModel> retrofitGetTopicChannel() {
        return retrofitCommunicationService.getTopicChannel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public String getTopicChannel() {
        return topicChannel;
    }

    public void setTopicChannel(String topicChannel) {
        this.topicChannel = topicChannel;
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
