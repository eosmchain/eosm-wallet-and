package com.token.mangowallet.bean;

import java.util.List;

public class A {

    /**
     * code : 0
     * msg : success
     * data : {"list":[{"id":16,"owner":"aaaaaaaaaa11","candidate":"aaaaaaaaaa11","quantity":"200.0000 MGP","voted_at":"2020-12-04T02:24:44.500","unvoted_at":"1970-01-01T00:00:00.000","restarted_at":"2020-12-04T02:24:44.500","election_round":182,"reward_round":185,"node_name":"呵呵","node_url":"http://www.taobao.com","share_ratio":100},{"id":38,"owner":"aaaaaaaaaa11","candidate":"aaaaaaaaaa11","quantity":"100.0000 MGP","voted_at":"2020-12-04T09:45:50.000","unvoted_at":"1970-01-01T00:00:00.000","restarted_at":"2020-12-04T09:45:50.000","election_round":182,"reward_round":185,"node_name":"呵呵","node_url":"http://www.taobao.com","share_ratio":100},{"id":44,"owner":"aaaaaaaaaa11","candidate":"mgpheying111","quantity":"10.0000 MGP","voted_at":"2020-12-07T08:22:02.500","unvoted_at":"1970-01-01T00:00:00.000","restarted_at":"2020-12-07T08:22:02.500","election_round":1571,"reward_round":1571,"node_name":null,"node_url":null,"share_ratio":null},{"id":45,"owner":"aaaaaaaaaa11","candidate":"aaaaaaaaaa11","quantity":"10.0000 MGP","voted_at":"2020-12-07T08:22:22.000","unvoted_at":"1970-01-01T00:00:00.000","restarted_at":"2020-12-07T08:22:22.000","election_round":1571,"reward_round":1571,"node_name":"呵呵","node_url":"http://www.taobao.com","share_ratio":100},{"id":46,"owner":"aaaaaaaaaa11","candidate":"aaaaaaaaaa11","quantity":"10.0000 MGP","voted_at":"2020-12-07T08:22:44.000","unvoted_at":"1970-01-01T00:00:00.000","restarted_at":"2020-12-07T08:22:44.000","election_round":1571,"reward_round":1571,"node_name":"呵呵","node_url":"http://www.taobao.com","share_ratio":100}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 16
             * owner : aaaaaaaaaa11
             * candidate : aaaaaaaaaa11
             * quantity : 200.0000 MGP
             * voted_at : 2020-12-04T02:24:44.500
             * unvoted_at : 1970-01-01T00:00:00.000
             * restarted_at : 2020-12-04T02:24:44.500
             * election_round : 182
             * reward_round : 185
             * node_name : 呵呵
             * node_url : http://www.taobao.com
             * share_ratio : 100
             */

            private int id;
            private String owner;
            private String candidate;
            private String quantity;
            private String voted_at;
            private String unvoted_at;
            private String restarted_at;
            private int election_round;
            private int reward_round;
            private String node_name;
            private String node_url;
            private int share_ratio;

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

            public int getElection_round() {
                return election_round;
            }

            public void setElection_round(int election_round) {
                this.election_round = election_round;
            }

            public int getReward_round() {
                return reward_round;
            }

            public void setReward_round(int reward_round) {
                this.reward_round = reward_round;
            }

            public String getNode_name() {
                return node_name;
            }

            public void setNode_name(String node_name) {
                this.node_name = node_name;
            }

            public String getNode_url() {
                return node_url;
            }

            public void setNode_url(String node_url) {
                this.node_url = node_url;
            }

            public int getShare_ratio() {
                return share_ratio;
            }

            public void setShare_ratio(int share_ratio) {
                this.share_ratio = share_ratio;
            }
        }
    }
}
