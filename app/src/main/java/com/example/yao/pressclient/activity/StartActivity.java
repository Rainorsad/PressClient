package com.example.yao.pressclient.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.UserBean;
import com.example.yao.pressclient.url.UelBack;
import com.example.yao.pressclient.utils.LoginDialog;
import com.example.yao.pressclient.utils.MyApp;
import com.example.yao.pressclient.utils.MySharepreference;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yao on 2016/9/22.
 */
public class StartActivity extends BaseActivity{
    private ImageView img,img_two;
    private ViewPager vp;
    private View view1,view2,view3,view4;
    private MySharepreference ms;
    private HttpUtils httpUtils;

    private static final int[] mInageId=new int[]{R.mipmap.bd,R.mipmap.small,R.mipmap.welcome,R.mipmap.wy};
    private List<View> views;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_start);
        //隐藏导航栏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void setView() {
        img = (ImageView) findViewById(R.id.start_img);
        vp = (ViewPager) findViewById(R.id.start_viewpager);
        img_two = (ImageView) findViewById(R.id.start_img_two);

        ms = new MySharepreference(StartActivity.this);
    }

    @Override
    protected void setDeal() {
        int i=ms.getInfo();
        if (i==1){
            loadTwo();
        }else {
            loadOne();
        }
    }

    /**
     * 第二次登陆
     */
    private void loadTwo() {
//        LoginDialog.show(StartActivity.this);
        startAnimal();
        vp.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);

        /**
         * 进行share登录判断
         */
        final MySharepreference ms = new MySharepreference(StartActivity.this);
        final String Imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();

        final String name = ms.getUserName();
        final String password = ms.getPassWord();

        if (name.length()==0 || password.length() == 0){
            ms.setInfo(0);
            Intent it = new Intent(StartActivity.this,MainActivity.class);
            startActivity(it);
            finish();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","0000000");
        params.addBodyParameter("uid",name);
        params.addBodyParameter("pwd",password);
        params.addBodyParameter("device","0");
        httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.login, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    int status = json.getInt("status");
                    if (status == 0){
                        JSONObject jsData = json.getJSONObject("data");
                        String token = jsData.getString("token");
                        UserBean userBean = new UserBean();
                        userBean.setName(name);
                        userBean.setPassword(password);
                        userBean.setToken(token);
                        ms.setPhoneInfo(Imei,token);
                        MyApp myApp = (MyApp) StartActivity.this.getApplication();
                        myApp.userBean = userBean;
                        Glide.with(StartActivity.this).load(R.drawable.pikaqiu).thumbnail(0.1f).into(img);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(1);
                            }
                        },2000);
                    }else {
                        handler.sendEmptyMessage(1);
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

    /**
     * 动画启动
     */
    private void startAnimal() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.roat_style);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        animation.setFillAfter(true);
        img_two.setAnimation(animation);
    }

    /**
     * 第一次登陆
     */
    private void loadOne() {
        vp.setVisibility(View.VISIBLE);
        img.setVisibility(View.GONE);

        views = new ArrayList<>();
        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.item_onelayout,null);
        view2 = inflater.inflate(R.layout.item_twolayout,null);
        view3 = inflater.inflate(R.layout.item_threelayout,null);
        view4 = inflater.inflate(R.layout.item_fourlayout,null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);

        vp.setAdapter(new MyPageAdapter());//viewpager设值

        View view = views.get(views.size()-1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(1);
            }
        });//监听最后一张图片，跳转

        ms.setInfo(1);//数据库更新
    }

    /**
     * handler线程
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    LoginDialog.dismiss();
                    Intent it = new Intent(StartActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();
                    break;

            }
        }
    };

    /**
     * 适配器
     */
    class MyPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mInageId.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
