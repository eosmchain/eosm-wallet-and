package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.BindMidBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.activity.QRCodeScanActivity;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class ActivateMidFragment extends BaseFragment {

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
    @BindView(R.id.inviterEt)
    AppCompatEditText inviterEt;
    @BindView(R.id.nextstepBtn)
    QMUIRoundButton nextstepBtn;

    private Unbinder unbinder;
    private int mRadius;
    private String walletAddress;
    private MangoWallet mangoWallet;
    private String parentMid;
    private QMUIDialog qmuiDialog;
    private BindMidBean bindMidBean;
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_activate_mid, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        mRadius = QMUIDisplayHelper.dp2px(getActivity(), 8);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
    }

    @Override
    public void initView() {
        topBar.setTitle(getString(R.string.str_mid_activate));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        walletInfoLayout.setRadius(mRadius);
        walletNameTv.setText(walletType + "-Wallet");
        walletAccountTv.setText(walletAddress);
    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.qrCodeIv, R.id.nextstepBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qrCodeIv:
                startQrCode();
                break;
            case R.id.nextstepBtn:
                if (qmuiDialog == null) {
                    qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                            getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                }
                qmuiDialog.show();
                break;
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            dialog.dismiss();
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                isFindMIDBinding();
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
        }
    };

    private void bindMid() {
        try {
            parentMid = inviterEt.getText().toString().trim();
            if (ObjectUtils.isEmpty(parentMid)) {
                ToastUtils.showShort(StringUtils.getString(R.string.str_please_enter_inviter));
            } else {
                Map params = MapUtils.newHashMap();
                params.put("mgpName", walletAddress);
                params.put("parentMid", parentMid);
                String jsonData2 = GsonUtils.toJson(params);
                String content = NRSAUtils.encrypt(jsonData2);
                NetWorkManager.getRequest().getBindMidMgp(content)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::bindMidSuccess, this::onError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isFindMIDBinding() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("type", "1");
            String jsonData = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData);
            NetWorkManager.getRequest().isFindMIDBinding(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::findMIDSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findMIDSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonData)) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
            if (msgCodeBean != null) {
                if (msgCodeBean.getCode() == 0) {
                    bindMid();
                } else {
                    ToastUtils.showLong(msgCodeBean.getMsg());
                }
            }
        }
    }

    private void bindMidSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            bindMidBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), BindMidBean.class);
            if (bindMidBean.getCode() == 0) {
                popBackStack();
            } else {
                ToastUtils.showShort(bindMidBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    public void setNextstepBtnEnabled(boolean isEnabled) {
        nextstepBtn.setEnabled(isEnabled);//去掉点击时编辑框下面横线:
        nextstepBtn.setBackgroundColor(isEnabled ? ColorUtils.getColor(R.color.app_color_dark_blue)
                : ColorUtils.getColor(R.color.item_divider_bg_color));//
    }

    /**
     * 开始扫码
     */
    public void startQrCode() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(getActivity(), shouldRequest);
                    }
                }).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Bundle bundle;
                        if (!ObjectUtils.isEmpty(result)) {
                            int resultCode = result.getResultCode();
                            Intent intent = result.getData();
                            String scanResult = intent.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                            if (!ObjectUtils.isEmpty(scanResult)) {
                                parentMid = scanResult;
                                inviterEt.setText(parentMid);
                            }
                        }
                    }
                }).launch(intent);
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
            }
        }).request();
    }

}
