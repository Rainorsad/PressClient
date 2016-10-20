package com.example.yao.pressclient.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.bean.ModelBean;
import com.example.yao.pressclient.url.UelBack;
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
 * Created by Yao on 2016/10/14.
 */
public class ImageFragAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private MySharepreference ms;
    private List<ModelBean> modelBeen;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    ImageFragAdapter adapter;

    private HttpUtils httpUtils;
    public ImageFragAdapter(Context context,List<ModelBean> modelBeen){
        super();
        this.context = context;
        this.modelBeen = modelBeen;
        adapter = this;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_imagefragment,null);
        Holder holder = new Holder(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (ModelBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ModelBean modelBean);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder h = (Holder) holder;
        h.tv.setText(modelBeen.get(position).getTitle());
        Glide.with(context).load(modelBeen.get(position).getIcon()).thumbnail(0.1f).fitCenter().into(h.img);
        h.itemView.setTag(modelBeen.get(position));

        h.imgbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<modelBeen.size();i++){
                    if (modelBeen.get(i).getShow()){
                        modelBeen.get(i).setShow(false);
                    }else if (i == position){
                        modelBeen.get(i).setShow(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        if (modelBeen.get(position).getShow()){
            h.lin.setVisibility(View.VISIBLE);
        }else {
            h.lin.setVisibility(View.GONE);
        }

        h.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.lin.getVisibility()==View.VISIBLE){
                    ms = new MySharepreference(context);
                    if(ms.getToken().length() == 0){
                        Toast.makeText(context,"请登录",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String message = h.edit.getText().toString();
                    if (message.length()==0){
                        Toast.makeText(context,"请输入评论的内容",Toast.LENGTH_SHORT).show();
                    }else {
                        sendMess(message,position);
                        h.lin.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 访问网络发送评论
     * @param message
     * @param position
     */
    private void sendMess(String message, int position) {
        httpUtils = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","0000000");
        params.addBodyParameter("nid",modelBeen.get(position).getNid()+"");
        params.addBodyParameter("token",ms.getToken());
        params.addBodyParameter("imei",ms.getImei());
        params.addBodyParameter("ctx",message);
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.seng_message, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    int status = jsonObject.getInt("status");
                    if (status == 0){
                        JSONObject jsData = jsonObject.getJSONObject("data");
                        String explain = jsData.getString("explain");
                        Message ms = mHandler.obtainMessage();
                        ms.what=1;
                        ms.obj = explain;
                        mHandler.sendMessage(ms);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.d("测试","访问网络失败");
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelBeen.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv;
        private ImageButton imgbut;
        private EditText edit;
        private Button bt;
        private LinearLayout lin;
        public Holder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.item_imagefragment_title);
            img= (ImageView) itemView.findViewById(R.id.item_imagefragment_bigimg);
            imgbut = (ImageButton) itemView.findViewById(R.id.item_imagefragment_pinglun);
            edit = (EditText) itemView.findViewById(R.id.item_imagefrag_editmess);
            bt = (Button) itemView.findViewById(R.id.item_imagefrag_but);
            lin = (LinearLayout) itemView.findViewById(R.id.item_imagefrag_discoss);
            lin.setVisibility(View.GONE);
        }
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String s = (String) msg.obj;
                    Toast.makeText(context,s,Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };
}
