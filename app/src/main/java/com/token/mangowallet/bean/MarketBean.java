package com.token.mangowallet.bean;

import java.util.List;

public class MarketBean {

    /**
     * code : 0
     * msg : success
     * data : {"sellerGainPro":"0.20","sellerGainEditFlag":1,"minOrderGainPro":"0.20","orderGainPro":"0.30","orderGainEditFlag":1,"buyerGainPro":"0.50","buyerGainEditFlag":0}
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
         * "sellerGainPro": "0.20", // 卖家收取比例
         * "sellerGainEditFlag": 1, // 卖家收取比例是否可自行调整 1 是
         * "minOrderGainPro": "0.20", // 最小奖金激励比例
         * "orderGainPro": "0.30", // 奖金激励比例
         * "orderGainEditFlag": 1, // 卖家是否可自行调整奖金比例
         * "buyerGainPro": "0.50", // 抵押订单比例
         * "buyerGainEditFlag": 0, // 抵押比例调整模式 1 可调整固定比例 0 固定比例不可调整 2 使用比例选项
         * "buyerGainPros": [ // 抵押订单比例选项
         * "0.5",
         * "0.9"
         * ]
         */

        private String sellerGainPro;
        private int sellerGainEditFlag;
        private String minOrderGainPro;
        private String orderGainPro;
        private int orderGainEditFlag;
        private String buyerGainPro;
        private int buyerGainEditFlag;
        private List<String> buyerGainPros;

        public String getSellerGainPro() {
            return sellerGainPro;
        }

        public void setSellerGainPro(String sellerGainPro) {
            this.sellerGainPro = sellerGainPro;
        }

        public int getSellerGainEditFlag() {
            return sellerGainEditFlag;
        }

        public void setSellerGainEditFlag(int sellerGainEditFlag) {
            this.sellerGainEditFlag = sellerGainEditFlag;
        }

        public String getMinOrderGainPro() {
            return minOrderGainPro;
        }

        public void setMinOrderGainPro(String minOrderGainPro) {
            this.minOrderGainPro = minOrderGainPro;
        }

        public String getOrderGainPro() {
            return orderGainPro;
        }

        public void setOrderGainPro(String orderGainPro) {
            this.orderGainPro = orderGainPro;
        }

        public int getOrderGainEditFlag() {
            return orderGainEditFlag;
        }

        public void setOrderGainEditFlag(int orderGainEditFlag) {
            this.orderGainEditFlag = orderGainEditFlag;
        }

        public String getBuyerGainPro() {
            return buyerGainPro;
        }

        public void setBuyerGainPro(String buyerGainPro) {
            this.buyerGainPro = buyerGainPro;
        }

        public int getBuyerGainEditFlag() {
            return buyerGainEditFlag;
        }

        public void setBuyerGainEditFlag(int buyerGainEditFlag) {
            this.buyerGainEditFlag = buyerGainEditFlag;
        }

        public List<String> getBuyerGainPros() {
            return buyerGainPros;
        }

        public void setBuyerGainPros(List<String> buyerGainPros) {
            this.buyerGainPros = buyerGainPros;
        }
    }
}
