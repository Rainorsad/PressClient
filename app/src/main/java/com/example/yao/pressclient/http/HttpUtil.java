package com.example.yao.pressclient.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.yao.pressclient.url.UelBack;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Yao on 2016/10/19.
 */
public class HttpUtil {
    private HttpUtils httpUtils;
    public HttpUtil(){
        super();
        httpUtils = new HttpUtils();
    }
    static HttpUtil httpUtil = null;
    public static HttpUtil getHttpUtil(){
        if (httpUtil == null){
            return httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    public void setHttpClick(String s, RequestParams params, final Class c, final Handler handler){
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + s, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    int status = jsonObject.getInt("status");
                    if (status == 0){
                        JSONObject data = jsonObject.getJSONObject("data");
                        Object o = gson.fromJson(String.valueOf(data), c);
                        Message ms = handler.obtainMessage();
                        ms.obj = o;
                        ms.what = 1;
                        handler.sendMessage(ms);
                    }else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
    public void setHttpClickArray(String s, RequestParams params, final Type t, final Handler handler){

        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + s, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    int status = jsonObject.getInt("status");
                    if (status == 0){
                        JSONArray data = jsonObject.getJSONArray("data");
                        Object o = gson.fromJson(String.valueOf(data), t);
                        Message ms = handler.obtainMessage();
                        ms.obj = o;
                        ms.what = 1;
                        handler.sendMessage(ms);
                    }else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
