package com.token.mangowallet.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.ui.activity.AddWalletActivity;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.ui.adapter.WalletAdapter;
import com.token.mangowallet.ui.viewmodel.TokensViewModel;
import com.token.mangowallet.ui.viewmodel.TokensViewModelFactory;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.PopCreationWallet;
import com.token.mangowallet.view.WalletCardView;
import com.token.mangowallet.view.WalletPopupWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import party.loveit.eosforandroidlibrary.Ecc;

import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.CREATE_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_ADD_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.EXTRA_CREATE_ACCOUNT_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.IMPORT_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.TO_CREATE_WALLET;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class WalletFragment extends BaseFragment implements PopCreationWallet.OnCreationWalletClickListener, QMUIPullRefreshLayout.OnPullListener {

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

    private static final int PERMISSIONS_REQUEST_CODE = 10;
    private WalletPopupWindow walletPopupWindow;
    private PopCreationWallet popCreationWallet;
    private Unbinder unbinder = null;
    private WalletAdapter walletAdapter;
    private MangoWallet mangoWallet;
    private Constants.WalletType mAddWalletType = ALL;
    private String walletAddress;
    private String privatekey;
    private String publicKey;
    private String[] mPermissionList;
    private Constants.WalletType mCurrentWalletType = ALL;
    private List<Token> tokenItems;
    private String currency;

    TokensViewModelFactory tokensViewModelFactory;
    private TokensViewModel tokensViewModel;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, root);
        pullToRefresh.setOnPullListener(this);
        return root;
    }

    @Override
    protected void initData() {
        mangoWallet = WalletDaoUtils.getCurrentWallet();
        mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
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

    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.app_name);
        initRecycler();
        tokensViewModel.defaultWallet().observe(this, this::showWallet);
        tokensViewModel.tokens().observe(this, this::onTokens);
        tokensViewModel.prices().observe(this, this::onPrices);
        currency = tokensViewModel.getCurrency();
    }


    @Override
    protected void initAction() {
        topbar.addLeftImageButton(R.mipmap.ic_menu_2, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                showWalletPopupWindow(v);
            }
        });
        topbar.addRightImageButton(R.mipmap.icon_scan2, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                startQrCode();
            }
        });

        walletCardView.setSwitchCheckedListener(new WalletCardView.OnWalletCardClickListener() {
            @Override
            public void onActivateWallet() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("SelectOrRegisterWalletFragment", bundle);
            }

            @Override
            public void onWalletInfo() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("WalletManagementFragment", bundle);
            }

            @Override
            public void onWalletAddress() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("ProceedsAdressFragment", bundle);
            }
        });
    }

    @OnClick({R.id.nodeVoteTv, R.id.assetsManageTv, R.id.addAssetIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nodeVoteTv:
                break;
            case R.id.assetsManageTv:
                if (mangoWallet != null) {
                    if (Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == EOS
                            || Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == MGP) {
                        if (ObjectUtils.isNotEmpty(mangoWallet.getWalletAddress())) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                            startFragment("ResourceAdministrationFragment", bundle);
                        }
                    }
                }
                break;
            case R.id.addAssetIv:

                break;
        }
    }

    private void onTokens(List<Token> tokens) {
        tokenItems = tokens;
        LogUtils.d("onTokens==", "tokens = " + new Gson().toJson(tokenItems));
        walletAdapter.setTokens(tokenItems);
    }

    private void onPrices(CurrencyPrice currencyPrice) {
        LogUtils.d("onTokens==", "onPrices = " + new Gson().toJson(currencyPrice));
        BigDecimal sum = new BigDecimal(0);

        for (Token token : tokenItems) {
            if ((token.tokenInfo.symbol + "_USDT").equals(currencyPrice.getData().getPair())) {//EOS_USDT
                if (token.balance == null) {
                    token.value = "0";
                } else {
                    String price = "0";
                    BigDecimal bigDecimal = currencyPrice.getData().getPrice();
                    if (ObjectUtils.isEmpty(bigDecimal)) {
                        price = "0";
                    } else {
                        price = bigDecimal.toPlainString();
                    }
                    token.price = price;
                    token.value = BalanceUtils.ethToUsd(price, token.balance);
                }
            }
            if (!TextUtils.isEmpty(token.value)) {
                sum = sum.add(new BigDecimal(token.value));
            }
        }
        walletCardView.setTolalAssetValue(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(sum.toPlainString()) ? "0.00" : sum.toPlainString(), 2, RoundingMode.FLOOR));
        walletAdapter.setTokens(tokenItems);
    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
        updateView();
    }

    private void initRecycler() {
        walletAdapter = new WalletAdapter(new ArrayList<>());
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseFragmentActivity()));
        assetRecyclerView.setAdapter(walletAdapter);
        walletAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("TransactionRecordFragment", bundle);
            }
        });
    }

    private void showCreationWallet(View v) {
        if (popCreationWallet == null) {
            popCreationWallet = new PopCreationWallet(getActivity());
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

    private void showWalletPopupWindow(View view) {
        if (walletPopupWindow == null) {
            walletPopupWindow = new WalletPopupWindow(getActivity());
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
                    mangoWallet = wallet;
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

    private void updateView() {
        if (mangoWallet != null) {
            mCurrentWalletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
            walletAddress = mangoWallet.getWalletAddress();
            privatekey = mangoWallet.getPrivateKey();
            publicKey = mangoWallet.getPublicKey();
            if (ObjectUtils.isEmpty(publicKey)) {
                publicKey = Ecc.privateToPublic(privatekey);
            }
            String walletName = mCurrentWalletType + "-Wallet";
            walletCardView.setWalletAddress(ObjectUtils.isNotEmpty(walletAddress), walletAddress, "");
            walletCardView.setWalletType(mCurrentWalletType);
            walletCardView.setWalletName(walletName.contains("-") ? walletName : mCurrentWalletType + "-" + walletName);
            if (mCurrentWalletType == EOS || mCurrentWalletType == MGP) {
                walletInfoLayout.setVisibility(View.VISIBLE);
            } else {
                walletInfoLayout.setVisibility(View.GONE);
            }
            List<String> tokens = mangoWallet.getTokens();
            String json = tokens.toString();
            LogUtils.dTag("GsonUtils==", "tokens = " + json);
            tokenItems = GsonUtils.fromJson(json, new TypeToken<List<Token>>() {
            }.getType());
            BigDecimal sum = new BigDecimal("0");
            for (Token token : tokenItems) {
                String vStr = token.value;
                if (ObjectUtils.isEmpty(vStr)) {
                    vStr = "0";
                }
                sum.add(new BigDecimal(vStr));
            }
            walletCardView.setTolalAssetValue(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(sum.toPlainString()) ? "0.00" : sum.toPlainString(), 2, RoundingMode.FLOOR));
            walletAdapter.setTokens(tokenItems);
            walletAdapter.notifyDataSetChanged();
        }
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
                        DialogHelper.showRationaleDialog(getActivity(), shouldRequest);
                    }
                }).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent intent = result.getData();
                        Bundle bundle;
                        if (intent != null) {
                            String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                            LogUtils.dTag("onActivityResult==1", "scanResult = " + scanResult + " resultCode = " + resultCode);
                            if (!ObjectUtils.isEmpty(scanResult)) {
                                if (scanResult.contains("\"type\":\"" + TO_CREATE_WALLET + "\"") || scanResult.contains("\"type\":" + TO_CREATE_WALLET)) {
                                    bundle = new Bundle();
                                    bundle.putString(EXTRA_CREATE_ACCOUNT_DATA, scanResult);
                                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                                    startFragment("AssistCreateFragment", bundle);
//                        FragmentUtils.replace(getFragmentManager(), fragment, R.id.eostoken_main, "AssistCreateFragment", true);
                                }

                            }
                        }
                    }
                }).launch(intent);

            }

            @Override
            public void onDenied
                    (List<String> permissionsDeniedForever, List<String> permissionsDenied) {

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
        Intent intent = new Intent(getActivity(), AddWalletActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
//        startFragment("ImportWalletFragment", bundle);
    }

    @Override
    public void onCreateWallet() {
        dismissCreationWallet();
        dismissWalletPopupWindow();
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_EXTRA_KEY_WALLET_TYPE, mAddWalletType.getType());
        bundle.putInt(EXTRA_ADD_WALLET_TYPE, CREATE_WALLET);
        Intent intent = new Intent(getActivity(), AddWalletActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @BusUtils.Bus(tag = BUS_TO_WALLET)
    public void busToWallet(ToWallet toWallet) {
        if (!ObjectUtils.isEmpty(toWallet)) {
            MyApplication.mMid = "";
            int type = toWallet.getType();
            this.mangoWallet = toWallet.getWallet();
            WalletDaoUtils.updateCurrent(mangoWallet);
            tokensViewModel.updateDefaultWallet(mangoWallet);
            updateView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private boolean allPermissionsGranted() {
        for (String permission : mPermissionList) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        tokensViewModel.prepare();
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

    @Override
    public void onMoveTarget(int offset) {

    }

    @Override
    public void onMoveRefreshView(int offset) {

    }

    @Override
    public void onRefresh() {
        tokensViewModel.prepare();
        pullToRefresh.finishRefresh();
        updateView();
    }

}