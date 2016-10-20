package com.example.yao.pressclient.activity;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.adapter.MyFragmentPagerAdapter;
import com.example.yao.pressclient.bean.ModelBean;
import com.example.yao.pressclient.fragment.CollectionFragment;
import com.example.yao.pressclient.fragment.ImageFragent;
import com.example.yao.pressclient.fragment.PostFragment;
import com.example.yao.pressclient.fragment.PressFragment;
import com.example.yao.pressclient.utils.MyApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ViewPager vp;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter fragmentadapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView tv_headlayout;
    private ImageView img_headlayout_left,img_headlayout_right;
    public List<ModelBean> modelBeen;

    private MyApp myApp;

    private List<Fragment> fragments;
    private List<String> list_titles;
    private int[] drawable_lists = new int[]{R.mipmap.biz_navigation_tab_news,R.mipmap.biz_navigation_tab_read,
            R.mipmap.biz_navigation_tab_ties,R.mipmap.biz_navigation_tab_pics};
    private String[] head_lists = new String[]{"新闻","收藏","跟帖","图片"};

    @Override
    protected void setLayout() {
        myApp = (MyApp) getApplication();
        myApp.AddActivity(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setView() {
        vp = (ViewPager) findViewById(R.id.main_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        navigationView = (NavigationView) findViewById(R.id.main_navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_draw);
        tv_headlayout = (TextView) findViewById(R.id.head_layout_text);
        img_headlayout_left = (ImageView) findViewById(R.id.head_layout_image_left);
        img_headlayout_right = (ImageView) findViewById(R.id.head_layout_image_right);

        modelBeen = new ArrayList<>();
    }

    @Override
    protected void setDeal() {
        setHeadView();
        setMainData();
        setNavigation();
    }

    /**
     * 处理头部布局
     */
    private void setHeadView() {
        tv_headlayout.setText("新闻");
        img_headlayout_left.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.ic_title_home_default).into(img_headlayout_right);
        img_headlayout_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myApp.userBean==null){
                    Jump(MainActivity.this,LoginActivity.class);
                }else {
                    Jump(MainActivity.this,UserHomeActivity.class);
                }
            }
        });
    }


    /**
     * navigatuion设置
     */
    private void setNavigation() {

        View headView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_favourite:
                        break;
                    case R.id.drawer_home:
                        if (myApp.userBean==null){
                            Jump(MainActivity.this,LoginActivity.class);
                        }else {
                            Jump(MainActivity.this,UserHomeActivity.class);
                        }
                        break;
                    case R.id.drawer_settings:
                        ToaS(MainActivity.this,"Setting");
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 主要数据展示
     */
    private void setMainData() {
        fragments = new ArrayList<>();
        fragments.add(new PressFragment());
        fragments.add(new CollectionFragment());
        fragments.add(new PostFragment());
        fragments.add(new ImageFragent());

        list_titles = new ArrayList<>();
        list_titles.add("新闻");
        list_titles.add("收藏");
        list_titles.add("跟帖");
        list_titles.add("图片");
        list_titles.add("图片");

        fragmentadapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),this,fragments,list_titles);
        vp.setAdapter(fragmentadapter);
        vp.addOnPageChangeListener(new listener());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vp);
        for (int i=0;i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null){
                tab.setCustomView(fragmentadapter.getView(i));
            }
        }
    }

    /**
     * viewpager滑动件听
     */
    class  listener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tv_headlayout.setText(head_lists[position]);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    long i=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4){
            if (System.currentTimeMillis() - i>2000){
                ToaS(this,"再按一次退出");
                i = System.currentTimeMillis();
            }
            else {
                myApp.DestroyActivity();
//                moveTaskToBack(true);
            }
        }

        return true;
    }
}
