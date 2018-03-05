package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.ClassComunications.User;

/**
 * Created by davidmaman on 08/08/2017.
 */

public class ConstractorFriendsList extends User{

    private String kind_message;

    public ConstractorFriendsList() {

    }

    public ConstractorFriendsList(String id, String full_name, String message, String hour, int photo, String kind_message) {
        /* ירושה!! */
        super(id, full_name, message, hour, photo);
        this.kind_message = kind_message;
    }

    public String getKind_message() {
        return kind_message;
    }

    public void setKind_message(String kind_message) {
        this.kind_message = kind_message;
    }
}
