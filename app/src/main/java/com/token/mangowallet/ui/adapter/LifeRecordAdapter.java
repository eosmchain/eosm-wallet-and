package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.MixMortgageBean;
import com.token.mangowallet.bean.OrderListBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class LifeRecordAdapter extends BaseQuickAdapter<OrderListBean.DataBean.ListBean, BaseViewHolder> {

    public LifeRecordAdapter(@Nullable List<OrderListBean.DataBean.ListBean> data) {
        super(R.layout.item_mix_mortgage, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderListBean.DataBean.ListBean dataBean) {
        QMUIRadiusImageView tokenIv = baseViewHolder.getView(R.id.tokenIv);
        AppCompatTextView mixMortgageTv = baseViewHolder.getView(R.id.mixMortgageTv);
        AppCompatTextView mixMortgageStatusTv = baseViewHolder.getView(R.id.mixMortgageStatusTv);
        AppCompatTextView numTv = baseViewHolder.getView(R.id.numTv);
        AppCompatImageView arrowsIv = baseViewHolder.getView(R.id.arrowsIv);

        mixMortgageTv.setText((ObjectUtils.isEmpty(dataBean.getOrderName()) ? "" : dataBean.getOrderName())
                + (ObjectUtils.isEmpty(dataBean.getMsg()) ? "" : "(" + dataBean.getMsg() + ")"));
        mixMortgageStatusTv.setText(ObjectUtils.isEmpty(dataBean.getCreateTime()) ? "" : dataBean.getCreateTime());
        numTv.setText(ObjectUtils.isEmpty(dataBean.getPayNum()) ? "0.00" + MGP_SYMBOL : dataBean.getPayNum() + MGP_SYMBOL);
        arrowsIv.setVisibility(View.GONE);
    }
}
