package com.token.mangowallet.bean;

public class FindProBean {

    /**
     * code : 0
     * msg : success
     * data : {"merId":7906,"image":"[\"https://api.mgpchain.com/static/img/8450312801971598076665142.jpg\"]","image_url":["https://api.mgpchain.com/static/img/8450312801971598076665142.jpg"],"sliderImages":["https://api.mgpchain.com/static/img/2808317413921598076667986.jpg","https://api.mgpchain.com/static/img/5331998513281598076667988.jpg","https://api.mgpchain.com/static/img/9490866491701598076667990.jpg","https://api.mgpchain.com/static/img/0834173527851598076667991.jpg","https://api.mgpchain.com/static/img/2869526566621598076667992.jpg","https://api.mgpchain.com/static/img/3578675645641598076667994.jpg"],"storeName":"抖音新款快手老虎蜜蜂手提包公文包网红斜挎单肩包大购物袋电脑包","storeUnit":"个","storeInfo":"抖音新款快手老虎蜜蜂手提包公文包网红斜挎单肩包大购物袋电脑包","storeType":"中","cateId":2,"price":158,"postage":0,"sales":1,"stock":37,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.3,"buyerPro":0.5,"browse":77,"upTime":"2020-08-27","addTime":"2020-08-22","countryNum":1,"auditStatus":1,"auditTime":"2020-08-25","auditMsg":"审核成功","proID":9}
     */

    private int code;
    private String msg;
    private ProBean data;

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

    public ProBean getData() {
        return data;
    }

