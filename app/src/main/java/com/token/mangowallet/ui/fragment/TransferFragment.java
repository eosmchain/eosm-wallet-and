package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.android.material.textfield.TextInputEditText;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.TransferNameMode;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.ui.viewmodel.TransactionModelFactory;
import com.token.mangowallet.ui.viewmodel.TransactionViewModel;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.TO_TRANSFER_WALLET;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;

public class TransferFragment extends BaseFragment {


    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.EOSAmountEt)
    TextInputEditText EOSAmountEt;
    @BindView(R.id.CollectionAddressTv)
    AppCompatTextView CollectionAddressTv;
    @BindView(R.id.CollectionAddressEt)
    TextInputEditText CollectionAddressEt;
    @BindView(R.id.memoTv)
    AppCompatTextView memoTv;
    @BindView(R.id.memoEt)
    TextInputEditText memoEt;
    @BindView(R.id.transactionBtn)
    QMUIRoundButton transactionBtn;

    private Unbinder unbinder;
    private TransactionModelFactory transactionModelFactory;
    private TransactionViewModel transactionViewModel;

    private AccountInfo accountInfo;
    private String walletAddress;
    private String balanceStr = "0";
    private String privatekey;
    private String jsonData;
    private MangoWallet mangoWallet;
    private QMUIDialog qmuiDialog;
    private String to;
    private String scanResult;
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_transfer, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        privatekey = mangoWallet.getPrivateKey();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        transactionModelFactory = new TransactionModelFactory();
        transactionViewModel = ViewModelProviders.of(this.getActivity(), transactionModelFactory)
                .get(TransactionViewModel.class);
        transactionViewModel.prepare(mangoWallet);
        transactionViewModel.balance().observe(this, this::onBalance);
        transactionViewModel.transaction().observe(this, this::onTransaction);
        transactionViewModel.fetchBalance();
    }

    @Override
    protected void initView() {
        topbar.setTitle(walletType + getString(R.string.str_transfer));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                clearEditText();
                popBackStack();
            }
        });
        topbar.addRightImageButton(R.mipmap.icon_scan2, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                startQrCode();
            }
        });
        balanceTv.setText(String.format(getString(R.string.str_balance_value), balanceStr) + " " + walletType);
        InputFilter[] filters = {ViewUtils.filter};
        EOSAmountEt.setFilters(filters);
    }

    @Override
    protected void initAction() {

    }

    private void onBalance(BigDecimal balance) {
        balanceStr = balance.setScale(4, RoundingMode.CEILING).toPlainString();
        balanceTv.setText(String.format(getString(R.string.str_balance_value), balanceStr) + " " + walletType);
    }

    private void onTransaction(TransactionBean transactionBean) {
        try {
            if (transactionBean.isSuccess) {
                clearEditText();
                ToastUtils.showShort(R.string.str_transaction_success);
                popBackStack();
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                        LogUtils.dTag("onActivityResult==", "scanResult = " + scanResult + " resultCode = " + resultCode);
                        Intent intent = result.getData();
                        if (intent != null) {
                            String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                            if (scanResult.contains("\"type\":\"" + TO_TRANSFER_WALLET + "\"") || scanResult.contains("\"type\":" + TO_TRANSFER_WALLET)) {
                                TransferNameMode transferNameMode = GsonUtils.fromJson(scanResult, TransferNameMode.class);
                                to = transferNameMode.getAccountName();
                                CollectionAddressEt.setText(to);
                            } else {
                                CollectionAddressEt.setText(scanResult); 
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

    @OnClick(R.id.transactionBtn)
    public void onViewClicked() {
//        "{\n" +
//                "\"from\": \"" + fromAccount + "\",\n" +
//                "\"to\": \"" + toAccount + "\",\n" +
//                "\"quantity\": \"" + amount + "\",\n" +
//                "\"memo\" : \"" + memo + "\"\n" +
//                "}";

        String quantity = EOSAmountEt.getText().toString().trim();
        to = CollectionAddressEt.getText().toString().trim();
        if (ObjectUtils.isEmpty(quantity) || ObjectUtils.isEmpty(to)) {
            ToastUtils.showLong(R.string.str_cannot_empty);
            return;
        }
        BigDecimal bdQuantity = new BigDecimal(quantity);
        String[] arr = balanceStr.split(" ");
        BigDecimal bdBalance = new BigDecimal(arr[0]);

        if (bdQuantity.compareTo(bdBalance) == 1) {
            ToastUtils.showLong(R.string.str_cannot_greater_balance);
            return;
        }

        quantity = bdQuantity.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();
        /**
         * from : fromAccount
         * to : toAccount
         * quantity :  amount
         * memo : memo
         */
        String memo = memoEt.getText().toString().trim();
        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", to);
        params.put("quantity", quantity + " " + walletType);

        jsonData = GsonUtils.toJson(params);
        if (qmuiDialog == null) {
            qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
        qmuiDialog.show();
    }

    private void clearEditText() {
        EOSAmountEt.setText("");
        memoEt.setText("");
        CollectionAddressEt.setText("");
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            dialog.dismiss();
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                transferTransaction();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
        }
    };


    private void transferTransaction() {
        LogUtils.dTag(Constants.LOG_TAG, " privatekey = " + privatekey
                + "accountName = " + walletAddress
                + "params = " + jsonData);
        transactionViewModel.sendTransaction(TRANSFER_ACTION, EOSIO_TOKEN_CONTRACT_CODE, jsonData);
        jsonData = "";
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearEditText();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
