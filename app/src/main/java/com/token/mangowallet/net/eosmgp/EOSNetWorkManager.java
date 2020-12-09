package com.token.mangowallet.net.eosmgp;

import com.blankj.utilcode.util.ToastUtils;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.utils.Constants;

import one.block.eosiojavarpcprovider.error.EosioJavaRpcProviderInitializerError;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;

public class EOSNetWorkManager {

    static EOSNetWorkManager mInstance;
    static EosioJavaRpcProviderImpl rpcProvider;

    public static EOSNetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (EOSNetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new EOSNetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {//String baseUrl
        try {
            rpcProvider = new EosioJavaRpcProviderImpl(BaseUrlUtils.getInstance().getDIGICCYBaseUrl(), Constants.ISTEST);
        } catch (EosioJavaRpcProviderInitializerError eosioJavaRpcProviderInitializerError) {
            eosioJavaRpcProviderInitializerError.printStackTrace();
            // Happens if creating EosioJavaRpcProviderImpl unsuccessful
            ToastUtils.showShort(eosioJavaRpcProviderInitializerError.asJsonString());
            rpcProvider = null;
        }
    }

    public static EosioJavaRpcProviderImpl getRpcProvider() throws EosioJavaRpcProviderInitializerError {
        if (rpcProvider == null) {
            synchronized (EosioJavaRpcProviderImpl.class) {
                rpcProvider = new EosioJavaRpcProviderImpl(BaseUrlUtils.getInstance().getDIGICCYBaseUrl(), Constants.ISTEST);
            }
        }
        return rpcProvider;
    }

    public static void reSetRequest() {
        rpcProvider = null;
    }
}
