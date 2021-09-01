package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.LPOrderBean;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.viewmodel.MortgageModelFactory;
import com.token.mangowallet.ui.viewmodel.MortgageViewModel;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;

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

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class MiningBigFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.totalMortgageVolumeTv)
    AppCompatTextView totalMortgageVolumeTv;
    @BindView(R.id.accumulativeDestructionTv)
    AppCompatTextView accumulativeDestructionTv;
    @BindView(R.id.layout1)
    QMUILinearLayout layout1;
    @BindView(R.id.yesterdayOrderValueTv)
    AppCompatTextView yesterdayOrderValueTv;
    @BindView(R.id.yesterdayIncentiveTv)
    AppCompatTextView yesterdayIncentiveTv;
    @BindView(R.id.motivateIndexTv)
    AppCompatTextView motivateIndexTv;
    @BindView(R.id.layout2)
    QMUILinearLayout layout2;
    @BindView(R.id.mortgageValueTv)
    AppCompatTextView mortgageValueTv;
    @BindView(R.id.amountMoneyTv)
    AppCompatTextView amountMoneyTv;
    @BindView(R.id.valueTv)
    AppCompatTextView valueTv;
    @BindView(R.id.mortgageStatusTv)
    AppCompatTextView mortgageStatusTv;
    @BindView(R.id.openingTimeTv)
    AppCompatTextView openingTimeTv;
    @BindView(R.id.expectedUnlockTv)
    AppCompatTextView expectedUnlockTv;
    @BindView(R.id.currencyDayValueTv)
    AppCompatTextView currencyDayValueTv;
    @BindView(R.id.layout3)
    QMUILinearLayout layout3;
    @BindView(R.id.layout4)
    QMUILinearLayout layout4;
    @BindView(R.id.button1)
    QMUIRoundButton button1;
    @BindView(R.id.button2)
    QMUIRoundButton button2;
    @BindView(R.id.bottomLayout)
    QMUILinearLayout bottomLayout;
    @BindView(R.id.mortgageStatusLayout)
    RelativeLayout mortgageStatusLayout;
    @BindView(R.id.expectedUnlockLayout)
    RelativeLayout expectedUnlockLayout;
    @BindView(R.id.openingTimeLayout)
    RelativeLayout openingTimeLayout;
    @BindView(R.id.layout5)
    QMUIConstraintLayout layout5;
    @BindView(R.id.hashrateUserValTv)
    AppCompatTextView hashrateUserValTv;
    @BindView(R.id.hashrateShareValTv)
    AppCompatTextView hashrateShareValTv;
    @BindView(R.id.hashrateTeamValTv)
    AppCompatTextView hashrateTeamValTv;
    @BindView(R.id.hashrateLightnodeValTv)
    AppCompatTextView hashrateLightnodeValTv;
    @BindView(R.id.hashrateNodeValTv)
    AppCompatTextView hashrateNodeValTv;
    @BindView(R.id.hashrateIndexValTv)
    AppCompatTextView hashrateIndexValTv;
    @BindView(R.id.hashrateText1)
    AppCompatTextView hashrateText1;
    @BindView(R.id.text1)
    AppCompatTextView text1;
    @BindView(R.id.text2)
    AppCompatTextView text2;
    @BindView(R.id.layout)
    QMUILinearLayout layout;
    @BindView(R.id.hashrateUserTv)
    AppCompatTextView hashrateUserTv;
    @BindView(R.id.hashrateShareTv)
    AppCompatTextView hashrateShareTv;
    @BindView(R.id.hashrateTeamTv)
    AppCompatTextView hashrateTeamTv;
    @BindView(R.id.hashrateLightnodeTv)
    AppCompatTextView hashrateLightnodeTv;
    @BindView(R.id.hashrateNodeTv)
    AppCompatTextView hashrateNodeTv;
    @BindView(R.id.hashrateIndexTv)
    AppCompatTextView hashrateIndexTv;
    @BindView(R.id.text3)
    AppCompatTextView text3;
    @BindView(R.id.layout3titleLayout)
    RelativeLayout layout3titleLayout;
    @BindView(R.id.text4)
    AppCompatTextView text4;
    @BindView(R.id.text5layout)
    RelativeLayout text5layout;
    @BindView(R.id.valuetextTv)
    AppCompatTextView valuetextTv;
    @BindView(R.id.mortgage_status_tv)
    AppCompatTextView mortgage_status_tv;
    @BindView(R.id.opening_time_tv)
    AppCompatTextView opening_time_tv;
    @BindView(R.id.currency_day_value_layout)
    RelativeLayout currency_day_value_layout;
    @BindView(R.id.earnings_value_layout)
    RelativeLayout earnings_value_layout;
    @BindView(R.id.earningsValueTv)
    AppCompatTextView earningsValueTv;
    @BindView(R.id.capping_value_layout)
    RelativeLayout capping_value_layout;
    @BindView(R.id.cappingValueTv)
    AppCompatTextView cappingValueTv;

    private Unbinder unbinder;
    private MortgageModelFactory mortgageModelFactory;
    private MortgageViewModel mortgageViewModel;
    private MangoWallet mangoWallet;
    private String code;
    private String action;
    private String privateKey;
    private String walletAddress;
    private String type;
    private int mRadius;
    private String remaining;
    private String mMgpNum = "0.0000";
    private LPOrderBean mLPOrderBean;
    private Constants.WalletType walletType;
    private int toType = 2;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mortgage_big, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        mortgageModelFactory = new MortgageModelFactory();
        mortgageViewModel = ViewModelProviders.of(this.getActivity(), mortgageModelFactory)
                .get(MortgageViewModel.class);
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        privateKey = mangoWallet.getPrivateKey();
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 10);
        mortgageViewModel.prepare(mangoWallet);
