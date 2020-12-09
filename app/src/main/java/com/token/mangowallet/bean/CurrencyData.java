package com.token.mangowallet.bean;

public class CurrencyData {
    /**
     * name : 人民币
     * symbol : ￥
     * price : 6.91
     * sort : 1
     * symbolName : CNY
     */

    private String name;
    private String symbol;
    private String price;
    private int sort;
    private String symbolName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
}
