package com.example.websocketclient.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.example.websocketclient.databinding.ActivityMainBinding;
import com.example.websocketclient.R;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.adapters.PageAdapter;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLog";
    private ActivityMainBinding activityMainBinding;
    private MainViewModel mainViewModel;
    private PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBindingInit();
        stompLiveDataInit();
    }

    public void dataBindingInit() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set LifecycleOwner
        activityMainBinding.setLifecycleOwner(this);

        // MainViewModel.class를 Binding한 Layout에 지정
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), activityMainBinding.mainTabLayout.getTabCount());
        pageAdapter.setMainViewModel(mainViewModel);
        activityMainBinding.mainViewPager.setAdapter(pageAdapter);

        activityMainBinding.mainTabLayout.setupWithViewPager(activityMainBinding.mainViewPager);
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
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("QueueChannelCheck", "MainActivity : onDestroy()");
    }
}
