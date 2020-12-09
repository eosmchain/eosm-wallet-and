package com.token.mangowallet.bean;

public class OrderIDBean {

    /**
     * code : 0
     * msg : success
     * data : {"payMoney":70,"orderID":"MGP1597387208065581377014750"}
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
             * payMoney : 70
             * orderID : MGP1597387208065581377014750
             */

        private String payMoney;
        private String orderID;

        public String getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(String payMoney) {
            this.payMoney = payMoney;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }
    }
}
