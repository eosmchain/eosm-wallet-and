package com.token.mangowallet.utils;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TimeUtil {
    public static Flowable<Long> newTime(LifecycleProvider lifecycleProvider) {
        return Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map((Function) o -> 59 - (Long) o);
    }

    public static Flowable<Long> newTime() {
        return Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map((Function) o -> 59 - (Long) o);
    }
}
