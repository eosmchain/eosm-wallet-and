package com.token.mangowallet.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.bean.CountryBean;
import com.token.mangowallet.bean.FindBean;
import com.token.mangowallet.bean.MerchantBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.ServerInfo;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.activity.LanguageActivity;
import com.token.mangowallet.ui.activity.WebActivity;
import com.token.mangowallet.ui.activity.pinyin.CountryCodeActivity;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.token.mangowallet.ui.home.HomeFragment.homeFragment;
import static com.token.mangowallet.utils.Constants.EXTRA_TO_URL;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.GUOYU_URL;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class MyController extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;
    @BindView(R.id.appVersionTv)
    AppCompatTextView appVersionTv;

    private Unbinder unbinder;
    private HomeFragment baseFragment;
    private QMUICommonListItemView walletItem;
    private QMUICommonListItemView chainstoreItem;
    private QMUICommonListItemView myInvitationCodeItem;
    private QMUICommonListItemView mgpStimulateItem;
    private String walletAddress;
    private QMUIBottomSheet bottomSheet;
    private QMUICommonListItemView bindEthAddressItem;
    private int bindCode = -1;
    private FindBean.DataBean dataBean;
    private EMWalletRepository emWalletRepository;

    public MyController(BaseFragment baseFragment) {
        super(baseFragment.getActivity());
        this.baseFragment = (HomeFragment) baseFragment;
        LayoutInflater.from(baseFragment.getActivity()).inflate(R.layout.fragment_dashboard, this);
        unbinder = ButterKnife.bind(this);
        initData();
        initView();
    }

    QMUIDialog TSqmuiDialog;
    AppCompatEditText editt1;
    AppCompatEditText editt2;

    private void initView() {
        topbar.setTitle(R.string.str_my);
        appVersionTv.setVisibility(VISIBLE);
        appVersionTv.setText("v" + AppUtils.getAppVersionName());
        emWalletRepository = new EMWalletRepository();
        appVersionTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
//                baseFragment.startFragment("OTCDealFragment", bundle);
//                getTradeOrder("");
            }
        });
    }

    /**
     * 获取交易订单
     */
    private void getTradeOrder(String order_sn) {
        try {
//            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("scope", "dex.oo2");
            mapTableRows.put("code", "dex.oo2");
            mapTableRows.put("table", "order");
            mapTableRows.put("index_position", "5");
            mapTableRows.put("key_type", "i256");
            mapTableRows.put("lower_bound", baseFragment.mangoWallet.getWalletAddress());
            mapTableRows.put("upper_bound", baseFragment.mangoWallet.getWalletAddress());
            mapTableRows.put("reverse", false);
            mapTableRows.put("show_payer", false);
            emWalletRepository.fetchTableRowsStr(mapTableRows, Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType()))
                    .subscribe(this::onTradeOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTradeOrderSuccess(Object o) {
        baseFragment.dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "o = " + GsonUtils.toJson(o));
    }

    private void onError(Object e) {
        baseFragment.dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    private void initData() {
        initGroupListView();
        fetchFindMgp();
        newCheckstatus();
    }

    private void initGroupListView() {
        int height = QMUIDisplayHelper.dp2px(getContext(), 80);
        walletAddress = Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType()) + "_Wallet";
        if (!ObjectUtils.isEmpty(baseFragment.mangoWallet)) {
            walletAddress = ObjectUtils.isEmpty(baseFragment.mangoWallet.getWalletAddress()) ? walletAddress : baseFragment.mangoWallet.getWalletAddress();
        }
        walletItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_mgp),
                walletAddress,
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.AUTOFILL_TYPE_NONE, height);
        walletItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        walletItem.getTextView().setSingleLine(true);
        walletItem.getTextView().setEllipsize(TextUtils.TruncateAt.MIDDLE);
        ///////////////////////////////////////////////

        QMUICommonListItemView walletAdminItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_wallet_address),
                getString(R.string.str_wallet_manage),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        walletAdminItem.setOrientation(QMUICommonListItemView.HORIZONTAL);

        chainstoreItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_chainstore),
                getString(R.string.str_chain_business_center),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        chainstoreItem.setOrientation(QMUICommonListItemView.HORIZONTAL);

        myInvitationCodeItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_invitation_code),
                getString(R.string.str_invitation_code),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        myInvitationCodeItem.setOrientation(QMUICommonListItemView.HORIZONTAL);

        ////////////////////////////////////////////////////////////
        mgpStimulateItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_mgp_stimulate),
                getString(R.string.str_mgp_stimulate),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        mgpStimulateItem.setOrientation(QMUICommonListItemView.VERTICAL);

        bindEthAddressItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.bind_wallet_icon),
                getString(R.string.str_bind_eth_address),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        bindEthAddressItem.setOrientation(QMUICommonListItemView.HORIZONTAL);

        QMUICommonListItemView languageSettingsItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_language_settings),
                getString(R.string.str_language_settings),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        languageSettingsItem.setOrientation(QMUICommonListItemView.VERTICAL);
