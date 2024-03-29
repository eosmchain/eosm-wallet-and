package com.token.mangowallet.ui.fragment.mgp_deal;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.OTCGlobalBean;
import com.token.mangowallet.bean.PayInfoUserInfoBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.BACKDEAL_ORDER;
import static com.token.mangowallet.utils.Constants.BUYER_REVOKE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.OTC_ARBITER_ORDERS;
import static com.token.mangowallet.utils.Constants.OTC_BUYER_ORDERS;
import static com.token.mangowallet.utils.Constants.OTC_SELLER_ORDERS;
import static com.token.mangowallet.utils.Constants.PASS_DEAL;
import static com.token.mangowallet.utils.Constants.RESTART_ORDER;

public class BuyerTransactionInfoFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.buyerStatusTv)
    AppCompatTextView buyerStatusTv;
    @BindView(R.id.buyerStatusValTv)
    AppCompatTextView buyerStatusValTv;
    @BindView(R.id.paymentTimeRemainingTv)
    AppCompatTextView paymentTimeRemainingTv;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.serviceTipTv)
    AppCompatTextView serviceTipTv;
    @BindView(R.id.serviceWechatNumberTv)
    AppCompatTextView serviceWechatNumberTv;
    @BindView(R.id.serviceWechatNumberValTv)
    AppCompatTextView serviceWechatNumberValTv;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.cancelOrderBtn)
    QMUIRoundButton cancelOrderBtn;
    @BindView(R.id.paidPutCoinBtn)
    QMUIRoundButton paidPutCoinBtn;
    @BindView(R.id.btnLayout)
    ConstraintLayout btnLayout;
    @BindView(R.id.paymentTimeRemainingValTv)
    AppCompatTextView paymentTimeRemainingValTv;
    @BindView(R.id.paymentInfoFragment)
    FrameLayout paymentInfoFragment;
    @BindView(R.id.dealContactFragment)
    FrameLayout dealContactFragment;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.transactionDetailFragment)
    FrameLayout transactionDetailFragment;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.restartTimerValTv)
    AppCompatTextView restartTimerValTv;

    private Unbinder unbinder;
    private int mCurIndex = 0;
    private Dialog cancelledDialog;
    private Dialog paymentWayDialog;
    private Dialog confirmReceiptDialog;
    private Bundle bundle;
    private MangoWallet mangoWallet;
    private String amountPaid = "0";
    private PaymentInfoFragment mPaymentInfoFragment;
    private ContactMethodFragment mContactMethodFragment;
    private TransactionDetailFragment mTransactionDetailFragment;
    private CountDownTimer timer;
    private List<PayInfoUserInfoBean.DataBean.PayInfosBean> payInfoList = new ArrayList<>();
    private DealsOrderBean.RowsBean mRowsBean;
    private int OTC_TYPE = 0;//0 ： 卖家， 1 : 买家 ， 2 ：客服
    private String mStartTime = "0";
    private long dieoutTime = 0;
    private String mOwner;
    /**
     * // 代付款
     * taker_passed = 0， taker_passed_at= 1970-01-01T00:00:00", close = 0
     * // 待放行
     * taker_passed = 1， taker_passed_at  != 1970-01-01T00:00:00 ,close = 0
     * // 交易完成
     * 三者和时间通过三个有两个及以上!= 1970-01-01T00:00:00，close = 1
     * // 交易失败
     * 时间上三个有两个及以上!= 1970-01-01T00:00:00~~~~ 三者有两个及以上没有通过,close = 0
     * // 支付超时
     * close = 1
     * <p>
     * orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
     */
    private long orderStatus = 0;//orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
    public EMWalletRepository emWalletRepository;
    private Constants.WalletType walletType;
    private Dialog payWayTipDialog;
    private PayInfoUserInfoBean.DataBean.PayInfosBean curPayInfoBean;
    private String orderStatusTimeStr = "";//str_expire_nonpayment
    private String orderCNYQuantity = "";
    private String orderUsdQuantity = "";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_buyer_transaction_info, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        OTC_TYPE = bundle.getInt("OTC_TYPE", 0);
        amountPaid = bundle.getString("amountPaid", amountPaid);
        mOwner = bundle.getString("owner");
        mRowsBean = bundle.getParcelable("RowsBean");
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
        dieoutTime = 15 * 60 * 1000;
