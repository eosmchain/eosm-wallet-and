package com.token.mangowallet.ui.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
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
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.GlobalBean;
import com.token.mangowallet.bean.MyVotesBean;
import com.token.mangowallet.bean.NodeBean;
import com.token.mangowallet.bean.NodeListBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.VotersBean;
import com.token.mangowallet.bean.VotesBean;
import com.token.mangowallet.bean.entity.NodeSection;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.StakeVotesMainAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.RSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.DragFloatActionButton;

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
import one.block.eosiojava.models.rpcProvider.response.GetInfoResponse;

import static com.token.mangowallet.utils.Constants.CANCEL_NODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.USDT_SYMBOL;
import static com.token.mangowallet.utils.Constants.WITHDRAW_VOTE;

public class StakeVoteMainFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tabs)
    QMUITabSegment tabs;
    @BindView(R.id.layout_for_shadow)
    DragFloatActionButton sendFloating;
    @BindView(R.id.becomeNodeTv)
    QMUIRoundButton becomeNodeTv;

    AppCompatTextView progressTv;
    ContentLoadingProgressBar progressBar;
    AppCompatTextView biNumTv;
    AppCompatTextView blockHeightValTv;
    AppCompatTextView viewRuleTv;
    AppCompatTextView unpaidValueTv;
    AppCompatTextView toTransferTv;
    QMUIRadiusImageView unpaidIv;
    AppCompatTextView unpaidUnitTv;
    Button mRightBtn;
    RadioGroup radioGroup;
    RadioButton defaultBtn;
    RadioButton voteNumBtn;
    RadioButton rateBtn;


    private Unbinder unbinder;
    private EMWalletRepository emWalletRepository;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private PageInfo pageInfo;
    private StakeVotesMainAdapter votesMainAdapter;
    private List<NodeSection> nodeSectionList = new ArrayList<>();
    private boolean isManage = false;
    private int mIndex = 0;
    public int type = 0;//0：获取全部投票数据；1：获取我的投票数据；2：获取我的节点；3：获取投票奖励;4：获取撤回时间
    private QMUIDialog passwordQmuiDialog;
    public GlobalBean.RowsBean mGlobalRowsBean;
    private View mMainHeaderView = null, mSubHeaderView = null;
    private String limit = "500";
    public static String mVoteContract;
    private int mCurCheckedId;
    private int sort = 1;
    private NodeSection mNodeSection;
    private NodeBean mCurNodeBean;

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
        mVoteContract = BaseUrlUtils.getInstance().getVoteContract();
        emWalletRepository = new EMWalletRepository();

    }

    @Override
    protected void initView() {
        topBar.setTitle("StakeVote");
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (isManage) {
                    isManage = false;
                    updateUI();
                } else {
                    popBackStack();
                }
            }
        });
        mRightBtn = topBar.addRightTextButton(R.string.str_management, R.id.topbar_right_change_button);
        mRightBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (!isManage) {
                    isManage = true;
                    updateUI();
                }
            }
        });
        mMainHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.view_stake_vote_header, null, false);
        progressTv = mMainHeaderView.findViewById(R.id.progressTv);
        progressBar = mMainHeaderView.findViewById(R.id.progressBar);
        biNumTv = mMainHeaderView.findViewById(R.id.biNumTv);
        blockHeightValTv = mMainHeaderView.findViewById(R.id.blockHeightValTv);
        viewRuleTv = mMainHeaderView.findViewById(R.id.viewRuleTv);
        radioGroup = mMainHeaderView.findViewById(R.id.radioGroup);
        defaultBtn = mMainHeaderView.findViewById(R.id.defaultBtn);
        voteNumBtn = mMainHeaderView.findViewById(R.id.voteNumBtn);
        rateBtn = mMainHeaderView.findViewById(R.id.rateBtn);
        progressBar.setMax(100);
        progressTv.setText(String.format(getString(R.string.str_vote_progress), "0"));


        mSubHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.view_manage_header, null, false);
        unpaidValueTv = mSubHeaderView.findViewById(R.id.unpaidValueTv);
        toTransferTv = mSubHeaderView.findViewById(R.id.toTransferTv);
        unpaidIv = mSubHeaderView.findViewById(R.id.unpaidIv);
        unpaidUnitTv = mSubHeaderView.findViewById(R.id.unpaidUnitTv);

        sendFloating.setVisibility(View.GONE);
        votesMainAdapter = new StakeVotesMainAdapter(this, nodeSectionList);
        updateUI();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(votesMainAdapter);

        initTabs();
        getTableRowsGlobal();//配置表
        getDIGICCYPrice();
        getInfo();
    }

    private void updateUI() {
        String mRightStr = "";
        String mTitle = "";
        if (isManage) {
            getTableRowsVoters();
            type = 1;
            mIndex = 0;
            tabs.selectTab(mIndex);
            mTitle = getString(R.string.str_management_center);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(0, true);
            } else {
                progressBar.setProgress(0);
            }
            mRightStr = getString(R.string.str_management);
            type = 0;
            mTitle = "StakeVote";
        }
        topBar.setTitle(mTitle);
        LogUtils.dTag("LogInterceptor==", "updateUI type = " + type);
        getTableRows();
        mRightBtn.setText(mRightStr);
        votesMainAdapter.setManage(isManage);
        votesMainAdapter.setHeaderView(isManage ? mSubHeaderView : mMainHeaderView);
        votesMainAdapter.notifyDataSetChanged();
        becomeNodeTv.setVisibility(isManage ? View.GONE : View.VISIBLE);
        tabs.setVisibility(isManage ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initAction() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageInfo.reset();
                LogUtils.dTag("LogInterceptor==", "onRefresh type = " + type);
                getTableRowsGlobal();//配置表
                if (type == 1) {
                    getTableRowsVoters();//
                }
                getDIGICCYPrice();
                getInfo();
                getTableRows();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageInfo.nextPage();

            }
        });
