package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.bean.PayInfoUserInfoBean;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.MyOrderAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.OTC_BUYER_ORDERS;
import static com.token.mangowallet.utils.Constants.OTC_SELLER_ORDERS;

public class MyOrderFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    public int mCurIndex = 0;
    private EMWalletRepository emWalletRepository;
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;
    private List<DealsOrderBean.RowsBean> mRowsBeanList = new ArrayList<>();
    private List<SelordersBean.RowsBean> entrustRowsBeanList = new ArrayList<>();
    private List<Object> dataList = new ArrayList<>();
    private MyOrderAdapter myOrderAdapter;
    private Bundle orderBundle;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_order, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        emWalletRepository = new EMWalletRepository();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        getDealsOrder();
        getSelordersRows();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_my_order);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        initTabSegment();
        myOrderAdapter = new MyOrderAdapter(this, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(myOrderAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initAction() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mCurIndex == 0 || mCurIndex == 1) {
                    getDealsOrder();
                } else {
                    getSelordersRows();
                }
            }
        });
        refreshLayout.setEnableLoadMore(false);

        myOrderAdapter.setOnItemClickListener(new MyOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Map<String, Object> dataMap, int position) {
                orderBundle = new Bundle();
                Object o = dataList.get(position);
                DealsOrderBean.RowsBean mDealsOrderBean = null;
                SelordersBean.RowsBean mSelordersBean;
                orderBundle.putParcelable(EXTRA_WALLET, mangoWallet);
                //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
                int orderStatus = (int) dataMap.get("orderStatus");
                if (mCurIndex == 0) {
                    mDealsOrderBean = (DealsOrderBean.RowsBean) o;
                    orderBundle.putInt("OTC_TYPE", OTC_BUYER_ORDERS);
                    orderBundle.putString("amountPaid", String.valueOf(dataMap.get("transactionAmountVal")));
                    orderBundle.putParcelable("RowsBean", mDealsOrderBean);
                    if (orderStatus == 0 || orderStatus == 1 || orderStatus == 2 || orderStatus == 4) {
                        getPayInfo(mDealsOrderBean.getOrder_maker());
                    } else {
                        startFragment("BuyerTransactionInfoFragment", orderBundle);
                    }
                } else if (mCurIndex == 1) {
                    mDealsOrderBean = (DealsOrderBean.RowsBean) o;
                    orderBundle.putInt("OTC_TYPE", OTC_SELLER_ORDERS);
                    orderBundle.putString("amountPaid", String.valueOf(dataMap.get("transactionAmountVal")));
                    orderBundle.putParcelable("RowsBean", mDealsOrderBean);
                    if (orderStatus == 0 || orderStatus == 1 || orderStatus == 2 || orderStatus == 4) {
                        getPayInfo(mDealsOrderBean.getOrder_maker());
                    } else {
                        startFragment("BuyerTransactionInfoFragment", orderBundle);
                    }
                } else if (mCurIndex == 2) {
                    mSelordersBean = (SelordersBean.RowsBean) o;
                    orderBundle.putParcelable("SelordersBean", mSelordersBean);
                    startFragment("EntrustFragment", orderBundle);
                }
            }
        });

    }


    private void initTabSegment() {
        String[] tabData = new String[]{getString(R.string.str_buy_mgp), getString(R.string.str_sell_mgp), getString(R.string.str_my_entrust)};
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();
        tabBuilder.setColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_deputy),
                ContextCompat.getColor(getActivity(), R.color.qmui_config_color_red));
        for (String tabText : tabData) {
            if (ObjectUtils.isNotEmpty(tabText)) {
                QMUITab tab = tabBuilder.setText(tabText).build(getContext());
                mTabSegment.addTab(tab);
            }
        }
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.selectTab(mCurIndex);
        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mCurIndex = index;
                dataList.clear();
                if (index == 2) {
                    dataList.addAll(entrustRowsBeanList);
                    myOrderAdapter.notifyDataSetChanged();
                } else {
                    getDealsOrder();
                }
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }

    /**
     * 获取收款方式
     */
    private void getPayInfo(String Owner) {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", Owner);
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().payInfo(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPayInfoSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有交易订单
     */
    private void getDealsOrder() {
        try {
            showTipDialog(getString(R.string.str_loading));
            String index_position = "";
            if (mCurIndex == 0) {
                index_position = "4";
            } else if (mCurIndex == 1) {
                index_position = "3";
            }
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("scope", MainActivity.deal_contract);
            mapTableRows.put("code", MainActivity.deal_contract);
            mapTableRows.put("table", "deals");
            mapTableRows.put("limit", "500");

            mapTableRows.put("key_type", "i64");
            mapTableRows.put("index_position", index_position);
            mapTableRows.put("lower_bound", " " + mangoWallet.getWalletAddress());
            mapTableRows.put("upper_bound", " " + mangoWallet.getWalletAddress());
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTradeOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的委托
     * 挂售订单
     */
    private void getSelordersRows() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("table", "selorders");
            mapTableRows.put("scope", MainActivity.deal_contract);
            mapTableRows.put("code", MainActivity.deal_contract);
            mapTableRows.put("json", true);
            mapTableRows.put("limit", "500");
            mapTableRows.put("index_position", "3");
            mapTableRows.put("key_type", "i64");
            mapTableRows.put("lower_bound", " " + mangoWallet.getWalletAddress());
            mapTableRows.put("upper_bound", " " + mangoWallet.getWalletAddress());
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onSelordersSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPayInfoSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            PayInfoUserInfoBean payInfoUserInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayInfoUserInfoBean.class);
            if (payInfoUserInfoBean != null) {
                PayInfoUserInfoBean.DataBean dataBean = payInfoUserInfoBean.getData();
                orderBundle.putParcelable("PayInfoUserInfoBean", dataBean);
                startFragment("BuyerTransactionInfoFragment", orderBundle);
            }
        }
    }

    private void onTradeOrderSuccess(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        mRowsBeanList.clear();

        if (ObjectUtils.isNotEmpty(o)) {
            DealsOrderBean dealsOrderBean = GsonUtils.fromJson(o.toString(), DealsOrderBean.class);
            if (dealsOrderBean != null) {
                List<DealsOrderBean.RowsBean> rowsBeanList = dealsOrderBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    mRowsBeanList.addAll(rowsBeanList);
                    if (mCurIndex == 0) {
                        //购买
                        CollectionUtils.filter(mRowsBeanList, new CollectionUtils.Predicate<DealsOrderBean.RowsBean>() {
                            @Override
                            public boolean evaluate(DealsOrderBean.RowsBean item) {//
                                return ObjectUtils.equals(mangoWallet.getWalletAddress(), item.getOrder_taker());
                            }
                        });
                        Collections.reverse(mRowsBeanList);
                    } else if (mCurIndex == 1) {
                        //出售
                        CollectionUtils.filter(mRowsBeanList, new CollectionUtils.Predicate<DealsOrderBean.RowsBean>() {
                            @Override
                            public boolean evaluate(DealsOrderBean.RowsBean item) {//
                                return ObjectUtils.equals(mangoWallet.getWalletAddress(), item.getOrder_maker());
                            }
                        });
                        Collections.reverse(mRowsBeanList);
                    }
                }
            }
        }

        if (mCurIndex == 0 || mCurIndex == 1) {
            dataList.clear();
            dataList.addAll(mRowsBeanList);
            myOrderAdapter.notifyDataSetChanged();
        }
    }

    private void onSelordersSuccess(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        entrustRowsBeanList.clear();
        if (ObjectUtils.isNotEmpty(o)) {
            SelordersBean selordersBean = GsonUtils.fromJson(o.toString(), SelordersBean.class);
            if (selordersBean != null) {
                if (CollectionUtils.isNotEmpty(selordersBean.getRows())) {
                    entrustRowsBeanList.addAll(selordersBean.getRows());
                    Collections.reverse(entrustRowsBeanList);
                    if (mCurIndex == 2) {
                        myOrderAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        if (mCurIndex == 2) {
            dataList.clear();
            dataList.addAll(entrustRowsBeanList);
            myOrderAdapter.notifyDataSetChanged();
        }
    }

    private void onError(Object e) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurIndex == 0 || mCurIndex == 1) {
            getDealsOrder();
        } else {
            getSelordersRows();
        }
    }
}