//        mortgageViewModel.balance().observe(this, this::onBalance);
        mortgageViewModel.tableRows().observe(this, this::onTableRows);
//        mortgageViewModel.orderIndex().observe(this, this::onOrderIndex);
//        mortgageViewModel.emWalletRepository.fetchBalance(EOSIO_TOKEN_CONTRACT_CODE, walletType)
//                .subscribe(this::onBalance, this::onError);
        earnings_value_layout.setVisibility(View.VISIBLE);
        capping_value_layout.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_ecological_mortgage));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
//        topBar.addRightTextButton(R.string.str_mix_mortgage, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
//                startFragment("MixMortgageFragment", bundle);
//            }
//        });
        layout1.setRadius(mRadius);
        layout2.setRadius(mRadius);
        layout3.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        layout4.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        layout5.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
    }

    @Override
    protected void initAction() {

    }

    /**
     *
     */
    private void LPOrder() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);

            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().LPOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onOrderIndex, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void onBalance(BigDecimal balance) {
//        String balanceStr = balance.setScale(4, RoundingMode.CEILING).toPlainString();
//        accumulativeDestructionTv.setText(balanceStr);
//    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    private void onTableRows(TableRowsBean tableRowsBean) {
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(tableRowsBean));
        if (!ObjectUtils.isEmpty(tableRowsBean)) {
            if (tableRowsBean != null) {
                List<TableRowsBean.RowsBean> rowsBeanList = tableRowsBean.getRows();
                if (ObjectUtils.isEmpty(rowsBeanList)) {
                    remaining = "0";
                } else {
                    TableRowsBean.RowsBean rowsBean = rowsBeanList.get(0);
                    remaining = rowsBean.getRemaining().split(" ")[0];
                    mMgpNum = rowsBean.getRemaining().split(" ")[0];
                }
                BigDecimal bdRemaining = new BigDecimal(remaining);

                // 如果指定的数与参数相等返回0。
                // 如果指定的数小于参数返回 -1。
                // 如果指定的数大于参数返回  1。
                if (bdRemaining.compareTo(BigDecimal.ZERO) > 0) {
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button1.setText(getString(R.string.str_additional_mortgage));
                } else {
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
                    button1.setText(getString(R.string.str_mortgage_initiated));
                }
            }
        }
    }

    private void onOrderIndex(JsonObject object) {
        dismissTipDialog();
        if (object != null) {
            mLPOrderBean = GsonUtils.fromJson(GsonUtils.toJson(object), LPOrderBean.class);
            if (mLPOrderBean != null) {
//        this.orderIndexBean = orderIndexBean;
                if (mLPOrderBean.getCode() == 0) {
                } else {
                    ToastUtils.showShort(mLPOrderBean.getMsg());
                }
            }
        }
        updataView();
    }

    public void lazyLoad() {
        try {
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", Constants.EMCONTRACT);
            mapTableRows.put("code", Constants.EMCONTRACT);
            mapTableRows.put("table", "balances");
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("lower_bound", " " + walletAddress);
            mapTableRows.put("upper_bound", " " + walletAddress);
            mortgageViewModel.fetchTableRows(mapTableRows);
            LPOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updataView() {
        text2.setText(getString(R.string.str_cumulative_distribution));
        hashrateText1.setText("LP");
        hashrateUserTv.setText(R.string.str_order);
        hashrateShareTv.setText(R.string.str_share);
        hashrateTeamTv.setText(R.string.str_exponent);
        hashrateLightnodeTv.setVisibility(View.GONE);
        hashrateLightnodeValTv.setVisibility(View.GONE);
        hashrateNodeTv.setVisibility(View.GONE);
        hashrateNodeValTv.setVisibility(View.GONE);
        hashrateIndexTv.setVisibility(View.GONE);
        hashrateIndexValTv.setVisibility(View.GONE);
        text5layout.setVisibility(View.GONE);
        valuetextTv.setText(R.string.str_lp_quantity_money);
        mortgage_status_tv.setText(R.string.str_order_value);
        mortgage_status_tv.setText(R.string.str_order_value);
        opening_time_tv.setText(R.string.str_mortgage_time);
        expectedUnlockLayout.setVisibility(View.GONE);
        currency_day_value_layout.setVisibility(View.GONE);
        mortgageValueTv.setText("");
        if (mLPOrderBean != null) {
            LPOrderBean.DataBean dataBean = mLPOrderBean.getData();
            if (dataBean != null) {
                totalMortgageVolumeTv.setText(dataBean.getTotalNum() == null ? "0" : dataBean.getTotalNum().setScale(4).toPlainString());
                accumulativeDestructionTv.setText(String.valueOf(dataBean.getTotalOut()));
                yesterdayIncentiveTv.setText((ObjectUtils.isEmpty(dataBean.getYesterdayRewardNum()) ? "0" : String.valueOf(dataBean.getYesterdayRewardNum())) + walletType);
                yesterdayOrderValueTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(dataBean.getYesterdayDollarValue()) ? "0" : String.valueOf(dataBean.getYesterdayDollarValue()), 2, RoundingMode.FLOOR));
                LPOrderBean.DataBean.PowerBean powerBean = dataBean.getPower();
                if (powerBean != null) {
                    hashrateUserValTv.setText(ObjectUtils.isEmpty(powerBean.getUserPower()) ? "0" : String.valueOf(powerBean.getUserPower()));
                    hashrateShareValTv.setText(ObjectUtils.isEmpty(powerBean.getSharePower()) ? "0" : String.valueOf(powerBean.getSharePower()));
                    hashrateTeamValTv.setText(ObjectUtils.isEmpty(powerBean.getPowerIndex()) ? "0" : String.valueOf(powerBean.getPowerIndex()));
                }
                LPOrderBean.DataBean.OrderBean orderBean = dataBean.getOrder();
                if (orderBean != null) {
                    amountMoneyTv.setText((ObjectUtils.isEmpty(orderBean.getNum()) ? "0" : orderBean.getNum()) + "");//
                    valueTv.setText(ObjectUtils.isEmpty(orderBean.getLpNum()) ? "0" : String.valueOf(orderBean.getLpNum()));
                    mortgageStatusTv.setText("$" + (ObjectUtils.isEmpty(orderBean.getDollarValue()) ? "0" : String.valueOf(orderBean.getDollarValue())));
                    openingTimeTv.setText(ObjectUtils.isEmpty(orderBean.getCreateDate()) ? "" : orderBean.getCreateDate());
                    mortgageValueTv.setText(ObjectUtils.isEmpty(orderBean.getLevelName()) ? "" : orderBean.getLevelName());
                    cappingValueTv.setText((ObjectUtils.isEmpty(orderBean.getCappingMoney()) ? "0" : orderBean.getCappingMoney()) + "$");
                    earningsValueTv.setText((ObjectUtils.isEmpty(orderBean.getGainMoney()) ? "0" : orderBean.getGainMoney()) + "$");
                }
            }
        }
    }

    @OnClick({R.id.button1, R.id.button2})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        boolean isButton1 = true;
        switch (view.getId()) {
            case R.id.button1:
                isButton1 = true;
                if (button2.getVisibility() == View.GONE) {
                    mMgpNum = "0";
                }
                bundle.putString("mgpNum", mMgpNum);
                break;
            case R.id.button2:
                isButton1 = false;
                bundle.putString("remaining", remaining);
                break;
        }
        bundle.putString("type", isButton1 ? button1.getText().toString() : button2.getText().toString());
        bundle.putBoolean("isMortgage", isButton1);
        bundle.putInt("toType", toType);
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        startFragment("MiningMortgageFragment", bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
