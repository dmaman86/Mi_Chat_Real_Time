package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.ConstractorFriendsList;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.AcceptRequestFragmentUsers;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.RequestFragmentFragmentUsers;
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
 * Created by davidmaman on 09/08/2017.
 */

public class Fragment_Request extends Fragment {

    private RecyclerView rv;
    private RequestAdapter adapter;
    private List<Request> Listrequests;
    private LinearLayout layoutNoRequest;

    private EventBus bus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_membership_requests,container,false);

        Listrequests = new ArrayList<>();

        rv = (RecyclerView)v.findViewById(R.id.cardView_request);
        layoutNoRequest = (LinearLayout)v.findViewById(R.id.layoutEmpty);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        adapter = new RequestAdapter(Listrequests, getContext(), this);
        rv.setAdapter(adapter);

        checkRequest();

        return v;
    }

    public void checkRequest(){
        if(Listrequests.isEmpty()){
            layoutNoRequest.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        else{
            layoutNoRequest.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    public void AddTargetRequest(int photo,String id, String name, String hour, int status){
        Request request = new Request();
        request.setPhoto(photo);
        request.setFullName(name);
        request.setHour(hour);
        request.setId(id);
        request.setStatus(status);
        Listrequests.add(0, request);
        UpdateTarget();
    }

    public void AddTargetRequest(Request rq){
        Listrequests.add(0, rq);
        UpdateTarget();
    }

    public void UpdateTarget(){
        adapter.notifyDataSetChanged();
        checkRequest();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exectCall(Request b){
        AddTargetRequest(b);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelRequest(RequestFragmentRequestFragment d){
        deleteCard(d.getId());
    }

    public void cancelRequest(final String id){

        String userSender = Preference.getPreferenceString(getContext(),Preference.PREFERENCE_USER_LOGIN);

        RequestJSON rj = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    String response = j.getString("response");
                    if(response.equals("200")){
                        //cancel correctly
                        deleteCard(id);
                        bus.post(new RequestFragmentFragmentUsers(id));
                        Toast.makeText(getContext(),j.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                    else if(response.equals("-1")){
                        //error in cancel
                        Toast.makeText(getContext(),j.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Can't cancel request", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void requestError() {

            }
        };
        HashMap<String, String > h = new HashMap<>();
        h.put("sender", userSender);
        h.put("receptor", id);
        rj.requestJsonPOST(getContext(),RequestJSON.URL_CANCEL_REQUEST, h);
    }

    public void aceptRequest(final String id){
        String userSender = Preference.getPreferenceString(getContext(),Preference.PREFERENCE_USER_LOGIN);

        RequestJSON s = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    String response = j.getString("result");
                    if(response.equals("200")){
                        //request was correctly
                        bus.post(new AcceptRequestFragmentUsers(id));
                        deleteCard(id);
                        ConstractorFriendsList cf = new ConstractorFriendsList();//create new card to fragment "Friend List"
                        cf.setId(id);
                        cf.setFullName(j.getString("fullName"));
                        cf.setPhoto(R.drawable.ic_account_circle);
                        cf.setMessage(j.getString("LastMessage"));
                        cf.setHour(j.getString("hour").split(",")[0]);
                        cf.setKind_message(j.getString("kind_message"));
                        bus.post(cf);

                    }else if(response.equals("-1")){
                        //request friendly fail
                        Toast.makeText(getContext(), "Error send request friendly", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error send request friendly", Toast.LENGTH_SHORT).show();
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
        s.requestJsonPOST(getContext(), RequestJSON.URL_ACCEPT_REQUEST,hs);
    }

    public void deleteCard(String id){
        for(int i = 0; i < Listrequests.size(); i++){
            if(Listrequests.get(i).getId().equals(id)){
                Listrequests.remove(i);
                UpdateTarget();
            }
        }
    }

}
