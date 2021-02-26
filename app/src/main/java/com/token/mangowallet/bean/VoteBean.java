package com.token.mangowallet.bean;

import java.util.List;

public class VoteBean {

    /**
     * rows : [{"id":0,"account":"mgpchain2222","created_at":"2021-02-25T09:39:29","vote_count":"2.0000 MGP","scheme_id":0,"is_remit":0,"is_super_node":0,"is_del":0,"scheme_title":"阿狸","scheme_content":"几斤几两"}]
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
         * account : mgpchain2222
         * created_at : 2021-02-25T09:39:29
         * vote_count : 2.0000 MGP
         * scheme_id : 0
         * is_remit : 0
         * is_super_node : 0
         * is_del : 0
         * scheme_title : 阿狸
         * scheme_content : 几斤几两
         */

        private int id;
        private String account;
        private String created_at;
        private String vote_count;
        private int scheme_id;
        private int is_remit;
        private int is_super_node;
        private int is_del;
        private String scheme_title;
        private String scheme_content;

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

        public String getVote_count() {
            return vote_count;
        }

        public void setVote_count(String vote_count) {
            this.vote_count = vote_count;
        }

        public int getScheme_id() {
            return scheme_id;
        }

        public void setScheme_id(int scheme_id) {
            this.scheme_id = scheme_id;
        }

        public int getIs_remit() {
            return is_remit;
        }

        public void setIs_remit(int is_remit) {
            this.is_remit = is_remit;
        }

        public int getIs_super_node() {
            return is_super_node;
        }

        public void setIs_super_node(int is_super_node) {
            this.is_super_node = is_super_node;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
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
    }
}
