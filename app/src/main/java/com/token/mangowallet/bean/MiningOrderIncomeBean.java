package com.token.mangowallet.bean;

import java.util.List;

public class MiningOrderIncomeBean {

    /**
     * code : 0
     * msg : success
     * data : {"balance":"100.0000","lockMoney":"0.0000","balanceValue":"71.61","list":[{"typeName":"V","type":11,"money":"157.50000000","createTime":"2020-05-19","orderLevelName":"MGP"},{"typeName":"V","type":11,"money":"157.50000000","createTime":"2020-05-19","orderLevelName":"MGP"},{"typeName":"V","type":11,"money":"72.00000000","createTime":"2020-05-19","orderLevelName":"MGP"},{"typeName":"V","type":11,"money":"27.00000000","createTime":"2020-05-19","orderLevelName":"MGP"}]}
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
         * balance : 100.0000
         * lockMoney : 0.0000
         * balanceValue : 71.61
         * list : [{"typeName":"V","type":11,"money":"157.50000000","createTime":"2020-05-19","orderLevelName":"MGP"},{"typeName":"V","type":11,"money":"157.50000000","createTime":"2020-05-19","orderLevelName":"MGP"},{"typeName":"V","type":11,"money":"72.00000000","createTime":"2020-05-19","orderLevelName":"MGP"},{"typeName":"V","type":11,"money":"27.00000000","createTime":"2020-05-19","orderLevelName":"MGP"}]
         */

        private String balance;
        private String lockMoney;
        private String balanceValue;
        private List<ListBean> list;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getLockMoney() {
            return lockMoney;
        }

        public void setLockMoney(String lockMoney) {
            this.lockMoney = lockMoney;
        }

        public String getBalanceValue() {
            return balanceValue;
        }

        public void setBalanceValue(String balanceValue) {
            this.balanceValue = balanceValue;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * typeName : V
             * type : 11
             * money : 157.50000000
             * createTime : 2020-05-19
             * orderLevelName : MGP
             */

            private String typeName;
            private int type;
            private String money;
            private String createTime;
            private String orderLevelName;

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getOrderLevelName() {
                return orderLevelName;
            }

            public void setOrderLevelName(String orderLevelName) {
                this.orderLevelName = orderLevelName;
            }
        }
    }
}
