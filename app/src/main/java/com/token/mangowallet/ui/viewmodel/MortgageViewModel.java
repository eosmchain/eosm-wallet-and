package com.token.mangowallet.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.google.gson.JsonObject;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.OrderIndexBean;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;

import java.math.BigDecimal;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MortgageViewModel extends BaseViewModel {

    public final MutableLiveData<MangoWallet> defaultWallet = new MutableLiveData<>();
    public final MutableLiveData<BigDecimal> balance = new MutableLiveData<>();
    public final MutableLiveData<TableRowsBean> tableRowsBean = new MutableLiveData<>();
    public final MutableLiveData<OrderIndexBean> orderIndexBean = new MutableLiveData<>();
    public final MutableLiveData<CurrencyPrice> prices = new MutableLiveData<>();
    public final MutableLiveData<TransactionBean> transactionBean = new MutableLiveData<>();
    public final MutableLiveData<String> blendChannelModel = new MutableLiveData<>();

    public final FetchWalletInteract fetchWalletInteract;
    public final EMWalletRepository emWalletRepository;

    public MortgageViewModel() {
        this.fetchWalletInteract = new FetchWalletInteract();
        this.emWalletRepository = new EMWalletRepository();

    }

    public void prepare() {
        progress.postValue(true);
        disposable = fetchWalletInteract.findDefault()
                .subscribe(this::onDefaultWallet, this::onError);

    }

    public void prepare(MangoWallet wallet) {
        disposable = Single.fromCallable(() -> {
            return wallet;
        }).subscribe(this::onDefaultWallet);
    }

    public void fetchBalance() {
        progress.postValue(true);
        emWalletRepository.fetchBalance(defaultWallet.getValue().getWalletAddress(),
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onBalance, this::onError);
    }

    public void fetchBalance(String walletAddress) {
        progress.postValue(true);
        emWalletRepository.fetchBalance(walletAddress,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onBalance, this::onError);
    }

    public void fetchTableRows(Map<String, Object> pamars) {
        progress.postValue(true);
        emWalletRepository.fetchTableRows(pamars,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTableRows, this::onError);
    }

    public void fetchOrderIndex(String content) {
        progress.postValue(true);
        NetWorkManager.getRequest().getOrderIndex(content).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::onOrderIndex, this::onError);
    }

    public void getTicker() {
        try {
            progress.postValue(true);
            Map map = MapUtils.newHashMap();
            map.put("pair", Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()) + "_USDT");
            String json = GsonUtils.toJson(map);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCoinPrice(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPrice, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBlendChannel() {
        try {
            progress.postValue(true);
            NetWorkManager.getRequest().blendChannel()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBlendChannel, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDefaultWallet(MangoWallet wallet) {
        defaultWallet.setValue(wallet);
        fetchBalance();
    }

    public void onBalance(BigDecimal balance) {
        progress.postValue(false);
        this.balance.postValue(balance);
    }

    public void onTableRows(TableRowsBean tableRowsBean) {
        progress.postValue(false);
        this.tableRowsBean.postValue(tableRowsBean);
    }

    public void onOrderIndex(OrderIndexBean orderIndexBean) {
        progress.postValue(false);
        this.orderIndexBean.postValue(orderIndexBean);
    }

    public void onPrice(CurrencyPrice currencyPrice) {
        progress.postValue(false);
        LogUtils.d("Tokens", "price: " + currencyPrice.getData().getPair() + "  " + currencyPrice.getData().getPrice());
        this.prices.postValue(currencyPrice);
    }

    public void onBlendChannel(JsonObject jsonObject) {
        progress.postValue(false);
        this.blendChannelModel.postValue(GsonUtils.toJson(jsonObject));
    }

    public void sendTransaction(String action, String code, String jsonData) {
        progress.postValue(true);
        emWalletRepository.sendTransaction(action, defaultWallet.getValue().getPrivateKey(),
                defaultWallet.getValue().getWalletAddress(), code, jsonData,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTransaction, this::onError);
    }

    public void onTransaction(TransactionBean transactionBean) {
        progress.postValue(false);
        this.transactionBean.postValue(transactionBean);
    }

    public LiveData<MangoWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<BigDecimal> balance() {
        return balance;
    }

    public LiveData<TableRowsBean> tableRows() {
        return tableRowsBean;
    }

    public LiveData<OrderIndexBean> orderIndex() {
        return orderIndexBean;
    }

    public LiveData<CurrencyPrice> prices() {
        return prices;
    }

    public LiveData<String> blendChannelModel() {
        return blendChannelModel;
    }

    public LiveData<TransactionBean> transaction() {
        return transactionBean;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
