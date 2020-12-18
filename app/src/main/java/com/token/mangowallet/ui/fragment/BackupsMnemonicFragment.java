package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.interact.CreateWalletInteract;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import party.loveit.bip44forandroidlibrary.utils.Bip44Utils;
import party.loveit.eosforandroidlibrary.Ecc;

import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;
import static com.token.mangowallet.utils.Constants.isTest;
import static com.token.mangowallet.utils.MangoWalletUtils.EOS_PATH;

public class BackupsMnemonicFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.mnemonicWordTv)
    AppCompatTextView mnemonicWordTv;
    @BindView(R.id.skipTv)
    AppCompatTextView skipTv;

    private Unbinder unbinder;
    private MangoWallet wallet;
    private StringBuilder mnemonicSB;
    private boolean isCreate = true;
    private List<String> mnemonicCode;
    private Constants.WalletType mWalletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_backups_mnemonic_word, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        isCreate = bundle.getBoolean("isCreate", true);
        wallet = bundle.getParcelable(EXTRA_WALLET);
        mWalletType = Constants.WalletType.getPagerFromPositon(wallet.getWalletType());
        try {
            mnemonicCode = wallet.getMnemonicCode();
            mnemonicSB = new StringBuilder();
            for (String s : mnemonicCode) {
                mnemonicSB.append(s).append(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_backup_mnemonic));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        mnemonicWordTv.setText(mnemonicSB.toString().trim());
//        qmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_backup_wallet), getString(R.string.str_backup_wallet_msg),
//                "", getString(R.string.str_backup_mnemonic), null, new QMUIDialogAction.ActionListener() {
//                    @Override
//                    public void onClick(QMUIDialog dialog, int index) {
//                        dialog.dismiss();
//                    }
//                });
//        qmuiDialog.show();
    }

    @Override
    protected void initAction() {

    }

    //    @OnClick(R.id.nextStepBtn)
//    public void onViewClicked() {
    @OnClick({R.id.nextStepBtn, R.id.skipTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nextStepBtn:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isCreate", isCreate);
                bundle.putParcelable(EXTRA_WALLET, wallet);
                if (isCreate) {
                    startFragment("ConfirmMnemonicFragment", bundle);
                } else {
                    startFragmentAndDestroyCurrent("ConfirmMnemonicFragment", bundle, true);
                }
                break;
            case R.id.skipTv:
                if (isCreate) {
                    if (mWalletType == ALL || mWalletType == Constants.WalletType.MGP) {
                        showTipDialog(getString(R.string.str_creating_wallet_tip));
                        userRegister();
                    } else {
                        toMainActivity(wallet);
                    }
                } else {
                    popBackStack();
                }
                break;
        }
    }

    /**
     * 账号自动激活
     */
    private void userRegister() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("publicKey", wallet.getPublicKey());
            params.put("account", wallet.getWalletAddress());
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().userRegister(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::userRegisterSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onError(Throwable throwable) {
        dismissTipDialog();
        if (isTest) {
            FileIOUtils.writeFileFromString(getSDPath(), throwable.toString() + "  ==========  " + throwable.getMessage());
        }
        LogUtils.eTag(LOG_TAG, "e = " + throwable.toString() + " ===== " + throwable.getMessage());
        ToastUtils.showShort(R.string.str_create_wallet_fail);
    }

    private void userRegisterSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "userRegisterSuccess = " + GsonUtils.toJson(jsonObject));
        ToastUtils.showShort(R.string.str_create_wallet_succeed);
        toMainActivity(wallet);
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    private static File getSDPath() {
        File sdDir = null;
        try {
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                // 这里可以修改为你的路径
                sdDir = new File(Constants.HttpRequestLog, "Error_" + TimeUtils.getNowString() + ".txt");
                if (!sdDir.exists()) {
                    File dir = new File(sdDir.getParent());
                    dir.mkdirs();
                    sdDir.createNewFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sdDir;
    }

    private void toMainActivity(MangoWallet wallet) {
        if (wallet != null) {
            wallet.setIsBackup(false);
            WalletDaoUtils.insertNewWallet(wallet);
        }
        if (isCreate) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            WalletDaoUtils.updateCurrent(wallet);
            BusUtils.post(BUS_TO_WALLET, new ToWallet(wallet, BUS_CUT_WALLET));
            getActivity().finish();
        } else {
            popBackStack();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();
    }
}
