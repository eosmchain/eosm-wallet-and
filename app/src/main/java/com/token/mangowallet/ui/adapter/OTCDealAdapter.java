package com.token.mangowallet.ui.adapter;

import android.view.View;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.OrderGoodsBean;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.ui.fragment.mgp_deal.OTCDealFragment;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.BalanceUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.token.mangowallet.utils.Constants.DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class OTCDealAdapter extends BaseQuickAdapter<SelordersBean.RowsBean, BaseViewHolder> {
    private OTCDealFragment otcDealFragment;

    public OTCDealAdapter(OTCDealFragment otcDealFragment, @Nullable List<SelordersBean.RowsBean> data) {
        super(R.layout.item_mgp_deal, data);
        this.otcDealFragment = otcDealFragment;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SelordersBean.RowsBean rowsBean) {
//        getBusinessTableRows(rowsBean.getOwner());
        /**
         *   "id": 0, // 订单ID
         *       "owner": "mgptest11111", // 挂售人
         *       "price": "1000.00 CNY", // 价格
         *       "quantity": "1000.0000 MGP", // 总数
         *       "min_accept_quantity": "10.00 CNY", // 总价
         *       "frozen_quantity": "0 ", // 冻结币
         *       "fufilled_quantity": "0 ", // 交易完成数量
         *       "closed": 1, // 是否关闭
         *       "created_at": "2020-12-31T03:05:50",
         *       "closed_at": "2020-12-31T03:06:32"
         */
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal frozen_quantity = BigDecimal.ZERO;
        BigDecimal fufilled_quantity = BigDecimal.ZERO;
        BigDecimal min_accept_quantity = BigDecimal.ZERO;
        if (ObjectUtils.isNotEmpty(rowsBean.getPrice())) { // 价格
            String priceStr = rowsBean.getPrice();
            priceStr = priceStr.split(" ")[0];
            price = new BigDecimal(ObjectUtils.isEmpty(priceStr) ? "0" : priceStr);
        }
        if (ObjectUtils.isNotEmpty(rowsBean.getQuantity())) {// 总数
            String quantityStr = rowsBean.getQuantity();
            quantityStr = quantityStr.split(" ")[0];
            quantity = new BigDecimal(ObjectUtils.isEmpty(quantityStr) ? "0" : quantityStr);
        }
        if (ObjectUtils.isNotEmpty(rowsBean.getFrozen_quantity())) {// 冻结币
            String frozen_quantityStr = rowsBean.getFrozen_quantity();
            frozen_quantityStr = frozen_quantityStr.split(" ")[0];
            frozen_quantity = new BigDecimal(ObjectUtils.isEmpty(frozen_quantityStr) ? "0" : frozen_quantityStr);
        }
        if (ObjectUtils.isNotEmpty(rowsBean.getFufilled_quantity())) {//交易完成数量
            String fufilled_quantityStr = rowsBean.getFufilled_quantity();
            fufilled_quantityStr = fufilled_quantityStr.split(" ")[0];
            fufilled_quantity = new BigDecimal(ObjectUtils.isEmpty(fufilled_quantityStr) ? "0" : fufilled_quantityStr);
        }
        if (ObjectUtils.isNotEmpty(rowsBean.getMin_accept_quantity())) {
            String min_accept_quantityStr = rowsBean.getMin_accept_quantity();
            min_accept_quantityStr = min_accept_quantityStr.split(" ")[0];
            min_accept_quantity = new BigDecimal(ObjectUtils.isEmpty(min_accept_quantityStr) ? "0" : min_accept_quantityStr);
        }
        BigDecimal remaining_quantity = BigDecimal.ZERO;// 剩余数量
        remaining_quantity = quantity.subtract(frozen_quantity).subtract(fufilled_quantity);
        BigDecimal max_accept_quantity = remaining_quantity.multiply(price);

        baseViewHolder.setText(R.id.accountNameTv, ObjectUtils.isEmpty(rowsBean.getOwner()) ? "" : rowsBean.getOwner());
        baseViewHolder.setText(R.id.priceTv, BalanceUtils.currencyToBase(price.toPlainString(), 2, RoundingMode.FLOOR));
        baseViewHolder.setText(R.id.quantityValueTv, APPUtils.dataFormat(ObjectUtils.isEmpty(remaining_quantity) ? "0" : remaining_quantity.toPlainString()) + " " + MGP_SYMBOL);
        baseViewHolder.setText(R.id.quotaValueTv, BalanceUtils.currencyToBase(min_accept_quantity.toPlainString(), 2, RoundingMode.FLOOR)
                + "-" + BalanceUtils.currencyToBase(max_accept_quantity.toPlainString(), 2, RoundingMode.FLOOR));


    }

    /**
     * 商家信息
     */
    private void getBusinessTableRows(String owner) {
        try {
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("table", "sellers");
            mapTableRows.put("scope", DEAL_CONTRACT);
            mapTableRows.put("code", DEAL_CONTRACT);
            mapTableRows.put("lower_bound", owner);
            mapTableRows.put("upper_bound", owner);
            mapTableRows.put("json", true);
            mapTableRows.put("limit", "500");
            otcDealFragment.emWalletRepository.fetchTableRowsStr(mapTableRows, otcDealFragment.walletType)
                    .subscribe(this::onOrdersSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onOrdersSuccess(Object o) {
        if (ObjectUtils.isNotEmpty(o)) {

        }
    }

    private void onError(Object e) {
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }
}
