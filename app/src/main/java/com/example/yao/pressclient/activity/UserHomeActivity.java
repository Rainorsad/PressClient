package com.example.yao.pressclient.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yao.pressclient.R;
import com.example.yao.pressclient.adapter.UserLoginAdapter;
import com.example.yao.pressclient.bean.LogBean;
import com.example.yao.pressclient.bean.TestBean;
import com.example.yao.pressclient.bean.UserInfoBean;
import com.example.yao.pressclient.db.UserDb;
import com.example.yao.pressclient.http.HttpUtil;
import com.example.yao.pressclient.recyitemutil.CustomItemAnimator;
import com.example.yao.pressclient.url.UelBack;
import com.example.yao.pressclient.utils.MyApp;
import com.example.yao.pressclient.utils.MySharepreference;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Yao on 2016/9/28.
 */
public class UserHomeActivity extends BaseActivity{
    private Toolbar toolbar;
    private RecyclerView mrecycleview;
    private ImageView imageView_coll;
    private CircleImageView circleImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tv_usename,tv_jifen,tv_tienumbers;

    private HttpUtils httpUtils;
    private String uid;
    private String portrait;
    private int comnum;
    private int integration;

    private LinearLayoutManager linearLayoutManager;

    private Dialog dialog;

    private MyApp myApp;

    private UserDb udb;
    private String Imei;
    private String token;

    private Bitmap tempBitmap;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public File tempFile = new File(Environment.getExternalStorageDirectory()+"/DCIM", getName());

