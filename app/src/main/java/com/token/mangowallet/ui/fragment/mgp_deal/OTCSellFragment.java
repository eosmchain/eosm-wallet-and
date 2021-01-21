package com.token.mangowallet.ui.fragment.mgp_deal;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyData;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.CurrencySetupBean;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.CashierInputFilter;
import com.token.mangowallet.view.DialogHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.KEY_COIN_SYMBOL;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;
import static com.token.mangowallet.utils.Constants.OPEN_ORDER;
import static com.token.mangowallet.utils.Constants.SHOP_ACCOUNT;
import static com.token.mangowallet.utils.Constants.SP_MangoWallet_info;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.percent;

public class OTCSellFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.errorTv)
    AppCompatTextView errorTv;
    @BindView(R.id.toSetupTv)
    AppCompatTextView toSetupTv;
    @BindView(R.id.errorLayout)
    FrameLayout errorLayout;
    @BindView(R.id.mgpPriceTv)
    AppCompatTextView mgpPriceTv;
    @BindView(R.id.unitPriceTv)
    AppCompatTextView unitPriceTv;
    @BindView(R.id.subtractBtn)
    QMUIRoundButton subtractBtn;
    @BindView(R.id.numTv)
    AppCompatEditText numTv;
    @BindView(R.id.additionBtn)
    QMUIRoundButton additionBtn;
    @BindView(R.id.quantityTv)
    AppCompatTextView quantityTv;
    @BindView(R.id.quantityEt)
    AppCompatEditText quantityEt;
    @BindView(R.id.totalTv)
    AppCompatTextView totalTv;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.minSaleAmountTv)
    AppCompatTextView minSaleAmountTv;
    @BindView(R.id.minSaleAmountEt)
    AppCompatEditText minSaleAmountEt;
    @BindView(R.id.fiatMoneyUnitTv)
    AppCompatTextView fiatMoneyUnitTv;
    @BindView(R.id.totalMoneyTv)
    AppCompatTextView totalMoneyTv;
    @BindView(R.id.totalMoneyValueTv)
    AppCompatTextView totalMoneyValueTv;
    @BindView(R.id.fiatMoneyUnitTv2)
    AppCompatTextView fiatMoneyUnitTv2;
    @BindView(R.id.sellBtn)
    QMUIRoundButton sellBtn;
    @BindView(R.id.cnyTv)
    AppCompatTextView cnyTv;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    public Constants.WalletType walletType;
    public EMWalletRepository emWalletRepository;
    private boolean isBind = false;
    private boolean isPledge = false;
    private String remaining;
    private String walletAddress;
    private BigDecimal price = BigDecimal.ZERO;//当前MGP单价
    private BigDecimal priceDecimal = BigDecimal.ZERO;//出售单价价额（选中货币的）
    private BigDecimal incrementDecimal = BigDecimal.ZERO;//出售价额增减值0.5
    private BigDecimal balanceDecimal = BigDecimal.ZERO;//钱包余额
    private BigDecimal CNYDecimal = BigDecimal.ZERO;//出售单价人民币价额（priceDecimal转人民币） priceDecimal/currencyDecimal*CNYPriceDecimal
    private BigDecimal currencyDecimal = BigDecimal.ZERO;//当前选中货币兑美元的单价价额
    private BigDecimal CNYPriceDecimal = BigDecimal.ZERO;//人民币兑美元的单价价额
    private BigDecimal minCNYDecimal = BigDecimal.ZERO;//最小出售价额（最小出售MGP数量*CNYPriceDecimal）
    private BigDecimal maxCNYDecimal = BigDecimal.ZERO;//当前MGP单价
    private BigDecimal quantityDecimal = BigDecimal.ZERO;//输入的MGP数量
    private BigDecimal minDecimal = BigDecimal.ZERO;//最小出售输入框的值

    private boolean isMinAmount = true;
    private boolean isQuantity = false;
    private boolean isMinSale = false;
    private boolean isTransfer = true;
    private CurrencyData currencyData;
    private QMUIDialog passwordQmuiDialog;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mgp_sell, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        walletAddress = mangoWallet.getWalletAddress();
        emWalletRepository = new EMWalletRepository();
        incrementDecimal = new BigDecimal("0.5");
        currencyData = BalanceUtils.getCurrencyData();
        if (currencyData != null) {
            currencyDecimal = currencyData.getPrice();
        }
        if (currencyDecimal == null) {
            currencyDecimal = BigDecimal.ZERO;
        }
        String json = SPUtils.getInstance(SP_MangoWallet_info).getString(KEY_COIN_SYMBOL, "");
        if (ObjectUtils.isNotEmpty(json)) {
            CurrencySetupBean currencySetupBean = GsonUtils.fromJson(json, CurrencySetupBean.class);
            if (CollectionUtils.isNotEmpty(currencySetupBean.getData())) {
                for (int i = 0; i < currencySetupBean.getData().size(); i++) {
                    CurrencyData currencyData = currencySetupBean.getData().get(i);
                    if (ObjectUtils.equals("￥", currencyData.getSymbol())) {
                        CNYPriceDecimal = currencyData.getPrice();
                    }
                }
            }
        }

        isBind();
        isPosPledge();
        getCoinPrice();
        getWalletBalance();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_my_sell);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        toSetupTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        toSetupTv.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void initAction() {
        addEditTextListener(quantityEt);
        addEditTextListener(minSaleAmountEt);
        addEditTextListener(numTv);
    }

    private void addEditTextListener(AppCompatEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (editText.getId()) {
                    case R.id.quantityEt:
                        BigDecimal bigDecimal = new BigDecimal(ObjectUtils.isEmpty(s) ? "0" : s.toString());
                        balanceTv.setText(String.format(getString(R.string.str_balance_value), balanceDecimal.subtract(bigDecimal).setScale(4).toPlainString() + " MGP"));
                        totalMoneyValueTv.setText(bigDecimal.multiply(priceDecimal).setScale(2, RoundingMode.FLOOR).toPlainString());
                        if (ObjectUtils.isEmpty(s)) {
                            isQuantity = false;
                        } else {
                            isQuantity = true;
                        }
                        break;
                    case R.id.minSaleAmountEt:
                        BigDecimal minBigDecimal = new BigDecimal(ObjectUtils.isEmpty(s) ? "0" : s.toString());
                        if (ObjectUtils.isEmpty(s)) {
                            isMinSale = false;
                        } else {
                            isMinSale = true;
                        }
                        if (isMinAmount) {
                            minCNYDecimal = minBigDecimal;
                        } else {
                            minCNYDecimal = minBigDecimal.multiply(priceDecimal);
                        }
                        break;
                    case R.id.numTv:
                        priceDecimal = new BigDecimal(ObjectUtils.isEmpty(numTv.getText()) ? "0" : numTv.getText().toString());
                        break;
                }
                if (isQuantity && isMinSale && isBind && isPledge) {
                    sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue2));
                    sellBtn.setClickable(true);
                } else {
                    sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
                    sellBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (editText.getId()) {
                    case R.id.quantityEt:
                        BigDecimal bigDecimal = new BigDecimal(ObjectUtils.isEmpty(s) ? "0" : s.toString());
                        balanceTv.setText(String.format(getString(R.string.str_balance_value), balanceDecimal.subtract(bigDecimal).setScale(4).toPlainString() + " MGP"));
                        totalMoneyValueTv.setText(bigDecimal.multiply(priceDecimal).setScale(2, RoundingMode.FLOOR).toPlainString());
                        if (ObjectUtils.isEmpty(s)) {
                            isQuantity = false;
                        } else {
                            isQuantity = true;
                        }
                        break;
                    case R.id.minSaleAmountEt:
                        BigDecimal minBigDecimal = new BigDecimal(ObjectUtils.isEmpty(s) ? "0" : s.toString());
                        if (ObjectUtils.isEmpty(s)) {
                            isMinSale = false;
                        } else {
                            isMinSale = true;
                        }
                        if (isMinAmount) {
                            minCNYDecimal = minBigDecimal;
                        } else {
                            minCNYDecimal = minBigDecimal.multiply(priceDecimal);
                        }
                        break;
                    case R.id.numTv:
                        priceDecimal = new BigDecimal(ObjectUtils.isEmpty(numTv.getText()) ? "0" : numTv.getText().toString());
                        break;
                }
                if (isQuantity && isMinSale && isBind && isPledge) {
                    sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue2));
                    sellBtn.setClickable(true);
                } else {
                    sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
                    sellBtn.setClickable(false);
                }
            }
        });
    }

    @OnClick({R.id.toSetupTv, R.id.subtractBtn, R.id.additionBtn, R.id.sellBtn, R.id.totalTv, R.id.minSaleAmountTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toSetupTv:
                Bundle bundle = new Bundle();
                if (ObjectUtils.equals(getString(R.string.str_to_add), toSetupTv.getText().toString())) {
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("OTCSetupFragment", bundle);
                } else if (ObjectUtils.equals(getString(R.string.str_to_pledge), toSetupTv.getText().toString())) {
                    bundle.putString("mgpNum", "0");
                    bundle.putString("type", getString(R.string.str_mortgage_initiated));
                    bundle.putBoolean("isMortgage", true);
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("MiningMortgageFragment", bundle);
                }
                break;
            case R.id.subtractBtn:
                unitPrice(false);
                break;
            case R.id.additionBtn:
                unitPrice(true);
                break;
            case R.id.sellBtn:
                quantityDecimal = new BigDecimal(ObjectUtils.isEmpty(quantityEt.getText()) ? "0" : quantityEt.getText().toString());
                minDecimal = new BigDecimal(ObjectUtils.isEmpty(minSaleAmountEt.getText()) ? "0" : minSaleAmountEt.getText().toString());
                BigDecimal minMGPDecimal = BigDecimal.ZERO;
                if (isMinAmount) {
                    minMGPDecimal = minDecimal.divide(priceDecimal, 4, RoundingMode.FLOOR);
                } else {
                    minMGPDecimal = minDecimal;
                }
                if (quantityDecimal.compareTo(minMGPDecimal) >= 0) {
                    if (passwordQmuiDialog == null) {
                        passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                    }
                    passwordQmuiDialog.show();
                } else {
                    ToastUtils.showLong(R.string.str_sell_quantity_tip);
                }
                break;
            case R.id.totalTv:
                quantityEt.setText(balanceDecimal.toPlainString());
                balanceTv.setText(String.format(getString(R.string.str_balance_value), "0.0000 MGP"));
                totalMoneyValueTv.setText(balanceDecimal.multiply(priceDecimal).toPlainString());
                break;
            case R.id.minSaleAmountTv:
                showMinSalePop();
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

    private void unitPrice(boolean isAdd) {
        BigDecimal numDecimal = new BigDecimal(ObjectUtils.isEmpty(numTv.getText()) ? "0" : numTv.getText().toString());
        if (numDecimal.compareTo(BigDecimal.ZERO) > 0) {//-1表示小于，0是等于，1是大于。
            if (isAdd) {
                priceDecimal = numDecimal.add(incrementDecimal);
            } else {
                priceDecimal = numDecimal.subtract(incrementDecimal);
            }
            if (priceDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                numTv.setText("0");
            } else {
                numTv.setText(priceDecimal.toPlainString());
            }
        }
        toCNY();
    }

    private void updataErrerView() {
        if (!isBind || !isPledge) {
            errorLayout.setVisibility(View.VISIBLE);
            if (!isBind) {
                errorTv.setText(R.string.str_no_contact_msg);
                toSetupTv.setText(R.string.str_to_add);
            }
            if (!isPledge) {
                errorTv.setText(R.string.str_no_pospledge_msg);
                toSetupTv.setText(R.string.str_to_pledge);
            }
            sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
        } else {
            errorLayout.setVisibility(View.GONE);
//            sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue2));
        }
    }

    private void showMinSalePop() {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .setTitle("")
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (position == 0) {
                            isMinAmount = true;
                            fiatMoneyUnitTv.setText("| CNY");
                            minSaleAmountTv.setText(getString(R.string.str_min_sale_amount));
                            minSaleAmountEt.setHint(R.string.str_enter_min_sale_amount);
                            minSaleAmountEt.setFilters(new InputFilter[]{new CashierInputFilter(maxCNYDecimal, 2)});
                        } else {
                            isMinAmount = false;
                            fiatMoneyUnitTv.setText("| MGP");
                            minSaleAmountTv.setText(getString(R.string.str_min_sale_quantity));
                            minSaleAmountEt.setHint(R.string.str_import_min_sale_quantity);
                            minSaleAmountEt.setFilters(new InputFilter[]{new CashierInputFilter(balanceDecimal, 4)});
                        }
                    }
                });
        builder.addItem(getString(R.string.str_min_sale_amount));
        builder.addItem(getString(R.string.str_min_sale_quantity));
        builder.build().show();
    }

    /**
     * 账号自动激活
     */
    private void isBind() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("type", "0");
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().isBind(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onIsBindSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isPosPledge() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", Constants.contractAddress);
            mapTableRows.put("code", Constants.contractAddress);
            mapTableRows.put("table", "balances");
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("lower_bound", mangoWallet.getWalletAddress());
            mapTableRows.put("upper_bound", mangoWallet.getWalletAddress());
            emWalletRepository.fetchTableRows(mapTableRows, walletType)
                    .subscribe(this::onTableRows, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付订单转账
     * {"orderSn":"MGP1597654887759958804071291","currencyPrice":"0.51","dollar":"160"}
     */
    private void transferTransaction() {
        if (isTransfer) {
            showTipDialog(getString(R.string.str_loading));
        }
        Map params = MapUtils.newHashMap();
        String action;
        String code;
        priceDecimal = new BigDecimal(ObjectUtils.isEmpty(numTv.getText()) ? "0" : numTv.getText().toString());
        if (isTransfer) {
            action = TRANSFER_ACTION;
            code = EOSIO_TOKEN_CONTRACT_CODE;
            String memo = "sell";
            params.put("memo", memo);
            params.put("from", walletAddress);
            params.put("to", DEAL_CONTRACT);
            params.put("quantity", quantityDecimal.setScale(4) + " " + walletType);
        } else {
            action = OPEN_ORDER;
            code = DEAL_CONTRACT;
            if (isMinAmount) {
                minCNYDecimal = minDecimal;
            } else {
                minCNYDecimal = minDecimal.multiply(priceDecimal);
            }
            params.put("owner", walletAddress);
            params.put("quantity", new BigDecimal(quantityEt.getText().toString()).setScale(4) + " " + MGP_SYMBOL);
            params.put("price", priceDecimal.setScale(2).toPlainString() + " CNY");
            params.put("min_accept_quantity", minCNYDecimal.setScale(2).toPlainString() + " CNY");
        }

        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(Constants.LOG_TAG, "accountName = " + walletAddress
                + "params = " + jsonData);
        //openorder
        emWalletRepository.sendTransaction(action, privatekey, walletAddress, code, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean.isSuccess) {
            if (isTransfer) {
                isTransfer = false;
                transferTransaction();
            } else {
                dismissTipDialog();
                ToastUtils.showLong(R.string.str_order_succeed);
                popBackStack();
            }
        } else {
            ToastUtils.showLong(transactionBean.msg);
            dismissTipDialog();
        }
    }

    private void getCoinPrice() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map map = MapUtils.newHashMap();
            map.put("pair", "MGP_USDT");
            String json = GsonUtils.toJson(map);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCoinPrice(content)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::onCoinPriceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWalletBalance() {
        try {
            showTipDialog(getString(R.string.str_loading));
            emWalletRepository.fetchBalance(walletAddress, walletType)
                    .subscribe(this::onWalletBalance, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCoinPriceSuccess(CurrencyPrice currencyPrice) {
        dismissTipDialog();
//        BigDecimal price = BigDecimal.ZERO;
        if (ObjectUtils.isNotEmpty(currencyPrice)) {
            CurrencyPrice.DataBean dataBean = currencyPrice.getData();
            if (dataBean != null) {
                if (dataBean.getPrice() != null)
                    price = dataBean.getPrice();
            }
        }

        if (ObjectUtils.isNotEmpty(CNYPriceDecimal) && ObjectUtils.isNotEmpty(price)) {
            if (ObjectUtils.isNotEmpty(currencyData.getPrice())) {
                priceDecimal = price.multiply(CNYPriceDecimal).setScale(2, RoundingMode.FLOOR);
                numTv.setText(priceDecimal.toPlainString());
                mgpPriceTv.setText(String.format(getString(R.string.str_current_mgp_price), "￥" + priceDecimal.toPlainString()));
            }
        } else {
            priceDecimal = price.setScale(2, RoundingMode.FLOOR);
            numTv.setText(priceDecimal.toPlainString());
            mgpPriceTv.setText(String.format(getString(R.string.str_current_mgp_price), "$" + priceDecimal.toPlainString()));
        }
        maxCNYDecimal = balanceDecimal.multiply(price).multiply(CNYPriceDecimal);
        minSaleAmountEt.setFilters(new InputFilter[]{new CashierInputFilter(maxCNYDecimal, 2)});
        toCNY();
    }

    private void toCNY() {
//        CNYDecimal = priceDecimal.multiply(CNYPriceDecimal).divide(currencyDecimal, 2, RoundingMode.FLOOR);
//        cnyTv.setText("≈ " + CNYDecimal.toPlainString() + " CNY");
    }

    private void onIsBindSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean isBindBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (isBindBean != null)
                isBind = isBindBean.getData() == 0 ? true : false;
        }
        updataErrerView();
    }

    private void onWalletBalance(BigDecimal bigDecimal) {
        dismissTipDialog();
        if (ObjectUtils.isEmpty(bigDecimal)) {
            bigDecimal = BigDecimal.ZERO;
        }
        balanceDecimal = bigDecimal;
        quantityEt.setFilters(new InputFilter[]{new CashierInputFilter(balanceDecimal, 4)});
        balanceTv.setText(String.format(getString(R.string.str_balance_value), bigDecimal.setScale(4).toPlainString() + " MGP"));
    }

    private void onTableRows(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            TableRowsBean tableRowsBean = (TableRowsBean) o;
            List<TableRowsBean.RowsBean> rowsBeanList = tableRowsBean.getRows();
            if (ObjectUtils.isEmpty(rowsBeanList)) {
                remaining = "0";
            } else {
                TableRowsBean.RowsBean rowsBean = rowsBeanList.get(0);
                remaining = rowsBean.getRemaining().split(" ")[0];
            }
            BigDecimal bdRemaining = new BigDecimal(remaining);
            if (bdRemaining.compareTo(BigDecimal.ZERO) > 0) {//-1表示小于，0是等于，1是大于。
                isPledge = true;
            } else {
                isPledge = false;
            }
        }
        updataErrerView();
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isBind) {
            isBind();
        }
        if (!isPledge) {
            isPosPledge();
        }
    }
}
