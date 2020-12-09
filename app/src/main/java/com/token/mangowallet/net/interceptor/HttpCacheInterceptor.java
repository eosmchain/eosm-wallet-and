package com.token.mangowallet.net.interceptor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.token.mangowallet.utils.Constants;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //有网的时候,读接口上的@Headers里的注解配置
        String cacheControl = request.cacheControl().toString();
        //没有网络并且添加了注解,才使用缓存.
        if (!isNetworkAvailable() && !TextUtils.isEmpty(cacheControl)) {
            //重置请求体;
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .removeHeader("Accept-Encoding")
                    .build();
        }

        //如果没有添加注解,则不缓存
        if (TextUtils.isEmpty(cacheControl) || "no-store".contains(cacheControl)) {
            //响应头设置成无缓存
            cacheControl = "no-store";
        } else if (isNetworkAvailable()) {
            //如果有网络,则将缓存的过期事件,设置为0,获取最新数据
            cacheControl = "public, max-age=" + 0;
        } else {
            //...如果无网络,则根据@headers注解的设置进行缓存.
            // 无网络时，设置缓存过期超时时间为4周
//            int maxStale = 60 * 60 * 24 * 28;
//            cacheControl = "public, only-if-cached, max-stale=" + maxStale;
        }
        request.newBuilder().removeHeader("Accept-Encoding").build();
        Response response = chain.proceed(request);
        LogUtils.dTag(Constants.LOG_TAG, "HttpCacheInterceptor: " + request.toString());
        return response.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .removeHeader("Accept-Encoding")
                .build();
    }


    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) Utils.getApp()
                .getSystemService(Utils.getApp().CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
//    //有网的时候,读接口上的@Headers里的注解配置
//    String cacheControl = request.cacheControl().toString();
////没有网络并且添加了注解,才使用缓存.
//        if (!NetworkUtils.isAvailable()&&!TextUtils.isEmpty(cacheControl)){
//                //重置请求体;
//                request = request.newBuilder()
//                .cacheControl(CacheControl.FORCE_CACHE)
//                .build();
//                }
//
//                //如果没有添加注解,则不缓存
//                if (TextUtils.isEmpty(cacheControl) || "no-store" .contains(cacheControl)) {
//                //响应头设置成无缓存
//                cacheControl = "no-store";
//                } else if (NetworkUtils.isAvailable()) {
//                //如果有网络,则将缓存的过期事件,设置为0,获取最新数据
//                cacheControl = "public, max-age=" + 0;
//                }else {
//                //...如果无网络,则根据@headers注解的设置进行缓存.
//                }
//                Response response = chain.proceed(request);
//                Log.d("httpInterceptor", cacheControl);
//                return response.newBuilder()
//                .header("Cache-Control", cacheControl)
//                .removeHeader("Pragma")
//                .build();
