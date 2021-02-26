package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.blankj.utilcode.util.ObjectUtils;
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
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppStoreLifeBean;
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.bean.LowerBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.SchemeConfigBean;
import com.token.mangowallet.bean.SchemesThemesBean;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.VoteMainAdapter;
import com.token.mangowallet.utils.AssociationVoteTable;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DragFloatActionButton2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.ASSOCIATION_VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class VoteMainFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_for_shadow)
    DragFloatActionButton2 sendFloating;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.startTimeTv)
    AppCompatTextView startTimeTv;

    private VoteMainAdapter voteMainAdapter;
    private Unbinder unbinder = null;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private final static int limit = 20;
    private List<SchemesThemesBean.RowsBean> themesList = new ArrayList<>();
    private SchemesThemesBean.RowsBean dataBean;
    public static BigDecimal bdNumer = BigDecimal.ZERO;
    public static String mNumer;
    public EMWalletRepository emWalletRepository;
    public Constants.WalletType walletType;
    public static boolean isScheme = false;
    public static BigDecimal mbdTotalVoteCount = BigDecimal.ZERO;
    public static SchemeConfigBean.RowsBean rowsConfigBean;

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
        isScheme();
        getSchemeMoney();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_vote_title));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightTextButton(R.string.str_my, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                themes();
            }
        });

//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
////                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//                pageInfo.nextPage();
//                themes();
//            }
//        });
//        themes();
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initAction() {
        sendFloating.setOnClickListener(new DragFloatActionButton2.OnClickListener() {
            @Override
            public void onClick() {
                if (isScheme) {
                    toFragment(0);
                } else {
                    ToastUtils.showLong(R.string.str_notopened_vote);
                }
            }
        });
    }

    /**
     * 获取投递方案数额
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
        try {
            showTipDialog(getString(R.string.str_loading));
//           @{@"json": @1,@"code": mgp_cmvoting,@"scope":mgp_cmvoting,@"table":@"configs"}
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("code", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("scope", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("table", AssociationVoteTable.getConfigs());
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::isSchemeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private void isScheme() {
//        showTipDialog(getString(R.string.str_loading));
//        Map params = MapUtils.newHashMap();
//        params.put("address", walletAddress);
//        String json = GsonUtils.toJson(params);
//        try {
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().isScheme(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::isSchemeSuccess, this::onError);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 是否开启投递方案
     */
    private void themes() {
        showTipDialog(getString(R.string.str_loading));

        Map mapTableRows = MapUtils.newHashMap();
        mapTableRows.put("json", true);
        mapTableRows.put("code", ASSOCIATION_VOTE_CONTRACT);
        mapTableRows.put("scope", ASSOCIATION_VOTE_CONTRACT);
        mapTableRows.put("table", AssociationVoteTable.getSchemes());
        mapTableRows.put("index_position", "4");
        mapTableRows.put("key_type", "i64");
        mapTableRows.put("lower_bound", "1");
        mapTableRows.put("upper_bound", "1");
        emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                .subscribe(this::themesSuccess, this::onError);
//        Map params = MapUtils.newHashMap();
//        params.put("page", String.valueOf(pageInfo.page));
//        params.put("limit", String.valueOf(limit));
//        String json = GsonUtils.toJson(params);
//        try {
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().themes(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::themesSuccess, this::onError);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

    private void themesSuccess(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        themesList.clear();
        if (o != null) {
            SchemesThemesBean schemesThemesBean = GsonUtils.fromJson((String) o, SchemesThemesBean.class);
            if (schemesThemesBean != null) {
                List<SchemesThemesBean.RowsBean> rowsBeanList = schemesThemesBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    themesList.addAll(rowsBeanList);
//                    CollectionUtils.filter(themesList, new CollectionUtils.Predicate<SchemesThemesBean.RowsBean>() {
//                        @Override
//                        public boolean evaluate(SchemesThemesBean.RowsBean item) {//
//
//
//                            return ObjectUtils.equals(mangoWallet.getWalletAddress(), item.getOrder_taker());
//                        }
//                    });
                }
            }
//            if (themesBean.getCode() == 0) {
//                if (pageInfo.isFirstPage()) {
//                    if (CollectionUtils.isNotEmpty(themesBean.getData())) {
//                        ThemesBean.DataBean dataBean = themesBean.getData().get(0);
//                        startTimeTv.setText((ObjectUtils.isEmpty(dataBean.getVoteStartTime()) ? "" : dataBean.getVoteStartTime()) + (ObjectUtils.isEmpty(dataBean.getVoteEndTime()) ? "" : dataBean.getVoteEndTime()));
//                        startTimeTv.setVisibility(View.VISIBLE);
//                    } else {
//                        startTimeTv.setVisibility(View.GONE);
//                    }
//                    themesList.clear();
//                    themesList.addAll(themesBean.getData());
//                } else {
//                    themesList.addAll(themesList.size(), themesBean.getData());
//                }
//                voteMainAdapter.notifyDataSetChanged();
//            } else {
//                ToastUtils.showLong(themesBean.getMsg());
//            }
        }
        voteMainAdapter.notifyDataSetChanged();
    }

    private void isSchemeSuccess(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            SchemeConfigBean schemeConfigBean = GsonUtils.fromJson((String) o, SchemeConfigBean.class);
            if (schemeConfigBean != null) {
                List<SchemeConfigBean.RowsBean> rowsBeanList = schemeConfigBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    rowsConfigBean = rowsBeanList.get(0);
                    if (rowsConfigBean != null) {
                        isScheme = rowsConfigBean.getScheme() == 1;
                        String mTotalVoteCount = ObjectUtils.isEmpty(rowsConfigBean.getVote_count()) ? "0.0000 MGP" : rowsConfigBean.getVote_count();
                        mTotalVoteCount = mTotalVoteCount.split(" ")[0];
                        mbdTotalVoteCount = new BigDecimal(mTotalVoteCount);
                    }
                }
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        LogUtils.dTag("error==", "e = " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void onError(Object e) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    private void toFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        if (type == 0) {//发布方案
            bundle.putBoolean("isEdit", false);
            startFragment("AddVoteMainFragment", bundle);
        } else if (type == 1) {//方案详情
            bundle.putParcelable(EXTRA_VOTE_DATA, dataBean);
            startFragment("VoteDetailsFragment", bundle);
        } else if (type == 2) {//我的方案
            startFragment("MyVoteMainFragment", bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isScheme();
        refreshLayout.autoRefresh();
    }
}
