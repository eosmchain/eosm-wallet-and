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
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppStoreLifeBean;
import com.token.mangowallet.bean.LowerBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.VoteMainAdapter;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DragFloatActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class VoteMainFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_for_shadow)
    DragFloatActionButton sendFloating;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private VoteMainAdapter voteMainAdapter;
    private Unbinder unbinder = null;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private PageInfo pageInfo;
    private final static int limit = 20;
    private List<ThemesBean.DataBean> themesList = new ArrayList<>();
    private ThemesBean.DataBean dataBean;
    public static BigDecimal bdNumer = BigDecimal.ZERO;
    public static String mNumer;

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
        pageInfo = new PageInfo();
        getSchemeMoney();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_vote_title));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightTextButton(R.string.str_my, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                toFragment(2);
            }
        });
        voteMainAdapter = new VoteMainAdapter(themesList, false);
        voteMainAdapter.setEmptyView(R.layout.view_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(voteMainAdapter);

        voteMainAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                dataBean = themesList.get(position);
                toFragment(1);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageInfo.reset();
                themes();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageInfo.nextPage();
                themes();
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initAction() {
        sendFloating.setOnClickListener(new DragFloatActionButton.OnClickListener() {
            @Override
            public void onClick() {
                isScheme();
            }
        });
    }

    /**
     * 是否开启投递方案
     */
    private void getSchemeMoney() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getSchemeMoney(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::schemeMoneySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启投递方案
     */
    private void isScheme() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().isScheme(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::isSchemeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启投递方案
     */
    private void themes() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("page", String.valueOf(pageInfo.page));
        params.put("limit", String.valueOf(limit));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().themes(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::themesSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void schemeMoneySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            LowerBean lowerBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), LowerBean.class);
            if (lowerBean.getCode() == 0) {
                bdNumer = lowerBean.getData();
                if (bdNumer != null) {
                    mNumer = bdNumer.setScale(4).toPlainString();
                    LogUtils.dTag("mNumer==", "mNumer = " + mNumer);
                }
            } else {
                ToastUtils.showLong(lowerBean.getMsg());
            }
        }
    }

    private void themesSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
        if (jsonObject != null) {
            ThemesBean themesBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), ThemesBean.class);
            if (themesBean.getCode() == 0) {
                if (pageInfo.isFirstPage()) {
                    themesList.clear();
                    themesList.addAll(themesBean.getData());
                } else {
                    themesList.addAll(themesList.size(), themesBean.getData());
                }
                voteMainAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showLong(themesBean.getMsg());
            }
        }
    }

    private void isSchemeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                toFragment(0);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
        LogUtils.dTag("error==", "e = " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void toFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        if (type == 0) {//发布方案
            bundle.putBoolean("isEdit", false);
            startFragment("SendVoteMainFragment", bundle);
        } else if (type == 1) {//方案详情
            bundle.putParcelable(EXTRA_VOTE_DATA, dataBean);
            startFragment("VoteDetailsFragment", bundle);
        } else if (type == 2) {//我的方案
            startFragment("MyVoteMainFragment", bundle);
        }
    }
}
