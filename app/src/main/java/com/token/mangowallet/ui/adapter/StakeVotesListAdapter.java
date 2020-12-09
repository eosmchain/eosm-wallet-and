package com.token.mangowallet.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.VoteListBean;
import com.token.mangowallet.bean.VotesBean;
import com.token.mangowallet.ui.fragment.StakeVoteMainFragment;
import com.token.mangowallet.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public class StakeVotesListAdapter extends BaseQuickAdapter<VoteListBean.RowsBean, BaseViewHolder> {


    public StakeVotesListAdapter(@Nullable List<VoteListBean.RowsBean> data) {
        super(R.layout.item_main_stake_vote, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VoteListBean.RowsBean rowsBean) {
        QMUIConstraintLayout itemRoot = baseViewHolder.getView(R.id.itemRoot);
        QMUIRadiusImageView imageIv = baseViewHolder.getView(R.id.imageIv);
        AppCompatTextView rankingTv = baseViewHolder.getView(R.id.rankingTv);
        AppCompatTextView nodeNameTv = baseViewHolder.getView(R.id.nodeNameTv);
        AppCompatTextView profitRatioTv = baseViewHolder.getView(R.id.profitRatioTv);
        AppCompatTextView nodeURLTv = baseViewHolder.getView(R.id.nodeURLTv);
        AppCompatTextView voteNumTv = baseViewHolder.getView(R.id.voteNumTv);
        QMUIRoundButton voteBtn = baseViewHolder.getView(R.id.voteBtn);
        itemRoot.setRadius(QMUIDisplayHelper.dp2px(getContext(), 12));

        Glide.with(imageIv).load(R.mipmap.ic_mgp).into(imageIv);
        imageIv.setVisibility(View.VISIBLE);
        rankingTv.setVisibility(View.GONE);
        profitRatioTv.setVisibility(View.GONE);
        voteNumTv.setVisibility(View.GONE);
        voteBtn.setVisibility(View.GONE);

        nodeNameTv.setText(ObjectUtils.isEmpty(rowsBean.getOwner()) ? "" : rowsBean.getOwner());
        nodeURLTv.setText(ObjectUtils.isEmpty(rowsBean.getQuantity()) ? "0.0000 MGP" : rowsBean.getQuantity());
    }
}
