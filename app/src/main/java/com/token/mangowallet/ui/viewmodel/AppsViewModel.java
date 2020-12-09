package com.token.mangowallet.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.bean.FindBean;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.RSAUtils;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppsViewModel extends BaseViewModel {

    private final MutableLiveData<MangoWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<TransactionBean> transactionBean = new MutableLiveData<>();
    private final MutableLiveData<TableRowsBean> tableRowsBean = new MutableLiveData<>();
    private final MutableLiveData<AppHomeBean> appHomeData = new MutableLiveData<>();
    private final MutableLiveData<FindBean> findBeanData = new MutableLiveData<>();
    private final FetchWalletInteract fetchWalletInteract;
    private final EMWalletRepository emWalletRepository;

    public AppsViewModel(FetchWalletInteract fetchWalletInteract,
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

    public void fetchFindMgp() {
        progress.postValue(true);
        Map map = MapUtils.newHashMap();
        map.put("mgpName", defaultWallet.getValue().getWalletAddress());
        String jsonData2 = GsonUtils.toJson(map);
        try {
            String content = RSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().getFindMgp(content)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFindBeanData, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    private void onTransaction(TransactionBean transactionBean) {
        progress.postValue(false);
        this.transactionBean.postValue(transactionBean);
    }

    private void onTableRows(TableRowsBean tableRowsBean) {
        progress.postValue(false);
        this.tableRowsBean.postValue(tableRowsBean);
    }

    private void onAppHomeData(AppHomeBean appHomeData) {
        progress.postValue(false);
        this.appHomeData.postValue(appHomeData);
    }

    private void onFindBeanData(FindBean findBean) {
        progress.postValue(false);
        this.findBeanData.postValue(findBean);
    }

    public LiveData<MangoWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<TransactionBean> transaction() {
        return transactionBean;
    }

    public LiveData<TableRowsBean> tableRows() {
        return tableRowsBean;
    }

    public LiveData<AppHomeBean> appHomeData() {
        return appHomeData;
    }

    public LiveData<FindBean> findBeanData() {
        return findBeanData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
