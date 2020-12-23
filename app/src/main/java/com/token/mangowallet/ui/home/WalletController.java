package com.token.mangowallet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.BaseBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.activity.AddWalletActivity;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.ui.adapter.WalletAdapter;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.PopCreationWallet;
import com.token.mangowallet.view.WalletCardView;
import com.token.mangowallet.view.WalletPopupWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import party.loveit.eosforandroidlibrary.Ecc;

import static com.token.mangowallet.ui.home.HomeFragment.homeFragment;
import static com.token.mangowallet.utils.Constants.CREATE_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_ADD_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.EXTRA_CREATE_ACCOUNT_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.IMPORT_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TO_CREATE_WALLET;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;
import static com.token.mangowallet.utils.Constants.isTest;

public class WalletController extends QMUIWindowInsetLayout implements PopCreationWallet.OnCreationWalletClickListener, QMUIPullRefreshLayout.OnPullListener {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.walletCardView)
    WalletCardView walletCardView;
    @BindView(R.id.nodeVoteTv)
    AppCompatTextView nodeVoteTv;
    @BindView(R.id.assetsManageTv)
    AppCompatTextView assetsManageTv;
    @BindView(R.id.walletInfoLayout)
    LinearLayout walletInfoLayout;
    @BindView(R.id.itemTv)
    AppCompatTextView itemTv;
    @BindView(R.id.addAssetIv)
    AppCompatImageView addAssetIv;
    @BindView(R.id.tipLayout)
    FrameLayout tipLayout;
    @BindView(R.id.assetRecyclerView)
    RecyclerView assetRecyclerView;
    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout pullToRefresh;

    public WalletPopupWindow walletPopupWindow;
    public PopCreationWallet popCreationWallet;
    public Unbinder unbinder = null;
    public WalletAdapter walletAdapter;
    public Constants.WalletType mAddWalletType = ALL;
    public String walletAddress;
    public String privatekey;
    public String publicKey;
    public Constants.WalletType mCurrentWalletType = ALL;
    public List<Token> tokenItems;
    public String currency;
    public HomeFragment baseFragment;

    public WalletController(BaseFragment baseFragment) {
        super(baseFragment.getActivity());
        this.baseFragment = (HomeFragment) baseFragment;

        LayoutInflater.from(baseFragment.getActivity()).inflate(R.layout.fragment_home, this);
        unbinder = ButterKnife.bind(this);
        pullToRefresh.setOnPullListener(this);
        initData();
        initView();
        initAction();
    }

    private void initData() {

    }

    private void initView() {
        initRecycler();
    }

    private void initAction() {
        topbar.setTitle(R.string.app_name);
        topbar.addLeftImageButton(R.mipmap.ic_menu_2, R.id.topbar_left_change_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalletPopupWindow(v);
            }
        });
        topbar.addRightImageButton(R.mipmap.icon_scan2, R.id.topbar_right_change_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });
        walletCardView.setSwitchCheckedListener(new WalletCardView.OnWalletCardClickListener() {
            @Override
            public void onActivateWallet() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                baseFragment.startFragment("SelectOrRegisterWalletFragment", bundle);
            }

            @Override
            public void onWalletInfo() {
                if (homeFragment.isActivate) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                    if (baseFragment.mangoWallet.getIsBackup()) {
                        homeFragment.startFragment("WalletManagementFragment", bundle);
                    } else {
                        bundle.putBoolean("isCreate", false);
                        homeFragment.startFragment("BackupsMnemonicFragment", bundle);
                    }
                } else {
                    userRegister();
                }
            }

            @Override
            public void onWalletAddress() {
                if (ObjectUtils.isNotEmpty(walletAddress)) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isWalletAdress", true);
                    bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                    homeFragment.startFragment("ProceedsAdressFragment", bundle);
                }
            }
        });
    }

    @OnClick({R.id.nodeVoteTv, R.id.assetsManageTv, R.id.addAssetIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nodeVoteTv:
                if (baseFragment.mangoWallet != null) {
                    if (Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType()) == EOS
                            || Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType()) == MGP) {
                        if (ObjectUtils.isNotEmpty(baseFragment.mangoWallet.getWalletAddress())) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                            homeFragment.startFragment("StakeVoteMainFragment", bundle);
                        }
                    }
                }
                break;
            case R.id.assetsManageTv:
                if (baseFragment.mangoWallet != null) {
                    if (Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType()) == EOS
                            || Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType()) == MGP) {
                        if (ObjectUtils.isNotEmpty(baseFragment.mangoWallet.getWalletAddress())) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                            homeFragment.startFragment("ResourceAdministrationFragment", bundle);
                        }
                    }
                }
                break;
            case R.id.addAssetIv:

                break;
        }
    }

    private void initRecycler() {
        walletAdapter = new WalletAdapter(new ArrayList<>());
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(baseFragment.getActivity()));
        assetRecyclerView.setAdapter(walletAdapter);
        walletAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                homeFragment.startFragment("TransactionRecordFragment", bundle);
            }
        });
    }

    private void showCreationWallet(View v) {
        if (popCreationWallet == null) {
            popCreationWallet = new PopCreationWallet(baseFragment.getActivity());
            popCreationWallet.createPopup();
            popCreationWallet.setAnimationStyle(R.style.dialogSlideAnim);
            popCreationWallet.setOnCreationWalletClickListener(this);
        }
        if (popCreationWallet.isShowing()) {
            popCreationWallet.dismiss();
        } else {
            popCreationWallet.showPop(v);
        }
    }

    private void dismissCreationWallet() {
        if (popCreationWallet != null) {
            if (popCreationWallet.isShowing()) {
                popCreationWallet.dismiss();
            }
        }
    }

    public void showWalletPopupWindow(View view) {
        if (walletPopupWindow == null) {
            walletPopupWindow = new WalletPopupWindow(baseFragment.getActivity());
            walletPopupWindow.createPopup();
            walletPopupWindow.setAnimationStyle(R.style.dialogSlideAnim);
            walletPopupWindow.setOnAddWalletClickListener(new WalletPopupWindow.OnAddWalletClickListener() {
                @Override
                public void onAddWallet(View v) {
                    showCreationWallet(view);
                }

                @Override
                public void onSelectWalletType(Constants.WalletType walletType) {
                    mAddWalletType = walletType;
                }

                @Override
                public void onSelectWallet(MangoWallet wallet) {
                    baseFragment.mangoWallet = wallet;
                    updateView();
                }
            });
        }
        if (walletPopupWindow.isShowing()) {
            walletPopupWindow.dismiss();
        } else {
            walletPopupWindow.showPop(view);
        }
    }

    public void updateView() {
        if (baseFragment.mangoWallet != null) {
            baseFragment.verifyWallet();
            mCurrentWalletType = Constants.WalletType.getPagerFromPositon(baseFragment.mangoWallet.getWalletType());
            walletAddress = baseFragment.mangoWallet.getWalletAddress();
            privatekey = baseFragment.mangoWallet.getPrivateKey();
            publicKey = baseFragment.mangoWallet.getPublicKey();
            if (ObjectUtils.isEmpty(publicKey)) {
                publicKey = Ecc.privateToPublic(privatekey);
            }
            String walletName = mCurrentWalletType + "-Wallet";
            walletCardView.setWalletAddress(ObjectUtils.isNotEmpty(walletAddress), walletAddress, "");
            walletCardView.setWalletType(mCurrentWalletType);
            updataWalletState();
            walletCardView.setWalletName(walletName.contains("-") ? walletName : mCurrentWalletType + "-" + walletName);
            if (mCurrentWalletType == EOS || mCurrentWalletType == MGP) {
                walletInfoLayout.setVisibility(View.VISIBLE);
            } else {
                walletInfoLayout.setVisibility(View.GONE);
            }
            List<String> tokens = baseFragment.mangoWallet.getTokens();
            String json = tokens.toString();
            LogUtils.dTag("GsonUtils==", "tokens = " + json);
            tokenItems = GsonUtils.fromJson(json, new TypeToken<List<Token>>() {
            }.getType());
            BigDecimal sum = new BigDecimal("0");
            for (Token token : tokenItems) {
                if (ObjectUtils.isEmpty(token.value)) {
                    token.value = "0";
                }
                sum.add(new BigDecimal(token.value));
            }
            walletCardView.setTolalAssetValue(BalanceUtils.currencyToBase(sum.toPlainString(), 2, RoundingMode.FLOOR));
            walletAdapter.setTokens(tokenItems);
            walletAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 账号自动激活
     */
    private void userRegister() {
        try {
            homeFragment.showTipDialog(homeFragment.getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("publicKey", homeFragment.mangoWallet.getPublicKey());
            params.put("account", homeFragment.mangoWallet.getWalletAddress());
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

    public void updataWalletState() {//更新钱包状态
        if (homeFragment.isActivate) {
            walletCardView.setIsBackup(homeFragment.mangoWallet.getIsBackup());
        } else {
            if (Constants.WalletType.getPagerFromPositon(homeFragment.mangoWallet.getWalletType()) == MGP) {
                walletCardView.setIsActivate(false);
            }
        }
    }

    private void userRegisterSuccess(JsonObject jsonObject) {
        homeFragment.dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "userRegisterSuccess = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            BaseBean baseBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), BaseBean.class);
            if (baseBean != null) {
                if (baseBean.getCode() == 0) {
                    homeFragment.isActivate = true;
                    updataWalletState();
                    ToastUtils.showShort(R.string.str_activate_succeed);
                } else {
                    MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
                    ToastUtils.showShort(msgCodeBean.getMsg());
                }
            } else {
                ToastUtils.showShort(R.string.str_activate_succeed);
            }
        } else {
            ToastUtils.showShort(R.string.str_activate_succeed);
        }
    }

    private void onError(Throwable throwable) {
        homeFragment.dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + throwable.toString() + " ===== " + throwable.getMessage());
        ToastUtils.showShort(R.string.str_activate_fail);
    }

    private void dismissWalletPopupWindow() {
        if (walletPopupWindow != null) {
            if (walletPopupWindow.isShowing()) {
                walletPopupWindow.dismiss();
            }
        }
    }

    /**
     * 开始扫码
     */
    public void startQrCode() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(baseFragment.getActivity(), shouldRequest);
                    }
                }).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                Intent intent = new Intent(baseFragment.getActivity(), QRCodeScanActivity.class);
                baseFragment.getActivity().registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent intent = result.getData();
                        Bundle bundle;
                        if (intent != null) {
                            String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                            LogUtils.dTag("onActivityResult==", "scanResult = " + scanResult + " resultCode = " + resultCode);
                            if (!ObjectUtils.isEmpty(scanResult)) {
                                if (scanResult.contains("\"type\":\"" + TO_CREATE_WALLET + "\"") || scanResult.contains("\"type\":" + TO_CREATE_WALLET)) {
                                    bundle = new Bundle();
                                    bundle.putString(EXTRA_CREATE_ACCOUNT_DATA, scanResult);
                                    bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                                    baseFragment.startFragment("AssistCreateFragment", bundle);
//                        FragmentUtils.replace(getFragmentManager(), fragment, R.id.eostoken_main, "AssistCreateFragment", true);
                                }

                            }
                        }
                    }
                }).launch(intent);

            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

            }
        }).request();
    }

    @Override
    public void onCancel() {
        dismissCreationWallet();
    }

    @Override
    public void onImportWallet() {
        dismissCreationWallet();
        dismissWalletPopupWindow();
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_EXTRA_KEY_WALLET_TYPE, mAddWalletType.getType());
        bundle.putInt(EXTRA_ADD_WALLET_TYPE, IMPORT_WALLET);
        Intent intent = new Intent(baseFragment.getActivity(), AddWalletActivity.class);
        intent.putExtras(bundle);
        homeFragment.startActivity(intent);
//        startFragment("ImportWalletFragment", bundle);
    }

    @Override
    public void onCreateWallet() {
        dismissCreationWallet();
        dismissWalletPopupWindow();
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_EXTRA_KEY_WALLET_TYPE, mAddWalletType.getType());
        bundle.putInt(EXTRA_ADD_WALLET_TYPE, CREATE_WALLET);
        Intent intent = new Intent(baseFragment.getActivity(), AddWalletActivity.class);
        intent.putExtras(bundle);
        homeFragment.startActivity(intent);
    }

    public void release() {
        dismissCreationWallet();
        if (popCreationWallet != null) {
            popCreationWallet.release();
        }
        popCreationWallet = null;
        dismissWalletPopupWindow();
        if (walletPopupWindow != null) {
            walletPopupWindow.release();
        }
        walletPopupWindow = null;
        if (walletCardView != null) {
            walletCardView.release();
        }
        walletCardView = null;
    }


    @Override
    public void onMoveTarget(int offset) {

    }

    @Override
    public void onMoveRefreshView(int offset) {

    }

    @Override
    public void onRefresh() {
        ((HomeFragment) baseFragment).tokensViewModel.prepare();
        pullToRefresh.finishRefresh();
        updateView();
    }

}