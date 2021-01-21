package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class CheckSuperNodeBean {

    /**
     * code : 0
     * msg : success
     * data : {"isSuperNode":1,"money":331821.1064,"isVote":0}
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
         * isSuperNode : 1
         * money : 331821.1064
         * isVote : 0
         */

        private int isSuperNode;
        private BigDecimal money;
        private int isVote;

        public int getIsSuperNode() {
            return isSuperNode;
        }

        public void setIsSuperNode(int isSuperNode) {
            this.isSuperNode = isSuperNode;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }

        public int getIsVote() {
            return isVote;
        }

        public void setIsVote(int isVote) {
            this.isVote = isVote;
        }
    }
}
