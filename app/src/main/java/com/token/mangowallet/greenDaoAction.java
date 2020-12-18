package com.token.mangowallet;

import com.blankj.utilcode.util.AppUtils;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class greenDaoAction {

    public static void main(String[] args) {
        Schema schema = new Schema(4, AppUtils.getAppPackageName());
        Entity entity = schema.addEntity("MANGO_WALLET");
        entity.addBooleanProperty("isBackupss");
    }
}
