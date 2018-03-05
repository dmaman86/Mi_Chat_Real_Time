package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Internet;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.Request;
import com.example.david_maman_c.mi_chat_real_time.Preference;
import com.example.david_maman_c.mi_chat_real_time.R;
import com.example.david_maman_c.mi_chat_real_time.VolleyRP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by davidmaman on 10/08/2017.
 */

public abstract class RequestJSON {

    public final static String URL_GET_ALL_DATA = "https://indexproje.000webhostapp.com/phpFiles/Data_GETALL.php?Id=";

    public final static String URL_GET_ALL_MESSAGES_USER = "https://indexproje.000webhostapp.com/phpFiles/Messages_GETID.php";

    public final static String URL_SEND_REQUEST = "https://indexproje.000webhostapp.com/phpFiles/SEND_Request.php";

    public final static String URL_CANCEL_REQUEST = "https://indexproje.000webhostapp.com/phpFiles/CANCEL_Request.php";

    public static final String IP_TOKEN_UPLOAD = "https://indexproje.000webhostapp.com/phpFiles/Token_INSERTandUPDATE.php";

    public static final String URL_ACCEPT_REQUEST = "https://indexproje.000webhostapp.com/phpFiles/ACCEPT_Request.php";

    public static final String URL_DELETE_USER = "https://indexproje.000webhostapp.com/phpFiles/DELETE_Request.php";

    public abstract void requestCompleted(JSONObject j);

    public abstract void requestError();

    public RequestJSON(){}

    public void requestJsonGET(Context c, String URL){
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestError();
            }
        });
        VolleyRP.addToQueue(request,VolleyRP.getInstance(c).getRequestQueue(),c,VolleyRP.getInstance(c));

    }

    public void requestJsonPOST(Context c, String URL, HashMap h){
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST,URL,new JSONObject(h), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestError();
            }
        });
        VolleyRP.addToQueue(request,VolleyRP.getInstance(c).getRequestQueue(),c,VolleyRP.getInstance(c));

    }


}
