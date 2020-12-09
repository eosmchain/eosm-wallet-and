package com.token.mangowallet.callback;

/**
 * @time 2019/6/14 11:28
 * @class 描述:通用的回调函数
 */
public interface CommonCallback<T> {
    void onSuccess(T data, String successMsg, int responseSuccessCode);

    void onFailure(String msg, int responseFailCode);
}
