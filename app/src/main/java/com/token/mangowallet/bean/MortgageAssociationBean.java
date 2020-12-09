package com.token.mangowallet.bean;

import java.util.List;

public class MortgageAssociationBean {

    /**
     * code : 0
     * msg : success
     * data : {"lightNodeFlag":0,"yesterdayTeamMoney":"0.0000","yesterdayLightNodeMoney":"0.0000","myPushPro":"30 %","yesterdayMoney":"0.0000","teamValue":"0.00","teamNum":"0.0000","teamList":[{"code":"M929626135","coinValue":"14.32","coinNum":"20.0000","roleList":["M1"],"teamAddress":"mgpchain3333"},{"code":"M773412099","coinValue":"0.00","coinNum":"0.0000","roleList":[],"teamAddress":"thebestlrnio"}],"yesterdayPushMoney":"0.0000","myFloor":2}
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
         * lightNodeFlag : 0
         * yesterdayTeamMoney : 0.0000
         * yesterdayLightNodeMoney : 0.0000
         * myPushPro : 30 %
         * yesterdayMoney : 0.0000
         * teamValue : 0.00
         * teamNum : 0.0000
         * teamList : [{"code":"M929626135","coinValue":"14.32","coinNum":"20.0000","roleList":["M1"],"teamAddress":"mgpchain3333"},{"code":"M773412099","coinValue":"0.00","coinNum":"0.0000","roleList":[],"teamAddress":"thebestlrnio"}]
         * yesterdayPushMoney : 0.0000
         * myFloor : 2
         */

        private int lightNodeFlag;
        private String yesterdayTeamMoney;
        private String yesterdayLightNodeMoney;
        private String myPushPro;
        private String yesterdayMoney;
        private String teamValue;
        private String teamNum;
        private String yesterdayPushMoney;
        private int myFloor;
        private List<TeamListBean> teamList;

        public int getLightNodeFlag() {
            return lightNodeFlag;
        }

        public void setLightNodeFlag(int lightNodeFlag) {
            this.lightNodeFlag = lightNodeFlag;
        }

        public String getYesterdayTeamMoney() {
            return yesterdayTeamMoney;
        }

        public void setYesterdayTeamMoney(String yesterdayTeamMoney) {
            this.yesterdayTeamMoney = yesterdayTeamMoney;
        }

        public String getYesterdayLightNodeMoney() {
            return yesterdayLightNodeMoney;
        }

        public void setYesterdayLightNodeMoney(String yesterdayLightNodeMoney) {
            this.yesterdayLightNodeMoney = yesterdayLightNodeMoney;
        }

        public String getMyPushPro() {
            return myPushPro;
        }

        public void setMyPushPro(String myPushPro) {
            this.myPushPro = myPushPro;
        }

        public String getYesterdayMoney() {
            return yesterdayMoney;
        }

        public void setYesterdayMoney(String yesterdayMoney) {
            this.yesterdayMoney = yesterdayMoney;
        }

        public String getTeamValue() {
            return teamValue;
        }

        public void setTeamValue(String teamValue) {
            this.teamValue = teamValue;
        }

        public String getTeamNum() {
            return teamNum;
        }

        public void setTeamNum(String teamNum) {
            this.teamNum = teamNum;
        }

        public String getYesterdayPushMoney() {
            return yesterdayPushMoney;
        }

        public void setYesterdayPushMoney(String yesterdayPushMoney) {
            this.yesterdayPushMoney = yesterdayPushMoney;
        }

        public int getMyFloor() {
            return myFloor;
        }

        public void setMyFloor(int myFloor) {
            this.myFloor = myFloor;
        }

        public List<TeamListBean> getTeamList() {
            return teamList;
        }

        public void setTeamList(List<TeamListBean> teamList) {
            this.teamList = teamList;
        }

        public static class TeamListBean {
            /**
             * code : M929626135
             * coinValue : 14.32
             * coinNum : 20.0000
             * roleList : ["M1"]
             * teamAddress : mgpchain3333
             */

            private String code;
            private String coinValue;
            private String coinNum;
            private String teamAddress;
            private List<String> roleList;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCoinValue() {
                return coinValue;
            }

            public void setCoinValue(String coinValue) {
                this.coinValue = coinValue;
            }

            public String getCoinNum() {
                return coinNum;
            }

            public void setCoinNum(String coinNum) {
                this.coinNum = coinNum;
            }

            public String getTeamAddress() {
                return teamAddress;
            }

            public void setTeamAddress(String teamAddress) {
                this.teamAddress = teamAddress;
            }

            public List<String> getRoleList() {
                return roleList;
            }

            public void setRoleList(List<String> roleList) {
                this.roleList = roleList;
            }
        }
    }
}
