package com.token.mangowallet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.token.mangowallet.db.greendao.DaoMaster;

import org.greenrobot.greendao.database.Database;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.token.mangowallet.db.greendao.MangoWalletDao;

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, MangoWalletDao.class);
    }
}
