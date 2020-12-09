package com.token.mangowallet.bean;

import java.util.List;

public class MyOrderListBean {
    /**
     * code : 0
     * msg : success
     * data : {"money":"0.0000","list":[]}
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
         * money : 0.0000
         * list : []
         */

        private String money;
        private List<MiningOrderIncomeItem> list;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public List<MiningOrderIncomeItem> getList() {
            return list;
        }

        public void setList(List<MiningOrderIncomeItem> list) {
            this.list = list;
        }
    }

}
