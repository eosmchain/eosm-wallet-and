package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.qmuiteam.qmui.widget.QMUISlider;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.DelegatebwParams;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.viewmodel.ResManModelFactory;
import com.token.mangowallet.ui.viewmodel.ResManViewModel;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.ConversionUnits;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.view.BatteryView;
import com.token.mangowallet.view.CashierInputFilter;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.DELEGATEBW_ACTION;
import static com.token.mangowallet.utils.Constants.EOSIO_SYSTEM_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_ACCOUNT_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_TRANSACTION;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.UNDELEGATEBW_ACTION;
import static com.token.mangowallet.utils.Constants.isTest;
import static com.token.mangowallet.utils.Constants.percent;
import static com.token.mangowallet.utils.Constants.thousand;


public class MortgageOrRedeemFragment extends BaseFragment implements QMUISlider.Callback {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.CPUView)
    BatteryView CPUView;
    @BindView(R.id.CPUPledgeAmountTv)
    AppCompatTextView CPUPledgeAmountTv;
    @BindView(R.id.availableCPUTv)
    AppCompatTextView availableCPUTv;
    @BindView(R.id.NETView)
    BatteryView NETView;
    @BindView(R.id.NETPledgeAmountTv)
    AppCompatTextView NETPledgeAmountTv;
    @BindView(R.id.availableNETTv)
    AppCompatTextView availableNETTv;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.pledgeChBox)
    MaterialCheckBox pledgeChBox;
    @BindView(R.id.ransomChBox)
    MaterialCheckBox ransomChBox;
    @BindView(R.id.receptionAccountEt)
    TextInputEditText receptionAccountEt;
    @BindView(R.id.balanceTv1)
    MaterialTextView balanceTv1;
    @BindView(R.id.EOSAmountEt1)
    TextInputEditText EOSAmountEt1;
    @BindView(R.id.balanceTv2)
    MaterialTextView balanceTv2;
    @BindView(R.id.EOSAmountEt2)
    TextInputEditText EOSAmountEt2;
    @BindView(R.id.undelegatebwLayout)
    LinearLayout undelegatebwLayout;
    @BindView(R.id.CPURatesTv)
    MaterialTextView CPURatesTv;
    @BindView(R.id.broadbandRatesTv)
    MaterialTextView broadbandRatesTv;
    @BindView(R.id.mortgageRatesSlider)
    QMUISlider mortgageRatesSlider;
    @BindView(R.id.delegatebwLayout)
    LinearLayout delegatebwLayout;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.mortgageBtn)
    QMUIRoundButton mortgageBtn;
    @BindView(R.id.text1)
    MaterialTextView text1;
    @BindView(R.id.text2)
    MaterialTextView text2;

    private Unbinder unbinder;
    private boolean isPledge;
    private String bntText;
    private String accountInfoJSON;
    private AccountInfo accountInfo;
    private BigDecimal bdCPUMax;//CPU最大值
    private BigDecimal bdCPUUsed;//CPU已使用
    private BigDecimal bdCPUAvailable;//CPU可用
    private BigDecimal bdNetMax;//CPU最大值
    private BigDecimal bdNetUsed;//CPU已使用
    private BigDecimal bdNetAvailable;//CPU可用
    private BigDecimal bgNetWeight;//抵押网络
    private BigDecimal bgCpuWeight;//抵押CPU
    //    private BigDecimal bdPledge;
    private BigDecimal bdBalance;
    private String pledgeNet = "0";
    private String pledgeCpu = "0";
    private String walletAddress;
    private int progress = 40;
    //    private String pledge = "0";
    private String cpu_quantity = "0";
    private String net_quantity = "0";
    private BigDecimal bdProgress;
    private String amount = "0";
    private BigDecimal bdAmount;
    private String privatekey;
    private String balance;
    private String balanceUnit;
    private QMUIDialog qmuiDialog;
    private String selfPledgeNet = "0";
    private String selfPledgeCpu = "0";
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;

    private ResManModelFactory resManModelFactory;
    private ResManViewModel resManViewModel;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_un_delegatebw, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        accountInfoJSON = bundle.getString(EXTRA_ACCOUNT_INFO, "");
        isPledge = bundle.getBoolean(EXTRA_TRANSACTION, true);
        resManModelFactory = new ResManModelFactory();
        resManViewModel = ViewModelProviders.of(this.getActivity(), resManModelFactory)
                .get(ResManViewModel.class);
        resManViewModel.prepare(mangoWallet);
        resManViewModel.defaultWallet().observe(this, this::showWallet);
        resManViewModel.accountInfo().observe(this, this::onAccountInfo);
        resManViewModel.transaction().observe(this, this::onTransaction);
        showTipDialog(getString(R.string.str_loading));
        if (!ObjectUtils.isEmpty(accountInfoJSON))
            accountInfo = GsonUtils.fromJson(accountInfoJSON, AccountInfo.class);
    }

    @Override
    protected void initView() {
        topbar.setTitle(getString(R.string.str_network));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        updateView();
    }

    @Override
    protected void initAction() {
        ViewUtils.setEditableEditText(receptionAccountEt, false);
        ViewUtils.cashierFiltersEditText(EOSAmountEt1);
        ViewUtils.cashierFiltersEditText(EOSAmountEt2);
        InputFilter[] filters = {ViewUtils.filter};
        EOSAmountEt1.setFilters(filters);
        mortgageRatesSlider.setCallback(this);
        pledgeChBox.setChecked(isPledge);
        ransomChBox.setChecked(!isPledge);
        checkBoxListener(pledgeChBox);
        checkBoxListener(ransomChBox);
        EOSAmountEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                输入之前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                正在输入
            }

            @Override
            public void afterTextChanged(Editable s) {
//                输入之后，一般就用这个
                amount = s.toString();
                if (!ObjectUtils.isEmpty(amount))
                    CPUproportionView(amount);
            }
        });
    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        updateView();
    }

    private void onAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        updateView();
        dismissTipDialog();
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean.isSuccess) {
            ToastUtils.showShort(R.string.str_transaction_success);
        } else {
            ToastUtils.showLong(transactionBean.msg);
        }
    }

    private void CPUproportionView(String amount) {
        bdProgress = new BigDecimal(progress);
        bdAmount = new BigDecimal(amount);
        BigDecimal bdCpuQuantity = bdAmount.multiply(bdProgress).divide(percent, 4, BigDecimal.ROUND_HALF_UP);
        cpu_quantity = bdCpuQuantity.stripTrailingZeros().toPlainString();
        BigDecimal bdNetQuantity = bdAmount.subtract(bdCpuQuantity);
        net_quantity = bdNetQuantity.stripTrailingZeros().toPlainString();
        CPURatesTv.setText(getString(R.string.str_cpu) + "≈" + cpu_quantity + " " + walletType);
        broadbandRatesTv.setText(getString(R.string.str_net) + "≈" + net_quantity + " " + walletType);
    }

    private void updateView() {
        mortgageRatesSlider.setCurrentProgress(progress);
        if (!ObjectUtils.isEmpty(accountInfo)) {
            walletAddress = accountInfo.getAccount_name();
            receptionAccountEt.setText(walletAddress);
            balanceUnit = accountInfo.getCore_liquid_balance();
            if (!ObjectUtils.isEmpty(balanceUnit)) {
                String[] arr = balanceUnit.split(" ");
                balance = arr[0];
            } else {
                balance = "0.0000";
            }
            AccountInfo.SelfDelegatedBandwidthBean selfDelegatedBandwidthBean = accountInfo.getSelf_delegated_bandwidth();
            String SCD = "0.0000 " + walletType;
            String SND = "0.0000 " + walletType;
            if (selfDelegatedBandwidthBean != null) {
                SCD = selfDelegatedBandwidthBean.getCpu_weight();
                SND = selfDelegatedBandwidthBean.getNet_weight();
            }
            if (!ObjectUtils.isEmpty(SCD)) {
                String[] aSCD = SCD.split(" ");
                selfPledgeCpu = aSCD[0];
            } else {
                selfPledgeCpu = "0.0000";
            }
            if (!ObjectUtils.isEmpty(SND)) {
                String[] aSND = SND.split(" ");
                selfPledgeNet = aSND[0];
            } else {
                selfPledgeNet = "0.0000";
            }
            bgCpuWeight = accountInfo.getCpu_weight();
            bgNetWeight = accountInfo.getNet_weight();
            bgCpuWeight = bgCpuWeight.divide(thousand, 4, BigDecimal.ROUND_HALF_DOWN);
            bgNetWeight = bgNetWeight.divide(thousand, 4, BigDecimal.ROUND_HALF_DOWN);
            pledgeCpu = bgCpuWeight.toPlainString();
            pledgeNet = bgNetWeight.toPlainString();

            CPUPledgeAmountTv.setText(pledgeCpu + " " + walletType);
            NETPledgeAmountTv.setText(pledgeNet + " " + walletType);

            text2.setText(getString(R.string.str_redemption_net));
            balanceTv2.setText(String.format(getString(R.string.str_balance_value), selfPledgeNet + " " + walletType));

            AccountInfo.CpuLimitBean cpu_limit = accountInfo.getCpu_limit();
            bdCPUMax = cpu_limit.getMax();
            bdCPUUsed = cpu_limit.getUsed();
            bdCPUAvailable = cpu_limit.getAvailable();

            int CPUPercent = bdCPUAvailable.divide(bdCPUMax, 2, BigDecimal.ROUND_HALF_DOWN).multiply(percent).intValue();
            CPUView.setPower(CPUPercent);
            String availableCPU = ConversionUnits.getCPUConversionUnit(bdCPUAvailable.longValue());
            String maxCPU = ConversionUnits.getCPUConversionUnit(bdCPUMax.longValue());
            availableCPUTv.setText(availableCPU + "/" + maxCPU);

            AccountInfo.NetLimitBean net_limit = accountInfo.getNet_limit();
            bdNetMax = net_limit.getMax();
            bdNetUsed = net_limit.getUsed();
            bdNetAvailable = net_limit.getAvailable();
            int NetPercent = bdNetAvailable.divide(bdNetMax, 2, BigDecimal.ROUND_HALF_DOWN).multiply(percent).intValue();
            NETView.setPower(NetPercent);

            String netMax = ConversionUnits.getRAMConversionUnit(bdNetMax.longValue());
            String netAvailable = ConversionUnits.getRAMConversionUnit(bdNetAvailable.longValue());
            availableNETTv.setText(netAvailable + "/" + netMax);
        }
        isDelegatebwLayoutView();
    }

    private void isDelegatebwLayoutView() {
        EOSAmountEt1.setText("");
        EOSAmountEt2.setText("");
        delegatebwLayout.setVisibility(isPledge ? View.VISIBLE : View.GONE);
        undelegatebwLayout.setVisibility(isPledge ? View.GONE : View.VISIBLE);
        pledgeChBox.setChecked(isPledge);
        ransomChBox.setChecked(!isPledge);
        mortgageBtn.setText(isPledge ? getString(R.string.str_pledge) : getString(R.string.str_redemption));
        if (isPledge) {
            text1.setText(getString(R.string.str_pledge_amount));
            balanceTv1.setText(String.format(getString(R.string.str_balance_value), balance + " " + walletType));
        } else {
            text1.setText(getString(R.string.str_redemption_cpu));
            balanceTv1.setText(String.format(getString(R.string.str_balance_value), selfPledgeCpu + " " + walletType));
        }
    }

    private void checkBoxListener(MaterialCheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.getId() == pledgeChBox.getId()) {
                    isPledge = isChecked ? true : false;
                } else {
                    isPledge = isChecked ? false : true;
                }
                isDelegatebwLayoutView();
            }
        });
    }

    private String jsonData;

    @OnClick(R.id.mortgageBtn)
    public void onViewClicked() {
        /**
         * 参考：https://www.jianshu.com/p/c64d33d457b6
         *   1、 A以租借的形式为B抵押资源，A可以赎回，B无法赎回
         *   2、 A以赠送的方式为B抵押资源，A无法赎回，B可以赎回
         *   3、 B自己抵押资源并且可以赎回
         *   4、 transfer为0，表示是以delegated租借的方式抵押资源；为1，表示是以delegated赠送的方式抵押资源
         * 一、抵押
         * params
         * {
         *     "code": "eosio",
         *     "action": "delegatebw",
         *     "args": {
         *         "from": "testnetyy111",
         *         "receiver": "testnetstake",
         *         "stake_net_quantity": "100.0000 EOS",
         *         "stake_cpu_quantity": "100.0000 EOS",
         *         "transfer": 0,
         *     }
         * }
         * 二、赎回
         * params
         * {
         *     "code": "eosio",
         *     "action": "undelegatebw",
         *     "args": {
         *         "from": "testnetstake",
         *         "receiver": "testnetstake",
         *         "unstake_net_quantity": "100.0000 EOS",
         *         "unstake_cpu_quantity": "100.0000 EOS"
         *     }
         * }
         */
        int transfer = 0;
        amount = EOSAmountEt1.getText().toString().trim();

        if (ObjectUtils.isEmpty(amount)) {
            ToastUtils.showShort(getString(R.string.str_quantity_less_than_0));
            return;
        }

        CPUproportionView(amount);
        BigDecimal bdCpu_quantity;
        BigDecimal bdNet_quantity;
        if (isPledge) {
            if (bdAmount.compareTo(new BigDecimal(0)) <= 0) {
                ToastUtils.showShort(getString(R.string.str_quantity_less_than_0));
                return;
            }

            if (bdAmount.compareTo(new BigDecimal(balance)) == 1) {
                ToastUtils.showShort(getString(R.string.str_no_more_balance));
                return;
            }

            BigDecimal bdCpuQuantity = bdAmount.multiply(bdProgress).divide(percent, 4, BigDecimal.ROUND_HALF_UP);
            cpu_quantity = bdCpuQuantity.stripTrailingZeros().toPlainString();
            BigDecimal bdNetQuantity = bdAmount.subtract(bdCpuQuantity);
            net_quantity = bdNetQuantity.stripTrailingZeros().toPlainString();

            bdCpu_quantity = new BigDecimal(cpu_quantity);
            cpu_quantity = bdCpu_quantity.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();

            bdNet_quantity = new BigDecimal(net_quantity);
            net_quantity = bdNet_quantity.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();

            Map delegatebwParams = EOSParams.getDelegatebw(walletAddress, walletAddress, net_quantity  + " " + walletType,
                    cpu_quantity + " " + walletType, false);
            jsonData = GsonUtils.toJson(delegatebwParams);
//            '["masteraychen","dragonmaster","1.0000 MGP", "1.0000 MGP",0]
//            List list = new ArrayList();
//            list.add(walletAddress);
//            list.add(walletAddress);
//            list.add(cpu_quantity + " " + walletType);
//            list.add(net_quantity + " " + walletType);
//            list.add(transfer);
//            DelegatebwParams delegatebwParams = new DelegatebwParams();
//            delegatebwParams.setFrom(walletAddress);
//            delegatebwParams.setReceiver(walletAddress);
//            delegatebwParams.setStake_cpu_quantity(cpu_quantity + " " + walletType);
//            delegatebwParams.setStake_net_quantity(net_quantity + " " + walletType);
//            delegatebwParams.setTransfer(transfer);
//            jsonData = GsonUtils.toJson(list);
        } else {
            String amount2 = EOSAmountEt2.getText().toString().trim();
            if (ObjectUtils.isEmpty(amount2)) {
                ToastUtils.showShort(getString(R.string.str_quantity_less_than_0));
                return;
            }
            BigDecimal bdAmount2 = new BigDecimal(amount2);
            if (bdAmount.compareTo(new BigDecimal(selfPledgeCpu)) == 1) {
                ToastUtils.showShort(R.string.str_redemption_cpu_restrict);
                return;
            }
            if (bdAmount2.compareTo(new BigDecimal(selfPledgeNet)) == 1) {
                ToastUtils.showShort(R.string.str_redemption_net_restrict);
                return;
            }
            cpu_quantity = amount;
            net_quantity = amount2;

            bdCpu_quantity = new BigDecimal(cpu_quantity);
            cpu_quantity = bdCpu_quantity.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();

            bdNet_quantity = new BigDecimal(net_quantity);
            net_quantity = bdNet_quantity.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();

            Map undelegatebwParams = EOSParams.getUnDelegatebw(walletAddress, walletAddress, cpu_quantity + " " + walletType,
                    net_quantity + " " + walletType);

            jsonData = GsonUtils.toJson(undelegatebwParams);
        }
        if (qmuiDialog == null) {
            qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
        qmuiDialog.show();
        LogUtils.dTag(Constants.LOG_TAG, "params = " + jsonData);

    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                NETCPUTransaction();
            } else {
                ToastUtils.showShort(getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    private void NETCPUTransaction() {
        if (isPledge) {
            EMWalletRepository emWalletRepository = new EMWalletRepository();
            emWalletRepository.sendTransaction(DELEGATEBW_ACTION, mangoWallet.getPrivateKey(), walletAddress, EOSIO_SYSTEM_CONTRACT_CODE, jsonData, walletType)
                    .subscribe(this::onTransaction, this::onError);
        } else {
            resManViewModel.sendTransaction(isPledge ? DELEGATEBW_ACTION : UNDELEGATEBW_ACTION, EOSIO_SYSTEM_CONTRACT_CODE, jsonData);
        }
        jsonData = "";
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    @Override
    public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
        this.progress = progress;
        LogUtils.dTag(Constants.LOG_TAG, "progress = " + progress);
        if (!ObjectUtils.isEmpty(amount))
            CPUproportionView(amount);
    }

    @Override
    public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {

    }

    @Override
    public void onTouchUp(QMUISlider slider, int progress, int tickCount) {

    }

    @Override
    public void onStartMoving(QMUISlider slider, int progress, int tickCount) {

    }

    @Override
    public void onStopMoving(QMUISlider slider, int progress, int tickCount) {

    }

    @Override
    public void onResume() {
        super.onResume();
        resManViewModel.prepare();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();

    }
}
