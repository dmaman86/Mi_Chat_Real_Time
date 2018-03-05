package com.example.david_maman_c.mi_chat_real_time;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Activity_Users;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet.RequestJSON;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    /* הגדרת משתנים */
    private EditText eTuser;
    private EditText eTpassword;
    private Button bTlogin;
    private Button btRegistration;
    private RadioButton RBconnected;
    /* משתנים קשורים לrequest */
    private RequestQueue mRequest;
    private VolleyRP volley;

    /* כתובת URL שבה אנו משרשרים את ID */
    private static final String IP = "https://indexproje.000webhostapp.com/phpFiles/Login_GETID.php?Id=";

    /* משתנים עזר לטובת בדיקה */
    private String USER = "";
    private String PASSWORD = "";

    private boolean isActivateRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* אם המשתמש כבר היה מחובר והפעיל ״תשאיר אותי מחבר״ אין צורך להתחברות מחודשת */
        if(Preference.getPreferenceBoolean(this, Preference.PREFERENCE_STATE_RD)){
            JumperActivity();
        }

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        /* קישור משתנים לXML */
        eTuser = (EditText)findViewById(R.id.eTuser);
        eTpassword = (EditText)findViewById(R.id.eTpasword);
        bTlogin = (Button)findViewById(R.id.bTlogin);
        btRegistration = (Button)findViewById(R.id.register);
        RBconnected = (RadioButton)findViewById(R.id.RBconnected);

        /* default radio button is false, variable boolean is false too */
        isActivateRadioButton = RBconnected.isChecked();//is off

        /*if radiobutton is on and we wont to turn off*/
        /* if radio button change to true so boolean variable (isActivateRadioButton) need to change to true */
        RBconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isActivateRadioButton){
                    RBconnected.setChecked(false);
                }
                isActivateRadioButton = RBconnected.isChecked();
            }
        });

        //אחרי הזנת הפרטיים שולחים לפונקציה שבודקת את הפרטיים (checkLogin)
        bTlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin(eTuser.getText().toString().toLowerCase(),
                        eTpassword.getText().toString().toLowerCase());
            }
        });
        btRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });
    }

    //לכאן מקבל שם משתמש וסיסמא
    public void CheckLogin(String user, String password){
        /* משתני עזר מקבלים את הפרטים שהוזנו */
        USER = user;
        PASSWORD = password;
        RequestJSON(IP + user);//RequestJSON מקבל את כתובת ip + שם משתמש
    }

    public void RequestJSON(String URL){
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* response => response from php file */
                TestLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"sorry but happened an error, please contact with a admin",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(request,mRequest,this,volley);
    }

    public void TestLogin(JSONObject data){
      //monitor json
        try {
            String result = data.getString("Answer");
            if(result.equals("AC")){ //AC => Answer Correct
                JSONObject jsondata = new JSONObject(data.getString("data"));
                String user = jsondata.getString("Id").toLowerCase();
                String password = jsondata.getString("Password").toLowerCase();
                if(user.equals(USER) && password.equals(PASSWORD)){
                    String token = FirebaseInstanceId.getInstance().getToken();
                    if(token != null) {
                        //JSONObject js = new JSONObject(token);
                        //String token_st = js.getString("token");
                        UploadToken(token);
                    }
                    else {
                        Toast.makeText(this, "a token is null", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(this,"a password is incorrect",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {}
    }

    private void UploadToken(String token){

        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("id",USER);
        hashMapToken.put("token",token);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RequestJSON.IP_TOKEN_UPLOAD,new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* class preference save a value of variable boolean and user name */
                Preference.SavePreferenceBoolean(Login.this, RBconnected.isChecked(), Preference.PREFERENCE_STATE_RD);
                Preference.SavePreferenceString(Login.this, USER, Preference.PREFERENCE_USER_LOGIN);
                try {
                    Toast.makeText(Login.this,response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {}
                JumperActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"can't update token to database",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(request,mRequest,this,volley);
    }

    public void JumperActivity(){
        /* activity_users is a father of all fragments class */
        Intent i = new Intent(Login.this,Activity_Users.class);
        startActivity(i);
        finish();
    }

}
