package com.example.yao.pressclient.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.adapter.LoginAdapter;
import com.example.yao.pressclient.bean.UserBean;
import com.example.yao.pressclient.bean.UserInfoBean;
import com.example.yao.pressclient.db.UserDb;
import com.example.yao.pressclient.recyitemutil.CustomItemAnimator;
import com.example.yao.pressclient.url.UelBack;
import com.example.yao.pressclient.utils.MyApp;
import com.example.yao.pressclient.utils.MyRecycleviewItemStyle;
import com.example.yao.pressclient.utils.MySharepreference;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Yao on 2016/9/26.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextInputLayout textInputLayoutname,textInputLayoutpass;
    private EditText editName,editPass;
    private Button bt_login,bt_regist,bt_find;
    private TextView tv_headlayout;
    private ImageView img_headlayout_left,img_headlayout_right;
    private ImageView bttest;
    private LinearLayout lin_login,lin_regist,lin_regist_check,lin_findpwd;
    private ImageSwitcher imgSwitch;

    private TextInputLayout textInputLayoutemail,textInputLayoutRegistname,textInputLayoutRegistpass;
    private EditText editTextemail,editRegistname,editRegistpass;
    private Checkable checkable;

    private TextInputLayout FindPwdtextInputLayout;
    private EditText editFindPwdText;

    private int index=0;//0是登录界面，1是注册显示，2是密码找回显示

    private HttpUtils httpUtils;
    private int imgIndex=1;//imageswitch下标表示，1为向右箭头，2为向下箭头

    private PopupWindow popupWindow;

    private LoginAdapter loginAdapter;

    private MySharepreference ms;

    private MyApp myApp;
    @Override
    protected void setLayout() {
        myApp = (MyApp) getApplication();
        myApp.AddActivity(this);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setView() {
        lin_login = (LinearLayout) findViewById(R.id.login);
        lin_regist = (LinearLayout) findViewById(R.id.regist);
        lin_regist_check = (LinearLayout) findViewById(R.id.regist_check);
        lin_findpwd = (LinearLayout) findViewById(R.id.findpwd);

        //登录模块组件
        textInputLayoutname = (TextInputLayout) findViewById(R.id.activity_login_textinputname);
        textInputLayoutpass = (TextInputLayout) findViewById(R.id.textinput_login_pass);
        editName = (EditText) findViewById(R.id.activity_login_name);
        editPass = (EditText) findViewById(R.id.activity_login_pass);
        bt_login = (Button) findViewById(R.id.activity_login_butlog);
        bt_regist = (Button) findViewById(R.id.activity_login_butregis);
        bt_find = (Button) findViewById(R.id.activity_login_butfind);
        tv_headlayout = (TextView) findViewById(R.id.head_layout_text);
        img_headlayout_left = (ImageView) findViewById(R.id.head_layout_image_left);
        img_headlayout_right = (ImageView) findViewById(R.id.head_layout_image_right);
        bttest = (ImageView) findViewById(R.id.activity_log_check);
        imgSwitch = (ImageSwitcher) findViewById(R.id.activity_login_imgswitch);

        //注册模块组件
        textInputLayoutemail = (TextInputLayout) findViewById(R.id.activity_regist_textinputemail);
        textInputLayoutRegistname = (TextInputLayout) findViewById(R.id.activity_regist_textinput_name);
        textInputLayoutRegistpass = (TextInputLayout) findViewById(R.id.activity_regist_textinput_pass);
        editTextemail = (EditText) findViewById(R.id.activity_regist_email);
        editRegistname = (EditText) findViewById(R.id.activity_regist_name);
        editRegistpass = (EditText) findViewById(R.id.activity_regist_pass);
        checkable = (Checkable) findViewById(R.id.activity_regist_check);

        //找回密码模块
        FindPwdtextInputLayout = (TextInputLayout) findViewById(R.id.activity_fpw_textinputemail);
        editFindPwdText = (EditText) findViewById(R.id.activity_fpw_email);

        //shareperference启动
        ms = new MySharepreference(LoginActivity.this);

        bt_login.setOnClickListener(this);
        bt_find.setOnClickListener(LoginActivity.this);
        bt_regist.setOnClickListener(this);
    }

    @Override
    protected void setDeal() {
        setHeadData();
        bttest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });//密码明文暗文转换

        //处理imageswitch动画
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        imgSwitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView img = new ImageView(LoginActivity.this);
                return img;
            }
        });
        imgSwitch.setInAnimation(in);
        imgSwitch.setOutAnimation(out);
        imgSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgIndex == 1){
                    imgIndex = 2;
                    imgSwitch.setBackgroundResource(R.drawable.arrow_up);
                    showPopuWindow();
                }else {
                    imgIndex = 1;
                    imgSwitch.setBackgroundResource(R.drawable.arrow_down_b);
                    popupWindow.dismiss();
                }
            }
        });

        //recycleview监听

    }

    /**
     * 密码明文暗文转换显示
     */
    private void checkPassword() {
        //129是表示密文  131073明文
        if (editPass.getInputType() == 129){
            editPass.setInputType(131073);
            bttest.setBackgroundResource(R.drawable.eyetrue);
        }else {
            editPass.setInputType(129);
            bttest.setBackgroundResource(R.drawable.eyefalse);
        }
    }

    /**
     * 处理头部数据
     */
    private void setHeadData() {
        tv_headlayout.setText("登录界面");
        img_headlayout_right.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.back_ico).into(img_headlayout_left);
        img_headlayout_left.setOnClickListener(this);
    }

    /**
     * 监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_butregis:
                registModel();
                break;
            case R.id.activity_login_butlog:
                String login = editName.getText().toString();
                String password = editPass.getText().toString();
                if (login.length()<=0 || password.length()<=0){
                    ToaL(LoginActivity.this,"账号和密码不能为空");
                }else {
                    login(login,password);
                }
                break;
            case R.id.head_layout_image_left:
                Finish();
                break;
            case R.id.activity_login_butfind:
                if (index == 1){
                    Boolean fx = checkable.isChecked();
                    if (!fx){
                        ToaS(LoginActivity.this,"请阅读协议");
                        return;
                    }
                    String email = editTextemail.getText().toString();
                    String name = editRegistname.getText().toString();
                    String pass = editRegistpass.getText().toString();
                    if (email.length()<=0 || name.length()>24 || name.length()<=5 || pass.length()<=0){
                        ToaL(LoginActivity.this,"邮箱、昵称、密码不能为空且长度必须在6~24位之间");
                    }else {
                        registDeal(email,name,pass);
                    }
                }else if (index == 0){
                    FindPassword();
                }else if (index == 2){
                    String email = editFindPwdText.getText().toString();
                    if (email.length()<=0){
                        ToaL(LoginActivity.this,"请输入正确的邮箱地址");
                    }else {
                        FindPwd(email);
                    }
                }
                break;
        }
    }

    /**
     * 注册模块
     */
    private void registModel() {
        index=1;
        int w = lin_login.getWidth();
        ObjectAnimator animator = ObjectAnimator.ofFloat(lin_login,"translationX",0,-w);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lin_login.setVisibility(View.GONE);
                lin_regist.setVisibility(View.VISIBLE);
                lin_regist.animate().setDuration(300).scaleX(1).scaleY(1);
                lin_regist_check.setVisibility(View.VISIBLE);
                bt_regist.setVisibility(View.GONE);
                bt_login.setVisibility(View.GONE);
                bt_find.setText("确定");
                tv_headlayout.setText("注册界面");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }

    /**
     * 注册模块逻辑事件处理
     * @param email
     * @param name
     * @param password
     */
    private void registDeal(String email, String name, String password) {
        startProgressDialog("正在注册",this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","0000000");
        params.addBodyParameter("uid",name);
        params.addBodyParameter("email",email);
        params.addBodyParameter("pwd",password);
        httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.regist, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    int status = jsonObject.getInt("status");
                    if (status==0){
                        JSONObject data = jsonObject.getJSONObject("data");
                        int result = data.getInt("result");
                        if (result == 0){
                            handler.sendEmptyMessage(6);
                        }else if (result == -1){
                            handler.sendEmptyMessage(7);
                        }else if (result == -2){
                            handler.sendEmptyMessage(8);
                        }else if (result == -3){
                            handler.sendEmptyMessage(9);
                        }
                    }else {
                        handler.sendEmptyMessage(5);
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
     * 找回密码模块
     */
    private void FindPassword() {
        index=2;
        ObjectAnimator animator = ObjectAnimator.ofFloat(lin_login,"translationX",0,-lin_login.getWidth());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lin_login.setVisibility(View.GONE);
                lin_findpwd.setVisibility(View.VISIBLE);
                lin_findpwd.animate().scaleY(1).scaleX(1).setDuration(300);
                bt_regist.setVisibility(View.INVISIBLE);
                bt_login.setVisibility(View.INVISIBLE);
                tv_headlayout.setText("找回密码");
                bt_find.setText("确定");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    /**
     * 找回密码逻辑事件处理
     * @param email
     */
    private void FindPwd(String email) {
        startProgressDialog("正在发送",this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","0000000");
        params.addBodyParameter("email",email);
        httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.finfpass, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONObject jsData = new JSONObject(jsonObject.getString("data"));
                    String explain = jsData.getString("explain");
                    int result = jsData.getInt("result");
                    Message ms = handler.obtainMessage();
                    ms.obj = explain;
                    if (result == 0){
                        ms.what = 11;
                        handler.sendMessage(ms);
                    }else {
                        ms.what = 10;
                        handler.sendMessage(ms);
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
     * 登录模块逻辑事件处理
     * @param login
     * @param password
     */
    private void login(final String login, final String password) {
        startProgressDialog("正在登陆",this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","0000000");
        params.addBodyParameter("uid",login);
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
                        MySharepreference ms = new MySharepreference(LoginActivity.this);
                        ms.setUserInfo(login,password);
                        JSONObject jsData = json.getJSONObject("data");
                        String token = jsData.getString("token");
                        UserBean userBean = new UserBean();
                        userBean.setName(login);
                        userBean.setPassword(password);
                        userBean.setToken(token);
                        MyApp myApp = (MyApp) LoginActivity.this.getApplication();
                        myApp.userBean = userBean;
                        handler.sendEmptyMessage(1);
                    }else if (status == -1){
                        handler.sendEmptyMessage(2);
                    }else if (status == -2){
                        handler.sendEmptyMessage(3);
                    }else if (status == -3){
                        handler.sendEmptyMessage(4);
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
     * handler接收
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    stopProgressDialog();

                    ms.setInfo(1);
                    Jump(LoginActivity.this,UserHomeActivity.class);
                    Finish();
                    break;
                case 2:
                    stopProgressDialog();
                    ToaL(LoginActivity.this,"用户名或密码错误");
                    break;
                case 3:
                    ToaL(LoginActivity.this,"限制登录（禁言，IP查封）");
                    break;
                case 4:
                    ToaL(LoginActivity.this,"限制登录（异地登录异常）");
                    break;
                case 5:
                    ToaL(LoginActivity.this,"服务器出错");
                    break;
                case 6:
                    ToaL(LoginActivity.this,"注册成功");
                    finish();
                    break;
                case 7:
                    ToaL(LoginActivity.this,"用户已满，不允许注册");
                    break;
                case 8:
                    ToaL(LoginActivity.this,"用户名重复");
                    break;
                case 9:
                    ToaL(LoginActivity.this,"邮箱重复");
                    break;
                case 10:
                    stopProgressDialog();
                    String s = (String) msg.obj;
                    ToaL(LoginActivity.this,s);
                    break;
                case 11:
                    stopProgressDialog();
                    String re = (String) msg.obj;
                    ToaL(LoginActivity.this,re);
                    Finish();
                    break;
            }
        }
    };

    /**
     * 重写结束方法
     */
    private void Finish() {
        if (index==0){
            finish();
        }else if (index == 1){
            index=0;
            lin_regist.animate().setDuration(300).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int w = lin_login.getWidth();
                    lin_regist.setVisibility(View.GONE);
                    lin_regist_check.setVisibility(View.GONE);
                    lin_login.setVisibility(View.VISIBLE);
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(lin_login,"translationX",-w,0);
                    animator1.setDuration(300);
                    animator1.cancel();
                    animator1.start();

                    bt_regist.setVisibility(View.VISIBLE);
                    bt_login.setVisibility(View.VISIBLE);
                    bt_find.setText("忘记密码");
                    tv_headlayout.setText("登录界面");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animation.cancel();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }else if (index == 2){
            index = 0;
            lin_findpwd.animate().scaleX(0).scaleY(0).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    lin_findpwd.setVisibility(View.GONE);
                    lin_login.setVisibility(View.VISIBLE);
                    lin_login.animate().translationXBy(-lin_login.getWidth()).translationX(0).setDuration(300);
                    bt_login.setVisibility(View.VISIBLE);
                    bt_regist.setVisibility(View.VISIBLE);
                    bt_find.setText("找回密码");
                    tv_headlayout.setText("登录界面");
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }
    }
    /**
     * popuwindow展示
     */
    private void showPopuWindow(){
        MyRecycleviewItemStyle ms = new MyRecycleviewItemStyle(MyRecycleviewItemStyle.VERTICAL);
        ms.setColor(0xFFFF0000);
        ms.setSize(2);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new CustomItemAnimator());
        recyclerView.addItemDecoration(ms);

        UserDb udb = new UserDb(LoginActivity.this);
        List<UserInfoBean> loginBeen = udb.select(UserInfoBean.class);


        loginAdapter = new LoginAdapter(this,loginBeen);
        recyclerView.setAdapter(loginAdapter);

        popupWindow = new PopupWindow(recyclerView,editName.getWidth(),500);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_spinner_listview_background));
        popupWindow.showAsDropDown(editName,5,-5);

        loginAdapter.setmOnItemClickListener(new LoginAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, UserInfoBean userInfoBean) {
                editName.setText(userInfoBean.getName());
                editPass.setText(userInfoBean.getPassword());
                imgIndex = 1;
                imgSwitch.setBackgroundResource(R.drawable.arrow_down_b);
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imgIndex = 1;
                imgSwitch.setBackgroundResource(R.drawable.arrow_down_b);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }
}
