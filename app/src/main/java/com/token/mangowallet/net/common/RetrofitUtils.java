package com.token.mangowallet.net.common;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.token.mangowallet.net.interceptor.BaseUrlInterceptor;
import com.token.mangowallet.net.interceptor.CommonParamInterceptor;
import com.token.mangowallet.net.interceptor.HttpCacheInterceptor;
import com.token.mangowallet.net.interceptor.LogInterceptor;
import com.token.mangowallet.net.interceptor.LogInterceptor2;
import com.token.mangowallet.utils.Constants;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class RetrofitUtils {
    private static final String TAG = "RetrofitUtils";

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        File cacheFile = new File(Utils.getApp().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                try {
//                    String text = URLDecoder.decode(message, "utf-8");
//                    LogUtils.e(LOG_TAG, text);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    LogUtils.e(LOG_TAG, message);
//                }
//            }
//        });
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseUrlInterceptor())
                .addInterceptor(new CommonParamInterceptor())
                .addInterceptor(new LogInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache);
    }

    public static Retrofit.Builder getRetrofitBuilder() {//String baseUrl
        OkHttpClient okHttpClient = getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BaseUrlUtils.getInstance().getCompanyBaseUrl());
    }
}
