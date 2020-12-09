package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class ProBean implements Parcelable {
    /**
     * merId : 7908
     * image : ["https://api.coom.pub/img/6317738730161598670271804.jpg"]
     * image_url : ["https://api.coom.pub/img/6317738730161598670271804.jpg"]
     * sliderImages : ["https://api.coom.pub/img/9639475086311598670273246.jpg","https://api.coom.pub/img/3429599893431598670273246.jpg"]
     * storeName : 50
     * storeUnit : 1
     * storeInfo : 123456
     * storeType : 1
     * cateId : 1
     * price : 5
     * postage : 0
     * sales : 0
     * stock : 5
     * isShow : true
     * isHot : false
     * isNew : true
     * isPostage : true
     * isDel : false
     * givePro : 0.2
     * bonusPro : 0.3
     * buyerPro : 0.5
     * browse : 0
     * addTime : 2020-08-29
     * address : 111
     * cateName : ele.name
     * countryNum : 1
     * countryName : 中国
     * auditStatus : 1
     * proID : 17
     * usdtAddr : 0xf4128a3E1da533C84A26a3Af5286c0ccF65EffD6
     * payConfigs : [{"name":"MGP","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/4059368566361600828643744.png","payId":1},{"name":"USDT","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/4388383598131600828669488.png","payId":2}]
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
    private String address;
    private String cateName;
    private int countryNum;
    private String countryName;
    private int auditStatus;
    private int proID;
    private String usdtAddr;
    private List<String> image_url;
    private List<String> sliderImages;
    private List<PayConfigsBean> payConfigs;
    private String serviceCharge;

    protected ProBean(Parcel in) {
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
        address = in.readString();
        cateName = in.readString();
        countryNum = in.readInt();
        countryName = in.readString();
        auditStatus = in.readInt();
        proID = in.readInt();
        usdtAddr = in.readString();
        image_url = in.createStringArrayList();
        sliderImages = in.createStringArrayList();
        payConfigs = in.createTypedArrayList(PayConfigsBean.CREATOR);
        serviceCharge = in.readString();
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
        dest.writeString(address);
        dest.writeString(cateName);
        dest.writeInt(countryNum);
        dest.writeString(countryName);
        dest.writeInt(auditStatus);
        dest.writeInt(proID);
        dest.writeStringList(image_url);
        dest.writeStringList(sliderImages);
        dest.writeTypedList(payConfigs);
        dest.writeString(usdtAddr);
        dest.writeString(serviceCharge);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProBean> CREATOR = new Creator<ProBean>() {
        @Override
        public ProBean createFromParcel(Parcel in) {
            return new ProBean(in);
        }

        @Override
        public ProBean[] newArray(int size) {
            return new ProBean[size];
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public int getCountryNum() {
        return countryNum;
    }

    public void setCountryNum(int countryNum) {
        this.countryNum = countryNum;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public String getUsdtAddr() {
        return usdtAddr;
    }

    public void setUsdtAddr(String usdtAddr) {
        this.usdtAddr = usdtAddr;
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

    public List<PayConfigsBean> getPayConfigs() {
        return payConfigs;
    }

    public void setPayConfigs(List<PayConfigsBean> payConfigs) {
        this.payConfigs = payConfigs;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public static class PayConfigsBean implements Parcelable {
        /**
         * name : MGP
         * isDel : false
         * createTime : 2020-09-22
         * pic : https://api.mgpchain.com/static/img/4059368566361600828643744.png
         * payId : 1
         */

        private String name;
        private boolean isDel;
        private String createTime;
        private String pic;
        private int payId;

        protected PayConfigsBean(Parcel in) {
            name = in.readString();
            isDel = in.readByte() != 0;
            createTime = in.readString();
            pic = in.readString();
            payId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeByte((byte) (isDel ? 1 : 0));
            dest.writeString(createTime);
            dest.writeString(pic);
            dest.writeInt(payId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PayConfigsBean> CREATOR = new Creator<PayConfigsBean>() {
            @Override
            public PayConfigsBean createFromParcel(Parcel in) {
                return new PayConfigsBean(in);
            }

            @Override
            public PayConfigsBean[] newArray(int size) {
                return new PayConfigsBean[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getPayId() {
            return payId;
        }

        public void setPayId(int payId) {
            this.payId = payId;
        }
    }
}
