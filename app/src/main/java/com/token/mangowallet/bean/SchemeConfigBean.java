package com.token.mangowallet.bean;

import java.util.List;

public class SchemeConfigBean {

    /**
     * rows : [{"scheme":0,"vote":0,"award":0,"cash_money":"1.0000 MGP","vote_count":"14.0000 MGP"}]
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
         * scheme : 0
         * vote : 0
         * award : 0
         * cash_money : 1.0000 MGP
         * vote_count : 14.0000 MGP
         */

        private int scheme;
        private int vote;
        private int award;
        private String cash_money;
        private String vote_count;

        public int getScheme() {
            return scheme;
        }

        public void setScheme(int scheme) {
            this.scheme = scheme;
        }

        public int getVote() {
            return vote;
        }

        public void setVote(int vote) {
            this.vote = vote;
        }

        public int getAward() {
            return award;
        }

        public void setAward(int award) {
            this.award = award;
        }

        public String getCash_money() {
            return cash_money;
        }

        public void setCash_money(String cash_money) {
            this.cash_money = cash_money;
        }

        public String getVote_count() {
            return vote_count;
        }

        public void setVote_count(String vote_count) {
            this.vote_count = vote_count;
        }
    }
}
