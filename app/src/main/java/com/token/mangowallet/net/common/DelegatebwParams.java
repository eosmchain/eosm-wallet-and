package com.token.mangowallet.net.common;

public class DelegatebwParams {

    /**
     * from : testnetyy111
     * receiver : testnetstake
     * stake_net_quantity : 100.0000 EOS
     * stake_cpu_quantity : 100.0000 EOS
     * transfer : 0
     */

    private String from;
    private String receiver;
    private String stake_net_quantity;
    private String stake_cpu_quantity;
    private int transfer;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStake_net_quantity() {
        return stake_net_quantity;
    }

    public void setStake_net_quantity(String stake_net_quantity) {
        this.stake_net_quantity = stake_net_quantity;
    }

    public String getStake_cpu_quantity() {
        return stake_cpu_quantity;
    }

    public void setStake_cpu_quantity(String stake_cpu_quantity) {
        this.stake_cpu_quantity = stake_cpu_quantity;
    }

    public int getTransfer() {
        return transfer;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }
}
