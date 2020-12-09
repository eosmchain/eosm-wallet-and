package com.token.mangowallet.test;

import java.util.List;

public class TES {

    /**
     * code : 0
     * msg : success
     * data : {"sellerGainPro":"0.2000","sellerGainEditFlag":1,"minOrderGainPro":"0.0000","orderGainPro":"0.3000","orderGainEditFlag":0,"buyerGainPro":"0.5000","buyerGainEditFlag":2,"buyerGainPros":["0.2","0.333","0.5"]}
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
         * sellerGainPro : 0.2000
         * sellerGainEditFlag : 1
         * minOrderGainPro : 0.0000
         * orderGainPro : 0.3000
         * orderGainEditFlag : 0
         * buyerGainPro : 0.5000
         * buyerGainEditFlag : 2
         * buyerGainPros : ["0.2","0.333","0.5"]
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
