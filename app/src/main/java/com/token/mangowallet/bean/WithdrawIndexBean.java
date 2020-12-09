package com.token.mangowallet.bean;

public class WithdrawIndexBean {

    /**
     * code : 0
     * msg : success
     * data : {"serviceCharge":"0.008","money":0,"lockMoney":"0.00","nextUnlockTime":""}
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
         * serviceCharge : 0.008
         * money : 0
         * lockMoney : 0.00
         * nextUnlockTime :
         */

        private String serviceCharge;
        private String money;
        private String lockMoney;
        private String nextUnlockTime;

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getLockMoney() {
            return lockMoney;
        }

        public void setLockMoney(String lockMoney) {
            this.lockMoney = lockMoney;
        }

        public String getNextUnlockTime() {
            return nextUnlockTime;
        }

        public void setNextUnlockTime(String nextUnlockTime) {
            this.nextUnlockTime = nextUnlockTime;
        }
    }
}
