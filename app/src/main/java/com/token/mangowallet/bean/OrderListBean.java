package com.token.mangowallet.bean;

import java.math.BigDecimal;
import java.util.List;


public class OrderListBean {

    /**
     * code : 0
     * msg : success
     * data : {"list":[{"id":23,"orderName":"Ijjjjsjkd","orderSn":"MLO20200911173706000023","gainAddress":"mgptest11444","createTime":"2020-09-11","payTime":null,"endTime":null,"updateTime":"2020-11-02","payPrice":0,"money":1400,"payNum":0,"payHash":null,"status":1,"storePro":0,"buyerPro":0,"msg":null,"serviceChargePro":null,"serviceCharge":null,"storeFlag":1}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 23
             * orderName : Ijjjjsjkd
             * orderSn : MLO20200911173706000023
             * gainAddress : mgptest11444
             * createTime : 2020-09-11
             * payTime : null
             * endTime : null
             * updateTime : 2020-11-02
             * payPrice : 0
             * money : 1400
             * payNum : 0
             * payHash : null
             * status : 1
             * storePro : 0
             * buyerPro : 0
             * msg : null
             * serviceChargePro : null
             * serviceCharge : null
             * storeFlag : 1
             */

            private int id;
            private String orderName;
            private String orderSn;
            private String gainAddress;
            private String createTime;
            private String payTime;
            private String endTime;
            private String updateTime;
            private BigDecimal payPrice;
            private BigDecimal money;
            private BigDecimal payNum;
            private String payHash;
            private int status;
            private String storePro;
            private String buyerPro;
            private String msg;
            private String serviceChargePro;
            private String serviceCharge;
            private int storeFlag;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderName() {
                return orderName;
            }

            public void setOrderName(String orderName) {
                this.orderName = orderName;
            }

            public String getOrderSn() {
                return orderSn;
            }

            public void setOrderSn(String orderSn) {
                this.orderSn = orderSn;
            }

            public String getGainAddress() {
                return gainAddress;
            }

            public void setGainAddress(String gainAddress) {
                this.gainAddress = gainAddress;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public BigDecimal getPayPrice() {
                return payPrice;
            }

            public void setPayPrice(BigDecimal payPrice) {
                this.payPrice = payPrice;
            }

            public BigDecimal getMoney() {
                return money;
            }

            public void setMoney(BigDecimal money) {
                this.money = money;
            }

            public BigDecimal getPayNum() {
                return payNum;
            }

            public void setPayNum(BigDecimal payNum) {
                this.payNum = payNum;
            }

            public String getPayHash() {
                return payHash;
            }

            public void setPayHash(String payHash) {
                this.payHash = payHash;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getStorePro() {
                return storePro;
            }

            public void setStorePro(String storePro) {
                this.storePro = storePro;
            }

            public String getBuyerPro() {
                return buyerPro;
            }

            public void setBuyerPro(String buyerPro) {
                this.buyerPro = buyerPro;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getServiceChargePro() {
                return serviceChargePro;
            }

            public void setServiceChargePro(String serviceChargePro) {
                this.serviceChargePro = serviceChargePro;
            }

            public String getServiceCharge() {
                return serviceCharge;
            }

            public void setServiceCharge(String serviceCharge) {
                this.serviceCharge = serviceCharge;
            }

            public int getStoreFlag() {
                return storeFlag;
            }

            public void setStoreFlag(int storeFlag) {
                this.storeFlag = storeFlag;
            }
        }
    }
}
