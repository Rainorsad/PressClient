package com.example.yao.pressclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yao on 2016/9/22.
 */
public class MySharepreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public MySharepreference(Context context){
        sharedPreferences = context.getSharedPreferences("useinfo",Context.MODE_PRIVATE);
    }

    /**
     * 判断是否为第一次登陆
     */
    public void setInfo(int i){
        editor = sharedPreferences.edit();
        editor.putInt("number",i);
        editor.apply();
    }

    public int getInfo(){
        int s1 = sharedPreferences.getInt("number",0);
        return s1;
    }

    /**
     * 存储用户名和密码
     */
    public void setUserInfo(String name,String pwd){
        editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("password",pwd);
        editor.apply();
    }

    public String getUserName(){
        return sharedPreferences.getString("name",null);
    }

    public String getPassWord(){
        return sharedPreferences.getString("password",null);
    }

    /**
     *存储token和imei
     */
    public void setPhoneInfo(String imei,String token){
        editor = sharedPreferences.edit();
        editor.putString("imei",imei);
        editor.putString("token",token);
        editor.apply();
    }

    public String getImei(){
        return sharedPreferences.getString("imei",null);
    }
    public String getToken(){
        return sharedPreferences.getString("token",null);
    }
}
