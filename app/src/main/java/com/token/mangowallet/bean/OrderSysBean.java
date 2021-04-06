package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class OrderSysBean {
    /**
     * code : 0
     * msg : success
     * data : {"receive":"0xDA79e4a8839CB8EAF9BeCe5DB71feAA4565347B2","mgp_pro":"0.7","coin_type":"USBC","contract":"0xa5b80588ee71e165e0d2f7284eac0ee4c2883aaa","hmio_pro":"0.3"}
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

    public static class DataBean implements Parcelable {
        /**
         * receive : 0xDA79e4a8839CB8EAF9BeCe5DB71feAA4565347B2
         * mgp_pro : 0.7
         * coin_type : USBC
         * contract : 0xa5b80588ee71e165e0d2f7284eac0ee4c2883aaa
         * hmio_pro : 0.3
         */

        private String receive;
        private BigDecimal mgp_pro;
        private String coin_type;
        private String contract;
        private BigDecimal hmio_pro;

        protected DataBean(Parcel in) {
            receive = in.readString();
            coin_type = in.readString();
            contract = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(receive);
            dest.writeString(coin_type);
            dest.writeString(contract);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getReceive() {
            return receive;
        }

        public void setReceive(String receive) {
            this.receive = receive;
        }

        public BigDecimal getMgp_pro() {
            return mgp_pro;
        }

        public void setMgp_pro(BigDecimal mgp_pro) {
            this.mgp_pro = mgp_pro;
        }

        public String getCoin_type() {
            return coin_type;
        }

        public void setCoin_type(String coin_type) {
            this.coin_type = coin_type;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public BigDecimal getHmio_pro() {
            return hmio_pro;
        }

        public void setHmio_pro(BigDecimal hmio_pro) {
            this.hmio_pro = hmio_pro;
        }
    }

//    /**
//     * code : 0
//     * msg : success
//     * data : {"mgp_pro":"0.6","hmio_pro":"0.4"}
//     */
//
//    private int code;
//    private String msg;
//    private DataBean data;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * mgp_pro : 0.6
//         * hmio_pro : 0.4
//         */
//
//        private BigDecimal mgp_pro;
//        private BigDecimal hmio_pro;
//
//        public BigDecimal getMgp_pro() {
//            return mgp_pro;
//        }
//
//        public void setMgp_pro(BigDecimal mgp_pro) {
//            this.mgp_pro = mgp_pro;
//        }
//
//        public BigDecimal getHmio_pro() {
//            return hmio_pro;
//        }
//
//        public void setHmio_pro(BigDecimal hmio_pro) {
//            this.hmio_pro = hmio_pro;
//        }
//    }
}
