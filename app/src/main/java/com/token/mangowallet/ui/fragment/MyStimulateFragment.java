package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.MiningOrderIncomeBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.WithdrawIndexBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.MiningOrderIncomeAdapter;
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

public class MyStimulateFragment extends BaseFragment {


    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.myBalanceTitleTv)
    AppCompatTextView myBalanceTitleTv;
    @BindView(R.id.myBalanceTv)
    AppCompatTextView myBalanceTv;
    @BindView(R.id.freezeTv)
    AppCompatTextView freezeTv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private MiningOrderIncomeAdapter miningOrderIncomeAdapter;
    private String money;
    private static final String moneyType = "1";
    private static int limit = 50;
    private PageInfo pageInfo = new PageInfo();
    private WithdrawIndexBean withdrawIndexBean;
    private BigDecimal bdPrice;
    private MiningOrderIncomeBean myOrderListBean;
    private List<MiningOrderIncomeBean.DataBean.ListBean> myOrderListBeanList;
    private boolean isLoad;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_stimulate, null);
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
    }

    @Override
    protected void initView() {
        topBar.setTitle(StringUtils.getString(R.string.str_returns_detailed));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        miningOrderIncomeAdapter = new MiningOrderIncomeAdapter();
        miningOrderIncomeAdapter.setAnimationEnable(true);
        miningOrderIncomeAdapter.setEmptyView(R.layout.qmui_empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(miningOrderIncomeAdapter);
        myBalanceTitleTv.setText(String.format(getString(R.string.str_my_balance), walletType));
        myBalanceTv.setText("0.0000" + "≈" + BalanceUtils.currencyToBase("0.00", 2, RoundingMode.FLOOR));
        freezeTv.setText(String.format(getString(R.string.str_freeze), "0.00"));
        initLoadMore();
        swipeLayout.setRefreshing(true);
        refresh();
    }

    @Override
    protected void initAction() {

    }

    private void initLoadMore() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        miningOrderIncomeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getMyOrderWallet();
            }
        });
        miningOrderIncomeAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        miningOrderIncomeAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    /**
     * 刷新
     */
    private void refresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        miningOrderIncomeAdapter.getLoadMoreModule().setEnableLoadMore(false);
        miningOrderIncomeAdapter.setList(myOrderListBeanList);
        // 下拉刷新，需要重置页数
        pageInfo.reset();
        getMyOrderWallet();
    }

    @OnClick(R.id.nextstepBtn)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("moneyType", moneyType);
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        startFragment("ExtractionYieldFragment", bundle);
    }

    public void getMyOrderWallet() {
        isLoad = true;
        showTipDialog(getString(R.string.str_loading));
        try {
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("limit", limit + "");
            params.put("page", pageInfo.page + "");
            String jsonData = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData);
            NetWorkManager.getRequest().getMiningOrderIncome(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::miningOrderIncomeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWithdrawIndex() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("moneyType", moneyType);
            String jsonData = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData);
            NetWorkManager.getRequest().getWithdrawIndex(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::withdrawIndexSuccess, this::onError);
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

    private void withdrawIndexSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            withdrawIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), WithdrawIndexBean.class);
            if (withdrawIndexBean != null) {
                if (withdrawIndexBean.getCode() == 0) {
                    WithdrawIndexBean.DataBean dataBean = withdrawIndexBean.getData();
                    if (dataBean != null) {
                        money = dataBean.getMoney();
                        freezeTv.setText(String.format(getString(R.string.str_freeze), dataBean.getLockMoney()));
                    }
                } else {
                    ToastUtils.showShort(withdrawIndexBean.getMsg());
                }
            }
        }
    }

    private void miningOrderIncomeSuccess(JsonObject jsonObject) {
        isLoad = false;
        dismissTipDialog();
        swipeLayout.setRefreshing(false);
        miningOrderIncomeAdapter.getLoadMoreModule().setEnableLoadMore(true);
        if (jsonObject != null) {
            myOrderListBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MiningOrderIncomeBean.class);
            if (myOrderListBean != null) {
                if (myOrderListBean.getCode() == 0) {
                    MiningOrderIncomeBean.DataBean dataBean = myOrderListBean.getData();
                    if (dataBean != null) {
                        myOrderListBeanList = dataBean.getList();
                        money = dataBean.getBalance();
                        if (!ObjectUtils.isEmpty(myOrderListBeanList)) {
                            if (pageInfo.isFirstPage()) {
                                //如果是加载的第一页数据，用 setData()
                                miningOrderIncomeAdapter.setList(myOrderListBeanList);
                            } else {
                                //不是第一页，则用add
                                miningOrderIncomeAdapter.addData(myOrderListBeanList);
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(myOrderListBean.getMsg());
                }
            }
        }
        if (!ObjectUtils.isEmpty(myOrderListBeanList)) {
            if (myOrderListBeanList.size() < limit) {
                //如果不够一页,显示没有更多数据布局
                miningOrderIncomeAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                miningOrderIncomeAdapter.getLoadMoreModule().loadMoreComplete();
            }
        } else {
            miningOrderIncomeAdapter.getLoadMoreModule().loadMoreEnd();
        }
        pageInfo.nextPage();

        if (ObjectUtils.isNotEmpty(money) && bdPrice != null) {
            BigDecimal bdMoney = new BigDecimal(money);
            String moneyValue = bdMoney.multiply(bdPrice).stripTrailingZeros().toPlainString();

            myBalanceTv.setText(money + "≈" + BalanceUtils.currencyToBase(ObjectUtils.isEmpty(moneyValue) ? "0.00" : moneyValue, 2, RoundingMode.FLOOR));
        }
    }

    private void priceSuccess(CurrencyPrice currencyPrice) {
        dismissTipDialog();
        if (currencyPrice != null) {
            if (currencyPrice.getCode() == 0) {
                CurrencyPrice.DataBean dataBean = currencyPrice.getData();
                bdPrice = dataBean.getPrice();
            } else {
            }
        }
        if (ObjectUtils.isNotEmpty(money) && bdPrice != null) {
            BigDecimal bdMoney = new BigDecimal(money);
            String moneyValue = bdMoney.multiply(bdPrice).stripTrailingZeros().toPlainString();

            myBalanceTv.setText(money + "≈" + BalanceUtils.currencyToBase(ObjectUtils.isEmpty(moneyValue) ? "0.00" : moneyValue, 2, RoundingMode.FLOOR));
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
        if (isLoad) {
            isLoad = false;
            swipeLayout.setRefreshing(false);
            miningOrderIncomeAdapter.getLoadMoreModule().setEnableLoadMore(true);
            miningOrderIncomeAdapter.getLoadMoreModule().loadMoreFail();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        pageInfo.reset();
    }

    @Override
    public void onResume() {
        super.onResume();
        getWithdrawIndex();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
