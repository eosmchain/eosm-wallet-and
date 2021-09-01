package com.token.mangowallet.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;

import java.util.Map;

import io.reactivex.Single;

public class ResManViewModel extends BaseViewModel {

    private final MutableLiveData<MangoWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<AccountInfo> accountInfo = new MutableLiveData<>();
    private final MutableLiveData<TransactionBean> transactionBean = new MutableLiveData<>();
    private final MutableLiveData<TableRowsBean> tableRowsBean = new MutableLiveData<>();

    private final FetchWalletInteract fetchWalletInteract;
    private final EMWalletRepository emWalletRepository;

    public ResManViewModel(FetchWalletInteract fetchWalletInteract,
                           EMWalletRepository emWalletRepository) {
        this.fetchWalletInteract = fetchWalletInteract;
        this.emWalletRepository = emWalletRepository;

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

    public void fetchAccountInfo() {
        progress.postValue(true);
        disposable = emWalletRepository.fetchAccountInfo(defaultWallet.getValue().getWalletAddress(),
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onAccountInfo, this::onError);
    }

    public void sendTransaction(String action, String code, String jsonData) {
        progress.postValue(true);
        emWalletRepository.sendTransaction(action, defaultWallet.getValue().getPrivateKey(),
                defaultWallet.getValue().getWalletAddress(), code, jsonData,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTransaction, this::onError);
    }

    public void fetchTableRows(Map<String, Object> pamars) {
        progress.postValue(true);
        emWalletRepository.fetchTableRows(pamars,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTableRows, this::onError);
    }

    private void onDefaultWallet(MangoWallet wallet) {
        defaultWallet.setValue(wallet);
        fetchAccountInfo();
    }

    private void onAccountInfo(AccountInfo accountInfo) {
        progress.postValue(false);
        this.accountInfo.postValue(accountInfo);
    }

    private void onTransaction(TransactionBean transactionBean) {
        progress.postValue(false);
        this.transactionBean.postValue(transactionBean);
    }

    private void onTableRows(TableRowsBean tableRowsBean) {
        progress.postValue(false);
        this.tableRowsBean.postValue(tableRowsBean);
    }

    public LiveData<MangoWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<AccountInfo> accountInfo() {
        return accountInfo;
    }

    public LiveData<TransactionBean> transaction() {
        return transactionBean;
    }

    public LiveData<TableRowsBean> tableRows() {
        return tableRowsBean;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
