package com.token.mangowallet.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.token.mangowallet.bean.CountryBean;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.GoodsTypeBean;
import com.token.mangowallet.bean.OrderSysBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.viewmodel.MortgageModelFactory;
import com.token.mangowallet.ui.viewmodel.MortgageViewModel;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.RSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.ui.fragment.OperatingStepsFragment.FIRST_MIX_MORTGAGE_TYPE;
import static com.token.mangowallet.ui.fragment.OperatingStepsFragment.TWICE_MIX_MORTGAGE_TYPE;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_FIRST;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.HMIO_SYMBOL;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.percent;

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

    private Unbinder unbinder;
    private MortgageModelFactory mortgageModelFactory;
    private MortgageViewModel mortgageViewModel;
    private Bundle bundle;
    private String type;
    private MangoWallet mangoWallet;
    private String remaining = "";
    private boolean isRemaining;
    private BigDecimal balance;
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
    private BigDecimal mHmioDuty = BigDecimal.ZERO;
    private BigDecimal bdQuantity;
    private BigDecimal bdNumHMIO = BigDecimal.ZERO;
    private BigDecimal bdMgpValue = BigDecimal.ZERO;

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
        isMortgage = bundle.getBoolean("isMortgage");
        String mMgpNum = bundle.getString("mgpNum", "0");
        if (ObjectUtils.isEmpty(mMgpNum)) {
            mMgpNum = "0";
        }
        mgpNum = new BigDecimal(mMgpNum);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        mortgageViewModel.prepare(mangoWallet);
        mortgageViewModel.balance().observe(this, this::onBalance);
        mortgageViewModel.prices().observe(this, this::onPrice);
        mortgageViewModel.transaction().observe(this, this::onTransaction);
        mortgageViewModel.getTicker();
//        presenter.requestCurrencyBalance(Params.getBalancePamars(accountName, EOSIO_TOKEN_CONTRACT_CODE, MGP_SYMBOL));
//        presenter.requestPortData(Constants.PRICE_MGP, PRICE);
        orderSys();
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void initView() {
        topBar.setTitle(type);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        if (isMortgage) {
            quantityTitleTv.setText(getString(R.string.str_mortgage_initiated));
            layout1.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            balanceTv.setVisibility(View.VISIBLE);
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

    private void mgpValueView() {
        if (quantityMGP == null) {
            quantityMGP = BigDecimal.ZERO;
        }
        bdMGPValue = (mgpNum.add(quantityMGP)).multiply(price).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String MGPValue = bdMGPValue.toPlainString();
        orderValueTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(MGPValue) ? "0.00" : MGPValue, 2, RoundingMode.FLOOR));
    }

    @OnClick(R.id.nextstepBtn)
    public void onViewClicked() {
        String quantity = quantityEt.getText().toString();
        if (ObjectUtils.isEmpty(quantity)) {
            ToastUtils.showLong(R.string.str_cannot_empty);
            return;
        }
        // 如果指定的数与参数相等返回0。
        // 如果指定的数小于参数返回 -1。
        // 如果指定的数大于参数返回  1。
        bdQuantity = new BigDecimal(quantity.trim());
        bdNumHMIO = bdQuantity.multiply(mHmioDuty);
        numHMIO = bdNumHMIO.toPlainString();
//        if (isMortgage && bdMGPValue.compareTo(new BigDecimal(100)) < 0) {
//            ToastUtils.showLong(R.string.str_cant_less_than_100);
//            return;
//        }
        if (isMortgage) {
            showSimpleBottomSheetList();
        } else {
            if (qmuiDialog == null) {
                qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                        getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
            }
            qmuiDialog.show();
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

    private void transferTransaction() {
        Map params = MapUtils.newHashMap();
        if (isRemaining) {
            action = "redeem";
            code = Constants.contractAddress;
            params.put("account", walletAddress);
        } else {
            String quantity = bdMgpValue.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();
//        String memo = type;
            code = EOSIO_TOKEN_CONTRACT_CODE;
            action = TRANSFER_ACTION;
            params.put("memo", "staking");
            params.put("from", walletAddress);
            params.put("to", Constants.contractAddress);
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
                            if (position == 0) {
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
                            } else if (position == 1) {
                                bdMgpValue = bdQuantity.subtract(bdNumHMIO);
//                                if (isMortgage && bdMgpValue.compareTo(balance) > 0) {
//                                    ToastUtils.showLong(R.string.str_cannot_greater_balance);
//                                    return;
//                                }
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                                bundle.putInt("type", FIRST_MIX_MORTGAGE_TYPE);
                                bundle.putString("num", numHMIO);
                                bundle.putString("SYMBOL", HMIO_SYMBOL);
                                startFragment("OperatingStepsFragment", bundle);
                            }
                        }
                    });
            builder.addItem("MGP");
            builder.addItem("HMIO+MGP");
            bottomSheet = builder.build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

    private void orderSys() {
        showTipDialog(getString(R.string.str_loading));
        try {
            NetWorkManager.getRequest().orderSys()
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
                    mHmioDuty = orderSysBean.getData().getHmio_pro();
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
