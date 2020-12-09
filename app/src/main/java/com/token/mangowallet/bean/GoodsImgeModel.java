package com.token.mangowallet.bean;

public class GoodsImgeModel {
    public static final int GOODS_COVER = 1;
    public static final int GOODS_SLIDESHOW = 2;
    public int type;
    public String filePath;

    public GoodsImgeModel(int type, String filePath) {
        this.type = type;
        this.filePath = filePath;
    }
}
