package com.token.mangowallet.ui.fragment;

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
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
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
import com.token.mangowallet.bean.CategoryBean;
import com.token.mangowallet.bean.MixMortgageBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.MixMortgageAdapter;
import com.token.mangowallet.ui.viewmodel.MortgageViewModel;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.ui.fragment.OperatingStepsFragment.TWICE_MIX_MORTGAGE_TYPE;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_FIRST;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_PLEDGE_ACCOUNT;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;

public class MixMortgageFragment extends BaseFragment {

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
    private MixMortgageAdapter mixMortgageAdapter;
    private List<MixMortgageBean.DataBean> dataBeanList = new ArrayList<>();
    private QMUIDialog qmuiDialog;
    private MixMortgageBean.DataBean dataBean;
    private EMWalletRepository emWalletRepository;

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
        emWalletRepository = new EMWalletRepository();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_mgp_payment);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        mixMortgageAdapter = new MixMortgageAdapter(dataBeanList);
        mixMortgageAdapter.setEmptyView(R.layout.view_empty);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(mixMortgageAdapter);
        if (qmuiDialog == null) {
            qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
    }

    @Override
    protected void initAction() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageInfo.reset();
                getMixMortgageList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageInfo.nextPage();
                getMixMortgageList();
            }
        });

        mixMortgageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                dataBean = dataBeanList.get(position);
                if (dataBean.getStatus() == 2) {//2 待转hmio
                    String id = String.valueOf(dataBean.getId());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    bundle.putInt("type", TWICE_MIX_MORTGAGE_TYPE);
                    bundle.putString("id", id);
                    bundle.putString("num", dataBean.getNum().toPlainString());
                    bundle.putString("SYMBOL", dataBean.getPayMoneyType());
                    startFragment("OperatingStepsFragment", bundle);
                } else if (dataBean.getStatus() == 3) {//3 待转MGP
                    if (!qmuiDialog.isShowing()) {
                        qmuiDialog.show();
                    }
                }
            }
        });
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            dialog.dismiss();
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                transferTransaction();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
        }
    };

    private void transferTransaction() {
        showTipDialog(getString(R.string.str_loading));
        String quantity = dataBean.getNum().setScale(4, BigDecimal.ROUND_HALF_DOWN).toPlainString();

        Map params = MapUtils.newHashMap();
        String code = EOSIO_TOKEN_CONTRACT_CODE;
        String action = TRANSFER_ACTION;
        params.put("memo", "staking");
        params.put("from", walletAddress);
        params.put("to", MGP_PLEDGE_ACCOUNT);
        params.put("quantity", quantity + " MGP");

        String jsonData = GsonUtils.toJson(params);

        emWalletRepository.sendTransaction(action, mangoWallet.getPrivateKey(),
                walletAddress, code, jsonData,
                Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()))
                .subscribe(this::onTransaction, this::onError);
    }

    private void getMixMortgageList() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);//walletAddress、"gaoyexingabm"
        params.put("page", String.valueOf(pageInfo.page));
        params.put("size", "20");
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getMixMortgageList(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::mixMortgageListSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadHash(String hash) {
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);//walletAddress、"gaoyexingabm"
        params.put("moneyType", "1");// 需支付币种 4 HMIO ，1 MGP
        params.put("hash", hash);
        params.put("id", String.valueOf(dataBean.getId()));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().editHash(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::uploadHashSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadHashSuccess(JsonObject jsonData) {
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showShort(R.string.str_transaction_success);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
            refreshLayout.autoRefresh();
        }
    }

    private void mixMortgageListSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            MixMortgageBean mixMortgageBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MixMortgageBean.class);
            if (mixMortgageBean.getCode() == 0) {
                if (ObjectUtils.isEmpty(mixMortgageBean.getData())) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    if (pageInfo.isFirstPage()) {
                        dataBeanList.clear();
                        dataBeanList.addAll(mixMortgageBean.getData());
                    } else {
                        dataBeanList.addAll(dataBeanList.size(), mixMortgageBean.getData());
                    }
                    mixMortgageAdapter.notifyDataSetChanged();
                }
            } else {
                refreshLayout.finishLoadMoreWithNoMoreData();
                ToastUtils.showLong(mixMortgageBean.getMsg());
            }
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean.isSuccess) {
            uploadHash(transactionBean.msg);
        } else {
            dismissTipDialog();
            ToastUtils.showLong(transactionBean.msg);
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