//        languageSettingsItem.setVisibility(GONE);

        QMUICommonListItemView currencySettingsItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_currency_settings),
                getString(R.string.str_currency_settings),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        currencySettingsItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView helpCenterItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_help_center),
                getString(R.string.str_help_center),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        helpCenterItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView aboutUsItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_aboutus),
                getString(R.string.str_about_us),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        aboutUsItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView testItem = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_aboutus),
                "测试test",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        testItem.setOrientation(QMUICommonListItemView.VERTICAL);
        testItem.setVisibility(GONE);


        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle;
                Intent intent;
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    if (ObjectUtils.equals(text, walletAddress)) {
//                        bundle = new Bundle();
//                        bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
//                        baseFragment.startFragment("ChainStoreFragment", bundle);
                    } else if (ObjectUtils.equals(getString(R.string.str_wallet_manage), text)) {
                        homeFragment.startFragment("WalletListFragment");
                    } else if (ObjectUtils.equals(getString(R.string.str_chain_business_center), text)) {
                        bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                        baseFragment.startFragment("ChainStoreFragment", bundle);
                    } else if (ObjectUtils.equals(getString(R.string.str_invitation_code), text)) {
                        bundle = new Bundle();
                        if (ObjectUtils.isNotEmpty(MyApplication.getInstance().mMid)) {
                            bundle.putParcelable("DataBean", dataBean);
                            homeFragment.startFragment("InvitationFragment", bundle);
                        } else {
                            bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                            homeFragment.startFragment("ActivateMidFragment", bundle);
                        }
                    } else if (ObjectUtils.equals(getString(R.string.str_mgp_stimulate), text)) {
                        bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                        baseFragment.startFragment("MyStimulateFragment", bundle);
                    } else if (ObjectUtils.equals(getString(R.string.str_language_settings), text)) {
//                        baseFragment.startFragment("LanguageFragment");
                        intent = new Intent(baseFragment.getActivity(), LanguageActivity.class);
                        baseFragment.startActivity(intent);
                    } else if (ObjectUtils.equals(getString(R.string.str_currency_settings), text)) {
                        baseFragment.startFragment("CurrencySetupFragmeng");
                    } else if (ObjectUtils.equals(getString(R.string.str_help_center), text)) {
                        String aboutus_url = "https://mangochain.io";
                        intent = new Intent(baseFragment.getActivity(), WebActivity.class);
                        intent.putExtra(EXTRA_TO_URL, aboutus_url);
                        baseFragment.startActivity(intent);
                    } else if (ObjectUtils.equals(getString(R.string.str_about_us), text)) {
                        String aboutus_url = "https://mangochain.io";
                        intent = new Intent(baseFragment.getActivity(), WebActivity.class);
                        intent.putExtra(EXTRA_TO_URL, aboutus_url);
                        baseFragment.startActivity(intent);
                    } else if (ObjectUtils.equals(getString(R.string.str_bind_eth_address), text)) {
                        bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                        //备注: 0绑定成功 1绑定中 2 需绑定 3 错误信息(直接弹出)  5暂未开启
                        if (bindCode == 2)
                            baseFragment.startFragment("BindETHFragment", bundle);
                    } else if (ObjectUtils.equals("测试test", text)) {
                        baseFragment.startFragment("TestFragment");
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 35);
        int iconSize = QMUIDisplayHelper.dp2px(getContext(), 20);
        int separator = QMUIDisplayHelper.dp2px(getContext(), 2);
        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(walletItem, onClickListener)
                .setMiddleSeparatorInset(separator, 0)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(iconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(walletAdminItem, onClickListener)
                .setMiddleSeparatorInset(separator, 0)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(iconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(chainstoreItem, onClickListener)
                .addItemView(bindEthAddressItem, onClickListener)
                .setMiddleSeparatorInset(separator, 0)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(iconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(myInvitationCodeItem, onClickListener)
                .addItemView(mgpStimulateItem, onClickListener)
                .setMiddleSeparatorInset(separator, 0)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(iconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(languageSettingsItem, onClickListener)
                .addItemView(currencySettingsItem, onClickListener)
                .setMiddleSeparatorInset(separator, 0)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(iconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(helpCenterItem, onClickListener)
                .addItemView(aboutUsItem, onClickListener)
                .addItemView(testItem, onClickListener)
                .setMiddleSeparatorInset(separator, 0)
                .addTo(groupListView);
    }

    public void fetchFindMgp() {
        walletItem.setText(baseFragment.mangoWallet.getWalletAddress());
        baseFragment.showTipDialog(baseFragment.getString(R.string.str_loading));
        Map map = MapUtils.newHashMap();
        map.put("mgpName", baseFragment.mangoWallet.getWalletAddress());
        String jsonData2 = GsonUtils.toJson(map);
        try {
            String content = NRSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().getFindMgp(content)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFindBeanData, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newCheckstatus() {
        baseFragment.showTipDialog(baseFragment.getString(R.string.str_loading));
        try {
            Map params = MapUtils.newHashMap();
            params.put("currentAddr", baseFragment.mangoWallet.getWalletAddress());
            params.put("type", "2");
            String jsonData2 = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().newCheckstatus(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::newCheckstatusSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFindBeanData(FindBean findBean) {
        baseFragment.dismissTipDialog();
        if (findBean != null) {
            if (findBean.getCode() == 0) {
                dataBean = findBean.getData();
                MyApplication.getInstance().mMid = dataBean.getLnvitationCode();
            } else {
                MyApplication.getInstance().mMid = "";
            }
        }
        myInvitationCodeItem.setDetailText(MyApplication.getInstance().mMid);
        myInvitationCodeItem.getDetailTextView().setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_red));
        myInvitationCodeItem.setVisibility(ObjectUtils.isEmpty(homeFragment.mangoWallet.getWalletAddress()) ? GONE : VISIBLE);
        chainstoreItem.setVisibility(ObjectUtils.isEmpty(MyApplication.getInstance().mMid) ? GONE : VISIBLE);
    }

    private void newCheckstatusSuccess(JsonObject jsonData) {
        baseFragment.dismissTipDialog();
        if (!ObjectUtils.isEmpty(jsonData)) {
            MerchantBean merchantBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MerchantBean.class);
            if (merchantBean != null) {
                bindCode = merchantBean.getData();
                String msg = merchantBean.getMsg();
                bindEthAddressItem.setVisibility(View.VISIBLE);
                if (bindCode == 3) {
                    ToastUtils.showLong(merchantBean.getMsg());
                } else if (bindCode == 5) {
                    bindEthAddressItem.setVisibility(View.GONE);
                }
                if (msg.contains("0x")) {
                    String s1 = msg.substring(0, 5);
                    String s2 = msg.substring(msg.length() - 5, msg.length());
                    msg = s1 + "..." + s2;
                }
                bindEthAddressItem.setDetailText(msg);
            }
        }
    }

    private void onError(Throwable e) {
        baseFragment.dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    public void release() {
    }
}