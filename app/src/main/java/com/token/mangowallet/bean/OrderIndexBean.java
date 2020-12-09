package com.token.mangowallet.bean;

public class OrderIndexBean {

    /**
     * code : 0
     * msg : success
     * data : {"sysMgpNum":"2411412.9565","calPower":{"pushPower":"22153.3923","teamPower":"14502.1287","powerIndex":"0","lightPower":"6228.1936","userPower":"31937.7436","lightNodePower":"3298.3172"},"yesterdayOrder":{"money":"0","pro":"0","orderValue":"0"},"order":{"lockStatus":"UNLOCK","orderLevel":"M1","createTime":"2020-07-29","unlockTime":"2020-07-29","dailyPro":"0.001400","orderValue":"300.00","mgpNum":"300.0000"}}
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
         * sysMgpNum : 2411412.9565
         * calPower : {"pushPower":"22153.3923","teamPower":"14502.1287","powerIndex":"0","lightPower":"6228.1936","userPower":"31937.7436","lightNodePower":"3298.3172"}
         * yesterdayOrder : {"money":"0","pro":"0","orderValue":"0"}
         * order : {"lockStatus":"UNLOCK","orderLevel":"M1","createTime":"2020-07-29","unlockTime":"2020-07-29","dailyPro":"0.001400","orderValue":"300.00","mgpNum":"300.0000"}
         */

        private String sysMgpNum;
        private CalPowerBean calPower;
        private YesterdayOrderBean yesterdayOrder;
        private OrderBean order;

        public String getSysMgpNum() {
            return sysMgpNum;
        }

        public void setSysMgpNum(String sysMgpNum) {
            this.sysMgpNum = sysMgpNum;
        }

        public CalPowerBean getCalPower() {
            return calPower;
        }

        public void setCalPower(CalPowerBean calPower) {
            this.calPower = calPower;
        }

        public YesterdayOrderBean getYesterdayOrder() {
            return yesterdayOrder;
        }

        public void setYesterdayOrder(YesterdayOrderBean yesterdayOrder) {
            this.yesterdayOrder = yesterdayOrder;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public static class CalPowerBean {
            /**
             * pushPower : 22153.3923
             * teamPower : 14502.1287
             * powerIndex : 0
             * lightPower : 6228.1936
             * userPower : 31937.7436
             * lightNodePower : 3298.3172
             */

            private String pushPower;
            private String teamPower;
            private String powerIndex;
            private String lightPower;
            private String userPower;
            private String lightNodePower;

            public String getPushPower() {
                return pushPower;
            }

            public void setPushPower(String pushPower) {
                this.pushPower = pushPower;
            }

            public String getTeamPower() {
                return teamPower;
            }

            public void setTeamPower(String teamPower) {
                this.teamPower = teamPower;
            }

            public String getPowerIndex() {
                return powerIndex;
            }

            public void setPowerIndex(String powerIndex) {
                this.powerIndex = powerIndex;
            }

            public String getLightPower() {
                return lightPower;
            }

            public void setLightPower(String lightPower) {
                this.lightPower = lightPower;
            }

            public String getUserPower() {
                return userPower;
            }

            public void setUserPower(String userPower) {
                this.userPower = userPower;
            }

            public String getLightNodePower() {
                return lightNodePower;
            }

            public void setLightNodePower(String lightNodePower) {
                this.lightNodePower = lightNodePower;
            }
        }

        public static class YesterdayOrderBean {
            /**
             * money : 0
             * pro : 0
             * orderValue : 0
             */

            private String money;
            private String pro;
            private String orderValue;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getPro() {
                return pro;
            }

            public void setPro(String pro) {
                this.pro = pro;
            }

            public String getOrderValue() {
                return orderValue;
            }

            public void setOrderValue(String orderValue) {
                this.orderValue = orderValue;
            }
        }

        public static class OrderBean {
            /**
             * lockStatus : UNLOCK
             * orderLevel : M1
             * createTime : 2020-07-29
             * unlockTime : 2020-07-29
             * dailyPro : 0.001400
             * orderValue : 300.00
             * mgpNum : 300.0000
             */

            private String lockStatus;
            private String orderLevel;
            private String createTime;
            private String unlockTime;
            private String dailyPro;
            private String orderValue;
            private String mgpNum;

            public String getLockStatus() {
                return lockStatus;
            }

            public void setLockStatus(String lockStatus) {
                this.lockStatus = lockStatus;
            }

            public String getOrderLevel() {
                return orderLevel;
            }

            public void setOrderLevel(String orderLevel) {
                this.orderLevel = orderLevel;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUnlockTime() {
                return unlockTime;
            }

            public void setUnlockTime(String unlockTime) {
                this.unlockTime = unlockTime;
            }

            public String getDailyPro() {
                return dailyPro;
            }

            public void setDailyPro(String dailyPro) {
                this.dailyPro = dailyPro;
            }

            public String getOrderValue() {
                return orderValue;
            }

            public void setOrderValue(String orderValue) {
                this.orderValue = orderValue;
            }

            public String getMgpNum() {
                return mgpNum;
            }

            public void setMgpNum(String mgpNum) {
                this.mgpNum = mgpNum;
            }
        }
    }
}