    private MySharepreference ms;
    // 使用系统当前日期加以调整作为照片的名称
    public String getName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    @Override
    protected void setLayout() {
        myApp = (MyApp) this.getApplication();
        myApp.AddActivity(this);
        setContentView(R.layout.activity_userhome);
        //获得运行权限。6.0版本之后必须获得运行权限，而相机属于运行权限之一，不加的话opera()方法会一直调用出错
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            //init(barcodeScannerView, getIntent(), null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
        }
    }

    @Override
    protected void setView() {
        toolbar = (Toolbar) findViewById(R.id.activity_userhome_toolbar);
        mrecycleview = (RecyclerView) findViewById(R.id.recycleview);
        imageView_coll = (ImageView) findViewById(R.id.activity_userHome_scoll_img);
        circleImageView = (CircleImageView) findViewById(R.id.activity_userhome_tool_circler);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_userhome_coll);
        tv_usename = (TextView) findViewById(R.id.userhome_usename);
        tv_jifen = (TextView) findViewById(R.id.userhome_usefenshu);
        tv_tienumbers = (TextView) findViewById(R.id.userhome_tienumbers);

        linearLayoutManager = new LinearLayoutManager(this);

        ms = new MySharepreference(UserHomeActivity.this);

        udb = new UserDb(UserHomeActivity.this);

    }

    @Override
    protected void setDeal() {
        getData();
        setCollasToolBar();
    }

    /**
     * collapsingToolbarLayout展示
     */
    private void setCollasToolBar() {
        Glide.with(UserHomeActivity.this).load(R.drawable.headbackground).thumbnail(0.1f).into(imageView_coll);
        Glide.with(UserHomeActivity.this).load(R.drawable.headbackground).thumbnail(0.1f).into(circleImageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar左上角监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //toolbar右上角菜单选项监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_settings:
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserHomeActivity.this);
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                            }
                        });
                        builder.setTitle("确定要退出么");

                        builder.setNegativeButton("不退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyApp app = (MyApp) UserHomeActivity.this.getApplication();
                                app.userBean = null;
                                ms.setInfo(0);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                }
                return true;
            }
        });
        //头像监听
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(UserHomeActivity.this).inflate(R.layout.item_login, null);
                LinearLayout li = (LinearLayout) view.findViewById(R.id.item_login_linlog);
                li.setVisibility(View.GONE);
                PhotoView img = (PhotoView) view.findViewById(R.id.item_login_img);
                LinearLayout lin = (LinearLayout) view.findViewById(R.id.item_login_lin);
                lin.setVisibility(View.VISIBLE);
                final Dialog dialog = new Dialog(UserHomeActivity.this,R.style.LogDialog);
                img.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        dialog.dismiss();
                    }
                });

                Window window = dialog.getWindow();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);
                Glide.with(UserHomeActivity.this).load(portrait).asBitmap().into(img);
                dialog.setContentView(view,layoutParams);
                dialog.show();
            }
        });
        //长按监听，打开相册上传图片
        circleImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PhotoDialogShow();
                return true;
            }
        });
    }

    /**
     * 点击头像拍照相册dialog显示
     */
    private void PhotoDialogShow() {
        dialog = new Dialog(UserHomeActivity.this,R.style.MyDialog);
        View v = LayoutInflater.from(UserHomeActivity.this).inflate(R.layout.item_userhome_photo,null);
        Button bt_photo = (Button) v.findViewById(R.id.bt_photo);
        Button bt_img = (Button) v.findViewById(R.id.bt_imgs);
        Button bt_finish = (Button) v.findViewById(R.id.bt_finish);

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 调用系统的拍照功能
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
            }
        });
        bt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setContentView(v,layoutParams);
        dialog.show();

    }

    /**
     * 访问网络请求数据
     */
    public void getData() {
        startProgressDialog("正在获取数据",UserHomeActivity.this);
        token = myApp.userBean.getToken();
        Imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ver","0000000");
        params.addBodyParameter("imei",Imei);
        params.addBodyParameter("token",token);
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.setHttpClick(UelBack.usehome,params,TestBean.class,handler);
//        httpUtils = new HttpUtils();
//        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.usehome, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                try {
//                    JSONObject jsonObject = new JSONObject(responseInfo.result);
//                    int status = jsonObject.getInt("status");
//                    if (status==0){
//                        JSONObject jsdata = jsonObject.getJSONObject("data");
//                        uid = jsdata.getString("uid");//用户名
//                        portrait = jsdata.getString("portrait");//用户图标
//                        integration = jsdata.getInt("integration");//用户积分票总数
//                        comnum = jsdata.getInt("comnum");//用户评论总数
//
//
//                        JSONArray data = jsdata.getJSONArray("loginlog");
//                        Gson gson = new Gson();
//                        List<LogBean> loginBeen = gson.fromJson(String.valueOf(data),new TypeToken<List<LogBean>>() {}.getType());
//                        Message message = handler.obtainMessage();
//                        message.what=1;
//                        message.obj=loginBeen;
//                        handler.sendMessage(message);
//                    }else {
//                        handler.sendEmptyMessage(2);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                stopProgressDialog();
//                Jump(UserHomeActivity.this,MainActivity.class);
//                ToaL(UserHomeActivity.this,"登录失败，请检查网络设置");
//            }
//        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    stopProgressDialog();
                    Object obj = msg.obj;
                    TestBean testBean = (TestBean) obj;
                    uid = testBean.getUid();
                    portrait = testBean.getPortrait();//用户图标
                    integration = testBean.getIntegration();//用户积分票总数
                    comnum = testBean.getComnum();//用户评论总数
                    List<LogBean> loginBeen = testBean.getLoginlog();
//                    List<LogBean> loginBeen = (List<LogBean>) msg.obj;
                    setData(loginBeen);
                    break;
                case 2:
                    stopProgressDialog();
                    ToaL(UserHomeActivity.this,"获取数据失败");
                    break;
                case 3:
                    ToaS(UserHomeActivity.this,"上传成功");
                    circleImageView.setImageBitmap(tempBitmap);
                    break;
                case 4:
                    ToaS(UserHomeActivity.this,"上传失败");

                    break;
            }
        }
    };

    /**
     * recycleview设值
     * @param loginBeen
     */
    private void setData(List<LogBean> loginBeen) {
        ms.setPhoneInfo(Imei,token);
        UserInfoBean us = new UserInfoBean();
        us.setName(myApp.userBean.getName());
        us.setPassword(myApp.userBean.getPassword());
        us.setToken(myApp.userBean.getToken());
        us.setIcon(portrait);

        List<UserInfoBean> queryByWhere = udb.getQueryByWhere(UserInfoBean.class, "token = ?", new String[]{myApp.userBean.getToken()});
        if (queryByWhere.size() == 0){
            udb.Add(us);
        }

        mrecycleview.setLayoutManager(new LinearLayoutManager(this));
        mrecycleview.setItemAnimator(new CustomItemAnimator());
        UserLoginAdapter adapter = new UserLoginAdapter(UserHomeActivity.this,loginBeen);
        mrecycleview.setAdapter(adapter);

        tv_jifen.setText("用户积分："+integration+"");
        tv_usename.setText(uid);
        tv_tienumbers.setText("跟帖数统计: "+comnum+"");
        Glide.with(this).load(portrait).into(circleImageView);

        collapsingToolbarLayout.setTitle(uid);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);//没收缩时字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//收缩后字体颜色

    }

    /**
     * 用来绑定toolbar右上角菜单列表
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 拍照完成后调用裁剪
            case PHOTO_REQUEST_TAKEPHOTO:
                Log.d("拍照",tempFile.toString());
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;
            // 打开相册后调用图片裁剪
            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    Log.d("相册",data.getData().toString());
                    startPhotoZoom(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null)
                    try {
                        setPicToView(data);//将头像显示在UI上
//                        sendHead(data);//将头像上传到服务器
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 图片剪裁
     * @param uri
     * @param size
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);// 去黑边
//        intent.putExtra("scaleUpIfNeeded", true);// 去黑边

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    // 将进行剪裁后的图片显示到UI界面上
    @SuppressLint("NewApi")
    private Bitmap setPicToView(Intent picdata) throws Exception {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            tempBitmap = bundle.getParcelable("data");
//            Glide.with(UserHomeActivity.this).load(tempBitmap).asBitmap().into(circleImageView);
            File f = new File(String.valueOf(tempBitmap));
            Log.d("ceshi",f.toString());
            Log.d("测试",tempFile.toString());
            String path = String.valueOf(picdata.getParcelableExtra("data"));
            circleImageView.setImageBitmap(tempBitmap);
            Log.d("路径",path);
            Log.d("ceshi","运行到此处");
            sendHead(path);
            return tempBitmap;
        }
        return null;
    }

    private void sendHead(String data) throws Exception {
        File file = new File(data);
        MyApp myApp = (MyApp) this.getApplication();
        httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("token",myApp.userBean.getToken());
        params.addBodyParameter("portrait",file);
        Log.d("ceshi",myApp.userBean.getToken());
        Log.d("path",file.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, UelBack.ip + UelBack.send_user_head, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    Log.d("结果",jsonObject.getInt("status")+"  "+jsonObject.getString("message"));
                    stopProgressDialog();
                    if (jsonObject.getInt("result") == 0){
                        handler.sendEmptyMessage(3);
                    }else{
                        handler.sendEmptyMessage(4);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToaS(UserHomeActivity.this,"上传失败，请重新上传");
                handler.sendEmptyMessage(4);
            }
        });

    }

    /**
     * bitmap转换为file
     * @param bm
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File saveFile(Bitmap bm,String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }
}
