package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.OrderListBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.LifeRecordAdapter;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class LifePaymentRecordFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private PageInfo pageInfo = new PageInfo();
    private MangoWallet mangoWallet;
    private String walletAddress;
    private LifeRecordAdapter lifeRecordAdapter;
    private QMUIDialog qmuiDialog;
    private List<OrderListBean.DataBean.ListBean> dataBeanList = new ArrayList<>();
    private OrderListBean.DataBean.ListBean listBean;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mix_mortgage, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();

    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_record);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        lifeRecordAdapter = new LifeRecordAdapter(dataBeanList);
        lifeRecordAdapter.setEmptyView(R.layout.view_empty);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(lifeRecordAdapter);
    }

    @Override
    protected void initAction() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageInfo.reset();
                getOrderList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageInfo.nextPage();
                getOrderList();
            }
        });

        lifeRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                listBean = dataBeanList.get(position);

            }
        });
    }

    private void getOrderList() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);//walletAddress、"gaoyexingabm"
        params.put("page", String.valueOf(pageInfo.page));
        params.put("limit", "20");
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().orderList(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::orderListSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void orderListSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            OrderListBean orderListBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), OrderListBean.class);
            if (orderListBean.getCode() == 0) {
                if (ObjectUtils.isEmpty(orderListBean.getData())) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    if (ObjectUtils.isEmpty(orderListBean.getData().getList())) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        //OrderListBean.DataBean.ListBean
                        if (pageInfo.isFirstPage()) {
                            dataBeanList.clear();
                            dataBeanList.addAll(orderListBean.getData().getList());
                        } else {
                            dataBeanList.addAll(dataBeanList.size(), orderListBean.getData().getList());
                        }
                        lifeRecordAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                refreshLayout.finishLoadMoreWithNoMoreData();
                ToastUtils.showLong(orderListBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
            refreshLayout.finishLoadMore();
        }
        LogUtils.dTag("error==", "e = " + e.getMessage());
    }
}