//        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);

        votesMainAdapter.setOnClickListener(new StakeVotesMainAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                NodeSection nodeSection = nodeSectionList.get(position);
                if (!nodeSection.isHeader) {
                    mCurNodeBean = (NodeBean) nodeSection.getObject();
                    if (type == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        bundle.putParcelable("RowsBean", mCurNodeBean);
                        bundle.putBoolean("isMy", false);
                        bundle.putInt("type", type);
                        startFragment("StakeVoteDetailsFragment", bundle);
                    } else {
                        if (type == 2) {
                            String mVoteNum = ObjectUtils.isEmpty(mCurNodeBean.getReceived_votes()) ? "0.00 MGP" : mCurNodeBean.getReceived_votes();
                            String[] strings = mVoteNum.split(" ");
                            BigDecimal mVoteNumDecimal = new BigDecimal(strings[0]);
                            if (mVoteNumDecimal.compareTo(BigDecimal.ZERO) > 0) {
                                //-1表示小于，0是等于，1是大于。
                                ToastUtils.showLong(String.format(getString(R.string.str_irrevocable_voted), mVoteNum));
                                return;
                            }
                        }
                        if (passwordQmuiDialog == null) {
                            passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                        }
                        passwordQmuiDialog.show();
                    }
                }
            }
        });

        votesMainAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                NodeSection nodeSection = nodeSectionList.get(position);
                if (!nodeSection.isHeader) {
                    NodeBean nodeBean = (NodeBean) nodeSection.getObject();
                    boolean isMy;
                    if (isManage) {
                        isMy = mIndex == 0 ? false : true;
                    } else {
                        isMy = false;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    bundle.putParcelable("RowsBean", nodeBean);
                    bundle.putBoolean("isMy", isMy);
                    bundle.putInt("type", type);
                    startFragment("StakeVoteDetailsFragment", bundle);
                }
            }
        });

        becomeNodeTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (mGlobalRowsBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    bundle.putParcelable("mGlobalRowsBean", mGlobalRowsBean);
                    bundle.putBoolean("isAdd", true);
                    startFragment("StakeAddVoteFragment", bundle);
                }
            }
        });

        unpaidIv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
