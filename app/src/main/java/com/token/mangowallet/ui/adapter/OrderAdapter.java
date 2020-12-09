package com.token.mangowallet.ui.adapter;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.OrderGoodsBean;
import com.token.mangowallet.utils.BalanceUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderGoodsBean.DataBean, BaseViewHolder> {

    boolean isBuyer;
    private OnOrderClickListener listener;

    public OrderAdapter(@Nullable List<OrderGoodsBean.DataBean> data, boolean isBuyer) {
        super(R.layout.item_order, data);
        this.isBuyer = isBuyer;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderGoodsBean.DataBean dataBean) {
        int mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        QMUIConstraintLayout itemGoods = baseViewHolder.getView(R.id.itemGoods);
        AppCompatTextView usernameTv = baseViewHolder.getView(R.id.usernameTv);
        AppCompatTextView statusTv = baseViewHolder.getView(R.id.statusTv);
        AppCompatImageView goodsPicIv = baseViewHolder.getView(R.id.goodsPicIv);
        AppCompatTextView goodsNameTv = baseViewHolder.getView(R.id.goodsNameTv);
        AppCompatTextView goodsSpecificationTv = baseViewHolder.getView(R.id.goodsSpecificationTv);
        AppCompatTextView goodsNumberTv = baseViewHolder.getView(R.id.goodsNumberTv);
        AppCompatTextView goodsPriceTv = baseViewHolder.getView(R.id.goodsPriceTv);
        QMUIRoundButton goodsBtn1 = baseViewHolder.getView(R.id.goodsBtn1);
        QMUIRoundButton goodsBtn2 = baseViewHolder.getView(R.id.goodsBtn2);

        itemGoods.setRadius(mRadius);
        OrderGoodsBean.DataBean.OrderBean orderBean = dataBean.getOrder();
        OrderGoodsBean.DataBean.ProBean proBean = dataBean.getPro();
//买家：返回的数据中order中的payStatus 0已支付1支付中2支付失败3退款中4已退款5退款失败 7待支付
//      如果返回的payStatus为0 则需要根据isDeliver显示内容 0 待发货 1发货中 2 已收货
//      返回order中的 refund为0时不允许退款
//卖家：返回字段payStatus
//      0已支付1支付中2支付失败3退款中4已退款5退款失败6取消订单
//      当pay_status = 0时显示  isDeliver
//      0 待发货 1发货中 2已收货~~~~
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
            } else {
                if (orderBean.getPay() != 1) {
                    goodsBtnText = getContext().getString(R.string.str_collection_confirmation);
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
        usernameTv.setText(dataBean.getUsername());
        statusTv.setText(payStatus);
        if (proBean.getImage_url() != null) {
            Glide.with(goodsPicIv)
                    .load(proBean.getImage_url().get(0))
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(goodsPicIv);
        }
        goodsNameTv.setText(proBean.getStoreName());
        goodsSpecificationTv.setText(getContext().getString(R.string.str_goods_specification) + proBean.getStoreType());
        goodsNumberTv.setText(getContext().getString(R.string.str_goods_quantity) + orderBean.getTotalNum());
        goodsPriceTv.setText(getContext().getString(R.string.str_price).replace("$", "") +
                BalanceUtils.currencyToBase(ObjectUtils.isEmpty(orderBean.getProductPrice()) ? "0" : orderBean.getProductPrice(),
                        2, RoundingMode.FLOOR));

        if (ObjectUtils.isEmpty(goodsBtnText)) {
            goodsBtn1.setVisibility(View.GONE);
        } else {
            goodsBtn1.setVisibility(View.VISIBLE);
            goodsBtn1.setText(goodsBtnText);
            if (ObjectUtils.equals(getContext().getString(R.string.str_refund), goodsBtnText)) {
                goodsBtn1.setStrokeColors(ContextCompat.getColorStateList(getContext(), R.color.app_color_common_black));
                goodsBtn1.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_common_black));
            } else if (ObjectUtils.equals(getContext().getString(R.string.str_sure_goods), goodsBtnText)
                    || ObjectUtils.equals(getContext().getString(R.string.str_shipments), goodsBtnText)
                    || ObjectUtils.equals(getContext().getString(R.string.str_collection_confirmation), goodsBtnText)) {
                goodsBtn1.setStrokeColors(ContextCompat.getColorStateList(getContext(), R.color.qmui_config_color_red));
                goodsBtn1.setTextColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
            } else if (ObjectUtils.equals(getContext().getString(R.string.str_go_payment), goodsBtnText)) {
                goodsBtn1.setStrokeColors(ContextCompat.getColorStateList(getContext(), R.color.blueColor));
                goodsBtn1.setTextColor(ContextCompat.getColor(getContext(), R.color.blueColor));
            }
        }
        if (ObjectUtils.equals(getContext().getString(R.string.str_go_payment), goodsBtnText)) {
            goodsBtn2.setVisibility(View.VISIBLE);
        } else {
            goodsBtn2.setVisibility(View.GONE);
        }
        goodsBtn1.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onOrder((QMUIRoundButton) v, baseViewHolder.getLayoutPosition());
                }
            }
        });
        goodsBtn2.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onOrder((QMUIRoundButton) v, baseViewHolder.getLayoutPosition());
                }
            }
        });

    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public interface OnOrderClickListener {
        void onOrder(QMUIRoundButton view, int position);
    }
}
