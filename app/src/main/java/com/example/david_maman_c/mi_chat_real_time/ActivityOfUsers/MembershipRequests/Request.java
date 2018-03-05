package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.ClassComunications.User;

/**
 * Created by davidmaman on 09/08/2017.
 */

public class Request extends User{

    public Request() {

    }

    public Request(String id, String full_name, int status, String hour, int photo) {
        super(id, full_name, status, hour, photo);
    }
}
