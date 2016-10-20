package com.example.yao.pressclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.LogBean;

import java.util.List;

/**
 * Created by Yao on 2016/9/28.
 */
public class UserLoginAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<LogBean> loginBeen;

    public UserLoginAdapter(Context context,List<LogBean> loginBeen){
        this.context = context;
        this.loginBeen = loginBeen;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_userhome,null);
        RecyclerView.ViewHolder holder = new LoginShowHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LoginShowHolder loginShowHolder = (LoginShowHolder) holder;
        loginShowHolder.tv_time.setText(loginBeen.get(position).getTime());
        loginShowHolder.tv_adress.setText(loginBeen.get(position).getAddress());
        loginShowHolder.tv_device.setText(loginBeen.get(position).getDevice()+"");
    }

    @Override
    public int getItemCount() {
        return loginBeen.size();
    }

    public class LoginShowHolder extends RecyclerView.ViewHolder{
        private TextView tv_time,tv_adress,tv_device;
        public LoginShowHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.item_usehome_tvtime);
            tv_adress = (TextView) itemView.findViewById(R.id.item_usehome_tvaddress);
            tv_device = (TextView) itemView.findViewById(R.id.item_usehome_tvdevice);
        }
    }
}
