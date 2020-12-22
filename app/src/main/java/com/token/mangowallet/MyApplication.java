/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.token.mangowallet;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.bugly.crashreport.CrashReport;
import com.token.mangowallet.db.greendao.DBManager;
import com.token.mangowallet.db.greendao.DaoMaster;
import com.token.mangowallet.db.greendao.DaoSession;
import com.token.mangowallet.db.greendao.MangoWalletDao;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.RetrofitUtils;
import com.token.mangowallet.repository.RepositoryFactory;
import com.token.mangowallet.repository.SharedPreferenceRepository;
import com.token.mangowallet.service.LocationService;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.DynamicTimeFormat;
import com.token.mangowallet.utils.MediaLoader;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.io.File;
import java.util.Locale;

import okhttp3.OkHttpClient;

/**
 * Demo 的 Application 入口。
 * Created by cgine on 16/3/22.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

    private static OkHttpClient httpClient;
    public static RepositoryFactory repositoryFactory;
    public static SharedPreferenceRepository sp;
    private static Context context;
    public static String mMid = "";
    public LocationService locationService;
    public static FirebaseAnalytics mFirebaseAnalytics;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat(StringUtils.getString(R.string.str_updated_on)));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });

    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //强制APP不跟随系统进入深色模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setApplication(this);
//        setDatabase();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());

        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    public static synchronized void setApplication(@NonNull MyApplication application) {
        sInstance = application;
        CrashUtils.init(getSDPath());
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = APPUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "ae70a2f85f", Constants.ISTEST, strategy);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(application);
        NetWorkManager.getInstance().init();
        //初始化工具类
        Utils.init(application);
        QMUISwipeBackActivityManager.init(application);
        //拍照和图片裁剪框架初始化
        Album.initialize(AlbumConfig.newBuilder(application)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build());
        AppFilePath.init(sInstance);
//        UMConfigure.init(sInstance, "5f222e22d309322154733321", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
//                "");
//        Realm.init(sInstance);
        sp = SharedPreferenceRepository.init(sInstance);
//        httpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LogInterceptor())
//                .build();
        httpClient = RetrofitUtils.getOkHttpClientBuilder().build();
        repositoryFactory = RepositoryFactory.init(sp, httpClient, GsonUtils.getGson());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public DaoSession getDaoSession() {
        return DBManager.getInstance(context).getDaoSession();
    }

    public static MyApplication getInstance() {
        return sInstance;
    }


    /**
     * 获取sd卡路径
     *
     * @return
     */
    private static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Constants.CrashFilePath);
            if (!sdDir.exists()) {
                sdDir.mkdirs();
            }
        }
        return sdDir;
    }

    public static OkHttpClient okHttpClient() {
        return httpClient;
    }

    public static RepositoryFactory repositoryFactory() {
        return repositoryFactory;
    }

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, context.getPackageName(), null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
}
