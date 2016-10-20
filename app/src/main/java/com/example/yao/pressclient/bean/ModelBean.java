package com.example.yao.pressclient.bean;

/**
 * Created by Yao on 2016/9/21.
 */
public class ModelBean {

    //新闻界面
    private String title;  //新闻标题
    private String summary;  //新闻摘要
    private int nid;  //新闻编号
    private String stamp;  //新闻时间戳
    private String link;  //新闻链接
    private String icon;  //图片路径
    private int type; // 1表示列表新闻 2表示大图新闻
    private Boolean isShow = false;//评论是否显示

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
