package com.token.mangowallet.bean;

import java.math.BigDecimal;
import java.util.List;

public class MixMortgageBean {

    /**
     * code : 0
     * msg : success
     * <p>"data":
     * "id":1,// id
     * "moneyType": 4, // 需支付币种 4 HMIO ，1 MGP
     * "payMoneyType": "HMIO",
     * "num": 100, // 需支付数量
     * "status": 4, // 状态 1 已完成 2 待转hmio 3 待转MGP 4 MGP 扫描中 5 hmio扫描中 6 生成中 7 失败
     * "statusName":"lsl" // 状态名称
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
         * moneyType : 4
         * payMoneyType : HMIO
         * num : 100
         * status : 4
         * statusName : lsl
         */

        private int id;
        private int moneyType;
        private String payMoneyType;
        private BigDecimal num;
        private int status;
        private String statusName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(int moneyType) {
            this.moneyType = moneyType;
        }

        public String getPayMoneyType() {
            return payMoneyType;
        }

        public void setPayMoneyType(String payMoneyType) {
            this.payMoneyType = payMoneyType;
        }

        public BigDecimal getNum() {
            return num;
        }

        public void setNum(BigDecimal num) {
            this.num = num;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }
    }
}
