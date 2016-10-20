package com.example.yao.pressclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yao.pressclient.R;
import com.example.yao.pressclient.activity.MainActivity;
import com.example.yao.pressclient.activity.PressShowActivity;
import com.example.yao.pressclient.adapter.ImageFragAdapter;
import com.example.yao.pressclient.bean.ModelBean;
import com.example.yao.pressclient.recyitemutil.CustomItemAnimator;
import com.example.yao.pressclient.utils.MyRecycleviewItemStyle;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Yao on 2016/9/20.
 */
public class ImageFragent extends Fragment{
    private RecyclerView recyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        MyRecycleviewItemStyle m =new MyRecycleviewItemStyle(MyRecycleviewItemStyle.VERTICAL);
        m.setColor(0xFFDBD6D2);
        m.setSize(2);
        recyclerView.addItemDecoration(m);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new CustomItemAnimator());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) getActivity();
        List<ModelBean> modelBeen = mainActivity.modelBeen;
        ImageFragAdapter adapter = new ImageFragAdapter(getActivity(),modelBeen);
        recyclerView.setAdapter(adapter);

        adapter.setmOnItemClickListener(new ImageFragAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ModelBean modelBean) {
                Gson gson = new Gson();
                String s = gson.toJson(modelBean);
                Intent it = new Intent(getActivity(), PressShowActivity.class);
                it.putExtra("gson",s);
                startActivity(it);
            }
        });


    }
}
