package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.OrderIndexBean;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.ui.viewmodel.MortgageModelFactory;
import com.token.mangowallet.ui.viewmodel.MortgageViewModel;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.RSAUtils;

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
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class MortgageBigFragment extends BaseFragment {

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
    private OrderIndexBean orderIndexBean;
    private Constants.WalletType walletType;

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
        mortgageViewModel.orderIndex().observe(this, this::onOrderIndex);
        mortgageViewModel.emWalletRepository.fetchBalance(EOSIO_TOKEN_CONTRACT_CODE, walletType)
                .subscribe(this::onBalance, this::onError);
//        fetchBalance(EOSIO_TOKEN_CONTRACT_CODE);
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_hashrate_theme));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightTextButton(R.string.str_mix_mortgage, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("MixMortgageFragment", bundle);
            }
        });
        layout1.setRadius(mRadius);
        layout2.setRadius(mRadius);
        layout3.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        layout4.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        layout5.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
    }

    @Override
    protected void initAction() {

    }

    private void onBalance(BigDecimal balance) {
        String balanceStr = balance.setScale(4, RoundingMode.CEILING).toPlainString();
        accumulativeDestructionTv.setText(balanceStr);
    }

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

    private void onOrderIndex(OrderIndexBean orderIndexBean) {
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(orderIndexBean));
        this.orderIndexBean = orderIndexBean;
        if (orderIndexBean.getCode() == 0) {
            updataView();
        } else {
            ToastUtils.showShort(orderIndexBean.getMsg());
        }
    }

    public void lazyLoad() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", Constants.contractAddress);
            mapTableRows.put("code", Constants.contractAddress);
            mapTableRows.put("table", "balances");
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("lower_bound", walletAddress);
            mapTableRows.put("upper_bound", walletAddress);
            mortgageViewModel.fetchTableRows(mapTableRows);

            Map mapOrderIndex = MapUtils.newHashMap();
            mapOrderIndex.put("address", walletAddress);
            String jsonData2 = GsonUtils.toJson(mapOrderIndex);
            String content = RSAUtils.encrypt(jsonData2);
            mortgageViewModel.fetchOrderIndex(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updataView() {
        if (orderIndexBean != null) {
            OrderIndexBean.DataBean dataBean = orderIndexBean.getData();
            if (dataBean != null) {
                totalMortgageVolumeTv.setText(dataBean.getSysMgpNum());
                OrderIndexBean.DataBean.YesterdayOrderBean yesterdayOrderBean = dataBean.getYesterdayOrder();
                if (yesterdayOrderBean != null) {
                    yesterdayOrderValueTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(yesterdayOrderBean.getOrderValue()) ? "0" : yesterdayOrderBean.getOrderValue(), 2, RoundingMode.FLOOR));
                    yesterdayIncentiveTv.setText((ObjectUtils.isEmpty(yesterdayOrderBean.getMoney()) ? "0" : yesterdayOrderBean.getMoney()) + walletType);
                    motivateIndexTv.setText((ObjectUtils.isEmpty(yesterdayOrderBean.getPro()) ? "0" : yesterdayOrderBean.getPro()) + "%");
                }
                OrderIndexBean.DataBean.OrderBean orderBean = dataBean.getOrder();
                if (orderBean != null) {
                    if (ObjectUtils.isEmpty(orderBean.getLockStatus())) {
                        mortgageStatusLayout.setVisibility(View.GONE);
                    } else {
                        mortgageStatusTv.setText(orderBean.getLockStatus());
                    }
                    mortgageValueTv.setText(ObjectUtils.isEmpty(orderBean.getOrderLevel()) ? "M0" : orderBean.getOrderLevel());
                    if (ObjectUtils.isEmpty(orderBean.getCreateTime())) {
                        openingTimeLayout.setVisibility(View.GONE);
                    } else {
                        openingTimeTv.setText(orderBean.getCreateTime());
                    }
                    if (ObjectUtils.isEmpty(orderBean.getUnlockTime())) {
                        expectedUnlockLayout.setVisibility(View.GONE);
                    } else {
                        expectedUnlockTv.setText(orderBean.getUnlockTime());
                    }
                    String dailyPro = orderBean.getDailyPro();
                    String mDailyPro = null;
                    if (!ObjectUtils.isEmpty(dailyPro)) {
                        BigDecimal dailyProDecimal = new BigDecimal(dailyPro);
                        mDailyPro = dailyProDecimal.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                    }
                    currencyDayValueTv.setText(ObjectUtils.isEmpty(mDailyPro) ? "0" : (mDailyPro + "%"));
                    valueTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(orderBean.getOrderValue()) ? "0" : orderBean.getOrderValue(), 2, RoundingMode.FLOOR));
                    mMgpNum = orderBean.getMgpNum();
                    amountMoneyTv.setText((ObjectUtils.isEmpty(mMgpNum) ? "0" : orderBean.getMgpNum()) + "MGP");
                }
                OrderIndexBean.DataBean.CalPowerBean calPowerBean = dataBean.getCalPower();
                if (calPowerBean != null) {
                    hashrateUserValTv.setText(ObjectUtils.isEmpty(calPowerBean.getUserPower()) ? "0" : calPowerBean.getUserPower());
                    hashrateShareValTv.setText(ObjectUtils.isEmpty(calPowerBean.getPushPower()) ? "0" : calPowerBean.getPushPower());
                    hashrateTeamValTv.setText(ObjectUtils.isEmpty(calPowerBean.getTeamPower()) ? "0" : calPowerBean.getTeamPower());
                    hashrateLightnodeValTv.setText(ObjectUtils.isEmpty(calPowerBean.getLightNodePower()) ? "0" : calPowerBean.getLightNodePower());
                    hashrateNodeValTv.setText(ObjectUtils.isEmpty(calPowerBean.getLightPower()) ? "0" : calPowerBean.getLightPower());
                    hashrateIndexValTv.setText(ObjectUtils.isEmpty(calPowerBean.getPowerIndex()) ? "0" : calPowerBean.getPowerIndex());
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
