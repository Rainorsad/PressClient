package com.example.yao.pressclient.bean;

/**
 * Created by Yao on 2016/10/10.
 */
public class LoginBean {
    private String name;
    private String password;
    public LoginBean(String name ,String password){
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }
}
