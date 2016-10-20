package com.example.yao.pressclient.url;

/**
 * Created by Administrator on 2016/8/2.
 */
public  class UelBack {
    //主ip地址
    public static String ip = "http://118.244.212.82:9092/newsClient/";
    //登陆 user_login?ver=版本号&uid=用户名&pwd=密码&device=0
    public static String login = "user_login?";
    //注册user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
    public static String regist = "user_register?";
    //user_forgetpass?ver=版本号&email=邮箱
    public static String finfpass = "user_forgetpass?";
    //user_home?ver=版本号&imei=手机标识符&token =用户令牌
    public static String usehome = "user_home?";
    //news_list?ver=版本号&subid=分类名&dir=1&nid=新闻id&stamp=20140321&cnt=20
    public static String news = "news_list?";
    //user_image?token=用户令牌& portrait =头像
    public static String send_user_head = "user_image?";
    //cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
    public static String seng_message = "cmt_commit?" ;
    //显示评论界面cmt_list?ver=1&nid=20&type=1&stamp=20160617&dir=0&cid=111
    public static String show_tell = "cmt_list?";

}
