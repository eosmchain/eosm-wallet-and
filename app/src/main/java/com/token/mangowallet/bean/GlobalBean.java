package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class GlobalBean {

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

    public static class RowsBean implements Parcelable {
        private int max_tally_vote_iterate_steps;
        private int max_tally_unvote_iterate_steps;
        private int max_reward_iterate_steps;
        private int max_bp_size;
        private int election_round_sec;
        private BigDecimal refund_delay_sec;
        private int election_round_start_hour;
        private String bp_rewards_per_day;
        private String min_bp_list_quantity;
        private String min_bp_accept_quantity;
        private String min_bp_vote_quantity;
        private String total_listed;
        private String total_voted;
        private String total_received_rewards;
        private String available_rewards;
        private String started_at;
        private int last_election_round;
        private int last_execution_round;

        protected RowsBean(Parcel in) {
            max_tally_vote_iterate_steps = in.readInt();
            max_tally_unvote_iterate_steps = in.readInt();
            max_reward_iterate_steps = in.readInt();
            max_bp_size = in.readInt();
            election_round_sec = in.readInt();
            election_round_start_hour = in.readInt();
            bp_rewards_per_day = in.readString();
            min_bp_list_quantity = in.readString();
            min_bp_accept_quantity = in.readString();
            min_bp_vote_quantity = in.readString();
            total_listed = in.readString();
            total_voted = in.readString();
            total_received_rewards = in.readString();
            available_rewards = in.readString();
            started_at = in.readString();
            last_election_round = in.readInt();
            last_execution_round = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(max_tally_vote_iterate_steps);
            dest.writeInt(max_tally_unvote_iterate_steps);
            dest.writeInt(max_reward_iterate_steps);
            dest.writeInt(max_bp_size);
            dest.writeInt(election_round_sec);
            dest.writeInt(election_round_start_hour);
            dest.writeString(bp_rewards_per_day);
            dest.writeString(min_bp_list_quantity);
            dest.writeString(min_bp_accept_quantity);
            dest.writeString(min_bp_vote_quantity);
            dest.writeString(total_listed);
            dest.writeString(total_voted);
            dest.writeString(total_received_rewards);
            dest.writeString(available_rewards);
            dest.writeString(started_at);
            dest.writeInt(last_election_round);
            dest.writeInt(last_execution_round);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel in) {
                return new RowsBean(in);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };

        public int getMax_tally_vote_iterate_steps() {
            return max_tally_vote_iterate_steps;
        }

        public void setMax_tally_vote_iterate_steps(int max_tally_vote_iterate_steps) {
            this.max_tally_vote_iterate_steps = max_tally_vote_iterate_steps;
        }

        public int getMax_tally_unvote_iterate_steps() {
            return max_tally_unvote_iterate_steps;
        }

        public void setMax_tally_unvote_iterate_steps(int max_tally_unvote_iterate_steps) {
            this.max_tally_unvote_iterate_steps = max_tally_unvote_iterate_steps;
        }

        public int getMax_reward_iterate_steps() {
            return max_reward_iterate_steps;
        }

        public void setMax_reward_iterate_steps(int max_reward_iterate_steps) {
            this.max_reward_iterate_steps = max_reward_iterate_steps;
        }

        public int getMax_bp_size() {
            return max_bp_size;
        }

        public void setMax_bp_size(int max_bp_size) {
            this.max_bp_size = max_bp_size;
        }

        public int getElection_round_sec() {
            return election_round_sec;
        }

        public void setElection_round_sec(int election_round_sec) {
            this.election_round_sec = election_round_sec;
        }

        public BigDecimal getRefund_delay_sec() {
            return refund_delay_sec;
        }

        public void setRefund_delay_sec(BigDecimal refund_delay_sec) {
            this.refund_delay_sec = refund_delay_sec;
        }

        public int getElection_round_start_hour() {
            return election_round_start_hour;
        }

        public void setElection_round_start_hour(int election_round_start_hour) {
            this.election_round_start_hour = election_round_start_hour;
        }

        public String getBp_rewards_per_day() {
            return bp_rewards_per_day;
        }

        public void setBp_rewards_per_day(String bp_rewards_per_day) {
            this.bp_rewards_per_day = bp_rewards_per_day;
        }

        public String getMin_bp_list_quantity() {
            return min_bp_list_quantity;
        }

        public void setMin_bp_list_quantity(String min_bp_list_quantity) {
            this.min_bp_list_quantity = min_bp_list_quantity;
        }

        public String getMin_bp_accept_quantity() {
            return min_bp_accept_quantity;
        }

        public void setMin_bp_accept_quantity(String min_bp_accept_quantity) {
            this.min_bp_accept_quantity = min_bp_accept_quantity;
        }

        public String getMin_bp_vote_quantity() {
            return min_bp_vote_quantity;
        }

        public void setMin_bp_vote_quantity(String min_bp_vote_quantity) {
            this.min_bp_vote_quantity = min_bp_vote_quantity;
        }

        public String getTotal_listed() {
            return total_listed;
        }

        public void setTotal_listed(String total_listed) {
            this.total_listed = total_listed;
        }

        public String getTotal_voted() {
            return total_voted;
        }

        public void setTotal_voted(String total_voted) {
            this.total_voted = total_voted;
        }

        public String getTotal_received_rewards() {
            return total_received_rewards;
        }

        public void setTotal_received_rewards(String total_received_rewards) {
            this.total_received_rewards = total_received_rewards;
        }

        public String getAvailable_rewards() {
            return available_rewards;
        }

        public void setAvailable_rewards(String available_rewards) {
            this.available_rewards = available_rewards;
        }

        public String getStarted_at() {
            return started_at;
        }

        public void setStarted_at(String started_at) {
            this.started_at = started_at;
        }

        public int getLast_election_round() {
            return last_election_round;
        }

        public void setLast_election_round(int last_election_round) {
            this.last_election_round = last_election_round;
        }

        public int getLast_execution_round() {
            return last_execution_round;
        }

        public void setLast_execution_round(int last_execution_round) {
            this.last_execution_round = last_execution_round;
        }
    }
}
