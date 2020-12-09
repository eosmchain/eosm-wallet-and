package com.token.mangowallet.bean;

import java.util.List;

public class CurrencySetupBean {

    /**
     * code : 0
     * msg : success
     * data : [{"name":"人民币","symbol":"￥","price":"6.91","sort":1,"symbolName":"CNY"},{"name":"美元","symbol":"$","price":"1.00","sort":2,"symbolName":"USD"},{"name":"韩元元","symbol":"\u20a9","price":"1.00","sort":3,"symbolName":"KRW"},{"name":"日元","symbol":"¥","price":"105.84","sort":4,"symbolName":"Yen"}]
     */

    private int code;
    private String msg;
    private List<CurrencyData> data;

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

    public List<CurrencyData> getData() {
        return data;
    }

    public void setData(List<CurrencyData> data) {
        this.data = data;
    }

}
