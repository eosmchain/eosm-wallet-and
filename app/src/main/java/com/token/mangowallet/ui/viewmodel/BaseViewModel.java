package com.token.mangowallet.ui.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.LogUtils;
import com.token.mangowallet.entity.ErrorEnvelope;
import com.token.mangowallet.entity.ServiceException;
import com.token.mangowallet.utils.Constants;

import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    protected final MutableLiveData<ErrorEnvelope> error = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> progress = new MutableLiveData<>();
    protected Disposable disposable;

    @Override
    protected void onCleared() {
        cancel();
    }

    protected void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public LiveData<ErrorEnvelope> error() {
        return error;
    }

    public LiveData<Boolean> progress() {
        return progress;
    }

    public void onError(Throwable throwable) {
        if (throwable instanceof ServiceException) {
            error.postValue(((ServiceException) throwable).error);
        } else {
            error.postValue(new ErrorEnvelope(Constants.ErrorCode.UNKNOWN, null, throwable));
            // TODO: Add dialog with offer send error log to developers: notify about error.
            LogUtils.d("SESSION", "Err", throwable);
        }
    }
}
