package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.BaseBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;

public class ConfirmMnemonicFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topBar;
    @BindView(R.id.text1)
    AppCompatTextView text1;
    @BindView(R.id.mnemonicTextLayout)
    QMUIFloatLayout mnemonicTextLayout;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.mnemonicTagLayout)
    QMUIFloatLayout mnemonicTagLayout;
    @BindView(R.id.completeBtn)
    AppCompatButton completeBtn;

    private Unbinder unbinder;
    private MangoWallet wallet;
    private List<String> shuffleList = new ArrayList<>();
    private List<String> confirmList = new ArrayList<>();
    private List<String> originList;
    private boolean isCreate = true;
    private Constants.WalletType mWalletType;


    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirm_mnemonic, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_backup_mnemonic));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        for (int i = 0; i < shuffleList.size(); i++) {
            addItemToFloatLayout(mnemonicTagLayout, shuffleList.get(i), true);
        }
        int height = SizeUtils.getMeasuredHeight(mnemonicTagLayout);
        mnemonicTextLayout.setMinimumHeight(height);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        isCreate = bundle.getBoolean("isCreate", true);
        wallet = bundle.getParcelable(EXTRA_WALLET);
        mWalletType = Constants.WalletType.getPagerFromPositon(wallet.getWalletType());
        originList = wallet.getMnemonicCode();
        shuffleList.clear();
        shuffleList.addAll(originList);
        Collections.shuffle(shuffleList);
    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.completeBtn)
    public void onViewClicked() {
        if (ObjectUtils.equals(originList, confirmList)) {
            if (isCreate) {
                if (mWalletType == ALL || mWalletType == Constants.WalletType.MGP) {
                    showTipDialog(getString(R.string.str_creating_wallet_tip));
                    userRegister();
                } else {
                    toMainActivity();
                }
            } else {
                toMainActivity();
            }
        } else {
            ToastUtils.showShort(R.string.str_backup_mnemonics_error);
        }
    }

    private void addItemToFloatLayout(QMUIFloatLayout floatLayout, String text, boolean isClick) {
        TextView textView = new TextView(getActivity());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(text);
        if (isClick) {
//            boolean isSelect = false;
            int textViewPadding = QMUIDisplayHelper.dp2px(getActivity(), 5);
            textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
            textView.setGravity(Gravity.CENTER);
            textView.setTag(R.id.mnemon_select_tag, false);
            textView.setBackgroundColor(ColorUtils.getColor(R.color.qmui_config_color_white));

            textView.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
                @Override
                public void onDebouncingClick(View v) {
                    boolean isSelect = (boolean) ((TextView) v).getTag(R.id.mnemon_select_tag);
                    int currentChildCount = 0;
                    String text = ((TextView) v).getText().toString().trim();
                    if (isSelect) {
                        ((TextView) v).setTag(R.id.mnemon_select_tag, false);
                        ((TextView) v).setBackgroundColor(ColorUtils.getColor(R.color.qmui_config_color_white));
                        for (int i = 0; i < confirmList.size(); i++) {
                            String str = confirmList.get(i);
                            if (ObjectUtils.equals(str, text)) {
                                currentChildCount = i;
                                confirmList.remove(currentChildCount);
                                removeItemFromFloatLayout(mnemonicTextLayout, currentChildCount);
                            }
                        }
                    } else {
                        ((TextView) v).setTag(R.id.mnemon_select_tag, true);
                        ((TextView) v).setBackgroundColor(ColorUtils.getColor(R.color.qmui_config_color_gray_9));
                        confirmList.add(text);
                        addItemToFloatLayout(mnemonicTextLayout, text, false);
                    }
                    if (confirmList.size() == originList.size()) {
                        completeBtn.setEnabled(true);
                        completeBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue));
                    } else {
                        completeBtn.setEnabled(false);
                        completeBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.item_divider_bg_color));
                    }
                }
            });
        }
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(SizeUtils.dp2px(5),
                SizeUtils.dp2px(5),
                SizeUtils.dp2px(5),
                SizeUtils.dp2px(5));
        floatLayout.addView(textView, lp);
    }

    private void removeItemFromFloatLayout(QMUIFloatLayout floatLayout, int index) {
        if (floatLayout.getChildCount() == 0) {
            return;
        }
        floatLayout.removeView(floatLayout.getChildAt(index));
    }

    private void toMainActivity() {
        if (wallet == null) {
            return;
        }

        if (isCreate) {
            WalletDaoUtils.insertNewWallet(wallet);
        } else {
            updateWallet();
        }

        if (isCreate) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            popBackStack();
        }
        WalletDaoUtils.updateCurrent(wallet);
        BusUtils.post(BUS_TO_WALLET, new ToWallet(wallet, BUS_CUT_WALLET));
    }


    private void updateWallet() {
        List<MangoWallet> allWalletList = WalletDaoUtils.loadAll();
        for (MangoWallet wallet : allWalletList) {
            List<String> mnemonicCodeList = wallet.getMnemonicCode();
            if (CollectionUtils.isEqualCollection(originList, mnemonicCodeList)) {
                wallet.setIsBackup(true);
                WalletDaoUtils.mangoWalletDao.update(wallet);
            }
        }
    }

    /**
     * 账号自动激活
     */
    private void userRegister() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("publicKey", wallet.getPublicKey());
            params.put("account", wallet.getWalletAddress());
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().userRegister(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::userRegisterSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onError(Throwable throwable) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + throwable.toString() + " ===== " + throwable.getMessage());
        ToastUtils.showShort(R.string.str_create_wallet_fail);
    }

    private void userRegisterSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "userRegisterSuccess = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            BaseBean baseBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), BaseBean.class);
            if (baseBean != null) {
                if (baseBean.getCode() == 0) {
                    ToastUtils.showShort(R.string.str_create_wallet_succeed);
                    toMainActivity();
                } else {
                    MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
                    ToastUtils.showShort(msgCodeBean.getMsg());
                }
            } else {
                ToastUtils.showShort(R.string.str_create_wallet_fail);
            }
        } else {
            ToastUtils.showShort(R.string.str_create_wallet_fail);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
