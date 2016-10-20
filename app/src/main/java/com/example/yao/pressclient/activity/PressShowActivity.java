package com.example.yao.pressclient.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.ModelBean;
import com.example.yao.pressclient.bean.PressId;
import com.example.yao.pressclient.utils.MyApp;
import com.example.yao.pressclient.db.PressLiteDb;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Yao on 2016/10/13.
 */
public class PressShowActivity extends BaseActivity{
    private WebView webView;
    private TextView tv_headlayout;
    private ImageView img_headlayout_left,img_headlayout_right;

    private PressLiteDb pressLiteDb;
    private ModelBean modelBean;

    private String token;
    private MyApp myApp;
    @Override
    protected void setLayout() {
        myApp = (MyApp) getApplication();
        myApp.AddActivity(this);
        setContentView(R.layout.activity_press);
    }

    @Override
    protected void setView() {
        tv_headlayout = (TextView) findViewById(R.id.head_layout_text);
        img_headlayout_left = (ImageView) findViewById(R.id.head_layout_image_left);
        img_headlayout_right = (ImageView) findViewById(R.id.head_layout_image_right);
        webView = (WebView) findViewById(R.id.activity_press_webview);

        pressLiteDb = new PressLiteDb(PressShowActivity.this);
    }

    @Override
    protected void setDeal() {
        startProgressDialog("正在加载...",this);
        setHead();

        String s = getIntent().getStringExtra("gson");
        Gson gson = new Gson();
        modelBean = gson.fromJson(s, ModelBean.class);
        webView.loadUrl(modelBean.getLink());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopProgressDialog();
            }
        });
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


    }

    private void setHead() {

        tv_headlayout.setText("新闻");
        Glide.with(this).load(R.drawable.back_ico).into(img_headlayout_left);
        Glide.with(this).load(R.drawable.plus).into(img_headlayout_right);

        img_headlayout_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_headlayout_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp myapp = (MyApp) PressShowActivity.this.getApplication();
                if (myapp.userBean == null) {
                    ToaS(PressShowActivity.this, "请先登录");
                    Intent it = new Intent(PressShowActivity.this, LoginActivity.class);
                    startActivity(it);
                } else {
                    token = myapp.userBean.getToken();
                    PressId pressId = new PressId();
                    pressId.setNid(modelBean.getNid());
                    pressId.setType(modelBean.getType());
                    pressId.setTitle(modelBean.getTitle());
                    pressId.setSummary(modelBean.getSummary());
                    pressId.setIcon(modelBean.getIcon());
                    pressId.setLink(modelBean.getLink());
                    pressId.setStamp(modelBean.getStamp());
                    pressId.setToken(token);

                    List<PressId> pressdata = pressLiteDb.getQueryByWhere(PressId.class, "nid = ? and token = ?", new String[]{modelBean.getNid() + "",token});
                    if (pressdata != null && pressdata.size() > 0) {
                        ToaS(PressShowActivity.this, "已收藏，不能重复收藏");
                    } else {
                        pressLiteDb.Add(pressId);
                        ToaS(PressShowActivity.this, "收藏成功");
                    }
                }
            }
        });
    }
}
