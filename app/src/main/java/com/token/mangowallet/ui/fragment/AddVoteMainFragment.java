package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.SchemesThemesBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.view.DialogHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import one.block.eosiojava.models.rpcProvider.Action;
import one.block.eosiojava.models.rpcProvider.Authorization;

import static com.token.mangowallet.utils.Constants.ADDSCHEME;
import static com.token.mangowallet.utils.Constants.ASSOCIATION_VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_VOTE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.UPSCHEME;


public class AddVoteMainFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.voteThemeEt)
    AppCompatEditText voteThemeEt;
    @BindView(R.id.voteDescribeEt)
    AppCompatEditText voteDescribeEt;


    private Unbinder unbinder;
    private QMUIDialog qmuiDialog;
    private MangoWallet mangoWallet;
    private boolean isEdit;
    private SchemesThemesBean.RowsBean dataBean;
    private String walletAddress;
    private Constants.WalletType walletType;
    private EMWalletRepository emWalletRepository;
    private String privatekey = "";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_vote, null);
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
        privatekey = mangoWallet.getPrivateKey();
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
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
                voteThemeEt.setText(ObjectUtils.isEmpty(dataBean.getScheme_title()) ? "" : dataBean.getScheme_title());
                voteDescribeEt.setText(ObjectUtils.isEmpty(dataBean.getScheme_content()) ? "" : dataBean.getScheme_content());
            }
        }
    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
        if (ObjectUtils.isEmpty(voteThemeEt.getText())) {
            ToastUtils.showLong(R.string.str_enter_vote_theme);
            return;
        }
        if (ObjectUtils.isEmpty(voteDescribeEt.getText())) {
            ToastUtils.showLong(R.string.str_enter_vote_describe);
            return;
        }
        if (qmuiDialog == null) {
            qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
        qmuiDialog.show();
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            dialog.dismiss();
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                addAssociationSchemes();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
        }
    };

    private void addAssociationSchemes() {
        showTipDialog(getString(R.string.str_loading));
        if (isEdit) {
            Map params = MapUtils.newHashMap();
            params.put("account", walletAddress);
            params.put("scheme_title", voteThemeEt.getText().toString());
            params.put("scheme_content", voteDescribeEt.getText().toString());
            params.put("scheme_id", String.valueOf(dataBean.getId()));
            String jsonData = GsonUtils.toJson(params);
            emWalletRepository.sendTransaction(UPSCHEME, privatekey, walletAddress, ASSOCIATION_VOTE_CONTRACT, jsonData, walletType).subscribe(this::onTransaction, this::onError);
        } else {
            List<Action> actionList = new ArrayList<>();
            Action mAction1;
            Action mAction2;

            String memo = "";
            Map params = MapUtils.newHashMap();
            params.put("memo", memo);
            params.put("from", walletAddress);
            params.put("to", ASSOCIATION_VOTE_CONTRACT);
            params.put("quantity", VoteMainFragment.rowsConfigBean.getCash_money());
            String jsonData = GsonUtils.toJson(params);

//        LogUtils.dTag(LOG_TAG, "accountName = " + walletAddress
//                + "params = " + jsonData);

            mAction1 = new Action(EOSIO_TOKEN_CONTRACT_CODE, TRANSFER_ACTION, Collections.singletonList(new Authorization(walletAddress, "active")), jsonData);
            params.clear();
            params.put("account", walletAddress);
            params.put("scheme_title", voteThemeEt.getText().toString());
            params.put("scheme_content", voteDescribeEt.getText().toString());
            params.put("cash_money", VoteMainFragment.rowsConfigBean.getCash_money());
            jsonData = GsonUtils.toJson(params);
            mAction2 = new Action(ASSOCIATION_VOTE_CONTRACT, ADDSCHEME, Collections.singletonList(new Authorization(walletAddress, "active")), jsonData);

            actionList.add(mAction1);
            actionList.add(mAction2);

            emWalletRepository.sendTransaction(actionList, privatekey, walletType)
                    .subscribe(this::onTransaction, this::onError);
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                if (isEdit) {
                    ToastUtils.showLong(R.string.address_msg_edit_success);
                } else {
                    ToastUtils.showLong(R.string.str_release_success);
                }
                popBackStack();
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            if (isEdit) {
                ToastUtils.showLong(R.string.str_edit_failure);
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
