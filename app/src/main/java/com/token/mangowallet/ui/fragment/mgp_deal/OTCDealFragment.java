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
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.PayInfoUserInfoBean;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import static com.token.mangowallet.utils.Constants.OTC_BUYER_ORDERS;

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
    private String order_sn = "";
    private int adapterPosition = 0;

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
//        otcDealAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                SelordersBean.RowsBean rowsBean = rowsBeanList.get(position);
//                showDealMGPPopup(view, rowsBean);
//            }
//        });
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
                adapterPosition = position;
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
        mysellDragBtn.setOnClickListener(new DragFloatActionButton.OnClickListener() {
            @Override
            public void onClick() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("OTCSellFragment", bundle);
            }
        });
    }

    private void showDialog() {
        if (mQMUIDialog == null) {
            mQMUIDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_add_contact_title), getString(R.string.str_add_contact_msg), getString(R.string.str_cancel), getString(R.string.str_add_contact_title), new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
//                    ToastUtils.showLong(getString(R.string.str_cancel));
                    dialog.dismiss();
                }
            }, new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
//                    ToastUtils.showLong(getString(R.string.str_add_contact_title));
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("SetupContactFragment", bundle);
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
                    PayInfoUserInfoBean.DataBean dataBean = otcDealAdapter.mUserInfoHashMap.get(adapterPosition);
                    orderBundle = new Bundle();
                    orderBundle.putInt("OTC_TYPE", OTC_BUYER_ORDERS);
                    orderBundle.putString("amountPaid", amountPaid);
                    orderBundle.putParcelable("PayInfoUserInfoBean", dataBean);
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
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("MyOrderFragment", bundle);
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
        showTipDialog(getString(R.string.str_loading));
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
        showTipDialog(getString(R.string.str_loading));
        try {
            order_sn = TimeUtils.getNowMills() + "";// + order_id + deal_quantity.replace(".", "");
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

    /**
     * 获取交易订单
     */
    private void getTradeOrder(String order_sn) {
        try {
//            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("scope", DEAL_CONTRACT);
            mapTableRows.put("code", DEAL_CONTRACT);
            mapTableRows.put("index_position", "6");
            mapTableRows.put("table", "deals");
            mapTableRows.put("key_type", "i64");
            mapTableRows.put("lower_bound", order_sn);
            mapTableRows.put("upper_bound", order_sn);
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onTradeOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTradeOrderSuccess(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            DealsOrderBean dealsOrderBean = GsonUtils.fromJson(o.toString(), DealsOrderBean.class);
            if (dealsOrderBean != null) {
                List<DealsOrderBean.RowsBean> rowsBeanList = dealsOrderBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    DealsOrderBean.RowsBean rowsBean = rowsBeanList.get(0);
                    orderBundle.putParcelable("RowsBean", rowsBean);
                    startFragment("BuyerTransactionInfoFragment", orderBundle);
                }
            }
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
        rowsBeanList.clear();
        if (ObjectUtils.isNotEmpty(o)) {
            SelordersBean selordersBean = GsonUtils.fromJson(o.toString(), SelordersBean.class);
            if (selordersBean != null) {
                if (CollectionUtils.isNotEmpty(selordersBean.getRows())) {
                    rowsBeanList.addAll(selordersBean.getRows());
                    CollectionUtils.filter(rowsBeanList, new CollectionUtils.Predicate<SelordersBean.RowsBean>() {
                        @Override
                        public boolean evaluate(SelordersBean.RowsBean item) {//过滤closed=1、MGP数量小于最小购买值的不要
                            BigDecimal min_accept_quantity = BigDecimal.ZERO;
                            BigDecimal min_mgp_num = BigDecimal.ZERO;
                            BigDecimal price = BigDecimal.ZERO;
                            BigDecimal quantity = BigDecimal.ZERO;
                            BigDecimal frozen_quantity = BigDecimal.ZERO;
                            BigDecimal fufilled_quantity = BigDecimal.ZERO;
                            BigDecimal remaining_quantity = BigDecimal.ZERO;
                            if (ObjectUtils.isNotEmpty(item.getPrice())) { // 价格
                                String priceStr = item.getPrice();
                                priceStr = priceStr.split(" ")[0];
                                price = new BigDecimal(ObjectUtils.isEmpty(priceStr) ? "0" : priceStr);
                            }
                            if (ObjectUtils.isNotEmpty(item.getMin_accept_quantity())) {
                                String min_accept_quantityStr = item.getMin_accept_quantity();
                                min_accept_quantityStr = min_accept_quantityStr.split(" ")[0];
                                min_accept_quantity = new BigDecimal(ObjectUtils.isEmpty(min_accept_quantityStr) ? "0" : min_accept_quantityStr);
                            }
                            if (ObjectUtils.isNotEmpty(item.getQuantity())) {// 总数
                                String quantityStr = item.getQuantity();
                                quantityStr = quantityStr.split(" ")[0];
                                quantity = new BigDecimal(ObjectUtils.isEmpty(quantityStr) ? "0" : quantityStr);
                            }
                            if (ObjectUtils.isNotEmpty(item.getFrozen_quantity())) {// 冻结币
                                String frozen_quantityStr = item.getFrozen_quantity();
                                frozen_quantityStr = frozen_quantityStr.split(" ")[0];
                                frozen_quantity = new BigDecimal(ObjectUtils.isEmpty(frozen_quantityStr) ? "0" : frozen_quantityStr);
                            }
                            if (ObjectUtils.isNotEmpty(item.getFufilled_quantity())) {//交易完成数量
                                String fufilled_quantityStr = item.getFufilled_quantity();
                                fufilled_quantityStr = fufilled_quantityStr.split(" ")[0];
                                fufilled_quantity = new BigDecimal(ObjectUtils.isEmpty(fufilled_quantityStr) ? "0" : fufilled_quantityStr);
                            }
                            remaining_quantity = quantity.subtract(frozen_quantity).subtract(fufilled_quantity);
                            min_mgp_num = min_accept_quantity.divide(price, 4, BigDecimal.ROUND_HALF_UP);
                            LogUtils.dTag("evaluate==", quantity.toPlainString() + " - " + min_mgp_num.toPlainString() + " = ");
                            return ObjectUtils.equals(0, item.getClosed()) && remaining_quantity.compareTo(min_mgp_num) >= 0;//-1表示小于，0是等于，1是大于。
                        }
                    });
                    Collections.sort(rowsBeanList, new Comparator<SelordersBean.RowsBean>() {
                        @Override
                        public int compare(SelordersBean.RowsBean o1, SelordersBean.RowsBean o2) {
                            int compare;
                            String price1 = ObjectUtils.isEmpty(o1.getPrice()) ? "0.00 CNY" : o1.getPrice();
                            String price2 = ObjectUtils.isEmpty(o2.getPrice()) ? "0.00 CNY" : o2.getPrice();
                            price1 = price1.split(" ")[0];
                            price2 = price2.split(" ")[0];
                            BigDecimal priceDecimal1 = new BigDecimal(ObjectUtils.isEmpty(price1) ? "0" : price1);
                            BigDecimal priceDecimal2 = new BigDecimal(ObjectUtils.isEmpty(price2) ? "0" : price2);
                            if (priceDecimal1.compareTo(priceDecimal2) > 0) {//-1表示小于，0是等于，1是大于。
                                compare = 1;
                            } else if (priceDecimal1.compareTo(priceDecimal2) < 0) {
                                compare = -1;
                            } else {
                                compare = 0;
                            }
                            return compare;
                        }
                    });
                    otcDealAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void onTransactionSuccess(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                getTradeOrder(order_sn);
                mDealMGPPopup.dismiss();
            } else {
                dismissTipDialog();
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
    public void onResume() {
        super.onResume();
        getOrdersTableRows();
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
