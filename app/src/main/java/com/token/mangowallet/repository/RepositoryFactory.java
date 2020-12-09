package com.token.mangowallet.repository;

import com.google.gson.Gson;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.eosmgp.CustomEosioJavaRpcProviderImpl;
import com.token.mangowallet.net.eth.service.BlockExplorerClient;
import com.token.mangowallet.net.eth.service.EthplorerTokenService;
import com.token.mangowallet.net.eth.service.TokenExplorerClientType;

import okhttp3.OkHttpClient;
import one.block.eosiojavarpcprovider.error.EosioJavaRpcProviderInitializerError;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * 微信: xlbxiong
 */

public class RepositoryFactory {

    public TokenRepository tokenRepository;
    public TransactionRepository transactionRepository;
    public EthereumNetworkRepository ethereumNetworkRepository;
    public static RepositoryFactory sSelf;
    public CustomEosioJavaRpcProviderImpl customEosioJavaRpcProvider;

    private RepositoryFactory(SharedPreferenceRepository sp, OkHttpClient httpClient, Gson gson) {
        ethereumNetworkRepository = EthereumNetworkRepository.init(sp);

        TokenLocalSource tokenLocalSource = new RealmTokenSource();

        TokenExplorerClientType tokenExplorerClientType = new EthplorerTokenService(httpClient, gson);
        BlockExplorerClient blockExplorerClient = new BlockExplorerClient(httpClient, gson, ethereumNetworkRepository);
        try {
            customEosioJavaRpcProvider = new CustomEosioJavaRpcProviderImpl(NetWorkManager.getRequest());
        } catch (EosioJavaRpcProviderInitializerError eosioJavaRpcProviderInitializerError) {
            eosioJavaRpcProviderInitializerError.printStackTrace();
        }

        tokenRepository = new TokenRepository(httpClient, ethereumNetworkRepository, tokenExplorerClientType, tokenLocalSource, customEosioJavaRpcProvider);

        TransactionLocalSource inMemoryCache = new TransactionInMemorySource();

        transactionRepository = new TransactionRepository(ethereumNetworkRepository, inMemoryCache, null, blockExplorerClient);
    }

    public static RepositoryFactory init(SharedPreferenceRepository sp, OkHttpClient httpClient, Gson gson) {
        if (sSelf == null) {
            sSelf = new RepositoryFactory(sp, httpClient, gson);
        }
        return sSelf;
    }

}
