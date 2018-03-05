package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.ClassComunications.User;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.ConstractorFriendsList;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.DeleteFragmentFriends;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.RequestFragmentRequestFragment;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.Request;
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
 * Created by davidmaman on 10/08/2017.
 */

public class Fragment_Users extends Fragment {

    private List<UsersSearch_Constractor> list_u;//this list is from adapter and this list is dinamic
    private List<UsersSearch_Constractor> list_aux;//we need same list but not dinamic (static)
    private RecyclerView rv;
    private SearchAdapter adapter;
    private EditText search;

    private LinearLayout layoutEmpty;

    private EventBus bus = EventBus.getDefault();


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search_users, container, false);

        /*volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();*/

        list_u = new ArrayList<>();
        list_aux = new ArrayList<>();

        rv = (RecyclerView)v.findViewById(R.id.rvUsers);
        search = (EditText)v.findViewById(R.id.search_user);
        layoutEmpty = (LinearLayout)v.findViewById(R.id.layoutEmpty);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        adapter = new SearchAdapter(list_u, getContext(), this);
        rv.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searcher("" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkExistPerson();

        return v;
    }

    public void checkExistPerson(){
        if(list_u.isEmpty()){
            layoutEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        else{
            layoutEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    public void InsertUser(int photo, String name, int status, String id){
        UsersSearch_Constractor search_friends = new UsersSearch_Constractor();
        search_friends.setPhoto(photo);
        search_friends.setFullName(name);
        search_friends.setStatus(status);
        search_friends.setId(id);
        list_u.add(search_friends);
        list_aux.add(search_friends);
        adapter.notifyDataSetChanged();
        checkExistPerson();
    }

    public void InsertUser(UsersSearch_Constractor b){
        list_u.add(b);
        list_aux.add(b);
        adapter.notifyDataSetChanged();
        checkExistPerson();
    }

    /*פונקציה לחיפוש וסינון על ידי הקלדה*/
    public void searcher(String txt){
        list_u.clear();
        for(int i = 0; i < list_aux.size(); i++){
            if(list_aux.get(i).getFullName().toLowerCase().contains(txt.toLowerCase())){
                list_u.add(list_aux.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        checkExistPerson();
    }


    @Subscribe
    public void excetCall(UsersSearch_Constractor b){
        InsertUser(b);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelRequest(RequestFragmentFragmentUsers b){
        changeStatus(b.getId(),1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void aceptRequest(AcceptRequestFragmentUsers a){
        changeStatus(a.getId(), 4);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteUser(DeleteFriendFragmentUsers d){
        changeStatus(d.getId(), 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRequest(GetRequestFriendlyFragmentUsers d){
        changeStatus(d.getId(), 3);
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

    public void sendRequest(final String id){

        String userSender = Preference.getPreferenceString(getContext(),Preference.PREFERENCE_USER_LOGIN);

        RequestJSON s = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    String response = j.getString("result");
                    if(response.equals("200")){
                        //request was correctly
                        Toast.makeText(getContext(), "Request Friends is send correctly", Toast.LENGTH_SHORT).show();
                        int status = j.getInt("status");
                        String fullName = j.getString("fullName");
                        String hour = j.getString("hour").split(",")[0];
                        Request r = new Request();
                        r.setId(id);
                        r.setStatus(status);
                        r.setFullName(fullName);
                        r.setHour(hour);
                        r.setPhoto(R.drawable.ic_account_circle);
                        bus.post(r);
                        changeStatus(id,2);

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
        s.requestJsonPOST(getContext(), RequestJSON.URL_SEND_REQUEST,hs);

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
                        changeStatus(id, 1);
                        bus.post(new RequestFragmentRequestFragment(id));
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
                        changeStatus(id, 4);//change status between users
                        bus.post(new RequestFragmentRequestFragment(id));//delete card from "Friend Request"
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

    public void deleteUser(final String id){
        String userSender = Preference.getPreferenceString(getContext(),Preference.PREFERENCE_USER_LOGIN);
        RequestJSON s = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    String response = j.getString("result");
                    if(response.equals("200")){
                        changeStatus(id,1);
                        bus.post(new DeleteFragmentFriends(id));

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

    private void changeStatus(String id, int status) {
        for (int i = 0; i < list_aux.size(); i++) {
            if (list_aux.get(i).getId().equals(id)) {
                list_aux.get(i).setStatus(status);
                break;
            }
        }
        int posUser = -1;
        for (int i = 0; i < list_u.size(); i++) {
            if (list_u.get(i).getId().equals(id)) {
                list_u.get(i).setStatus(status);
                posUser = i;
                break;
            }
        }

        if (posUser != -1) {
            adapter.notifyItemChanged(posUser);
        }
        else{
            Toast.makeText(getContext(), "status not change in list friends", Toast.LENGTH_SHORT).show();
        }
    }

    private User getUserById(String id){
        for(int i = 0; i < list_u.size(); i++){
            if(list_u.get(i).getId().equals(id)){
                return list_u.get(i);
            }
        }
        return null;
    }
}
