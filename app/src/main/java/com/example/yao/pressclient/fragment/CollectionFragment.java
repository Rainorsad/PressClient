package com.example.yao.pressclient.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yao.pressclient.R;
import com.example.yao.pressclient.activity.PressShowActivity;
import com.example.yao.pressclient.adapter.MyRecycleAdapter;
import com.example.yao.pressclient.bean.ModelBean;
import com.example.yao.pressclient.bean.PressId;
import com.example.yao.pressclient.db.PressLiteDb;
import com.example.yao.pressclient.recyitemutil.CustomItemAnimator;
import com.example.yao.pressclient.utils.MyApp;
import com.example.yao.pressclient.utils.MyRecycleviewItemStyle;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yao on 2016/9/20.
 */
public class CollectionFragment extends Fragment{
    private RecyclerView recyclerView;
    private ImageView imageView;
    private List<ModelBean> modelBeen;
    private PressLiteDb pressLiteDb;
    private MyRecycleAdapter adapter;

    private MyApp myApp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        pressLiteDb = new PressLiteDb(getActivity());
        MyRecycleviewItemStyle m =new MyRecycleviewItemStyle(MyRecycleviewItemStyle.VERTICAL);
        m.setColor(0xFFDBD6D2);
        m.setSize(2);
        recyclerView.addItemDecoration(m);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new CustomItemAnimator());
        modelBeen = new ArrayList<>();
        myApp = (MyApp) getActivity().getApplication();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myApp.userBean==null){
            Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
        }else {
            loadData();
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        List<PressId> pressdata = pressLiteDb.getQueryByWhere(PressId.class,"token = ?",new String[]{myApp.userBean.getToken()});
        for (int i=0;i<pressdata.size();i++){
            ModelBean modelBean = new ModelBean();
            PressId pressId = pressdata.get(i);
            modelBean.setTitle(pressId.getTitle());
            modelBean.setSummary(pressId.getSummary());
            modelBean.setIcon(pressId.getIcon());
            modelBeen.add(modelBean);
        }
        adapter = new MyRecycleAdapter(getActivity(),modelBeen);
        recyclerView.setAdapter(adapter);

        adapter.setmOnItemClickListener(new MyRecycleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ModelBean modelBean) {
                Gson gson = new Gson();
                String s = gson.toJson(modelBean);
                Intent it = new Intent(getActivity(), PressShowActivity.class);
                it.putExtra("gson",s);
                startActivity(it);
            }
        });
        //长按监听删除数据
        adapter.setOnRecyclerViewItemOnLongClickListener(new MyRecycleAdapter.OnRecyclerViewItemOnLongClickListener() {
            @Override
            public void onItemLongClick(View view, final ModelBean modelBean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                builder.setTitle("确定要删除么");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pressLiteDb.deleteWhere(PressId.class,"nid = ?",new String[]{modelBean.getNid()+""});
                        modelBeen.remove(modelBean);
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
