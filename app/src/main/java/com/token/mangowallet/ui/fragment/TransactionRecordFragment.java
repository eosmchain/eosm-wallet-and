package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.TransactionRecordBean;
import com.token.mangowallet.bean.TransferDataBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.ui.adapter.TransactionRecordAdapter;
import com.token.mangowallet.ui.viewmodel.TransactionModelFactory;
import com.token.mangowallet.ui.viewmodel.TransactionViewModel;
import com.token.mangowallet.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_TRANSACTION_ID;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class TransactionRecordFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.recordRecyclerView)
    RecyclerView recordRecyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private Unbinder unbinder;
    private TransactionModelFactory transactionModelFactory;
    private TransactionViewModel transactionViewModel;

    private String[] titleArr;
    private TransactionRecordAdapter transactionRecordAdapter;
    private final int pos = -1;
    private int offset = -50;
    private int classify = 0;//0:全部；1：转出；2：转入；3：失败
    private List<TransactionRecordBean> originalList = new ArrayList<>();
    private List<TransactionRecordBean.ActionsBean> allActionsList = new ArrayList<>();
    private List<TransactionRecordBean.ActionsBean> outActionsList = new ArrayList<>();
    private List<TransactionRecordBean.ActionsBean> intoActionsList = new ArrayList<>();
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;
    private boolean isLoadMore;
    private String walletAddress;


    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_transaction_record, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        transactionModelFactory = new TransactionModelFactory();
        transactionViewModel = ViewModelProviders.of(this.getActivity(), transactionModelFactory)
                .get(TransactionViewModel.class);

        transactionViewModel.prepare(mangoWallet);
        transactionViewModel.defaultWallet().observe(this, this::showWallet);
        transactionViewModel.transactionRecord().observe(this, this::onTransactionRecord);
        walletAddress = mangoWallet.getWalletAddress();
        isLoadMore = false;
        getTransactionRecord();
        showTipDialog(getString(R.string.str_loading));
    }

    @Override
    protected void initView() {
        topbar.setTitle(getString(R.string.str_transaction_record));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        initTabs();
        initRecycler();
        initLoadMore();
    }

    @Override
    protected void initAction() {

    }

    private void initTabs() {
        titleArr = new String[]{getString(R.string.str_all), getString(R.string.str_out), getString(R.string.str_into), getString(R.string.str_fail)};
        QMUITabBuilder builder = mTabSegment.tabBuilder().setGravity(Gravity.CENTER);
        for (int i = 0; i < titleArr.length; i++) {
            mTabSegment.addTab(builder.setText(titleArr[i]).build(getActivity()));
        }
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, false));
        mTabSegment.selectTab(0);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                classify = index;
                switch (classify) {
                    case 0://全部
                        transactionRecordAdapter.setNewData(allActionsList);
                        break;
                    case 1://转出
                        transactionRecordAdapter.setNewData(outActionsList);
                        break;
                    case 2://转入
                        transactionRecordAdapter.setNewData(intoActionsList);
                        break;
                    case 3://失败
                        transactionRecordAdapter.setNewData(null);
                        break;
                    default://全部
                        transactionRecordAdapter.setNewData(allActionsList);
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

    private void initRecycler() {
        transactionRecordAdapter = new TransactionRecordAdapter(walletAddress);
        transactionRecordAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.view_empty, null, false));
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseFragmentActivity()));
        recordRecyclerView.setAdapter(transactionRecordAdapter);
        //添加Android自带的分割线
        recordRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        transactionRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String orderID = (String) view.getTag(R.id.OrderID);
                String quantity = (String) view.getTag(R.id.quantity);
                LogUtils.dTag("TransactionDetailsFragment==", " orderID = " + orderID);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putString(EXTRA_TRANSACTION_ID, orderID);
                bundle.putString("quantity", quantity);
                startFragment("TransactionDetailsFragment", bundle);
            }
        });
    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
    }

    public void onTransactionRecord(TransactionRecordBean transactionRecord) {
        dismissTipDialog();
        if (isLoadMore) {
            smartRefreshLayout.finishLoadMore(true);
        } else {
            originalList.clear();
            intoActionsList.clear();
            outActionsList.clear();
            allActionsList.clear();
            smartRefreshLayout.finishRefresh(true);
        }
        if (transactionRecord != null) {
            originalList.add(transactionRecord);
            List<TransactionRecordBean.ActionsBean> actions = transactionRecord.getActions();
            List<TransactionRecordBean.ActionsBean> actionsList = new ArrayList<>();
            Collections.reverse(actions);
            for (int i = 0; i < actions.size(); i++) {
                TransactionRecordBean.ActionsBean actionsBean = actions.get(i);
                TransactionRecordBean.ActionsBean.ActionTraceBean actionTraceBean = actionsBean.getAction_trace();
                TransactionRecordBean.ActionsBean.ActionTraceBean.ReceiptBean receiptBean = actionTraceBean.getReceipt();
                String receiver = receiptBean.getReceiver();
                if (ObjectUtils.equals(walletAddress, receiver)) {
                    TransactionRecordBean.ActionsBean.ActionTraceBean.ActBean actBean = actionTraceBean.getAct();
                    if (ObjectUtils.equals("transfer", actBean.getName())) {
                        try {
                            LogUtils.dTag("actBean==", actBean.getData().toString());
                            TransferDataBean dataBean = GsonUtils.fromJson(GsonUtils.toJson(actBean.getData()), new TypeToken<TransferDataBean>() {
                            }.getType());
                            String to = dataBean.getTo();
                            //0:全部；1：转出；2：转入；3：失败
                            if (ObjectUtils.equals(walletAddress, to)) {
                                if (classify == 2) {
                                    actionsList.add(actionsBean);
                                }
                                intoActionsList.add(actionsBean);
                            } else {
                                if (classify == 1) {
                                    actionsList.add(actionsBean);
                                }
                                outActionsList.add(actionsBean);
                            }
                            if (classify == 0) {
                                actionsList.add(actionsBean);
                            }
                            allActionsList.add(actionsBean);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                        actions.remove(actionsBean);
                    }
                }
            }
            if (actionsList.size() > 0) {
                transactionRecordAdapter.setList(actionsList);
            }
//        transactionRecordAdapter
        }
    }

    @OnClick({R.id.proceedsBtn, R.id.transferAccountsBtn})
    public void onViewClicked(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.proceedsBtn:
                if (ObjectUtils.isNotEmpty(walletAddress)) {
                    bundle = new Bundle();
                    bundle.putBoolean("isWalletAdress", true);
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("ProceedsAdressFragment", bundle);
                }
                break;
            case R.id.transferAccountsBtn:
                bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("TransferFragment", bundle);
                break;
        }
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                getTransactionRecord();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = false;
                getTransactionRecord();
            }
        });
        smartRefreshLayout.setEnableRefresh(true);
    }

    public void getTransactionRecord() {
        try {
            if (isLoadMore) {
                offset -= 50;
            } else {
                offset = -50;
            }
            Map params = EOSParams.getActionsPamars(pos, offset, mangoWallet.getWalletAddress());
            transactionViewModel.fetchActions(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
