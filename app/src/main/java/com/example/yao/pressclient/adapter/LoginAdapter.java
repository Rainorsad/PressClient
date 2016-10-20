package com.example.yao.pressclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.UserInfoBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yao on 2016/10/10.
 */
public class LoginAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private Context context;
    private List<UserInfoBean> userInfoBeen;
    private OnRecyclerViewItemClickListener mOnItemClickListener=null;

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,UserInfoBean userInfoBeen);
    }
    public LoginAdapter(Context context,List<UserInfoBean> userInfoBeen){
        super();
        this.context = context;
        this.userInfoBeen = userInfoBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_login,null);
        RecyclerView.ViewHolder holder = new LoginViewHolder(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LoginViewHolder loginViewHolder = (LoginViewHolder) holder;
        loginViewHolder.textView.setText(userInfoBeen.get(position).getName());
        Glide.with(context).load(userInfoBeen.get(position).getIcon()).thumbnail(0.1f).into(loginViewHolder.cie);
        loginViewHolder.itemView.setTag(userInfoBeen.get(position));
    }

    @Override
    public int getItemCount() {
        return userInfoBeen.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (UserInfoBean) v.getTag());
        }
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public class LoginViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CircleImageView cie;
        public LoginViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_login_text);
            cie = (CircleImageView) itemView.findViewById(R.id.item_login_cir);
        }
    }


}
