package com.token.mangowallet.bean;

import java.util.List;

public class VoteLogBean {

    /**
     * rows : [{"id":0,"account":"mgptest11111","created_at":"2021-02-25T03:44:14","money":"1.0000 MGP","award_type":0,"is_del":0},{"id":1,"account":"mgpchain2222","created_at":"2021-02-25T03:44:14","money":"1.0000 MGP","award_type":0,"is_del":0},{"id":2,"account":"mgpchain2222","created_at":"2021-02-25T03:44:14","money":"2.0000 MGP","award_type":1,"is_del":0},{"id":3,"account":"mgpchain2222","created_at":"2021-02-25T03:44:14","money":"1.0000 MGP","award_type":1,"is_del":0},{"id":4,"account":"mwalletadmin","created_at":"2021-02-25T03:44:14","money":"8.0000 MGP","award_type":1,"is_del":0},{"id":5,"account":"mgpchain2222","created_at":"2021-02-25T03:44:14","money":"100.0000 MGP","award_type":1,"is_del":0}]
     * more : false
     * next_key :
     */

    private boolean more;
    private String next_key;
    private List<RowsBean> rows;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getNext_key() {
        return next_key;
    }

    public void setNext_key(String next_key) {
        this.next_key = next_key;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * id : 0
         * account : mgptest11111
         * created_at : 2021-02-25T03:44:14
         * money : 1.0000 MGP
         * award_type : 0
         * is_del : 0
         */

        private int id;
        private String account;
        private String created_at;
        private String money;
        private int award_type;
        private int is_del;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getAward_type() {
            return award_type;
        }

        public void setAward_type(int award_type) {
            this.award_type = award_type;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }
    }
}
