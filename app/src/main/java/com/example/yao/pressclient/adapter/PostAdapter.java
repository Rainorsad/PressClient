package com.example.yao.pressclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.PostBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yao on 2016/10/18.
 */
public class PostAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<PostBean> postBeen;

    public PostAdapter(List<PostBean> postBeen,Context contex){
        super();
        this.postBeen = postBeen;
        this.context = contex;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,null);
        RecyclerView.ViewHolder holder = new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        Glide.with(context).load(postBeen.get(position).getPortrait()).into(viewholder.cir);
        viewholder.tv_title.setText(postBeen.get(position).getUid());
        viewholder.tv_message.setText(postBeen.get(position).getStamp());
    }

    @Override
    public int getItemCount() {
        return postBeen.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        private CircleImageView cir;
        private TextView tv_title;
        private TextView tv_message;
        public Viewholder(View itemView) {
            super(itemView);
            cir = (CircleImageView) itemView.findViewById(R.id.item_post_img);
            tv_title = (TextView) itemView.findViewById(R.id.item_post_title);
            tv_message = (TextView) itemView.findViewById(R.id.item_post_summary);
        }
    }
}
