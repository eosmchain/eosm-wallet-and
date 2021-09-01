package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.NodeBean;
import com.token.mangowallet.bean.NodeDetailBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.ui.fragment.StakeVoteMainFragment.mVoteContract;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.percent;

public class StakeVotePaymentFragment extends BaseFragment {
    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.walletNameTv)
    AppCompatTextView walletNameTv;
    @BindView(R.id.accountTv)
    AppCompatTextView accountTv;
    @BindView(R.id.transferAmountTv)
    AppCompatTextView transferAmountTv;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.transferAmountEt)
    AppCompatEditText transferAmountEt;
    @BindView(R.id.rankingTv)
    AppCompatTextView rankingTv;
    @BindView(R.id.nodeNameTv)
    AppCompatTextView nodeNameTv;
    @BindView(R.id.profitRatioTv)
    AppCompatTextView profitRatioTv;
    @BindView(R.id.nodeURLTv)
    AppCompatTextView nodeURLTv;
    @BindView(R.id.voteBtn)
    QMUIRoundButton voteBtn;


    private Unbinder unbinder;
    private EMWalletRepository emWalletRepository;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private NodeDetailBean.DataBean mDataBean;
    private int type;
    private String mNodeName = "";
    private String mProfitRatio = "";
    private String mVoteNum = "";
    private String mNodeUrl = "";
    private QMUIDialog passwordQmuiDialog;
    private NodeBean mNodeBean;
    private String mNodeAddress = "";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_payment_vote, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        mDataBean = bundle.getParcelable("NodeDetailBean.DataBean");
        mNodeBean = bundle.getParcelable("RowsBean");
        type = bundle.getInt("type", 0);
        emWalletRepository = new EMWalletRepository();
        getEOSMGPBalance();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_vote);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        transferAmountTv.setText(walletType + "");
        walletNameTv.setText(walletType + "_" + walletAddress);
        accountTv.setText(walletAddress);


        if (mDataBean != null) {
            mNodeName = ObjectUtils.isEmpty(mDataBean.getNodeName()) ? "" : mDataBean.getNodeName();
            mProfitRatio = ObjectUtils.isEmpty(mDataBean.getNodeShareRatio()) ? ""
                    : percent.subtract(mDataBean.getNodeShareRatio().divide(percent, 2, RoundingMode.HALF_UP)).toPlainString() + "%";
            mNodeUrl = ObjectUtils.isEmpty(mDataBean.getNodeUrl()) ? "" : mDataBean.getNodeUrl();
            mNodeAddress = ObjectUtils.isEmpty(mDataBean.getMgpAddress()) ? "" : mDataBean.getMgpAddress();
        } else {
            if (type == 0) {
                mNodeName = ObjectUtils.isEmpty(mNodeBean.getOwner()) ? "" : mNodeBean.getOwner();
                mProfitRatio = ObjectUtils.isEmpty(mNodeBean.getSelf_reward_share()) ? "" :
                        percent.subtract(mNodeBean.getSelf_reward_share().divide(Constants.percent)) + "%";
                mVoteNum = ObjectUtils.isEmpty(mNodeBean.getReceived_votes()) ? "0.00 MGP" : mNodeBean.getReceived_votes();
            } else if (type == 1) {
                mNodeName = ObjectUtils.isEmpty(mNodeBean.getCandidate()) ? "" : mNodeBean.getCandidate();
                mProfitRatio = "";
                mVoteNum = ObjectUtils.isEmpty(mNodeBean.getQuantity()) ? "0.00 MGP" : mNodeBean.getQuantity();
            } else if (type == 2) {
                mNodeName = ObjectUtils.isEmpty(mNodeBean.getOwner()) ? "" : mNodeBean.getOwner();
                mProfitRatio = ObjectUtils.isEmpty(mNodeBean.getSelf_reward_share()) ? "" :
                        percent.subtract(mNodeBean.getSelf_reward_share().divide(Constants.percent)) + "%";
                mVoteNum = ObjectUtils.isEmpty(mNodeBean.getReceived_votes()) ? "0.00 MGP" : mNodeBean.getReceived_votes();
            }
            mNodeUrl = ObjectUtils.isEmpty(mNodeBean.getNode_url()) ? "" : mNodeBean.getNode_url();
            mNodeAddress = mNodeName;
        }

        nodeNameTv.setText(mNodeName);
        profitRatioTv.setText(ObjectUtils.isEmpty(mProfitRatio) ? "" : String.format(getContext().getString(R.string.str_income_ratio), mProfitRatio));
        nodeURLTv.setText(ObjectUtils.isEmpty(mNodeUrl) ? "" : getString(R.string.str_node) + ":" + mNodeUrl);

//        profitRatioTv.setText(String.format(getContext().getString(R.string.str_income_ratio), mProfitRatio));

    }

    @Override
    protected void initAction() {
        InputFilter[] filters = {ViewUtils.filter};
        transferAmountEt.setFilters(filters);
        transferAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                voteBtn.setText(getString(R.string.str_vote_t) + "(" + s + ")" + walletType);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjectUtils.isEmpty(transferAmountEt.getText())) {
                    ToastUtils.showLong(R.string.str_input_quantity);
                    return;
                }
                if (passwordQmuiDialog == null) {
                    passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                            getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                }
                passwordQmuiDialog.show();
            }
        });
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

    private void transferTransaction() {
        showTipDialog(getString(R.string.str_loading));
        BigDecimal bdQuantity = new BigDecimal(transferAmountEt.getText().toString());
        if (ObjectUtils.isEmpty(mNodeAddress)) {
            ToastUtils.showShort(R.string.str_node_address_exception);
            return;
        }
        String memo = "vote:" + mNodeAddress;

        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", mVoteContract);
        params.put("quantity", bdQuantity.setScale(4) + " " + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    private void getEOSMGPBalance() {
        try {
            emWalletRepository.fetchBalance(walletAddress, walletType).subscribe(this::balanceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void balanceSuccess(BigDecimal balance) {
        balanceTv.setText(String.format(getString(R.string.str_balance_value), balance.toPlainString() + walletType));
    }

    private void onTransaction(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                popBackStack();
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }
}
