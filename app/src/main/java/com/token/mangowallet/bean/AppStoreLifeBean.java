package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AppStoreLifeBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":7,"name":"万达影城","img":["https://api.coom.pub/img/1304828937741599101168935.jpg"],"detailImg":["https://api.coom.pub/img/8496167544241599101169076.jpg","https://api.coom.pub/img/1095287934221599101169076.jpg"],"address":"哈哈哈哈","bankTime":"3:44-3:11","storePro":0.25,"buyerPro":0.33,"rewardPro":0.42}]
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
         * id : 7
         * name : 万达影城
         * img : ["https://api.coom.pub/img/1304828937741599101168935.jpg"]
         * detailImg : ["https://api.coom.pub/img/8496167544241599101169076.jpg","https://api.coom.pub/img/1095287934221599101169076.jpg"]
         * address : 哈哈哈哈
         * bankTime : 3:44-3:11
         * storePro : 0.25
         * buyerPro : 0.33
         * rewardPro : 0.42
         */

        private int id;
        private String name;
        private String address;
        private String bankTime;
        private double storePro;
        private double buyerPro;
        private double rewardPro;
        private List<String> img;
        private List<String> detailImg;

        protected DataBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            address = in.readString();
            bankTime = in.readString();
            storePro = in.readDouble();
            buyerPro = in.readDouble();
            rewardPro = in.readDouble();
            img = in.createStringArrayList();
            detailImg = in.createStringArrayList();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(address);
            dest.writeString(bankTime);
            dest.writeDouble(storePro);
            dest.writeDouble(buyerPro);
            dest.writeDouble(rewardPro);
            dest.writeStringList(img);
            dest.writeStringList(detailImg);
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBankTime() {
            return bankTime;
        }

        public void setBankTime(String bankTime) {
            this.bankTime = bankTime;
        }

        public double getStorePro() {
            return storePro;
        }

        public void setStorePro(double storePro) {
            this.storePro = storePro;
        }

        public double getBuyerPro() {
            return buyerPro;
        }

        public void setBuyerPro(double buyerPro) {
            this.buyerPro = buyerPro;
        }

        public double getRewardPro() {
            return rewardPro;
        }

        public void setRewardPro(double rewardPro) {
            this.rewardPro = rewardPro;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }

        public List<String> getDetailImg() {
            return detailImg;
        }

        public void setDetailImg(List<String> detailImg) {
            this.detailImg = detailImg;
        }
    }
}
