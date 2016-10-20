package com.example.yao.pressclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.ModelBean;

import java.util.List;

/**
 * Created by Yao on 2016/9/21.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private List<ModelBean> modelBeans;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerViewItemOnLongClickListener onRecyclerViewItemOnLongClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (ModelBean) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onRecyclerViewItemOnLongClickListener != null){
            onRecyclerViewItemOnLongClickListener.onItemLongClick(v, (ModelBean) v.getTag());
        }
        return false;
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ModelBean modelBean);
    }
    public static interface OnRecyclerViewItemOnLongClickListener{
        void onItemLongClick(View view,ModelBean modelBean);
    }

    public MyRecycleAdapter(Context context, List<ModelBean> modelBeans) {
        super();
        this.modelBeans = modelBeans;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_news, null);
        holder = new NewsViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        Glide.with(context).load(modelBeans.get(position).getIcon()).thumbnail(0.1f).into(newsViewHolder.img);
        newsViewHolder.tv_title.setText(modelBeans.get(position).getTitle());
        newsViewHolder.tv_sumary.setText(modelBeans.get(position).getSummary().trim());
        newsViewHolder.itemView.setTag(modelBeans.get(position));

    }

    @Override
    public int getItemCount() {
        return modelBeans.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_title, tv_sumary;

        public NewsViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_news_img);
            tv_title = (TextView) itemView.findViewById(R.id.item_news_title);
            tv_sumary = (TextView) itemView.findViewById(R.id.item_news_summary);
        }
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnRecyclerViewItemOnLongClickListener(OnRecyclerViewItemOnLongClickListener listener){
        this.onRecyclerViewItemOnLongClickListener = listener;
    }

}
