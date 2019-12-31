package com.example.websocketclient.views.utils.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.websocketclient.databinding.FragmentChatRoomListBinding;
import com.example.websocketclient.databinding.FragmentConfigurationBinding;
import com.example.websocketclient.databinding.FragmentFriendListBinding;
import com.example.websocketclient.databinding.FragmentSchedulerBinding;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.utils.fragments.ChatRoomListFragment;
import com.example.websocketclient.views.utils.fragments.ConfigurationFragment;
import com.example.websocketclient.views.utils.fragments.FriendListFragment;
import com.example.websocketclient.views.utils.fragments.SchedulerFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTaps;
    private final int FRIEND = 0;
    private final int CHAT_ROOM = 1;
    private final int SCHEDULER = 2;
    private final int CONFIGURATION = 3;
    private final CharSequence[] viewPagerTitle = {"친구", "채팅", "일정", "설정"};

    private FragmentFriendListBinding fragmentFriendListBinding;
    private FragmentChatRoomListBinding fragmentChatRoomListBinding;
    private FragmentSchedulerBinding schedulerBinding;
    private FragmentConfigurationBinding configurationBinding;

    private MainViewModel mainViewModel;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTaps) {
        super(fm, numOfTaps);
        this.numOfTaps = numOfTaps;
    }

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRIEND : return new FriendListFragment();
            case CHAT_ROOM : return new ChatRoomListFragment();
            case SCHEDULER : return new SchedulerFragment(mainViewModel);
            case CONFIGURATION : return new ConfigurationFragment(mainViewModel);
            default : return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return viewPagerTitle[position];
    }

    @Override
    public int getCount() {
        return numOfTaps;
    }
}
