package com.token.mangowallet.bean;

import java.util.List;

public class CandidatesBean {

    /**
     * rows : [{"owner":"mgpchain2222","self_reward_share":2800,"staked_votes":"100000.0000 MGP","received_votes":"0.0000 MGP","tallied_votes":"0.0000 MGP","last_claimed_rewards":"0.0000 MGP","total_claimed_rewards":"0.0000 MGP","unclaimed_rewards":"0.0000 MGP"}]
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
         * owner : mgpchain2222
         * self_reward_share : 2800
         * staked_votes : 100000.0000 MGP
         * received_votes : 0.0000 MGP
         * tallied_votes : 0.0000 MGP
         * last_claimed_rewards : 0.0000 MGP
         * total_claimed_rewards : 0.0000 MGP
         * unclaimed_rewards : 0.0000 MGP
         */

        private String owner;
        private int self_reward_share;
        private String staked_votes;
        private String received_votes;
        private String tallied_votes;
        private String last_claimed_rewards;
        private String total_claimed_rewards;
        private String unclaimed_rewards;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public int getSelf_reward_share() {
            return self_reward_share;
        }

        public void setSelf_reward_share(int self_reward_share) {
            this.self_reward_share = self_reward_share;
        }

        public String getStaked_votes() {
            return staked_votes;
        }

        public void setStaked_votes(String staked_votes) {
            this.staked_votes = staked_votes;
        }

        public String getReceived_votes() {
            return received_votes;
        }

        public void setReceived_votes(String received_votes) {
            this.received_votes = received_votes;
        }

        public String getTallied_votes() {
            return tallied_votes;
        }

        public void setTallied_votes(String tallied_votes) {
            this.tallied_votes = tallied_votes;
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
