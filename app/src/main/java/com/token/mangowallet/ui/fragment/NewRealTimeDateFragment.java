package com.token.mangowallet.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.CurrencyStatsBean;
import com.token.mangowallet.bean.RealTimeIndexBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;
import static com.token.mangowallet.utils.Constants.bdCurrencyAmount;

public class NewRealTimeDateFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topbar;
    @BindView(R.id.tv_allTotal_coins)
    TextView tvAllTotalCoins;
    @BindView(R.id.tv_allTotal_coins_usd)
    TextView tvAllTotalCoinsUsd;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.tv_cumulative_destruction)
    TextView tvCumulativeDestruction;
    @BindView(R.id.ll_cumulative_destruction)
    LinearLayout llCumulativeDestruction;
    @BindView(R.id.tv_real_time_price)
    TextView tvRealTimePrice;
    @BindView(R.id.tv_real_time_circulation)
    TextView tvRealTimeCirculation;
    @BindView(R.id.tv_market_value_of_circulation)
    TextView tvMarketValueOfCirculation;
    @BindView(R.id.tv_all_network_mortgage)
    TextView tvAllNetworkMortgage;
    @BindView(R.id.tv_mortgage_value)
    TextView tvMortgageValue;
    @BindView(R.id.tv_operation_and_construction)
    TextView tvOperationAndConstruction;
    @BindView(R.id.ll_yunying)
    LinearLayout llYunying;
    @BindView(R.id.tv_ecological_construction)
    TextView tvEcologicalConstruction;
    @BindView(R.id.tv_ecological_fund)
    TextView tvEcologicalFund;
    @BindView(R.id.ll_shengtai)
    LinearLayout llShengtai;
    @BindView(R.id.tv_team_motivation)
    TextView tvTeamMotivation;
    @BindView(R.id.ll_tuandui)
    LinearLayout llTuandui;
    @BindView(R.id.appsItemLayout)
    QMUILinearLayout appsItemLayout;
    @BindView(R.id.mappingIndexTv)
    TextView mappingIndexTv;
    @BindView(R.id.tv_latest_block)
    TextView tvLatestBlock;
    @BindView(R.id.tv_block_reward)
    TextView tvBlockReward;
    @BindView(R.id.tv_produced)
    TextView tvProduced;
    @BindView(R.id.tv_surplus_produced)
    TextView tvSurplusProduced;
    @BindView(R.id.ll_LatestBlock)
    LinearLayout llLatestBlock;
    @BindView(R.id.ll_BlockReward)
    LinearLayout llBlockReward;
    @BindView(R.id.ll_produced)
    LinearLayout llProduced;
    @BindView(R.id.ll_surplusProduced)
    LinearLayout llSurplusProduced;

    private Unbinder unbinder;
    private RealTimeIndexBean realTimeIndexBean;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private BigDecimal bdDestruction = new BigDecimal("2100000");
    private BigDecimal bdMiningpool = new BigDecimal("350000000");
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal bdCurrentAmount = BigDecimal.ZERO;//目前币量 做市币量
    private BigDecimal bdSoTheMortgage = BigDecimal.ZERO;//全网抵押(接口返回)
    //    private BigDecimal bdMarketAmount = new BigDecimal("1100000");//做市币量（固定110万）
    private BigDecimal bdMangochain33 = BigDecimal.ZERO;//运营(mangochain33)
    private BigDecimal bdMangochain11 = BigDecimal.ZERO;//团队(mangochain11)
    private BigDecimal bdMgpchain2222 = BigDecimal.ZERO;//mgpchain2222(余额)
    private BigDecimal bdMangochain15 = BigDecimal.ZERO;//mangochain15(余额)
    private BigDecimal bdMasteraychen = BigDecimal.ZERO;//masteraychen(余额)
    private BigDecimal bdMgpbpvoting = BigDecimal.ZERO;//抵押投票总量(mgp.bpvoting)
    private BigDecimal bdEosioToken = BigDecimal.ZERO;//eosio.token(余额)
    private BigDecimal bdRealtimeCirculation = BigDecimal.ZERO;//实时流通
    private BigDecimal bdMangochain22 = BigDecimal.ZERO;//生态基金地址(余额)

    private boolean isPrice = false;//价格
    private boolean isSoTheMortgage = false;
    private boolean isMangochain33 = false;
    private boolean isMangochain11 = false;
    private boolean isMgpchain2222 = false;
    private boolean isMangochain15 = false;
    private boolean isMasteraychen = false;
    private boolean isMgpbpvoting = false;
    private boolean isCurrentAmount = false;
    private boolean isEosioToken = false;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_real_time_date, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        getPrice();
        getRealTimeIndex();
        getCurrencyStats();
        //地址:mangochain11 生态激励地址 47700000
        // 地址:mangochain22 生态基金地址  46760000
        // 地址:mangochain33 运营地址 9769290
        //实时流通 = 目前币量(eosio.token) - 全网抵押(接口返回) - 运营(mangochain33) - 团队(mangochain11)
        // - mgpchain2222(余额) - mangochain15(余额) - masteraychen(余额) - 抵押投票总量(mgp.bpvoting)
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain33", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 1);
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain11", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 2);
        getCurrencyBalance(EOSParams.getBalancePamars("mgpchain2222", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 3);
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain15", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 4);
        getCurrencyBalance(EOSParams.getBalancePamars("masteraychen", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 5);
        getCurrencyBalance(EOSParams.getBalancePamars("mgp.bpvoting", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 6);
//        getCurrencyBalance(EOSParams.getBalancePamars("eosio.token", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 7);
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain22", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 8);

    }

    @Override
    public void initView() {
        topbar.setTitle(StringUtils.getString(R.string.str_real_time_data));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        llProduced.setVisibility(View.GONE);
        llSurplusProduced.setVisibility(View.GONE);

    }

    @Override
    protected void initAction() {

    }


    @OnClick({R.id.ll_tuandui})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tuandui:
                break;
        }
    }

    private void getRealTimeIndex() {
        showTipDialog(getString(R.string.str_loading));
        try {
            NetWorkManager.getRequest().getRealTimeIndex()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::realTimeIndexSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPrice() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map map = MapUtils.newHashMap();
            map.put("pair", walletType + "_USDT");
            String json = GsonUtils.toJson(map);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCoinPrice(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::priceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void realTimeIndexSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        isSoTheMortgage = true;
        isEosioToken = true;
        if (jsonObject != null) {
            realTimeIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), RealTimeIndexBean.class);
            if (realTimeIndexBean != null) {
                if (realTimeIndexBean.getCode() == 0) {
                    RealTimeIndexBean.DataBean dataBean = realTimeIndexBean.getData();
                    if (dataBean != null) {
                        bdEosioToken = new BigDecimal(ObjectUtils.isEmpty(dataBean.getDestroyMgpNum()) ? "0" : dataBean.getDestroyMgpNum());
                        tvAllTotalCoins.setText(APPUtils.dataFormat(bdCurrencyAmount.subtract(bdEosioToken).toPlainString()));
                        tvCumulativeDestruction.setText(APPUtils.dataFormat(bdEosioToken.toPlainString()));
                        bdSoTheMortgage = new BigDecimal(ObjectUtils.isEmpty(dataBean.getMgpNum()) ? "0" : dataBean.getMgpNum());
                        tvAllNetworkMortgage.setText(APPUtils.dataFormat(ObjectUtils.isEmpty(dataBean.getMgpNum()) ? "0" : dataBean.getMgpNum()));
                        tvMortgageValue.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(dataBean.getMapValue()) ?
                                "0.00" : dataBean.getMapValue(), 2, RoundingMode.FLOOR));
                        mappingIndexTv.setText(APPUtils.dataFormat(ObjectUtils.isEmpty(dataBean.getTransferSpeed()) ? "0" : dataBean.getTransferSpeed()));
                        tvBlockReward.setText(APPUtils.dataFormat(ObjectUtils.isEmpty(dataBean.getTotalPower()) ? "0" : dataBean.getTotalPower()));
                        tvLatestBlock.setText(APPUtils.dataFormat(ObjectUtils.isEmpty(dataBean.getMinerPoolNum()) ? "0" : dataBean.getMinerPoolNum()));
                    }
                } else {
                    ToastUtils.showShort(realTimeIndexBean.getMsg());
                }
            }
        }
        realtimeCirculation();
    }

    private void priceSuccess(CurrencyPrice currencyPrice) {
        dismissTipDialog();
        isPrice = true;
        if (currencyPrice != null) {
            if (currencyPrice.getCode() == 0) {
                CurrencyPrice.DataBean dataBean = currencyPrice.getData();
                if (dataBean != null) {
                    price = dataBean.getPrice();
                }
                if (price == null) {
                    price = BigDecimal.ZERO;
                }
                tvRealTimePrice.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(price.toPlainString()) ?
                        "0.00" : price.toPlainString(), 4, RoundingMode.FLOOR));
            }
        }
        realtimeCirculation();
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        isPrice = true;
        isSoTheMortgage = true;
        isEosioToken = true;
        realtimeCirculation();
    }

    public void getCurrencyBalance(Map<String, Object> map, int type) {
        Observable.fromCallable(() -> MyApplication.repositoryFactory.customEosioJavaRpcProvider.getCurrencyBalance(EOSParams.getRequestBody(map)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<String>(getActivity(), true) {
//                    目前币量(eosio.token) - 全网抵押(接口返回) - 运营(mangochain33) - 团队(mangochain11)
                    // - mgpchain2222(余额) - mangochain15(余额) - masteraychen(余额) - 做市币量（固定110万）
                    // - 抵押投票总量(mgp.bpvoting)
//                    bdMangochain33 =BigDecimal.ZERO;//运营(mangochain33)1
//                    bdMangochain11 =BigDecimal.ZERO;//团队(mangochain11)2
//                    bdMgpchain2222 =BigDecimal.ZERO;//mgpchain2222(余额)3
//                    bdMangochain15 =BigDecimal.ZERO;//mangochain15(余额)4
//                    bdMasteraychen =BigDecimal.ZERO;//masteraychen(余额) 5
//                    bdMgpbpvoting =BigDecimal.ZERO;//抵押投票总量(mgp.bpvoting)6

                    @Override
                    public void onFail(String failMsg) {
                        String b = "0";
                        switch (type) {
                            case 1://mangochain33
                                isMangochain33 = true;
                                tvOperationAndConstruction.setText(APPUtils.dataFormat(b));
                                break;
                            case 2://bdMangochain11
                                isMangochain11 = true;
                                tvTeamMotivation.setText(APPUtils.dataFormat(b));
                                break;
                            case 3://bdMgpchain2222
                                isMgpchain2222 = true;
                                break;
                            case 4://bdMangochain15
                                isMangochain15 = true;
                                break;
                            case 5://bdMasteraychen
                                isMasteraychen = true;
                                break;
                            case 6://bdMgpbpvoting
                                isMgpbpvoting = true;
                                break;
//                            case 7://bdEosioToken(余额)
//                                isEosioToken = true;
//                                tvAllTotalCoins.setText(bdCurrencyAmount.subtract(bdEosioToken).toPlainString());
//                                tvCumulativeDestruction.setText(b);
//                                break;
                            case 8://生态基金 bdMangochain22
                                tvEcologicalFund.setText("0");
                                break;
                            default:
                                break;
                        }
                        realtimeCirculation();
                    }

                    @Override
                    public void onSuccess(String s) {//["161.4881 MGP"]
//                    bdMangochain33 =BigDecimal.ZERO;//运营(mangochain33)1
//                    bdMangochain11 =BigDecimal.ZERO;//团队(mangochain11)2
//                    bdMgpchain2222 =BigDecimal.ZERO;//mgpchain2222(余额)3
//                    bdMangochain15 =BigDecimal.ZERO;//mangochain15(余额)4
//                    bdMasteraychen =BigDecimal.ZERO;//masteraychen(余额) 5
//                    bdMgpbpvoting =BigDecimal.ZERO;//抵押投票总量(mgp.bpvoting)6
                        String b = "0";
                        if (!ObjectUtils.isEmpty(s)) {
                            if (s.contains("MGP")) {
                                b = s.split(" ")[0].replace("[\"", "");
                            }
                        }
                        switch (type) {
                            case 1://bdMangochain33
                                isMangochain33 = true;
                                bdMangochain33 = new BigDecimal(b);
                                tvOperationAndConstruction.setText(APPUtils.dataFormat(b));
                                break;
                            case 2://bdMangochain11
                                isMangochain11 = true;
                                bdMangochain11 = new BigDecimal(b);
                                tvTeamMotivation.setText(APPUtils.dataFormat(b));
                                break;
                            case 3://bdMgpchain2222
                                isMgpchain2222 = true;
                                bdMgpchain2222 = new BigDecimal(b);
                                break;
                            case 4://bdMangochain15
                                isMangochain15 = true;
                                bdMangochain15 = new BigDecimal(b);
                                break;
                            case 5://bdMasteraychen
                                isMasteraychen = true;
                                bdMasteraychen = new BigDecimal(b);
                                break;
                            case 6://bdMgpbpvoting
                                isMgpbpvoting = true;
                                bdMgpbpvoting = new BigDecimal(b);
                                break;
//                            case 7://bdEosioToken(余额)
//                                isEosioToken = true;
//                                bdEosioToken = new BigDecimal(b);
//                                tvAllTotalCoins.setText(bdCurrencyAmount.subtract(bdEosioToken).toPlainString());
//                                tvCumulativeDestruction.setText(b);
//                                break;
                            case 8://生态基金 bdMangochain22
                                bdMangochain22 = new BigDecimal(b);
                                tvEcologicalFund.setText(APPUtils.dataFormat(b));
                                break;
                            default:
                                break;
                        }
                        realtimeCirculation();
                    }
                });
    }

    private void realtimeCirculation() {
        //price bdSoTheMortgage bdCurrentAmount
        //实时流通 = 目前币量(eosio.token) - 全网抵押(接口返回) - 运营(mangochain33) - 团队(mangochain11)
        // - mgpchain2222(余额) - mangochain15(余额) - masteraychen(余额) - 做市币量（固定110万）
        // - 抵押投票总量(mgp.bpvoting)
        if (isCurrentAmount && isSoTheMortgage && isMangochain33 && isMangochain11 && isMgpchain2222 && isMangochain15 && isMasteraychen && isMgpbpvoting) {
            bdRealtimeCirculation = bdCurrentAmount.subtract(bdSoTheMortgage).subtract(bdMangochain33)
                    .subtract(bdMangochain11).subtract(bdMgpchain2222).subtract(bdMangochain15)
                    .subtract(bdMasteraychen).subtract(bdMgpbpvoting);

            LogUtils.dTag("realtimeCirculation==", "目前币量(" + bdCurrentAmount + ") - 全网抵押(" + bdSoTheMortgage
                    + ") - 运营(" + bdMangochain33 + ") - 团队(" + bdMangochain11 + ") - mgpchain2222(" + bdMgpchain2222
                    + ") - mangochain15(" + bdMangochain15 + ") - masteraychen(" + bdMasteraychen + ") - 抵押投票总量(" + bdMgpbpvoting + ") = " + bdRealtimeCirculation);
            tvRealTimeCirculation.setText(APPUtils.dataFormat(bdRealtimeCirculation.toPlainString()));
            tvMarketValueOfCirculation.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(bdRealtimeCirculation.multiply(price).setScale(2, RoundingMode.FLOOR).toPlainString()) ?
                    "0.00" : bdRealtimeCirculation.multiply(price).toPlainString(), 2, RoundingMode.FLOOR));
        }
        if (isEosioToken && isPrice) {
            tvAllTotalCoinsUsd.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty((bdCurrencyAmount.subtract(bdEosioToken)).multiply(price).toPlainString()) ?
                    "0.00" : (bdCurrencyAmount.subtract(bdEosioToken)).multiply(price).toPlainString(), 2, RoundingMode.FLOOR));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 投票配置表 比如：过多长时间才能撤回自己参加的投票（refund_delay_sec）；发布节点最小支付数额（min_bp_list_quantity）
     */
    private void getCurrencyStats() {
        Observable.fromCallable(() -> MyApplication.repositoryFactory.customEosioJavaRpcProvider
                .getCurrencyStats(EOSParams.getRequestBody(EOSParams.getCurrencyStatsPamars(EOSIO_TOKEN_CONTRACT_CODE, MGP_SYMBOL))))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<String>(getActivity(), true) {

                    @Override
                    public void onFail(String failMsg) {
                        isCurrentAmount = true;
                        LogUtils.dTag("getCurrencyStats==", "onFail = " + failMsg);
                        realtimeCirculation();
                    }

                    @Override
                    public void onSuccess(String s) {//["161.4881 MGP"]
                        isCurrentAmount = true;
                        CurrencyStatsBean currencyStatsBean = GsonUtils.fromJson(s, CurrencyStatsBean.class);
                        if (currencyStatsBean != null) {
                            if (currencyStatsBean.getMGP() != null) {
                                String mSupply = ObjectUtils.isEmpty(currencyStatsBean.getMGP().getSupply()) ? "0 MGP" : currencyStatsBean.getMGP().getSupply();
                                bdCurrentAmount = new BigDecimal(mSupply.split(" ")[0]);
                            }
                        }
                        realtimeCirculation();
                        LogUtils.dTag("getCurrencyStats==", "onSuccess = " + s);
                    }
                });
    }
}
