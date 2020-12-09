package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class FindBean {

    /**
     * code : 0
     * data : {"id":7918,"mgpName":"aaaaaab12345","mgpInvitationCode":"uuuuuuuu1234","lnvitationCode":"M41774607","balance":0,"createTime":"2020-10-15","isMer":1,"isShop":0,"part":1,"credit":0,"download":"https://www.mangochain.io/","shareTitle":"分享规则","shareContent":"1. 分享二维码或复制链接给好友安装和使用\r\n2. 邀请码点击直接复制","shareCount":0}
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
         * id : 7918
         * mgpName : aaaaaab12345
         * mgpInvitationCode : uuuuuuuu1234
         * lnvitationCode : M41774607
         * balance : 0.0
         * createTime : 2020-10-15
         * isMer : 1
         * isShop : 0
         * part : 1
         * credit : 0
         * download : https://www.mangochain.io/
         * shareTitle : 分享规则
         * shareContent : 1. 分享二维码或复制链接给好友安装和使用
         * 2. 邀请码点击直接复制
         * shareCount : 0
         */

        private int id;
        private String mgpName;
        private String mgpInvitationCode;
        private String lnvitationCode;
        private BigDecimal balance;
        private String createTime;
        private int isMer;
        private int isShop;
        private int part;
        private int credit;
        private String download;
        private String shareTitle;
        private String shareContent;
        private int shareCount;

        protected DataBean(Parcel in) {
            id = in.readInt();
            mgpName = in.readString();
            mgpInvitationCode = in.readString();
            lnvitationCode = in.readString();
            createTime = in.readString();
            isMer = in.readInt();
            isShop = in.readInt();
            part = in.readInt();
            credit = in.readInt();
            download = in.readString();
            shareTitle = in.readString();
            shareContent = in.readString();
            shareCount = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(mgpName);
            dest.writeString(mgpInvitationCode);
            dest.writeString(lnvitationCode);
            dest.writeString(createTime);
            dest.writeInt(isMer);
            dest.writeInt(isShop);
            dest.writeInt(part);
            dest.writeInt(credit);
            dest.writeString(download);
            dest.writeString(shareTitle);
            dest.writeString(shareContent);
            dest.writeInt(shareCount);
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

        public String getMgpName() {
            return mgpName;
        }

        public void setMgpName(String mgpName) {
            this.mgpName = mgpName;
        }

        public String getMgpInvitationCode() {
            return mgpInvitationCode;
        }

        public void setMgpInvitationCode(String mgpInvitationCode) {
            this.mgpInvitationCode = mgpInvitationCode;
        }

        public String getLnvitationCode() {
            return lnvitationCode;
        }

        public void setLnvitationCode(String lnvitationCode) {
            this.lnvitationCode = lnvitationCode;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getIsMer() {
            return isMer;
        }

        public void setIsMer(int isMer) {
            this.isMer = isMer;
        }

        public int getIsShop() {
            return isShop;
        }

        public void setIsShop(int isShop) {
            this.isShop = isShop;
        }

        public int getPart() {
            return part;
        }

        public void setPart(int part) {
            this.part = part;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }
    }
}
