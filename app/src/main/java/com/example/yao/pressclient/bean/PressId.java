package com.example.yao.pressclient.bean;

/**
 * Created by Yao on 2016/10/13.
 */

public class PressId extends ModelBean{
    private int _id;
    private String token;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
