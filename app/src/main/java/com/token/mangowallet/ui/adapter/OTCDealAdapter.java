package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.JsonObject;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.OrderGoodsBean;
import com.token.mangowallet.bean.PayInfoUserInfoBean;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.ui.fragment.mgp_deal.OTCDealFragment;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.NRSAUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class OTCDealAdapter extends BaseQuickAdapter<SelordersBean.RowsBean, BaseViewHolder> {
    private OTCDealFragment otcDealFragment;
    public HashMap<Integer, PayInfoUserInfoBean.DataBean> mUserInfoHashMap = new HashMap<>();

    public OTCDealAdapter(OTCDealFragment otcDealFragment, @Nullable List<SelordersBean.RowsBean> data) {
        super(R.layout.item_mgp_deal, data);
        this.otcDealFragment = otcDealFragment;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SelordersBean.RowsBean rowsBean) {
//        getBusinessTableRows(rowsBean.getOwner(), baseViewHolder.getAdapterPosition());
        AppCompatImageView bankCardIv = baseViewHolder.getView(R.id.bankCardIv);
        AppCompatImageView alipayIv = baseViewHolder.getView(R.id.alipayIv);
        AppCompatImageView wechatIv = baseViewHolder.getView(R.id.wechatIv);
        try {
            Map params = MapUtils.newHashMap();
            params.put("mgpName", rowsBean.getOwner());
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().payInfo(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<JsonObject>(otcDealFragment.getActivity(), false) {
                        @Override
                        public void onFail(String failMsg) {
                            LogUtils.eTag(LOG_TAG, "failMsg = " + failMsg);
                        }

                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            if (ObjectUtils.isNotEmpty(jsonObject)) {
                                PayInfoUserInfoBean payInfoUserInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayInfoUserInfoBean.class);
                                if (payInfoUserInfoBean != null) {
                                    if (payInfoUserInfoBean.getCode() == 0) {
                                        PayInfoUserInfoBean.DataBean dataBean = payInfoUserInfoBean.getData();
                                        mUserInfoHashMap.put(baseViewHolder.getAdapterPosition(), dataBean);
                                        List<PayInfoUserInfoBean.DataBean.PayInfosBean> payInfosBeanList = dataBean.getPayInfos();
//                                        PayInfoUserInfoBean.DataBean.UserInfoBean userInfo = dataBean.getUserInfo();
                                        if (CollectionUtils.isNotEmpty(payInfosBeanList)) {
                                            for (int i = 0; i < payInfosBeanList.size(); i++) {
                                                PayInfoUserInfoBean.DataBean.PayInfosBean payInfosBean = payInfosBeanList.get(i);
                                                if (payInfosBean.getPayId() == 1) {
                                                    bankCardIv.setVisibility(View.VISIBLE);
                                                } else if (payInfosBean.getPayId() == 2) {
                                                    wechatIv.setVisibility(View.VISIBLE);
                                                } else if (payInfosBean.getPayId() == 3) {
                                                    alipayIv.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        baseViewHolder.setText(R.id.priceTv, "￥" + price.setScale(2, RoundingMode.FLOOR).toPlainString());
        //BalanceUtils.currencyToBase(price.toPlainString(), 2, RoundingMode.FLOOR));
        baseViewHolder.setText(R.id.quantityValueTv, APPUtils.dataFormat(ObjectUtils.isEmpty(remaining_quantity) ? "0" : remaining_quantity.toPlainString()) + " " + MGP_SYMBOL);
//        baseViewHolder.setText(R.id.quotaValueTv, BalanceUtils.currencyToBase(min_accept_quantity.toPlainString(), 2, RoundingMode.FLOOR)
//                + "-" + BalanceUtils.currencyToBase(max_accept_quantity.toPlainString(), 2, RoundingMode.FLOOR));
        baseViewHolder.setText(R.id.quotaValueTv, "￥" + min_accept_quantity.setScale(2, RoundingMode.FLOOR).toPlainString()
                + "-" + "￥" + max_accept_quantity.setScale(2, RoundingMode.FLOOR).toPlainString());
    }
}
