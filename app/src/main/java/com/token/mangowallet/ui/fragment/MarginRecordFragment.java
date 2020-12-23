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
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MarginRecordBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.MarginRecordAdapter;
import com.token.mangowallet.ui.home.HomeFragment;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class MarginRecordFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private MarginRecordBean marginRecordBean;
    private MarginRecordAdapter marginRecordAdapter;
    private QMUIDialog mMsgQMUIDialog;

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
        getLog();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_margin_transaction_record);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        topBar.addRightTextButton(R.string.str_cancelled, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMsgQMUIDialog == null) {
                    mMsgQMUIDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_warm_prompt),
                            getString(R.string.str_cancelled_prompt),
                            getString(R.string.str_cancel),
                            getString(R.string.str_ok), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            }, new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    marginRefund();
                                }
                            });
                }
                mMsgQMUIDialog.show();
            }
        });
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        marginRecordAdapter = new MarginRecordAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(marginRecordAdapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        marginRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
            }
        });
    }

    @Override
    protected void initAction() {

    }

    /**
     * 获取保证金记录
     */
    private void getLog() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getLog(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindUsdtSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销退回保证金
     */
    private void marginRefund() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().marginRefund(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::marginRefundSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void marginRefundSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                popBackStack(HomeFragment.class);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void bindUsdtSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MarginRecordBean marginRecordBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MarginRecordBean.class);
            if (marginRecordBean.getCode() == 0) {
                if (ObjectUtils.isNotEmpty(marginRecordBean.getData())) {
                    marginRecordAdapter.setList(marginRecordBean.getData());
                }
            } else {
                ToastUtils.showLong(marginRecordBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }
}
