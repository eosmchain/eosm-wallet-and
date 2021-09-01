package com.token.mangowallet.net.eosmgp;

import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import one.block.eosiojavarpcprovider.error.EosioJavaRpcProviderInitializerError;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;

import static com.token.mangowallet.utils.Constants.WalletType.ALL;

public class EOSMGPExplorerService implements EOSMGPExplorerClientType {

    private EosioJavaRpcProviderImpl rpcProvider = null;

    public EOSMGPExplorerService() {

    }

    public void init() {
        String service = "";
        Constants.WalletType walletType = ALL;
        MangoWallet wallet = WalletDaoUtils.getCurrentWallet();
        if (wallet != null) {
            walletType = Constants.WalletType.getPagerFromPositon(wallet.getWalletType());
        }
        service = BaseUrlUtils.getInstance().getDIGICCYBaseUrl();
        buildApiClient(service);
    }

    public EosioJavaRpcProviderImpl buildApiClient(String baseUrl) {
        try {
            return rpcProvider = new EosioJavaRpcProviderImpl(baseUrl, Constants.ISTEST);
        } catch (EosioJavaRpcProviderInitializerError eosioJavaRpcProviderInitializerError) {
            eosioJavaRpcProviderInitializerError.printStackTrace();
        }
        return null;
    }

    public Observable<String> getCurrencyBalance(Map<String, Object> map) {
        return Observable.fromCallable(() -> rpcProvider.getCurrencyBalance(EOSParams.getRequestBody(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void getKeyAccounts(Map<String, Object> map) {
        Observable.fromCallable(() -> rpcProvider.getKeyAccounts(EOSParams.getRequestBody(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
