package com.token.mangowallet.repository;


import com.token.mangowallet.entity.Token;
import com.token.mangowallet.utils.Constants;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface TokenRepositoryType {

    Observable<Token[]> fetch(String walletAddress, Constants.WalletType walletType);

    Completable addToken(String walletAddress, String address, String symbol, int decimals, Constants.WalletType walletType);

}
