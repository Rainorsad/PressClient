package com.example.yao.pressclient.utils;

import android.app.Activity;
import android.app.Application;

import com.example.yao.pressclient.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yao on 2016/9/26.
 */
public class MyApp extends Application{
    public UserBean userBean;
    public List<Activity> activities = null;
    public void AddActivity(Activity a){
        if (activities == null){
            activities = new ArrayList<>();
        }
        activities.add(a);
    }

    public void DestroyActivity(){
        for (int i=0;i<activities.size();i++){
            Activity activity = activities.get(i);
            activity.finish();
        }
    }

}
