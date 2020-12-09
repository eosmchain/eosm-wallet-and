package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ShippingAddressBean implements Parcelable {

    /**
     * code : 0
     * data : [{"userId":7908,"userName":"LOL","phone":"12345","country":4,"detailedAddress":"您Rom","isDefault":true,"isDel":false,"createTime":"2020-08-25","countryName":"马来西亚","addrID":39},{"userId":7908,"userName":"郭大侠","phone":"13546352586","country":1,"city":"天津市静海县","detailedAddress":"流塘市场22号","isDefault":false,"isDel":false,"createTime":"2020-08-21","countryName":"中国","addrID":24},{"userId":7908,"userName":"郭大侠","phone":"13546352586","country":1,"city":"天津市静海县","detailedAddress":"流塘市场22号","isDefault":false,"isDel":false,"createTime":"2020-08-21","countryName":"中国","addrID":25}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    protected ShippingAddressBean(Parcel in) {
        code = in.readInt();
        msg = in.readString();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(msg);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingAddressBean> CREATOR = new Creator<ShippingAddressBean>() {
        @Override
        public ShippingAddressBean createFromParcel(Parcel in) {
            return new ShippingAddressBean(in);
        }

        @Override
        public ShippingAddressBean[] newArray(int size) {
            return new ShippingAddressBean[size];
        }
    };

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
         * userId : 7908
         * userName : LOL
         * phone : 12345
         * country : 4
         * detailedAddress : 您Rom
         * isDefault : true
         * isDel : false
         * createTime : 2020-08-25
         * countryName : 马来西亚
         * addrID : 39
         * city : 天津市静海县
         */

        private int userId;
        private String userName;
        private String phone;
        private int country;
        private String detailedAddress;
        private boolean isDefault;
        private boolean isDel;
        private String createTime;
        private String countryName;
        private int addrID;
        private String city;

        protected DataBean(Parcel in) {
            userId = in.readInt();
            userName = in.readString();
            phone = in.readString();
            country = in.readInt();
            detailedAddress = in.readString();
            isDefault = in.readByte() != 0;
            isDel = in.readByte() != 0;
            createTime = in.readString();
            countryName = in.readString();
            addrID = in.readInt();
            city = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(userId);
            dest.writeString(userName);
            dest.writeString(phone);
            dest.writeInt(country);
            dest.writeString(detailedAddress);
            dest.writeByte((byte) (isDefault ? 1 : 0));
            dest.writeByte((byte) (isDel ? 1 : 0));
            dest.writeString(createTime);
            dest.writeString(countryName);
            dest.writeInt(addrID);
            dest.writeString(city);
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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getCountry() {
            return country;
        }

        public void setCountry(int country) {
            this.country = country;
        }

        public String getDetailedAddress() {
            return detailedAddress;
        }

        public void setDetailedAddress(String detailedAddress) {
            this.detailedAddress = detailedAddress;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public int getAddrID() {
            return addrID;
        }

        public void setAddrID(int addrID) {
            this.addrID = addrID;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
