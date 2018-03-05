package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.Fragment_FriendsList;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.Fragment_Request;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.Fragment_Users;

/**
 * Created by davidmaman on 09/08/2017.
 */

public class AdapterUsers extends FragmentPagerAdapter {


    public AdapterUsers(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){
            return new Fragment_FriendsList();
        }
        else if(position == 1){
            return new Fragment_Request();
        }
        else if(position == 2){
            return new Fragment_Users();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Friends List";
        }
        else if(position == 1){
            return "Friend requests";
        }
        else if(position == 2){
            return "Search Friends";
        }

        return null;
    }
}
