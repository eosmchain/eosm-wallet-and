package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.ui.fragment.mgp_deal.MyOrderFragment;
import com.token.mangowallet.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArbitrationOrderAdapter extends BaseQuickAdapter<DealsOrderBean.RowsBean, BaseViewHolder> {

    public ArbitrationOrderAdapter(@Nullable List<DealsOrderBean.RowsBean> data) {
        super(R.layout.item_my_order, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DealsOrderBean.RowsBean dealRowsBean) {
        AppCompatTextView totalSaleTv = baseViewHolder.getView(R.id.totalSaleTv);
        AppCompatTextView orderStatusTv = baseViewHolder.getView(R.id.orderStatusTv);
        AppCompatTextView mgpnumTv = baseViewHolder.getView(R.id.mgpnumTv);
        AppCompatTextView transactionAmountTv = baseViewHolder.getView(R.id.transactionAmountTv);
        AppCompatTextView timeValTv = baseViewHolder.getView(R.id.timeValTv);
        AppCompatTextView mgpnumValTv = baseViewHolder.getView(R.id.mgpnumValTv);
        AppCompatTextView transactionAmountValTv = baseViewHolder.getView(R.id.transactionAmountValTv);
        ConstraintLayout itemView = baseViewHolder.getView(R.id.item_view);

        String totalSaleStr = "";
        String orderStatusStr = "";
        String mgpnumTitle = "";
        String timeVal = "";
        String mgpnumVal = "";
        String transactionAmountVal = "";
        int maker_passed = 0;//商家等于1表示通过，反正没通过
        int taker_passed = 0;//买家等于1表示通过，反正没通过
        int arbiter_passed = 0;//客服等于1表示通过，反正没通过
        int orderStatus = 0;//订单状态：0:代付款;1:待放行;2:交易完成;3:交易失败;4:支付超时; 10:出售中；11:已完成；12：已撤销;

        if (dealRowsBean != null) {
            totalSaleStr = dealRowsBean.getOrder_sn();
            mgpnumTitle = getContext().getString(R.string.str_sell) + "(MGP)";
            timeVal = dealRowsBean.getCreated_at();
            mgpnumVal = dealRowsBean.getDeal_quantity();

            maker_passed = ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getMaker_passed_at()) ? 0 : 1;
            taker_passed = ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getTaker_passed_at()) ? 0 : 1;
            arbiter_passed = ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getArbiter_passed_at()) ? 0 : 1;
            String order_price = (ObjectUtils.isEmpty(dealRowsBean.getOrder_price()) ? "0.00 CNY" : dealRowsBean.getOrder_price()).split(" ")[0];
            String deal_quantity = (ObjectUtils.isEmpty(dealRowsBean.getDeal_quantity()) ? "0.0000 MGP" : dealRowsBean.getDeal_quantity()).split(" ")[0];
            transactionAmountVal = new BigDecimal(order_price).multiply(new BigDecimal(deal_quantity)).setScale(2, RoundingMode.FLOOR).toPlainString();

            //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
            if (dealRowsBean.getTaker_passed() == 0 && taker_passed == 0 && dealRowsBean.getClosed() == 0) {
                //代付款 taker_passed = 0， taker_passed_at= 1970-01-01T00:00:00", close = 0
                long mistiming = TimeUtils.getSurplusMillisTime(dealRowsBean.getExpired_at());//expiration_at
                if (mistiming > 0) {
                    orderStatus = 0;
                    orderStatusStr = getContext().getString(R.string.str_obligation);
                } else {
                    orderStatus = 1;
                    orderStatusStr = getContext().getString(R.string.str_timeout_cancel);
                }
            } else if (dealRowsBean.getTaker_passed() == 1
                    && !ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getTaker_passed_at())
                    && dealRowsBean.getClosed() == 0) {
                //待放行 taker_passed = 1， taker_passed_at  != 1970-01-01T00:00:00 ,close = 0
                long mistiming = TimeUtils.getSurplusMillisTime(dealRowsBean.getMaker_expired_at());//maker_expiration_at
                if (mistiming > 0) {
                    orderStatus = 2;
                    orderStatusStr = getContext().getString(R.string.str_be_released);
                } else {
                    orderStatus = 3;
                    orderStatusStr = getContext().getString(R.string.str_timeout_pass);
                }
            } else if ((maker_passed + taker_passed + arbiter_passed) > 1
                    && (dealRowsBean.getTaker_passed() + dealRowsBean.getMaker_passed() + dealRowsBean.getArbiter_passed()) > 1
                    && dealRowsBean.getClosed() == 1) {
                //maker_passed_at 商家通过时间
                //taker_passed_at 买家通过时间
                //arbiter_passed_at 客服通过时间
                // 交易完成  三者和时间通过三个有两个及以上!= 1970-01-01T00:00:00，close = 1
                orderStatus = 4;
                orderStatusStr = getContext().getString(R.string.str_complete_transaction);
            } else if (dealRowsBean.getClosed() == 1) {
                //支付超时 close = 1
                orderStatus = 5;
                orderStatusStr = getContext().getString(R.string.str_transaction_cancelled);
            }
            //            else if ((maker_passed + taker_passed + arbiter_passed) > 1 && dealRowsBean.getClosed() == 0) {
//                //交易失败 时间上三个有两个及以上!= 1970-01-01T00:00:00~~~~ 三者有两个及以上没有通过,close = 0
//                orderStatus = 3;
//                orderStatusStr = getContext().getString(R.string.str_transaction_fail);
//            }
        }

        totalSaleTv.setText(totalSaleStr);
        orderStatusTv.setText(orderStatusStr);
        timeValTv.setText(TimeUtils.getStringTime2(timeVal));
        mgpnumTv.setText(mgpnumTitle);
        mgpnumValTv.setText(mgpnumVal);
        transactionAmountTv.setText(getContext().getString(R.string.str_amount_trade) + "(CYN)");
        transactionAmountValTv.setText(transactionAmountVal);
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("transactionAmountVal", transactionAmountVal);
//        dataMap.put("mgpnumVal", mgpnumVal);
//        dataMap.put("orderStatus", orderStatus);
        itemView.setTag(R.id.orderStatus, orderStatus);
        itemView.setTag(R.id.transactionAmountVal, transactionAmountVal);
    }

//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(Map<String, Object> dataMap, int position);
//    }
}
