package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.OTCDealAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DealMGPPopup;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.DragFloatActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class OTCDealFragment extends BaseFragment {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.mysellDragBtn)
    DragFloatActionButton mysellDragBtn;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.footer)
    ClassicsFooter footer;

    private Unbinder unbinder;
    private OTCDealAdapter otcDealAdapter;
    private QMUIDialog mQMUIDialog;
    private DealMGPPopup mDealMGPPopup;
    private MangoWallet mangoWallet;
    public EMWalletRepository emWalletRepository;
    public Constants.WalletType walletType;
    private List<SelordersBean.RowsBean> rowsBeanList = new ArrayList<>();
    private boolean isBind = false;
    private Bundle orderBundle;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mgp_buy, null);
        unbinder = ButterKnife.bind(this, root);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        return root;
    }


    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
        getOrdersTableRows();
        isBind();
    }

    @Override
    protected void initView() {
        toolbar.setTitle(R.string.str_quick_buy_mgp);
        TextView textView = (TextView) toolbar.getChildAt(2);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER_HORIZONTAL);//水平居中，CENTER，即水平也垂直，自选
        otcDealAdapter = new OTCDealAdapter(this, rowsBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(otcDealAdapter);
    }

    @Override
    protected void initAction() {
        otcDealAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SelordersBean.RowsBean rowsBean = rowsBeanList.get(position);
                showDealMGPPopup(view, rowsBean);
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getOrdersTableRows();
                isBind();
            }
        });
        otcDealAdapter.addChildClickViewIds(R.id.purchaseBtn);
        otcDealAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.purchaseBtn) {
                    if (isBind) {
                        SelordersBean.RowsBean rowsBean = rowsBeanList.get(position);
                        showDealMGPPopup(view, rowsBean);
                    } else {
                        showDialog();
                    }
                }
            }
        });
    }

    @OnClick(R.id.mysellDragBtn)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        startFragment("OTCSellFragment");
    }

    private void showDialog() {
        if (mQMUIDialog == null) {
            mQMUIDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_add_contact_title), getString(R.string.str_add_contact_msg), getString(R.string.str_cancel), getString(R.string.str_add_contact_title), new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    ToastUtils.showLong(getString(R.string.str_cancel));
                    dialog.dismiss();
                }
            }, new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    ToastUtils.showLong(getString(R.string.str_add_contact_title));
                    dialog.dismiss();
                }
            });
        }
        if (!mQMUIDialog.isShowing()) {
            mQMUIDialog.show();
        }
    }

    private void showDealMGPPopup(View view, SelordersBean.RowsBean rowsBean) {
        if (mDealMGPPopup == null) {
            mDealMGPPopup = new DealMGPPopup(getActivity());
            mDealMGPPopup.createPopup();
            mDealMGPPopup.setAnimationStyle(R.style.dialogSlideAnim);
            mDealMGPPopup.setOnOrdersClickedListener(new DealMGPPopup.OnOrdersClickedListener() {
                @Override
                public void onOrders(int ordersId, int index, String num, String amountPaid, String buyNum) {
                    getOrders(String.valueOf(ordersId), buyNum);
                    orderBundle = new Bundle();
                    orderBundle.putInt("index", index);
                    orderBundle.putString("num", num);
                    orderBundle.putString("amountPaid", amountPaid);
                    orderBundle.putParcelable(EXTRA_WALLET, mangoWallet);
                }
            });
        }
        mDealMGPPopup.setSelordersBean(rowsBean);
        if (!mDealMGPPopup.isShowing()) {
            mDealMGPPopup.showPop(view);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_otc, menu); //通过反射让menu的图标可见
        MenuItem settingItem = menu.findItem(R.id.action_setting);
        MenuItem orderItem = menu.findItem(R.id.action_order);
        settingItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("OTCSetupFragment", bundle);
            }
        });
        orderItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //当点击一个条目时，不显示另外一个
            case android.R.id.home:
                popBackStack();
                break;
        }
        return true;

    }

    /**
     * 账号自动激活
     */
    private void isBind() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("type", "0");
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().isBind(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onIsBindSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 买家下单
     *
     * @param order_id      订单id
     * @param deal_quantity MGP数量
     */
    private void getOrders(String order_id, String deal_quantity) {
        try {
            String order_sn = TimeUtils.getNowMills() + order_id + deal_quantity.replace(".", "");
            showTipDialog(getString(R.string.str_loading));
            Map param = MapUtils.newHashMap();
            param.put("taker", mangoWallet.getWalletAddress());
            param.put("order_id", order_id);
            param.put("deal_quantity", deal_quantity + " " + MGP_SYMBOL);
            param.put("order_sn", order_sn);
            String json = GsonUtils.toJson(param);
            emWalletRepository.sendTransaction("opendeal", mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), DEAL_CONTRACT, json, walletType)
                    .subscribe(this::onTransactionSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 挂售订单
     */
    private void getOrdersTableRows() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("table", "selorders");
            mapTableRows.put("scope", DEAL_CONTRACT);
            mapTableRows.put("code", DEAL_CONTRACT);
            mapTableRows.put("json", true);
            mapTableRows.put("limit", "500");
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onOrdersSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onIsBindSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
//{"code":0,"msg":"success","data":1}
            IsBindBean isBindBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (isBindBean != null)
                isBind = isBindBean.getData() == 0 ? true : false;
        }
    }


    private void onOrdersSuccess(Object o) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (ObjectUtils.isNotEmpty(o)) {
            SelordersBean selordersBean = GsonUtils.fromJson(o.toString(), SelordersBean.class);
            if (selordersBean != null) {
                if (CollectionUtils.isNotEmpty(selordersBean.getRows())) {
                    rowsBeanList.addAll(selordersBean.getRows());
                    CollectionUtils.filter(rowsBeanList, new CollectionUtils.Predicate<SelordersBean.RowsBean>() {
                        @Override
                        public boolean evaluate(SelordersBean.RowsBean item) {
                            return ObjectUtils.equals(0, item.getClosed());
                        }
                    });
                    Collections.reverse(rowsBeanList);
                    otcDealAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void onTransactionSuccess(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                mDealMGPPopup.dismiss();
                startFragment("BuyerTransactionInfoFragment", orderBundle);
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
//        if (pageInfo.isFirstPage()) {
//            refreshLayout.finishRefresh(false);
//        } else {
//            refreshLayout.finishLoadMore(false);
//        }
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismiss(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss(true);
    }

    private void dismiss(boolean isNull) {
        if (mDealMGPPopup != null) {
            if (mDealMGPPopup.isShowing()) {
                mDealMGPPopup.dismiss();
                if (isNull)
                    mDealMGPPopup = null;
            }
        }
        if (mQMUIDialog != null) {
            if (mQMUIDialog.isShowing()) {
                mQMUIDialog.dismiss();
                if (isNull)
                    mDealMGPPopup = null;
            }
        }
    }
}
