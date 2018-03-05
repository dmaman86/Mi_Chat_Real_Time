package com.example.david_maman_c.mi_chat_real_time;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by davidmaman on 08/08/2017.
 */

public class Preference {

    public static final String STRING_PREFERENCES = "mi_chat_real_time.Sms.Messages";
    public static final String PREFERENCE_STATE_RD = "state.button.connection";
    public static final String PREFERENCE_USER_LOGIN = "user.login";

    public static void SavePreferenceBoolean(Context c, boolean b,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putBoolean(key, b).apply();
    }

    public static void SavePreferenceString(Context c, String b,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putString(key, b).apply();
    }


    public static boolean getPreferenceBoolean(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }

    public static String getPreferenceString(Context c, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

}
