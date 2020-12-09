package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class StoreIncomeBean {

    /**
     * code : 0
     * data : {"shop":{"name":"万达影城","userId":7906,"homeImg":"[\"https://api.coom.pub/img/1304828937741599101168935.jpg\"]","detailImg":"[\"https://api.coom.pub/img/8496167544241599101169076.jpg\",\"https://api.coom.pub/img/1095287934221599101169076.jpg\"]","bankTime":"3:44-3:11","createTime":"2020-09-03","isShow":1,"isDel":0,"status":1,"isHot":0,"country":1,"storeAddress":"哈哈哈哈","longitude":1,"latitude":1,"storePro":0.25,"buyerPro":0.33,"rewardPro":0.42,"categoryId":1,"imgs":["https://api.coom.pub/img/1304828937741599101168935.jpg"],"detailImgs":["https://api.coom.pub/img/8496167544241599101169076.jpg","https://api.coom.pub/img/1095287934221599101169076.jpg"],"lifeID":7},"top":{"proNum":1,"orderIncome":6.25,"totalMoney":9856,"orderNum":2},"orders":{"all":1,"prepare":0,"prepay":1,"goods":0,"enter":0,"refund":0},"down":{"income":0,"waitIncome":0,"refundIncome":0}}
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
         * shop : {"name":"万达影城","userId":7906,"homeImg":"[\"https://api.coom.pub/img/1304828937741599101168935.jpg\"]","detailImg":"[\"https://api.coom.pub/img/8496167544241599101169076.jpg\",\"https://api.coom.pub/img/1095287934221599101169076.jpg\"]","bankTime":"3:44-3:11","createTime":"2020-09-03","isShow":1,"isDel":0,"status":1,"isHot":0,"country":1,"storeAddress":"哈哈哈哈","longitude":1,"latitude":1,"storePro":0.25,"buyerPro":0.33,"rewardPro":0.42,"categoryId":1,"imgs":["https://api.coom.pub/img/1304828937741599101168935.jpg"],"detailImgs":["https://api.coom.pub/img/8496167544241599101169076.jpg","https://api.coom.pub/img/1095287934221599101169076.jpg"],"lifeID":7}
         * top : {"proNum":1,"orderIncome":6.25,"totalMoney":9856,"orderNum":2}
         * orders : {"all":1,"prepare":0,"prepay":1,"goods":0,"enter":0,"refund":0}
         * down : {"income":0,"waitIncome":0,"refundIncome":0}
         */

        private ShopBean shop;
        private TopBean top;
        private OrdersBean orders;
        private DownBean down;
        private AppUserBean appUser;

        public AppUserBean getAppUser() {
            return appUser;
        }

        public void setAppUser(AppUserBean appUser) {
            this.appUser = appUser;
        }

        public ShopBean getShop() {
            return shop;
        }

        public void setShop(ShopBean shop) {
            this.shop = shop;
        }

        public TopBean getTop() {
            return top;
        }

        public void setTop(TopBean top) {
            this.top = top;
        }

        public OrdersBean getOrders() {
            return orders;
        }

        public void setOrders(OrdersBean orders) {
            this.orders = orders;
        }

        public DownBean getDown() {
            return down;
        }

        public void setDown(DownBean down) {
            this.down = down;
        }

        public static class ShopBean implements Parcelable {
            /**
             * name : 万达影城
             * userId : 7906
             * homeImg : ["https://api.coom.pub/img/1304828937741599101168935.jpg"]
             * detailImg : ["https://api.coom.pub/img/8496167544241599101169076.jpg","https://api.coom.pub/img/1095287934221599101169076.jpg"]
             * bankTime : 3:44-3:11
             * createTime : 2020-09-03
             * isShow : 1
             * isDel : 0
             * status : 1
             * isHot : 0
             * country : 1
             * storeAddress : 哈哈哈哈
             * longitude : 1
             * latitude : 1
             * storePro : 0.25
             * buyerPro : 0.33
             * rewardPro : 0.42
             * categoryId : 1
             * imgs : ["https://api.coom.pub/img/1304828937741599101168935.jpg"]
             * detailImgs : ["https://api.coom.pub/img/8496167544241599101169076.jpg","https://api.coom.pub/img/1095287934221599101169076.jpg"]
             * lifeID : 7
             */

            private String name;
            private int userId;
            private String homeImg;
            private String detailImg;
            private String bankTime;
            private String createTime;
            private int isShow;
            private int isDel;
            private int status;
            private int isHot;
            private int country;
            private String storeAddress;
            private String longitude;
            private String latitude;
            private String failMsg;
            private BigDecimal storePro;
            private BigDecimal buyerPro;
            private BigDecimal rewardPro;
            private int categoryId;
            private int lifeID;
            private List<String> imgs;
            private List<String> detailImgs;

            protected ShopBean(Parcel in) {
                name = in.readString();
                userId = in.readInt();
                homeImg = in.readString();
                detailImg = in.readString();
                bankTime = in.readString();
                createTime = in.readString();
                isShow = in.readInt();
                isDel = in.readInt();
                status = in.readInt();
                isHot = in.readInt();
                country = in.readInt();
                storeAddress = in.readString();
                longitude = in.readString();
                latitude = in.readString();
                categoryId = in.readInt();
                lifeID = in.readInt();
                imgs = in.createStringArrayList();
                detailImgs = in.createStringArrayList();
                failMsg = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
                dest.writeInt(userId);
                dest.writeString(homeImg);
                dest.writeString(detailImg);
                dest.writeString(bankTime);
                dest.writeString(createTime);
                dest.writeInt(isShow);
                dest.writeInt(isDel);
                dest.writeInt(status);
                dest.writeInt(isHot);
                dest.writeInt(country);
                dest.writeString(storeAddress);
                dest.writeString(longitude);
                dest.writeString(latitude);
                dest.writeInt(categoryId);
                dest.writeInt(lifeID);
                dest.writeStringList(imgs);
                dest.writeStringList(detailImgs);
                dest.writeString(failMsg);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ShopBean> CREATOR = new Creator<ShopBean>() {
                @Override
                public ShopBean createFromParcel(Parcel in) {
                    return new ShopBean(in);
                }

                @Override
                public ShopBean[] newArray(int size) {
                    return new ShopBean[size];
                }
            };

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getHomeImg() {
                return homeImg;
            }

            public void setHomeImg(String homeImg) {
                this.homeImg = homeImg;
            }

            public String getDetailImg() {
                return detailImg;
            }

            public void setDetailImg(String detailImg) {
                this.detailImg = detailImg;
            }

            public String getBankTime() {
                return bankTime;
            }

            public void setBankTime(String bankTime) {
                this.bankTime = bankTime;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getIsShow() {
                return isShow;
            }

            public void setIsShow(int isShow) {
                this.isShow = isShow;
            }

            public int getIsDel() {
                return isDel;
            }

            public void setIsDel(int isDel) {
                this.isDel = isDel;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getIsHot() {
                return isHot;
            }

            public void setIsHot(int isHot) {
                this.isHot = isHot;
            }

            public int getCountry() {
                return country;
            }

            public void setCountry(int country) {
                this.country = country;
            }

            public String getStoreAddress() {
                return storeAddress;
            }

            public void setStoreAddress(String storeAddress) {
                this.storeAddress = storeAddress;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public BigDecimal getStorePro() {
                return storePro;
            }

            public void setStorePro(BigDecimal storePro) {
                this.storePro = storePro;
            }

            public BigDecimal getBuyerPro() {
                return buyerPro;
            }

            public void setBuyerPro(BigDecimal buyerPro) {
                this.buyerPro = buyerPro;
            }

            public BigDecimal getRewardPro() {
                return rewardPro;
            }

            public void setRewardPro(BigDecimal rewardPro) {
                this.rewardPro = rewardPro;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public int getLifeID() {
                return lifeID;
            }

            public void setLifeID(int lifeID) {
                this.lifeID = lifeID;
            }

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }

            public List<String> getDetailImgs() {
                return detailImgs;
            }

            public void setDetailImgs(List<String> detailImgs) {
                this.detailImgs = detailImgs;
            }

            public String getFailMsg() {
                return failMsg;
            }

            public void setFailMsg(String failMsg) {
                this.failMsg = failMsg;
            }
        }

        public static class TopBean {
            /**
             * proNum : 1
             * orderIncome : 6.25
             * totalMoney : 9856
             * orderNum : 2
             */

            private String proNum;
            private String orderIncome;
            private BigDecimal totalMoney;
            private String orderNum;
            private int isBindUsdt;

            public String getProNum() {
                return proNum;
            }

            public void setProNum(String proNum) {
                this.proNum = proNum;
            }

            public String getOrderIncome() {
                return orderIncome;
            }

            public void setOrderIncome(String orderIncome) {
                this.orderIncome = orderIncome;
            }

            public BigDecimal getTotalMoney() {
                return totalMoney;
            }

            public void setTotalMoney(BigDecimal totalMoney) {
                this.totalMoney = totalMoney;
            }

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public int getIsBindUsdt() {
                return isBindUsdt;
            }

            public void setIsBindUsdt(int isBindUsdt) {
                this.isBindUsdt = isBindUsdt;
            }
        }

        public static class OrdersBean {
            /**
             * all : 1
             * prepare : 0
             * prepay : 1
             * goods : 0
             * enter : 0
             * refund : 0
             */

            private String all;
            private String prepare;
            private String prepay;
            private String goods;
            private String enter;
            private String refund;

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }

            public String getPrepare() {
                return prepare;
            }

            public void setPrepare(String prepare) {
                this.prepare = prepare;
            }

            public String getPrepay() {
                return prepay;
            }

            public void setPrepay(String prepay) {
                this.prepay = prepay;
            }

            public String getGoods() {
                return goods;
            }

            public void setGoods(String goods) {
                this.goods = goods;
            }

            public String getEnter() {
                return enter;
            }

            public void setEnter(String enter) {
                this.enter = enter;
            }

            public String getRefund() {
                return refund;
            }

            public void setRefund(String refund) {
                this.refund = refund;
            }
        }

        public static class DownBean {
            /**
             * income : 0
             * waitIncome : 0
             * refundIncome : 0
             */

            private BigDecimal income;
            private BigDecimal waitIncome;
            private BigDecimal refundIncome;

            public BigDecimal getIncome() {
                return income;
            }

            public void setIncome(BigDecimal income) {
                this.income = income;
            }

            public BigDecimal getWaitIncome() {
                return waitIncome;
            }

            public void setWaitIncome(BigDecimal waitIncome) {
                this.waitIncome = waitIncome;
            }

            public BigDecimal getRefundIncome() {
                return refundIncome;
            }

            public void setRefundIncome(BigDecimal refundIncome) {
                this.refundIncome = refundIncome;
            }
        }

        public static class AppUserBean {

            /**
             * id : 1
             * userId : 7906
             * userName : 郭大侠
             * identityCard : 358556245276283953
             * socialCode : X1185685545688
             * businessLicense : [ http://172.16.11.239:8888/img/4343500242221599464945558.jpg ]
             * identityCardPhoto : [ http://172.16.11.239:8888/img/4263193053451599464945558.jpg ]
             * handCardPhoto : [ http://172.16.11.239:8888/img/2707704310841599464945674.jpg, http://172.16.11.239:8888/img/5801101240531599464945677.jpg ]
             * status : 1
             * createTime : 2020-09-07
             * phone : 13546352568
             */

            private int id;
            private int userId;
            private String userName;
            private String identityCard;
            private String socialCode;
            private String businessLicense;
            private String identityCardPhoto;
            private String handCardPhoto;
            private int status;
            private String createTime;
            private String phone;
            private String mark;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

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

            public String getIdentityCard() {
                return identityCard;
            }

            public void setIdentityCard(String identityCard) {
                this.identityCard = identityCard;
            }

            public String getSocialCode() {
                return socialCode;
            }

            public void setSocialCode(String socialCode) {
                this.socialCode = socialCode;
            }

            public String getBusinessLicense() {
                return businessLicense;
            }

            public void setBusinessLicense(String businessLicense) {
                this.businessLicense = businessLicense;
            }

            public String getIdentityCardPhoto() {
                return identityCardPhoto;
            }

            public void setIdentityCardPhoto(String identityCardPhoto) {
                this.identityCardPhoto = identityCardPhoto;
            }

            public String getHandCardPhoto() {
                return handCardPhoto;
            }

            public void setHandCardPhoto(String handCardPhoto) {
                this.handCardPhoto = handCardPhoto;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }
        }
    }
}
