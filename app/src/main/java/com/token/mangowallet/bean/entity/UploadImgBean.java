package com.token.mangowallet.bean.entity;

import java.util.List;

public class UploadImgBean {

    /**
     * code : 0
     * data : ["https://api.coom.pub/img/2963292847341597845282236.png","https://api.coom.pub/img/5821038940471597845282236.png","https://api.coom.pub/img/7487256228871597845282236.png"]
     */

    private int code;
    private String msg;
    private List<String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
