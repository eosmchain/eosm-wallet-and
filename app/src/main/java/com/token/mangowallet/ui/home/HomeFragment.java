package com.token.mangowallet.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.ErrorEnvelope;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.viewmodel.TokensViewModel;
import com.token.mangowallet.ui.viewmodel.TokensViewModelFactory;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.pager)
    QMUIViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;

    private static final int PERMISSIONS_REQUEST_CODE = 10;
    public static HomeFragment homeFragment;
    private Unbinder unbinder = null;
    public MangoWallet mangoWallet;
    private String[] mPermissionList;
    private TokensViewModelFactory tokensViewModelFactory;
    public TokensViewModel tokensViewModel;
    private HashMap<Pager, QMUIWindowInsetLayout> mPages;
    private WalletController walletController;
    private AppsController appsController;
    private MyController myController;
    private int mCurIndex = 0;
    private QMUIDialog msgQmuiDialog;
    public boolean isActivate = true;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
        unbinder = ButterKnife.bind(this, root);
        homeFragment = this;
        return root;
    }

    @Override
    protected void initView() {
        tokensViewModel.prepare();
        tokensViewModel.defaultWallet().observe(this, this::showWallet);
        tokensViewModel.tokens().observe(this, this::onTokens);
        tokensViewModel.prices().observe(this, this::onPrices);
        tokensViewModel.appHomeData().observe(this, this::onAppHomeData);
        tokensViewModel.error().observe(this, this::onError);
        initTabs();
        initPagers();

    }

    @Override
    protected void initData() {
//        SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_WALLET_ID, new Long(12));
        mangoWallet = WalletDaoUtils.getCurrentWallet();
        if (mangoWallet == null) {
            mangoWallet = WalletDaoUtils.getCurrentWallet(getActivity());
        }
        List<MangoWallet> mangoWalletList = WalletDaoUtils.loadAll();
        LogUtils.d(LOG_TAG, "MangoWallet = " + GsonUtils.toJson(mangoWalletList));
        SPUtils.getInstance(Constants.SP_MangoWallet_info).put(Constants.KEY_DIGICCY_SERVER, String.valueOf(Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType())));
        mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS};
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(getActivity(), mPermissionList, PERMISSIONS_REQUEST_CODE);
        }
        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this.getActivity(), tokensViewModelFactory)
                .get(TokensViewModel.class);
        showTipDialog(getString(R.string.str_loading));
    }

    @Override
    protected void initAction() {
    }

    private void onTokens(List<Token> tokens) {
        walletController.tokenItems = tokens;
        LogUtils.d("onTokens==", "tokens = " + new Gson().toJson(tokens));
        walletController.walletAdapter.setTokens(tokens);
//        walletController.updateView();
    }

    private void onAppHomeData(AppHomeBean appHomeBean) {
        dismissTipDialog();
        appsController.appHomeData(appHomeBean);
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void onPrices(CurrencyPrice currencyPrice) {
        LogUtils.d("onTokens==", "onPrices = " + new Gson().toJson(currencyPrice));
        BigDecimal sum = new BigDecimal(0);
        String price = "0";
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if (currencyPrice.getData() != null)
            bigDecimal = currencyPrice.getData().getPrice();
        if (ObjectUtils.isEmpty(bigDecimal)) {
            price = "0";
        } else {
            price = bigDecimal.toPlainString();
        }
        for (Token token : walletController.tokenItems) {
            if ((token.tokenInfo.symbol + "_USDT").equals(currencyPrice.getData() == null ? "" : currencyPrice.getData().getPair())) {//EOS_USDT
                if (token.balance == null) {
                    token.value = "0";
                    token.price = price;
                } else {
                    token.price = price;
                    token.value = BalanceUtils.ethToUsd(price, token.balance);
                }
            }
            if (!TextUtils.isEmpty(token.value)) {
                sum = sum.add(new BigDecimal(token.value));
            }
        }
        walletController.walletCardView.setTolalAssetValue(BalanceUtils.currencyToBase(sum.toPlainString(), 2, RoundingMode.FLOOR));
        walletController.walletAdapter.setTokens(walletController.tokenItems);
    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
        walletController.updateView();
    }

    private void initTabs() {
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.1f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 13), QMUIDisplayHelper.sp2px(getContext(), 15))
                .setDynamicChangeIconColor(true);
        QMUITab component = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.wallet_normal_icon))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.wallet_activated_icon))
                .setText(getString(R.string.str_wallet))
                .setColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_1), ContextCompat.getColor(getActivity(), R.color.app_color_orange))
                .build(getContext());
        QMUITab util = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.apps_normal_icon))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.apps_activated_icon))
                .setText(getString(R.string.str_apps))
                .setColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_1), ContextCompat.getColor(getActivity(), R.color.app_color_orange))
                .build(getContext());
        QMUITab lab = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.my_normal_icon))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.my_activated_icon))
                .setText(getString(R.string.str_my))
                .setColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_1), ContextCompat.getColor(getActivity(), R.color.app_color_orange))
                .build(getContext());

        mTabSegment.addTab(component)
                .addTab(util)
                .addTab(lab);

        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                String name = getString(R.string.str_wallet);
                if (index == 0) {
                    name = getString(R.string.str_wallet);
                } else if (index == 1) {
                    name = getString(R.string.str_apps);
                } else if (index == 2) {
                    name = getString(R.string.str_my);
                }
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(index));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, name);
                MyApplication.mFirebaseAnalytics.logEvent("TabSegment", bundle);
                mCurIndex = index;
                LogUtils.dTag(LOG_TAG, "mCurIndex = " + mCurIndex);
                if (index == 1) {//开始轮播
                    tokensViewModel.fetchAppHomeData("1");
                    appsController.banner.start();
                    if (ObjectUtils.isEmpty(mangoWallet.getWalletAddress())) {
                        if (msgQmuiDialog == null) {
                            msgQmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_warm_prompt),
                                    getString(R.string.str_no_account_tip),
                                    null,
                                    getString(R.string.str_ok), null, new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                        msgQmuiDialog.show();
                    }
                } else {//停止轮播
                    appsController.banner.stop();
                }
                if (index == 2) {
                    myController.fetchFindMgp();
                    myController.newCheckstatus();
                }

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }

    private void initPagers() {
        mPages = new HashMap<>();
        walletController = new WalletController(this);
//        walletController.setHomeControlListener(listener);
        mPages.put(Pager.WALLET, walletController);

        appsController = new AppsController(this);
//        homeComponentsController.setHomeControlListener(listener);
        mPages.put(Pager.APPS, appsController);

        myController = new MyController(this);
//        homeUtilController.setHomeControlListener(listener);
        mPages.put(Pager.MY, myController);

        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            QMUIWindowInsetLayout page = mPages.get(Pager.getPagerFromPositon(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page, params);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };

    enum Pager {
        WALLET, APPS, MY;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return WALLET;
                case 1:
                    return APPS;
                case 2:
                    return MY;
                default:
                    return WALLET;
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : mPermissionList) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (allPermissionsGranted()) {
            } else {
                ToastUtils.showLong("Permissions not granted by the user.");
            }
        }
    }

    @BusUtils.Bus(tag = BUS_TO_WALLET)
    public void busToWallet(ToWallet toWallet) {
        LogUtils.dTag(LOG_TAG, "busToWallet = " + GsonUtils.toJson(toWallet));
        if (!ObjectUtils.isEmpty(toWallet)) {
            int type = toWallet.getType();
            this.mangoWallet = toWallet.getWallet();
            WalletDaoUtils.updateCurrent(mangoWallet);
            tokensViewModel.updateDefaultWallet(mangoWallet);
            walletController.updateView();
            userLogin();
        }
    }

    /**
     *
     */
    private void userLogin() {
        try {
            NetWorkManager.getRequest().userLogin()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::userLoginSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userLoginSuccess(JsonObject jsonObject) {

    }

    private void jumpToVerifyWallet(AccountInfo accountInfo) {
        isActivate = true;
    }

    public void verifyWalletError(Throwable errorInfo) {
        isActivate = false;
    }

    private void onError(Throwable e) {

    }

    @Override
    public void onStart() {
        super.onStart();
        BusUtils.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        tokensViewModel.prepare();
        if (tokensViewModel.getVerifyWallet(mangoWallet) != null) {
            tokensViewModel.getVerifyWallet(mangoWallet).subscribe(this::jumpToVerifyWallet, this::verifyWalletError);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusUtils.unregister(this);
        walletController.release();
        appsController.release();
        myController.release();
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }

    @Override
    public Object onLastFragmentFinish() {
        return null;
    }
}