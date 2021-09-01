package com.token.mangowallet.ui.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MortgageModelFactory implements ViewModelProvider.Factory {


    public MortgageModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MortgageViewModel();
    }
}
