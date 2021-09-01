package com.token.mangowallet.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.TransactionDetailsBean;
import com.token.mangowallet.bean.TransactionRecordBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;

import java.math.BigDecimal;
import java.util.Map;

import io.reactivex.Single;

public class TransactionViewModel extends BaseViewModel {

    private final MutableLiveData<MangoWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<TransactionRecordBean> transactionRecordBean = new MutableLiveData<>();
    private final MutableLiveData<TransactionBean> transactionBean = new MutableLiveData<>();
    private final MutableLiveData<TransactionDetailsBean> transactionDetailsBean = new MutableLiveData<>();
    private final MutableLiveData<BigDecimal> balance = new MutableLiveData<>();
    private final MutableLiveData<TableRowsBean> tableRowsBean = new MutableLiveData<>();

    private final FetchWalletInteract fetchWalletInteract;
    private final EMWalletRepository emWalletRepository;

    public TransactionViewModel(FetchWalletInteract fetchWalletInteract, EMWalletRepository emWalletRepository) {
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

    private void onDefaultWallet(MangoWallet wallet) {
        progress.postValue(false);
        defaultWallet.setValue(wallet);
    }

    public void fetchActions(Map<String, Object> pamars) {
        progress.postValue(true);
        emWalletRepository.fetchActions(pamars,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTransactionRecord, this::onError);
    }

    public void sendTransaction(String action, String code, String jsonData) {
        progress.postValue(true);
        emWalletRepository.sendTransaction(action, defaultWallet.getValue().getPrivateKey(),
                defaultWallet.getValue().getWalletAddress(), code, jsonData,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTransaction, this::onError);
    }

    public void fetchTransactionDetail(String id) {
        progress.postValue(true);
        emWalletRepository.fetchTransactionDetails(id,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTransactionDetail, this::onError);
    }

    public void fetchBalance() {
        progress.postValue(true);
        emWalletRepository.fetchBalance(defaultWallet.getValue().getWalletAddress(),
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onBalance, this::onError);
    }

    public void fetchTableRows(Map<String, Object> pamars) {
        progress.postValue(true);
        emWalletRepository.fetchTableRows(pamars,
                Constants.WalletType.getPagerFromPositon(defaultWallet.getValue().getWalletType()))
                .subscribe(this::onTableRows, this::onError);
    }

    private void onTransactionRecord(TransactionRecordBean transactionRecordBean) {
        progress.postValue(false);
        this.transactionRecordBean.postValue(transactionRecordBean);
    }

    private void onTransaction(TransactionBean transactionBean) {
        progress.postValue(false);
        this.transactionBean.postValue(transactionBean);
    }

    private void onTransactionDetail(TransactionDetailsBean transactionDetailsBean) {
        progress.postValue(false);
        this.transactionDetailsBean.postValue(transactionDetailsBean);
    }

    private void onBalance(BigDecimal balance) {
        progress.postValue(false);
        this.balance.postValue(balance);
    }

    private void onTableRows(TableRowsBean tableRowsBean) {
        progress.postValue(false);
        this.tableRowsBean.postValue(tableRowsBean);
    }

    public LiveData<MangoWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<TransactionRecordBean> transactionRecord() {
        return transactionRecordBean;
    }

    public LiveData<TransactionBean> transaction() {
        return transactionBean;
    }

    public LiveData<TransactionDetailsBean> transactionDetail() {
        return transactionDetailsBean;
    }

    public LiveData<BigDecimal> balance() {
        return balance;
    }

    public LiveData<TableRowsBean> tableRows() {
        return tableRowsBean;
    }
}
