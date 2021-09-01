package com.token.mangowallet.ui.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.token.mangowallet.interact.FetchWalletInteract;

public class StoreModelFactory implements ViewModelProvider.Factory {


    public StoreModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StoreViewModel(new FetchWalletInteract());
    }
}
