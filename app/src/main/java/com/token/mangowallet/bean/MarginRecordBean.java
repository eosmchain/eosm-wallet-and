package com.token.mangowallet.bean;

import java.util.List;

public class MarginRecordBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":1,"userId":9999,"money":11,"createTime":"2020-09-29","channelType":1,"moneyType":1,"price":1,"mark":"首次开通"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * userId : 9999
         * money : 11
         * createTime : 2020-09-29
         * channelType : 1
         * moneyType : 1
         * price : 1
         * mark : 首次开通
         */

        private int id;
        private int userId;
        private String money;
        private String createTime;
        private int channelType;
        private int moneyType;
        private String price;
        private String mark;
        private String money_str;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getChannelType() {
            return channelType;
        }

        public void setChannelType(int channelType) {
            this.channelType = channelType;
        }

        public int getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(int moneyType) {
            this.moneyType = moneyType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getMoney_str() {
            return money_str;
        }

        public void setMoney_str(String money_str) {
            this.money_str = money_str;
        }
    }
}
