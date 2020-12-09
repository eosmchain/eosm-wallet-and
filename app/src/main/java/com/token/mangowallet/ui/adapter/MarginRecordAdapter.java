package com.token.mangowallet.ui.adapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.MarginRecordBean;
import com.token.mangowallet.bean.MiningOrderIncomeItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class MarginRecordAdapter extends BaseQuickAdapter<MarginRecordBean.DataBean, BaseViewHolder> {

    public MarginRecordAdapter() {
        super(R.layout.item_miner_income);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MarginRecordBean.DataBean dataBean) {
        AppCompatImageView incomeTypeIv = baseViewHolder.getView(R.id.iv_income_type);
        AppCompatTextView incomeTypeTv = baseViewHolder.getView(R.id.tv_income_type);
        AppCompatTextView incomeTimeTv = baseViewHolder.getView(R.id.tv_income_time);
        AppCompatTextView incomeAmounTv = baseViewHolder.getView(R.id.tv_income_amount);

        Glide.with(incomeTypeIv).asDrawable().load(R.mipmap.ic_mgp).into(incomeTypeIv);
        incomeTypeTv.setText(ObjectUtils.isEmpty(dataBean.getMark()) ? "" : dataBean.getMark());
        incomeTimeTv.setText(ObjectUtils.isEmpty(dataBean.getCreateTime()) ? "" : dataBean.getCreateTime());
        incomeAmounTv.setText(ObjectUtils.isEmpty(dataBean.getMoney_str()) ? "0.00" : dataBean.getMoney_str() + MGP_SYMBOL);
    }
}
