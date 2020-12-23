package com.token.mangowallet.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
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
import com.token.mangowallet.bean.RealTimeIndexBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.net.eosmgp.EOSParams;
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
import static com.token.mangowallet.utils.Constants.bdCurrencyAmount;

public class RealTimeDateFragment extends BaseFragment {

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
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal bdDestruction = new BigDecimal("2100000");
    private BigDecimal bdMiningpool = new BigDecimal("350000000");
    private BigDecimal bdRealtimeCirculation = BigDecimal.ZERO;
    private BigDecimal bdSoTheMortgage = BigDecimal.ZERO;
    private BigDecimal bdOperatingFunds = BigDecimal.ZERO;
    private BigDecimal bdNikkoEcoFund = BigDecimal.ZERO;
    private BigDecimal bdEcologicalReward = BigDecimal.ZERO;
    private BigDecimal bdMagpchain2222 = BigDecimal.ZERO;
    private BigDecimal bdAccumulativeDestruction = BigDecimal.ZERO;
    private boolean isPrice = false;//价格
    private boolean isSoTheMortgage = false;//全网抵押
    private boolean isOperatingFunds = false;//运营基金
    private boolean isNikkoEcoFund = false;//生态基金
    private boolean isEcologicalReward = false;//生态奖励
    private boolean isMagpchain2222 = false;//magpchain2222
    private boolean isAccumulativeDestruction = false;//累计销毁

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
        getCurrencyBalance(EOSParams.getBalancePamars(walletAddress, EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 0);
//地址:mangochain11 生态激励地址 47700000
//地址:mangochain22 生态基金地址  46760000
//地址:mangochain33 运营地址 9769290
        //实时流通 = 5亿 - 销毁(91010622.8367) - 全网抵押(7186976.5334)
        // - 运营(7644665.5263) - 生态(0) - 团队(37700000)
        // - 矿池(350000000) - mgpchain2222余额(465424.0639) - 2100000
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain11", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 1);
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain22", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 2);
        getCurrencyBalance(EOSParams.getBalancePamars("mangochain33", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 3);
        getCurrencyBalance(EOSParams.getBalancePamars("mgpchain2222", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 4);
        getCurrencyBalance(EOSParams.getBalancePamars("eosio.token", EOSIO_TOKEN_CONTRACT_CODE, String.valueOf(walletType)), 5);
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
        tvLatestBlock.setText(bdMiningpool.stripTrailingZeros().toPlainString());
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
        if (jsonObject != null) {
            isSoTheMortgage = true;
            realTimeIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), RealTimeIndexBean.class);
            if (realTimeIndexBean != null) {
                if (realTimeIndexBean.getCode() == 0) {
                    RealTimeIndexBean.DataBean dataBean = realTimeIndexBean.getData();
                    if (dataBean != null) {
                        bdSoTheMortgage = new BigDecimal(ObjectUtils.isEmpty(dataBean.getMgpNum()) ? "0" : dataBean.getMgpNum());
                        tvAllNetworkMortgage.setText(ObjectUtils.isEmpty(dataBean.getMgpNum()) ? "0" : dataBean.getMgpNum());
                        tvMortgageValue.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(dataBean.getMapValue()) ? "0.00" : dataBean.getMapValue(), 2, RoundingMode.FLOOR));
                        mappingIndexTv.setText(ObjectUtils.isEmpty(dataBean.getTransferSpeed()) ? "0" : dataBean.getTransferSpeed());

                        tvBlockReward.setText(ObjectUtils.isEmpty(dataBean.getTotalPower()) ? "0" : dataBean.getTotalPower());
                    }
                } else {
                    ToastUtils.showShort(realTimeIndexBean.getMsg());
                }
            }
            realtimeCirculation();
        }
    }

    private void priceSuccess(CurrencyPrice currencyPrice) {
        dismissTipDialog();
        if (currencyPrice != null) {
            if (currencyPrice.getCode() == 0) {
                isPrice = true;
                CurrencyPrice.DataBean dataBean = currencyPrice.getData();
                if (dataBean != null) {
                    price = dataBean.getPrice();
                }
                if (price == null) {
                    price = BigDecimal.ZERO;
                }
                tvRealTimePrice.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(price.toPlainString()) ? "0.00" : price.toPlainString(), 2, RoundingMode.FLOOR));
                realtimeCirculation();
            } else {
                isPrice = true;
                realtimeCirculation();
            }
        } else {
            isPrice = true;
            realtimeCirculation();
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        ToastUtils.showLong(R.string.str_network_error);
        isPrice = true;
        isSoTheMortgage = true;
        realtimeCirculation();
    }


    public void getCurrencyBalance(Map<String, Object> map, int type) {
        Observable.fromCallable(() -> MyApplication.repositoryFactory.customEosioJavaRpcProvider.getCurrencyBalance(EOSParams.getRequestBody(map)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<String>(getActivity(), true) {

                    @Override
                    public void onFail(String failMsg) {
                        String b = "0";
                        switch (type) {
                            case 1:
                                isEcologicalReward = true;
                                bdEcologicalReward = new BigDecimal(b);
                                tvTeamMotivation.setText(b);
                                break;
                            case 2:
                                isNikkoEcoFund = true;
                                bdNikkoEcoFund = new BigDecimal(b);
                                tvEcologicalFund.setText(b);
                                break;
                            case 3:
                                isOperatingFunds = true;
                                bdOperatingFunds = new BigDecimal(b);
                                tvOperationAndConstruction.setText(b);
                                break;
                            case 4:
                                isMagpchain2222 = true;
                                bdMagpchain2222 = new BigDecimal(b);
                                break;
                            case 5:
                                isAccumulativeDestruction = true;
                                bdAccumulativeDestruction = new BigDecimal(b);
                                tvCumulativeDestruction.setText(b);
                                break;
                            default:
                                balance = new BigDecimal(b);
                                break;
                        }
                        realtimeCirculation();
                    }

                    @Override
                    public void onSuccess(String s) {//["161.4881 MGP"]
                        //地址:mangochain11 生态激励地址 47700000
                        // 地址:mangochain22 生态基金地址  46760000
                        // 地址:mangochain33 运营地址 9769290
                        String b = "0";
                        if (!ObjectUtils.isEmpty(s)) {
                            if (s.contains("MGP")) {
                                b = s.split(" ")[0].replace("[\"", "");
                            }
                        }
                        switch (type) {
                            case 1:
                                isEcologicalReward = true;
                                bdEcologicalReward = new BigDecimal(b);
                                tvTeamMotivation.setText(b);
                                break;
                            case 2:
                                isNikkoEcoFund = true;
                                bdNikkoEcoFund = new BigDecimal(b);
                                tvEcologicalFund.setText(b);
                                break;
                            case 3:
                                isOperatingFunds = true;
                                bdOperatingFunds = new BigDecimal(b);
                                tvOperationAndConstruction.setText(b);
                                break;
                            case 4:
                                isMagpchain2222 = true;
                                bdMagpchain2222 = new BigDecimal(b);
                                break;
                            case 5:
                                isAccumulativeDestruction = true;
                                bdAccumulativeDestruction = new BigDecimal(b);
                                tvCumulativeDestruction.setText(b);
                                break;
                            default:
                                balance = new BigDecimal(b);
                                break;
                        }
                        realtimeCirculation();
                    }
                });
    }

    private void realtimeCirculation() {
        //实时流通 = 总量 - 全网抵押 -（运营基金 + 生态基金 + 生态奖励）-magpchain2222-矿池
        if (isSoTheMortgage && isOperatingFunds && isNikkoEcoFund && isEcologicalReward && isMagpchain2222 && isAccumulativeDestruction && isPrice) {
            bdRealtimeCirculation = bdCurrencyAmount.subtract(bdMiningpool).subtract(bdDestruction)
                    .subtract(bdSoTheMortgage).subtract(bdOperatingFunds).subtract(bdNikkoEcoFund)
                    .subtract(bdEcologicalReward).subtract(bdMagpchain2222).subtract(bdAccumulativeDestruction);
            tvAllTotalCoins.setText(bdCurrencyAmount.subtract(bdAccumulativeDestruction).toPlainString());
            tvAllTotalCoinsUsd.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty((bdCurrencyAmount.subtract(bdAccumulativeDestruction)).multiply(price).toPlainString()) ?
                    "0.00" : (bdCurrencyAmount.subtract(bdAccumulativeDestruction)).multiply(price).toPlainString(), 2, RoundingMode.FLOOR));
            tvRealTimeCirculation.setText(bdRealtimeCirculation.toPlainString());
            tvMarketValueOfCirculation.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(bdRealtimeCirculation.multiply(price).setScale(2, RoundingMode.FLOOR).toPlainString()) ?
                    "0.00" : (bdCurrencyAmount.subtract(bdAccumulativeDestruction)).multiply(price).toPlainString(), 2, RoundingMode.FLOOR));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
