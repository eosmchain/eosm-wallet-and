package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderGoodsBean {


    /**
     * code : 0
     * data : [{"pro":{"merId":7907,"image":"[\"https://api.coom.pub/img/1597138234022.jpg\"]","image_url":["https://api.coom.pub/img/1597138234022.jpg"],"storeName":"电饭锅","storeUnit":"台","storeInfo":"电饭锅简介","storeType":"红色","cateId":2,"price":66,"postage":4,"sales":51,"stock":960,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.3,"buyerPro":0.5,"browse":92,"addTime":"2020-08-11","msg":"商品已售完系统自动下架","auditStatus":1,"auditTime":"2020-08-12","auditMsg":"审核成功","proID":1},"order":{"id":104,"orderId":"MGP1598008711595103936430037","userId":7908,"cartId":0,"productId":1,"totalNum":1,"productPrice":66,"mgpPrice":0.4118,"payMgp":169.9854,"payMoney":70,"totalPostage":4,"payStatus":0,"isDel":false,"mark":"已支付","isDeliver":1,"createTime":"2020-08-21","payTime":"2020-08-21","upTime":"2020-08-21","refund":1,"hash":"db268dc25ae5812289de92bf58720a58719aa06f290e0824aa0db988c3a91dac","appStoreUserDelivery":{"id":104,"city":"天津市静海县","phone":"13546352586","detailedAddress":"流塘市场22号","num":"yyy","userId":7908,"userName":"郭大侠","orderId":104,"status":1,"createTime":"2020-08-21","mark":"发货中","isDel":false},"merId":7907},"username":"aaaaaab12345"},{"pro":{"merId":7907,"image":"[\"https://api.coom.pub/img/1597138234022.jpg\"]","image_url":["https://api.coom.pub/img/1597138234022.jpg"],"storeName":"电饭锅","storeUnit":"台","storeInfo":"电饭锅简介","storeType":"红色","cateId":2,"price":66,"postage":4,"sales":51,"stock":960,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.3,"buyerPro":0.5,"browse":92,"addTime":"2020-08-11","msg":"商品已售完系统自动下架","auditStatus":1,"auditTime":"2020-08-12","auditMsg":"审核成功","proID":1},"order":{"id":103,"orderId":"MGP1598008161661528937547308","userId":7908,"cartId":0,"productId":1,"totalNum":1,"productPrice":66,"mgpPrice":0.4144,"payMgp":168.9189,"payMoney":70,"totalPostage":4,"payStatus":0,"isDel":false,"mark":"已收货","isDeliver":2,"createTime":"2020-08-21","payTime":"2020-08-21","upTime":"2020-08-21","refund":1,"hash":"9a26d9c4c3e87b316f9387ab383f14b531c824fb3e1a9e23a27bfdfbc3bf19d6","appStoreUserDelivery":{"id":103,"city":"天津市静海县","phone":"13546352586","detailedAddress":"流塘市场22号","num":"基督教","userId":7908,"userName":"郭大侠","orderId":103,"status":2,"createTime":"2020-08-21","mark":"已收货","isDel":false},"merId":7907},"username":"aaaaaab12345"},{"pro":{"merId":7907,"image":"[\"https://api.coom.pub/img/1597138234022.jpg\"]","image_url":["https://api.coom.pub/img/1597138234022.jpg"],"storeName":"电饭锅","storeUnit":"台","storeInfo":"电饭锅简介","storeType":"红色","cateId":2,"price":66,"postage":4,"sales":51,"stock":960,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.3,"buyerPro":0.5,"browse":92,"addTime":"2020-08-11","msg":"商品已售完系统自动下架","auditStatus":1,"auditTime":"2020-08-12","auditMsg":"审核成功","proID":1},"order":{"id":97,"orderId":"MGP1598006658376033651603212","userId":7908,"cartId":0,"productId":1,"totalNum":1,"productPrice":66,"mgpPrice":0.4182,"payMgp":161.8192,"payMoney":70,"totalPostage":4,"payStatus":1,"isDel":false,"mark":"入账中","isDeliver":3,"createTime":"2020-08-21","upTime":"2020-08-21","refund":1,"hash":"1ff90d3bc07db673203bc34f9ea4da09019ec6463e7bedfba61945ea1c01a854","appStoreUserDelivery":{"id":97,"city":"天津市静海县","phone":"13546352586","detailedAddress":"流塘市场22号","userId":7908,"userName":"郭大侠","orderId":97,"status":3,"createTime":"2020-08-21","mark":"待支付","isDel":false}},"username":"aaaaaab12345"}]
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
         * pro : {"merId":7907,"image":"[\"https://api.coom.pub/img/1597138234022.jpg\"]","image_url":["https://api.coom.pub/img/1597138234022.jpg"],"storeName":"电饭锅","storeUnit":"台","storeInfo":"电饭锅简介","storeType":"红色","cateId":2,"price":66,"postage":4,"sales":51,"stock":960,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.3,"buyerPro":0.5,"browse":92,"addTime":"2020-08-11","msg":"商品已售完系统自动下架","auditStatus":1,"auditTime":"2020-08-12","auditMsg":"审核成功","proID":1}
         * order : {"id":104,"orderId":"MGP1598008711595103936430037","userId":7908,"cartId":0,"productId":1,"totalNum":1,"productPrice":66,"mgpPrice":0.4118,"payMgp":169.9854,"payMoney":70,"totalPostage":4,"payStatus":0,"isDel":false,"mark":"已支付","isDeliver":1,"createTime":"2020-08-21","payTime":"2020-08-21","upTime":"2020-08-21","refund":1,"hash":"db268dc25ae5812289de92bf58720a58719aa06f290e0824aa0db988c3a91dac","appStoreUserDelivery":{"id":104,"city":"天津市静海县","phone":"13546352586","detailedAddress":"流塘市场22号","num":"yyy","userId":7908,"userName":"郭大侠","orderId":104,"status":1,"createTime":"2020-08-21","mark":"发货中","isDel":false},"merId":7907}
         * username : aaaaaab12345
         */

        private ProBean pro;
        private OrderBean order;
        private String username;

        protected DataBean(Parcel in) {
            pro = in.readParcelable(ProBean.class.getClassLoader());
            order = in.readParcelable(OrderBean.class.getClassLoader());
            username = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(pro, flags);
            dest.writeParcelable(order, flags);
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

        public ProBean getPro() {
            return pro;
        }

        public void setPro(ProBean pro) {
            this.pro = pro;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public static class ProBean implements Parcelable {
            /**
             * merId : 7907
             * image : ["https://api.coom.pub/img/1597138234022.jpg"]
             * image_url : ["https://api.coom.pub/img/1597138234022.jpg"]
             * storeName : 电饭锅
             * storeUnit : 台
             * storeInfo : 电饭锅简介
             * storeType : 红色
             * cateId : 2
             * price : 66
             * postage : 4
             * sales : 51
             * stock : 960
             * isShow : true
             * isHot : false
             * isNew : true
             * isPostage : true
             * isDel : false
             * givePro : 0.2
             * bonusPro : 0.3
             * buyerPro : 0.5
             * browse : 92
             * addTime : 2020-08-11
             * msg : 商品已售完系统自动下架
             * auditStatus : 1
             * auditTime : 2020-08-12
             * auditMsg : 审核成功
             * proID : 1
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
            private String msg;
            private int auditStatus;
            private String auditTime;
            private String auditMsg;
            private int proID;
            private List<String> image_url;

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
                msg = in.readString();
                auditStatus = in.readInt();
                auditTime = in.readString();
                auditMsg = in.readString();
                proID = in.readInt();
                image_url = in.createStringArrayList();
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
                dest.writeString(msg);
                dest.writeInt(auditStatus);
                dest.writeString(auditTime);
                dest.writeString(auditMsg);
                dest.writeInt(proID);
                dest.writeStringList(image_url);
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

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public int getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(int auditStatus) {
                this.auditStatus = auditStatus;
            }

            public String getAuditTime() {
                return auditTime;
            }

            public void setAuditTime(String auditTime) {
                this.auditTime = auditTime;
            }

            public String getAuditMsg() {
                return auditMsg;
            }

            public void setAuditMsg(String auditMsg) {
                this.auditMsg = auditMsg;
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
        }

        public static class OrderBean implements Parcelable {
            /**
             * id : 104
             * orderId : MGP1598008711595103936430037
             * userId : 7908
             * cartId : 0
             * productId : 1
             * totalNum : 1
             * productPrice : 66
             * mgpPrice : 0.4118
             * payMgp : 169.9854
             * payMoney : 70
             * totalPostage : 4
             * payStatus : 0
             * isDel : false
             * mark : 已支付
             * isDeliver : 1
             * createTime : 2020-08-21
             * payTime : 2020-08-21
             * upTime : 2020-08-21
             * refund : 1
             * hash : db268dc25ae5812289de92bf58720a58719aa06f290e0824aa0db988c3a91dac
             * appStoreUserDelivery : {"id":104,"city":"天津市静海县","phone":"13546352586","detailedAddress":"流塘市场22号","num":"yyy","userId":7908,"userName":"郭大侠","orderId":104,"status":1,"createTime":"2020-08-21","mark":"发货中","isDel":false}
             * merId : 7907
             */

            private int id;
            private String orderId;
            private int userId;
            private int cartId;
            private int productId;
            private String totalNum;
            private String productPrice;
            private String mgpPrice;
            private String payMgp;
            private String payMoney;
            private String totalPostage;
            private int payStatus;
            private boolean isDel;
            private String mark;
            private int isDeliver;
            private String createTime;
            private String payTime;
            private String upTime;
            private int refund;
            private String hash;
            private String num;//快递单号
            private AppStoreUserDeliveryBean appStoreUserDelivery;
            private int merId;
            private String company;
            private String buyMark;
            private int pay;
            private String usdtAddr;

            protected OrderBean(Parcel in) {
                id = in.readInt();
                orderId = in.readString();
                userId = in.readInt();
                cartId = in.readInt();
                productId = in.readInt();
                totalNum = in.readString();
                productPrice = in.readString();
                mgpPrice = in.readString();
                payMgp = in.readString();
                payMoney = in.readString();
                totalPostage = in.readString();
                payStatus = in.readInt();
                isDel = in.readByte() != 0;
                mark = in.readString();
                isDeliver = in.readInt();
                createTime = in.readString();
                payTime = in.readString();
                upTime = in.readString();
                refund = in.readInt();
                hash = in.readString();
                num = in.readString();
                appStoreUserDelivery = in.readParcelable(AppStoreUserDeliveryBean.class.getClassLoader());
                merId = in.readInt();
                company = in.readString();
                buyMark = in.readString();
                pay = in.readInt();
                usdtAddr = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(orderId);
                dest.writeInt(userId);
                dest.writeInt(cartId);
                dest.writeInt(productId);
                dest.writeString(totalNum);
                dest.writeString(productPrice);
                dest.writeString(mgpPrice);
                dest.writeString(payMgp);
                dest.writeString(payMoney);
                dest.writeString(totalPostage);
                dest.writeInt(payStatus);
                dest.writeByte((byte) (isDel ? 1 : 0));
                dest.writeString(mark);
                dest.writeInt(isDeliver);
                dest.writeString(createTime);
                dest.writeString(payTime);
                dest.writeString(upTime);
                dest.writeInt(refund);
                dest.writeString(hash);
                dest.writeString(num);
                dest.writeParcelable(appStoreUserDelivery, flags);
                dest.writeInt(merId);
                dest.writeString(company);
                dest.writeString(buyMark);
                dest.writeInt(pay);
                dest.writeString(usdtAddr);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
                @Override
                public OrderBean createFromParcel(Parcel in) {
                    return new OrderBean(in);
                }

                @Override
                public OrderBean[] newArray(int size) {
                    return new OrderBean[size];
                }
            };

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getCartId() {
                return cartId;
            }

            public void setCartId(int cartId) {
                this.cartId = cartId;
            }

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getTotalNum() {
                return totalNum;
            }

            public void setTotalNum(String totalNum) {
                this.totalNum = totalNum;
            }

            public String getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(String productPrice) {
                this.productPrice = productPrice;
            }

            public String getMgpPrice() {
                return mgpPrice;
            }

            public void setMgpPrice(String mgpPrice) {
                this.mgpPrice = mgpPrice;
            }

            public String getPayMgp() {
                return payMgp;
            }

            public void setPayMgp(String payMgp) {
                this.payMgp = payMgp;
            }

            public String getPayMoney() {
                return payMoney;
            }

            public void setPayMoney(String payMoney) {
                this.payMoney = payMoney;
            }

            public String getTotalPostage() {
                return totalPostage;
            }

            public void setTotalPostage(String totalPostage) {
                this.totalPostage = totalPostage;
            }

            public int getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(int payStatus) {
                this.payStatus = payStatus;
            }

            public boolean isIsDel() {
                return isDel;
            }

            public void setIsDel(boolean isDel) {
                this.isDel = isDel;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public int getIsDeliver() {
                return isDeliver;
            }

            public void setIsDeliver(int isDeliver) {
                this.isDeliver = isDeliver;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public String getUpTime() {
                return upTime;
            }

            public void setUpTime(String upTime) {
                this.upTime = upTime;
            }

            public int getRefund() {
                return refund;
            }

            public void setRefund(int refund) {
                this.refund = refund;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public AppStoreUserDeliveryBean getAppStoreUserDelivery() {
                return appStoreUserDelivery;
            }

            public void setAppStoreUserDelivery(AppStoreUserDeliveryBean appStoreUserDelivery) {
                this.appStoreUserDelivery = appStoreUserDelivery;
            }

            public int getMerId() {
                return merId;
            }

            public void setMerId(int merId) {
                this.merId = merId;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getBuyMark() {
                return buyMark;
            }

            public void setBuyMark(String buyMark) {
                this.buyMark = buyMark;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public String getUsdtAddr() {
                return usdtAddr;
            }

            public void setUsdtAddr(String usdtAddr) {
                this.usdtAddr = usdtAddr;
            }

            public static class AppStoreUserDeliveryBean implements Parcelable {
                /**
                 * id : 104
                 * city : 天津市静海县
                 * phone : 13546352586
                 * detailedAddress : 流塘市场22号
                 * num : yyy
                 * userId : 7908
                 * userName : 郭大侠
                 * orderId : 104
                 * status : 1
                 * createTime : 2020-08-21
                 * mark : 发货中
                 * isDel : false,
                 * company:西游降魔
                 */

                private int id;
                private String city;
                private String phone;
                private String detailedAddress;
                private String num;
                private int userId;
                private String userName;
                private int orderId;
                private int status;
                private String createTime;
                private String mark;
                private String country;
                private boolean isDel;
                private String company;

                protected AppStoreUserDeliveryBean(Parcel in) {
                    id = in.readInt();
                    city = in.readString();
                    phone = in.readString();
                    detailedAddress = in.readString();
                    num = in.readString();
                    userId = in.readInt();
                    userName = in.readString();
                    orderId = in.readInt();
                    status = in.readInt();
                    createTime = in.readString();
                    mark = in.readString();
                    country = in.readString();
                    isDel = in.readByte() != 0;
                    company = in.readString();
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(id);
                    dest.writeString(city);
                    dest.writeString(phone);
                    dest.writeString(detailedAddress);
                    dest.writeString(num);
                    dest.writeInt(userId);
                    dest.writeString(userName);
                    dest.writeInt(orderId);
                    dest.writeInt(status);
                    dest.writeString(createTime);
                    dest.writeString(mark);
                    dest.writeString(country);
                    dest.writeByte((byte) (isDel ? 1 : 0));
                    dest.writeString(company);
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                public static final Creator<AppStoreUserDeliveryBean> CREATOR = new Creator<AppStoreUserDeliveryBean>() {
                    @Override
                    public AppStoreUserDeliveryBean createFromParcel(Parcel in) {
                        return new AppStoreUserDeliveryBean(in);
                    }

                    @Override
                    public AppStoreUserDeliveryBean[] newArray(int size) {
                        return new AppStoreUserDeliveryBean[size];
                    }
                };

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getDetailedAddress() {
                    return detailedAddress;
                }

                public void setDetailedAddress(String detailedAddress) {
                    this.detailedAddress = detailedAddress;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
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

                public int getOrderId() {
                    return orderId;
                }

                public void setOrderId(int orderId) {
                    this.orderId = orderId;
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

                public String getMark() {
                    return mark;
                }

                public void setMark(String mark) {
                    this.mark = mark;
                }

                public boolean isIsDel() {
                    return isDel;
                }

                public void setIsDel(boolean isDel) {
                    this.isDel = isDel;
                }

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getCompany() {
                    return company;
                }

                public void setCompany(String company) {
                    this.company = company;
                }
            }
        }
    }
}
