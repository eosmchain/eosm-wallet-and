package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class PayInitBean {

    /**
     * code : 0
     * msg : success
     * data : {"buyerPro":1,"storePro":0,"isMer":1,"rewardPro":0,"gainAddress":"uuuuuuuu1234"}
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
         * buyerPro : 1
         * storePro : 0
         * isMer : 1
         * rewardPro : 0
         * gainAddress : uuuuuuuu1234
         */

        private String buyerPro;
        private String storePro;
        private int isMer;
        private String rewardPro;
        private String gainAddress;
        private BigDecimal serviceChargePro;

        public String getBuyerPro() {
            return buyerPro;
        }

        public void setBuyerPro(String buyerPro) {
            this.buyerPro = buyerPro;
        }

        public String getStorePro() {
            return storePro;
        }

        public void setStorePro(String storePro) {
            this.storePro = storePro;
        }

        public int getIsMer() {
            return isMer;
        }

        public void setIsMer(int isMer) {
            this.isMer = isMer;
        }

        public String getRewardPro() {
            return rewardPro;
        }

        public void setRewardPro(String rewardPro) {
            this.rewardPro = rewardPro;
        }

        public String getGainAddress() {
            return gainAddress;
        }

        public void setGainAddress(String gainAddress) {
            this.gainAddress = gainAddress;
        }

        public BigDecimal getServiceChargePro() {
            return serviceChargePro;
        }

        public void setServiceChargePro(BigDecimal serviceChargePro) {
            this.serviceChargePro = serviceChargePro;
        }


    }
}
