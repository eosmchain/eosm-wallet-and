package com.token.mangowallet.ui.adapter;

import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.PayInfoBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PayInfoAdapter extends BaseQuickAdapter<PayInfoBean.DataBean, BaseViewHolder> {


    public PayInfoAdapter(@Nullable List<PayInfoBean.DataBean> data) {
        super(R.layout.item_payment_term, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PayInfoBean.DataBean dataBean) {
        AppCompatTextView paymentNameTv = baseViewHolder.getView(R.id.paymentNameTv);
        int mipRes = 0;
        boolean isBank = false;
        /**
         * payId = 1银行；2微信；3支付宝；
         */
        if (dataBean.getPayId() == 1) {
            mipRes = R.mipmap.ic_bank_card;
            isBank = true;
        } else if (dataBean.getPayId() == 2) {
            mipRes = R.mipmap.ic_wechat;
            isBank = false;
        } else if (dataBean.getPayId() == 3) {
            mipRes = R.mipmap.ic_alipay;
            isBank = false;
        } else if (dataBean.getPayId() == 4) {
            mipRes = R.mipmap.ic_usdt_20;
            isBank = true;
        } else if (dataBean.getPayId() == 5) {
            mipRes = R.mipmap.ic_usdt_20;
            isBank = true;
        }
        baseViewHolder.setGone(R.id.qrcodeIv, isBank);
        if (mipRes != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), mipRes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            paymentNameTv.setCompoundDrawables(drawable, null, null, null);
        }
        paymentNameTv.setText(ObjectUtils.isEmpty(dataBean.getName()) ? "" : dataBean.getName());
        baseViewHolder.setText(R.id.usernameTv, ObjectUtils.isEmpty(dataBean.getUsername()) ? "" : dataBean.getUsername());
        baseViewHolder.setText(R.id.accountTv, ObjectUtils.isEmpty(dataBean.getCardNum()) ? "" : dataBean.getCardNum());
    }
}
