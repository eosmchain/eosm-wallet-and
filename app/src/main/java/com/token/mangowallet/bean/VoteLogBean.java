package com.token.mangowallet.bean;

import java.util.List;

public class VoteLogBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":1,"awardId":2,"address":"aaaaaab12345","money":1,"voteCount":2,"voteId":5,"type":0,"typeName":"方案奖励"},{"id":3,"awardId":3,"address":"aaaaaab12345","money":1,"voteCount":2,"voteId":10,"type":0,"typeName":"方案奖励"},{"id":4,"awardId":3,"address":"aaaaaab12345","money":5,"voteCount":2,"voteId":10,"type":1,"typeName":"投票成功奖励"}]
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

    public static class DataBean {
        /**
         * id : 1
         * awardId : 2
         * address : aaaaaab12345
         * money : 1
         * voteCount : 2
         * voteId : 5
         * type : 0
         * typeName : 方案奖励
         */

        private int id;
        private int awardId;
        private String address;
        private int money;
        private int voteCount;
        private int voteId;
        private int type;
        private String typeName;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAwardId() {
            return awardId;
        }

        public void setAwardId(int awardId) {
            this.awardId = awardId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public int getVoteId() {
            return voteId;
        }

        public void setVoteId(int voteId) {
            this.voteId = voteId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
