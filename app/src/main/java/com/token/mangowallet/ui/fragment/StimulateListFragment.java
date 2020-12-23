package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MiningOrderIncomeItem;
import com.token.mangowallet.bean.MyOrderListBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.WithdrawIndexBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.MinerIncomeAdapter;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class StimulateListFragment extends BaseFragment {

    @BindView(R.id.incomeTv)
    AppCompatTextView incomeTv;
    @BindView(R.id.topBar)
    QMUITopBarLayout topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private Unbinder unbinder;
    private static final String moneyType = "2";
    private MinerIncomeAdapter minerIncomeAdapter;
    private int limit = 50;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private boolean isLoadMore = true;
    private PageInfo pageInfo = new PageInfo();
    private List<MiningOrderIncomeItem> myOrderListBeanList;
    private MyOrderListBean myOrderListBean;
    private String money = "0.0000";
    private WithdrawIndexBean withdrawIndexBean;
    private boolean isLoad = false;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_stimulate_list, null);
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
        topBar.setTitle(StringUtils.getString(R.string.str_my_motivation));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightTextButton(StringUtils.getString(R.string.str_mining_index), R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("MiningIndexFragment", bundle);
            }
        });

        minerIncomeAdapter = new MinerIncomeAdapter();
        minerIncomeAdapter.setAnimationEnable(true);
        minerIncomeAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.view_empty, null, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(minerIncomeAdapter);
        initLoadMore();
        incomeTv.setText(money);  // 进入页面，刷新数据
        swipeLayout.setRefreshing(true);
        refresh();
    }

    @Override
    protected void initAction() {

    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        minerIncomeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getMyOrderWallet();
            }
        });
        minerIncomeAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        minerIncomeAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    /**
     * 刷新
     */
    private void refresh() {
        isLoadMore = false;
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        minerIncomeAdapter.getLoadMoreModule().setEnableLoadMore(false);
        minerIncomeAdapter.setList(null);
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
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("limit", limit + "");
            params.put("page", pageInfo.page + "");
            String jsonData = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData);

            NetWorkManager.getRequest().getMyOrderWallet(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::myOrderWalletSuccess, this::onError);
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

    private void withdrawIndexSuccess(JsonObject jsonData) {
        if (jsonData != null) {
            withdrawIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), WithdrawIndexBean.class);
            if (withdrawIndexBean != null) {
                if (withdrawIndexBean.getCode() == 0) {
                    WithdrawIndexBean.DataBean dataBean = withdrawIndexBean.getData();
                    if (dataBean != null) {
                        money = dataBean.getMoney();
                        incomeTv.setText(money);
                    }
                } else {
                    ToastUtils.showShort(withdrawIndexBean.getMsg());
                }
            }
        }
    }

    private void myOrderWalletSuccess(JsonObject jsonData) {
        dismissTipDialog();
        isLoad = false;
        swipeLayout.setRefreshing(false);
        minerIncomeAdapter.getLoadMoreModule().setEnableLoadMore(true);
        if (jsonData != null) {
            myOrderListBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MyOrderListBean.class);
            if (myOrderListBean != null) {
                if (myOrderListBean.getCode() == 0) {
                    MyOrderListBean.DataBean dataBean = myOrderListBean.getData();
                    if (dataBean != null) {
                        myOrderListBeanList = dataBean.getList();
                        money = dataBean.getMoney();
                        if (!ObjectUtils.isEmpty(myOrderListBeanList)) {
                            if (pageInfo.isFirstPage()) {
                                //如果是加载的第一页数据，用 setData()
                                minerIncomeAdapter.setList(myOrderListBeanList);
                            } else {
                                //不是第一页，则用add
                                minerIncomeAdapter.addData(myOrderListBeanList);
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
                minerIncomeAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                minerIncomeAdapter.getLoadMoreModule().loadMoreComplete();
            }
        } else {
            minerIncomeAdapter.getLoadMoreModule().loadMoreEnd();
        }
        pageInfo.nextPage();
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        if (isLoad) {
            isLoad = false;
            swipeLayout.setRefreshing(false);
            minerIncomeAdapter.getLoadMoreModule().setEnableLoadMore(true);
            minerIncomeAdapter.getLoadMoreModule().loadMoreFail();
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
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BarUtils.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.qmui_config_color_transparent));
        getWithdrawIndex();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
