package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
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
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.OrderGoodsBean;
import com.token.mangowallet.bean.OrderIDBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.exception.CompanyException;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.ui.adapter.OrderAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.ui.fragment.OperatingStepsFragment.GOODS_PAYMENT_TYPE;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_GOODS;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_BUYER;
import static com.token.mangowallet.utils.Constants.EXTRA_ORDER_TYPE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.SHOP_ACCOUNT;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.USDT_SYMBOL;

public class OrderCenterFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private static final int tabNum = 3;
    private EMWalletRepository emWalletRepository;
    private Unbinder unbinder;
    private OrderAdapter orderAdapter;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private int mCurIndex;
    private boolean isBuyer;
    private QMUIDialog passwordQmuiDialog;
    private QMUIDialog TSqmuiDialog;
    private QMUIDialog msgQmuiDialog;
    private QMUIDialog msgQmuiDialog2;
    private List<OrderGoodsBean.DataBean> orderGoodsList = new ArrayList<>();
    private OrderGoodsBean.DataBean dataBean;
    private int type = -1;//0:发货；1：退款；2：确定收货；3：去付款；
    private OrderGoodsBean.DataBean.OrderBean orderBean;
    private OrderGoodsBean.DataBean.ProBean proBean;
    private AppCompatEditText editt1;
    private AppCompatEditText editt2;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_center, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        mCurIndex = bundle.getInt(EXTRA_ORDER_TYPE);
        isBuyer = bundle.getBoolean(EXTRA_IS_BUYER);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        getOutOrder();
        emWalletRepository = new EMWalletRepository();
    }

    @Override
    protected void initView() {
        topBar.setTitle(isBuyer ? R.string.str_order_centre : R.string.str_seller_order_center);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        initTabSegment();
        orderAdapter = new OrderAdapter(orderGoodsList, isBuyer);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (type == 0) {//0:发货；1：退款；2：确定收货；3：去付款；

            } else {
                if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                    toGoods(orderBean.getOrderId(), null, null);
                } else {
                    ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
                }
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    @Override
    protected void initAction() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getOutOrder();
            }
        });
        orderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                OrderGoodsBean.DataBean dataBean = orderGoodsList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_ORDER_TYPE, mCurIndex);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                bundle.putParcelable(EXTRA_GOODS, dataBean);
                startFragment("OrderDetailsFragment", bundle);
            }
        });
        orderAdapter.setOnOrderClickListener(new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onOrder(QMUIRoundButton view, int position) {
                dataBean = orderGoodsList.get(position);
                orderBean = dataBean.getOrder();
                proBean = dataBean.getPro();
                if (ObjectUtils.equals(getContext().getString(R.string.str_shipments), view.getText().toString())) {
                    type = 0;
                    if (TSqmuiDialog == null) {
                        TSqmuiDialog = DialogHelper.showEdit2Dialog(getActivity(), getString(R.string.str_please_enter_tracking_number),
                                getString(android.R.string.cancel),
                                getString(android.R.string.ok), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        editt1.setText("");
                                        editt2.setText("");
                                    }
                                }, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        if (ObjectUtils.isEmpty(editt1.getText())) {
                                            ToastUtils.showLong(R.string.str_please_enter_DHL_name);
                                            return;
                                        }
                                        if (ObjectUtils.isEmpty(editt2.getText())) {
                                            ToastUtils.showLong(R.string.str_please_enter_tracking_number);
                                            return;
                                        }
                                        if (ObjectUtils.isNotEmpty(editt1.getText().toString()) && ObjectUtils.isNotEmpty(editt2.getText().toString())) {
                                            toGoods(orderBean.getOrderId(), editt2.getText().toString().trim(), editt1.getText().toString());
                                        } else {
                                            ToastUtils.showLong(R.string.str_please_enter_EVPI);
                                        }
                                        editt1.setText("");
                                        editt2.setText("");
                                    }
                                });
                        editt1 = (AppCompatEditText) TSqmuiDialog.findViewById(R.id.editt1);
                        editt2 = (AppCompatEditText) TSqmuiDialog.findViewById(R.id.editt2);
                    }
                    if (!TSqmuiDialog.isShowing()) {
                        TSqmuiDialog.show();
                    }
                } else if (ObjectUtils.equals(getContext().getString(R.string.str_refund), view.getText().toString())) {
                    ToastUtils.showLong(R.string.str_development_of);
//                    type = 1;
//                    if (passwordQmuiDialog == null) {
//                        passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
//                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
//                    }
//                    passwordQmuiDialog.show();
                } else if (ObjectUtils.equals(getContext().getString(R.string.str_sure_goods), view.getText().toString())) {
                    type = 2;
                    if (passwordQmuiDialog == null) {
                        passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                    }
                    passwordQmuiDialog.show();
                } else if (ObjectUtils.equals(getContext().getString(R.string.str_go_payment), view.getText().toString())) {
                    type = 3;
                    if (orderBean.getPay() == 1) {
                        if (passwordQmuiDialog == null) {
                            passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                        }
                        passwordQmuiDialog.show();
                    } else if (orderBean.getPay() == 2) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        bundle.putInt("type", GOODS_PAYMENT_TYPE);
                        bundle.putString("num", orderBean.getPayMgp());
                        bundle.putString("CollectionAddress", orderBean.getUsdtAddr());
                        bundle.putString("SYMBOL", USDT_SYMBOL);
                        bundle.putString("OrderID", orderBean.getOrderId());
                        startFragment("OperatingStepsFragment", bundle);
                    }
                } else if (ObjectUtils.equals(getContext().getString(R.string.str_cancel_order), view.getText().toString())) {
                    type = 4;
                    if (msgQmuiDialog == null) {
                        msgQmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_warm_prompt),
                                getString(R.string.str_cancellation_order_prompt),
                                getString(R.string.str_cancel),
                                getString(R.string.str_ok), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                }, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        toGoods(orderBean.getOrderId(), null, null);
                                        msgQmuiDialog.dismiss();
                                    }
                                });
                    }
                    msgQmuiDialog.show();
                } else if (ObjectUtils.equals(getContext().getString(R.string.str_collection_confirmation), view.getText().toString())) {
                    type = 5;
                    if (msgQmuiDialog2 == null) {
                        msgQmuiDialog2 = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_warm_prompt),
                                getString(R.string.str_inbooks_msg),
                                getString(R.string.str_cancel),
                                getString(R.string.str_ok), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                }, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        toGoods(orderBean.getOrderId(), null, null);
                                        msgQmuiDialog2.dismiss();
                                    }
                                });
                    }
                    msgQmuiDialog2.show();
                }
            }
        });
    }

    private void initTabSegment() {
        String[] tabData = new String[]{getString(R.string.str_all), getString(R.string.str_unpaid), getString(R.string.str_inbooks), getString(R.string.str_tosend_goods),
                getString(R.string.str_receiving), getString(R.string.str_completed), getString(R.string.str_refund_sale)};
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();

        for (String tabText : tabData) {
            QMUITab tab = tabBuilder.setText(tabText).build(getContext());
            mTabSegment.addTab(tab);
        }
        int space = QMUIDisplayHelper.dp2px(getContext(), 50);
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setPadding(space, 0, space, 0);
        mTabSegment.selectTab(mCurIndex);

        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
//                0 全部  1 待付款  2 待发货  3 发货中  4 已收货 5 退款中/已退款 /退款失败
                mCurIndex = index;
                getOutOrder();
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

    private void getOutOrder() {
        showTipDialog(getString(R.string.str_loading));
        String type = "0";
        if (mCurIndex == 0) {
            type = "0";
        } else if (mCurIndex == 1) {
            type = "7";
        } else if (mCurIndex == 2) {
            type = "1";
        } else if (mCurIndex == 3) {
            type = "2";
        } else if (mCurIndex == 4) {
            type = "3";
        } else if (mCurIndex == 5) {
            type = "4";
        } else if (mCurIndex == 6) {
            type = "5";
        }
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("type", type);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            Observable<JsonObject> observable;
            if (isBuyer) {
                //买家：0 全部  1 待付款  2 待发货  3 发货中  4 已收货 5 退款中/已退款 /退款失败
                observable = NetWorkManager.getRequest().getOrder(content);
            } else {
                //卖家：0 全部  1 待付款  2 待发货  3 发货中  4 已收货 5 退款中/已退款 /退款失败
                observable = NetWorkManager.getRequest().getOutOrder(content);
            }
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::outOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商家确认发货
     */
    private void toGoods(String orderId, String trackingNum, String company) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String content = null;
        String json;
        try {
            Observable<JsonObject> observable = null;
            //0:发货；1：退款；2：确定收货；3：去付款；
            if (type == 0) {//0:发货
                params.put("orderId", orderId);
                params.put("num", trackingNum);//快递单号
                params.put("company", company);
                json = GsonUtils.toJson(params);
                content = NRSAUtils.encrypt(json);
                observable = NetWorkManager.getRequest().merConfirm(content);
            } else if (type == 1) {//1：退款
                ToastUtils.showLong(R.string.str_development_of);
//                observable = NetWorkManager.getRequest().merConfirm(content);
            } else if (type == 2) {//2：确定收货
                params.put("orderId", orderId);
                json = GsonUtils.toJson(params);
                content = NRSAUtils.encrypt(json);
                observable = NetWorkManager.getRequest().buyerConfirm(content);
            } else if (type == 3) {//3：去付款
                transferTransaction();
            } else if (type == 4) {//4：取消订单
                params.put("orderId", orderId);
                json = GsonUtils.toJson(params);
                content = NRSAUtils.encrypt(json);
                observable = NetWorkManager.getRequest().delOrder(content);
            } else if (type == 5) {//5：商家入账中 确认收款
                params.put("orderId", orderId);
                json = GsonUtils.toJson(params);
                content = NRSAUtils.encrypt(json);
                observable = NetWorkManager.getRequest().confirm(content);
            }
            if (observable != null) {
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::toGoodsSuccess, this::onError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付订单转账
     * {"orderSn":"MGP1597654887759958804071291","currencyPrice":"0.51","dollar":"160"}
     */
    private void transferTransaction() {
        Map memoParams = MapUtils.newHashMap();
        memoParams.put("orderSn", orderBean.getOrderId());
        memoParams.put("currencyPrice", orderBean.getMgpPrice());
        memoParams.put("dollar", orderBean.getPayMoney());
        String memo = GsonUtils.toJson(memoParams);

        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", SHOP_ACCOUNT);
        params.put("quantity", orderBean.getPayMgp() + " " + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(Constants.LOG_TAG,  "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    /**
     * 添加订单
     */
    private void buyOrder(String transactionId) {
        OrderGoodsBean.DataBean.OrderBean.AppStoreUserDeliveryBean addOrderSuccess = orderBean.getAppStoreUserDelivery();
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);//用户地址或账户名
        params.put("productId", String.valueOf(proBean.getProID()));//商品ID
        params.put("detailedAddress", addOrderSuccess.getDetailedAddress());//收货详细地址
        params.put("userName", addOrderSuccess.getUserName());//用户名称
        params.put("productNum", String.valueOf(orderBean.getTotalNum()));//购买商品数量
        params.put("city", addOrderSuccess.getCity());//收货城市
        params.put("productPrice", String.valueOf(orderBean.getProductPrice()));//商品价格
        params.put("totalPostage", String.valueOf(orderBean.getTotalPostage()));//邮费
        params.put("mark", walletAddress);//用户备注
        params.put("phone", addOrderSuccess.getPhone());//电话
        params.put("payMgp", orderBean.getPayMgp());//需要支付的MGP数量
        params.put("mgpPrice", orderBean.getMgpPrice());
        params.put("hash", transactionId);
        params.put("orderId", orderBean.getOrderId());
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().buyOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOrderSuccess(JsonObject jsonData) {
        dismissTipDialog();
        OrderIDBean orderIDBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), OrderIDBean.class);
        if (orderIDBean.getCode() == 0) {
            ToastUtils.showLong(R.string.str_transaction_success);
//            String payMoney = String.valueOf(orderIDBean.getData().getPayMoney());
//            String orderID = orderIDBean.getData().getOrderID();
//            if (ObjectUtils.isEmpty(payMoney)) {
//                payMoney = "0";
//            }
//            String payMgp = new BigDecimal(payMoney).divide(bdMGPprice, BigDecimal.ROUND_HALF_UP).toPlainString();
        } else {
            ToastUtils.showLong(orderIDBean.getMsg());
        }
        getOutOrder();
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                buyOrder(transactionBean.msg);
            } else {
                dismissTipDialog();
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            dismissTipDialog();
            ToastUtils.showLong(R.string.str_payment_failure);
        }
    }

    private void outOrderSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            OrderGoodsBean orderGoodsBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), OrderGoodsBean.class);
            if (orderGoodsBean.getCode() == 0) {
                orderGoodsList.clear();
                List<OrderGoodsBean.DataBean> dataBeanList = orderGoodsBean.getData();
                if (ObjectUtils.isNotEmpty(dataBeanList)) {
                    orderGoodsList.addAll(dataBeanList);
                }
                orderAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showLong(orderGoodsBean.getMsg());
            }
        }

    }

    private void toGoodsSuccess(JsonObject jsonObject) {
        type = -1;
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            CompanyException companyException = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), CompanyException.class);
            if (companyException.getCode() == 0) {

            } else {
                ToastUtils.showLong(companyException.getMsg());
            }
        }
        getOutOrder();
    }

    private void onError(Throwable e) {
        type = -1;
        dismissTipDialog();
        refreshLayout.finishRefresh();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();
    }
}
