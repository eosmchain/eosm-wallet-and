package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class OrderSysBean {

    /**
     * code : 0
     * msg : success
     * data : {"mgp_pro":"0.6","hmio_pro":"0.4"}
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
         * mgp_pro : 0.6
         * hmio_pro : 0.4
         */

        private BigDecimal mgp_pro;
        private BigDecimal hmio_pro;

        public BigDecimal getMgp_pro() {
            return mgp_pro;
        }

        public void setMgp_pro(BigDecimal mgp_pro) {
            this.mgp_pro = mgp_pro;
        }

        public BigDecimal getHmio_pro() {
            return hmio_pro;
        }

        public void setHmio_pro(BigDecimal hmio_pro) {
            this.hmio_pro = hmio_pro;
        }
    }
}
