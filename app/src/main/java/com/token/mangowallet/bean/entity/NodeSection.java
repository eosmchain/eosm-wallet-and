package com.token.mangowallet.bean.entity;

import com.chad.library.adapter.base.entity.JSectionEntity;

public class NodeSection extends JSectionEntity {
    public boolean isHeader;
    public boolean isSuperNode;
    public Object object;

    public NodeSection(boolean isHeader, boolean isSuperNode, Object object) {
        this.isHeader = isHeader;
        this.object = object;
        this.isSuperNode = isSuperNode;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }

}