//        getPayInfoList();
        getTableRowsGlobal();
        if (mRowsBean != null) {
            String deal_quantity = ObjectUtils.isEmpty(mRowsBean.getDeal_quantity()) ? "0.0000 MGP" : mRowsBean.getDeal_quantity();
            BigDecimal dealQuantityDecimal = new BigDecimal(deal_quantity.split(" ")[0]);
            String order_price_usd = ObjectUtils.isEmpty(mRowsBean.getOrder_price_usd()) ? "0.0000 USD" : mRowsBean.getOrder_price_usd();
            BigDecimal orderPriceUsdDecimal = new BigDecimal(order_price_usd.split(" ")[0]);
            String order_price = ObjectUtils.isEmpty(mRowsBean.getOrder_price()) ? "0.00 CNY" : mRowsBean.getOrder_price();
            BigDecimal orderPriceDecimal = new BigDecimal(order_price.split(" ")[0]);
            BigDecimal totalPricesDecimal = dealQuantityDecimal.multiply(orderPriceDecimal);
            orderCNYQuantity = "¥" + totalPricesDecimal.setScale(2, RoundingMode.FLOOR).toPlainString();
            orderUsdQuantity = dealQuantityDecimal.multiply(orderPriceUsdDecimal).setScale(4, RoundingMode.FLOOR).toPlainString() + " USD";
        }
        getPayInfo();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_my_order);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        buyerStatusValTv.setText(amountPaid.contains("￥") ? amountPaid : "￥" + amountPaid);

        countDown();
    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.cancelOrderBtn, R.id.paidPutCoinBtn, R.id.serviceWechatNumberValTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelOrderBtn:
                //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
                if (OTC_TYPE == OTC_BUYER_ORDERS) {
                    if (orderStatus == 0) {
                        showConfirmMessageDialog();
                    }
                } else if (OTC_TYPE == OTC_SELLER_ORDERS) {
                    if (orderStatus == 2) {
                        ToastUtils.showLong(getString(R.string.str_arbitration_collection));
                    }
                } else if (OTC_TYPE == OTC_ARBITER_ORDERS) {
                    if (orderStatus == 3) {

                    }
                }
                break;
            case R.id.paidPutCoinBtn:
                if (OTC_TYPE == OTC_BUYER_ORDERS) {
                    if (orderStatus == 0) {
                        showPayWayTipDialog();
                    }
                } else if (OTC_TYPE == OTC_SELLER_ORDERS) {
                    if (orderStatus == 2) {
                        showConfirmReceiptDialog();
                    }
                } else if (OTC_TYPE == OTC_ARBITER_ORDERS) {
                    if (orderStatus == 3 || orderStatus == 2) {
//                        sendBackdealOrder();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        bundle.putParcelable("RowsBean", mRowsBean);
                        startFragment("SpendConfirmFragment", bundle);
                    }
                }
                break;
            case R.id.serviceWechatNumberValTv:
                if (ObjectUtils.isNotEmpty(serviceWechatNumberValTv.getText())) {
                    ClipboardUtils.copyText(serviceWechatNumberValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.restartTimerValTv:
                sendRestartOrder();
                break;
        }
    }

    private void initTabSegment() {
//        String[] tabData = new String[]{getString(R.string.str_bank_card), getString(R.string.str_alipay), getString(R.string.str_wechat_pay)};
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();
        tabBuilder.setColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_deputy),
                ContextCompat.getColor(getActivity(), R.color.app_color_orange));
        for (PayInfoUserInfoBean.DataBean.PayInfosBean dataBean : payInfoList) {
            String tabText = null;
            if (dataBean.getPayId() == 1) {
                tabText = getString(R.string.str_bank_card);
            } else if (dataBean.getPayId() == 2) {
                tabText = getString(R.string.str_wechat_pay);
            } else if (dataBean.getPayId() == 3) {
                tabText = getString(R.string.str_alipay);
            } else if (dataBean.getPayId() == 4) {
                tabText = getString(R.string.str_usdt_erc20);
            } else if (dataBean.getPayId() == 5) {
                tabText = getString(R.string.str_usdt_trc20);
            }
            if (ObjectUtils.isNotEmpty(tabText)) {
                QMUITab tab = tabBuilder.setText(tabText).build(getContext());
                mTabSegment.addTab(tab);
            }
        }
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.selectTab(mCurIndex);
        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
