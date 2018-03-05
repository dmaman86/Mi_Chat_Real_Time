package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.DeleteFriendFragmentUsers;
import com.example.david_maman_c.mi_chat_real_time.Preference;
import com.example.david_maman_c.mi_chat_real_time.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidmaman on 08/08/2017.
 */

public class Fragment_FriendsList extends Fragment {

    private RecyclerView rv;
    private List<ConstractorFriendsList> constractorLists;
    private FriendsAdapter adapter;

    private LinearLayout layoutEmpty;

    private EventBus bus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_friends, container, false);

        constractorLists = new ArrayList<>();

        rv = (RecyclerView)v.findViewById(R.id.list_friends);
        layoutEmpty = (LinearLayout)v.findViewById(R.id.layoutEmpty);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        adapter = new FriendsAdapter(constractorLists, getContext(), this);
        rv.setAdapter(adapter);

        checkIfHaveFriends();

        return v;
    }

    public void checkIfHaveFriends(){
        if(constractorLists.isEmpty()){
            layoutEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        else{
            layoutEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    private void apdateCard(){
        adapter.notifyDataSetChanged();
        checkIfHaveFriends();
    }


    public void AddFriends(int friend_photo, String name, String last_message, String hour, String id){
        ConstractorFriendsList constractorFriendsList = new ConstractorFriendsList();
        constractorFriendsList.setPhoto(friend_photo);
        constractorFriendsList.setFullName(name);
        constractorFriendsList.setMessage(last_message);
        constractorFriendsList.setHour(hour);
        constractorFriendsList.setId(id);
        constractorLists.add(constractorFriendsList);
        adapter.notifyDataSetChanged();
        checkIfHaveFriends();
    }

    public void AddFriends(ConstractorFriendsList a){
        constractorLists.add(a);
        adapter.notifyDataSetChanged();
        checkIfHaveFriends();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exectCall(ConstractorFriendsList b){
        AddFriends(b);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteFriends(DeleteFragmentFriends b){

        for(int i = 0; i < constractorLists.size(); i++){
            if(constractorLists.get(i).getId().equals(b.getId())){
                constractorLists.remove(i);
                apdateCard();
            }
        }
    }

    public void deleteFriends(final String id){

        String userSender = Preference.getPreferenceString(getContext(),Preference.PREFERENCE_USER_LOGIN);
        RequestJSON s = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    String response = j.getString("result");
                    if(response.equals("200")){
                        bus.post(new DeleteFriendFragmentUsers(id));
                        for(int i = 0; i < constractorLists.size(); i++){
                            if(constractorLists.get(i).getId().equals(id)){
                                constractorLists.remove(i);
                                apdateCard();
                            }
                        }

                    }else if(response.equals("-1")){
                        //request friendly fail
                        Toast.makeText(getContext(), "Error to delete user", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error to delete user", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void requestError() {
                Toast.makeText(getContext(), "sorry but something wrong to send request friends", Toast.LENGTH_SHORT).show();
            }
        };
        HashMap<String, String> hs = new HashMap<>();
        hs.put("sender", userSender);
        hs.put("receptor",id);
        s.requestJsonPOST(getContext(), RequestJSON.URL_DELETE_USER,hs);

    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }
}
