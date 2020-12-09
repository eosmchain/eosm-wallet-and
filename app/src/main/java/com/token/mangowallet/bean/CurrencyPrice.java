package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class CurrencyPrice {

    /**
     * code : 0
     * msg : success
     * data : {"price":"1.2481","pair":"MGP_USDT"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * price : 1.2481
         * pair : MGP_USDT
         */

        private BigDecimal price;
        private String pair;

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }
    }
}
