package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.NodeBean;
import com.token.mangowallet.bean.VotesBean;
import com.token.mangowallet.bean.entity.NodeSection;
import com.token.mangowallet.ui.fragment.StakeVoteMainFragment;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.view.ViewUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public class StakeVotesMainAdapter extends BaseSectionQuickAdapter<NodeSection, BaseViewHolder> {

    private boolean isManage = false;
    private BigDecimal refund_delay_sec;
    private com.token.mangowallet.utils.TimeUtils timeUtils;
    private StakeVoteMainFragment baseFragment;

    public StakeVotesMainAdapter(StakeVoteMainFragment fragment, @Nullable List<NodeSection> data) {
        super(R.layout.item_titile, data);
        setNormalLayout(R.layout.item_main_stake_vote);
        this.baseFragment = fragment;
        timeUtils = new com.token.mangowallet.utils.TimeUtils();
        addChildClickViewIds(R.id.voteLayout);
    }

//    public StakeVotesMainAdapter(StakeVoteMainFragment baseFragment, @Nullable List<VotesBean.RowsBean> data) {
//        super(R.layout.item_main_stake_vote, data);
//        timeUtils = new com.token.mangowallet.utils.TimeUtils();
//        this.baseFragment = baseFragment;
//    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull NodeSection nodeSection) {
        AppCompatTextView titleTv = baseViewHolder.getView(R.id.titleTv);
        if (nodeSection.isHeader)
            if (nodeSection.getObject() instanceof String) {
                titleTv.setText((String) nodeSection.getObject());
            }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NodeSection nodeSection) {
        if (!nodeSection.isHeader) {
            NodeBean nodeBean = (NodeBean) nodeSection.getObject();
            QMUIConstraintLayout itemRoot = baseViewHolder.getView(R.id.itemRoot);
            AppCompatTextView rankingTv = baseViewHolder.getView(R.id.rankingTv);
            AppCompatTextView nodeNameTv = baseViewHolder.getView(R.id.nodeNameTv);
            AppCompatTextView profitRatioTv = baseViewHolder.getView(R.id.profitRatioTv);
            AppCompatTextView nodeURLTv = baseViewHolder.getView(R.id.nodeURLTv);
            AppCompatTextView voteNumTv = baseViewHolder.getView(R.id.voteNumTv);
            QMUIRoundButton voteBtn = baseViewHolder.getView(R.id.voteBtn);
            itemRoot.setRadius(QMUIDisplayHelper.dp2px(getContext(), 12));

            LogUtils.dTag("StakeVotesMainAdapter==", "item json = " + GsonUtils.toJson(nodeBean));
            String mNodeName = "";
            String mProfitRatio = "";
            String mVoteNum = "";
            String mVoteStr = "";
            String mVote = "";
            String mNodeUrl = "";
            int mSort = baseViewHolder.getBindingAdapterPosition() - 1;
            int mTextColor;
            int mTextSize;
            if (mSort != 0) {
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
                rankingTv.setText(String.valueOf(mSort));
                rankingTv.setTextSize(mTextSize);
            }

            if (nodeSection.isSuperNode && baseFragment.type == 0) {
                rankingTv.setVisibility(View.VISIBLE);
            } else {
                rankingTv.setVisibility(View.GONE);
            }

//        int mRadii = QMUIDisplayHelper.dp2px(getContext(), 10);
            //0：获取全部投票数据；1：获取我的投票数据；2：获取我的节点；3：获取投票奖励;4：获取撤回时间
            if (baseFragment.type == 0) {//0：获取全部投票数据；
                mVote = getContext().getString(R.string.str_to_vote);
                voteBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_color_theme_11));
                mNodeName = ObjectUtils.isEmpty(nodeBean.getNode_name()) ? "" : nodeBean.getNode_name();
                mProfitRatio = ObjectUtils.isEmpty(nodeBean.getSelf_reward_share()) ? "" :
                        Constants.percent.subtract(nodeBean.getSelf_reward_share().divide(Constants.percent)) + "%";
                String mReceivedVotes = ObjectUtils.isEmpty(nodeBean.getReceived_votes()) ? "0.00 MGP" : nodeBean.getReceived_votes();
                String mStakedVotes = ObjectUtils.isEmpty(nodeBean.getStaked_votes()) ? "0.00 MGP" : nodeBean.getStaked_votes();
                mVoteNum = new BigDecimal(mReceivedVotes.split(" ")[0]).add(new BigDecimal(mStakedVotes.split(" ")[0])).toPlainString() + mStakedVotes.split(" ")[1];
                mNodeUrl = ObjectUtils.isEmpty(nodeBean.getNode_url()) ? "" : nodeBean.getNode_url();
                voteBtn.setVisibility(View.VISIBLE);
            } else if (baseFragment.type == 1) {//1：获取我的投票数据；
                //NSString *status = [dic[@"status"]intValue] == 0 ? NSLocalizedString(@"撤票中", nil) : NSLocalizedString(@"撤回", nil);
                if (nodeBean.getStatus() == 0) {
                    mVote = getContext().getString(R.string.str_cancel_ticket);
                    voteBtn.setEnabled(false);
                    voteBtn.setClickable(false);
                } else {
                    mVote = getContext().getString(R.string.str_withdraw);
                    voteBtn.setEnabled(true);
                    voteBtn.setClickable(true);
                }
                voteBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
                mNodeName = ObjectUtils.isEmpty(nodeBean.getNode_name()) ? "" : nodeBean.getNode_name();
                mVoteNum = ObjectUtils.isEmpty(nodeBean.getQuantity()) ? "0.00 MGP" : nodeBean.getQuantity();
                voteBtn.setVisibility(View.VISIBLE);
//                if (ObjectUtils.isNotEmpty(nodeBean.getVoted_at())) {
//                    if (refund_delay_sec == null) {
//                        voteBtn.setVisibility(View.INVISIBLE);
//                    } else {//1605 774 592 039 1605 773 906 000
//                        long mCurMillis = TimeUtils.getNowMills();
//                        String timeVote = timeUtils.getStringTime(nodeBean.getVoted_at());
//                        long mMillis = TimeUtils.string2Millis(timeVote);
//                        BigDecimal mBDCurMillis = new BigDecimal(mCurMillis);
//                        BigDecimal mBDMillis = new BigDecimal(mMillis);
//                        BigDecimal mTotalMillis = mBDMillis.add(refund_delay_sec.multiply(new BigDecimal("1000")));
//
//                        voteBtn.setVisibility(View.VISIBLE);
//                        if (mTotalMillis.subtract(mBDCurMillis).compareTo(BigDecimal.ZERO) > 0) {
//                            //-1表示小于，0是等于，1是大于。
//                            voteBtn.setVisibility(View.INVISIBLE);
//                        } else {
//                            voteBtn.setVisibility(View.VISIBLE);
//                        }
//                    }
//                } else {
//                    voteBtn.setVisibility(View.INVISIBLE);
//                }
                mNodeUrl = ObjectUtils.isEmpty(nodeBean.getNode_url()) ? "" : nodeBean.getNode_url();
                mProfitRatio = ObjectUtils.isEmpty(nodeBean.getShare_ratio()) ? "" : Constants.percent.subtract(nodeBean.getShare_ratio().divide(Constants.percent)) + "%";
            } else if (baseFragment.type == 2) {//2：获取我的节点；
                mVote = getContext().getString(R.string.str_cancel);
                voteBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
                mNodeName = ObjectUtils.isEmpty(nodeBean.getOwner()) ? "" : nodeBean.getOwner();
                mProfitRatio = ObjectUtils.isEmpty(nodeBean.getSelf_reward_share()) ? "" :
                        Constants.percent.subtract(nodeBean.getSelf_reward_share().divide(Constants.percent)) + "%";
                mVoteNum = ObjectUtils.isEmpty(nodeBean.getReceived_votes()) ? "0.00 MGP" : nodeBean.getReceived_votes();
                voteBtn.setVisibility(View.VISIBLE);
                mVoteStr = getContext().getString(R.string.str_invested_vote);
                mNodeUrl = getContext().getString(R.string.str_hypothecation) + (ObjectUtils.isEmpty(nodeBean.getStaked_votes()) ? "0.00 MGP" : nodeBean.getStaked_votes());
            }

            if (ObjectUtils.isNotEmpty(mNodeUrl)) {
                nodeURLTv.setText(mNodeUrl);
                nodeURLTv.setVisibility(View.VISIBLE);
            } else {
                nodeURLTv.setVisibility(View.GONE);
            }
            nodeNameTv.setText(mNodeName);
            profitRatioTv.setText(ObjectUtils.isEmpty(mProfitRatio) ? "" : String.format(getContext().getString(R.string.str_income_ratio), mProfitRatio));
            voteBtn.setText(mVote);
            voteNumTv.setText(mVoteStr + mVoteNum);
        }
    }

    public void setManage(boolean isManage) {
        this.isManage = isManage;
    }

    public void setRefundDelaySec(BigDecimal refund_delay_sec) {
        this.refund_delay_sec = refund_delay_sec;
    }

    public interface OnClickListener {
        void onClick(View v, int position);
    }
}
