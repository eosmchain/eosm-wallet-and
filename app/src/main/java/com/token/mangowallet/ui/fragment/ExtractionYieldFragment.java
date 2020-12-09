package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.WithdrawIndexBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.RSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class ExtractionYieldFragment extends BaseFragment {
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
    @BindView(R.id.quantityTitleTv)
    AppCompatTextView quantityTitleTv;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.quantityEt)
    AppCompatEditText quantityEt;
    @BindView(R.id.orderValueTv)
    AppCompatTextView orderValueTv;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.text1)
    AppCompatTextView text1;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    @BindView(R.id.layout)
    QMUILinearLayout layout;
    @BindView(R.id.nextstepBtn)
    QMUIRoundButton nextstepBtn;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private int mRadius;
    private String moneyType;
    private WithdrawIndexBean withdrawIndexBean;
    private QMUIDialog qmuiDialog;
    String money = "0.0000";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mortgage_mining, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        moneyType = bundle.getString("moneyType", "2");
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        getWithdrawIndex();
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_my_motivation));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        walletInfoLayout.setRadius(mRadius);
        walletNameTv.setText(walletType + "-Wallet");
        walletAccountTv.setText(walletAddress);
        quantityTitleTv.setText(getString(R.string.str_total_recoverable_amount));
        balanceTv.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        text1.setText(getString(R.string.str_service_charge));
        ViewUtils.setEditableEditText(quantityEt, false);
    }

    @Override
    protected void initAction() {

    }

    private void updataView() {
        String lockMoney = "0";
        String serviceCharge = "0.00";
        String nextUnlockTime = "";
        if (withdrawIndexBean != null) {
            if (withdrawIndexBean.getCode() == 0) {
                WithdrawIndexBean.DataBean dataBean = withdrawIndexBean.getData();
                if (dataBean != null) {
                    money = dataBean.getMoney();
                    lockMoney = dataBean.getLockMoney();
                    serviceCharge = dataBean.getServiceCharge();
                    nextUnlockTime = dataBean.getNextUnlockTime();
                }
            } else {
                ToastUtils.showShort(withdrawIndexBean.getMsg());
            }
        }

        quantityEt.setText(ObjectUtils.isEmpty(money) ? "0.0000" : money);
        orderValueTv.setText((ObjectUtils.isEmpty(serviceCharge) ? "0.00" : serviceCharge) + (ObjectUtils.equals("1", moneyType) ? walletType : "ETH"));
    }

    @OnClick(R.id.nextstepBtn)
    public void onViewClicked() {
        BigDecimal bigDecimal;
        if (ObjectUtils.isEmpty(money)) {
            bigDecimal = BigDecimal.ZERO;
        } else {
            bigDecimal = new BigDecimal(money);
        }
        if (bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
            if (qmuiDialog == null) {
                qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                        getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
            }
            qmuiDialog.show();
        } else {
            ToastUtils.showShort(R.string.str_no_withdrawal_limit);
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            dialog.dismiss();
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                getWithdrawAddOrder();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
        }
    };

    public void getWithdrawIndex() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("moneyType", moneyType);
            String jsonData = GsonUtils.toJson(params);
            String content = RSAUtils.encrypt(jsonData);
            NetWorkManager.getRequest().getWithdrawIndex(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::withdrawIndexSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWithdrawAddOrder() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("moneyType", moneyType);
            String jsonData = GsonUtils.toJson(params);
            String content = RSAUtils.encrypt(jsonData);
            NetWorkManager.getRequest().getWithdrawAddOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::withdrawAddOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void withdrawAddOrderSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        int string;
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                string = R.string.str_withdrawal_success;
            } else {
                string = R.string.str_withdrawal_fail;
            }
        } else {
            string = R.string.str_withdrawal_fail;
        }
        ToastUtils.showShort(string);
    }

    private void withdrawIndexSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            withdrawIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), WithdrawIndexBean.class);
            updataView();
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());

    }

    @Override
    public void onResume() {
        super.onResume();
        //取消全屏设置
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BarUtils.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
