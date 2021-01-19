package com.token.mangowallet.ui.adapter;

import android.os.Build;
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
import com.token.mangowallet.R;
import com.token.mangowallet.bean.ThemesBean;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

import static com.token.mangowallet.utils.Constants.percent;

public class VoteMainAdapter extends BaseQuickAdapter<ThemesBean.DataBean, BaseViewHolder> {

    private boolean isMyScheme = false;

    public VoteMainAdapter(List<ThemesBean.DataBean> themesList, boolean isMyScheme) {
        super(R.layout.item_vote_main, themesList);
        this.isMyScheme = isMyScheme;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ThemesBean.DataBean token) {
        AppCompatTextView rankingTv = baseViewHolder.getView(R.id.rankingTv);
        AppCompatTextView voteTitleTv = baseViewHolder.getView(R.id.voteTitleTv);
        AppCompatTextView voteSchemeTv = baseViewHolder.getView(R.id.voteSchemeTv);
        AppCompatTextView voteBtn = baseViewHolder.getView(R.id.voteBtn);
        AppCompatTextView rateTv = baseViewHolder.getView(R.id.rateTv);
        ContentLoadingProgressBar rectProgressBar = baseViewHolder.getView(R.id.rectProgressBar);

        BigDecimal mRate = BigDecimal.ZERO;//.setScale(2).toPlainString()
        int mSort = 1;
        int mTextColor = R.color.app_color_common_deputy;
        int mTextSize = 24;
        String statusStr = "";
        if (ObjectUtils.isNotEmpty(token)) {
            voteTitleTv.setText(ObjectUtils.isNotEmpty(token.getVoteTitle()) ? token.getVoteTitle() : "");
            voteSchemeTv.setText(ObjectUtils.isNotEmpty(token.getVoteContent()) ? token.getVoteContent() : "");
            mRate = ObjectUtils.isNotEmpty(token.getRate()) ? (token.getRate().multiply(percent)) : BigDecimal.ZERO;
            mSort = ObjectUtils.isNotEmpty(token.getSort()) ? token.getSort() : 1;
            rectProgressBar.setProgress(mRate.intValue(), true);
            rateTv.setText(mRate.setScale(2).toPlainString() + "%");
            if (isMyScheme) {
                //type 0待支付 1待审核 2审核失败展示mark 3投票中 4投票结束 5待投票
                //6支付中
                if (token.getType() == 0) {
                    statusStr = getContext().getString(R.string.str_unpaid);
                } else if (token.getType() == 1) {
                    statusStr = getContext().getString(R.string.str_check_pending);
                } else if (token.getType() == 2) {
                    statusStr = getContext().getString(R.string.str_audit_failure);
                    voteSchemeTv.setText(getContext().getString(R.string.str_check_pending));
                } else if (token.getType() == 3) {
                    statusStr = getContext().getString(R.string.str_voteing);
                } else if (token.getType() == 4) {
                    statusStr = getContext().getString(R.string.str_voteing_over);
                } else if (token.getType() == 5) {
                    statusStr = getContext().getString(R.string.str_be_over);
                } else if (token.getType() == 6) {
                    statusStr = getContext().getString(R.string.str_paymenting);
                }
            } else {
                if (token.getType() == 3) {
                    statusStr = getContext().getString(R.string.str_vote_t);
                } else if (token.getType() == 4) {
                    statusStr = getContext().getString(R.string.str_voteing_over);
                } else if (token.getType() == 5) {
                    statusStr = getContext().getString(R.string.str_be_over);
                } else {
                    statusStr = getContext().getString(R.string.str_vote_t);//R.string.str_vote_t
                }
            }
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
        rectProgressBar.setProgress(mRate.intValue(), true);
        rateTv.setText(mRate + "%");
    }
}
