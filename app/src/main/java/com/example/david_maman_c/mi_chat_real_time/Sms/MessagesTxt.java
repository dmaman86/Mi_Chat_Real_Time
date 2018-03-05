package com.example.david_maman_c.mi_chat_real_time.Sms;

/**
 * Created by david-maman-c on 2.8.2017.
 */

public class MessagesTxt {

    private String id;
    private String message;
    private int kindMessage;//this is if is sender or reserver
    private String HrMessage;

    public MessagesTxt() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getKindMessage() {
        return kindMessage;
    }

    public void setKindMessage(int kindMessage) {
        this.kindMessage = kindMessage;
    }

    public String getHrMessage() {
        return HrMessage;
    }

    public void setHrMessage(String hrMessage) {
        HrMessage = hrMessage;
    }
}
