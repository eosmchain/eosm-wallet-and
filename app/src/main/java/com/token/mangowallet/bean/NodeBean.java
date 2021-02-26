package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class NodeBean implements Parcelable {
    /**
     * owner : captainoo.us
     * self_reward_share : 3000
     * staked_votes : 100000.0000 MGP
     * received_votes : 60000.0000 MGP
     * tallied_votes : 180000.0000 MGP
     * last_claimed_rewards : 0.0000 MGP
     * total_claimed_rewards : 0.0000 MGP
     * unclaimed_rewards : 2295.8246 MGP
     * node_url : null
     * node_name : captainoo.us
     */
    ///////////////candidates
    private String owner;
    private BigDecimal self_reward_share;
    private String staked_votes;
    private String received_votes;
    private String last_claimed_rewards;
    private String total_claimed_rewards;
    private String unclaimed_rewards;

    ///////////////votes
    private int id;
    private String candidate;
    private String quantity;
    private String voted_at;
    private String unvoted_at;
    private String restarted_at;
    private String last_vote_tallied_at;
    private String last_unvote_tallied_at;
    private String last_rewarded_at;

    ////////////////////////////投票主界面数据
    /**
     * owner : abcabc123123
     * self_reward_share : 5000
     * staked_votes : 100000.0000 MGP
     * received_votes : 100500.0000 MGP
     * tallied_votes : 0.0000 MGP
     * last_claimed_rewards : 0.0000 MGP
     * total_claimed_rewards : 0.0000 MGP
     * unclaimed_rewards : 0.0000 MGP
     * node_url : null
     * node_name : abcabc123123
     */
    private String tallied_votes;
    private String node_url;
    private String node_name;

    private BigDecimal election_round;
    private BigDecimal reward_round;
    private BigDecimal share_ratio;

    private String staked_votes_num;
    private String received_votes_num;
    private String tallied_votes_num;
    private String total_votes_num;


    protected NodeBean(Parcel in) {
        owner = in.readString();
        self_reward_share = new BigDecimal(in.readString());
        staked_votes = in.readString();
        received_votes = in.readString();
        last_claimed_rewards = in.readString();
        total_claimed_rewards = in.readString();
        unclaimed_rewards = in.readString();
        id = in.readInt();
        candidate = in.readString();
        quantity = in.readString();
        voted_at = in.readString();
        unvoted_at = in.readString();
        restarted_at = in.readString();
        last_vote_tallied_at = in.readString();
        last_unvote_tallied_at = in.readString();
        last_rewarded_at = in.readString();
        tallied_votes = in.readString();
        node_url = in.readString();
        node_name = in.readString();
        election_round = new BigDecimal(in.readString());
        reward_round = new BigDecimal(in.readString());
        share_ratio = new BigDecimal(in.readString());
        staked_votes_num = in.readString();
        received_votes_num = in.readString();
        tallied_votes_num = in.readString();
        total_votes_num = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(owner);
        dest.writeString(self_reward_share == null ? "0" : self_reward_share.toPlainString());
        dest.writeString(staked_votes);
        dest.writeString(received_votes);
        dest.writeString(last_claimed_rewards);
        dest.writeString(total_claimed_rewards);
        dest.writeString(unclaimed_rewards);
        dest.writeInt(id);
        dest.writeString(candidate);
        dest.writeString(quantity);
        dest.writeString(voted_at);
        dest.writeString(unvoted_at);
        dest.writeString(restarted_at);
        dest.writeString(last_vote_tallied_at);
        dest.writeString(last_unvote_tallied_at);
        dest.writeString(last_rewarded_at);
        dest.writeString(tallied_votes);
        dest.writeString(node_url);
        dest.writeString(node_name);
        dest.writeString(election_round == null ? "0" : election_round.toPlainString());
        dest.writeString(reward_round == null ? "0" : reward_round.toPlainString());
        dest.writeString(share_ratio == null ? "0" : share_ratio.toPlainString());
        dest.writeString(staked_votes_num);
        dest.writeString(received_votes_num);
        dest.writeString(tallied_votes_num);
        dest.writeString(total_votes_num);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NodeBean> CREATOR = new Creator<NodeBean>() {
        @Override
        public NodeBean createFromParcel(Parcel in) {
            return new NodeBean(in);
        }

        @Override
        public NodeBean[] newArray(int size) {
            return new NodeBean[size];
        }
    };

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getSelf_reward_share() {
        return self_reward_share;
    }

    public void setSelf_reward_share(BigDecimal self_reward_share) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTallied_votes() {
        return tallied_votes;
    }

    public void setTallied_votes(String tallied_votes) {
        this.tallied_votes = tallied_votes;
    }

    public String getNode_url() {
        return node_url;
    }

    public void setNode_url(String node_url) {
        this.node_url = node_url;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public BigDecimal getElection_round() {
        return election_round;
    }

    public void setElection_round(BigDecimal election_round) {
        this.election_round = election_round;
    }

    public BigDecimal getReward_round() {
        return reward_round;
    }

    public void setReward_round(BigDecimal reward_round) {
        this.reward_round = reward_round;
    }

    public BigDecimal getShare_ratio() {
        return share_ratio;
    }

    public void setShare_ratio(BigDecimal share_ratio) {
        this.share_ratio = share_ratio;
    }

    public String getStaked_votes_num() {
        return staked_votes_num;
    }

    public void setStaked_votes_num(String staked_votes_num) {
        this.staked_votes_num = staked_votes_num;
    }

    public String getReceived_votes_num() {
        return received_votes_num;
    }

    public void setReceived_votes_num(String received_votes_num) {
        this.received_votes_num = received_votes_num;
    }

    public String getTallied_votes_num() {
        return tallied_votes_num;
    }

    public void setTallied_votes_num(String tallied_votes_num) {
        this.tallied_votes_num = tallied_votes_num;
    }

    public String getTotal_votes_num() {
        return total_votes_num;
    }

    public void setTotal_votes_num(String total_votes_num) {
        this.total_votes_num = total_votes_num;
    }

    public static Creator<NodeBean> getCREATOR() {
        return CREATOR;
    }
}
