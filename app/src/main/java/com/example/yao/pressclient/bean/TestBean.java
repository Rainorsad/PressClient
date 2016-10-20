package com.example.yao.pressclient.bean;

import java.util.List;

/**
 * Created by Yao on 2016/10/19.
 */
public class TestBean {
    private String uid;
    private String portrait;
    private int integration;
    private int comnum;
    public List<LogBean> loginlog;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public int getComnum() {
        return comnum;
    }

    public void setComnum(int comnum) {
        this.comnum = comnum;
    }

    public List<LogBean> getLoginlog() {
        return loginlog;
    }

    public void setLoginlog(List<LogBean> loginlog) {
        this.loginlog = loginlog;
    }
}
