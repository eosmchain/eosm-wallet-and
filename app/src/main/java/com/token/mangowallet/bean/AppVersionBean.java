package com.token.mangowallet.bean;

public class AppVersionBean {


    /**
     * code : 0
     * msg : success
     * data : {"msg":"","download":"http://kliamw.cn/apk/mangoWallet_1.0.0.12.apk","versionNum":"26","force":"0","versionCode":"1.0.0.9"}
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
         * msg :
         * download : http://kliamw.cn/apk/mangoWallet_1.0.0.12.apk
         * versionNum : 26
         * force : 0
         * versionCode : 1.0.0.9
         */

        private String msg;
        private String download;
        private String versionNum;
        private int force;
        private String versionCode;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getVersionNum() {
            return versionNum;
        }

        public void setVersionNum(String versionNum) {
            this.versionNum = versionNum;
        }

        public int getForce() {
            return force;
        }

        public void setForce(int force) {
            this.force = force;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }
    }
}
