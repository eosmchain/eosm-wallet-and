package com.token.mangowallet.bean;

import java.util.List;

public class MyVoteBean {

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
        private int id;
        private String owner;
        private String candidate;
        private String quantity;
        private String voted_at;
        private String unvoted_at;
        private String restarted_at;
        private String last_vote_tallied_at;
        private String last_unvote_tallied_at;
        private String last_rewarded_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getCandidate() {
            return candidate;
        }

        public void setCandidate(String candidate) {
            this.candidate = candidate;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getVoted_at() {
            return voted_at;
        }

        public void setVoted_at(String voted_at) {
            this.voted_at = voted_at;
        }

        public String getUnvoted_at() {
            return unvoted_at;
        }

        public void setUnvoted_at(String unvoted_at) {
            this.unvoted_at = unvoted_at;
        }

        public String getRestarted_at() {
            return restarted_at;
        }

        public void setRestarted_at(String restarted_at) {
            this.restarted_at = restarted_at;
        }

        public String getLast_vote_tallied_at() {
            return last_vote_tallied_at;
        }

        public void setLast_vote_tallied_at(String last_vote_tallied_at) {
            this.last_vote_tallied_at = last_vote_tallied_at;
        }

        public String getLast_unvote_tallied_at() {
            return last_unvote_tallied_at;
        }

        public void setLast_unvote_tallied_at(String last_unvote_tallied_at) {
            this.last_unvote_tallied_at = last_unvote_tallied_at;
        }

        public String getLast_rewarded_at() {
            return last_rewarded_at;
        }

        public void setLast_rewarded_at(String last_rewarded_at) {
            this.last_rewarded_at = last_rewarded_at;
        }
    }
}
