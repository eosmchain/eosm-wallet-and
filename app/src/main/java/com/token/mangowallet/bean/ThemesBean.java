package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class ThemesBean {

    /**
     * code : 0
     * msg : success
     * data : [{"userId":7918,"voteTitle":"给v吧唧嘴","voteContent":"饭后好借好还vv","createTime":"2020-10-22","status":0,"auditStatus":1,"voteDay":10,"cashMoney":1,"payStatus":true,"mark":"审核成功","hash":"5e33cdea271e581d3f2d34cd621a0ae1a0731ed82dcfcbec42655ede4c0593da","isDel":false,"address":"aaaaaab12345","type":3,"isRemit":false,"voteCount":0,"rate":0,"isStart":true,"sort":1,"voteId":3},{"userId":7914,"voteTitle":"这么开心呢更好地天天","voteContent":"鬼哈哈哈哈","createTime":"2020-10-22","status":0,"auditStatus":1,"voteDay":10,"cashMoney":1,"payStatus":true,"mark":"审核成功","hash":"2ecbce62c0caaf57593ba562aa77354f717ebf34005b1c4160fd2625fd1a2082","isDel":false,"address":"aaaaaac12345","type":3,"isRemit":false,"voteCount":0,"rate":0,"isStart":true,"sort":1,"voteId":2},{"userId":7918,"voteTitle":"窟窿","voteContent":"哦的哟哟哟","createTime":"2020-10-22","status":0,"auditStatus":1,"voteDay":10,"cashMoney":1,"payStatus":true,"mark":"审核成功","hash":"ab4af9bedbfdb4c4c2487e6269382c5fa8cb22f293bb6913c0bf56f1b7d05a46","isDel":false,"address":"aaaaaab12345","type":3,"isRemit":false,"voteCount":0,"rate":0,"isStart":true,"sort":1,"voteId":1}]
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

    public static class DataBean implements Parcelable, Comparable<DataBean> {
        /**
         * userId : 7918
         * voteTitle : 给v吧唧嘴
         * voteContent : 饭后好借好还vv
         * createTime : 2020-10-22
         * status : 0
         * auditStatus : 1
         * voteDay : 10
         * cashMoney : 1.0
         * payStatus : true
         * mark : 审核成功
         * hash : 5e33cdea271e581d3f2d34cd621a0ae1a0731ed82dcfcbec42655ede4c0593da
         * isDel : false
         * address : aaaaaab12345
         * type : 3
         * isRemit : false
         * voteCount : 0
         * rate : 0.0
         * isStart : true
         * sort : 1
         * voteId : 3
         */

        private int userId;
        private String voteTitle;
        private String voteContent;
        private String createTime;
        private int status;
        private int auditStatus;
        private int voteDay;
        private double cashMoney;
        private boolean payStatus;
        private String mark;
        private String hash;
        private boolean isDel;
        private String address;
        private int type;
        private boolean isRemit;
        private int voteCount;
        private BigDecimal rate;
        private boolean isStart;
        private int sort;
        private int voteId;

        protected DataBean(Parcel in) {
            userId = in.readInt();
            voteTitle = in.readString();
            voteContent = in.readString();
            createTime = in.readString();
            status = in.readInt();
            auditStatus = in.readInt();
            voteDay = in.readInt();
            cashMoney = in.readDouble();
            payStatus = in.readByte() != 0;
            mark = in.readString();
            hash = in.readString();
            isDel = in.readByte() != 0;
            address = in.readString();
            type = in.readInt();
            isRemit = in.readByte() != 0;
            voteCount = in.readInt();
            rate = new BigDecimal(in.readString());
            isStart = in.readByte() != 0;
            sort = in.readInt();
            voteId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(userId);
            dest.writeString(voteTitle);
            dest.writeString(voteContent);
            dest.writeString(createTime);
            dest.writeInt(status);
            dest.writeInt(auditStatus);
            dest.writeInt(voteDay);
            dest.writeDouble(cashMoney);
            dest.writeByte((byte) (payStatus ? 1 : 0));
            dest.writeString(mark);
            dest.writeString(hash);
            dest.writeByte((byte) (isDel ? 1 : 0));
            dest.writeString(address);
            dest.writeInt(type);
            dest.writeByte((byte) (isRemit ? 1 : 0));
            dest.writeInt(voteCount);
            dest.writeString(rate.toPlainString());
            dest.writeByte((byte) (isStart ? 1 : 0));
            dest.writeInt(sort);
            dest.writeInt(voteId);
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

        public String getVoteTitle() {
            return voteTitle;
        }

        public void setVoteTitle(String voteTitle) {
            this.voteTitle = voteTitle;
        }

        public String getVoteContent() {
            return voteContent;
        }

        public void setVoteContent(String voteContent) {
            this.voteContent = voteContent;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public int getVoteDay() {
            return voteDay;
        }

        public void setVoteDay(int voteDay) {
            this.voteDay = voteDay;
        }

        public double getCashMoney() {
            return cashMoney;
        }

        public void setCashMoney(double cashMoney) {
            this.cashMoney = cashMoney;
        }

        public boolean isPayStatus() {
            return payStatus;
        }

        public void setPayStatus(boolean payStatus) {
            this.payStatus = payStatus;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isIsRemit() {
            return isRemit;
        }

        public void setIsRemit(boolean isRemit) {
            this.isRemit = isRemit;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public boolean isIsStart() {
            return isStart;
        }

        public void setIsStart(boolean isStart) {
            this.isStart = isStart;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getVoteId() {
            return voteId;
        }

        public void setVoteId(int voteId) {
            this.voteId = voteId;
        }


        @Override
        public int compareTo(DataBean o) {
            int i = this.getSort() - o.getSort();//从小到大
            return i;
        }
    }
}
