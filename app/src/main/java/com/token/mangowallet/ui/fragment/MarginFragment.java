package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.LowerBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MARGIN_ACCOUNT;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.VOTE_ACCOUNT;

public class MarginFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.walletIconTv)
    QMUIRadiusImageView walletIconTv;
    @BindView(R.id.walletNameTv)
    AppCompatTextView walletNameTv;
    @BindView(R.id.walletAccountTv)
    AppCompatTextView walletAccountTv;
    @BindView(R.id.transferAmountEt)
    AppCompatEditText transferAmountEt;
    @BindView(R.id.qrCodeIv)
    AppCompatImageView qrCodeIv;
    @BindView(R.id.usdtAddressEt)
    AppCompatEditText usdtAddressEt;
    @BindView(R.id.usdtAddressLayout)
    QMUIRelativeLayout usdtAddressLayout;
    @BindView(R.id.promptTv)
    AppCompatTextView promptTv;
    @BindView(R.id.nextstepBtn)
    QMUIRoundButton nextstepBtn;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.transferAmountTv)
    AppCompatTextView transferAmountTv;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private EMWalletRepository emWalletRepository;
    private int isBindUsdt;
    private QMUIDialog passwordQmuiDialog;
    private BigDecimal mMixNum = BigDecimal.ZERO;
    private int type;
    private ThemesBean.DataBean dataBean;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_margin, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        isBindUsdt = bundle.getInt("isBindUsdt", 0);
        type = bundle.getInt("type", 0);
        walletAddress = mangoWallet.getWalletAddress();
        emWalletRepository = new EMWalletRepository();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        if (type == 0) {
            getLower();
        } else if (type == 1) {
            dataBean = bundle.getParcelable(EXTRA_VOTE_DATA);
        }
        getEOSMGPBalance();
    }

    @Override
    protected void initView() {
        topBar.setTitle(type == 0 ? (isBindUsdt == 0 ? R.string.str_apply_activation : R.string.str_additional_margin) : R.string.str_vote_t);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        walletAccountTv.setText(walletAddress);
        if (type == 0) {
            if (isBindUsdt != 0) {
                topBar.addRightTextButton(R.string.str_transaction_record, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        startFragment("MarginRecordFragment", bundle);
                    }
                });
            }
            usdtAddressLayout.setVisibility(isBindUsdt == 0 ? View.VISIBLE : View.GONE);
            InputFilter[] filters = {ViewUtils.filter};
            transferAmountEt.setFilters(filters);
        } else if (type == 1) {
            usdtAddressLayout.setVisibility(View.GONE);
            transferAmountTv.setText(R.string.str_number_responses);
            promptTv.setText(R.string.str_vote_details);

            transferAmountEt.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }
    }

    @Override
    protected void initAction() {

    }


    @OnClick({R.id.qrCodeIv, R.id.nextstepBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qrCodeIv:
                startQrCode();
                break;
            case R.id.nextstepBtn:
                if (ObjectUtils.isEmpty(transferAmountEt.getText())) {
                    ToastUtils.showLong(R.string.str_input_quantity);
                    return;
                }
                if (type == 0) {
                    if (isBindUsdt == 0) {
                        if (ObjectUtils.isEmpty(usdtAddressEt.getText())) {
                            ToastUtils.showLong(R.string.str_please_input_usdt_address);
                            return;
                        }
                    }
                }
                if ((new BigDecimal(transferAmountEt.getText().toString().trim())).compareTo(mMixNum) < 0) {//-1表示小于，0是等于，1是大于。
                    if (type == 0) {
                        ToastUtils.showLong(String.format(getString(R.string.str_margin_prompt), mMixNum + MGP_SYMBOL));
                    } else if (type == 1) {
                        ToastUtils.showLong(R.string.str_input_quantity);
                    }
                    return;
                }
                if (passwordQmuiDialog == null) {
                    passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                            getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                }
                passwordQmuiDialog.show();
                break;
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                transferTransaction();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

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
                        if (intent != null) {
                            String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                            usdtAddressEt.setText(scanResult);
                        }
                    }
                }).launch(intent);

            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

            }
        }).request();
    }

    private void getEOSMGPBalance() {
        try {
            emWalletRepository.fetchBalance(walletAddress, walletType).subscribe(this::balanceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transferTransaction() {
        showTipDialog(getString(R.string.str_loading));
        BigDecimal bdQuantity = new BigDecimal(transferAmountEt.getText().toString());
        String memo = "";
        if (type == 0) {
            memo = "Margin";
        } else if (type == 1) {
            //{"voteId":voteId,
            //"voteType":"0"} voteType:投票1，发布0
            memo = String.valueOf(dataBean.getVoteId()) + ",1";
        }
        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", type == 0 ? MARGIN_ACCOUNT : VOTE_ACCOUNT);
        params.put("quantity", bdQuantity.setScale(4) + " " + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(LOG_TAG, " privatekey = " + privatekey
                + "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    /**
     * 添加订单
     */
    private void bindUsdt(String hash) {
        Map params = MapUtils.newHashMap();
        params.put("mgpName", walletAddress);
        params.put("usdtAddr", usdtAddressEt.getText().toString().trim());
        params.put("hash", hash);
        params.put("money", transferAmountEt.getText().toString().trim());
        params.put("type", String.valueOf(isBindUsdt));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().bindUsdt(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindUsdtSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取保证金记录
     */
    private void getLower() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().lower(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::lowerSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lowerSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            LowerBean lowerBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), LowerBean.class);
            if (lowerBean.getCode() == 0) {
                if (lowerBean.getData() != null) {
                    mMixNum = lowerBean.getData();
                }
                promptTv.setText(lowerBean.getMsg());
            } else {
                ToastUtils.showLong(lowerBean.getMsg());
            }
        }
    }

    private void bindUsdtSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_transaction_success);
                popBackStack();
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void balanceSuccess(BigDecimal balance) {
        balanceTv.setText(String.format(getString(R.string.str_balance_value), balance.toPlainString() + walletType));
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                if (type == 0) {
                    bindUsdt(transactionBean.msg);
                } else if (type == 1) {
                    dismissTipDialog();
                    ToastUtils.showLong(R.string.str_vote_success);
                }
            } else {
                dismissTipDialog();
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            dismissTipDialog();
            if (type == 0) {
                ToastUtils.showLong(R.string.str_payment_failure);
            } else if (type == 1) {
                ToastUtils.showLong(R.string.str_vote_fail);
            }

        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }
}
