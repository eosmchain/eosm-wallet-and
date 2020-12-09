package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLoadMoreView;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullRefreshView;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.IndexMarkIndexBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.MarkIndexAdapter;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.RSAUtils;

import java.math.RoundingMode;
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

public class MiningIndexFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.numTv)
    AppCompatTextView numTv;
    @BindView(R.id.flayout)
    FrameLayout flayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshView)
    QMUIPullRefreshView refreshView;
    @BindView(R.id.loadMoreView)
    QMUIPullLoadMoreView loadMoreView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;

    private Unbinder unbinder;
    private int limit = 50;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private PageInfo pageInfo = new PageInfo();
    private QMUIPullLayout.PullAction mPullAction;
    private IndexMarkIndexBean indexMarkIndexBean;
    private MarkIndexAdapter markIndexAdapter;
    private List<IndexMarkIndexBean.DataBean.ListBean> markIndexItemList = new ArrayList<>();
    private String mNum = "0.00";
    private boolean isLoad = false;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mining_index, null);
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
        topBar.setTitle(getString(R.string.str_mining_index));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        markIndexAdapter = new MarkIndexAdapter(markIndexItemList);
        markIndexAdapter.setAnimationEnable(true);

        markIndexAdapter.setEmptyView(getEmptyView(recyclerView, R.mipmap.nofriend_groupchat, getString(R.string.str_no_empty_data)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(markIndexAdapter);
        initLoadMore();
    }

    @Override
    protected void initAction() {

    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                    // 这里的作用是防止下拉刷新的时候还可以上拉加载
                    markIndexAdapter.getLoadMoreModule().setEnableLoadMore(false);
                    pageInfo.reset();
                } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                }
                getIndexMarkIndex();
                mPullAction = pullAction;
            }
        });

        markIndexAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getIndexMarkIndex();
            }
        });
        markIndexAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        markIndexAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void getIndexMarkIndex() {
        try {
            isLoad = true;
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("limit", limit + "");
            params.put("page", pageInfo.page + "");
            String jsonData = GsonUtils.toJson(params);
            String content = RSAUtils.encrypt(jsonData);
            NetWorkManager.getRequest().getIndexMarkIndex(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::myOrderWalletSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void myOrderWalletSuccess(JsonObject jsonData) {
        dismissTipDialog();
        isLoad = false;
        if (mPullAction != null) {
            pullLayout.finishActionRun(mPullAction);
        }
        if (jsonData != null) {
            indexMarkIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), IndexMarkIndexBean.class);
            if (indexMarkIndexBean != null) {
                if (indexMarkIndexBean.getCode() == 0) {
                    IndexMarkIndexBean.DataBean dataBean = indexMarkIndexBean.getData();
                    if (dataBean != null) {
                        mNum = dataBean.getNum();
                        List<IndexMarkIndexBean.DataBean.ListBean> markIndexItems = dataBean.getList();
                        if (pageInfo.isFirstPage()) {
                            numTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(mNum) ? "0" : mNum, 2, RoundingMode.FLOOR));
                            if (!ObjectUtils.isEmpty(markIndexItems)) {
                                markIndexItemList.clear();
                                markIndexItemList.addAll(markIndexItems);
                            }
                            markIndexAdapter.setList(markIndexItemList);
                        } else {
                            if (!ObjectUtils.isEmpty(markIndexItems)) {
                                markIndexItemList.addAll(markIndexItemList.size(), markIndexItems);
                            }
                            markIndexAdapter.addData(markIndexItemList);
                        }

                    }
                } else {
                    ToastUtils.showShort(indexMarkIndexBean.getMsg());
                }
            }
        }
        if (markIndexItemList.size() < limit) {
            //如果不够一页,显示没有更多数据布局
            markIndexAdapter.getLoadMoreModule().loadMoreEnd();
        } else {
            markIndexAdapter.getLoadMoreModule().loadMoreComplete();
        }
        pageInfo.nextPage();
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
        if (isLoad) {
            isLoad = false;
            if (mPullAction != null) {
                pullLayout.finishActionRun(mPullAction);
            }
            markIndexAdapter.getLoadMoreModule().setEnableLoadMore(true);
            markIndexAdapter.getLoadMoreModule().loadMoreFail();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //取消全屏设置
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.durban_White));
        getIndexMarkIndex();
    }

    @Override
    public void onStop() {
        super.onStop();
        pageInfo.reset();
    }
}
