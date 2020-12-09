package com.token.mangowallet.net.common;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.token.mangowallet.R;
import com.token.mangowallet.net.exception.ApiException;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class RxSubscriber<T> implements Observer<T> {

    //    private LoadingDialog mLoadingDialog;
    private QMUITipDialog mLoadingDialog;
    private Disposable disposable;
    private Activity context;
    private String errorMsg;
    private boolean mShowLoading = false;

    public RxSubscriber(Activity context, boolean showLoading) {
        this.context = context;
        this.mShowLoading = showLoading;
    }

    public RxSubscriber() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = new CompositeDisposable();
        if (mShowLoading) {
            showLoading();
        }
    }


    @Override
    public void onNext(T t) {
        if (mShowLoading) {
            dismissLoading();
        }
        if (!isInvalidContext()) {
            if (t == null || ObjectUtils.isEmpty(t)) {
                onFail("报null");
            } else {
                onSuccess(t);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            /** 没有网络 */
            errorMsg = "Please check your network status";
        } else if (e instanceof HttpException) {
            /** 网络异常，http 请求失败，即 http 状态码不在 [200, 300) 之间, such as: "server internal error". */
            errorMsg = "errorBody:" + ((HttpException) e).response().errorBody()
                    + ";message:" + ((HttpException) e).response().message()
                    + ";code:" + ((HttpException) e).response().code();
        } else if (e instanceof ApiException) {
            /** 网络正常，http 请求成功，服务器返回逻辑错误 */
            errorMsg = ((ApiException) e).getDisplayMessage();
        } else {
            /** 其他未知错误 */
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "unknown error";
        }
        if (mShowLoading) {
            dismissLoading();
        }
       /* if (BuildConfig.DEBUG && !ObjectUtils.isEmpty(context)) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
            localBuilder.setTitle("提示");
            localBuilder.setMessage(errorMsg);
            localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int paramAnonymousInt) {
                    dialogInterface.dismiss();
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
            });
            localBuilder.setCancelable(false).create();
            localBuilder.show();
        }*/
        if (!isInvalidContext()) {
            onFail(errorMsg);
        }
    }


    public abstract void onFail(String failMsg);

    @Override
    public void onComplete() {
        if (mShowLoading) {
            dismissLoading();
        }
        Log.d("print", "-->执行了完成的方法");
    }

    public abstract void onSuccess(T t);


    private void showLoading() {
        try {
            if (!isInvalidContext()) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new QMUITipDialog.Builder(context)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord(StringUtils.getString(R.string.str_loading))
                            .create();
                }
                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissLoading() {
        try {
            if (!isInvalidContext()) {
                if (mLoadingDialog != null) {
                    if (mLoadingDialog.isShowing()) {
                        mLoadingDialog.dismiss();
                    }
                }
            }
            mLoadingDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInvalidContext() {
        return (context.isDestroyed() || context.isFinishing());
    }
}
