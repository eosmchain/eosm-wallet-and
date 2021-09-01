package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.CreateWalletInteract;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.view.DialogHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class CreationWalletFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.walletPasswordEt)
    AppCompatEditText walletPasswordEt;
    @BindView(R.id.againWalletPasswordEt)
    AppCompatEditText againWalletPasswordEt;
    @BindView(R.id.createBtn)
    AppCompatButton createBtn;
    @BindView(R.id.walletNameTil)
    TextInputLayout walletNameTil;
    @BindView(R.id.walletPasswordTil)
    TextInputLayout walletPasswordTil;
    @BindView(R.id.againWalletPasswordTil)
    TextInputLayout againWalletPasswordTil;
    @BindView(R.id.hintEt)
    AppCompatEditText hintEt;
    @BindView(R.id.hintTil)
    TextInputLayout hintTil;
    @BindView(R.id.importBtn)
    QMUIRoundButton importBtn;

    @BindView(R.id.accountNameEt)
    AppCompatEditText accountNameEt;
    @BindView(R.id.errorTv)
    AppCompatTextView errorTv;


    private Unbinder unbinder;
    private Constants.WalletType mWalletType;
    private boolean isNamePass = false;
    private boolean isPswPass = false;
    private boolean isAgainPswPass = false;
    private CreateWalletInteract createWalletInteract;
    private QMUIDialog qmuiDialog;
    private List<Constants.WalletType> walletTypeList;
    private QMUIBottomSheet qmuiBottomSheet;
    private boolean isExit;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_creation_wallet, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initView() {
        topBar.setTitle(mWalletType == ALL ? getString(R.string.str_create_import_account) : String.format(getString(R.string.str_create_title), mWalletType));
        importBtn.setText(String.format(getString(R.string.str_import_title), ""));
        if (mWalletType == ALL) {
            importBtn.setVisibility(View.VISIBLE);
        } else {
            topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popBackStack();
                }
            });
            importBtn.setVisibility(View.GONE);
        }

        if (mWalletType == ALL || mWalletType == MGP) {
            accountNameEt.setVisibility(View.VISIBLE);
            isNamePass = false;
        } else {
            isNamePass = true;
            accountNameEt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        int type;
        if (bundle != null) {
            type = bundle.getInt(INTENT_EXTRA_KEY_WALLET_TYPE, 0);
        } else {
            type = 0;
        }

        mWalletType = Constants.WalletType.values()[type];
        createWalletInteract = new CreateWalletInteract();

        walletTypeList = CollectionUtils.newArrayList();
        walletTypeList.add(MGP);
        walletTypeList.add(EOS);
        walletTypeList.add(ETH);
        walletTypeList.add(BTC);
    }

    @Override
    protected void initAction() {
        afterTextChanged(accountNameEt);
        afterTextChanged(walletPasswordEt);
        afterTextChanged(againWalletPasswordEt);
    }

    private void afterTextChanged(AppCompatEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String errorMsg = "";
                switch (editText.getId()) {
                    case R.id.accountNameEt:
                        if (mWalletType == ALL || mWalletType == MGP || mWalletType == EOS) {
                            boolean isPass = RegexUtils.isMatch(Constants.REGEX_ACCOUNT_NAME, s);
                            errorMsg = getString(R.string.str_account_hint);
                            upErrorLayout(errorMsg, s.toString(), isPass);
                        } else {
                            isNamePass = true;
                        }
                        break;
                    case R.id.walletPasswordEt:
                        isPswPass = false;
                        if (s.length() >= 8 && s.length() <= 18) {
                            isPswPass = true;
                        } else {
                            isPswPass = false;
                        }
                        errorMsg = getString(R.string.str_pwd_inconformity_rule_tips);
                        upTextInputLayout(walletPasswordTil, isPswPass, errorMsg);
                        break;
                    case R.id.againWalletPasswordEt:
                        isAgainPswPass = false;
                        if (ObjectUtils.equals(againWalletPasswordEt.getText().toString().trim(), walletPasswordEt.getText().toString().trim())) {//str_pwd_confirm_input_tips
                            isAgainPswPass = true;
                        } else {
                            isAgainPswPass = false;
                        }
                        errorMsg = getString(R.string.str_pwd_confirm_input_tips);
                        upTextInputLayout(againWalletPasswordTil, isAgainPswPass, errorMsg);
                        break;
                }
                createEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
            }
        });
    }

    private void createEnabled() {
        if (isPswPass && isAgainPswPass && isNamePass) {
            createBtn.setEnabled(true);
            createBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.gradient_left_to_right));
        } else {
            createBtn.setEnabled(false);
            createBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.item_divider_bg_color));
        }
    }

    private void upErrorLayout(String errorMsg, String walletAddress, boolean isPass) {
        if (isPass) {
            errorTv.setVisibility(View.GONE);
            errorTv.setText("");
            createWalletInteract.getVerifyWallet(walletAddress, mWalletType).subscribe(this::jumpToVerifyWallet, this::verifyWalletError);
        } else {
            errorTv.setVisibility(View.VISIBLE);
            errorTv.setText(errorMsg);
            errorTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_red));
        }
    }

    private void upTextInputLayout(TextInputLayout textInputLayout, boolean isVerify, String errorMsg) {
        if (isVerify) {
            textInputLayout.setError("");
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setBoxStrokeColor(ContextCompat.getColor(getActivity(), R.color.blueColor));
        } else {
            textInputLayout.setError(errorMsg);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setBoxStrokeColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_red));
        }
    }

    @OnClick({R.id.createBtn, R.id.importBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.createBtn:
                if (ObjectUtils.isEmpty(accountNameEt.getText())) {
                    ToastUtils.showLong(R.string.str_please_enter_account);
                    return;
                }
                if (ObjectUtils.isEmpty(walletPasswordEt.getText())) {
                    ToastUtils.showLong(R.string.str_enter_password);
                    return;
                }
                if (ObjectUtils.isEmpty(againWalletPasswordEt.getText())) {
                    ToastUtils.showLong(R.string.str_again_input_password);
                    return;
                }
                if (ObjectUtils.isEmpty(walletPasswordEt.getText())) {
                    ToastUtils.showLong(R.string.str_enter_password);
                    return;
                }
                if (walletPasswordEt.getText().length() < 8) {
                    ToastUtils.showLong(R.string.str_wallet_password);
                    return;
                }
//                if (ObjectUtils.isEmpty(againWalletPasswordEt.getText().toString())) {
//                    ToastUtils.showLong(R.string.str_again_input_password);
//                    return;
//                }

                String walletPwd = walletPasswordEt.getText().toString().trim();
                String againWalletPwd = againWalletPasswordEt.getText().toString().trim();
                String pwdReminder = hintEt.getText().toString().trim();
                if (ObjectUtils.equals(walletPwd, againWalletPwd)) {
                    createWalletInteract.create(getActivity(), mWalletType, walletPwd, "").subscribe(this::jumpToWalletBackUp, this::showError);
                } else {
                    ToastUtils.showLong(R.string.str_pwd_confirm_input_tips);
                }
                break;
            case R.id.importBtn:
                if (qmuiBottomSheet == null) {
                    qmuiBottomSheet = DialogHelper.showSimpleBottomSheetList(getActivity(),
                            false, false, false, null,
                            false, false, walletTypeList, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    qmuiBottomSheet.dismiss();
                                    Constants.WalletType walletType = walletTypeList.get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(INTENT_EXTRA_KEY_WALLET_TYPE, walletType.getType());
                                    startFragment("ImportWalletFragment", bundle);
                                }
                            });
                }
                qmuiBottomSheet.show();
                break;
        }
    }


    private void jumpToVerifyWallet(AccountInfo accountInfo) {
        dismissTipDialog();
        isNamePass = false;
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(getString(R.string.str_account_invalid));
        errorTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_red));
        createEnabled();
    }

    public void verifyWalletError(Throwable errorInfo) {
        dismissTipDialog();
        isNamePass = true;
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(getString(R.string.str_account_available));
        errorTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_viridis));
        createEnabled();
    }

    public void jumpToWalletBackUp(MangoWallet wallet) {
        dismissTipDialog();
        if (wallet != null) {
            wallet.setWalletAddress(accountNameEt.getText().toString().trim());
            Bundle bundle = new Bundle();
            bundle.putBoolean("isCreate", true);
            bundle.putParcelable(EXTRA_WALLET, wallet);
            startFragment("BackupsMnemonicFragment", bundle);
//            ToastUtils.showShort(R.string.str_create_wallet_succeed);
//            qmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_backup_wallet), getString(R.string.str_backup_wallet_msg),
//                    "", getString(R.string.str_backup_mnemonic), null, new QMUIDialogAction.ActionListener() {
//                        @Override
//                        public void onClick(QMUIDialog dialog, int index) {
//                            dialog.dismiss();
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable(EXTRA_WALLET, wallet);
//                            startFragment("BackupsMnemonicFragment", bundle);
//                            qmuiDialog = null;
//                        }
//                    });
//            qmuiDialog.show();
        } else {
            ToastUtils.showShort(R.string.str_create_wallet_fail);
        }
    }

    public void showError(Throwable errorInfo) {
        dismissTipDialog();
        LogUtils.e("CreateWalletActivity", errorInfo);
        ToastUtils.showShort(errorInfo.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onBackPressed() {
        if (mWalletType == ALL) {
            if (isExit) {
                AppUtils.exitApp();
            } else {
                ToastUtils.showLong(R.string.str_again_exit);
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
        }
    }

}
