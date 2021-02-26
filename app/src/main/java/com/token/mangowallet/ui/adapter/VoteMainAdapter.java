package com.token.mangowallet.ui.adapter;

import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUISlider;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.SchemesThemesBean;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.ui.fragment.MyVoteMainFragment;
import com.token.mangowallet.ui.fragment.VoteMainFragment;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.token.mangowallet.utils.Constants.percent;

public class VoteMainAdapter extends BaseQuickAdapter<SchemesThemesBean.RowsBean, BaseViewHolder> {

    private boolean isMyScheme = false;

    public VoteMainAdapter(List<SchemesThemesBean.RowsBean> themesList, boolean isMyScheme) {
        super(R.layout.item_vote_main, themesList);
        this.isMyScheme = isMyScheme;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SchemesThemesBean.RowsBean token) {
        AppCompatTextView rankingTv = baseViewHolder.getView(R.id.rankingTv);
        AppCompatTextView voteTitleTv = baseViewHolder.getView(R.id.voteTitleTv);
        AppCompatTextView voteSchemeTv = baseViewHolder.getView(R.id.voteSchemeTv);
        AppCompatTextView voteBtn = baseViewHolder.getView(R.id.voteBtn);
        AppCompatTextView rateTv = baseViewHolder.getView(R.id.rateTv);
        QMUISlider rectProgressBar = baseViewHolder.getView(R.id.rectProgressBar);

        BigDecimal mRate = BigDecimal.ZERO;//.setScale(2).toPlainString()
        int mSort = 1;
        int mTextColor = R.color.app_color_common_deputy;
        int mTextSize = 24;
        String statusStr = "";
        if (ObjectUtils.isNotEmpty(token)) {
            voteTitleTv.setText(ObjectUtils.isNotEmpty(token.getScheme_title()) ? token.getScheme_title() : "");
            voteSchemeTv.setText(ObjectUtils.isNotEmpty(token.getScheme_content()) ? token.getScheme_content() : "");
            String vote_count = ObjectUtils.isNotEmpty(token.getVote_count()) ? token.getVote_count() : "0.0000 MGP";
            String mVoteCount = vote_count.split(" ")[0];
            BigDecimal mbdVoteCount = new BigDecimal(mVoteCount);
            //-1表示小于，0是等于，1是大于。
            mRate = VoteMainFragment.mbdTotalVoteCount.compareTo(BigDecimal.ZERO) > 0 ? mbdVoteCount.divide(VoteMainFragment.mbdTotalVoteCount, 6, RoundingMode.FLOOR).multiply(percent).setScale(2, RoundingMode.FLOOR) : BigDecimal.ZERO;
            mSort = baseViewHolder.getBindingAdapterPosition() + 1;
            rectProgressBar.setCurrentProgress(mRate.intValue());
            rateTv.setText(mRate.setScale(2).toPlainString() + "%");
//            if (isMyScheme) {
//                //type 0待支付 1待审核 2审核失败展示mark 3投票中 4投票结束 5待投票
//                //6支付中
//                if (token.getType() == 0) {
//                    statusStr = getContext().getString(R.string.str_unpaid);
//                } else if (token.getType() == 1) {
//                    statusStr = getContext().getString(R.string.str_check_pending);
//                } else if (token.getType() == 2) {
//                    statusStr = getContext().getString(R.string.str_audit_failure);
//                    voteSchemeTv.setText(getContext().getString(R.string.str_check_pending));
//                } else if (token.getType() == 3) {
//                    statusStr = getContext().getString(R.string.str_voteing);
//                } else if (token.getType() == 4) {
//                    statusStr = getContext().getString(R.string.str_voteing_over);
//                } else if (token.getType() == 5) {
//                    statusStr = getContext().getString(R.string.str_be_over);
//                } else if (token.getType() == 6) {
//                    statusStr = getContext().getString(R.string.str_paymenting);
//                }
//            } else {
//                if (token.getType() == 3) {
//                    statusStr = getContext().getString(R.string.str_vote_t);
//                } else if (token.getType() == 4) {
//                    statusStr = getContext().getString(R.string.str_voteing_over);
//                } else if (token.getType() == 5) {
//                    statusStr = getContext().getString(R.string.str_be_over);
//                } else {
//                    statusStr = getContext().getString(R.string.str_vote_t);//R.string.str_vote_t
//                }
//            }
//            @[@"审核中",[NSString stringWithFormat:@"总票%.f",vote_count],@"审核失败"]

            int statusColor = R.color.app_color_orange;
            if (isMyScheme) {
                if (MyVoteMainFragment.mIndex == 0) {
                    rectProgressBar.setVisibility(View.INVISIBLE);
                    rateTv.setVisibility(View.INVISIBLE);
                    statusStr = String.format(getContext().getString(R.string.str_been_voted), String.valueOf(mbdVoteCount.intValue()));
                } else {
                    if (token.getAudit_status() == 0) {
                        statusStr = getContext().getString(R.string.str_inreview);
                        statusColor = R.color.app_color_common_deputy;
                    } else if (token.getAudit_status() == 1) {
                        statusStr = getContext().getString(R.string.str_total_votes) + String.valueOf(mbdVoteCount.intValue());
                        statusColor = R.color.app_color_orange;
                    } else if (token.getAudit_status() == 2) {
                        statusStr = getContext().getString(R.string.str_audit_failure);
                        statusColor = R.color.qmui_config_color_red;
                    }
                    rectProgressBar.setVisibility(View.VISIBLE);
                    rateTv.setVisibility(View.VISIBLE);

                }
                rankingTv.setVisibility(View.GONE);
            } else {
                if (VoteMainFragment.rowsConfigBean.getScheme() == 1 && VoteMainFragment.rowsConfigBean.getVote() == 0) {
                    statusStr = getContext().getString(R.string.str_be_over);
                } else if (VoteMainFragment.rowsConfigBean.getVote() == 1) {
                    statusStr = getContext().getString(R.string.str_vote_t);
                } else if (VoteMainFragment.rowsConfigBean.getScheme() == 0 && VoteMainFragment.rowsConfigBean.getVote() == 0) {
                    statusStr = getContext().getString(R.string.str_voteing_over);
                }
                rankingTv.setVisibility(View.VISIBLE);
                rectProgressBar.setVisibility(View.VISIBLE);
            }
            voteBtn.setTextColor(ContextCompat.getColor(getContext(), statusColor));
            voteBtn.setText(statusStr);
        } else {
            voteTitleTv.setText("");
            voteSchemeTv.setText("");
        }

        if (mSort == 1) {
            mTextColor = R.color.color_gold;
            mTextSize = QMUIDisplayHelper.px2sp(getContext(), 100);
        } else if (mSort == 2) {
            mTextColor = R.color.app_color_red;
            mTextSize = QMUIDisplayHelper.px2sp(getContext(), 100);
        } else if (mSort == 3) {
            mTextColor = R.color.color_coppery;
            mTextSize = QMUIDisplayHelper.px2sp(getContext(), 100);
        } else {
            mTextColor = R.color.app_color_common_deputy;
            mTextSize = QMUIDisplayHelper.px2sp(getContext(), 50);
        }
        rankingTv.setTextColor(ContextCompat.getColor(getContext(), mTextColor));
        rankingTv.setTextSize(mTextSize);
        rankingTv.setText(mSort + "");
        rectProgressBar.setCurrentProgress(mRate.intValue());
        rateTv.setText(mRate + "%");
    }
}
