package com.token.mangowallet.net.common;

import retrofit2.Retrofit;

public class NetWorkManager {

    private static NetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile RequestApi request = null;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {//String baseUrl
        retrofit = RetrofitUtils.getRetrofitBuilder().build();
    }

    public static RequestApi getRequest() {
        if (request == null) {
            synchronized (RequestApi.class) {
                request = retrofit.create(RequestApi.class);
            }
        }
        return request;
    }



    public static void reSetRequest() {
        request = null;
    }
}
