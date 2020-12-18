package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.CreateWalletInteract;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class ImportWalletFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.promptTv)
    AppCompatTextView promptTv;
    @BindView(R.id.importWalletKeyEt)
    AppCompatEditText importWalletKeyEt;
    @BindView(R.id.walletPasswordEt)
    AppCompatEditText walletPasswordEt;
    @BindView(R.id.againWalletPasswordEt)
    AppCompatEditText againWalletPasswordEt;
    @BindView(R.id.hintEt)
    AppCompatEditText hintEt;
    @BindView(R.id.startImportBtn)
    AppCompatButton startImportBtn;

    private Unbinder unbinder = null;
    private String[] arrTab;
    private int currIndex = 1;
    private Constants.WalletType mWalletType;
    private CreateWalletInteract createWalletInteract;
    private QMUIBottomSheet bottomSheet;
    private MangoWallet mangoWallet;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_import_wallet, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        int type;
        if (bundle != null) {
            type = bundle.getInt(INTENT_EXTRA_KEY_WALLET_TYPE);
        } else {
            type = 0;
        }
        mWalletType = Constants.WalletType.values()[type];
        createWalletInteract = new CreateWalletInteract();
        arrTab = new String[]{getString(R.string.str_mnemonic_word), getString(R.string.str_privatekey), getString(R.string.str_keystore)};
    }

    @Override
    protected void initView() {
        topBar.setTitle(String.format(getString(R.string.str_import_title), mWalletType == ALL ? "" : mWalletType));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightImageButton(R.mipmap.icon_scan2, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                startQrCode();
            }
        });
        initTabAndPager();
    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.startImportBtn)
    public void onViewClicked() {
        String importWalletData = importWalletKeyEt.getText().toString();
        String walletPwd = walletPasswordEt.getText().toString().trim();
        String confirmPwd = againWalletPasswordEt.getText().toString().trim();
        String hint = hintEt.getText().toString().trim();

        boolean isVerify = verifyInfo(importWalletData, walletPwd, confirmPwd);
        if (isVerify) {
            showTipDialog(getString(R.string.str_loading_wallet_tip));
            if (currIndex == 0) {//助记词
                List<String> mnemonicCodeList = Arrays.asList(importWalletData.split(" "));
                createWalletInteract.loadWalletByMnemonic(mWalletType, mnemonicCodeList, walletPwd, hint).subscribe(this::loadSuccess, this::onError);
            } else if (currIndex == 1) {//私钥
                createWalletInteract.loadWalletByPrivateKey(mWalletType, importWalletData, walletPwd, hint).subscribe(this::loadSuccess, this::onError);
            } else if (currIndex == 2) {//keystore
                createWalletInteract.loadWalletByKeystore(mWalletType, importWalletData, walletPwd, hint).subscribe(this::loadSuccess, this::onError);
            } else {//私钥
                createWalletInteract.loadWalletByPrivateKey(mWalletType, importWalletData, walletPwd, hint).subscribe(this::loadSuccess, this::onError);
            }
        }
    }

    private void initTabAndPager() {
        QMUITabBuilder builder = mTabSegment.tabBuilder().setGravity(Gravity.CENTER);
        for (int i = 0; i < arrTab.length; i++) {
            if (mWalletType == EOS || mWalletType == MGP) {
                if (!ObjectUtils.equals(getString(R.string.str_keystore), arrTab[i]))
                    mTabSegment.addTab(builder.setText(arrTab[i]).build(getActivity()));
            } else {
                mTabSegment.addTab(builder.setText(arrTab[i]).build(getActivity()));
            }
        }
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, false));
        mTabSegment.selectTab(currIndex);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                currIndex = index;
                updateView();
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

    private void updateView() {
        String promptRaw = "";
        int hintRaw = 0;
        String coinType = "";
        if (mWalletType == ETH) {
            coinType = getString(R.string.str_ethereum);
        } else if (mWalletType == BTC) {
            coinType = getString(R.string.str_bitcoin);
        }
        if (currIndex == 0) {//助记词
            promptRaw = getString(R.string.str_mnemonic_import_prompt);
            hintRaw = R.string.str_input_mnemonic;
        } else if (currIndex == 1) {//私钥
            promptRaw = getString(R.string.str_privatekey_import_prompt);
            hintRaw = R.string.str_input_privatekey;
        } else if (currIndex == 2) {//keystore
            promptRaw = String.format(getString(R.string.str_keystore_import_prompt), coinType);
            hintRaw = R.string.str_input_keystore;
        } else {
            promptRaw = getString(R.string.str_privatekey_import_prompt);
            hintRaw = R.string.str_input_privatekey;
        }

        promptTv.setText(promptRaw);
        importWalletKeyEt.setHint(hintRaw);
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
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
                        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                int resultCode = result.getResultCode();
                                Intent intent = result.getData();
                                if (intent != null) {
                                    String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                                    importWalletKeyEt.setText(scanResult);
                                    LogUtils.dTag("onActivityResult==", "scanResult = " + scanResult + " resultCode = " + resultCode);
                                }
                            }
                        }).launch(intent);

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

                    }
                }).request();
    }

    public void loadSuccess(MangoWallet wallet) {
        dismissTipDialog();
        WalletDaoUtils.updateCurrent(wallet);
        this.mangoWallet = wallet;
        if (mWalletType == MGP || mWalletType == EOS) {
            createWalletInteract.loadWalletAccount(wallet.getPrivateKey(), mWalletType).subscribe(this::walletAccountSuccess, this::onError);
        } else {
            toMainActivity(wallet);
        }
    }

    public void walletAccountSuccess(List<String> list) {
        if (ObjectUtils.isEmpty(list)) {
            toMainActivity(mangoWallet);
        } else {
            if (list.size() <= 0) {
                toMainActivity(mangoWallet);
            } else {
                showSimpleBottomSheetList(list, mWalletType);
            }
        }
    }

    public void onError(Throwable e) {
        ToastUtils.showLong(R.string.str_import_wallet_fail);
        dismissTipDialog();
    }

    private boolean verifyInfo(String importWalletData, String walletPwd, String confirmPwd) {
        if (currIndex == 0) {//助记词
            if (ObjectUtils.isEmpty(importWalletData)) {
                ToastUtils.showLong(R.string.str_load_wallet_by_mnemonic_input_tip);
                return false;
            } else {
                List<String> mnemonicCodeList = Arrays.asList(importWalletData.split(" "));
                if (WalletDaoUtils.isValidMnemonic(importWalletData)) {
                    ToastUtils.showLong(R.string.str_load_wallet_by_mnemonic_input_tip);
                    return false;
                } else if (WalletDaoUtils.checkRepeatByMenmonic(mnemonicCodeList)) {
                    ToastUtils.showLong(R.string.str_wallet_exist);
                    return false;
                }
            }
        } else if (currIndex == 1) {//私钥
            if (ObjectUtils.isEmpty(importWalletData)) {
                ToastUtils.showLong(R.string.str_please_privatekey);
                return false;
            } else if (mWalletType == ETH && !WalletDaoUtils.isPrivateKey(importWalletData)) {
                ToastUtils.showLong(R.string.str_invalid_privatekey);
                return false;
            } else if ((mWalletType == ETH || mWalletType == BTC) && WalletDaoUtils.checkRepeatByPrivateKey(importWalletData)) {
                ToastUtils.showLong(R.string.str_wallet_exist);
                return false;
            }
        } else if (currIndex == 2) {//keystore
            if (ObjectUtils.isEmpty(importWalletData)) {
                ToastUtils.showLong(R.string.str_invalid_keystore);
                return false;
            } else if (WalletDaoUtils.checkRepeatByKeystore(importWalletData)) {
                ToastUtils.showLong(R.string.str_wallet_exist);
                return false;
            }
        } else {//私钥
            if (ObjectUtils.isEmpty(importWalletData)) {
                ToastUtils.showLong(R.string.str_please_privatekey);
                return false;
            } else if (mWalletType == ETH && !WalletDaoUtils.isPrivateKey(importWalletData)) {
                ToastUtils.showLong(R.string.str_invalid_privatekey);
                return false;
            } else if ((mWalletType == ETH || mWalletType == BTC) && WalletDaoUtils.checkRepeatByPrivateKey(importWalletData)) {
                ToastUtils.showLong(R.string.str_wallet_exist);
                return false;
            }
        }

        if (walletPwd.length() < 8) {
            ToastUtils.showLong(R.string.str_wallet_password);
            return false;
        }
        if (ObjectUtils.isEmpty(confirmPwd) || !ObjectUtils.equals(confirmPwd, walletPwd)) {
            ToastUtils.showLong(R.string.str_pwd_confirm_input_tips);
            return false;
        }
        return true;
    }

    private void showSimpleBottomSheetList(List<String> list, Constants.WalletType walletType) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
        builder.setGravityCenter(true)
                .setTitle(String.format(getString(R.string.str_select_account), walletType))
                .setAddCancelBtn(false)
                .setAllowDrag(false)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        String walletAddress = list.get(position);
                        mangoWallet.setWalletAddress(walletAddress);
                        toMainActivity(mangoWallet);
                    }
                });

        for (int i = 0; i < list.size(); i++) {
            String walletAddress = list.get(i);
            if (ObjectUtils.isNotEmpty(walletAddress)) {
                builder.addItem(walletAddress);
            }
        }
        bottomSheet = builder.build();
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

    private void toMainActivity(MangoWallet mangoWallet) {
        if (mangoWallet != null) {
            mangoWallet.setIsBackup(true);
            WalletDaoUtils.insertNewWallet(mangoWallet);
            WalletDaoUtils.updateCurrent(mangoWallet);
        }
        ToastUtils.showLong(R.string.str_import_wallet_success);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        BusUtils.post(BUS_TO_WALLET, new ToWallet(mangoWallet, BUS_CUT_WALLET));
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
