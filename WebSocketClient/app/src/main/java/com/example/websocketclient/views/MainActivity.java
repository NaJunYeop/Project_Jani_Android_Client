package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.websocketclient.database.entity.UserInformation;
import com.example.websocketclient.databinding.ActivityMainBinding;
import com.example.websocketclient.databinding.ActivityMainBindingImpl;
import com.example.websocketclient.models.MessageModel;
import com.example.websocketclient.models.RequestModel;
import com.example.websocketclient.models.ServerModel;
import com.example.websocketclient.R;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.utils.adapters.ChatRoomAdapter;
import com.example.websocketclient.views.utils.adapters.PageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;

public class MainActivity extends AppCompatActivity {
    public static UserInformation intentUserInformation;

    private static final String TAG = "MainActivityLog";
    private ActivityMainBinding activityMainBinding;
    private MainViewModel mainViewModel;
    private PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "MainActivity onCreate() called()");

        intentUserInformation = (UserInformation) getIntent().getSerializableExtra("userInfo");

        dataBindingInit();
        stompLiveDataInit();

    }

    public void dataBindingInit() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set LifecycleOwner
        activityMainBinding.setMainActivity(this);
        activityMainBinding.setLifecycleOwner(this);

        // Binds MainViewModel.class
        //mainViewModel = new MainViewModel(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), activityMainBinding.mainTabLayout.getTabCount());
        pageAdapter.setMainViewModel(mainViewModel);
        activityMainBinding.mainViewPager.setAdapter(pageAdapter);

        /*activityMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Binds ChatRoomAdapter.class
        chatRoomAdapter = new ChatRoomAdapter(activityMainBinding.getMainViewModel());
        activityMainBinding.setChatRoomAdapter(chatRoomAdapter);
        activityMainBinding.recyclerView.setAdapter(chatRoomAdapter);*/
    }

    public void stompLiveDataInit() {
       //mainViewModel.createChatRoom("/topic/greetings");

        // STOMP Health를 Check함.
        mainViewModel.getStompHealthEvent()
                .observe(this, new Observer<LifecycleEvent.Type>() {
                    @Override
                    public void onChanged(LifecycleEvent.Type type) {
                        switch (type) {
                            // STOMP Over WebSocket이
                            case OPENED: // 성공적으로 Opened
                                Log.i(TAG, "Stomp connection opened");
                                break;
                            case ERROR: // Open 실패
                                Log.e(TAG, "Stomp connection error");
                                break;
                            case CLOSED: // 어떤 이유에서든 Closed 됨
                                break;
                            case FAILED_SERVER_HEARTBEAT: // Heartbeat를 감지할 수 없음
                                break;
                        }
                    }
                });

        // Message Event가 발생하면 Chat Room에서 Message를 띄워줘야한다.
        /*mainViewModel.getMessageEvent()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer position) {
                        activityMainBinding.recyclerView.scrollToPosition(position);
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
            super.onDestroy();
            Log.i(TAG, "MainActivity onDestroy() called ...");
    }
}
