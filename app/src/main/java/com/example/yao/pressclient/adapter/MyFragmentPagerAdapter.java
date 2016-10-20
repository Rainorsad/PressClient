package com.example.yao.pressclient.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yao.pressclient.R;

import java.util.List;

/**
 * Created by Yao on 2016/9/20.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
    private List<String> list_titles;
    private int[] drawable_lists = new int[]{R.mipmap.biz_navigation_tab_news,R.mipmap.biz_navigation_tab_read,
            R.mipmap.biz_navigation_tab_ties,R.mipmap.biz_navigation_tab_pics};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> listfragments, List<String> list_title) {
        super(fm);
        this.context = context;
        this.fragments = listfragments;
        this.list_titles = list_title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_titles.get(position);
    }

    public View getView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv = (TextView) v.findViewById(R.id.tab_item_text);
        tv.setText(list_titles.get(position));
        ImageView iv = (ImageView) v.findViewById(R.id.tab_item_img);
        iv.setImageResource(drawable_lists[position]);
        return v;
    }
}
