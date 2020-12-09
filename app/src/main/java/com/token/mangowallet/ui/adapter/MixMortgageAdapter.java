package com.token.mangowallet.ui.adapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.AppStoreLifeBean;
import com.token.mangowallet.bean.MixMortgageBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MixMortgageAdapter extends BaseQuickAdapter<MixMortgageBean.DataBean, BaseViewHolder> {

    public MixMortgageAdapter(@Nullable List<MixMortgageBean.DataBean> data) {
        super(R.layout.item_mix_mortgage, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MixMortgageBean.DataBean dataBean) {
        QMUIRadiusImageView tokenIv = baseViewHolder.getView(R.id.tokenIv);
        AppCompatTextView mixMortgageStatusTv = baseViewHolder.getView(R.id.mixMortgageStatusTv);
        AppCompatTextView numTv = baseViewHolder.getView(R.id.numTv);
        int mipmap;
        String status = "";
        if (dataBean.getMoneyType() == 4) {// 需支付币种 4 HMIO ，1 MGP
            mipmap = R.mipmap.ic_eth;
        } else {
            mipmap = R.mipmap.ic_mgp;
        }
        Glide.with(tokenIv).asDrawable().load(mipmap)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder)).into(tokenIv);
        if (dataBean.getStatus() == 1) {// 状态 1 已完成 2 待转hmio 3 待转MGP 4 MGP 扫描中 5 hmio扫描中 6 生成中 7 失败
            status = getContext().getString(R.string.str_completed);
        } else if (dataBean.getStatus() == 2) {
            status = getContext().getString(R.string.str_toturn) + "hmio";
        } else if (dataBean.getStatus() == 3) {
            status = getContext().getString(R.string.str_toturn) + "MGP";
        } else if (dataBean.getStatus() == 4) {
            status = "MGP" + getContext().getString(R.string.str_Idle);
        } else if (dataBean.getStatus() == 5) {
            status = "hmio" + getContext().getString(R.string.str_Idle);
        } else if (dataBean.getStatus() == 6) {
            status = getContext().getString(R.string.str_generated);
        } else if (dataBean.getStatus() == 7) {
            status = getContext().getString(R.string.str_fail);
        }
        mixMortgageStatusTv.setText(status);
        numTv.setText(dataBean.getNum().toPlainString() + dataBean.getPayMoneyType());
    }
}
