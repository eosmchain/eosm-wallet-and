package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.ui.viewmodel.ResManModelFactory;
import com.token.mangowallet.ui.viewmodel.ResManViewModel;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.ConversionUnits;
import com.token.mangowallet.view.BatteryView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_ACCOUNT_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_TRANSACTION;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.percent;
import static com.token.mangowallet.utils.Constants.thousand;


public class ResourceAdministrationFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.totalAssetsTv)
    AppCompatTextView totalAssetsTv;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.redemptionTv)
    AppCompatTextView redemptionTv;
    @BindView(R.id.pledgeTv)
    AppCompatTextView pledgeTv;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.RAMView)
    BatteryView RAMView;
    @BindView(R.id.availableRAMTv)
    AppCompatTextView availableRAMTv;
    @BindView(R.id.buyBtn)
    QMUIRoundButton buyBtn;
    @BindView(R.id.saleBtn)
    QMUIRoundButton saleBtn;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.CPUView)
    BatteryView CPUView;
    @BindView(R.id.availableCPUTv)
    AppCompatTextView availableCPUTv;
    @BindView(R.id.broadbandiew)
    BatteryView broadbandiew;
    @BindView(R.id.availableBroadbandTv)
    AppCompatTextView availableBroadbandTv;
    @BindView(R.id.pledgeBtn)
    QMUIRoundButton pledgeBtn;
    @BindView(R.id.ransomBtn)
    QMUIRoundButton ransomBtn;
    @BindView(R.id.layout3)
    LinearLayout layout3;

    private boolean isBuy = true;
    private boolean isPledge = true;
    private String accountInfoJSON;
    private String walletAddress;
    private String pledge;//
    private BigDecimal bgNetWeight;//抵押网络
    private BigDecimal bgCpuWeight;//抵押CPU
    private BigDecimal bgPledge;//抵押总额
    private BigDecimal bdBalance;//EOS余额
    private BigDecimal bdRamQuota;//内存总量
    private BigDecimal bdRamUsage;//已用内存
    private BigDecimal bdRamResidue;//剩余内存
    private BigDecimal bdCPUMax;//CPU最大值
    private BigDecimal bdCPUUsed;//CPU已使用
    private BigDecimal bdCPUAvailable;//CPU可用
    private BigDecimal bdNetMax;//CPU最大值
    private BigDecimal bdNetUsed;//CPU已使用
    private BigDecimal bdNetAvailable;//CPU可用
    private BigDecimal bdRedemption;//赎回中

    private Unbinder unbinder;
    private String balance = "0";
    private String redemption = "0";
    private AccountInfo accountInfo;
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;

    private ResManModelFactory resManModelFactory;
    private ResManViewModel resManViewModel;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_resource_administration, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }


    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        resManModelFactory = new ResManModelFactory();
        resManViewModel = ViewModelProviders.of(this.getActivity(), resManModelFactory)
                .get(ResManViewModel.class);
        walletAddress = mangoWallet.getWalletAddress();
        resManViewModel.prepare(mangoWallet);
        showTipDialog(getString(R.string.str_loading));
    }

    @Override
    protected void initView() {
        topbar.setTitle(getString(R.string.str_resource_administration));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        resManViewModel.defaultWallet().observe(this, this::showWallet);
        resManViewModel.accountInfo().observe(this, this::onAccountInfo);
    }

    @Override
    protected void initAction() {

    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        updateView();
    }

    private void onAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        accountInfoJSON = GsonUtils.toJson(accountInfo);
        LogUtils.dTag(LOG_TAG, "accountInfoJSON = " + accountInfoJSON);
        dismissTipDialog();
        updateView();

    }

    private void updateView() {
        if (!ObjectUtils.isEmpty(accountInfo)) {
            AccountInfo.RefundRequestBean refundRequestBean = accountInfo.getRefund_request();
            String aCPU = "0";
            String bNET = "0";
            if (refundRequestBean != null) {
                String redeemCPU = refundRequestBean.getCpu_amount();
                String redeemNET = refundRequestBean.getNet_amount();
                if (!ObjectUtils.isEmpty(redeemCPU)) {
                    String[] a = redeemCPU.split(" ");
                    aCPU = a[0];
                }
                if (!ObjectUtils.isEmpty(redeemNET)) {
                    String[] b = redeemNET.split(" ");
                    bNET = b[0];
                }
            }
            bdRedemption = new BigDecimal(aCPU).add(new BigDecimal(bNET));
            redemption = bdRedemption.setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();
            redemptionTv.setText(redemption + " " + walletType);

            bgNetWeight = accountInfo.getNet_weight();
            bgCpuWeight = accountInfo.getCpu_weight();
            bgPledge = (bgNetWeight.add(bgCpuWeight)).divide(thousand, 4, RoundingMode.CEILING);
            pledge = bgPledge.toPlainString();
            pledgeTv.setText(pledge + " " + walletType);//抵押
            balance = accountInfo.getCore_liquid_balance();
            if (ObjectUtils.isEmpty(balance)) {
                balance = "0.0000 MGP";
            }
            balanceTv.setText(balance);//余额

            String[] bArr = balance.split(" ");
            String b = bArr[0];
            bdBalance = new BigDecimal(b);
            String total = bgPledge.add(bdBalance).add(bdRedemption).setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();
            totalAssetsTv.setText(total + " " + walletType);//总资产

            bdRamQuota = accountInfo.getRam_quota();
            String ramQuota = ConversionUnits.getRAMConversionUnit(bdRamQuota.longValue());
            bdRamUsage = accountInfo.getRam_usage();
            bdRamResidue = bdRamQuota.subtract(bdRamUsage);
            String ramAvailable = ConversionUnits.getRAMConversionUnit(bdRamResidue.longValue());
            availableRAMTv.setText(ramAvailable + "/" + ramQuota);

            BigDecimal bigDecimal = bdRamResidue.divide(bdRamQuota, 2, BigDecimal.ROUND_HALF_DOWN).multiply(percent);
            int RamPercent = bigDecimal.intValue();
            RAMView.setPower(RamPercent);

            AccountInfo.CpuLimitBean cpu_limit = accountInfo.getCpu_limit();
            bdCPUMax = cpu_limit.getMax();
//            bdCPUUsed = cpu_limit.getUsed();
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
            broadbandiew.setPower(NetPercent);

            String netMax = ConversionUnits.getRAMConversionUnit(bdNetMax.longValue());
            String netAvailable = ConversionUnits.getRAMConversionUnit(bdNetAvailable.longValue());
            availableBroadbandTv.setText(netAvailable + "/" + netMax);
        }
    }

    @OnClick({R.id.buyBtn, R.id.saleBtn, R.id.pledgeBtn, R.id.ransomBtn})
    public void onViewClicked(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.buyBtn:
                isBuy = true;
                bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putBoolean(EXTRA_TRANSACTION, isBuy);
                bundle.putString(EXTRA_ACCOUNT_INFO, accountInfoJSON);
                startFragment("BuyOrSellRamFragment", bundle);
                break;
            case R.id.saleBtn:
                isBuy = false;
                bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putBoolean(EXTRA_TRANSACTION, isBuy);
                bundle.putString(EXTRA_ACCOUNT_INFO, accountInfoJSON);
                startFragment("BuyOrSellRamFragment", bundle);
                break;
            case R.id.pledgeBtn:
                isPledge = true;
                bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putString(EXTRA_ACCOUNT_INFO, accountInfoJSON);
                bundle.putBoolean(EXTRA_TRANSACTION, isPledge);
                startFragment("MortgageOrRedeemFragment", bundle);
                break;
            case R.id.ransomBtn:
                isPledge = false;
                bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putString(EXTRA_ACCOUNT_INFO, accountInfoJSON);
                bundle.putBoolean(EXTRA_TRANSACTION, isPledge);
                startFragment("MortgageOrRedeemFragment", bundle);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mangoWallet != null) {
            resManViewModel.prepare(mangoWallet);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();
    }
}
