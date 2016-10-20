package com.example.yao.pressclient.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Yao on 2016/9/20.
 */
public abstract class BaseActivity extends AppCompatActivity{
    private ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        setView();
        setDeal();
    }
    //绑定界面
    protected abstract void setLayout();
    //初始化控件'
    protected abstract void setView();
    //处理逻辑事件
    protected abstract void setDeal();
    //弹出框短时间内容显示
    public void ToaS(Context context, String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
    //弹出框长时间显示
    public void ToaL(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    //跳转界面
    public void Jump(Context context,Class c){
        Intent intent = new Intent(context,c);
        startActivity(intent);
    }

    //滚动条显示
    public void startProgressDialog(String msg,Context context) {
        if (pd == null) {
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage(msg);
        }
        pd.show();
    }
    //停止滚动条
    public void stopProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }
}
