package com.token.mangowallet.base;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.MyApplication.getContext;


public abstract class BaseActivity extends QMUIActivity {
    private Unbinder unbinder = null;

    private QMUITipDialog loadingDialog;
    private QMUITipDialog mTipDialog;

    public abstract int getContentView();

    public abstract void initParam();

    public abstract void initData();

    public abstract void initView();

    /**
     * 设置是否显示状态栏
     */
    public abstract boolean isShowState();

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Intent onLastActivityFinish() {
        return new Intent(this, MainActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        initParam();
        setContentView(getContentView());
        unbinder = ButterKnife.bind(this);
        //页面数据初始化方法
        initData();
        initView();
    }


    public View getEmptyView(int mipmap, String tip) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_empty, null, false);
        AppCompatImageView mIv = view.findViewById(R.id.iv_empty);
        mIv.setImageResource(mipmap);
        AppCompatTextView mTvTip = view.findViewById(R.id.tv_tip);
        mTvTip.setText(tip);
        return view;
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) { //check if dialog is showing.
                loadingDialog.dismiss();
            }
        }
        loadingDialog = null;
    }

    public void showLoading(String msg) {
        if (null == loadingDialog) {
            loadingDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg)
                    .create();
        }
        if (ActivityUtils.isActivityAlive(((ContextWrapper) loadingDialog.getContext()).getBaseContext()))
            loadingDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addTrace(String.valueOf(1), "onStart", getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        addTrace(String.valueOf(2), "onStop", getClass().getSimpleName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addTrace(String.valueOf(3), "onRestart", getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        addTrace(String.valueOf(4), "onPause", getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        //解除Messenger注册
        addTrace(String.valueOf(5), "onDestroy", getClass().getSimpleName());
    }


    private void addTrace(String ID, String NAME, String TYPE) {
        //上传事件
        if (MyApplication.mFirebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ID);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, NAME);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, TYPE);
            MyApplication.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }
    }
//    /**
//     * 创建ViewModel
//     *
//     * @param cls
//     * @param <T>
//     * @return
//     */
//    public <T extends ViewModel> T createViewModel(QMUIActivity activity, Class<T> cls) {
//        return new ViewModelProvider(activity).get(cls);
//    }
}
