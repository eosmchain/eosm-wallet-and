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
import com.token.mangowallet.bean.CheckSuperNodeBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

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
    private ThemesBean.DataBean dataBean;
    private Button mRightBtn;
    private QMUIDialog qmuiDialog;
    private CheckSuperNodeBean.DataBean checkSuperBean;

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
                if (qmuiDialog == null) {
                    qmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_supernode_voting)
                            , String.format(getString(R.string.str_supernode_voting_msg), String.valueOf(dataBean.getVoteCount())), getString(R.string.str_cancel), getString(R.string.str_vote_t), new QMUIDialogAction.ActionListener() {
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
        });
        if (ObjectUtils.isNotEmpty(dataBean)) {
            voteTitleTv.setText(ObjectUtils.isEmpty(dataBean.getVoteTitle()) ? "" : dataBean.getVoteTitle());
            usernameTv.setText(ObjectUtils.isEmpty(dataBean.getAddress()) ? "" : dataBean.getAddress());
            fireVoteTv.setText(ObjectUtils.isEmpty(dataBean.getVoteCount()) ? "0" + getString(R.string.str_vote_ticket) : dataBean.getVoteCount() + getString(R.string.str_vote_ticket));
            voteContentTv.setText(ObjectUtils.isEmpty(dataBean.getVoteContent()) ? "" : dataBean.getVoteContent());
            voteTimeTv.setText(ObjectUtils.isEmpty(dataBean.getCreateTime()) ? "" : dataBean.getCreateTime());
            if (dataBean.getType() != 4) {
                submitBtn.setVisibility(View.VISIBLE);
            } else {
                submitBtn.setVisibility(View.GONE);
            }
        }
        checkSuperNode();
    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
        isVote();
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
        Map params = MapUtils.newHashMap();
        params.put("mgpName", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().checkSuperNode(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::checkSuperNodeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测是不是超级节点
     */
    private void addSuperVote() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("voteId", String.valueOf(dataBean.getVoteId()));
        params.put("money", String.valueOf(checkSuperBean.getMoney()));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().addSuperVote(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addSuperVoteSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSuperVoteSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_vote_success);
                popBackStack();
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void checkSuperNodeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            CheckSuperNodeBean checkSuperNodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), CheckSuperNodeBean.class);
            if (checkSuperNodeBean.getCode() == 0) {
                checkSuperBean = checkSuperNodeBean.getData();
                if (checkSuperBean != null) {
                    if ((checkSuperBean.getIsSuperNode() == 1 && checkSuperBean.getIsVote() == 0) && dataBean.getType() != 4) {
                        mRightBtn.setVisibility(View.VISIBLE);
                    } else {
                        mRightBtn.setVisibility(View.GONE);
                    }
                }
            } else {
                ToastUtils.showLong(checkSuperNodeBean.getMsg());
            }
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
}
