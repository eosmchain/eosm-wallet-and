package com.token.mangowallet.bean;

public class AddOrderBean {

    /**
     * code : 0
     * msg : success
     * data : {"buyerPro":0.5,"orderSn":"MLO20200901163212000015","storePro":0.5,"rewardPro":0}
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
         * "buyerPro": 0.5,// 买家收取
         * "orderSn": "MLO20200901163212000015", // 订单号
         * "storePro": 0.5, // 商家收取
         * "rewardPro": 0 // 奖励
         */

        private String buyerPro;
        private String orderSn;
        private String storePro;
        private String rewardPro;

        public String getBuyerPro() {
            return buyerPro;
        }

        public void setBuyerPro(String buyerPro) {
            this.buyerPro = buyerPro;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public String getStorePro() {
            return storePro;
        }

        public void setStorePro(String storePro) {
            this.storePro = storePro;
        }

        public String getRewardPro() {
            return rewardPro;
        }

        public void setRewardPro(String rewardPro) {
            this.rewardPro = rewardPro;
        }
    }
}
