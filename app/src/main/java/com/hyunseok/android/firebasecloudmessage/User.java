package com.hyunseok.android.firebasecloudmessage;

/**
 * Created by Administrator on 2017-03-15.
 */

public class User {
    String id;
    String password;
    String token;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
