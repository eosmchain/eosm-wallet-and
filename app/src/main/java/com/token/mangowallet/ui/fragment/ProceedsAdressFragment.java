package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class ProceedsAdressFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.walletNameTv)
    AppCompatTextView walletNameTv;
    @BindView(R.id.walletAddressTv)
    AppCompatTextView walletAddressTv;
    @BindView(R.id.qrCodeAdressIv)
    AppCompatImageView qrCodeAdressIv;
    @BindView(R.id.logoIv)
    QMUIRadiusImageView logoIv;
    @BindView(R.id.cardView)
    QMUIFrameLayout cardView;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;
    private Bitmap qrBitmap;
    private final int size = 200;
    private String walletAddress;
    private int radius = 0;
    private boolean isWalletAdress;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_proceeds_adress, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        isWalletAdress = bundle.getBoolean("isWalletAdress");
        if (isWalletAdress) {
            mangoWallet = bundle.getParcelable(EXTRA_WALLET);
            walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
            walletAddress = mangoWallet.getWalletAddress();
        }

        radius = QMUIDisplayHelper.dp2px(getContext(), 10);
    }

    @Override
    protected void initView() {
        topBar.setTitle(isWalletAdress ? getString(R.string.str_proceeds) : getString(R.string.str_invitation_code));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightImageButton(R.mipmap.ic_share, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (qrBitmap != null) {
                    Intent imageIntent = new Intent(Intent.ACTION_SEND);
                    imageIntent.setType("image/*");  //设置分享内容的类型
                    imageIntent.putExtra(Intent.EXTRA_STREAM, AppFilePath.saveBitmap(getActivity(), qrBitmap));
                    startActivity(Intent.createChooser(imageIntent, getString(R.string.str_share)));
                }
            }
        });
        cardView.setRadius(radius);
        walletNameTv.setText(isWalletAdress ? walletType + "-Wallet" : getString(R.string.str_invitation_code));
        walletAddressTv.setText(isWalletAdress ? walletAddress : MyApplication.getInstance().mMid);
        qrBitmap = QRCodeEncoder.syncEncodeQRCode(isWalletAdress ? walletAddress : MyApplication.getInstance().mMid, size);
        qrCodeAdressIv.setImageBitmap(qrBitmap);
    }


    @Override
    protected void initAction() {

    }

    @OnClick(R.id.walletAddressTv)
    public void onViewClicked() {
        ClipboardUtils.copyText(walletAddress);
        ToastUtils.showLong(R.string.str_copy_success);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        qrBitmapRecycle();
    }


    private void qrBitmapRecycle() {
        if (qrBitmap != null) {
            qrBitmap.recycle();
        }
        qrBitmap = null;
    }
}
