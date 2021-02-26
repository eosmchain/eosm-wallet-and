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

public class VoteMainRecordAdapter extends BaseQuickAdapter<VoteLogBean.RowsBean, BaseViewHolder> {


    public VoteMainRecordAdapter(List<VoteLogBean.RowsBean> themesList) {
        super(R.layout.item_mix_mortgage, themesList);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VoteLogBean.RowsBean token) {
        AppCompatTextView numTv = baseViewHolder.getView(R.id.numTv);
        AppCompatTextView mixMortgageTv = baseViewHolder.getView(R.id.mixMortgageTv);
        AppCompatTextView mixMortgageStatusTv = baseViewHolder.getView(R.id.mixMortgageStatusTv);
        AppCompatImageView arrowsIv = baseViewHolder.getView(R.id.arrowsIv);
        arrowsIv.setVisibility(View.INVISIBLE);
        String channelName = "";
        if (ObjectUtils.isNotEmpty(token)) {
            if (token.getAward_type() == 0) {
                channelName = getContext().getString(R.string.str_deposit_return);
            } else if (token.getAward_type() == 1) {
                channelName = getContext().getString(R.string.str_vote_return);
            } else if (token.getAward_type() == 2) {
                channelName = getContext().getString(R.string.str_scheme_award);
            } else if (token.getAward_type() == 3) {
                channelName = getContext().getString(R.string.str_vote_award);
            }
            mixMortgageTv.setText(ObjectUtils.isNotEmpty(channelName) ? channelName : "");
            mixMortgageStatusTv.setText(ObjectUtils.isNotEmpty(token.getCreated_at()) ? token.getCreated_at() : "");
            numTv.setText((ObjectUtils.isNotEmpty(token.getMoney()) ? token.getMoney() : "0 MGP"));
        } else {
            mixMortgageTv.setText("");
            mixMortgageStatusTv.setText("");
            numTv.setText("0 " + MGP_SYMBOL);
        }
    }
}
