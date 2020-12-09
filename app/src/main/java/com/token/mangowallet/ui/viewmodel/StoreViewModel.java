package com.token.mangowallet.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;
import com.token.mangowallet.bean.StoreHomeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.RSAUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StoreViewModel extends BaseViewModel {
    private final MutableLiveData<MangoWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<StoreHomeBean> storeHomeData = new MutableLiveData<>();

    private final FetchWalletInteract fetchWalletInteract;

    public StoreViewModel(FetchWalletInteract fetchWalletInteract) {
        this.fetchWalletInteract = fetchWalletInteract;
    }

    public void prepare() {
        progress.postValue(true);
        disposable = fetchWalletInteract.findDefault()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    public void fetchStoreHome(int page) {
        progress.postValue(true);
        Map params = MapUtils.newHashMap();
        params.put("page", String.valueOf(1));
        params.put("limit", String.valueOf(20));
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            disposable = NetWorkManager.getRequest().getCategoryProduct(content)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(this::onStoreHome, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDefaultWallet(MangoWallet wallet) {
        defaultWallet.setValue(wallet);
        fetchStoreHome(1);
    }


    private void onStoreHome(StoreHomeBean data) {
        progress.postValue(false);
        this.storeHomeData.postValue(data);
    }

    public LiveData<MangoWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<StoreHomeBean> storeHomeData() {
        return storeHomeData;
    }
}