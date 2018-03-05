package com.example.david_maman_c.mi_chat_real_time.Sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.example.david_maman_c.mi_chat_real_time.Login;
import com.example.david_maman_c.mi_chat_real_time.Preference;
import com.example.david_maman_c.mi_chat_real_time.R;
import com.example.david_maman_c.mi_chat_real_time.Registration;
import com.example.david_maman_c.mi_chat_real_time.VolleyRP;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by david-maman-c on 31.7.2017.
 */

public class Messages extends AppCompatActivity {

    public static final String MESSAGE = "MESSAGE";
    private BroadcastReceiver bR;

    private RecyclerView rv;//this is a bubbles messages
    private Button sendMessage;
    private EditText txt_message;
    private List<MessagesTxt> messagestxt;
    private MessagesAdapter adapter;

    private String MESSAGE_SEND = "";
    private String SENDER = "";
    private String RECEPTOR;

    private static final String IP_MESSAGE = "https://indexproje.000webhostapp.com/phpFiles/Send_Messages.php";

    private VolleyRP volley;
    private RequestQueue mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        messagestxt = new ArrayList<>();

        SENDER = Preference.getPreferenceString(this, Preference.PREFERENCE_USER_LOGIN);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        if(bundle != null){
            RECEPTOR = bundle.getString("key_receptor");
        }

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        sendMessage = (Button)findViewById(R.id.bTsend);
        txt_message = (EditText)findViewById(R.id.txt_message);

        rv = (RecyclerView)findViewById(R.id.rvMessages);
        LinearLayoutManager lm = new LinearLayoutManager(this);

        rv.setLayoutManager(lm);//linearLayoutManager vertical bubbles messages

        adapter = new MessagesAdapter(messagestxt,this);
        rv.setAdapter(adapter);

        sendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String message = txt_message.getText().toString().trim();//this is for don't send message empty
                if(!message.isEmpty() && !RECEPTOR.isEmpty()) {
                    MESSAGE_SEND = message;

                    senderMessage();
                    CreateMessage(message,"00:00",1);//1 == sender
                    txt_message.setText("");
                }
            }
        });

        txt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()){
                            setScroolbarChat();
                        }
                        else{
                            rv.postDelayed(this,100);
                        }

                    }
                }, 100);

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setScroolbarChat();

        bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("key_message");
                String hour = intent.getStringExtra("key_hour");
                String hour_parameters[] = hour.split(",");
                String sender = intent.getStringExtra("key_senderPHP");

                if(sender.equals(RECEPTOR)) {
                    CreateMessage(message, hour_parameters[0], 2);//2 == receptor
                }

            }
        };

        RequestJSON s = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                try {
                    JSONArray js = j.getJSONArray("result");
                    for(int i = 0; i < js.length(); i++){
                        JSONObject jo = js.getJSONObject(i);
                        String message = jo.getString("message");
                        String hour = jo.getString("hour_sms").split(",")[0];
                        int kind_message = jo.getInt("kind_message");
                        CreateMessage(message,hour,kind_message);
                    }
                } catch (JSONException e) {
                    Toast.makeText(Messages.this, "happened error, sorry", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestError() {

            }
        };
        HashMap<String, String> hs = new HashMap<>();
        hs.put("sender", SENDER);
        hs.put("receptor", RECEPTOR);
        s.requestJsonPOST(this, RequestJSON.URL_GET_ALL_MESSAGES_USER, hs);
    }

    private void senderMessage(){
        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("sender",SENDER);
        hashMapToken.put("receptor",RECEPTOR);
        hashMapToken.put("message",MESSAGE_SEND);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,IP_MESSAGE,new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(Messages.this,response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Messages.this,"sorry but something wrong happened",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(request,mRequest,this,volley);
    }

    public void CreateMessage(String message,String hour, int kindMessage){
        MessagesTxt smsHelper = new MessagesTxt();
        smsHelper.setId("0");
        smsHelper.setMessage(message);
        smsHelper.setKindMessage(kindMessage);
        smsHelper.setHrMessage(hour);
        messagestxt.add(smsHelper);
        adapter.notifyDataSetChanged();

        setScroolbarChat();
    }

    /*when we app enter in onPause we need to pause too a broadcast(message)*/
    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bR);
    }

    /*this for after onPause we need to back a broadcast(message)*/
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bR,new IntentFilter(MESSAGE));
    }

    /*פונקציה כדי להציג מההודעה אחרונה*/
    public void setScroolbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }
}
