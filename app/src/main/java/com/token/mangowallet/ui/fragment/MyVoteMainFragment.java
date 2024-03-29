package com.token.mangowallet.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.SchemesThemesBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.VoteMainAdapter;
import com.token.mangowallet.utils.AssociationVoteTable;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DragFloatActionButton2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.ASSOCIATION_VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.VOTE_ACCOUNT;

public class MyVoteMainFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_for_shadow)
    DragFloatActionButton2 sendFloating;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tabs)
    QMUITabSegment tabs;

    private VoteMainAdapter voteMainAdapter;
    private Unbinder unbinder = null;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private PageInfo pageInfo;
    private final static int limit = 20;
    private List<SchemesThemesBean.RowsBean> themesList = new ArrayList<>();
    private SchemesThemesBean.RowsBean dataBean;
    private EMWalletRepository emWalletRepository;
    private int voteId;
    private QMUIDialog passwordQmuiDialog;
    private QMUIDialog mMsgQMUIDialog;
    public static int mIndex = 0;
    private BigDecimal balance;

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
        pageInfo = new PageInfo();
        emWalletRepository = new EMWalletRepository();
        getEOSMGPBalance();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_my_vote));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightTextButton(R.string.str_record, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFragment(2);
            }
        });
        initTabs();
        voteMainAdapter = new VoteMainAdapter(themesList, true);
        voteMainAdapter.setEmptyView(R.layout.view_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(voteMainAdapter);

        voteMainAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                dataBean = themesList.get(position);
                if (mIndex == 0) {
                    toFragment(1);
                } else {
                    if (dataBean.getAudit_status() == 0) {
                    } else if (dataBean.getAudit_status() == 1) {
                        toFragment(1);
                    } else if (dataBean.getAudit_status() == 2) {
                        toFragment(0);
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageInfo.reset();
                getMyScheme();
            }
        });

        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);
        sendFloating.setVisibility(View.GONE);
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                transferTransaction();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    @Override
    protected void initAction() {
        LogUtils.dTag("mNumer==", "mNumer = " + VoteMainFragment.mNumer);
    }

    private void initTabs() {
        tabs.setVisibility(View.VISIBLE);
        QMUITabBuilder builder = tabs.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 13), QMUIDisplayHelper.sp2px(getContext(), 15))
                .setDynamicChangeIconColor(false);

        QMUITab component = builder
                .setText(getString(R.string.str_participate_scheme))
                .setColorAttr(R.attr.qmui_config_color_gray_3, R.attr.qmui_config_color_blue)
                .build(getContext());

        QMUITab util = builder
                .setText(getString(R.string.str_issue_scheme))
                .setColorAttr(R.attr.qmui_config_color_gray_3, R.attr.qmui_config_color_blue)
                .build(getContext());

        tabs.addTab(component)
                .addTab(util);
        tabs.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                pageInfo.reset();
                mIndex = index;
                getMyScheme();
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
        tabs.selectTab(mIndex);
    }

    private void getEOSMGPBalance() {
        try {
            emWalletRepository.fetchBalance(walletAddress, walletType).subscribe(this::balanceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMyScheme() {
        try {//config/scheme/record/award
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("code", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("scope", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("table", mIndex == 0 ? AssociationVoteTable.getVoterecords() : AssociationVoteTable.getSchemes());
            mapTableRows.put("index_position", "2");
            mapTableRows.put("key_type", "i64");
            mapTableRows.put("lower_bound", walletAddress);
            mapTableRows.put("upper_bound", walletAddress);
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onMySchemeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMySchemeSuccess(Object o) {
        dismissTipDialog();
        themesList.clear();
        refreshLayout.finishRefresh();
        if (o != null) {
            SchemesThemesBean schemesThemesBean = GsonUtils.fromJson((String) o, SchemesThemesBean.class);
            if (schemesThemesBean != null) {
                List<SchemesThemesBean.RowsBean> rowsBeanList = schemesThemesBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    themesList.addAll(rowsBeanList);
                }
            }
        }
        voteMainAdapter.notifyDataSetChanged();
    }

    private void transferTransaction() {
        String memo = String.valueOf(voteId) + ",0";
        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", VOTE_ACCOUNT);

        params.put("quantity", VoteMainFragment.mNumer + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(LOG_TAG, "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

//    /**
//     * 我发布的方案
//     */
//    private void myTheme() {
//        showTipDialog(getString(R.string.str_loading));
//        Map params = MapUtils.newHashMap();
//        params.put("address", walletAddress);
//        params.put("page", String.valueOf(pageInfo.page));
//        params.put("limit", String.valueOf(limit));
//        String json = GsonUtils.toJson(params);
//        try {
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().myTheme(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::themesSuccess, this::onError);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 我参与的方案
//     */
//    private void participateThemes() {
//        showTipDialog(getString(R.string.str_loading));
//        Map params = MapUtils.newHashMap();
//        params.put("address", walletAddress);
//        params.put("page", String.valueOf(pageInfo.page));
//        params.put("limit", String.valueOf(limit));
//        String json = GsonUtils.toJson(params);
//        try {
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().participateUpdate(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::themesSuccess, this::onError);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 提交hash及抵押金额
     */
    private void upHash(String hash, String voteId) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("voteId", voteId);
        params.put("hash", hash);
        params.put("money", VoteMainFragment.mNumer);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().upHash(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::upHashSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upHashSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        refreshLayout.autoRefresh();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_release_success);
            } else {
                ToastUtils.showLong(R.string.str_release_fail);
            }
        }
    }

//    private void themesSuccess(JsonObject jsonObject) {
//        dismissTipDialog();
//        if (pageInfo.isFirstPage()) {
//            refreshLayout.finishRefresh();
//        } else {
//            refreshLayout.finishLoadMore();
//        }
////        if (jsonObject != null) {
////            ThemesBean themesBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), ThemesBean.class);
////            if (themesBean.getCode() == 0) {
////                if (pageInfo.isFirstPage()) {
////                    themesList.clear();
////                    themesList.addAll(themesBean.getData());
////                } else {
////                    themesList.addAll(themesList.size(), themesBean.getData());
////                }
////                voteMainAdapter.notifyDataSetChanged();
////            } else {
////                ToastUtils.showLong(themesBean.getMsg());
////            }
////        }
//    }

    private void balanceSuccess(BigDecimal balance) {
        this.balance = balance;
    }

    private void onTransaction(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                upHash(transactionBean.msg, String.valueOf(voteId));
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            ToastUtils.showLong(R.string.str_release_fail);
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
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
            bundle.putBoolean("isEdit", true);
            bundle.putParcelable(EXTRA_VOTE_DATA, dataBean);
            startFragment("AddVoteMainFragment", bundle);
        } else if (type == 1) {//方案详情
            bundle.putParcelable(EXTRA_VOTE_DATA, dataBean);
            startFragment("VoteDetailsFragment", bundle);
        } else if (type == 2) {
            startFragment("VoteMainRecordFragment", bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyScheme();
    }
}
