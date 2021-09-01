package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.BlendChannelBean;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.OrderSysBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.ui.viewmodel.MortgageModelFactory;
import com.token.mangowallet.ui.viewmodel.MortgageViewModel;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

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

import static com.token.mangowallet.ui.fragment.OperatingStepsFragment.FIRST_MIX_MORTGAGE_TYPE;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;

public class MiningMortgageFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.walletIconTv)
    QMUIRadiusImageView walletIconTv;
    @BindView(R.id.walletNameTv)
    AppCompatTextView walletNameTv;
    @BindView(R.id.walletAccountTv)
    AppCompatTextView walletAccountTv;
    @BindView(R.id.walletInfoLayout)
    QMUIRelativeLayout walletInfoLayout;
    @BindView(R.id.orderValueTv)
    AppCompatTextView orderValueTv;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.orderStarTv)
    AppCompatTextView orderStarTv;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    @BindView(R.id.layout)
    QMUILinearLayout layout;
    @BindView(R.id.nextstepBtn)
    QMUIRoundButton nextstepBtn;
    @BindView(R.id.quantityTitleTv)
    AppCompatTextView quantityTitleTv;
    @BindView(R.id.quantityEt)
    AppCompatEditText quantityEt;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.mortgageInitiatedTv)
    AppCompatTextView mortgageInitiatedTv;
    @BindView(R.id.qrcodeIv)
    AppCompatImageView qrcodeIv;
    @BindView(R.id.mortgageInitiatedEt)
    AppCompatEditText mortgageInitiatedEt;
    @BindView(R.id.text1)
    AppCompatTextView text1;
    @BindView(R.id.mortgageInitiatedLayout)
    RelativeLayout mortgageInitiatedLayout;

    private Unbinder unbinder;
    private MortgageModelFactory mortgageModelFactory;
    private MortgageViewModel mortgageViewModel;
    private Bundle bundle;
    private String type;
    private MangoWallet mangoWallet;
    private String remaining = "";
    private boolean isRemaining;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal quantityMGP;
    private String orderStar;
    private QMUIDialog qmuiDialog;
    private BigDecimal mgpNum;
    private BigDecimal bdMGPValue;
    private String code;
    private String action;
    private String walletAddress;
    private Constants.WalletType walletType;
    private int mRadius;
    private boolean isMortgage;
    private QMUIBottomSheet bottomSheet;
    private String numHMIO;
    private BigDecimal mHmioDuty = BigDecimal.ZERO;//混合比例
    private BigDecimal bdQuantity;
    private BigDecimal bdNumHMIO = BigDecimal.ZERO;
    private BigDecimal bdMgpValue = BigDecimal.ZERO;
    private String mAccountAddress = "";
    private List<BlendChannelBean.DataBean.ChannelListBean> channelListBeanList = new ArrayList<>();
    private BlendChannelBean.DataBean.ChannelListBean channelListBean;
    private OrderSysBean.DataBean orderSysDataBean;
    private int toType = 0;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mortgage_mining, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        mortgageModelFactory = new MortgageModelFactory();
        mortgageViewModel = new ViewModelProvider(this).get(MortgageViewModel.class);
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        type = bundle.getString("type", "");
        toType = bundle.getInt("toType", 0);
        isMortgage = bundle.getBoolean("isMortgage");
        String mMgpNum = bundle.getString("mgpNum", "0");
        if (ObjectUtils.isEmpty(mMgpNum)) {
            mMgpNum = "0";
        }
        mgpNum = new BigDecimal(mMgpNum);
        walletAddress = mangoWallet.getWalletAddress();
        mAccountAddress = walletAddress;
        mortgageInitiatedEt.setText(mAccountAddress);
        balanceTv.setText(String.format(getString(R.string.str_balance_value), balance.toPlainString()));
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        mortgageViewModel.prepare(mangoWallet);
        mortgageViewModel.balance().observe(this, this::onBalance);
        mortgageViewModel.prices().observe(this, this::onPrice);
        mortgageViewModel.transaction().observe(this, this::onTransaction);
        mortgageViewModel.getTicker();
        mortgageViewModel.getBlendChannel();
        mortgageViewModel.blendChannelModel().observe(this, this::onBlendChannelModel);
