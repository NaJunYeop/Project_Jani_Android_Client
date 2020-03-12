package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.database.entity.ParticipantModel;
import com.example.websocketclient.database.entity.RequestModel;
import com.example.websocketclient.database.entity.UserInformationModel;
import com.example.websocketclient.models.ChatModel;
import com.example.websocketclient.models.ModelRepository;
import com.example.websocketclient.models.PlainTextModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddChatRoomViewModel extends AndroidViewModel {

    private Context context;
    private ModelRepository modelRepository;
    private List<ParticipantModel> participantModels;
    private String userName;
    private String chatRoomName;
    private Gson gson = new Gson();

    private ChatModel chatModel;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<Integer> commandEvent;

    public ObservableBoolean buttonVisibility;

    public AddChatRoomViewModel(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(this.context);

        init();
    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public String getTopicChannel() {
        return modelRepository.getTopicChannel();
    }

    public void init() {
        buttonVisibility = new ObservableBoolean(false);
        compositeDisposable = new CompositeDisposable();
        userName = modelRepository.getUserRegisterModel().getRegUserName();
        chatRoomName = "";
        participantModels = new ArrayList<>();

        for (UserInformationModel uim : modelRepository.getUserInformationModels()) {
            uim.getChecked().set(false);
        }
    }

    public LiveData<Integer> getCommandEvent() {
        return commandEvent = new MutableLiveData<>();
    }

    public void createChatRoomButtonClicked() {
        participantModels.add(new ParticipantModel(userName, getTopicChannel(), userName));

        ParticipantModel participantModel;
        RequestModel requestModel;
        String participantFormat = "";
        int pSize = participantModels.size();

        for (int i = 0; i < pSize; i++) {
            participantModel = participantModels.get(i);
            insertClientDBParticipantModel(participantModel);

            participantFormat += (participantModel.getParticipantUserName() + ":");

            if (participantModel.getParticipantUserName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {
                continue;
            }
            chatRoomName += participantModel.getParticipantUserName();
            if (i != pSize - 1) {
                chatRoomName += ", ";
            }
        }

        for (int i = 0; i < pSize; i++) {
            participantModel = participantModels.get(i);

            if (participantModel.getParticipantUserName().equals(modelRepository.getUserRegisterModel().getRegUserName())) {
                continue;
            }

            requestModel = new RequestModel(4, participantFormat, participantModel.getParticipantUserName());
            requestModel.setChatChannel(modelRepository.getTopicChannel());

            sendMessageViaStomp("/req/" + participantModel.getParticipantUserName(), requestModel);
        }

        Log.d("formatChecking", participantFormat);
        Log.d("participantModelLog", participantModels.size() + "");

        insertClientDBChatRoomModel((chatModel = modelRepository.createChatModel(getTopicChannel(), chatRoomName, participantModels)).getChatRoomModel());

        modelRepository.getMainViewModel().createTopicChannel(getTopicChannel());
    }

    public void sendMessageViaStomp(String destination, Object model) {
        compositeDisposable.add(modelRepository
                .stompSendMessage(destination, gson.toJson((RequestModel)model))
                .subscribe(() -> {

                }, throwable -> {
                }));
    }

    public void insertClientDBChatRoomModel(ChatRoomModel chatRoomModel) {
        modelRepository.insertClientDBChatRoomModel(chatRoomModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        modelRepository.setSelectedChatModel(chatModel);
                        // Intent to ChatRoomActivity
                        commandEvent.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertClientDBParticipantModel(ParticipantModel participantModel) {
        modelRepository.insertClientDBParticipantModel(participantModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void listItemClicked(int position) {
        String participantName = modelRepository.getUserInformationModelAt(position).getUserInfoUserName();
        boolean cur = !getCheckBoxStateAt(position).get();
        getCheckBoxStateAt(position).set(cur);

        if (cur) {
            participantModels.add(new ParticipantModel(participantName, getTopicChannel(), userName));
        }
        else {
            for (ParticipantModel pm : participantModels) {
                if (pm.getParticipantUserName().equals(participantName)) {
                    participantModels.remove(pm);
                    break;
                }
            }
        }

        if (participantModels.size() == 0) {
            buttonVisibility.set(false);
        }
        else {
            buttonVisibility.set(true);
        }
    }

    public ObservableBoolean getCheckBoxStateAt(int position) {
        return modelRepository.getUserInformationModelAt(position).getChecked();
    }

}
