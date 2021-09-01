package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class LPOrderBean {

    /**
     * code : 0
     * msg : success
     * data : {"totalNum":2000,"totalOut":30000,"power":{"userPower":1,"sharePower":2,"powerIndex":3},"order":{"num":100,"lpNum":200,"dollarValue":1000,"levelName":"一星","createDate":"2021-05-31"},"yesterdayDollarValue":100,"yesterdayRewardNum":200}
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
         * totalNum : 2000.0
         * totalOut : 30000.0
         * power : {"userPower":1,"sharePower":2,"powerIndex":3}
         * order : {"num":100,"lpNum":200,"dollarValue":1000,"levelName":"一星","createDate":"2021-05-31"}
         * yesterdayDollarValue : 100.0
         * yesterdayRewardNum : 200.0
         */

        private BigDecimal totalNum;
        private double totalOut;
        private PowerBean power;
        private OrderBean order;
        private double yesterdayDollarValue;
        private double yesterdayRewardNum;

        public BigDecimal getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(BigDecimal totalNum) {
            this.totalNum = totalNum;
        }

        public double getTotalOut() {
            return totalOut;
        }

        public void setTotalOut(double totalOut) {
            this.totalOut = totalOut;
        }

        public PowerBean getPower() {
            return power;
        }

        public void setPower(PowerBean power) {
            this.power = power;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public double getYesterdayDollarValue() {
            return yesterdayDollarValue;
        }

        public void setYesterdayDollarValue(double yesterdayDollarValue) {
            this.yesterdayDollarValue = yesterdayDollarValue;
        }

        public double getYesterdayRewardNum() {
            return yesterdayRewardNum;
        }

        public void setYesterdayRewardNum(double yesterdayRewardNum) {
            this.yesterdayRewardNum = yesterdayRewardNum;
        }

        public static class PowerBean {
            /**
             * userPower : 1.0
             * sharePower : 2.0
             * powerIndex : 3.0
             */

            private double userPower;
            private double sharePower;
            private double powerIndex;

            public double getUserPower() {
                return userPower;
            }

            public void setUserPower(double userPower) {
                this.userPower = userPower;
            }

            public double getSharePower() {
                return sharePower;
            }

            public void setSharePower(double sharePower) {
                this.sharePower = sharePower;
            }

            public double getPowerIndex() {
                return powerIndex;
            }

            public void setPowerIndex(double powerIndex) {
                this.powerIndex = powerIndex;
            }
        }

        public static class OrderBean {
            /**
             * num : 100.0
             * lpNum : 200.0
             * dollarValue : 1000.0
             * levelName : 一星
             * createDate : 2021-05-31
             */

            private double num;
            private double lpNum;
            private double dollarValue;
            private String levelName;
            private String createDate;
            private double cappingMoney;
            private double gainMoney;

            public double getNum() {
                return num;
            }

            public void setNum(double num) {
                this.num = num;
            }

            public double getLpNum() {
                return lpNum;
            }

            public void setLpNum(double lpNum) {
                this.lpNum = lpNum;
            }

            public double getDollarValue() {
                return dollarValue;
            }

            public void setDollarValue(double dollarValue) {
                this.dollarValue = dollarValue;
            }

            public String getLevelName() {
                return levelName;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public double getCappingMoney() {
                return cappingMoney;
            }

            public void setCappingMoney(double cappingMoney) {
                this.cappingMoney = cappingMoney;
            }

            public double getGainMoney() {
                return gainMoney;
            }

            public void setGainMoney(double gainMoney) {
                this.gainMoney = gainMoney;
            }
        }
    }
}
