package com.token.mangowallet.bean;

import java.math.BigDecimal;

public class TransferNameMode {

    /**
     * type : 1
     * accountName : proltniouser
     */

    private BigDecimal type;
    private String accountName;

    public BigDecimal getType() {
        return type;
    }

    public void setType(BigDecimal type) {
        this.type = type;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
