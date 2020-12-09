package com.token.mangowallet.bean;

public class TransactionBean {
    public boolean isSuccess;
    public String msg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setData(boolean success, String msg) {
        this.isSuccess = success;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
