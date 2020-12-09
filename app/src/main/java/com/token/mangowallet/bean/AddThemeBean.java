package com.token.mangowallet.bean;

public class AddThemeBean {

    /**
     * code : 0
     * msg : success
     * data : {"userId":7918,"voteTitle":"TEST","voteContent":"test msg","createTime":"2020-10-21","status":2,"auditStatus":3,"voteDay":10,"voteId":2}
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
         * userId : 7918
         * voteTitle : TEST
         * voteContent : test msg
         * createTime : 2020-10-21
         * status : 2
         * auditStatus : 3
         * voteDay : 10
         * voteId : 2
         */

        private int userId;
        private String voteTitle;
        private String voteContent;
        private String createTime;
        private int status;
        private int auditStatus;
        private int voteDay;
        private int voteId;

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

        public int getVoteId() {
            return voteId;
        }

        public void setVoteId(int voteId) {
            this.voteId = voteId;
        }
    }
}
