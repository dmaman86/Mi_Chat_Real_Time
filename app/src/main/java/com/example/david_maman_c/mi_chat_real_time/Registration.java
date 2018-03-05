package com.example.david_maman_c.mi_chat_real_time;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONObject;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


/**
 * Created by davidmaman on 07/08/2017.
 */

public class Registration extends AppCompatActivity {

    /* url address to send all values of user (hashmap) in method post */
    private static final String IP_REGISTRATION = "https://indexproje.000webhostapp.com/phpFiles/Registration_INSERT.php";
    /* הגדרת משתנים */
    private EditText user;
    private EditText password;
    private EditText name;
    private EditText last_name;
    private EditText day;
    private EditText month;
    private EditText year;
    private EditText email;
    private EditText phone;
    private Button registration;

    /* variables to request */
    private VolleyRP volley;
    private RequestQueue mRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        /* שיוך משתנים לXML */
        user = (EditText)findViewById(R.id.new_user);
        password = (EditText)findViewById(R.id.pass_newUser);
        name = (EditText)findViewById(R.id.name_newUser);
        last_name = (EditText)findViewById(R.id.lastName_newUser);
        day = (EditText)findViewById(R.id.day_birth);
        month = (EditText)findViewById(R.id.month_birth);
        year = (EditText)findViewById(R.id.year_birth);
        email = (EditText)findViewById(R.id.em_newUser);
        phone = (EditText)findViewById(R.id.user_phone);

        registration = (Button)findViewById(R.id.save_register);

        registration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /* trim() --> Removing spaces from string */

                 RegisterNewUser(getStringET(user).trim(),
                        getStringET(password).trim(),
                        getStringET(name).trim(),
                        getStringET(last_name).trim(),
                        getStringET(day)+"/"+getStringET(month)+"/"+getStringET(year),
                        getStringET(email).trim(),
                        getStringET(phone));

            }
        });

    }

    private void RegisterNewUser(final String user_name, String password, String name, String last_name, String birthDate, String email, String phone ){

        /* בודקים האם המשתמש הכניס את כל הפרמטרים */
        if(!user_name.isEmpty() && !password.isEmpty() && !name.isEmpty() && !last_name.isEmpty() && !birthDate.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
            /* hashmap have all values of user */
            HashMap<String, String> hashMapToken = new HashMap<>();
            hashMapToken.put("id", user_name);
            hashMapToken.put("name", name);
            hashMapToken.put("last_name", last_name);
            hashMapToken.put("date_birth", birthDate);
            hashMapToken.put("email", email);
            hashMapToken.put("phone", phone);
            hashMapToken.put("Password", password);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IP_REGISTRATION, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    /* response is a response from url address (php) */
                    try {
                        String status = response.getString("result");
                        if (status.equalsIgnoreCase("Registration successful")) {
                            /* אם תשובה היא חיובית אנו שומרים את token של המשתמש החדש */
                            String token = FirebaseInstanceId.getInstance().getToken();
                            if(token != null) {
                                //JSONObject js = new JSONObject(token);
                                //String token_st = js.getString("token");
                                UploadToken(user_name, token);
                            }
                            else {
                                Toast.makeText(Registration.this, "a token is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Registration.this, status, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(Registration.this, "sorry, registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Registration.this, "sorry, registration failed", Toast.LENGTH_SHORT).show();
                }
            });
            VolleyRP.addToQueue(request, mRequest, this, volley);
        }
        else{
            Toast.makeText(this,"Please fill in all the details",Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadToken(String id, String token){

        RequestJSON j = new RequestJSON() {
            @Override
            public void requestCompleted(JSONObject j) {
                Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void requestError() {
                Toast.makeText(Registration.this, "sorry, registration failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("id",id);
        hashMapToken.put("token",token);
        /* כאן אנו משתמשים במחלקה RequestJSON */
        j.requestJsonPOST(this, RequestJSON.IP_TOKEN_UPLOAD, hashMapToken);
    }

    private String getStringET(EditText e){
        return e.getText().toString();
    }
    

}