//                if (passwordQmuiDialog == null) {
//                    passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
//                            getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
//                }
//                passwordQmuiDialog.show();
                if (ObjectUtils.isNotEmpty(unpaidValueTv.getText())) {
                    BigDecimal unpaidDecimal = new BigDecimal(unpaidValueTv.getText().toString());
                    if (unpaidDecimal.compareTo(BigDecimal.ZERO) > 0) {
                        getAward();
                    }
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mCurCheckedId = checkedId;
                listSort();
                getNodeList();
            }
        });

        viewRuleTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                startFragment("RuleFragment");
            }
        });
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

    private void initTabs() {
        QMUITabBuilder builder = tabs.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 13), QMUIDisplayHelper.sp2px(getContext(), 15))
                .setDynamicChangeIconColor(false);

        QMUITab component = builder
                .setText(getString(R.string.str_participate_scheme))
                .setColorAttr(R.attr.qmui_config_color_black, R.attr.qmui_config_color_red)
                .build(getContext());

        QMUITab util = builder
                .setText(getString(R.string.str_my_node))
                .setColorAttr(R.attr.qmui_config_color_black, R.attr.qmui_config_color_red)
                .build(getContext());

        tabs.addTab(component)
                .addTab(util);
        tabs.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                pageInfo.reset();
                mIndex = index;
                if (isManage) {
                    getTableRowsVoters();
                    type = index + 1;
                    LogUtils.dTag("LogInterceptor==", "onTabSelected type = " + type);
                    getTableRows();
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

            }
        });
        tabs.selectTab(mIndex);
    }

    private void listSort() {
        if (mCurCheckedId == defaultBtn.getId()) {
            sort = 1;
            defaultBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            voteNumBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_dark_black));
            rateBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_dark_black));
        } else if (mCurCheckedId == voteNumBtn.getId()) {
            sort = 2;
            defaultBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_dark_black));
            voteNumBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            rateBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_dark_black));
        } else if (mCurCheckedId == rateBtn.getId()) {
            sort = 3;
            defaultBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_dark_black));
            voteNumBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_dark_black));
            rateBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    /**
     * 获取MGP价格
     */
    private void getDIGICCYPrice() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map map = MapUtils.newHashMap();
            map.put("pair", walletType + "_USDT");
            String json = GsonUtils.toJson(map);
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCoinPrice(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::priceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取首页节点列表
     */
    private void getNodeList() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map params = MapUtils.newHashMap();
            params.put("page", String.valueOf(pageInfo.page));
            params.put("limit", limit);
            params.put("sort", String.valueOf(sort));
            String json = GsonUtils.toJson(params);
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().scNodeList(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::nodeListSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取我的投票
     */
    private void votes() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map params = MapUtils.newHashMap();
            params.put("account", walletAddress);
            String json = GsonUtils.toJson(params);
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().votes(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::votesSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nodeListSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh(true);
        } else {
            refreshLayout.finishLoadMore(true);
        }
        nodeSectionList.clear();
        LogUtils.dTag("nodeListSuccess==", "onTableRows = " + jsonObject);
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            NodeListBean nodeListBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), NodeListBean.class);
            if (nodeListBean.getCode() == 0) {
                NodeListBean.DataBean dataBean = nodeListBean.getData();
                if (dataBean != null) {
                    List<NodeBean> candidateNodeList = dataBean.getCandidateNodeList();
                    List<NodeBean> superNodeList = dataBean.getSuperNodeList();
                    if (CollectionUtils.isNotEmpty(superNodeList)) {
                        mNodeSection = new NodeSection(true, false, getString(R.string.str_super_node));
                        nodeSectionList.add(mNodeSection);
                        for (NodeBean nodeBean : superNodeList) {
                            mNodeSection = new NodeSection(false, true, nodeBean);
                            nodeSectionList.add(mNodeSection);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(candidateNodeList)) {
                        mNodeSection = new NodeSection(true, false, getString(R.string.str_candidate_node));
                        nodeSectionList.add(mNodeSection);
                        for (NodeBean nodeBean : candidateNodeList) {
                            mNodeSection = new NodeSection(false, false, nodeBean);
                            nodeSectionList.add(mNodeSection);
                        }
                    }
                }
            }
        }
        votesMainAdapter.notifyDataSetChanged();
    }

    private void votesSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh(true);
        } else {
            refreshLayout.finishLoadMore(true);
        }
        nodeSectionList.clear();
        LogUtils.dTag("nodeListSuccess==", "onTableRows = " + jsonObject);
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            MyVotesBean myVotesBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MyVotesBean.class);
            if (myVotesBean.getCode() == 0) {
                MyVotesBean.DataBean dataBean = myVotesBean.getData();
                if (dataBean != null) {
                    List<NodeBean> nodeList = dataBean.getList();
                    if (CollectionUtils.isNotEmpty(nodeList)) {
                        for (NodeBean nodeBean : nodeList) {
                            mNodeSection = new NodeSection(false, false, nodeBean);
                            nodeSectionList.add(mNodeSection);
                        }
                    }
                }
            }
        }
        votesMainAdapter.notifyDataSetChanged();
    }

    private void getInfo() {
        try {
            emWalletRepository.fetchInfo().subscribe(this::infoSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取投票列表
     * type = 0：获取首页数据（全部投票列表）
     * type = 1：获取我的投票数据（自己参与的投票）
     * type = 2：获取我的节点数据（自己发布节点）和 获取未领取数额（unclaimed_rewards）
     */
    private void getTableRows() {
        if (type == 0) {
            getNodeList();
            return;
        }
        if (type == 1) {
            votes();
            return;
        }

        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", mVoteContract);
            mapTableRows.put("code", mVoteContract);
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("limit", limit);
            String table = "";
            if (type == 0) {//弃用
                table = "candidates";
            } else if (type == 1) {
                table = "votes";
                mapTableRows.put("lower_bound", walletAddress);
                mapTableRows.put("upper_bound", walletAddress);
                mapTableRows.put("index_position", "2");
                mapTableRows.put("key_type", "i64");
            } else if (type == 2) {
                table = "candidates";
                mapTableRows.put("lower_bound", walletAddress);
                mapTableRows.put("upper_bound", walletAddress);
            } else if (type == 3) {
                table = "voters";
                mapTableRows.put("lower_bound", walletAddress);
                mapTableRows.put("upper_bound", walletAddress);
            }
            mapTableRows.put("table", table);
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTableRows, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 投票配置表 比如：过多长时间才能撤回自己参加的投票（refund_delay_sec）；发布节点最小支付数额（min_bp_list_quantity）
     */
    private void getTableRowsGlobal() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", mVoteContract);
            mapTableRows.put("code", mVoteContract);
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("table", "global");
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTableRowsGlobal, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的投票
     * 获取未领取数额（unclaimed_rewards）
     */
    private void getTableRowsVoters() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", mVoteContract);
            mapTableRows.put("code", mVoteContract);
            mapTableRows.put("lower_bound", walletAddress);
            mapTableRows.put("upper_bound", walletAddress);
            mapTableRows.put("json", true);
            mapTableRows.put("table", "voters");
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTableRowsVoters, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消接口
     * type = 1：我的投票撤回
     * type = 2：我的节点取消
     */
    private void transferTransaction() {
        showTipDialog(getString(R.string.str_loading));
//@{@"owner":VALIDATE_STRING([MGPHttpRequest shareManager].curretWallet.address),@"vote_id":dic[@"id"]}
        Map params = MapUtils.newHashMap();
        String action = null;
        if (type == 1) {
            action = WITHDRAW_VOTE;
            params.put("owner", walletAddress);
            params.put("vote_id", mCurNodeBean.getId());
        } else if (type == 2) {
            action = CANCEL_NODE;
            params.put("issuer", walletAddress);
        }
        if (ObjectUtils.isNotEmpty(action)) {
            String jsonData = GsonUtils.toJson(params);
            String privatekey = mangoWallet.getPrivateKey();
            emWalletRepository.sendTransaction(action, privatekey, walletAddress, mVoteContract, jsonData, walletType)
                    .subscribe(this::onTransaction, this::onError);
        }
    }

    /**
     * 领取奖励
     */
    private void getAward() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        String action = "claimrewards";
        params.put("issuer", walletAddress);
        boolean is_voter = true;
        if (type == 1) {
            is_voter = true;
        } else if (type == 2) {
            is_voter = false;
        }
        params.put("is_voter", is_voter);
        if (ObjectUtils.isNotEmpty(action)) {
            String jsonData = GsonUtils.toJson(params);
            String privatekey = mangoWallet.getPrivateKey();
            emWalletRepository.sendTransaction(action, privatekey, walletAddress, mVoteContract, jsonData, walletType)
                    .subscribe(this::onAward, this::onError);
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                getTableRows();
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

    private void onTableRowsVoters(Object o) {
        dismissTipDialog();
        unpaidUnitTv.setText("0.0000");
        if (ObjectUtils.isNotEmpty(o)) {
            VotersBean votersBean = GsonUtils.fromJson(o.toString(), VotersBean.class);
            if (ObjectUtils.isNotEmpty(votersBean.getRows())) {
                VotersBean.RowsBean rowsBean = votersBean.getRows().get(0);
                if (rowsBean != null) {
                    String mUnclaimed_rewards = ObjectUtils.isEmpty(rowsBean.getUnclaimed_rewards()) ? "0.0000 MGP" : rowsBean.getUnclaimed_rewards();
                    String[] arrRewards = mUnclaimed_rewards.split(" ");
                    unpaidUnitTv.setText(arrRewards[1]);
                    unpaidValueTv.setText(arrRewards[0]);
                }
            }
        }
    }

    private void onTableRowsGlobal(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            GlobalBean globalBean = GsonUtils.fromJson(o.toString(), GlobalBean.class);
            if (ObjectUtils.isNotEmpty(globalBean.getRows())) {
                mGlobalRowsBean = globalBean.getRows().get(0);
                if (mGlobalRowsBean != null) {
                    progressTv.setText(String.format(getString(R.string.str_vote_progress), ObjectUtils.isEmpty(mGlobalRowsBean.getTotal_voted()) ? "0" : mGlobalRowsBean.getTotal_voted()));
                    votesMainAdapter.setRefundDelaySec(mGlobalRowsBean.getRefund_delay_sec());
                    votesMainAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void onTableRows(Object o) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh(true);
        } else {
            refreshLayout.finishLoadMore(true);
        }
        nodeSectionList.clear();
        List<NodeBean> rowsBeanList = null;
        LogUtils.dTag("onTableRows==", "onTableRows = " + o);
        if (ObjectUtils.isNotEmpty(o)) {
            VotesBean votesBean = GsonUtils.fromJson(o.toString(), VotesBean.class);
            if (votesBean != null) {
                rowsBeanList = votesBean.getRows();
                if (ObjectUtils.isNotEmpty(rowsBeanList)) {
                    if (type == 1) {//我的投票只有Unvoted_at这个字段等于1970-01-01T00:00:00.000时才显示
                        CollectionUtils.filter(rowsBeanList, new CollectionUtils.Predicate<NodeBean>() {
                            @Override
                            public boolean evaluate(NodeBean item) {
                                return ObjectUtils.equals("1970-01-01T00:00:00.000", item.getUnvoted_at());
                            }
                        });
                        Collections.reverse(rowsBeanList);
                    }
                    for (NodeBean nodeBean : rowsBeanList) {
                        mNodeSection = new NodeSection(false, false, nodeBean);
                        nodeSectionList.add(mNodeSection);
                    }
                }
            }
        }
        votesMainAdapter.notifyDataSetChanged();
        if (type == 2) {
            if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                NodeBean rowsBean = rowsBeanList.get(0);
                if (rowsBean != null) {
                    String mUnclaimed_rewards = ObjectUtils.isEmpty(rowsBean.getUnclaimed_rewards()) ? "0.0000 MGP" : rowsBean.getUnclaimed_rewards();
                    String[] arrRewards = mUnclaimed_rewards.split(" ");
                    unpaidUnitTv.setText(arrRewards[1]);
                    unpaidValueTv.setText(arrRewards[0]);
                } else {
                    unpaidUnitTv.setText("0.0000");
                }
            }

        }
    }

    private void onAward(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                getTableRowsVoters();
                ToastUtils.showLong(R.string.str_award_get_success);
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh(false);
        } else {
            refreshLayout.finishLoadMore(false);
        }
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void onError(Object e) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh(false);
        } else {
            refreshLayout.finishLoadMore(false);
        }
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void priceSuccess(CurrencyPrice data) {
        dismissTipDialog();
        LogUtils.dTag("priceSuccess==", "data = " + GsonUtils.toJson(data));
        BigDecimal bdMGPprice = BigDecimal.ZERO;
        if (data.getCode() == 0) {
            bdMGPprice = data.getData().getPrice();
        } else {
            ToastUtils.showLong(data.getMsg());
        }
        biNumTv.setText(bdMGPprice.toPlainString() + " " + USDT_SYMBOL);
    }

    private void infoSuccess(GetInfoResponse infoResponse) {
        if (infoResponse != null) {
            blockHeightValTv.setText(ObjectUtils.isEmpty(infoResponse.getHeadBlockNum()) ? "0" : String.valueOf(infoResponse.getHeadBlockNum()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.dTag("LogInterceptor==", "onResume type = " + type);
        getTableRows();
    }
}
