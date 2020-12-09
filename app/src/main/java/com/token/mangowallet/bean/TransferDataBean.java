package com.token.mangowallet.bean;

public class TransferDataBean {

    /**
     * from : pieinstantoo
     * memo :
     * quantity : 5.6559 EOS
     * to : algorguo1234
     */

    private String from;
    private String memo;
    private String quantity;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