//               0按金额购买 1按数量购买
                if (CollectionUtils.isNotEmpty(payInfoList)) {
                    curPayInfoBean = payInfoList.get(index);
                    mPaymentInfoFragment.setPayInfo(curPayInfoBean);
                    if (OTC_TYPE == OTC_BUYER_ORDERS) {
                        if (curPayInfoBean.getPayId() == 4 || curPayInfoBean.getPayId() == 5) {
                            buyerStatusValTv.setText(orderUsdQuantity);
                            if (mTransactionDetailFragment != null) {
                                mTransactionDetailFragment.setTotalPrices(orderUsdQuantity);
                            }
                        } else {
                            buyerStatusValTv.setText(orderCNYQuantity);
                            if (mTransactionDetailFragment != null) {
                                mTransactionDetailFragment.setTotalPrices(orderCNYQuantity);
                            }
                        }
                    }
                }
                mCurIndex = index;
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

    private void showConfirmMessageDialog() {
        if (cancelledDialog == null) {
//            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.dialog_cancellation_deal, null);
            View view = View.inflate(getActivity(), R.layout.dialog_cancellation_deal, null);
            AppCompatCheckBox checkBox = view.findViewById(R.id.checkBox);
            QMUIRoundButton actionBtn1 = view.findViewById(R.id.actionBtn1);
            QMUIRoundButton actionBtn2 = view.findViewById(R.id.actionBtn2);
            cancelledDialog = new Dialog(getActivity(), R.style.loading_dialog);
            cancelledDialog.setContentView(view);
            cancelledDialog.setCancelable(false);
            cancelledDialog.setCanceledOnTouchOutside(false);
            actionBtn2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_hint));
            actionBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelledDialog.dismiss();
                }
            });
            actionBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        cancelledDialog.dismiss();
                        getClosedealOrder();
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int bgColor;
                    if (isChecked) {
                        bgColor = R.color.blueColor;
                    } else {
                        bgColor = R.color.app_color_common_hint;
                    }
                    actionBtn2.setBackgroundColor(ContextCompat.getColor(getActivity(), bgColor));
                }
            });
        }
        if (!cancelledDialog.isShowing()) {
            cancelledDialog.show();
        }
    }

    private void showPayWayTipDialog() {
        if (paymentWayDialog == null) {
//            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.dialog_cancellation_deal, null);
            View view = View.inflate(getActivity(), R.layout.dialog_cancellation_deal, null);
            AppCompatTextView titleTv = view.findViewById(R.id.titleTv);
            AppCompatTextView messageTv = view.findViewById(R.id.messageTv);
            AppCompatCheckBox checkBox = view.findViewById(R.id.checkBox);
            QMUIRoundButton actionBtn1 = view.findViewById(R.id.actionBtn1);
            QMUIRoundButton actionBtn2 = view.findViewById(R.id.actionBtn2);
            checkBox.setVisibility(View.GONE);
            titleTv.setText(getString(R.string.str_pay_mode));
            messageTv.setText(String.format(getString(R.string.str_payment_msg), curPayInfoBean.getName()));
            paymentWayDialog = new Dialog(getActivity(), R.style.loading_dialog);
            paymentWayDialog.setContentView(view);
            paymentWayDialog.setCancelable(false);
            paymentWayDialog.setCanceledOnTouchOutside(false);
            actionBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentWayDialog.dismiss();
                }
            });
            actionBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentWayDialog.dismiss();
                    sendPassdealOrder();
                }
            });
        }
        if (!paymentWayDialog.isShowing()) {
            paymentWayDialog.show();
        }
    }

    private void showConfirmReceiptDialog() {
        if (confirmReceiptDialog == null) {
//            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.dialog_cancellation_deal, null);
            View view = View.inflate(getActivity(), R.layout.dialog_cancellation_deal, null);
            AppCompatTextView titleTv = view.findViewById(R.id.titleTv);
            AppCompatTextView messageTv = view.findViewById(R.id.messageTv);
            AppCompatCheckBox checkBox = view.findViewById(R.id.checkBox);
            QMUIRoundButton actionBtn1 = view.findViewById(R.id.actionBtn1);
            QMUIRoundButton actionBtn2 = view.findViewById(R.id.actionBtn2);
            checkBox.setVisibility(View.GONE);
            titleTv.setText(getString(R.string.str_collection_confirmation));
            messageTv.setText(getString(R.string.str_otc_collection_msg));
            actionBtn1.setText(getString(R.string.str_cancel));
            actionBtn2.setText(getString(R.string.str_confirm));
            confirmReceiptDialog = new Dialog(getActivity(), R.style.loading_dialog);
            confirmReceiptDialog.setContentView(view);
            confirmReceiptDialog.setCancelable(false);
            confirmReceiptDialog.setCanceledOnTouchOutside(false);
            actionBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmReceiptDialog.dismiss();
                }
            });
            actionBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmReceiptDialog.dismiss();
                    sendPassdealOrder();
                }
            });
        }
        if (!confirmReceiptDialog.isShowing()) {
            confirmReceiptDialog.show();
        }
    }

    private void updateView() {
        if (mRowsBean != null) {
            serviceWechatNumberValTv.setText(ObjectUtils.isEmpty(mRowsBean.getArbiter()) ? "" : mRowsBean.getArbiter());
            //orderStatus 订单状态：0:代付款;1:待放行;2:交易完成;3:交易失败;4:支付超时;
            int maker_passed = 0;//商家等于1表示通过，反正没通过
            int taker_passed = 0;//买家等于1表示通过，反正没通过
            int arbiter_passed = 0;//客服等于1表示通过，反正没通过
            if (ObjectUtils.equals("1970-01-01T00:00:00", mRowsBean.getMaker_passed_at())) {
                maker_passed = 0;
            } else {
                maker_passed = 1;
            }
            if (ObjectUtils.equals("1970-01-01T00:00:00", mRowsBean.getTaker_passed_at())) {
                taker_passed = 0;
            } else {
                taker_passed = 1;
            }
            if (ObjectUtils.equals("1970-01-01T00:00:00", mRowsBean.getArbiter_passed_at())) {
                arbiter_passed = 0;
            } else {
                arbiter_passed = 1;
            }
            if (mRowsBean.getTaker_passed() == 0 && taker_passed == 0 && mRowsBean.getClosed() == 0) {
                //代付款 taker_passed = 0， taker_passed_at= 1970-01-01T00:00:00", close = 0
                mStartTime = ObjectUtils.isEmpty(mRowsBean.getExpired_at()) ? "0" : mRowsBean.getExpired_at();
                long mistiming = TimeUtils.getSurplusMillisTime(mStartTime);//expiration_at
                //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
                if (mistiming > 0) {
                    orderStatus = 0;
                } else {
                    orderStatus = 1;
                }
                if (OTC_TYPE == OTC_BUYER_ORDERS) {
                    initTabSegment();
                    mTabSegment.setVisibility(View.VISIBLE);
                    mPaymentInfoFragment = new PaymentInfoFragment();
                    if (CollectionUtils.isNotEmpty(payInfoList)) {
                        curPayInfoBean = payInfoList.get(mCurIndex);
                    }
                    bundle.putParcelable("PayInfosBean", curPayInfoBean);
                    mPaymentInfoFragment.setArguments(bundle);
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.paymentInfoFragment, mPaymentInfoFragment, "PaymentInfoFragment")
                            .addToBackStack(null)
                            .commit();
                    buyerStatusTv.setText(getString(R.string.str_pay_seller));
                    if (orderStatus == 0) {
                        btnLayout.setVisibility(View.VISIBLE);
                    } else {
                        btnLayout.setVisibility(View.GONE);
                    }
                    restartTimerValTv.setVisibility(View.GONE);
                } else if (OTC_TYPE == OTC_SELLER_ORDERS) {
                    mTabSegment.setVisibility(View.GONE);
                    buyerStatusTv.setText(getString(R.string.str_wait_buyer_payment));
                    btnLayout.setVisibility(View.GONE);
                    restartTimerValTv.setVisibility(View.GONE);
                } else if (OTC_TYPE == OTC_ARBITER_ORDERS) {
                    mTabSegment.setVisibility(View.GONE);
                    buyerStatusTv.setText(getString(R.string.str_wait_buyer_payment));
                    btnLayout.setVisibility(View.GONE);
                    if (orderStatus == 0) {
                        restartTimerValTv.setVisibility(View.GONE);
                    } else {
                        restartTimerValTv.setVisibility(View.VISIBLE);
                    }
                }

                mContactMethodFragment = new ContactMethodFragment();
                mContactMethodFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dealContactFragment, mContactMethodFragment, "ContactMethodFragment")
                        .addToBackStack(null)
                        .commit();
                mTransactionDetailFragment = new TransactionDetailFragment();
                mTransactionDetailFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.transactionDetailFragment, mTransactionDetailFragment, "TransactionDetailFragment")
                        .addToBackStack(null)
                        .commit();

                cancelOrderBtn.setVisibility(View.VISIBLE);
                paidPutCoinBtn.setVisibility(View.VISIBLE);
                cancelOrderBtn.setText(getString(R.string.str_cancel_order));
                paidPutCoinBtn.setText(getString(R.string.str_paid_put_coin));
                orderStatusTimeStr = getString(R.string.str_expire_nonpayment);
            } else if (mRowsBean.getTaker_passed() == 1
                    && !ObjectUtils.equals("1970-01-01T00:00:00", mRowsBean.getTaker_passed_at())
                    && mRowsBean.getClosed() == 0) {
                //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
                //待放行 taker_passed = 1， taker_passed_at  != 1970-01-01T00:00:00 ,close = 0 , maker_expiration_at
                mStartTime = ObjectUtils.isEmpty(mRowsBean.getMaker_expired_at()) ? "0" : mRowsBean.getMaker_expired_at();
                long mistiming = TimeUtils.getSurplusMillisTime(mStartTime);//maker_expiration_at
                if (mistiming > 0) {
                    orderStatus = 2;
                } else {
                    orderStatus = 3;
                }
                orderStatusTimeStr = getString(R.string.str_expire_notrelease);
                if (OTC_TYPE == OTC_BUYER_ORDERS) {
                    mTabSegment.setVisibility(View.VISIBLE);
                    buyerStatusTv.setVisibility(View.GONE);
                    buyerStatusValTv.setText(getString(R.string.str_waiting_seller_discharged));

                    mPaymentInfoFragment = new PaymentInfoFragment();
                    mPaymentInfoFragment.setArguments(bundle);
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.paymentInfoFragment, mPaymentInfoFragment, "PaymentInfoFragment")
                            .addToBackStack(null)
                            .commit();

                    btnLayout.setVisibility(View.GONE);
                } else if (OTC_TYPE == OTC_SELLER_ORDERS) {
                    String order_price = (ObjectUtils.isEmpty(mRowsBean.getOrder_price()) ? "0.00 CNY" : mRowsBean.getOrder_price()).split(" ")[0];
                    String deal_quantity = (ObjectUtils.isEmpty(mRowsBean.getDeal_quantity()) ? "0.0000 MGP" : mRowsBean.getDeal_quantity()).split(" ")[0];
                    String transactionAmountVal = new BigDecimal(order_price).multiply(new BigDecimal(deal_quantity)).setScale(2, RoundingMode.FLOOR).toPlainString();
                    mTabSegment.setVisibility(View.GONE);
                    buyerStatusTv.setText(getString(R.string.str_buyer_account_paid));
                    buyerStatusValTv.setText("￥" + transactionAmountVal);
                    if (orderStatus == 2) {
                        btnLayout.setVisibility(View.VISIBLE);
                    } else {
                        btnLayout.setVisibility(View.GONE);
                    }
                    cancelOrderBtn.setText(getString(R.string.str_apply_arbitration));
                    paidPutCoinBtn.setText(getString(R.string.str_confirmed_collection));
                } else if (OTC_TYPE == OTC_ARBITER_ORDERS) {
                    mTabSegment.setVisibility(View.GONE);
                    buyerStatusTv.setText(getString(R.string.str_buyer_account_paid));
                    btnLayout.setVisibility(View.VISIBLE);
                    if (orderStatus == 2) {
                        restartTimerValTv.setVisibility(View.GONE);
                        cancelOrderBtn.setVisibility(View.GONE);
                        paidPutCoinBtn.setText(getString(R.string.str_buyer_unpaid_confirm));
                    } else {
                        restartTimerValTv.setVisibility(View.VISIBLE);
                        cancelOrderBtn.setVisibility(View.VISIBLE);
                        cancelOrderBtn.setText(getString(R.string.str_buyer_spend_confirm));
                        paidPutCoinBtn.setText(getString(R.string.str_buyer_unpaid_confirm));
                    }
                    orderStatusTimeStr = getString(R.string.str_order_timeout);
                }
                mStartTime = ObjectUtils.isEmpty(mRowsBean.getMaker_expired_at()) ? "0" : mRowsBean.getMaker_expired_at();
                paymentTimeRemainingTv.setText(getString(R.string.str_discharged_time_remaining));

                paymentTimeRemainingValTv.setVisibility(View.VISIBLE);

                mContactMethodFragment = new ContactMethodFragment();
                mContactMethodFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dealContactFragment, mContactMethodFragment, "ContactMethodFragment")
                        .addToBackStack(null)
                        .commit();
                mTransactionDetailFragment = new TransactionDetailFragment();
                for (int i = 0; i < payInfoList.size(); i++) {
                    PayInfoUserInfoBean.DataBean.PayInfosBean payInfosBean = payInfoList.get(i);
                    if (payInfosBean.getPayId() == mRowsBean.getPay_type()) {
//                        mPaymentInfoFragment.setPayInfo(payInfosBean);
                        bundle.putParcelable("PayInfosBean", payInfosBean);
                    }
                }
                mTransactionDetailFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.transactionDetailFragment, mTransactionDetailFragment, "TransactionDetailFragment")
                        .addToBackStack(null)
                        .commit();
            } else if ((maker_passed + taker_passed + arbiter_passed) > 1
                    && (mRowsBean.getTaker_passed() + mRowsBean.getMaker_passed() + mRowsBean.getArbiter_passed()) > 1
                    && mRowsBean.getClosed() == 1) {
                //maker_passed_at 商家通过时间
                //taker_passed_at 买家通过时间
                //arbiter_passed_at 客服通过时间
                // 交易完成  三者和时间通过三个有两个及以上!= 1970-01-01T00:00:00，close = 1
                //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
                orderStatus = 4;
                mTabSegment.setVisibility(View.GONE);
                buyerStatusTv.setVisibility(View.GONE);
                btnLayout.setVisibility(View.GONE);
                buyerStatusValTv.setText(R.string.str_transaction_success);
//                paymentTimeRemainingTv.setVisibility(View.GONE);
//                paymentTimeRemainingValTv.setVisibility(View.GONE);
                String mgpnumVal = ObjectUtils.isEmpty(mRowsBean.getDeal_quantity()) ? "0.0000 MGP" : mRowsBean.getDeal_quantity();
                orderStatusTimeStr = getString(R.string.str_purchase_succeed) + mgpnumVal;
                paymentTimeRemainingTv.setTextColor(getResources().getColor(R.color.app_color_common_deputy));

                mContactMethodFragment = new ContactMethodFragment();
                mContactMethodFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dealContactFragment, mContactMethodFragment, "ContactMethodFragment")
                        .addToBackStack(null)
                        .commit();

                mTransactionDetailFragment = new TransactionDetailFragment();
                for (int i = 0; i < payInfoList.size(); i++) {
                    PayInfoUserInfoBean.DataBean.PayInfosBean payInfosBean = payInfoList.get(i);
                    if (payInfosBean.getPayId() == mRowsBean.getPay_type()) {
//                        mPaymentInfoFragment.setPayInfo(payInfosBean);
                        bundle.putParcelable("PayInfosBean", payInfosBean);
                    }
                }
                mTransactionDetailFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.transactionDetailFragment, mTransactionDetailFragment, "TransactionDetailFragment")
                        .addToBackStack(null)
                        .commit();
            }
