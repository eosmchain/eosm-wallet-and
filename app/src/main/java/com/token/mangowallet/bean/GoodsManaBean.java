package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GoodsManaBean {

    /**
     * code : 0
     * data : [{"merId":7908,"image":"[\"https://api.coom.pub/img/6376613167481598350641276.jpg\"]","image_url":["https://api.coom.pub/img/6376613167481598350641276.jpg"],"sliderImages":["https://api.coom.pub/img/8120335895701598350641336.jpg"],"storeName":"看","storeUnit":"了","storeInfo":"回来啦","storeType":"里","cateId":1,"price":4,"postage":4,"sales":0,"stock":2,"isShow":false,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0,"bonusPro":0.5,"buyerPro":0.5,"browse":0,"addTime":"2020-08-25","auditStatus":0,"proID":21},{"merId":7908,"image":"[\"https://api.coom.pub/img/2409724246721598350559656.jpg\"]","image_url":["https://api.coom.pub/img/2409724246721598350559656.jpg"],"sliderImages":["https://api.coom.pub/img/4376443873951598350559754.jpg"],"storeName":"1个","storeUnit":"来来来","storeInfo":"不露声色","storeType":"起来","cateId":1,"price":4,"postage":1,"sales":0,"stock":1,"isShow":false,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.4,"bonusPro":0.1,"buyerPro":0.5,"browse":0,"addTime":"2020-08-25","auditStatus":0,"proID":20}]
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
         * merId : 7908
         * image : ["https://api.coom.pub/img/6376613167481598350641276.jpg"]
         * image_url : ["https://api.coom.pub/img/6376613167481598350641276.jpg"]
         * sliderImages : ["https://api.coom.pub/img/8120335895701598350641336.jpg"]
         * storeName : 看
         * storeUnit : 了
         * storeInfo : 回来啦
         * storeType : 里
         * cateId : 1
         * price : 4
         * postage : 4
         * sales : 0
         * stock : 2
         * isShow : false
         * isHot : false
         * isNew : true
         * isPostage : true
         * isDel : false
         * givePro : 0
         * bonusPro : 0.5
         * buyerPro : 0.5
         * browse : 0
         * addTime : 2020-08-25
         * auditStatus : 0
         * proID : 21
         */

        private int merId;
        private String image;
        private String storeName;
        private String storeUnit;
        private String storeInfo;
        private String storeType;
        private int cateId;
        private String price;
        private String postage;
        private int sales;
        private int stock;
        private boolean isShow;
        private boolean isHot;
        private boolean isNew;
        private boolean isPostage;
        private boolean isDel;
        private String givePro;
        private String bonusPro;
        private String buyerPro;
        private int browse;
        private String addTime;
        private int auditStatus;
        private int proID;
        private List<String> image_url;
        private List<String> sliderImages;

        protected DataBean(Parcel in) {
            merId = in.readInt();
            image = in.readString();
            storeName = in.readString();
            storeUnit = in.readString();
            storeInfo = in.readString();
            storeType = in.readString();
            cateId = in.readInt();
            price = in.readString();
            postage = in.readString();
            sales = in.readInt();
            stock = in.readInt();
            isShow = in.readByte() != 0;
            isHot = in.readByte() != 0;
            isNew = in.readByte() != 0;
            isPostage = in.readByte() != 0;
            isDel = in.readByte() != 0;
            givePro = in.readString();
            bonusPro = in.readString();
            buyerPro = in.readString();
            browse = in.readInt();
            addTime = in.readString();
            auditStatus = in.readInt();
            proID = in.readInt();
            image_url = in.createStringArrayList();
            sliderImages = in.createStringArrayList();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(merId);
            dest.writeString(image);
            dest.writeString(storeName);
            dest.writeString(storeUnit);
            dest.writeString(storeInfo);
            dest.writeString(storeType);
            dest.writeInt(cateId);
            dest.writeString(price);
            dest.writeString(postage);
            dest.writeInt(sales);
            dest.writeInt(stock);
            dest.writeByte((byte) (isShow ? 1 : 0));
            dest.writeByte((byte) (isHot ? 1 : 0));
            dest.writeByte((byte) (isNew ? 1 : 0));
            dest.writeByte((byte) (isPostage ? 1 : 0));
            dest.writeByte((byte) (isDel ? 1 : 0));
            dest.writeString(givePro);
            dest.writeString(bonusPro);
            dest.writeString(buyerPro);
            dest.writeInt(browse);
            dest.writeString(addTime);
            dest.writeInt(auditStatus);
            dest.writeInt(proID);
            dest.writeStringList(image_url);
            dest.writeStringList(sliderImages);
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

        public int getMerId() {
            return merId;
        }

        public void setMerId(int merId) {
            this.merId = merId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreUnit() {
            return storeUnit;
        }

        public void setStoreUnit(String storeUnit) {
            this.storeUnit = storeUnit;
        }

        public String getStoreInfo() {
            return storeInfo;
        }

        public void setStoreInfo(String storeInfo) {
            this.storeInfo = storeInfo;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public int getCateId() {
            return cateId;
        }

        public void setCateId(int cateId) {
            this.cateId = cateId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPostage() {
            return postage;
        }

        public void setPostage(String postage) {
            this.postage = postage;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public boolean isIsShow() {
            return isShow;
        }

        public void setIsShow(boolean isShow) {
            this.isShow = isShow;
        }

        public boolean isIsHot() {
            return isHot;
        }

        public void setIsHot(boolean isHot) {
            this.isHot = isHot;
        }

        public boolean isIsNew() {
            return isNew;
        }

        public void setIsNew(boolean isNew) {
            this.isNew = isNew;
        }

        public boolean isIsPostage() {
            return isPostage;
        }

        public void setIsPostage(boolean isPostage) {
            this.isPostage = isPostage;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }

        public String getGivePro() {
            return givePro;
        }

        public void setGivePro(String givePro) {
            this.givePro = givePro;
        }

        public String getBonusPro() {
            return bonusPro;
        }

        public void setBonusPro(String bonusPro) {
            this.bonusPro = bonusPro;
        }

        public String getBuyerPro() {
            return buyerPro;
        }

        public void setBuyerPro(String buyerPro) {
            this.buyerPro = buyerPro;
        }

        public int getBrowse() {
            return browse;
        }

        public void setBrowse(int browse) {
            this.browse = browse;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public int getProID() {
            return proID;
        }

        public void setProID(int proID) {
            this.proID = proID;
        }

        public List<String> getImage_url() {
            return image_url;
        }

        public void setImage_url(List<String> image_url) {
            this.image_url = image_url;
        }

        public List<String> getSliderImages() {
            return sliderImages;
        }

        public void setSliderImages(List<String> sliderImages) {
            this.sliderImages = sliderImages;
        }
    }
}