    public void setData(ProBean data) {
        this.data = data;
    }

//    public static class DataBean {
//        /**
//         * merId : 7906
//         * image : ["https://api.mgpchain.com/static/img/8450312801971598076665142.jpg"]
//         * image_url : ["https://api.mgpchain.com/static/img/8450312801971598076665142.jpg"]
//         * sliderImages : ["https://api.mgpchain.com/static/img/2808317413921598076667986.jpg","https://api.mgpchain.com/static/img/5331998513281598076667988.jpg","https://api.mgpchain.com/static/img/9490866491701598076667990.jpg","https://api.mgpchain.com/static/img/0834173527851598076667991.jpg","https://api.mgpchain.com/static/img/2869526566621598076667992.jpg","https://api.mgpchain.com/static/img/3578675645641598076667994.jpg"]
//         * storeName : 抖音新款快手老虎蜜蜂手提包公文包网红斜挎单肩包大购物袋电脑包
//         * storeUnit : 个
//         * storeInfo : 抖音新款快手老虎蜜蜂手提包公文包网红斜挎单肩包大购物袋电脑包
//         * storeType : 中
//         * cateId : 2
//         * price : 158
//         * postage : 0
//         * sales : 1
//         * stock : 37
//         * isShow : true
//         * isHot : false
//         * isNew : true
//         * isPostage : true
//         * isDel : false
//         * givePro : 0.2
//         * bonusPro : 0.3
//         * buyerPro : 0.5
//         * browse : 77
//         * upTime : 2020-08-27
//         * addTime : 2020-08-22
//         * countryNum : 1
//         * auditStatus : 1
//         * auditTime : 2020-08-25
//         * auditMsg : 审核成功
//         * proID : 9
//         */
//
//        private int merId;
//        private String image;
//        private String storeName;
//        private String storeUnit;
//        private String storeInfo;
//        private String storeType;
//        private int cateId;
//        private int price;
//        private int postage;
//        private int sales;
//        private int stock;
//        private boolean isShow;
//        private boolean isHot;
//        private boolean isNew;
//        private boolean isPostage;
//        private boolean isDel;
//        private double givePro;
//        private double bonusPro;
//        private double buyerPro;
//        private int browse;
//        private String upTime;
//        private String addTime;
//        private int countryNum;
//        private int auditStatus;
//        private String auditTime;
//        private String auditMsg;
//        private int proID;
//        private List<String> image_url;
//        private List<String> sliderImages;
//
//        public int getMerId() {
//            return merId;
//        }
//
//        public void setMerId(int merId) {
//            this.merId = merId;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public String getStoreName() {
//            return storeName;
//        }
//
//        public void setStoreName(String storeName) {
//            this.storeName = storeName;
//        }
//
//        public String getStoreUnit() {
//            return storeUnit;
//        }
//
//        public void setStoreUnit(String storeUnit) {
//            this.storeUnit = storeUnit;
//        }
//
//        public String getStoreInfo() {
//            return storeInfo;
//        }
//
//        public void setStoreInfo(String storeInfo) {
//            this.storeInfo = storeInfo;
//        }
//
//        public String getStoreType() {
//            return storeType;
//        }
//
//        public void setStoreType(String storeType) {
//            this.storeType = storeType;
//        }
//
//        public int getCateId() {
//            return cateId;
//        }
//
//        public void setCateId(int cateId) {
//            this.cateId = cateId;
//        }
//
//        public int getPrice() {
//            return price;
//        }
//
//        public void setPrice(int price) {
//            this.price = price;
//        }
//
//        public int getPostage() {
//            return postage;
//        }
//
//        public void setPostage(int postage) {
//            this.postage = postage;
//        }
//
//        public int getSales() {
//            return sales;
//        }
//
//        public void setSales(int sales) {
//            this.sales = sales;
//        }
//
//        public int getStock() {
//            return stock;
//        }
//
//        public void setStock(int stock) {
//            this.stock = stock;
//        }
//
//        public boolean isIsShow() {
//            return isShow;
//        }
//
//        public void setIsShow(boolean isShow) {
//            this.isShow = isShow;
//        }
//
//        public boolean isIsHot() {
//            return isHot;
//        }
//
//        public void setIsHot(boolean isHot) {
//            this.isHot = isHot;
//        }
//
//        public boolean isIsNew() {
//            return isNew;
//        }
//
//        public void setIsNew(boolean isNew) {
//            this.isNew = isNew;
//        }
//
//        public boolean isIsPostage() {
//            return isPostage;
//        }
//
//        public void setIsPostage(boolean isPostage) {
//            this.isPostage = isPostage;
//        }
//
//        public boolean isIsDel() {
//            return isDel;
//        }
//
//        public void setIsDel(boolean isDel) {
//            this.isDel = isDel;
//        }
//
//        public double getGivePro() {
//            return givePro;
//        }
//
//        public void setGivePro(double givePro) {
//            this.givePro = givePro;
//        }
//
//        public double getBonusPro() {
//            return bonusPro;
//        }
//
//        public void setBonusPro(double bonusPro) {
//            this.bonusPro = bonusPro;
//        }
//
//        public double getBuyerPro() {
//            return buyerPro;
//        }
//
//        public void setBuyerPro(double buyerPro) {
//            this.buyerPro = buyerPro;
//        }
//
//        public int getBrowse() {
//            return browse;
//        }
//
//        public void setBrowse(int browse) {
//            this.browse = browse;
//        }
//
//        public String getUpTime() {
//            return upTime;
//        }
//
//        public void setUpTime(String upTime) {
//            this.upTime = upTime;
//        }
//
//        public String getAddTime() {
//            return addTime;
//        }
//
//        public void setAddTime(String addTime) {
//            this.addTime = addTime;
//        }
//
//        public int getCountryNum() {
//            return countryNum;
//        }
//
//        public void setCountryNum(int countryNum) {
//            this.countryNum = countryNum;
//        }
//
//        public int getAuditStatus() {
//            return auditStatus;
//        }
//
//        public void setAuditStatus(int auditStatus) {
//            this.auditStatus = auditStatus;
//        }
//
//        public String getAuditTime() {
//            return auditTime;
//        }
//
//        public void setAuditTime(String auditTime) {
//            this.auditTime = auditTime;
//        }
//
//        public String getAuditMsg() {
//            return auditMsg;
//        }
//
//        public void setAuditMsg(String auditMsg) {
//            this.auditMsg = auditMsg;
//        }
//
//        public int getProID() {
//            return proID;
//        }
//
//        public void setProID(int proID) {
//            this.proID = proID;
//        }
//
//        public List<String> getImage_url() {
//            return image_url;
//        }
//
//        public void setImage_url(List<String> image_url) {
//            this.image_url = image_url;
//        }
//
//        public List<String> getSliderImages() {
//            return sliderImages;
//        }
//
//        public void setSliderImages(List<String> sliderImages) {
//            this.sliderImages = sliderImages;
//        }
//    }
}