//            else if ((maker_passed + taker_passed + arbiter_passed) > 1 && mRowsBean.getClosed() == 0) {
//                //交易失败 时间上三个有两个及以上!= 1970-01-01T00:00:00~~~~ 三者有两个及以上没有通过,close = 0
//                orderStatus = 3;
//                mTabSegment.setVisibility(View.GONE);
//                buyerStatusTv.setVisibility(View.GONE);
//                paymentTimeRemainingTv.setVisibility(View.GONE);
//                paymentTimeRemainingValTv.setVisibility(View.GONE);
//                buyerStatusValTv.setText(R.string.str_transaction_fail);
//                paymentInfoFragment.setVisibility(View.GONE);
//                dealContactFragment.setVisibility(View.GONE);
//                btnLayout.setVisibility(View.GONE);
//                line1.setVisibility(View.GONE);
//
//                mTransactionDetailFragment = new TransactionDetailFragment();
//                getChildFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.transactionDetailFragment, mTransactionDetailFragment, "TransactionDetailFragment")
//                        .addToBackStack(null)
//                        .commit();
//                mTransactionDetailFragment.setArguments(bundle);
//            }
            else if (mRowsBean.getClosed() == 1) {
                //支付超时 close = 1
                orderStatus = 5;
                mTabSegment.setVisibility(View.GONE);
                buyerStatusTv.setVisibility(View.GONE);
                paymentTimeRemainingTv.setVisibility(View.GONE);
                paymentTimeRemainingValTv.setVisibility(View.GONE);
                buyerStatusValTv.setText(R.string.str_transaction_cancelled);
                paymentInfoFragment.setVisibility(View.GONE);
                dealContactFragment.setVisibility(View.GONE);
                btnLayout.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);

                mTransactionDetailFragment = new TransactionDetailFragment();
                mTransactionDetailFragment.setArguments(bundle);
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.transactionDetailFragment, mTransactionDetailFragment, "TransactionDetailFragment")
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    /**
     * 倒计时显示
     */
    private void countDown() {
        long mistiming = TimeUtils.getSurplusMillisTime(mStartTime);//开始时间与当前时间的差
        if (mistiming > 0) {
            timer = new CountDownTimer(mistiming, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Map<String, Long> timeMap = TimeUtils.formatDuring(millisUntilFinished);
                    long minutes = timeMap.get("minutes");
                    long seconds = timeMap.get("seconds");

                    if (minutes + seconds > 0) {
                        paymentTimeRemainingValTv.setText(minutes + ":" + seconds);
                    } else {
                        paymentTimeRemainingValTv.setText("");
                        paymentTimeRemainingTv.setText(orderStatusTimeStr);
                    }
                }

                @Override
                public void onFinish() {
//                paymentTimeRemainingValTv.setText("重新获取验证码");

                }
            }.start();
        } else {
            paymentTimeRemainingValTv.setText("");
            paymentTimeRemainingTv.setText(orderStatusTimeStr);
        }
    }

    /**
     * 买家撤单
     */
    private void getClosedealOrder() {
        if (mRowsBean != null) {
            try {
                showTipDialog(getString(R.string.str_loading));
                Map param = MapUtils.newHashMap();
                param.put("taker", mRowsBean.getOrder_taker());
                param.put("deal_id", String.valueOf(mRowsBean.getId()));
                String json = GsonUtils.toJson(param);
                emWalletRepository.sendTransaction(BUYER_REVOKE, mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), MainActivity.deal_contract, json, walletType)
                        .subscribe(this::onClosedealOrderSuccess, this::onError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 交易确认
     * <p>
     * user_type 0 ： 卖家， 1 : 买家 ， 2 ：客服
     */
    private void sendPassdealOrder() {
        if (mRowsBean != null) {
            try {
                showTipDialog(getString(R.string.str_loading));
                Map param = MapUtils.newHashMap();
                param.put("owner", mangoWallet.getWalletAddress());
                param.put("user_type", String.valueOf(OTC_TYPE));
                param.put("deal_id", String.valueOf(mRowsBean.getId()));
                param.put("pass", true);
                param.put("pay_type", OTC_TYPE == OTC_BUYER_ORDERS ? String.valueOf(curPayInfoBean.getPayId()) : String.valueOf(mRowsBean.getPay_type()));
                String json = GsonUtils.toJson(param);
                emWalletRepository.sendTransaction(PASS_DEAL, mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), MainActivity.deal_contract, json, walletType)
                        .subscribe(this::onPassdealSuccess, this::onError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 订单重启 （仲裁者账号）
     * <p>
     * user_type  0 ：重启待放行 1 重启代付款
     * //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
     */
    private void sendRestartOrder() {
        if (mRowsBean != null) {
            try {
                showTipDialog(getString(R.string.str_loading));
                String user_type = "";
                if (orderStatus == 1) {
                    user_type = "1";
                } else if (orderStatus == 3) {
                    user_type = "0";
                }
                Map param = MapUtils.newHashMap();
                param.put("owner", mangoWallet.getWalletAddress());
                param.put("deal_id", String.valueOf(mRowsBean.getId()));//交易订单id
                param.put("user_type", user_type);
                String json = GsonUtils.toJson(param);
                emWalletRepository.sendTransaction(RESTART_ORDER, mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), MainActivity.deal_contract, json, walletType)
                        .subscribe(this::onRestartOrderSuccess, this::onError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 款项异常（订单变成代付款）
     * <p>
     * user_type  0 ：重启待放行 1 重启代付款
     * //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
     */
    private void sendBackdealOrder() {
        if (mRowsBean != null) {
            try {
                showTipDialog(getString(R.string.str_loading));
                Map param = MapUtils.newHashMap();
                param.put("owner", mangoWallet.getWalletAddress());
                param.put("deal_id", String.valueOf(mRowsBean.getId()));//交易订单id
                String json = GsonUtils.toJson(param);
                emWalletRepository.sendTransaction(BACKDEAL_ORDER, mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), MainActivity.deal_contract, json, walletType)
                        .subscribe(this::onPassdealSuccess, this::onError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送验证码
     * 1、mail: String,  0. 绑定时的邮箱 ；type=1、2不传mail；
     * 2、mgpName: String,  type: 0当前账户； 1卖家账户 ； 2 买家账户；
     * 3、type: Int,  0邮箱绑定；1订单支付通知；2.放行；
     * 4、money: Double?  type： 0：不传money； 1：购买的MGP； 2需要打款的MGP
     *
     * @"已通知商家放行"
     * @{@"mgpName":self.dicData[@"order_maker"],@"money":self.dicData[@"deal_quantity"],@"type":@"1",@"payMgpName":self.dicData[@"order_taker"]}
     */
    private void sendVerificationCode() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            if (OTC_TYPE == OTC_BUYER_ORDERS) {
                params.put("mgpName", mRowsBean.getOrder_maker());
                params.put("money", mRowsBean.getDeal_quantity());
                params.put("type", "1");
                params.put("payMgpName", mangoWallet.getWalletAddress());
            } else if (OTC_TYPE == OTC_SELLER_ORDERS) {
                params.put("type", "2");
                params.put("mgpName", mRowsBean.getOrder_taker());
                params.put("money", mRowsBean.getDeal_quantity());
                params.put("payMgpName", mangoWallet.getWalletAddress());
            }
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().sendVerificationCode(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSendMailSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取收款方式
     */
//    private void getPayInfoList() {
//        try {
//            showTipDialog(getString(R.string.str_loading));
//            Map params = MapUtils.newHashMap();
//            params.put("mgpName", mangoWallet.getWalletAddress());
//            String json = GsonUtils.toJson(params);
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().getPayInfoList(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::onPayInfoSuccess, this::onError);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 投票配置表 比如：过多长时间才能撤回自己参加的投票（refund_delay_sec）；发布节点最小支付数额（min_bp_list_quantity）
     */
    private void getTableRowsGlobal() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", MainActivity.deal_contract);
            mapTableRows.put("code", MainActivity.deal_contract);
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("table", "global");
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onGlobalSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPayInfo() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", ObjectUtils.isEmpty(mOwner) ? mRowsBean.getOrder_maker() : mOwner);
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().payInfo(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPayInfoSuccess, this::onError);
//            new RxSubscriber<JsonObject>(otcDealFragment.getActivity(), false) {
//                        @Override
//                        public void onFail(String failMsg) {
//                            LogUtils.eTag(LOG_TAG, "failMsg = " + failMsg);
//                        }
//
//                        @Override
//                        public void onSuccess(JsonObject jsonObject) {
//                            if (ObjectUtils.isNotEmpty(jsonObject)) {
//                                PayInfoUserInfoBean payInfoUserInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayInfoUserInfoBean.class);
//                                if (payInfoUserInfoBean != null) {
//                                    if (payInfoUserInfoBean.getCode() == 0) {
//                                        PayInfoUserInfoBean.DataBean dataBean = payInfoUserInfoBean.getData();
//                                        mUserInfoHashMap.put(baseViewHolder.getAdapterPosition(), dataBean);
//                                        List<PayInfoUserInfoBean.DataBean.PayInfosBean> payInfosBeanList = dataBean.getPayInfos();
////                                        PayInfoUserInfoBean.DataBean.UserInfoBean userInfo = dataBean.getUserInfo();
//                                        if (CollectionUtils.isNotEmpty(payInfosBeanList)) {
//                                            for (int i = 0; i < payInfosBeanList.size(); i++) {
//                                                PayInfoUserInfoBean.DataBean.PayInfosBean payInfosBean = payInfosBeanList.get(i);
//                                                if (payInfosBean.getPayId() == 1) {
//                                                    bankCardIv.setVisibility(View.VISIBLE);
//                                                } else if (payInfosBean.getPayId() == 2) {
//                                                    wechatIv.setVisibility(View.VISIBLE);
//                                                } else if (payInfosBean.getPayId() == 3) {
//                                                    alipayIv.setVisibility(View.VISIBLE);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPayInfoSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            PayInfoUserInfoBean payInfoUserInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayInfoUserInfoBean.class);
            if (payInfoUserInfoBean != null) {
                if (payInfoUserInfoBean.getCode() == 0) {
                    PayInfoUserInfoBean.DataBean dataBean = payInfoUserInfoBean.getData();
                    if (dataBean != null) {
                        payInfoList.addAll(dataBean.getPayInfos());
                    }
                } else {
                    ToastUtils.showShort(payInfoUserInfoBean.getMsg());
                }
            }
        }
        updateView();
    }

    private void onRestartOrderSuccess(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean.isSuccess) {
            ToastUtils.showLong(R.string.str_restart_timer_succeed);
        } else {
            ToastUtils.showLong(R.string.str_restart_timer_fail);
        }
    }

    private void onSendMailSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean verificationCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (verificationCodeBean.getCode() == 0) {
                if (OTC_TYPE == OTC_BUYER_ORDERS) {
                    ToastUtils.showLong(R.string.str_notice_discharged);
                } else if (OTC_TYPE == OTC_SELLER_ORDERS) {
                    ToastUtils.showLong(R.string.str_buyer_notice_discharged);
                }
                popBackStack();
            } else {
                ToastUtils.showLong(verificationCodeBean.getMsg());
            }
        }
    }

    private void onClosedealOrderSuccess(TransactionBean transactionBean) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(transactionBean)) {
            if (transactionBean.isSuccess) {
                ToastUtils.showLong(R.string.str_cancel_successful);
                popBackStack();
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

    private void onPassdealSuccess(TransactionBean transactionBean) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(transactionBean)) {
            if (transactionBean.isSuccess) {
                sendVerificationCode();
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

//    private void onPayInfoSuccess(JsonObject jsonObject) {
//        dismissTipDialog();
//        payInfoList.clear();
//        if (ObjectUtils.isNotEmpty(jsonObject)) {
//            PayInfoBean payInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayInfoBean.class);
//            if (payInfoBean != null) {
//                if (payInfoBean.getCode() == 0) {
//                    if (CollectionUtils.isNotEmpty(payInfoBean.getData())) {
//                        payInfoList.addAll(payInfoBean.getData());
//                        initTabSegment();
//                    }
//                } else {
//                    ToastUtils.showLong(payInfoBean.getMsg());
//                }
//            }
//        }
//    }

    private void onGlobalSuccess(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            OTCGlobalBean otcGlobalBean = GsonUtils.fromJson(o.toString(), OTCGlobalBean.class);
            if (otcGlobalBean != null) {
                if (CollectionUtils.isNotEmpty(otcGlobalBean.getRows())) {
                    OTCGlobalBean.RowsBean mOTCGlobalBean = otcGlobalBean.getRows().get(0);
                    serviceWechatNumberValTv.setText(ObjectUtils.isEmpty(mOTCGlobalBean.getCs_contact()) ? "" : mOTCGlobalBean.getCs_contact());
                }
            }
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (mPaymentInfoFragment != null) {
//            getChildFragmentManager().beginTransaction().remove(mPaymentInfoFragment).commit();
//        }
//        if (mContactMethodFragment != null) {
//            getChildFragmentManager().beginTransaction().remove(mContactMethodFragment).commit();
//        }
//        if (mTransactionDetailFragment != null) {
//            getChildFragmentManager().beginTransaction().remove(mTransactionDetailFragment).commit();
//        }
    }
}
