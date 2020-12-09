package com.token.mangowallet.bean;

public class RealTimeIndexBean {

    /**
     * code : 0
     * msg : success
     * data : {"mapValue":"1018.800000","mgpNum":"1415.0000"}
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
         * mapValue : 1018.800000
         * mgpNum : 1415.0000
         */

        private String mapValue;
        private String mgpNum;
        private String transferSpeed;
        private String totalPower;
        private String destroyMgpNum;
        private String  minerPoolNum;

        public String getMapValue() {
            return mapValue;
        }

        public void setMapValue(String mapValue) {
            this.mapValue = mapValue;
        }

        public String getMgpNum() {
            return mgpNum;
        }

        public void setMgpNum(String mgpNum) {
            this.mgpNum = mgpNum;
        }

        public String getTransferSpeed() {
            return transferSpeed;
        }

        public void setTransferSpeed(String transferSpeed) {
            this.transferSpeed = transferSpeed;
        }

        public String getTotalPower() {
            return totalPower;
        }

        public void setTotalPower(String totalPower) {
            this.totalPower = totalPower;
        }

        public String getDestroyMgpNum() {
            return destroyMgpNum;
        }

        public void setDestroyMgpNum(String destroyMgpNum) {
            this.destroyMgpNum = destroyMgpNum;
        }

        public String getMinerPoolNum() {
            return minerPoolNum;
        }

        public void setMinerPoolNum(String minerPoolNum) {
            this.minerPoolNum = minerPoolNum;
        }
    }
}
