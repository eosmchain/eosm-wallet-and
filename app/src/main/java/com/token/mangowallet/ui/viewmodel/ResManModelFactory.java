package com.token.mangowallet.ui.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.token.mangowallet.interact.FetchWalletInteract;
import com.token.mangowallet.repository.EMWalletRepository;

public class ResManModelFactory implements ViewModelProvider.Factory {


    public ResManModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResManViewModel(
                new FetchWalletInteract(),
                new EMWalletRepository());
    }
}
