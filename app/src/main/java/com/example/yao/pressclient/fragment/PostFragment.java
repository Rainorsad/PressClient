package com.example.yao.pressclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yao.pressclient.R;
import com.example.yao.pressclient.adapter.PostAdapter;
import com.example.yao.pressclient.bean.PostBean;
import com.example.yao.pressclient.recyitemutil.CustomItemAnimator;
import com.example.yao.pressclient.url.UelBack;
import com.example.yao.pressclient.utils.MyRecycleviewItemStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yao on 2016/9/20.
 */
public class PostFragment extends Fragment{
    private HttpUtils httpUtils;
    private RecyclerView recyclerView;
    private List<PostBean> postBeen;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        //recycleview设置分割线风格
        MyRecycleviewItemStyle m =new MyRecycleviewItemStyle(MyRecycleviewItemStyle.VERTICAL);
        m.setColor(0xFFDBD6D2);
        m.setSize(2);
        recyclerView.addItemDecoration(m);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new CustomItemAnimator());

        postBeen = new ArrayList<>();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    //访问网络请求数据
    private void setData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","1");
        params.addBodyParameter("nid","20");
        params.addBodyParameter("type","1");
        params.addBodyParameter("stamp","20160617");
        params.addBodyParameter("dir","0");
        params.addBodyParameter("cid","111");
        httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.show_tell, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    int status = jsonObject.getInt("status");
                    if (status == 0){
                        JSONArray data = jsonObject.getJSONArray("data");
                        Gson gson = new Gson();
                        List<PostBean> p = gson.fromJson(String.valueOf(data),new TypeToken<List<PostBean>>(){}.getType());
                        Message ms = ha.obtainMessage();
                        ms.obj = p;
                        ms.what = 1;
                        ha.sendMessage(ms);
                    }else {
                        ha.sendEmptyMessage(2);
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


    Handler ha = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 1:
                   List<PostBean> ps = (List<PostBean>) msg.obj;
                   postBeen.clear();
                   postBeen.addAll(ps);
                   PostAdapter adapter = new PostAdapter(postBeen,getActivity());
                   recyclerView.setAdapter(adapter);
                   break;
               case 2:
                   Toast.makeText(getActivity(),"请重新刷新",Toast.LENGTH_SHORT).show();
                   break;
           }
        }
    };
}
