package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MerchantBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.BIND_ACTION;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class BindETHFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.walletIconTv)
    QMUIRadiusImageView walletIconTv;
    @BindView(R.id.walletNameTv)
    AppCompatTextView walletNameTv;
    @BindView(R.id.walletAccountTv)
    AppCompatTextView walletAccountTv;
    @BindView(R.id.walletInfoLayout)
    QMUIRelativeLayout walletInfoLayout;
    @BindView(R.id.bindAddressEt)
    AppCompatEditText bindAddressEt;
    @BindView(R.id.nextstepBtn)
    QMUIRoundButton nextstepBtn;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private int mRadius;
    private MerchantBean merchantBean;
    private QMUIDialog qmuiDialog;
    private String walletAddress;
    private EMWalletRepository emWalletRepository;
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bind_wallet, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
    }

    @Override
    public void initView() {
        topBar.setTitle(getString(R.string.str_bind_eth_address));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        walletInfoLayout.setRadius(mRadius);
        walletAccountTv.setText(walletAddress);

    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.nextstepBtn)
    public void onViewClicked() {
        if (ObjectUtils.isEmpty(bindAddressEt.getText())) {
            ToastUtils.showLong(R.string.str_please_enter_eth_address);
        } else {
            if (qmuiDialog == null) {
                qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                        getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
            }
            qmuiDialog.show();
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            dialog.dismiss();
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                checkAddr();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
        }
    };

    private void newCheckstatus() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("currentAddr", walletAddress);
            params.put("type", "2");
            String jsonData2 = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().newCheckstatus(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindWalletAddressSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindAddress() {
        String bindAddress = bindAddressEt.getText().toString().trim();
        Map params = MapUtils.newHashMap();
        params.put("account", walletAddress);
        params.put("address", bindAddress);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(LOG_TAG,  "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(BIND_ACTION, privatekey, walletAddress, Constants.contractAddress, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean.isSuccess) {
            if (ObjectUtils.isEmpty(transactionBean.msg)) {
                ToastUtils.showLong(R.string.str_binding_failure);
            } else {
                bindWalletAddress(transactionBean.msg);
            }
        } else {
            ToastUtils.showLong(R.string.str_binding_failure);
        }
    }

    private void bindWalletAddress(String hash) {
        try {
            String bindAddress = bindAddressEt.getText().toString().trim();
            Map params = MapUtils.newHashMap();
            params.put("currentAddr", walletAddress);
            params.put("bindAddr", bindAddress);
            params.put("hash", hash);
            params.put("type", "2");
            params.put("money", "0");
            String jsonData2 = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().bindWalletAddress(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindWalletSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAddr() {
        try {
            String bindAddress = bindAddressEt.getText().toString().trim();
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("bindAdress", bindAddress);
            params.put("type", "2");
            String jsonData2 = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().newCheckAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::checkAddrSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindWalletAddressSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            merchantBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MerchantBean.class);
            if (merchantBean != null) {
                //备注: 0绑定成功 1绑定中 2 需绑定 3 错误信息(直接弹出)  5暂未开启
                if (merchantBean.getData() == 0) {
                    nextstepBtn.setVisibility(View.GONE);
                    bindAddressEt.setText(merchantBean.getMsg());
                    ViewUtils.setEditableEditText(bindAddressEt, false);
                } else {
                    nextstepBtn.setVisibility(View.VISIBLE);
                    ViewUtils.setEditableEditText(bindAddressEt, true);
                }
            }
        }
    }

    private void bindWalletSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
            if (msgCodeBean != null) {
                if (msgCodeBean.getCode() == 0) {
                    ToastUtils.showLong(R.string.str_binding_succeed);
                    popBackStack();
                } else {
                    ToastUtils.showLong(msgCodeBean.getMsg());
                }
            }
        }
    }

    private void checkAddrSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            MerchantBean merchantBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MerchantBean.class);
            if (merchantBean != null) {
                if (merchantBean.getData() == 2) {
                    bindAddress();
                } else {
                    ToastUtils.showLong(merchantBean.getMsg());
                }
            }
        } else {
            ToastUtils.showLong(R.string.str_binding_failure);
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }
}
