package com.token.mangowallet.bean.entity;

import com.token.mangowallet.utils.Constants;

public class WalletBean {
    private Constants.WalletType walletType;
    private String balance;
    private String value;

    public Constants.WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(Constants.WalletType walletType) {
        this.walletType = walletType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
