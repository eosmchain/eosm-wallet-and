package com.token.mangowallet.db;

import android.content.Context;

import com.token.mangowallet.db.greendao.DaoMaster;

//public class MyGreenDaoUpgradeOpenHelper extends DaoMaster.OpenHelper {
//    public MyGreenDaoUpgradeOpenHelper(Context context, String name) {
//        super(context, name);
//    }
//
//    @Override
//    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
//
//            @Override
//            public void onCreateAllTables(Database db, boolean ifNotExists) {
//                DaoMaster.createAllTables(db, ifNotExists);
//            }
//
//            @Override
//            public void onDropAllTables(Database db, boolean ifExists) {
//                DaoMaster.dropAllTables(db, ifExists);
//            }
//        }, UserInfoTableDao.class, VideoHistroyTableDao.class);
//    }
//}

