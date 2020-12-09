package com.token.mangowallet.bean;

public class BindMidBean {
    /**
     * code : 0
     * data : {"id":6,"adress":"gygygyg12345","recommender":"jasonone1111","lnvitationCode":"M956775173","balance":0,"createTime":"2020-06-03"}
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
         * id : 6
         * adress : gygygyg12345
         * recommender : jasonone1111
         * lnvitationCode : M956775173
         * balance : 0
         * createTime : 2020-06-03
         */

        private int id;
        private String adress;
        private String recommender;
        private String lnvitationCode;
        private int balance;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getRecommender() {
            return recommender;
        }

        public void setRecommender(String recommender) {
            this.recommender = recommender;
        }

        public String getLnvitationCode() {
            return lnvitationCode;
        }

        public void setLnvitationCode(String lnvitationCode) {
            this.lnvitationCode = lnvitationCode;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
