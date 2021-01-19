package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class ThemesBean {
    /**
     * code : 0
     * msg : success
     * data : [{"userId":7799,"voteTitle":"我的方案","voteContent":"哈哈哈","createTime":"2021-01-14","status":1,"auditStatus":1,"voteDay":10,"endTime":null,"cashMoney":100000,"payStatus":true,"mark":null,"hash":null,"isDel":false,"address":"mgptest11112","type":4,"isRemit":true,"voteCount":5576757,"rate":1,"isStart":true,"sort":1,"voteStartTime":"2021-01-01","voteEndTime":"2021-01-31","awardId":1,"voteTitleEn":null,"voteContentEn":null,"voteTitleJa":null,"voteContentJa":null,"voteTitleKo":null,"voteContentKo":null,"voteTitleZh":"我的方案","voteContentZh":"哈哈哈","voteId":1}]
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
         * userId : 7799
         * voteTitle : 我的方案
         * voteContent : 哈哈哈
         * createTime : 2021-01-14
         * status : 1
         * auditStatus : 1
         * voteDay : 10
         * endTime : null
         * cashMoney : 100000
         * payStatus : true
         * mark : null
         * hash : null
         * isDel : false
         * address : mgptest11112
         * type : 4
         * isRemit : true
         * voteCount : 5576757
         * rate : 1
         * isStart : true
         * sort : 1
         * voteStartTime : 2021-01-01
         * voteEndTime : 2021-01-31
         * awardId : 1
         * voteTitleEn : null
         * voteContentEn : null
         * voteTitleJa : null
         * voteContentJa : null
         * voteTitleKo : null
         * voteContentKo : null
         * voteTitleZh : 我的方案
         * voteContentZh : 哈哈哈
         * voteId : 1
         */

        private int userId;
        private String voteTitle;
        private String voteContent;
        private String createTime;
        private int status;
        private int auditStatus;
        private int voteDay;
        private String endTime;
        private int cashMoney;
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
        private String voteStartTime;
        private String voteEndTime;
        private int awardId;
        private String voteTitleEn;
        private String voteContentEn;
        private String voteTitleJa;
        private String voteContentJa;
        private String voteTitleKo;
        private String voteContentKo;
        private String voteTitleZh;
        private String voteContentZh;
        private int voteId;

        protected DataBean(Parcel in) {
            userId = in.readInt();
            voteTitle = in.readString();
            voteContent = in.readString();
            createTime = in.readString();
            status = in.readInt();
            auditStatus = in.readInt();
            voteDay = in.readInt();
            cashMoney = in.readInt();
            payStatus = in.readByte() != 0;
            isDel = in.readByte() != 0;
            address = in.readString();
            type = in.readInt();
            isRemit = in.readByte() != 0;
            voteCount = in.readInt();
            isStart = in.readByte() != 0;
            sort = in.readInt();
            voteStartTime = in.readString();
            voteEndTime = in.readString();
            awardId = in.readInt();
            voteTitleZh = in.readString();
            voteContentZh = in.readString();
            voteId = in.readInt();
            rate = new BigDecimal(in.readString());
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
            dest.writeInt(cashMoney);
            dest.writeByte((byte) (payStatus ? 1 : 0));
            dest.writeByte((byte) (isDel ? 1 : 0));
            dest.writeString(address);
            dest.writeInt(type);
            dest.writeByte((byte) (isRemit ? 1 : 0));
            dest.writeInt(voteCount);
            dest.writeByte((byte) (isStart ? 1 : 0));
            dest.writeInt(sort);
            dest.writeString(voteStartTime);
            dest.writeString(voteEndTime);
            dest.writeInt(awardId);
            dest.writeString(voteTitleZh);
            dest.writeString(voteContentZh);
            dest.writeInt(voteId);
            dest.writeString(rate.toPlainString());
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getCashMoney() {
            return cashMoney;
        }

        public void setCashMoney(int cashMoney) {
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

        public String getVoteStartTime() {
            return voteStartTime;
        }

        public void setVoteStartTime(String voteStartTime) {
            this.voteStartTime = voteStartTime;
        }

        public String getVoteEndTime() {
            return voteEndTime;
        }

        public void setVoteEndTime(String voteEndTime) {
            this.voteEndTime = voteEndTime;
        }

        public int getAwardId() {
            return awardId;
        }

        public void setAwardId(int awardId) {
            this.awardId = awardId;
        }

        public String getVoteTitleEn() {
            return voteTitleEn;
        }

        public void setVoteTitleEn(String voteTitleEn) {
            this.voteTitleEn = voteTitleEn;
        }

        public String getVoteContentEn() {
            return voteContentEn;
        }

        public void setVoteContentEn(String voteContentEn) {
            this.voteContentEn = voteContentEn;
        }

        public String getVoteTitleJa() {
            return voteTitleJa;
        }

        public void setVoteTitleJa(String voteTitleJa) {
            this.voteTitleJa = voteTitleJa;
        }

        public String getVoteContentJa() {
            return voteContentJa;
        }

        public void setVoteContentJa(String voteContentJa) {
            this.voteContentJa = voteContentJa;
        }

        public String getVoteTitleKo() {
            return voteTitleKo;
        }

        public void setVoteTitleKo(String voteTitleKo) {
            this.voteTitleKo = voteTitleKo;
        }

        public String getVoteContentKo() {
            return voteContentKo;
        }

        public void setVoteContentKo(String voteContentKo) {
            this.voteContentKo = voteContentKo;
        }

        public String getVoteTitleZh() {
            return voteTitleZh;
        }

        public void setVoteTitleZh(String voteTitleZh) {
            this.voteTitleZh = voteTitleZh;
        }

        public String getVoteContentZh() {
            return voteContentZh;
        }

        public void setVoteContentZh(String voteContentZh) {
            this.voteContentZh = voteContentZh;
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

//    /**
//     * code : 0
//     * msg : success
//     * data : [{"userId":7918,"voteTitle":"给v吧唧嘴","voteContent":"饭后好借好还vv","createTime":"2020-10-22","status":0,"auditStatus":1,"voteDay":10,"cashMoney":1,"payStatus":true,"mark":"审核成功","hash":"5e33cdea271e581d3f2d34cd621a0ae1a0731ed82dcfcbec42655ede4c0593da","isDel":false,"address":"aaaaaab12345","type":3,"isRemit":false,"voteCount":0,"rate":0,"isStart":true,"sort":1,"voteId":3},{"userId":7914,"voteTitle":"这么开心呢更好地天天","voteContent":"鬼哈哈哈哈","createTime":"2020-10-22","status":0,"auditStatus":1,"voteDay":10,"cashMoney":1,"payStatus":true,"mark":"审核成功","hash":"2ecbce62c0caaf57593ba562aa77354f717ebf34005b1c4160fd2625fd1a2082","isDel":false,"address":"aaaaaac12345","type":3,"isRemit":false,"voteCount":0,"rate":0,"isStart":true,"sort":1,"voteId":2},{"userId":7918,"voteTitle":"窟窿","voteContent":"哦的哟哟哟","createTime":"2020-10-22","status":0,"auditStatus":1,"voteDay":10,"cashMoney":1,"payStatus":true,"mark":"审核成功","hash":"ab4af9bedbfdb4c4c2487e6269382c5fa8cb22f293bb6913c0bf56f1b7d05a46","isDel":false,"address":"aaaaaab12345","type":3,"isRemit":false,"voteCount":0,"rate":0,"isStart":true,"sort":1,"voteId":1}]
//     */
//
//    private int code;
//    private String msg;
//    private List<DataBean> data;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean implements Parcelable, Comparable<DataBean> {
//        /**
//         * userId : 7918
//         * voteTitle : 给v吧唧嘴
//         * voteContent : 饭后好借好还vv
//         * createTime : 2020-10-22
//         * status : 0
//         * auditStatus : 1
//         * voteDay : 10
//         * cashMoney : 1.0
//         * payStatus : true
//         * mark : 审核成功
//         * hash : 5e33cdea271e581d3f2d34cd621a0ae1a0731ed82dcfcbec42655ede4c0593da
//         * isDel : false
//         * address : aaaaaab12345
//         * type : 3
//         * isRemit : false
//         * voteCount : 0
//         * rate : 0.0
//         * isStart : true
//         * sort : 1
//         * voteId : 3
//         */
//
//        private int userId;
//        private String voteTitle;
//        private String voteContent;
//        private String voteTitleZh;
//        private String voteContentZh;
//        private String voteTitleEn;
//        private String voteContentEn;
//        private String voteTitleJa;
//        private String voteContentJa;
//        private String voteTitleKo;
//        private String voteContentKo;
//        private String createTime;
//        private int status;
//        private int auditStatus;
//        private int voteDay;
//        private double cashMoney;
//        private boolean payStatus;
//        private String mark;
//        private String hash;
//        private boolean isDel;
//        private String address;
//        private int type;
//        private boolean isRemit;
//        private int voteCount;
//        private BigDecimal rate;
//        private boolean isStart;
//        private int sort;
//        private int voteId;
//
//        protected DataBean(Parcel in) {
//            userId = in.readInt();
//            voteTitle = in.readString();
//            voteContent = in.readString();
//            createTime = in.readString();
//            status = in.readInt();
//            auditStatus = in.readInt();
//            voteDay = in.readInt();
//            cashMoney = in.readDouble();
//            payStatus = in.readByte() != 0;
//            mark = in.readString();
//            hash = in.readString();
//            isDel = in.readByte() != 0;
//            address = in.readString();
//            type = in.readInt();
//            isRemit = in.readByte() != 0;
//            voteCount = in.readInt();
//            rate = new BigDecimal(in.readString());
//            isStart = in.readByte() != 0;
//            sort = in.readInt();
//            voteId = in.readInt();
//            voteTitleZh = in.readString();
//            voteContentZh = in.readString();
//            voteTitleEn = in.readString();
//            voteContentEn = in.readString();
//            voteTitleJa = in.readString();
//            voteContentJa = in.readString();
//            voteTitleKo = in.readString();
//            voteContentKo = in.readString();
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeInt(userId);
//            dest.writeString(voteTitle);
//            dest.writeString(voteContent);
//            dest.writeString(createTime);
//            dest.writeInt(status);
//            dest.writeInt(auditStatus);
//            dest.writeInt(voteDay);
//            dest.writeDouble(cashMoney);
//            dest.writeByte((byte) (payStatus ? 1 : 0));
//            dest.writeString(mark);
//            dest.writeString(hash);
//            dest.writeByte((byte) (isDel ? 1 : 0));
//            dest.writeString(address);
//            dest.writeInt(type);
//            dest.writeByte((byte) (isRemit ? 1 : 0));
//            dest.writeInt(voteCount);
//            dest.writeString(rate.toPlainString());
//            dest.writeByte((byte) (isStart ? 1 : 0));
//            dest.writeInt(sort);
//            dest.writeInt(voteId);
//            dest.writeString(voteTitleZh);
//            dest.writeString(voteContentZh);
//            dest.writeString(voteTitleEn);
//            dest.writeString(voteContentEn);
//            dest.writeString(voteTitleJa);
//            dest.writeString(voteContentJa);
//            dest.writeString(voteTitleKo);
//            dest.writeString(voteContentKo);
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
//            @Override
//            public DataBean createFromParcel(Parcel in) {
//                return new DataBean(in);
//            }
//
//            @Override
//            public DataBean[] newArray(int size) {
//                return new DataBean[size];
//            }
//        };
//
//        public int getUserId() {
//            return userId;
//        }
//
//        public void setUserId(int userId) {
//            this.userId = userId;
//        }
//
//        public String getVoteTitle() {
//            return voteTitle;
//        }
//
//        public void setVoteTitle(String voteTitle) {
//            this.voteTitle = voteTitle;
//        }
//
//        public String getVoteContent() {
//            return voteContent;
//        }
//
//        public void setVoteContent(String voteContent) {
//            this.voteContent = voteContent;
//        }
//
//        public String getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//            this.createTime = createTime;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
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
//        public int getVoteDay() {
//            return voteDay;
//        }
//
//        public void setVoteDay(int voteDay) {
//            this.voteDay = voteDay;
//        }
//
//        public double getCashMoney() {
//            return cashMoney;
//        }
//
//        public void setCashMoney(double cashMoney) {
//            this.cashMoney = cashMoney;
//        }
//
//        public boolean isPayStatus() {
//            return payStatus;
//        }
//
//        public void setPayStatus(boolean payStatus) {
//            this.payStatus = payStatus;
//        }
//
//        public String getMark() {
//            return mark;
//        }
//
//        public void setMark(String mark) {
//            this.mark = mark;
//        }
//
//        public String getHash() {
//            return hash;
//        }
//
//        public void setHash(String hash) {
//            this.hash = hash;
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
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public boolean isIsRemit() {
//            return isRemit;
//        }
//
//        public void setIsRemit(boolean isRemit) {
//            this.isRemit = isRemit;
//        }
//
//        public int getVoteCount() {
//            return voteCount;
//        }
//
//        public void setVoteCount(int voteCount) {
//            this.voteCount = voteCount;
//        }
//
//        public BigDecimal getRate() {
//            return rate;
//        }
//
//        public void setRate(BigDecimal rate) {
//            this.rate = rate;
//        }
//
//        public boolean isIsStart() {
//            return isStart;
//        }
//
//        public void setIsStart(boolean isStart) {
//            this.isStart = isStart;
//        }
//
//        public int getSort() {
//            return sort;
//        }
//
//        public void setSort(int sort) {
//            this.sort = sort;
//        }
//
//        public int getVoteId() {
//            return voteId;
//        }
//
//        public void setVoteId(int voteId) {
//            this.voteId = voteId;
//        }
//
//        public String getVoteTitleEn() {
//            return voteTitleEn;
//        }
//
//        public void setVoteTitleEn(String voteTitleEn) {
//            this.voteTitleEn = voteTitleEn;
//        }
//
//        public String getVoteContentEn() {
//            return voteContentEn;
//        }
//
//        public void setVoteContentEn(String voteContentEn) {
//            this.voteContentEn = voteContentEn;
//        }
//
//        public String getVoteTitleJa() {
//            return voteTitleJa;
//        }
//
//        public void setVoteTitleJa(String voteTitleJa) {
//            this.voteTitleJa = voteTitleJa;
//        }
//
//        public String getVoteContentJa() {
//            return voteContentJa;
//        }
//
//        public void setVoteContentJa(String voteContentJa) {
//            this.voteContentJa = voteContentJa;
//        }
//
//        public String getVoteTitleKo() {
//            return voteTitleKo;
//        }
//
//        public void setVoteTitleKo(String voteTitleKo) {
//            this.voteTitleKo = voteTitleKo;
//        }
//
//        public String getVoteContentKo() {
//            return voteContentKo;
//        }
//
//        public void setVoteContentKo(String voteContentKo) {
//            this.voteContentKo = voteContentKo;
//        }
//
//        public String getVoteTitleZh() {
//            return voteTitleZh;
//        }
//
//        public void setVoteTitleZh(String voteTitleZh) {
//            this.voteTitleZh = voteTitleZh;
//        }
//
//        public String getVoteContentZh() {
//            return voteContentZh;
//        }
//
//        public void setVoteContentZh(String voteContentZh) {
//            this.voteContentZh = voteContentZh;
//        }
//
//        @Override
//        public int compareTo(DataBean o) {
//            int i = this.getSort() - o.getSort();//从小到大
//            return i;
//        }
//    }
}
