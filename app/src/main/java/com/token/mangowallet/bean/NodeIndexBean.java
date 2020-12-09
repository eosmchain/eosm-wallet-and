package com.token.mangowallet.bean;

public class NodeIndexBean {

    /**
     * code : 0
     * msg : success
     * data : {"nodeNum":0,"nodeLevel":"P0","teamMoney":"0.0000","yesterdayMoney":"0","teamValue":"0.00"}
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
         * nodeNum : 0
         * nodeLevel : P0
         * teamMoney : 0.0000
         * yesterdayMoney : 0
         * teamValue : 0.00
         */

        private int nodeNum;
        private String nodeLevel;
        private String teamMoney;
        private String yesterdayMoney;
        private String teamValue;

        public int getNodeNum() {
            return nodeNum;
        }

        public void setNodeNum(int nodeNum) {
            this.nodeNum = nodeNum;
        }

        public String getNodeLevel() {
            return nodeLevel;
        }

        public void setNodeLevel(String nodeLevel) {
            this.nodeLevel = nodeLevel;
        }

        public String getTeamMoney() {
            return teamMoney;
        }

        public void setTeamMoney(String teamMoney) {
            this.teamMoney = teamMoney;
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
    }
}
