package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CandidatesBean;
import com.token.mangowallet.bean.CheckSuperNodeBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.NodeBean;
import com.token.mangowallet.bean.NodeListBean;
import com.token.mangowallet.bean.SchemeConfigBean;
import com.token.mangowallet.bean.SchemesThemesBean;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.VoteBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.AssociationVoteTable;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.ADDVOTE;
import static com.token.mangowallet.utils.Constants.ASSOCIATION_VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MARGIN_ACCOUNT;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.VOTE_CONTRACT;

public class VoteDetailsFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.voteTitleTv)
    AppCompatTextView voteTitleTv;
    @BindView(R.id.voteContentTv)
    AppCompatTextView voteContentTv;
    @BindView(R.id.submitBtn)
    AppCompatButton submitBtn;
    @BindView(R.id.usernameTv)
    AppCompatTextView usernameTv;
    @BindView(R.id.fireVoteTv)
    AppCompatTextView fireVoteTv;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.voteTimeTv)
    AppCompatTextView voteTimeTv;

    private Unbinder unbinder = null;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private SchemesThemesBean.RowsBean dataBean;
    private Button mRightBtn;
    private QMUIDialog qmuiDialog;
    private CheckSuperNodeBean.DataBean checkSuperBean;
    private BigDecimal mbdVoteCount = BigDecimal.ZERO;
    private boolean isSuperNode = false;
    private boolean isSponsorSuperNode = false;
    private BigDecimal mTotalVotesNum = BigDecimal.ZERO;
    public EMWalletRepository emWalletRepository;
    public Constants.WalletType walletType;
    private String privatekey = "";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vote_details, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        dataBean = bundle.getParcelable(EXTRA_VOTE_DATA);
        walletAddress = mangoWallet.getWalletAddress();
        privatekey = mangoWallet.getPrivateKey();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        if (dataBean != null) {
            String vote_count = ObjectUtils.isNotEmpty(dataBean.getVote_count()) ? dataBean.getVote_count() : "0.0000 MGP";
            String mVoteCount = vote_count.split(" ")[0];
            mbdVoteCount = new BigDecimal(mVoteCount);
        }
        emWalletRepository = new EMWalletRepository();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_scheme_details));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mRightBtn = topBar.addRightTextButton(R.string.str_vote, R.id.topbar_right_change_button);
        mRightBtn.setVisibility(View.GONE);
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSuperNode && !isSponsorSuperNode) {
                    if (qmuiDialog == null) {
                        qmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_supernode_voting)
                                , String.format(getString(R.string.str_supernode_voting_msg), String.valueOf(mTotalVotesNum.intValue())), getString(R.string.str_cancel), getString(R.string.str_vote_t), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                }, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        addSuperVote();
                                        dialog.dismiss();
                                    }
                                });
                    }
                    if (!qmuiDialog.isShowing()) {
                        qmuiDialog.show();
                    }
                }
            }
        });
        if (ObjectUtils.isNotEmpty(dataBean)) {
            voteTitleTv.setText(ObjectUtils.isEmpty(dataBean.getScheme_title()) ? "" : dataBean.getScheme_title());
            usernameTv.setText(ObjectUtils.isEmpty(dataBean.getAccount()) ? "" : dataBean.getAccount());
            fireVoteTv.setText(String.valueOf(mbdVoteCount.intValue()) + getString(R.string.str_vote_ticket));
            voteContentTv.setText(ObjectUtils.isEmpty(dataBean.getScheme_content()) ? "" : dataBean.getScheme_content());
            voteTimeTv.setText(ObjectUtils.isEmpty(dataBean.getCreated_at()) ? "" : dataBean.getCreated_at());
//            if (dataBean.getType() != 4) {
//                submitBtn.setVisibility(View.VISIBLE);
//            } else {
//                submitBtn.setVisibility(View.GONE);
//            }
        }
        checkSuperNode();
        getVotes();
        if (VoteMainFragment.rowsConfigBean.getVote() == 1) {
            submitBtn.setVisibility(View.VISIBLE);
        } else {
            submitBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
//        isVote();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        bundle.putParcelable(EXTRA_VOTE_DATA, dataBean);
        bundle.putInt("type", 1);
        startFragment("MarginFragment", bundle);
    }

    /**
     * 是否开启投递方案
     */
    private void isVote() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().isVote(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::isVoteSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测是不是超级节点
     */
    private void checkSuperNode() {
        showTipDialog(getString(R.string.str_loading));
        try {
            //@{@"json": @1,@"code": mgp_bpvoting,@"scope":mgp_bpvoting,@"table":@"candidates"}
            Map params = MapUtils.newHashMap();
            params.put("json", true);
            params.put("code", VOTE_CONTRACT);
            params.put("scope", VOTE_CONTRACT);
            params.put("table", "candidates");
            params.put("key_type", "i64");
            params.put("lower_bound", walletAddress);
            params.put("upper_bound", walletAddress);
            emWalletRepository.fetchTableRowsStr(params, walletType)
                    .subscribe(this::checkSuperNodeSuccess, this::onError);

//            String json = GsonUtils.toJson(params);
//            String content = NRSAUtils.encrypt(json);
//            NetWorkManager.getRequest().scNodeList(content)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::checkSuperNodeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getVotes() {
        try {//config/scheme/record/award
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("json", true);
            mapTableRows.put("code", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("scope", ASSOCIATION_VOTE_CONTRACT);
            mapTableRows.put("table", AssociationVoteTable.getVotedetails());
            mapTableRows.put("index_position", "2");
            mapTableRows.put("key_type", "i64");
            mapTableRows.put("lower_bound", walletAddress);
            mapTableRows.put("upper_bound", walletAddress);
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onVotesSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onVotesSuccess(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            VoteBean voteBean = GsonUtils.fromJson((String) o, VoteBean.class);
            if (voteBean != null) {
                List<VoteBean.RowsBean> rowsBeanList = voteBean.getRows();
                if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                    VoteBean.RowsBean rowsBean = rowsBeanList.get(0);
                    if (rowsBean != null) {
                        isSponsorSuperNode = rowsBean.getIs_super_node() == 1;
                    }
                }
            }
        }
        if (isSuperNode && !isSponsorSuperNode) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 投票超级节点
     */
    private void addSuperVote() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("account", walletAddress);
            params.put("is_super_node", true);
            params.put("scheme_content", dataBean.getScheme_content());
            params.put("scheme_id", String.valueOf(dataBean.getId()));
            params.put("scheme_title", dataBean.getScheme_title());
            params.put("vote_count", mTotalVotesNum + " MGP");
            String jsonData = GsonUtils.toJson(params);
            LogUtils.dTag(LOG_TAG, "accountName = " + walletAddress
                    + "params = " + jsonData);
            emWalletRepository.sendTransaction(ADDVOTE, privatekey, walletAddress, ASSOCIATION_VOTE_CONTRACT, jsonData, walletType)
                    .subscribe(this::onTransaction, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                ToastUtils.showLong(R.string.str_vote_success);
                popBackStack();
            } else {
                ToastUtils.showLong(R.string.str_vote_fail);
            }
        }
    }

    private void checkSuperNodeSuccess(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            CandidatesBean candidatesBean = GsonUtils.fromJson((String) o, CandidatesBean.class);
            List<CandidatesBean.RowsBean> rowsBeanList = candidatesBean.getRows();
            if (CollectionUtils.isNotEmpty(rowsBeanList)) {
                isSuperNode = true;
                CandidatesBean.RowsBean rowsBean = rowsBeanList.get(0);
                String staked_votes = ObjectUtils.isEmpty(rowsBean.getStaked_votes()) ? "0.0000 MGP" : rowsBean.getStaked_votes();
                String received_votes = ObjectUtils.isEmpty(rowsBean.getReceived_votes()) ? "0.0000 MGP" : rowsBean.getReceived_votes();
                staked_votes = staked_votes.split(" ")[0];
                received_votes = received_votes.split(" ")[0];
                BigDecimal mStakedVotes = new BigDecimal(staked_votes);
                BigDecimal mReceivedVotes = new BigDecimal(received_votes);
                mTotalVotesNum = mStakedVotes.add(mReceivedVotes);
                mTotalVotesNum = new BigDecimal(mTotalVotesNum.intValue()).setScale(4);
            } else {
                isSuperNode = false;
            }
//            if (nodeListBean.getCode() == 0) {
//                NodeListBean.DataBean dataBean = nodeListBean.getData();
//                List<NodeBean> superNodeList = dataBean.getSuperNodeList();
//                if (CollectionUtils.isNotEmpty(superNodeList)) {
//                    for (int i = 0; i < superNodeList.size(); i++) {
//                        NodeBean nodeBean = superNodeList.get(i);
//                        if (ObjectUtils.equals(walletAddress, nodeBean.getNode_name())) {
//                            isSuperNode = true;
//                        }
//                        total_votes_num = nodeBean.getTotal_votes_num();
//                    }
//                }
//            } else {
//                ToastUtils.showLong(nodeListBean.getMsg());
//            }
        }

        if (isSuperNode && !isSponsorSuperNode) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.GONE);
        }
    }

    private void isVoteSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_VOTE_DATA, dataBean);
                bundle.putInt("type", 1);
                startFragment("MarginFragment", bundle);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }
}
