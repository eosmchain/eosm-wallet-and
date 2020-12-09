package com.token.mangowallet.utils;

import io.reactivex.disposables.Disposable;

public class DisposUtil {
    public static void close(Disposable disposable){
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
            disposable=null;
        }
    }
}
