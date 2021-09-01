package com.token.mangowallet.interact;


import com.token.mangowallet.entity.Token;
import com.token.mangowallet.repository.TokenRepositoryType;
import com.token.mangowallet.utils.Constants;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchTokensInteract {

    private final TokenRepositoryType tokenRepository;


    public FetchTokensInteract(TokenRepositoryType tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Observable<Token[]> fetch(String walletAddress, Constants.WalletType walletType) {
        return tokenRepository.fetch(walletAddress, walletType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
