package com.example.david_maman_c.mi_chat_real_time.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.ConstractorFriendsList;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends.DeleteFragmentFriends;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.Request;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests.RequestFragmentRequestFragment;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.AcceptRequestFragmentUsers;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.DeleteFriendFragmentUsers;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.GetRequestFriendlyFragmentUsers;
import com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users.RequestFragmentFragmentUsers;
import com.example.david_maman_c.mi_chat_real_time.Preference;
import com.example.david_maman_c.mi_chat_real_time.R;
import com.example.david_maman_c.mi_chat_real_time.Sms.Messages;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Created by david-maman-c on 3.8.2017.
 */

public class FirebaseServiceMessages extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type = remoteMessage.getData().get("type");
        String head = remoteMessage.getData().get("head");
        String body = remoteMessage.getData().get("body");

        switch (type){
            case "message":
                String message = remoteMessage.getData().get("message");
                String hour = remoteMessage.getData().get("hour");
                String receptor = remoteMessage.getData().get("receptor");
                String senderPHP = remoteMessage.getData().get("sender");
                String sender = Preference.getPreferenceString(this, Preference.PREFERENCE_USER_LOGIN);

                if(sender.equals(receptor)) {
                    Message(message, hour, senderPHP);
                    showNotification(head, body);
                }
                break;
            case "request":
                String sub_type_request = remoteMessage.getData().get("sub_type");
                String userSendrequest;
                switch (sub_type_request){
                    case "send_request":
                        EventBus.getDefault().post(new Request(remoteMessage.getData().get("user_send"),
                                remoteMessage.getData().get("user_send_request_name"),
                                3,
                                remoteMessage.getData().get("hour"),
                                R.drawable.ic_account_circle));

                        EventBus.getDefault().post(new GetRequestFriendlyFragmentUsers(remoteMessage.getData().get("user_send")));
                        userSendrequest = remoteMessage.getData().get("user_send");
                        showNotification(head, body);
                        break;
                    case "cancel_request":
                        EventBus.getDefault().post(new RequestFragmentFragmentUsers(remoteMessage.getData().get("user_send")));
                        EventBus.getDefault().post(new RequestFragmentRequestFragment(remoteMessage.getData().get("user_send")));
                        break;

                    case "accept_request":
                        EventBus.getDefault().post(new ConstractorFriendsList(
                                remoteMessage.getData().get("user_send"),
                                remoteMessage.getData().get("user_send_request_name"),
                                remoteMessage.getData().get("lastMessage"),
                                remoteMessage.getData().get("hour_sms").split(",")[0],
                                R.drawable.ic_account_circle,
                                remoteMessage.getData().get("kind_message")));
                        EventBus.getDefault().post(new RequestFragmentRequestFragment(remoteMessage.getData().get("user_send")));
                        EventBus.getDefault().post(new AcceptRequestFragmentUsers(remoteMessage.getData().get("user_send")));

                        userSendrequest = remoteMessage.getData().get("user_send");
                        showNotification(head, body);
                        break;

                    case "delete_request":
                        EventBus.getDefault().post(new DeleteFragmentFriends(remoteMessage.getData().get("user_send")));
                        EventBus.getDefault().post(new DeleteFriendFragmentUsers(remoteMessage.getData().get("user_send")));

                        break;
                }

                break;
        }


    }

    private void Message(String message,String hour,String sender){
        Intent i = new Intent(Messages.MESSAGE);
        i.putExtra("key_message", message);
        i.putExtra("key_hour", hour);
        i.putExtra("key_senderPHP", sender);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void showNotification(String head, String body){
        Intent i = new Intent(this,Messages.class);//this is for touch a notification go to the app
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

        Uri soundNoti = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder builder = new Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(head);
        builder.setContentText(body);
        builder.setSound(soundNoti);
        builder.setSmallIcon(R.drawable.ic_notifications_active);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();//this is for recept multi notification

        notificationManager.notify(random.nextInt(),builder.build());

    }

}
