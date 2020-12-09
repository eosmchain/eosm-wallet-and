package com.token.mangowallet.repository;


import com.token.mangowallet.entity.NetworkInfo;
import com.token.mangowallet.entity.TokenInfo;
import com.token.mangowallet.utils.Constants;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface TokenLocalSource {
    Completable put(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo);

    Single<TokenInfo[]> fetch(NetworkInfo networkInfo, String walletAddress, Constants.WalletType walletType);
}