//        presenter.requestCurrencyBalance(Params.getBalancePamars(accountName, EOSIO_TOKEN_CONTRACT_CODE, MGP_SYMBOL));
//        presenter.requestPortData(Constants.PRICE_MGP, PRICE);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void initView() {
        topBar.setTitle(type);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        if (isMortgage) {
            quantityTitleTv.setText(getString(R.string.str_mortgage_initiated));
            layout1.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            balanceTv.setVisibility(View.VISIBLE);
            mortgageInitiatedLayout.setVisibility(View.VISIBLE);
            mortgageInitiatedEt.setVisibility(View.VISIBLE);
            isRemaining = false;
            remaining = "";
        } else {
            quantityTitleTv.setText(getString(R.string.str_callable_quantity));
            remaining = bundle.getString("remaining", "");
            ViewUtils.setEditableEditText(quantityEt, false);
            layout1.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            balanceTv.setVisibility(View.GONE);
            mortgageInitiatedLayout.setVisibility(View.GONE);
            mortgageInitiatedEt.setVisibility(View.GONE);
            isRemaining = true;
        }
        if (ObjectUtils.isEmpty(remaining)) {
            remaining = "";
        }

        walletInfoLayout.setRadius(mRadius);
        walletNameTv.setText(walletType + "-Wallet");
        walletAccountTv.setText(walletAddress);

        quantityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!ObjectUtils.isEmpty(s)) {
                    quantityMGP = new BigDecimal(s.toString());
                    mgpValueView();
                    if (bdMGPValue.compareTo(new BigDecimal("5000")) >= 0) {
                        orderStar = "M4";
                    } else if (bdMGPValue.compareTo(new BigDecimal("2000")) >= 0) {
                        orderStar = "M3";
                    } else if (bdMGPValue.compareTo(new BigDecimal("500")) >= 0) {
                        orderStar = "M2";
                    } else if (bdMGPValue.compareTo(new BigDecimal("100")) >= 0) {
                        orderStar = "M1";
                    } else {
                        orderStar = "M0";
                    }
                    orderStarTv.setText(orderStar);
                }
            }
        });
        InputFilter[] filters = {ViewUtils.filter};
        quantityEt.setFilters(filters);
        mortgageInitiatedEt.setVisibility(toType == 2 ? View.GONE : View.VISIBLE);
        mortgageInitiatedLayout.setVisibility(toType == 2 ? View.GONE : View.VISIBLE);
    }

    private void onBalance(BigDecimal balance) {
        LogUtils.dTag("=======onBalance = ");
        this.balance = balance.setScale(4, RoundingMode.CEILING);
        balanceTv.setText(String.format(getString(R.string.str_balance_value), balance.toPlainString()));
    }

    private void onPrice(CurrencyPrice currencyPrice) {
        LogUtils.dTag("=======onPrice = ");
        if (currencyPrice != null) {
            CurrencyPrice.DataBean dataBean = currencyPrice.getData();
            price = dataBean.getPrice();
        }
        if (price == null) {
            price = BigDecimal.ZERO;
        }
        mgpValueView();
    }

    private void onTransaction(TransactionBean transactionBean) {
        LogUtils.dTag("=======onTransaction = ");
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                ToastUtils.showShort(R.string.str_transaction_success);
                popBackStack();
                return;
            }
        }
        ToastUtils.showLong(transactionBean.msg);
    }

    private void onBlendChannelModel(String s) {
        channelListBeanList.clear();
        if (ObjectUtils.isNotEmpty(s)) {
            BlendChannelBean blendChannelBean = GsonUtils.fromJson(s, BlendChannelBean.class);
            if (blendChannelBean != null) {
                if (blendChannelBean.getCode() == 0) {
                    BlendChannelBean.DataBean dataBean = blendChannelBean.getData();
                    if (CollectionUtils.isNotEmpty(dataBean.getChannelList())) {
                        channelListBeanList.addAll(dataBean.getChannelList());
                    }
                }
            }
        }
    }

    private void mgpValueView() {
        if (quantityMGP == null) {
            quantityMGP = BigDecimal.ZERO;
        }
        bdMGPValue = (mgpNum.add(quantityMGP)).multiply(price).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String MGPValue = bdMGPValue.toPlainString();
        orderValueTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(MGPValue) ? "0.00" : MGPValue, 2, RoundingMode.FLOOR));
    }

    @OnClick({R.id.nextstepBtn, R.id.qrcodeIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nextstepBtn:
                if (ObjectUtils.isEmpty(quantityEt.getText())) {
                    ToastUtils.showLong(R.string.str_cannot_empty);
                    return;
                }
                if (ObjectUtils.isEmpty(mortgageInitiatedEt.getText())) {
                    ToastUtils.showLong(R.string.str_cannot_empty);
                    return;
                }
                mAccountAddress = mortgageInitiatedEt.getText().toString();
                boolean isPass = RegexUtils.isMatch(Constants.REGEX_ACCOUNT_NAME, mAccountAddress);
                if (!isPass) {
                    ToastUtils.showLong(R.string.str_account_name_rule);
                    return;
                }
                String quantity = quantityEt.getText().toString();
                // 如果指定的数与参数相等返回0。
                // 如果指定的数小于参数返回 -1。
                // 如果指定的数大于参数返回  1。
                bdQuantity = new BigDecimal(quantity.trim());
//        if (isMortgage && bdMGPValue.compareTo(new BigDecimal(100)) < 0) {
//            ToastUtils.showLong(R.string.str_cant_less_than_100);
//            return;
//        }

                if (isMortgage && toType == 1) {
                    showSimpleBottomSheetList();
                } else {
                    if (qmuiDialog == null) {
                        qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                    }
                    qmuiDialog.show();
                }
                break;
            case R.id.qrcodeIv:
                startQrCode();
                break;
        }
//        showSimpleBottomSheetList();
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
                        Bundle bundle;
                        if (!ObjectUtils.isEmpty(result)) {
                            int resultCode = result.getResultCode();
                            Intent intent = result.getData();
                            String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                            if (!ObjectUtils.isEmpty(scanResult)) {
                                mAccountAddress = scanResult;
                                mortgageInitiatedEt.setText(mAccountAddress);
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

    private void transferTransaction() {
        Map params = MapUtils.newHashMap();
        if (isRemaining) {
            action = "redeem";
            code = toType == 2 ? Constants.EMCONTRACT : Constants.contractAddress;
            params.put("account", walletAddress);
        } else {
            String quantity = (toType == 2 ? bdQuantity : bdMgpValue).setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();
//        String memo = type;
            code = EOSIO_TOKEN_CONTRACT_CODE;
            action = TRANSFER_ACTION;
            params.put("memo", mAccountAddress);
            params.put("from", walletAddress);
            params.put("to", toType == 2 ? Constants.EMCONTRACT : Constants.contractAddress);
            params.put("quantity", quantity + " " + walletType);
        }
        String jsonData = "";
        if (ObjectUtils.isNotEmpty(params)) {
            jsonData = GsonUtils.toJson(params);
        }
        mortgageViewModel.sendTransaction(action, code, jsonData);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (qmuiDialog != null) {
            if (qmuiDialog.isShowing()) {
                qmuiDialog.dismiss();
            }
        }
        qmuiDialog = null;
        quantityEt.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMortgage)
            quantityEt.setText(remaining);
    }

    private void showSimpleBottomSheetList() {
        if (bottomSheet == null) {
            QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
            builder.setGravityCenter(true)
                    .setAddCancelBtn(true)
                    .setAllowDrag(false)
                    .setNeedRightMark(false)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            channelListBean = channelListBeanList.get(position);
                            if (channelListBean.getChainType() == 1) {
                                bdMgpValue = bdQuantity;
                                if (isMortgage && bdMgpValue.compareTo(balance) > 0) {
                                    ToastUtils.showLong(R.string.str_cannot_greater_balance);
                                    return;
                                }
                                if (qmuiDialog == null) {
                                    qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                            getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                                }
                                qmuiDialog.show();
                            } else {
                                orderSys();
//                                toOperatingStepsFragment();
                            }
                        }
                    });
            for (int i = 0; i < channelListBeanList.size(); i++) {
                BlendChannelBean.DataBean.ChannelListBean channelListBean = channelListBeanList.get(i);
                builder.addItem(ObjectUtils.isEmpty(channelListBean.getName()) ? "" : channelListBean.getName());
            }
            bottomSheet = builder.build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

    private void toOperatingStepsFragment() {
        bdNumHMIO = bdQuantity.multiply(mHmioDuty);
        numHMIO = bdNumHMIO.setScale(4).toPlainString();
        bdMgpValue = bdQuantity.subtract(bdNumHMIO);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        bundle.putInt("type", FIRST_MIX_MORTGAGE_TYPE);
        bundle.putString("num", numHMIO);
        bundle.putString("moneyType", String.valueOf(channelListBean.getMoneyType()));
        bundle.putString("receiveAddress", mAccountAddress);
        bundle.putString("SYMBOL", orderSysDataBean.getCoin_type());
        bundle.putParcelable("orderSysDataBean", orderSysDataBean);
        startFragment("OperatingStepsFragment", bundle);
    }

    private void orderSys() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map params = MapUtils.newHashMap();
            params.put("num", quantityEt.getText().toString());
            params.put("id", String.valueOf(channelListBean.getId()));
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().orderSys(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::orderSysSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void orderSysSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            OrderSysBean orderSysBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), OrderSysBean.class);
            if (orderSysBean.getCode() == 0) {
                if (ObjectUtils.isNotEmpty(orderSysBean.getData())) {
                    orderSysDataBean = orderSysBean.getData();
                    mHmioDuty = orderSysDataBean.getHmio_pro();
                    toOperatingStepsFragment();
                }
            } else {
                ToastUtils.showLong(orderSysBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
