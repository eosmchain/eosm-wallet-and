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
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.VoteLogBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.VoteMainRecordAdapter;
import com.token.mangowallet.utils.AssociationVoteTable;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.view.DragFloatActionButton2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.ASSOCIATION_VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class VoteMainRecordFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_for_shadow)
    DragFloatActionButton2 sendFloating;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private VoteMainRecordAdapter voteMainRecordAdapter;
    private Unbinder unbinder = null;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private final static int limit = 20;
    private List<VoteLogBean.RowsBean> themesList = new ArrayList<>();
    private VoteLogBean.RowsBean dataBean;
    private EMWalletRepository emWalletRepository;
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vote_main, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_record_over));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        voteMainRecordAdapter = new VoteMainRecordAdapter(themesList);
        voteMainRecordAdapter.setEmptyView(R.layout.view_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(voteMainRecordAdapter);

        voteMainRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                dataBean = themesList.get(position);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                getAward();
            }
        });

//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
////                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//                pageInfo.nextPage();
//                getAward();
//            }
//        });
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);
        sendFloating.setVisibility(View.GONE);
    }

    @Override
    protected void initAction() {
    }

    /**
     * 是否开启投递方案
     */
    private void getAward() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("code", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("scope", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("table", AssociationVoteTable.getAwards());
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::votesLogSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Map params = MapUtils.newHashMap();
//        params.put("address", walletAddress);
//        params.put("page", String.valueOf(pageInfo.page));
//        params.put("limit", String.valueOf(limit));
//        String json = GsonUtils.toJson(params);
//        try {
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().getAward(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::votesLogSuccess, this::onError);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void votesLogSuccess(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        themesList.clear();
        if (o != null) {
            VoteLogBean voteLogBean = GsonUtils.fromJson((String) o, VoteLogBean.class);
            if (voteLogBean != null) {
                List<VoteLogBean.RowsBean> rowsBeanList = voteLogBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    themesList.addAll(rowsBeanList);
                }
            }
        }
        voteMainRecordAdapter.notifyDataSetChanged();
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void onError(Object e) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    private void toFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        startFragment("MyVoteMainFragment", bundle);
    }
}
