package com.token.mangowallet.bean;

public class MiningOrderIncomeItem {
    /**
     * typeName : 挖矿收益
     * type : 1
     * money : 0.20000000
     * createTime : 2020-03-06
     * orderLevelName : M1
     */

    private String typeName;
    private int type;
    private String money;
    private String createTime;
    private String orderLevelName;
    private String channelName;
    private int channelType;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderLevelName() {
        return orderLevelName;
    }

    public void setOrderLevelName(String orderLevelName) {
        this.orderLevelName = orderLevelName;
    }
}

