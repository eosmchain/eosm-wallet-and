package com.token.mangowallet.ui.viewmodel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.NetworkInfo;
import com.token.mangowallet.entity.Ticker;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.entity.TokenInfo;
import com.token.mangowallet.interact.FetchTokensInteract;
import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.net.eth.service.TickerService;
import com.token.mangowallet.net.eth.service.UpWalletTickerService;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.repository.EthereumNetworkRepository;
import com.token.mangowallet.repository.TokenRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.RSAUtils;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;


public class TokensViewModel extends BaseViewModel {
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<MangoWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<CurrencyPrice> prices = new MutableLiveData<>();
    private final MutableLiveData<List<Token>> tokens = new MutableLiveData<>();
    private final MutableLiveData<TransactionBean> transactionBean = new MutableLiveData<>();
    private final MutableLiveData<TableRowsBean> tableRowsBean = new MutableLiveData<>();
    private final MutableLiveData<AppHomeBean> appHomeData = new MutableLiveData<>();
    private final EthereumNetworkRepository ethereumNetworkRepository;
    private final FetchWalletInteract findDefaultWalletInteract;

    private TokenRepository tokenRepository;
    private final EMWalletRepository emWalletRepository;

    TokensViewModel(EthereumNetworkRepository ethereumNetworkRepository,
                    FetchWalletInteract findDefaultWalletInteract,
                    EMWalletRepository emWalletRepository,
                    TokenRepository tokenRepository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.tokenRepository = tokenRepository;
        this.emWalletRepository = emWalletRepository;
    }

    public void prepare() {
        progress.postValue(true);
        defaultNetwork.postValue(ethereumNetworkRepository.getDefaultNetwork());
        disposable = findDefaultWalletInteract.findDefault()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    public void updateDefaultWallet(MangoWallet wallet) {
        Single.fromCallable(() -> {
            return wallet;
        }).subscribe(this::onDefaultWallet);
    }

    private void onDefaultWallet(MangoWallet wallet) {
        defaultWallet.setValue(wallet);
        if (wallet != null) {
            List<String> tokens = wallet.getTokens();
            String json = tokens.toString();
            LogUtils.dTag("GsonUtils==", "tokens = " + json);
            List<Token> tokenItems = GsonUtils.fromJson(json, new TypeToken<List<Token>>() {
            }.getType());
            this.tokens.postValue(tokenItems);
            fetchTokens(tokenItems);
            fetchAppHomeData("1");
        }
    }

    public void fetchTokens(List<Token> tokenList) {
        progress.postValue(true);
        disposable = fetchBalance(defaultWallet.getValue().getWalletAddress(), tokenList)
                .subscribe(this::onTokens, this::onError);
    }

    public void fetchAppHomeData(String type) {
        progress.postValue(true);
        Map map = MapUtils.newHashMap();
        map.put("address", defaultWallet.getValue().getWalletAddress());
        map.put("type", type);
        String jsonData2 = GsonUtils.toJson(map);
        try {
            String content = RSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().getHome(content)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(this::onAppHomeData, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<List<Token>> fetchBalance(String walletAddress, List<Token> tokens) {
        return Observable.fromCallable(() -> getToken(walletAddress, tokens)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Token> getToken(String walletAddress, List<Token> tokens) throws Exception {
        for (Token token : tokens) {
            BigDecimal balance = null;
            String balanceStr = null;
            TokenInfo tokenInfo = token.tokenInfo;
            Constants.WalletType walletType = tokenInfo.walletType;
            if (tokenInfo.address.isEmpty()) {
                if (walletType == MGP || walletType == EOS) {
                    balance = emWalletRepository.getEOSMGPBalance(walletAddress, walletType);
                } else if (walletType == ETH || walletType == BTC) {
                    balance = tokenRepository.getEthBalance(walletAddress);
                }
            } else {
                if (walletType == MGP || walletType == EOS) {
                    balance = emWalletRepository.getEOSMGPBalance(walletAddress, walletType);
                } else if (walletType == ETH) {
                    balance = tokenRepository.getBalance(walletAddress, tokenInfo);
                }

            }

            LogUtils.d("balance:" + balance);
            if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) {
                token.balance = "0";
            } else {
                if (walletType == MGP || walletType == EOS) {
                    token.balance = balance.setScale(tokenInfo.decimals, RoundingMode.CEILING).toPlainString();
                } else {
                    BigDecimal decimalDivisor = new BigDecimal(Math.pow(10, tokenInfo.decimals));
                    BigDecimal ethBalance = balance.divide(decimalDivisor);
                    if (tokenInfo.decimals > 4) {
                        token.balance = ethBalance.setScale(4, RoundingMode.CEILING).toPlainString();
                    } else {
                        token.balance = ethBalance.setScale(tokenInfo.decimals, RoundingMode.CEILING).toPlainString();
                    }
                }
            }
        }
        return tokens;
    }

    public Single<CurrencyPrice> getTicker(String content) {
        return Single.fromObservable(
                NetWorkManager.getRequest().getCoinPrice(content).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()));
    }

    private void onTokens(List<Token> tokens) {
        try {
            LogUtils.d("Tokens", "onTokens");
            progress.postValue(false);
            this.tokens.postValue(tokens);
            //  TODO： 是否出现重复调用
            for (Token token : tokens) {
                Map map = MapUtils.newHashMap();
                map.put("pair", token.tokenInfo.symbol + "_USDT");
                String json = GsonUtils.toJson(map);
                String content = RSAUtils.encrypt(json);
                getTicker(content).subscribe(this::onPrice, this::onError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrency() {
        return ethereumNetworkRepository.getCurrency();
    }

    private void onPrice(CurrencyPrice currencyPrice) {
        if (currencyPrice != null) {
            if (currencyPrice.getData() != null)
                LogUtils.d("Tokens", "price: " + currencyPrice.getData().getPair() + "  " + currencyPrice.getData().getPrice());
        }
        this.prices.postValue(currencyPrice);
    }

    private void onAppHomeData(AppHomeBean appHomeData) {
        progress.postValue(false);
        this.appHomeData.postValue(appHomeData);
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<MangoWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<AppHomeBean> appHomeData() {
        return appHomeData;
    }

    public LiveData<List<Token>> tokens() {
        return tokens;
    }

    public MutableLiveData<CurrencyPrice> prices() {
        return prices;
    }
}


