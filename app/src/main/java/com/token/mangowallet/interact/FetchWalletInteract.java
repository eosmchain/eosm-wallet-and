package com.token.mangowallet.interact;

import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class FetchWalletInteract {


    public FetchWalletInteract() {
    }

    public Single<List<MangoWallet>> fetch() {


        return Single.fromCallable(() -> {
            return WalletDaoUtils.loadAll();
        }).observeOn(AndroidSchedulers.mainThread());

    }

    public Single<MangoWallet> findDefault() {

        return Single.fromCallable(() -> {
            return WalletDaoUtils.getCurrentWallet();
        });

    }
}
