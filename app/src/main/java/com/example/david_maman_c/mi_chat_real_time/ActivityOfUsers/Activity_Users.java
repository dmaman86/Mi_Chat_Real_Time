package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers;

import android.app.Notification;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Checkable;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.ConstractorFriendsList;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.Request;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.UsersSearch_Constractor;
import com.example.david_maman_c.mi_chat_real_time.Login;
import com.example.david_maman_c.mi_chat_real_time.Preference;
import com.example.david_maman_c.mi_chat_real_time.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davidmaman on 09/08/2017.
 */

/* this is a class father of all fragments class */

public class Activity_Users extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    /* bus connect fragments class */
    private EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Chats");
        setContentView(R.layout.activity_users);

        bus = EventBus.getDefault();
        tabLayout = (TabLayout)findViewById(R.id.TabLayoutUsers);
        viewPager = (ViewPager)findViewById(R.id.viewpagerUsers);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new AdapterUsers(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    //setTitle("Friends List");
                }
                else if(position == 1){
                    //setTitle("");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOffscreenPageLimit(3);

        /* כאשר נכנסים לאפליקציה אנו צריכים להביא את המשתמשים הקיימים */
        /* אנו מקבלים מערך ונבדוק מי חבר ומי לא על מנת לשייך אותו לfragment הנכון */

        RequestJSON sj = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    JSONArray jA = j.getJSONArray("result");
                    for(int i = 0; i<jA.length(); i++){
                        JSONObject json = jA.getJSONObject(i);
                        String id = json.getString("id");
                        //this is no show me in search users
                        if(!id.equals(Preference.getPreferenceString(Activity_Users.this,Preference.PREFERENCE_USER_LOGIN))) {
                            /* all user insert to fragment search users */
                            String full_name = json.getString("name") + " " + json.getString("last_name");
                            String status = json.getString("status");
                            /* constructor from "fragment_users" */
                            UsersSearch_Constractor user = new UsersSearch_Constractor();
                            user.setPhoto(R.drawable.ic_account_circle);
                            user.setId(id);
                            user.setFullName(full_name);
                            user.setStatus(1);
                            Request r;

                            /* לפי ״status״ אנו מבדילים מצב חברות */
                            /* 1=> no friends, 2,3 => stand by to acept or cancel request friendly, 4=> friends!!!*/
                            switch (status) {
                                /* status 2,3 => מעדכנים את הבנאי של המחלקות הרלוונטיות*/
                                case "2":
                                    user.setStatus(2);
                                    r = new Request();
                                    r.setId(id);
                                    r.setFullName(full_name);
                                    r.setPhoto(R.drawable.ic_account_circle);
                                    r.setHour(json.getString("date_friends"));
                                    r.setStatus(2);
                                    bus.post(r);
                                    break;
                                case "3":
                                    user.setStatus(3);
                                    r = new Request();
                                    r.setId(id);
                                    r.setFullName(full_name);
                                    r.setPhoto(R.drawable.ic_account_circle);
                                    r.setHour(json.getString("date_friends"));
                                    r.setStatus(3);
                                    bus.post(r);
                                    break;
                                case "4":
                                    /* status 4 => אנו מעדכנים את הבנאי של חברים */
                                    user.setStatus(4);
                                    ConstractorFriendsList a = new ConstractorFriendsList();
                                    a.setId(id);
                                    a.setFullName(full_name);
                                    a.setPhoto(R.drawable.ic_account_circle);
                                    a.setMessage(json.getString("message"));
                                    a.setKind_message(json.getString("kind_message"));
                                    String hour_message = json.getString("hour_sms");
                                    String hour_vector[] = hour_message.split("\\,");
                                    a.setHour(hour_vector[0]);
                                    bus.post(a);
                                    break;
                            }
                                /* כל מחלקה יש בנאי שמציג את המשתמשים בצורה שונה */
                            bus.post(user);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError() {

            }
        };

        String user = Preference.getPreferenceString(this, Preference.PREFERENCE_USER_LOGIN);
        sj.requestJsonGET(this, RequestJSON.URL_GET_ALL_DATA + user);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* this class show menu if want to log out */
        getMenuInflater().inflate(R.menu.activity_menu_friends,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.menu_logout){
            /* אם בחר להתנתק אז צריך לעדכן את המחלקה Preference */
            Preference.SavePreferenceBoolean(Activity_Users.this, false, Preference.PREFERENCE_STATE_RD);
            Intent i = new Intent(Activity_Users.this, Login.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
