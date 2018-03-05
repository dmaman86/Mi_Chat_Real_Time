package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.ClassComunications;

/**
 * Created by davidmaman on 10/08/2017.
 */

public class User {

    private String id;
    private String full_name;
    private int status;
    private String message;
    private String hour;
    private int photo;

    public User(){}

    public User(String id, String full_name, int status, String hour, int photo) {
        this.id = id;
        this.full_name = full_name;
        this.status = status;
        this.hour = hour;
        this.photo = photo;
    }

    public User(String id, String full_name, String message, String hour, int photo) {
        this.id = id;
        this.full_name = full_name;
        this.message = message;
        this.hour = hour;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String name) {
        this.full_name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
