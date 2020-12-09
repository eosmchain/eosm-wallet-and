package com.token.mangowallet.bus;


import com.token.mangowallet.db.MangoWallet;

public class ToWallet {
    MangoWallet wallet;
    int type;

    public ToWallet(MangoWallet wallet, int type) {
        this.wallet = wallet;
        this.type = type;
    }

    public MangoWallet getWallet() {
        return wallet;
    }

    public void setWallet(MangoWallet wallet) {
        this.wallet = wallet;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
