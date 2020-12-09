package com.token.mangowallet.bean.entity;

import com.chad.library.adapter.base.entity.JSectionEntity;

public class ImgSection extends JSectionEntity {
    public static final int GOODS_COVER = 1;
    public static final int GOODS_SLIDESHOW = 2;
    public boolean isHeader;
    public boolean isAdd;
    public int type;
    public Object object;

    public ImgSection(boolean isHeader, boolean isAdd, int type, Object object) {
        this.isHeader = isHeader;
        this.type = type;
        this.object = object;
        this.isAdd = isAdd;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }

}

