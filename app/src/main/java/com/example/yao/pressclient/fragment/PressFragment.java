package com.example.yao.pressclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.activity.MainActivity;
import com.example.yao.pressclient.activity.PressShowActivity;
import com.example.yao.pressclient.adapter.MyRecycleAdapter;
import com.example.yao.pressclient.bean.ModelBean;
import com.example.yao.pressclient.bean.NewsBean;
import com.example.yao.pressclient.http.HttpUtil;
import com.example.yao.pressclient.recyitemutil.CustomItemAnimator;
import com.example.yao.pressclient.url.UelBack;
import com.example.yao.pressclient.utils.LoginDialog;
import com.example.yao.pressclient.utils.MyRecycleviewItemStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Yao on 2016/9/20.
 */
public class PressFragment extends Fragment {
    private HttpUtils httpUtils;
    private RecyclerView recyclerView;
    public List<ModelBean> modelBeen;
    private ImageView img;
    private int index=0;//0是从左往右，1是从右往左;
//    private RollPagerView roll; //轮播组件
    private ViewPager vp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginDialog.show(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        img = (ImageView) view.findViewById(R.id.recycleview_img);
//        roll = (RollPagerView) view.findViewById(R.id.roll);
        vp = (ViewPager) view.findViewById(R.id.recycleview_view);
        vp.setVisibility(View.VISIBLE);


        //设置轮播控件的属性
//        roll.setPlayDelay(3000);
//        roll.setAnimationDurtion(500);//设置透明度
//        roll.setVisibility(View.VISIBLE);

        //recycleview设置分割线风格
        MyRecycleviewItemStyle m = new MyRecycleviewItemStyle(MyRecycleviewItemStyle.VERTICAL);
        m.setColor(0xFFDBD6D2);
        m.setSize(2);
        recyclerView.addItemDecoration(m);


        modelBeen = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }).start();
    }

    /**
     * 下载数据
     */
    private void loadData() {
        //获取当前日期
        String out = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        Log.d("时间测试", out);
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver", "0000000");
        params.addBodyParameter("subid", "1");
        params.addBodyParameter("dir", "1");
        params.addBodyParameter("nid", "1");
        params.addBodyParameter("stamp", out);
        params.addBodyParameter("cnt", "20");
        Type type = new TypeToken<List<NewsBean>>() {
        }.getType();
        HttpUtil.getHttpUtil().setHttpClickArray(UelBack.news, params, type, handler);
    }
    /**
     * handler接收
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                LoginDialog.dismiss();
                Object obj = msg.obj;
                List<NewsBean> newsBeen = (List<NewsBean>) obj;
                setData(newsBeen);

                vp.setAdapter(new MyViewPage());
                handler.sendEmptyMessageDelayed(5,2000);
//                roll.setAdapter(new MyAdapter());
//                roll.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW, Color.WHITE));
            } else if (msg.what == 2) {
                LoginDialog.dismiss();
            }else if (msg.what == 5){
                int i = vp.getCurrentItem();
                if (index==0){
                    if (i<5){
                        i++;
                        if (i==5){
                            index=1;
                        }
                    }
                }else {
                    if (i>0){
                        i--;
                        if (i==0){
                            index=0;
                        }
                    }
                }
                vp.setCurrentItem(i);
                handler.sendEmptyMessageDelayed(5,2000);
            }
        }
    };

    /**
     * 设置数据
     *
     * @param nesBeen
     */
    private void setData(List<NewsBean> nesBeen) {
        for (NewsBean newsBean : nesBeen) {
            ModelBean modelBean = new ModelBean();
            modelBean.setTitle(newsBean.getTitle());
            modelBean.setTitle(newsBean.getTitle());
            modelBean.setNid(newsBean.getNid());
            modelBean.setStamp(newsBean.getStamp());
            modelBean.setIcon(newsBean.getIcon());
            modelBean.setLink(newsBean.getLink());
            modelBean.setSummary(newsBean.getSummary());
            modelBeen.add(modelBean);
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.modelBeen = modelBeen;

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new CustomItemAnimator());
//        recyclerView.setItemAnimator(new ReboundItemAnimator());
        MyRecycleAdapter adapter = new MyRecycleAdapter(getActivity(), modelBeen);
        recyclerView.setAdapter(adapter);

        adapter.setmOnItemClickListener(new MyRecycleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ModelBean modelBean) {
                Gson gson = new Gson();
                String s = gson.toJson(modelBean);
                Intent it = new Intent(getActivity(), PressShowActivity.class);
                it.putExtra("gson", s);
                startActivity(it);
            }
        });
    }

    //轮播控件的适配器
    class MyAdapter extends StaticPagerAdapter {
        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView img = new ImageView(container.getContext());
//            img.setImageResource(imags[position]);
            Glide.with(getActivity()).load(modelBeen.get(position).getIcon()).into(img);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
                }
            });
            return img;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    class MyViewPage extends PagerAdapter{
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = new ImageView(getActivity());
            Glide.with(getActivity()).load(modelBeen.get(position).getIcon()).into(img);
            container.addView(img);

            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);//删除handler中所有消息和回调
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(5,2000);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            handler.sendEmptyMessageDelayed(5,2000);
                            break;
                    }
                    return true;
                }
            });

            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    }
}
