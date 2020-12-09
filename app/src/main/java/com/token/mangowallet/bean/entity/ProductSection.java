package com.token.mangowallet.bean.entity;

import com.chad.library.adapter.base.entity.JSectionEntity;

public class ProductSection extends JSectionEntity {
    //    public static final int GOODS_COVER = 1;
//    public static final int GOODS_SLIDESHOW = 2;
    public boolean isHeader;
    public Object object;

    public ProductSection(boolean isHeader, Object object) {
        this.isHeader = isHeader;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }

}

