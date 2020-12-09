package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.NodeBean;
import com.token.mangowallet.bean.NodeDetailBean;
import com.token.mangowallet.bean.VotesBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.PhotoUtils;
import com.token.mangowallet.utils.RSAUtils;
import com.token.mangowallet.view.PictureChoicePop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.percent;

public class StakeVoteDetailsFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.nodeNameTv)
    AppCompatTextView nodeNameTv;
    @BindView(R.id.nodeURLNameTv)
    AppCompatTextView nodeURLNameTv;
    @BindView(R.id.nodeDescribeTv)
    AppCompatTextView nodeDescribeTv;
    @BindView(R.id.popularVoteBtn)
    QMUIRoundButton popularVoteBtn;
    @BindView(R.id.votesNumTv)
    AppCompatTextView votesNumTv;
    @BindView(R.id.nodeURLTv)
    AppCompatTextView nodeURLTv;
    @BindView(R.id.rateValTv)
    AppCompatTextView rateValTv;
    @BindView(R.id.associationIntroduceValTv)
    AppCompatTextView associationIntroduceValTv;
    @BindView(R.id.headerIv)
    QMUIRadiusImageView headerIv;
    @BindView(R.id.compileVoteBtn)
    AppCompatTextView compileVoteBtn;

    private Unbinder unbinder;
    private boolean isMy;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private NodeBean mNodeBean;
    private int type;
    private String mNodeName = "";
    private String mProfitRatio = "";
    private String mVoteNum = "";
    private NodeDetailBean.DataBean mDataBean;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_stake_vote_details, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        isMy = bundle.getBoolean("isMy", false);
        mNodeBean = bundle.getParcelable("RowsBean");
        type = bundle.getInt("type", 0);
    }

    @Override
    protected void initView() {
        topBar.setTitle("StakeVote");
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        if (isMy) {
            topBar.addRightTextButton(R.string.str_account, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
                @Override
                public void onDebouncingClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
//                    bundle.putParcelable("RowsBean", mRowsBean);
//                    bundle.putBoolean("isMy", false);
//                    bundle.putBoolean("isManage", type == 0 ? false : true);
                    startFragment("StakeVoteListFragment", bundle);
                }
            });
        }

        if (type == 0) {
            mNodeName = ObjectUtils.isEmpty(mNodeBean.getOwner()) ? "" : mNodeBean.getOwner();
            mProfitRatio = ObjectUtils.isEmpty(mNodeBean.getSelf_reward_share()) ? "0.00%" :
                    mNodeBean.getSelf_reward_share().divide(Constants.percent) + "%";
            mVoteNum = ObjectUtils.isEmpty(mNodeBean.getReceived_votes()) ? "0.00 MGP" : mNodeBean.getReceived_votes();
        } else if (type == 1) {
            mNodeName = ObjectUtils.isEmpty(mNodeBean.getCandidate()) ? "" : mNodeBean.getCandidate();
            mProfitRatio = "0.00%";
            mVoteNum = ObjectUtils.isEmpty(mNodeBean.getQuantity()) ? "0.00 MGP" : mNodeBean.getQuantity();
        } else if (type == 2) {
            mNodeName = ObjectUtils.isEmpty(mNodeBean.getOwner()) ? "" : mNodeBean.getOwner();
            mProfitRatio = ObjectUtils.isEmpty(mNodeBean.getSelf_reward_share()) ? "0.00%" :
                    mNodeBean.getSelf_reward_share().divide(Constants.percent) + "%";
            mVoteNum = ObjectUtils.isEmpty(mNodeBean.getReceived_votes()) ? "0.00 MGP" : mNodeBean.getReceived_votes();
        }

        nodeURLNameTv.setText(mNodeName);
        votesNumTv.setText(mVoteNum);
        getNodeDetail();
    }

    @Override
    protected void initAction() {
        popularVoteBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable("NodeDetailBean.DataBean", mDataBean);
                bundle.putParcelable("RowsBean", mNodeBean);
                bundle.putBoolean("isMy", false);
                bundle.putInt("type", type);
                startFragment("StakeVotePaymentFragment", bundle);
            }
        });
        compileVoteBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putBoolean("isAdd", false);
                startFragment("StakeAddVoteFragment", bundle);
            }
        });
    }

    private void getNodeDetail() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", mNodeName);
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().nodeDetail(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::nodeDetailSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nodeDetailSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        dismissTipDialog();
        compileVoteBtn.setVisibility(View.GONE);
        if (jsonObject != null) {
            NodeDetailBean nodeDetailBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), NodeDetailBean.class);
            if (nodeDetailBean.getCode() == 0) {
                mDataBean = nodeDetailBean.getData();
                if (mDataBean != null) {
                    Glide.with(getActivity()).load(ObjectUtils.isEmpty(mDataBean.getNodeHeadImg()) ?
                            R.mipmap.ic_mgp : mDataBean.getNodeHeadImg())
                            .placeholder(R.mipmap.ic_mgp).error(R.mipmap.ic_mgp).into(headerIv);
                    nodeNameTv.setText(ObjectUtils.isEmpty(mDataBean.getNodeName()) ? "" : mDataBean.getNodeName());
                    nodeURLNameTv.setText(ObjectUtils.isEmpty(mDataBean.getMgpAddress()) ? "" : mDataBean.getMgpAddress());
                    nodeDescribeTv.setText(ObjectUtils.isEmpty(mDataBean.getNodeContent()) ? "" : mDataBean.getNodeContent());

                    nodeURLTv.setText(ObjectUtils.isEmpty(mDataBean.getNodeUrl()) ? "" : mDataBean.getNodeUrl());
                    rateValTv.setText(ObjectUtils.isEmpty(mDataBean.getNodeShareRatio()) ? "" :
                            percent.subtract(mDataBean.getNodeShareRatio().divide(percent, 2, RoundingMode.HALF_UP)).toPlainString() + "%");
                    associationIntroduceValTv.setText(ObjectUtils.isEmpty(mDataBean.getNodeRewardRule()) ? "" : mDataBean.getNodeRewardRule());
                } else {
                    if (isMy) {
                        compileVoteBtn.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (isMy) {
                    compileVoteBtn.setVisibility(View.VISIBLE);
                }
                ToastUtils.showLong(nodeDetailBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    @Override
    public void onResume() {
        super.onResume();
        getNodeDetail();
    }
}
