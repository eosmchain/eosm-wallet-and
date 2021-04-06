package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BlendChannelBean {

    /**
     * code : 0
     * msg : success
     * data : {"channelList":[{"id":1,"name":"MGP","chainType":1,"contracts":"addressbookt","receiveAddress":"addressbookt","moneyType":1,"status":1,"createAt":"2021-03-02","updateAt":null,"moneyTypeName":"MGP"},{"id":2,"name":"HMIO + MGP","chainType":2,"contracts":"0x155697df0d39e18f719fa58e25a53a65ccb4864e","receiveAddress":"0xDA79e4a8839CB8EAF9BeCe5DB71feAA4565347B2","moneyType":4,"status":1,"createAt":"2021-03-02","updateAt":null,"moneyTypeName":"HMIO"},{"id":3,"name":"TST + MGP","chainType":2,"contracts":"0x766f824e31cd8624976d0e01ac8bf7faefd772f7","receiveAddress":"0xDA79e4a8839CB8EAF9BeCe5DB71feAA4565347B2","moneyType":5,"status":1,"createAt":"2021-03-02","updateAt":null,"moneyTypeName":"TST"},{"id":4,"name":"UBSC + MGP","chainType":2,"contracts":"0xa5b80588ee71e165e0d2f7284eac0ee4c2883aaa","receiveAddress":"0xDA79e4a8839CB8EAF9BeCe5DB71feAA4565347B2","moneyType":6,"status":1,"createAt":"2021-03-02","updateAt":null,"moneyTypeName":"USBC"}],"ratioList":[{"orderLevel":1,"orderDollarOpen":100,"orderDollarClose":499.99,"hmioRatio":"0.2","mgpRatio":"0.8"},{"orderLevel":2,"orderDollarOpen":500,"orderDollarClose":1999.99,"hmioRatio":"0.3","mgpRatio":"0.7"},{"orderLevel":3,"orderDollarOpen":2000,"orderDollarClose":4999.99,"hmioRatio":"0.4","mgpRatio":"0.6"},{"orderLevel":4,"orderDollarOpen":5000,"orderDollarClose":9.99999999999E9,"hmioRatio":"0.5","mgpRatio":"0.5"}]}
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
        private List<ChannelListBean> channelList;
        private List<RatioListBean> ratioList;

        protected DataBean(Parcel in) {
            channelList = in.createTypedArrayList(ChannelListBean.CREATOR);
            ratioList = in.createTypedArrayList(RatioListBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(channelList);
            dest.writeTypedList(ratioList);
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

        public List<ChannelListBean> getChannelList() {
            return channelList;
        }

        public void setChannelList(List<ChannelListBean> channelList) {
            this.channelList = channelList;
        }

        public List<RatioListBean> getRatioList() {
            return ratioList;
        }

        public void setRatioList(List<RatioListBean> ratioList) {
            this.ratioList = ratioList;
        }

        public static class ChannelListBean implements Parcelable {
            /**
             * id : 1
             * name : MGP
             * chainType : 1
             * contracts : addressbookt
             * receiveAddress : addressbookt
             * moneyType : 1
             * status : 1
             * createAt : 2021-03-02
             * updateAt : null
             * moneyTypeName : MGP
             */

            private int id;
            private String name;
            private int chainType;
            private String contracts;
            private String receiveAddress;
            private int moneyType;
            private int status;
            private String createAt;
            private Object updateAt;
            private String moneyTypeName;

            protected ChannelListBean(Parcel in) {
                id = in.readInt();
                name = in.readString();
                chainType = in.readInt();
                contracts = in.readString();
                receiveAddress = in.readString();
                moneyType = in.readInt();
                status = in.readInt();
                createAt = in.readString();
                moneyTypeName = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(name);
                dest.writeInt(chainType);
                dest.writeString(contracts);
                dest.writeString(receiveAddress);
                dest.writeInt(moneyType);
                dest.writeInt(status);
                dest.writeString(createAt);
                dest.writeString(moneyTypeName);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ChannelListBean> CREATOR = new Creator<ChannelListBean>() {
                @Override
                public ChannelListBean createFromParcel(Parcel in) {
                    return new ChannelListBean(in);
                }

                @Override
                public ChannelListBean[] newArray(int size) {
                    return new ChannelListBean[size];
                }
            };

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getChainType() {
                return chainType;
            }

            public void setChainType(int chainType) {
                this.chainType = chainType;
            }

            public String getContracts() {
                return contracts;
            }

            public void setContracts(String contracts) {
                this.contracts = contracts;
            }

            public String getReceiveAddress() {
                return receiveAddress;
            }

            public void setReceiveAddress(String receiveAddress) {
                this.receiveAddress = receiveAddress;
            }

            public int getMoneyType() {
                return moneyType;
            }

            public void setMoneyType(int moneyType) {
                this.moneyType = moneyType;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getCreateAt() {
                return createAt;
            }

            public void setCreateAt(String createAt) {
                this.createAt = createAt;
            }

            public Object getUpdateAt() {
                return updateAt;
            }

            public void setUpdateAt(Object updateAt) {
                this.updateAt = updateAt;
            }

            public String getMoneyTypeName() {
                return moneyTypeName;
            }

            public void setMoneyTypeName(String moneyTypeName) {
                this.moneyTypeName = moneyTypeName;
            }
        }

        public static class RatioListBean implements Parcelable{
            /**
             * orderLevel : 1
             * orderDollarOpen : 100
             * orderDollarClose : 499.99
             * hmioRatio : 0.2
             * mgpRatio : 0.8
             */

            private int orderLevel;
            private int orderDollarOpen;
            private double orderDollarClose;
            private String hmioRatio;
            private String mgpRatio;

            protected RatioListBean(Parcel in) {
                orderLevel = in.readInt();
                orderDollarOpen = in.readInt();
                orderDollarClose = in.readDouble();
                hmioRatio = in.readString();
                mgpRatio = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(orderLevel);
                dest.writeInt(orderDollarOpen);
                dest.writeDouble(orderDollarClose);
                dest.writeString(hmioRatio);
                dest.writeString(mgpRatio);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<RatioListBean> CREATOR = new Creator<RatioListBean>() {
                @Override
                public RatioListBean createFromParcel(Parcel in) {
                    return new RatioListBean(in);
                }

                @Override
                public RatioListBean[] newArray(int size) {
                    return new RatioListBean[size];
                }
            };

            public int getOrderLevel() {
                return orderLevel;
            }

            public void setOrderLevel(int orderLevel) {
                this.orderLevel = orderLevel;
            }

            public int getOrderDollarOpen() {
                return orderDollarOpen;
            }

            public void setOrderDollarOpen(int orderDollarOpen) {
                this.orderDollarOpen = orderDollarOpen;
            }

            public double getOrderDollarClose() {
                return orderDollarClose;
            }

            public void setOrderDollarClose(double orderDollarClose) {
                this.orderDollarClose = orderDollarClose;
            }

            public String getHmioRatio() {
                return hmioRatio;
            }

            public void setHmioRatio(String hmioRatio) {
                this.hmioRatio = hmioRatio;
            }

            public String getMgpRatio() {
                return mgpRatio;
            }

            public void setMgpRatio(String mgpRatio) {
                this.mgpRatio = mgpRatio;
            }
        }
    }
}
