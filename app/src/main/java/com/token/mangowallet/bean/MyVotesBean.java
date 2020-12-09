package com.token.mangowallet.bean;

import java.util.List;

public class MyVotesBean {

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
        private List<NodeBean> list;

        public List<NodeBean> getList() {
            return list;
        }

        public void setList(List<NodeBean> list) {
            this.list = list;
        }
    }
}
