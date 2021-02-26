package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

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

public class MyOrderAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private MyOrderFragment fragment;
    private OnItemClickListener listener;
    private List<Map<String, Object>> dataList = new ArrayList<>();

    public MyOrderAdapter(MyOrderFragment fragment, @Nullable List<Object> data) {
        super(R.layout.item_my_order, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {
        AppCompatTextView totalSaleTv = baseViewHolder.getView(R.id.totalSaleTv);
        AppCompatTextView orderStatusTv = baseViewHolder.getView(R.id.orderStatusTv);
        AppCompatTextView mgpnumTv = baseViewHolder.getView(R.id.mgpnumTv);
        AppCompatTextView transactionAmountTv = baseViewHolder.getView(R.id.transactionAmountTv);
        AppCompatTextView timeValTv = baseViewHolder.getView(R.id.timeValTv);
        AppCompatTextView mgpnumValTv = baseViewHolder.getView(R.id.mgpnumValTv);
        AppCompatTextView transactionAmountValTv = baseViewHolder.getView(R.id.transactionAmountValTv);

        DealsOrderBean.RowsBean dealRowsBean = null;
        String totalSaleStr = "";
        String orderStatusStr = "";
        String mgpnumTitle = "";
        String timeVal = "";
        String mgpnumVal = "";
        String transactionAmountVal = "";
        String unit = "CNY";
        int maker_passed = 0;//商家等于1表示通过，反正没通过
        int taker_passed = 0;//买家等于1表示通过，反正没通过
        int arbiter_passed = 0;//客服等于1表示通过，反正没通过
        int orderStatus = 0;//订单状态：0:代付款;1:待放行;2:交易完成;3:交易失败;4:支付超时; 10:出售中；11:已完成；12：已撤销;
        if (fragment.mCurIndex == 0) {
            dealRowsBean = (DealsOrderBean.RowsBean) o;
            totalSaleStr = getContext().getString(R.string.str_purchase) + dealRowsBean.getDeal_quantity();
            mgpnumTitle = getContext().getString(R.string.str_purchase) + "(MGP)";
            timeVal = dealRowsBean.getCreated_at();
            mgpnumVal = dealRowsBean.getDeal_quantity();
        } else if (fragment.mCurIndex == 1) {
            dealRowsBean = (DealsOrderBean.RowsBean) o;
            totalSaleStr = getContext().getString(R.string.str_sell) + dealRowsBean.getDeal_quantity();
            mgpnumTitle = getContext().getString(R.string.str_sell) + "(MGP)";
            timeVal = dealRowsBean.getCreated_at();
            mgpnumVal = dealRowsBean.getDeal_quantity();
        } else if (fragment.mCurIndex == 2) {
            SelordersBean.RowsBean entrustRowsBean = (SelordersBean.RowsBean) o;

            BigDecimal min_accept_quantity = BigDecimal.ZERO;
            BigDecimal min_mgp_num = BigDecimal.ZERO;
            BigDecimal price = BigDecimal.ZERO;
            BigDecimal quantity = BigDecimal.ZERO;
            BigDecimal frozen_quantity = BigDecimal.ZERO;
            BigDecimal fufilled_quantity = BigDecimal.ZERO;
            BigDecimal remaining_quantity = BigDecimal.ZERO;
            String priceStr = "";
            if (ObjectUtils.isNotEmpty(entrustRowsBean.getPrice())) { // 价格
                priceStr = ObjectUtils.isEmpty(entrustRowsBean.getPrice()) ? "0.00 CNY" : entrustRowsBean.getPrice();
                priceStr = priceStr.split(" ")[0];
                price = new BigDecimal(ObjectUtils.isEmpty(priceStr) ? "0" : priceStr);
            }
            if (ObjectUtils.isNotEmpty(entrustRowsBean.getMin_accept_quantity())) {
                String min_accept_quantityStr = entrustRowsBean.getMin_accept_quantity();
                min_accept_quantityStr = min_accept_quantityStr.split(" ")[0];
                min_accept_quantity = new BigDecimal(ObjectUtils.isEmpty(min_accept_quantityStr) ? "0" : min_accept_quantityStr);
            }
            if (ObjectUtils.isNotEmpty(entrustRowsBean.getQuantity())) {// 总数
                String quantityStr = entrustRowsBean.getQuantity();
                quantityStr = quantityStr.split(" ")[0];
                quantity = new BigDecimal(ObjectUtils.isEmpty(quantityStr) ? "0" : quantityStr);
            }
            if (ObjectUtils.isNotEmpty(entrustRowsBean.getFrozen_quantity())) {// 冻结币
                String frozen_quantityStr = entrustRowsBean.getFrozen_quantity();
                frozen_quantityStr = frozen_quantityStr.split(" ")[0];
                frozen_quantity = new BigDecimal(ObjectUtils.isEmpty(frozen_quantityStr) ? "0" : frozen_quantityStr);
            }
            if (ObjectUtils.isNotEmpty(entrustRowsBean.getFulfilled_quantity())) {//交易完成数量
                String fufilled_quantityStr = entrustRowsBean.getFulfilled_quantity();
                fufilled_quantityStr = fufilled_quantityStr.split(" ")[0];
                fufilled_quantity = new BigDecimal(ObjectUtils.isEmpty(fufilled_quantityStr) ? "0" : fufilled_quantityStr);
            }
            remaining_quantity = quantity.subtract(frozen_quantity).subtract(fufilled_quantity);

            totalSaleStr = getContext().getString(R.string.str_total_sale) + (ObjectUtils.isEmpty(entrustRowsBean.getQuantity()) ? "0.0000 MGP" : entrustRowsBean.getQuantity());
            //订单状态：0:代付款;1:待放行;2:交易完成;3:交易失败;4:支付超时;10:出售中;11:已完成;12:已撤销;
            if (remaining_quantity.compareTo(BigDecimal.ZERO) <= 0) {//-1表示小于，0是等于，1是大于。
                orderStatusStr = getContext().getString(R.string.str_completed);
                orderStatus = 11;
            } else {
                orderStatusStr = entrustRowsBean.getClosed() == 1 ? getContext().getString(R.string.str_undone) : getContext().getString(R.string.str_on_offer);
                orderStatus = entrustRowsBean.getClosed() == 1 ? 12 : 10;
            }
            mgpnumTitle = getContext().getString(R.string.str_residue) + "(MGP)";
            mgpnumVal = remaining_quantity.setScale(4).toPlainString() + " MGP";
            timeVal = entrustRowsBean.getCreated_at();
            transactionAmountVal = price.multiply(remaining_quantity).setScale(2, RoundingMode.FLOOR).toPlainString();
        }

        if ((fragment.mCurIndex == 0 || fragment.mCurIndex == 1) && dealRowsBean != null) {
            maker_passed = ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getMaker_passed_at()) ? 0 : 1;
            taker_passed = ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getTaker_passed_at()) ? 0 : 1;
            arbiter_passed = ObjectUtils.equals("1970-01-01T00:00:00", dealRowsBean.getArbiter_passed_at()) ? 0 : 1;
            String order_price = (ObjectUtils.isEmpty(dealRowsBean.getOrder_price()) ? "0.00 CNY" : dealRowsBean.getOrder_price()).split(" ")[0];
            BigDecimal orderPriceDecimal = new BigDecimal(order_price.split(" ")[0]);
            String deal_quantity = (ObjectUtils.isEmpty(dealRowsBean.getDeal_quantity()) ? "0.0000 MGP" : dealRowsBean.getDeal_quantity()).split(" ")[0];
            BigDecimal dealQuantityDecimal = new BigDecimal(deal_quantity.split(" ")[0]);
            BigDecimal totalPricesDecimal = dealQuantityDecimal.multiply(orderPriceDecimal);
            transactionAmountVal = totalPricesDecimal.setScale(2, RoundingMode.FLOOR).toPlainString();
            String order_price_usd = ObjectUtils.isEmpty(dealRowsBean.getOrder_price_usd()) ? "0.0000 USD" : dealRowsBean.getOrder_price_usd();
            BigDecimal orderPriceUsdDecimal = new BigDecimal(order_price_usd.split(" ")[0]);

            String orderCNYQuantity = totalPricesDecimal.setScale(2, RoundingMode.FLOOR).toPlainString();
            String orderUsdQuantity = dealQuantityDecimal.multiply(orderPriceUsdDecimal).setScale(4, RoundingMode.FLOOR).toPlainString();

            if (dealRowsBean.getPay_type() == 4 || dealRowsBean.getPay_type() == 5) {
                transactionAmountVal = orderUsdQuantity;
                unit = "USDT";
            } else {
                transactionAmountVal = orderCNYQuantity;
                unit = "CNY";
            }
            //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
            if (dealRowsBean.getTaker_passed() == 0 && taker_passed == 0 && dealRowsBean.getClosed() == 0) {
                //代付款 taker_passed = 0， taker_passed_at = "1970-01-01T00:00:00", close = 0
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
        transactionAmountTv.setText(getContext().getString(R.string.str_amount_trade) + "(" + unit + ")");
        transactionAmountValTv.setText(transactionAmountVal);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("transactionAmountVal", transactionAmountVal);
        dataMap.put("mgpnumVal", mgpnumVal);
        dataMap.put("orderStatus", orderStatus);
        dataList.add(dataMap);
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(dataList.get(baseViewHolder.getAbsoluteAdapterPosition()), baseViewHolder.getAbsoluteAdapterPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Map<String, Object> dataMap, int position);
    }
}
