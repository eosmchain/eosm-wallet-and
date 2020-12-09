package com.token.mangowallet.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.OrderGoodsBean;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.ClipboardUtils;

import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_GOODS;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_BUYER;
import static com.token.mangowallet.utils.Constants.EXTRA_ORDER_TYPE;

public class OrderDetailsFragment extends BaseFragment {
    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.orderStatusTv)
    AppCompatTextView orderStatusTv;
    @BindView(R.id.addressIv)
    AppCompatImageView addressIv;
    @BindView(R.id.addressNameTv)
    AppCompatTextView addressNameTv;
    @BindView(R.id.addressPhoneTv)
    AppCompatTextView addressPhoneTv;
    @BindView(R.id.addressTv)
    AppCompatTextView addressTv;
    @BindView(R.id.goodsPicIv)
    AppCompatImageView goodsPicIv;
    @BindView(R.id.goodsNameTv)
    AppCompatTextView goodsNameTv;
    @BindView(R.id.goodsSpecificationTv)
    AppCompatTextView goodsSpecificationTv;
    @BindView(R.id.goodsSumTv)
    AppCompatTextView goodsSumTv;
    @BindView(R.id.goodsNumTv)
    AppCompatTextView goodsNumTv;
    @BindView(R.id.orderInfoTv)
    AppCompatTextView orderInfoTv;
    @BindView(R.id.creationTimeTv)
    AppCompatTextView creationTimeTv;
    @BindView(R.id.paymentTimeTv)
    AppCompatTextView paymentTimeTv;
    @BindView(R.id.paymentHashTv)
    AppCompatTextView paymentHashTv;
    @BindView(R.id.orderNoTv)
    AppCompatTextView orderNoTv;
    @BindView(R.id.EVPICompanyTv)
    AppCompatTextView EVPICompanyTv;
    @BindView(R.id.trackingNumberTv)
    AppCompatTextView trackingNumberTv;
    @BindView(R.id.roundButton1)
    QMUIRoundButton roundButton1;
    @BindView(R.id.roundButton2)
    QMUIRoundButton roundButton2;
    @BindView(R.id.orderRemarksTv)
    AppCompatTextView orderRemarksTv;

    private Unbinder unbinder;
    private int type = -1;
    private boolean isBuyer;
    private OrderGoodsBean.DataBean dataBean;
    private String hash;
    private String orderNo;
    private String trackingNumber;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_details, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(EXTRA_ORDER_TYPE, 0);
        isBuyer = bundle.getBoolean(EXTRA_IS_BUYER, true);
        dataBean = bundle.getParcelable(EXTRA_GOODS);
    }

    @Override
    protected void initView() {
        topBar.setTitle(isBuyer ? R.string.str_order_details : R.string.str_seller_order_details);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        if (dataBean != null) {
            OrderGoodsBean.DataBean.ProBean proBean = dataBean.getPro();
            OrderGoodsBean.DataBean.OrderBean orderBean = dataBean.getOrder();
            OrderGoodsBean.DataBean.OrderBean.AppStoreUserDeliveryBean deliveryBean = orderBean.getAppStoreUserDelivery();

            String payStatus = "";
            String goodsBtnText = "";
            if (orderBean.getPayStatus() == 0) {//0已支付
                if (orderBean.getIsDeliver() == 0) {//0 待发货
                    payStatus = getContext().getString(R.string.str_tosend_goods);
                    if (isBuyer) {
                        if (orderBean.getRefund() != 0) {
//                        goodsBtnText = getContext().getString(R.string.str_refund);
                        }
                    } else {
                        goodsBtnText = getContext().getString(R.string.str_shipments);
                    }
                } else if (orderBean.getIsDeliver() == 1) {//1发货中
                    payStatus = getContext().getString(R.string.str_delivery);
                    if (isBuyer) {
                        goodsBtnText = getContext().getString(R.string.str_sure_goods);
                    }
                } else if (orderBean.getIsDeliver() == 2) {//2 已收货
                    payStatus = getContext().getString(R.string.str_complete_transaction);
                    if (isBuyer) {
                        if (orderBean.getRefund() != 0) {
//                        goodsBtnText = getContext().getString(R.string.str_refund);
                        }
                    }
                }
            } else if (orderBean.getPayStatus() == 1) {//1支付中
                payStatus = getContext().getString(R.string.str_inbooks);
                if (isBuyer) {
                    if (orderBean.getRefund() != 0) {
//                    goodsBtnText = getContext().getString(R.string.str_refund);
                    }
                }
            } else if (orderBean.getPayStatus() == 2) {//2支付失败
                payStatus = getContext().getString(R.string.str_payment_failure);
            } else if (orderBean.getPayStatus() == 3) {//3退款中
                payStatus = getContext().getString(R.string.str_refund_of);
            } else if (orderBean.getPayStatus() == 4) {//4已退款
                payStatus = String.format(getContext().getString(R.string.str_refunded), "").replace(":", "");
            } else if (orderBean.getPayStatus() == 5) {//5退款失败
                payStatus = getContext().getString(R.string.str_refund_failure);
            } else if (orderBean.getPayStatus() == 6) {//6取消订单
                payStatus = getContext().getString(R.string.str_cancel_order);
            } else if (orderBean.getPayStatus() == 7) {// 7待支付
                if (isBuyer) {
                    payStatus = getContext().getString(R.string.str_arrearage);
                    goodsBtnText = getContext().getString(R.string.str_go_payment);
                } else {
                    payStatus = getContext().getString(R.string.str_buyer_pay);
                }
            }

            orderStatusTv.setText(payStatus);
            if (deliveryBean != null) {
                addressNameTv.setText(ObjectUtils.isEmpty(deliveryBean.getUserName()) ? "" : deliveryBean.getUserName());
                addressTv.setText((ObjectUtils.isEmpty(deliveryBean.getCountry()) ? "" : deliveryBean.getCountry()) + (ObjectUtils.isEmpty(deliveryBean.getCity()) ? "" : deliveryBean.getCity()) + (ObjectUtils.isEmpty(deliveryBean.getDetailedAddress()) ? "" : deliveryBean.getDetailedAddress()));
                addressPhoneTv.setText(ObjectUtils.isEmpty(deliveryBean.getPhone()) ? "" : deliveryBean.getPhone());
            } else {
                addressNameTv.setText("");
                addressTv.setText("");
                addressPhoneTv.setText("");
            }
            if (proBean.getImage_url() != null) {
                Glide.with(getBaseFragmentActivity())
                        .load(proBean.getImage_url().get(0))
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .into(goodsPicIv);
            }

            goodsNameTv.setText(ObjectUtils.isEmpty(proBean.getStoreName()) ? "" : proBean.getStoreName());
            goodsSpecificationTv.setText(getContext().getString(R.string.str_goods_specification) +
                    (ObjectUtils.isEmpty(proBean.getStoreType()) ? "" : proBean.getStoreType()));
            goodsSumTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(orderBean.getProductPrice()) ? "0" : orderBean.getProductPrice(),
                    2, RoundingMode.FLOOR));

            goodsNumTv.setText("x" + (ObjectUtils.isEmpty(orderBean.getTotalNum()) ? "0" : orderBean.getTotalNum()));

            hash = ObjectUtils.isEmpty(orderBean.getHash()) ? "" : orderBean.getHash();
            orderNo = ObjectUtils.isEmpty(orderBean.getOrderId()) ? "" : orderBean.getOrderId();
            trackingNumber = ObjectUtils.isEmpty(orderBean.getNum()) ? "" : orderBean.getNum();

            orderRemarksTv.setText(getString(R.string.str_order_remarks) +
                    (ObjectUtils.isEmpty(orderBean.getBuyMark()) ? "" : orderBean.getBuyMark()));
            creationTimeTv.setText(getString(R.string.str_creation_time) +
                    (ObjectUtils.isEmpty(orderBean.getCreateTime()) ? "" : orderBean.getCreateTime()));
            paymentTimeTv.setText(getString(R.string.str_payment_time) + (ObjectUtils.isEmpty(orderBean.getPayTime()) ? "" : orderBean.getPayTime()));
            paymentHashTv.setText(getString(R.string.str_payment_hash) + hash);
            orderNoTv.setText(getString(R.string.str_orderNo) + orderNo);
            EVPICompanyTv.setText(getString(R.string.str_EVPI_company) + (ObjectUtils.isEmpty(deliveryBean.getCompany()) ? "" : deliveryBean.getCompany()));
            trackingNumberTv.setText(getString(R.string.str_tracking_number) + ":" + trackingNumber);
        }
    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.paymentHashTv, R.id.orderNoTv, R.id.trackingNumberTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.paymentHashTv:
                ClipboardUtils.copyText(hash);
                ToastUtils.showLong(R.string.str_copy_success);
                break;
            case R.id.orderNoTv:
                ClipboardUtils.copyText(orderNo);
                ToastUtils.showLong(R.string.str_copy_success);
                break;
            case R.id.trackingNumberTv:
                ClipboardUtils.copyText(trackingNumber);
                ToastUtils.showLong(R.string.str_copy_success);
                break;
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
