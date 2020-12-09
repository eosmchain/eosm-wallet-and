package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class LowerBean {

    /**
     * code : 0
     * msg : 您的保证金需要充1.5MGP,否则将会影响商户交易
     * data : 1.5
     */

    private int code;
    private String msg;
    private BigDecimal data;

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

    public BigDecimal getData() {
        return data;
    }

    public void setData(BigDecimal data) {
        this.data = data;
    }
}
