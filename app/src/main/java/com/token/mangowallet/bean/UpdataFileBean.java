package com.token.mangowallet.bean;

public class UpdataFileBean {

    /**
     * code : 0
     * msg : success
     * data : {"bucket":"","key":"","engine":"","endpoint":"","message":null,"url":"https://otcstore.mgps.me/mgp/images/20210111/239A2C568EEE4E75A12CB4CCF402772C20201218_165830.jpg"}
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
         * bucket :
         * key :
         * engine :
         * endpoint :
         * message : null
         * url : https://otcstore.mgps.me/mgp/images/20210111/239A2C568EEE4E75A12CB4CCF402772C20201218_165830.jpg
         */

        private String bucket;
        private String key;
        private String engine;
        private String endpoint;
        private Object message;
        private String url;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
