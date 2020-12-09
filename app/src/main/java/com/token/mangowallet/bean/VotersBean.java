package com.token.mangowallet.bean;

import java.util.List;

public class VotersBean {

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
        private String owner;
        private String total_staked;
        private String last_claimed_rewards;
        private String total_claimed_rewards;
        private String unclaimed_rewards;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getTotal_staked() {
            return total_staked;
        }

        public void setTotal_staked(String total_staked) {
            this.total_staked = total_staked;
        }

        public String getLast_claimed_rewards() {
            return last_claimed_rewards;
        }

        public void setLast_claimed_rewards(String last_claimed_rewards) {
            this.last_claimed_rewards = last_claimed_rewards;
        }

        public String getTotal_claimed_rewards() {
            return total_claimed_rewards;
        }

        public void setTotal_claimed_rewards(String total_claimed_rewards) {
            this.total_claimed_rewards = total_claimed_rewards;
        }

        public String getUnclaimed_rewards() {
            return unclaimed_rewards;
        }

        public void setUnclaimed_rewards(String unclaimed_rewards) {
            this.unclaimed_rewards = unclaimed_rewards;
        }
    }
}
