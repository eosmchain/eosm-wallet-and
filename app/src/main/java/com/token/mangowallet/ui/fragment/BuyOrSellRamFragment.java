package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.ui.viewmodel.ResManModelFactory;
import com.token.mangowallet.ui.viewmodel.ResManViewModel;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.ConversionUnits;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.view.BatteryView;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.BUYRAM_ACTION;
import static com.token.mangowallet.utils.Constants.DELEGATEBW_ACTION;
import static com.token.mangowallet.utils.Constants.EOSIO_SYSTEM_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_ACCOUNT_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_TRANSACTION;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.SELLRAM_ACTION;
import static com.token.mangowallet.utils.Constants.UNDELEGATEBW_ACTION;
import static com.token.mangowallet.utils.Constants.percent;

public class BuyOrSellRamFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.RAMView)
    BatteryView RAMView;
    @BindView(R.id.availableRAMTv)
    AppCompatTextView availableRAMTv;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.currentPriceTv)
    AppCompatTextView currentPriceTv;
    @BindView(R.id.buyChBox)
    MaterialCheckBox buyChBox;
    @BindView(R.id.saleChBox)
    MaterialCheckBox saleChBox;
    @BindView(R.id.balanceTv)
    MaterialTextView balanceTv;
    @BindView(R.id.EOSAmountEt)
    TextInputEditText EOSAmountEt;
    @BindView(R.id.receptionAccountEt)
    TextInputEditText receptionAccountEt;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.transactionBtn)
    QMUIRoundButton transactionBtn;
    @BindView(R.id.dealTipsTv)
    MaterialTextView dealTipsTv;
    @BindView(R.id.ramQuantityTv)
    MaterialTextView ramQuantityTv;

    private Unbinder unbinder;
    private ResManModelFactory resManModelFactory;
    private ResManViewModel resManViewModel;

    private boolean isBuy;
    private String accountInfoJSON;
    private String walletAddress;
    private AccountInfo accountInfo;
    private BigDecimal bdRamQuota;//内存总量
    private BigDecimal bdRamUsage;//已用内存
    private BigDecimal bdRamResidue;//剩余内存
    private MangoWallet mangoWallet;
    private String privatekey;
    private String balance;
    private String ramAvailable;
    private QMUIDialog qmuiDialog;
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_buysell, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initView() {
        topbar.setTitle(StringUtils.getString(R.string.str_ram));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        ViewUtils.cashierFiltersEditText(EOSAmountEt);
        receptionAccountEt.setText(walletAddress);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        privatekey = mangoWallet.getPrivateKey();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        isBuy = bundle.getBoolean(EXTRA_TRANSACTION, true);
        accountInfoJSON = bundle.getString(EXTRA_ACCOUNT_INFO, "");
        if (!ObjectUtils.isEmpty(accountInfoJSON))
            accountInfo = GsonUtils.fromJson(accountInfoJSON, AccountInfo.class);
        if (!ObjectUtils.isEmpty(accountInfo))
            walletAddress = accountInfo.getAccount_name();

        resManModelFactory = new ResManModelFactory();
        resManViewModel = ViewModelProviders.of(this.getActivity(), resManModelFactory)
                .get(ResManViewModel.class);
        resManViewModel.prepare(mangoWallet);
        resManViewModel.tableRows().observe(this, this::onTableRows);
        resManViewModel.accountInfo().observe(this, this::onAccountInfo);
        resManViewModel.transaction().observe(this, this::onTransaction);

        Map map = MapUtils.newHashMap();
        map.put("code", "eosio");
        map.put("scope", "eosio");
        map.put("table", "rammarket");
        map.put("json", true);
        resManViewModel.fetchTableRows(map);
        showTipDialog(getString(R.string.str_loading));
    }

    @Override
    protected void initAction() {
        checkBoxListener(buyChBox);
        checkBoxListener(saleChBox);
    }

    private void onTableRows(TableRowsBean tableRowsBean) {
        dismissTipDialog();
        if (tableRowsBean != null) {
            List<TableRowsBean.RowsBean> rows = tableRowsBean.getRows();
            if (ObjectUtils.isNotEmpty(rows)) {
                TableRowsBean.RowsBean rowsBean = rows.get(0);
                TableRowsBean.RowsBean.BaseBean baseBean = rowsBean.getBase();
                TableRowsBean.RowsBean.QuoteBean quoteBean = rowsBean.getQuote();
                String baseBalance = baseBean.getBalance().split(" ")[0];
                String quoteBalance = quoteBean.getBalance().split(" ")[0];
                BigDecimal bdbaseBalance = new BigDecimal(baseBalance);
                BigDecimal bdQuoteBalance = new BigDecimal(quoteBalance);
                BigDecimal bdKB = new BigDecimal(1024);
                String RAMCurrentPrice = bdQuoteBalance.divide(bdbaseBalance.divide(bdKB, 4, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP).toPlainString();
                currentPriceTv.setText(RAMCurrentPrice + " MGP/KB");
            }
        }
    }

    private void onAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        updataView();
        dismissTipDialog();
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean.isSuccess) {
            ToastUtils.showShort(R.string.str_transaction_success);
        } else {
            ToastUtils.showLong(transactionBean.msg);
        }
    }

    private void updataView() {
        if (!ObjectUtils.isEmpty(accountInfo)) {
            bdRamQuota = accountInfo.getRam_quota();
            String ramQuota = ConversionUnits.getRAMConversionUnit(bdRamQuota.longValue());
            bdRamUsage = accountInfo.getRam_usage();
            bdRamResidue = bdRamQuota.subtract(bdRamUsage);
            ramAvailable = ConversionUnits.getRAMConversionUnit(bdRamResidue.longValue());
            availableRAMTv.setText(ramAvailable + "/" + ramQuota);

            BigDecimal bigDecimal = bdRamResidue.divide(bdRamQuota, 2, BigDecimal.ROUND_HALF_DOWN).multiply(percent);
            int RamPercent = bigDecimal.intValue();
            RAMView.setPower(RamPercent);
            balance = accountInfo.getCore_liquid_balance();
            if (ObjectUtils.isEmpty(balance)) {
                balance = "0.0000 MGP";
            }
        }
        updatacheckBoxView();
    }

    private void checkBoxListener(MaterialCheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.getId() == buyChBox.getId()) {
                    isBuy = isChecked ? true : false;
                } else {
                    isBuy = isChecked ? false : true;
                }
                updatacheckBoxView();
            }
        });
    }

    private void updatacheckBoxView() {
        balanceTv.setText(String.format(getString(R.string.str_balance_value), isBuy ? balance : ramAvailable));
        ViewUtils.setEditableEditText(receptionAccountEt, isBuy);
        dealTipsTv.setVisibility(isBuy ? View.VISIBLE : View.GONE);
        buyChBox.setChecked(isBuy);
        saleChBox.setChecked(!isBuy);
        transactionBtn.setText(isBuy ? getString(R.string.str_buy) : getString(R.string.str_sale));
        EOSAmountEt.setHint(isBuy ? getString(R.string.str_enter_quantity_number) : getString(R.string.str_input_quantity_ram));
        ramQuantityTv.setText(isBuy ? getString(R.string.str_purchase_quantity) : getString(R.string.str_sellram));
    }

    @OnClick(R.id.transactionBtn)
    public void onViewClicked() {
        /**
         * 参考：https://www.jianshu.com/p/eb1a26e3ad4a
         * params
         *{
         * "code": "eosio",
         * "action": "buyram",
         * "args": {
         *  "payer": "testnetyy111",
         *  "receiver": "testnetyy111",
         * "quant": "100.0000 EOS",
         * }
         * }
         */
        String quantity = EOSAmountEt.getText().toString().trim();
        if (ObjectUtils.isEmpty(quantity) || ObjectUtils.equals("0", quantity)) {
            ToastUtils.showLong(StringUtils.getString(R.string.str_quantity_less_than_0));
            return;
        }
        String aName = receptionAccountEt.getText().toString().trim();
        if (ObjectUtils.isEmpty(aName)) {
            ToastUtils.showLong(StringUtils.getString(R.string.str_enter_receiving_account));
            return;
        }
        if (qmuiDialog == null) {
            qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
        qmuiDialog.show();

    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                RAMTransaction();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };


    private void RAMTransaction() {
        String aName = receptionAccountEt.getText().toString().trim();
        String quantity = EOSAmountEt.getText().toString().trim();
        BigDecimal bdQuant = new BigDecimal(quantity);

        Map<String, Object> map;
        if (isBuy) {
            quantity = bdQuant.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();
            map = EOSParams.getBuyRAMPamars(walletAddress, aName, quantity + " " + walletType);
        } else {
            quantity = bdQuant.toPlainString();
            map = EOSParams.getSellRAMPamars(walletAddress, quantity);
        }
        String jsonData = GsonUtils.toJson(map);
        LogUtils.dTag(Constants.LOG_TAG, "params = " + jsonData);
        resManViewModel.sendTransaction(isBuy ? BUYRAM_ACTION : SELLRAM_ACTION, EOSIO_SYSTEM_CONTRACT_CODE, jsonData);
    }
}
