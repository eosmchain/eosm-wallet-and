package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.bean.VoteLogBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class VoteMainRecordAdapter extends BaseQuickAdapter<VoteLogBean.DataBean, BaseViewHolder> {


    public VoteMainRecordAdapter(List<VoteLogBean.DataBean> themesList) {
        super(R.layout.item_mix_mortgage, themesList);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VoteLogBean.DataBean token) {
        AppCompatTextView numTv = baseViewHolder.getView(R.id.numTv);
        AppCompatTextView mixMortgageTv = baseViewHolder.getView(R.id.mixMortgageTv);
        AppCompatTextView mixMortgageStatusTv = baseViewHolder.getView(R.id.mixMortgageStatusTv);
        AppCompatImageView arrowsIv = baseViewHolder.getView(R.id.arrowsIv);
        arrowsIv.setVisibility(View.INVISIBLE);
        if (ObjectUtils.isNotEmpty(token)) {
            mixMortgageTv.setText(ObjectUtils.isNotEmpty(token.getTypeName()) ? token.getTypeName() : "");
            mixMortgageStatusTv.setText(ObjectUtils.isNotEmpty(token.getCreateTime()) ? token.getCreateTime() : "");
            numTv.setText((ObjectUtils.isNotEmpty(token.getMoney()) ? token.getMoney() : "0") + MGP_SYMBOL);
        } else {
            mixMortgageTv.setText("");
            mixMortgageStatusTv.setText("");
            numTv.setText("0" + MGP_SYMBOL);
        }
    }
}
