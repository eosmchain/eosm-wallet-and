package com.token.mangowallet.bean;

import java.util.List;

public class MyVoteRecordBean {

    /**
     * rows : [{"id":1,"account":"mgpchain2222","vote_count":"1.0000 MGP","created_at":"2021-02-24T08:37:47","scheme_title":"冰魄","scheme_content":"名字啊啊，我的话题终结者了。，我","scheme_id":0}]
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
         * id : 1
         * account : mgpchain2222
         * vote_count : 1.0000 MGP
         * created_at : 2021-02-24T08:37:47
         * scheme_title : 冰魄
         * scheme_content : 名字啊啊，我的话题终结者了。，我
         * scheme_id : 0
         */

        private int id;
        private String account;
        private String vote_count;
        private String created_at;
        private String scheme_title;
        private String scheme_content;
        private int scheme_id;
        ////////////////////////////////
        private String updated_at;
        private int is_del;
        private String cash_money;
        private int is_remit;

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

        public String getVote_count() {
            return vote_count;
        }

        public void setVote_count(String vote_count) {
            this.vote_count = vote_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getScheme_title() {
            return scheme_title;
        }

        public void setScheme_title(String scheme_title) {
            this.scheme_title = scheme_title;
        }

        public String getScheme_content() {
            return scheme_content;
        }

        public void setScheme_content(String scheme_content) {
            this.scheme_content = scheme_content;
        }

        public int getScheme_id() {
            return scheme_id;
        }

        public void setScheme_id(int scheme_id) {
            this.scheme_id = scheme_id;
        }
    }
}
