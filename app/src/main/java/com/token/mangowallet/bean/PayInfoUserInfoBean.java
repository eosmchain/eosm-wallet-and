package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayInfoUserInfoBean {

    /**
     * code : 0
     * msg : success
     * data : {"userInfo":{"weixin":"Were64646","mail":"guoqihai@126.com11","phone":"13543258322","mgpName":"mgptest11111","updateAt":"2021-01-07 08:45:18","id":15,"createAt":"2021-01-07 08:45:18"},"payInfos":[{"updateAt":"2021-01-12 09:13:13","del":false,"userId":15,"branch":null,"createAt":"2021-01-12 09:13:13","cardNum":"Zhifubao","isDefault":false,"default":false,"qrCode":"https://otcstore.mgps.me/mgp/images/20210112/31BECBD08222455BBAB418B653B26DAF20210112171305.jpg","payInfoId":15,"name":"支付宝","payId":3,"isDel":false,"username":"郭大侠"},{"updateAt":"2021-01-07 09:24:20","del":false,"userId":15,"branch":"南山支行","createAt":"2021-01-07 09:24:20","cardNum":"6523 5464 5724 6457 643","isDefault":false,"default":false,"qrCode":null,"payInfoId":9,"name":"中国银行","payId":1,"isDel":false,"username":"郭大侠"}]}
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
         * userInfo : {"weixin":"Were64646","mail":"guoqihai@126.com11","phone":"13543258322","mgpName":"mgptest11111","updateAt":"2021-01-07 08:45:18","id":15,"createAt":"2021-01-07 08:45:18"}
         * payInfos : [{"updateAt":"2021-01-12 09:13:13","del":false,"userId":15,"branch":null,"createAt":"2021-01-12 09:13:13","cardNum":"Zhifubao","isDefault":false,"default":false,"qrCode":"https://otcstore.mgps.me/mgp/images/20210112/31BECBD08222455BBAB418B653B26DAF20210112171305.jpg","payInfoId":15,"name":"支付宝","payId":3,"isDel":false,"username":"郭大侠"},{"updateAt":"2021-01-07 09:24:20","del":false,"userId":15,"branch":"南山支行","createAt":"2021-01-07 09:24:20","cardNum":"6523 5464 5724 6457 643","isDefault":false,"default":false,"qrCode":null,"payInfoId":9,"name":"中国银行","payId":1,"isDel":false,"username":"郭大侠"}]
         */

        private UserInfoBean userInfo;
        private List<PayInfosBean> payInfos;

        protected DataBean(Parcel in) {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
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

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public List<PayInfosBean> getPayInfos() {
            return payInfos;
        }

        public void setPayInfos(List<PayInfosBean> payInfos) {
            this.payInfos = payInfos;
        }

        public static class UserInfoBean implements Parcelable{
            /**
             * weixin : Were64646
             * mail : guoqihai@126.com11
             * phone : 13543258322
             * mgpName : mgptest11111
             * updateAt : 2021-01-07 08:45:18
             * id : 15
             * createAt : 2021-01-07 08:45:18
             */

            private String weixin;
            private String mail;
            private String phone;
            private String mgpName;
            private String updateAt;
            private int id;
            private String createAt;

            protected UserInfoBean(Parcel in) {
                weixin = in.readString();
                mail = in.readString();
                phone = in.readString();
                mgpName = in.readString();
                updateAt = in.readString();
                id = in.readInt();
                createAt = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(weixin);
                dest.writeString(mail);
                dest.writeString(phone);
                dest.writeString(mgpName);
                dest.writeString(updateAt);
                dest.writeInt(id);
                dest.writeString(createAt);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
                @Override
                public UserInfoBean createFromParcel(Parcel in) {
                    return new UserInfoBean(in);
                }

                @Override
                public UserInfoBean[] newArray(int size) {
                    return new UserInfoBean[size];
                }
            };

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getMail() {
                return mail;
            }

            public void setMail(String mail) {
                this.mail = mail;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getMgpName() {
                return mgpName;
            }

            public void setMgpName(String mgpName) {
                this.mgpName = mgpName;
            }

            public String getUpdateAt() {
                return updateAt;
            }

            public void setUpdateAt(String updateAt) {
                this.updateAt = updateAt;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreateAt() {
                return createAt;
            }

            public void setCreateAt(String createAt) {
                this.createAt = createAt;
            }
        }

        public static class PayInfosBean implements Parcelable{
            /**
             * updateAt : 2021-01-12 09:13:13
             * del : false
             * userId : 15
             * branch : null
             * createAt : 2021-01-12 09:13:13
             * cardNum : Zhifubao
             * isDefault : false
             * default : false
             * qrCode : https://otcstore.mgps.me/mgp/images/20210112/31BECBD08222455BBAB418B653B26DAF20210112171305.jpg
             * payInfoId : 15
             * name : 支付宝
             * payId : 3
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

            protected PayInfosBean(Parcel in) {
                updateAt = in.readString();
                del = in.readByte() != 0;
                userId = in.readInt();
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

            public static final Creator<PayInfosBean> CREATOR = new Creator<PayInfosBean>() {
                @Override
                public PayInfosBean createFromParcel(Parcel in) {
                    return new PayInfosBean(in);
                }

                @Override
                public PayInfosBean[] newArray(int size) {
                    return new PayInfosBean[size];
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
}
