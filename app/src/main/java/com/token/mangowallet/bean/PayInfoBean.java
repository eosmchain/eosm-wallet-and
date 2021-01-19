package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayInfoBean {

    /**
     * code : 0
     * msg : success
     * data : [{"updateAt":"2021-01-07 09:24:20","del":false,"userId":15,"branch":"南山支行","createAt":"2021-01-07 09:24:20","cardNum":"6523 5464 5724 6457 643","isDefault":false,"default":false,"qrCode":null,"payInfoId":9,"name":"中国银行","payId":1,"isDel":false,"username":"郭大侠"},{"updateAt":"2021-01-07 09:21:29","del":false,"userId":15,"branch":null,"createAt":"2021-01-07 17:21:28","cardNum":"Weichat64646","isDefault":null,"default":null,"qrCode":"https://api.mgpchain.com/static/img/3848305195961610011347011.jpg","payInfoId":8,"name":"微信支付","payId":2,"isDel":false,"username":"空领"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * updateAt : 2021-01-07 09:24:20
         * del : false
         * userId : 15
         * branch : 南山支行
         * createAt : 2021-01-07 09:24:20
         * cardNum : 6523 5464 5724 6457 643
         * isDefault : false
         * default : false
         * qrCode : null
         * payInfoId : 9
         * name : 中国银行
         * payId : 1
         * isDel : false
         * username : 郭大侠
         */

        private String updateAt;
        private boolean del;
        private int userId;
        private String branch;
        private String createAt;
        private String cardNum;
        private boolean isDefault;
        @SerializedName("default")
        private boolean defaultX;
        private String qrCode;
        private int payInfoId;
        private String name;
        private int payId;
        private boolean isDel;
        private String username;

        protected DataBean(Parcel in) {
            updateAt = in.readString();
            del = in.readByte() != 0;
            userId = in.readInt();
            branch = in.readString();
            createAt = in.readString();
            cardNum = in.readString();
            isDefault = in.readByte() != 0;
            defaultX = in.readByte() != 0;
            qrCode = in.readString();
            payInfoId = in.readInt();
            name = in.readString();
            payId = in.readInt();
            isDel = in.readByte() != 0;
            username = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(updateAt);
            dest.writeByte((byte) (del ? 1 : 0));
            dest.writeInt(userId);
            dest.writeString(branch);
            dest.writeString(createAt);
            dest.writeString(cardNum);
            dest.writeByte((byte) (isDefault ? 1 : 0));
            dest.writeByte((byte) (defaultX ? 1 : 0));
            dest.writeString(qrCode);
            dest.writeInt(payInfoId);
            dest.writeString(name);
            dest.writeInt(payId);
            dest.writeByte((byte) (isDel ? 1 : 0));
            dest.writeString(username);
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

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public boolean isDefaultX() {
            return defaultX;
        }

        public void setDefaultX(boolean defaultX) {
            this.defaultX = defaultX;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public int getPayInfoId() {
            return payInfoId;
        }

        public void setPayInfoId(int payInfoId) {
            this.payInfoId = payInfoId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPayId() {
            return payId;
        }

        public void setPayId(int payId) {
            this.payId = payId;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
