package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.VoteListBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.StakeVotesListAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.view.DragFloatActionButton2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.ui.fragment.StakeVoteMainFragment.mVoteContract;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class StakeVoteListFragment extends BaseFragment {
    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tabs)
    QMUITabSegment tabs;
    @BindView(R.id.layout_for_shadow)
    DragFloatActionButton2 sendFloating;
    @BindView(R.id.becomeNodeTv)
    QMUIRoundButton becomeNodeTv;

    private Unbinder unbinder;
    private EMWalletRepository emWalletRepository;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private StakeVotesListAdapter stakeVotesListAdapter;
    private List<VoteListBean.RowsBean> mRowsBeanList = new ArrayList<>();

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
        topBar.setTitle(getString(R.string.str_vote_list));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, true);
        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        stakeVotesListAdapter = new StakeVotesListAdapter(mRowsBeanList);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(stakeVotesListAdapter);//scrollToPosition

        becomeNodeTv.setVisibility(View.GONE);
        tabs.setVisibility(View.GONE);
        sendFloating.setVisibility(View.GONE);
    }

    @Override
    protected void initAction() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getTableRowsList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
            }
        });
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);
    }

    private void getTableRowsList() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("code", mVoteContract);
            mapTableRows.put("scope", mVoteContract);
            mapTableRows.put("index_position", "3");
            mapTableRows.put("table", "votes");
            mapTableRows.put("key_type", "i64");
            mapTableRows.put("limit", "500");
            mapTableRows.put("lower_bound", " " +walletAddress);
            mapTableRows.put("upper_bound"," " + walletAddress);
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTableRowsList, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTableRowsList(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        mRowsBeanList.clear();
        if (ObjectUtils.isNotEmpty(o)) {
            VoteListBean voteListBean = GsonUtils.fromJson(o.toString(), VoteListBean.class);
            if (CollectionUtils.isNotEmpty(voteListBean.getRows())) {
                mRowsBeanList.addAll(voteListBean.getRows());
            }
        }
        stakeVotesListAdapter.notifyDataSetChanged();
    }

    private void onError(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        LogUtils.dTag("error==", "e = " + ((Throwable) o).getMessage());
        ToastUtils.showLong(R.string.str_network_error);
    }
}
