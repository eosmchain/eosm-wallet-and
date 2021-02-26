package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AddThemeBean;
import com.token.mangowallet.bean.LowerBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.ThemesBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.RequiredTextView;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MARGIN_ACCOUNT;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.VOTE_ACCOUNT;

public class SendVoteMainFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.voteThemeZHEt)
    AppCompatEditText voteThemeZHEt;
    @BindView(R.id.voteDescribeZHEt)
    AppCompatEditText voteDescribeZHEt;

    @BindView(R.id.voteThemeENEt)
    AppCompatEditText voteThemeENEt;
    @BindView(R.id.voteDescribeENEt)
    AppCompatEditText voteDescribeENEt;

    @BindView(R.id.voteThemeJAEt)
    AppCompatEditText voteThemeJAEt;
    @BindView(R.id.voteDescribeJAEt)
    AppCompatEditText voteDescribeJAEt;

    @BindView(R.id.voteThemeKOEt)
    AppCompatEditText voteThemeKOEt;
    @BindView(R.id.voteDescribeKOEt)
    AppCompatEditText voteDescribeKOEt;
    @BindView(R.id.submitBtn)
    QMUIRoundButton submitBtn;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private EMWalletRepository emWalletRepository;
    private Constants.WalletType walletType;
    private int voteId;
    private QMUIDialog passwordQmuiDialog;
    private QMUIDialog mMsgQMUIDialog;
    private boolean isEdit;//true:编辑修改；false:添加
    private ThemesBean.DataBean dataBean;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_send_vote, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        isEdit = bundle.getBoolean("isEdit", false);
        if (isEdit) {
            dataBean = bundle.getParcelable(EXTRA_VOTE_DATA);
        }
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
        getEOSMGPBalance();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_deliver_solutions));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        if (isEdit) {
            if (dataBean != null) {
                voteThemeZHEt.setText(ObjectUtils.isEmpty(dataBean.getVoteTitleZh()) ? "" : dataBean.getVoteTitleZh());
                voteDescribeZHEt.setText(ObjectUtils.isEmpty(dataBean.getVoteContentZh()) ? "" : dataBean.getVoteContentZh());
                voteThemeENEt.setText(ObjectUtils.isEmpty(dataBean.getVoteTitleEn()) ? "" : dataBean.getVoteTitleEn());
                voteDescribeENEt.setText(ObjectUtils.isEmpty(dataBean.getVoteContentEn()) ? "" : dataBean.getVoteContentEn());
                voteThemeJAEt.setText(ObjectUtils.isEmpty(dataBean.getVoteTitleJa()) ? "" : dataBean.getVoteTitleJa());
                voteDescribeJAEt.setText(ObjectUtils.isEmpty(dataBean.getVoteContentJa()) ? "" : dataBean.getVoteContentJa());
                voteThemeKOEt.setText(ObjectUtils.isEmpty(dataBean.getVoteTitleKo()) ? "" : dataBean.getVoteTitleKo());
                voteDescribeKOEt.setText(ObjectUtils.isEmpty(dataBean.getVoteContentKo()) ? "" : dataBean.getVoteContentKo());
            }
        }
    }

    @Override
    protected void initAction() {
        LogUtils.dTag("mNumer==", "mNumer = " + VoteMainFragment.mNumer);
    }


    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
        if (ObjectUtils.isEmpty(voteThemeZHEt.getText())) {
            ToastUtils.showLong(R.string.str_import_chinese_title);
            return;
        }
        if (ObjectUtils.isEmpty(voteDescribeZHEt.getText())) {
            ToastUtils.showLong(R.string.str_import_chinese_describe);
            return;
        }
        if (ObjectUtils.isEmpty(voteThemeENEt.getText())) {
            ToastUtils.showLong(R.string.str_import_english_title);
            return;
        }
        if (ObjectUtils.isEmpty(voteDescribeENEt.getText())) {
            ToastUtils.showLong(R.string.str_import_english_describe);
            return;
        }
        if (ObjectUtils.isEmpty(voteThemeJAEt.getText())) {
            ToastUtils.showLong(R.string.str_import_japan_title);
            return;
        }
        if (ObjectUtils.isEmpty(voteDescribeJAEt.getText())) {
            ToastUtils.showLong(R.string.str_import_japan_describe);
            return;
        }
        if (ObjectUtils.isEmpty(voteThemeKOEt.getText())) {
            ToastUtils.showLong(R.string.str_import_korean_title);
            return;
        }
        if (ObjectUtils.isEmpty(voteDescribeKOEt.getText())) {
            ToastUtils.showLong(R.string.str_import_korean_describe);
            return;
        }
        if (isEdit) {
            votesUpdate();
        } else {
            addTheme();
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                transferTransaction();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    /**
     * 是否开启投递方案
     */
    private void votesUpdate() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("voteId", String.valueOf(dataBean.getVoteId()));
        params.put("address", walletAddress);
        params.put("voteTitle", voteThemeZHEt.getText().toString());
        params.put("voteContent", voteDescribeZHEt.getText().toString());
        params.put("voteTitleEn", voteThemeENEt.getText().toString());
        params.put("voteContentEn", voteDescribeENEt.getText().toString());
        params.put("voteTitleJa", voteThemeJAEt.getText().toString());
        params.put("voteContentJa", voteDescribeJAEt.getText().toString());
        params.put("voteTitleKo", voteThemeKOEt.getText().toString());
        params.put("voteContentKo", voteDescribeKOEt.getText().toString());
//        params.put("schemeContent", schemeContent);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().votesUpdate(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::votesUpdateSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启投递方案
     */
    private void addTheme() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("voteTitle", voteThemeZHEt.getText().toString());
        params.put("voteContent", voteDescribeZHEt.getText().toString());
        params.put("voteTitleEn", voteThemeENEt.getText().toString());
        params.put("voteContentEn", voteDescribeENEt.getText().toString());
        params.put("voteTitleJa", voteThemeJAEt.getText().toString());
        params.put("voteContentJa", voteDescribeJAEt.getText().toString());
        params.put("voteTitleKo", voteThemeKOEt.getText().toString());
        params.put("voteContentKo", voteDescribeKOEt.getText().toString());
//        params.put("schemeContent", schemeContent);
//        @{
//            @"code":mgp_cmvoting,
//            @"action":@"addscheme",
//            @"parameter":@{
//                    @"account":[MGPHttpRequest shareManager].curretWallet.address,
//                    @"scheme_title":cell0.Jh_info,
//                    @"scheme_content":cell1.Jh_info,
//                    @"cash_money":[NSString stringWithFormat:@"%.4f MGP", amount.doubleValue]
//                }
//        }
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().addTheme(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addThemeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEOSMGPBalance() {
        try {
            emWalletRepository.fetchBalance(walletAddress, walletType)
                    .subscribe(this::balanceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transferTransaction() {
        String memo = String.valueOf(voteId) + ",0";
        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", VOTE_ACCOUNT);
        params.put("quantity", VoteMainFragment.mNumer + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(LOG_TAG,  "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    /**
     * 提交hash及抵押金额
     */
    private void upHash(String hash, String voteId) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("voteId", voteId);
        params.put("hash", hash);
        params.put("money", VoteMainFragment.mNumer);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().upHash(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::upHashSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void votesUpdateSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            AddThemeBean addThemeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), AddThemeBean.class);
            if (addThemeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_release_success);
                popBackStack();
            } else {
                ToastUtils.showLong(addThemeBean.getMsg());
            }
        }
    }

    private void addThemeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            AddThemeBean addThemeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), AddThemeBean.class);
            if (addThemeBean.getCode() == 0) {
                if (ObjectUtils.isNotEmpty(addThemeBean.getData())) {
                    voteId = addThemeBean.getData().getVoteId();
                }
                if (mMsgQMUIDialog == null) {
                    mMsgQMUIDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_warm_prompt),
                            getString(R.string.str_vote_send_msg),
                            getString(R.string.str_cancel),
                            getString(R.string.str_ok), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            }, new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    if (passwordQmuiDialog == null) {
                                        passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                                    }
                                    passwordQmuiDialog.show();
                                }
                            });
                }
                mMsgQMUIDialog.show();
            } else {
                ToastUtils.showLong(addThemeBean.getMsg());
            }
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                upHash(transactionBean.msg, String.valueOf(voteId));
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            ToastUtils.showLong(R.string.str_release_fail);
        }
    }

    private void balanceSuccess(BigDecimal balance) {
        if (VoteMainFragment.bdNumer.compareTo(balance) > 0) {
            ToastUtils.showLong(R.string.str_vote_balance_deficiency);
            popBackStack();
        }
    }

    private void upHashSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_release_success);
                popBackStack();
            } else {
                ToastUtils.showLong(R.string.str_release_fail);
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }
}
