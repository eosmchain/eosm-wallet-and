package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
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
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.ArbitrationOrderAdapter;
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
import static com.token.mangowallet.utils.Constants.OTC_ARBITER_ORDERS;

public class OTCArbitrationFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private EMWalletRepository emWalletRepository;
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;
    private ArbitrationOrderAdapter arbitrationOrderAdapter;
    private List<DealsOrderBean.RowsBean> allRowsBeanList = new ArrayList<>();
    private List<DealsOrderBean.RowsBean> rowsBeanList = new ArrayList<>();
    private List<DealsOrderBean.RowsBean> mSearchRowsBeanList = new ArrayList<>();
    private boolean isSearch = false;
    private String queryName = "";
    private Bundle orderBundle;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_otc_arbitration_order, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
        getDealsOrder();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_otc_arbitration);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        arbitrationOrderAdapter = new ArbitrationOrderAdapter(rowsBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(arbitrationOrderAdapter);
    }

    @Override
    protected void initAction() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryName = newText;
                isSearch = true;
                mSearchRowsBeanList.clear();
                mSearchRowsBeanList.addAll(allRowsBeanList);
                rowsBeanList.clear();
                CollectionUtils.filter(mSearchRowsBeanList, new CollectionUtils.Predicate<DealsOrderBean.RowsBean>() {
                    @Override
                    public boolean evaluate(DealsOrderBean.RowsBean item) {//过滤closed=1的不要
                        return item.getOrder_sn().contains(newText);
                    }
                });
                rowsBeanList.addAll(mSearchRowsBeanList);
                arbitrationOrderAdapter.notifyDataSetChanged();
                return false;
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getDealsOrder();
            }
        });
        refreshLayout.setEnableLoadMore(false);

        arbitrationOrderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                View itemView = adapter.getViewByPosition(position, R.id.item_view);
                DealsOrderBean.RowsBean mDealsOrderBean = rowsBeanList.get(position);
                int orderStatus = (int) itemView.getTag(R.id.orderStatus);
                String transactionAmountVal = (String) itemView.getTag(R.id.transactionAmountVal);
                orderBundle = new Bundle();
                orderBundle.putParcelable(EXTRA_WALLET, mangoWallet);
                orderBundle.putInt("OTC_TYPE", OTC_ARBITER_ORDERS);
                orderBundle.putString("amountPaid", transactionAmountVal);
                orderBundle.putParcelable("RowsBean", mDealsOrderBean);

                if (orderStatus == 0 || orderStatus == 1 || orderStatus == 2 || orderStatus == 4) {
                    getPayInfo(mDealsOrderBean.getOrder_maker());
                } else {
                    startFragment("BuyerTransactionInfoFragment", orderBundle);
                }
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
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("scope", MainActivity.deal_contract);
            mapTableRows.put("code", MainActivity.deal_contract);
            mapTableRows.put("table", "deals");
            mapTableRows.put("limit", "500");

//            mapTableRows.put("key_type", "i64");
//            mapTableRows.put("index_position", "5");
//            mapTableRows.put("lower_bound", mangoWallet.getWalletAddress());
//            mapTableRows.put("upper_bound", mangoWallet.getWalletAddress());
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTradeOrderSuccess, this::onError);
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
        allRowsBeanList.clear();
        rowsBeanList.clear();
        if (ObjectUtils.isNotEmpty(o)) {
            DealsOrderBean dealsOrderBean = GsonUtils.fromJson(o.toString(), DealsOrderBean.class);
            if (dealsOrderBean != null) {
                List<DealsOrderBean.RowsBean> rowsBeanList = dealsOrderBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    allRowsBeanList.addAll(rowsBeanList);
                    Collections.reverse(allRowsBeanList);
                }
            }
        }
        rowsBeanList.addAll(allRowsBeanList);
        arbitrationOrderAdapter.notifyDataSetChanged();
    }

    private void onError(Object e) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }
}
