package com.token.mangowallet.bean;

import java.util.List;

public class VotesBean {

    private boolean more;
    private String next_key;
    private List<NodeBean> rows;

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

    public List<NodeBean> getRows() {
        return rows;
    }

    public void setRows(List<NodeBean> rows) {
        this.rows = rows;
    }

}
