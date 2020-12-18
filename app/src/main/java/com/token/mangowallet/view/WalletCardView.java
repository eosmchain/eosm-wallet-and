package com.token.mangowallet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO: document your custom view class.
 */
public class WalletCardView extends FrameLayout {

    @BindView(R.id.coinView)
    AppCompatImageView coinView;
    @BindView(R.id.moreIv)
    AppCompatImageView moreIv;
    @BindView(R.id.walletMainNameTv)
    AppCompatTextView walletMainNameTv;
    @BindView(R.id.walletNameTv)
    AppCompatTextView walletNameTv;
    @BindView(R.id.toActivateTv)
    AppCompatTextView toActivateTv;
    @BindView(R.id.walletNameLayout)
    LinearLayout walletNameLayout;
    @BindView(R.id.tvTolalAssetValue)
    AppCompatTextView tvTolalAssetValue;
    @BindView(R.id.cardView)
    QMUIFrameLayout cardView;
    @BindView(R.id.noBackupTv)
    AppCompatTextView noBackupTv;

    private Context context;
    private Unbinder unbinder;
    private OnWalletCardClickListener listener;
    private int mRadius;

    public WalletCardView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WalletCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public WalletCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public WalletCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        View view = LayoutInflater.from(context).inflate(R.layout.view_wallet_assets, this);
        unbinder = ButterKnife.bind(this, view);
        cardView.setRadius(mRadius);
        int width = ScreenUtils.getScreenWidth() / 2;
        walletNameTv.setMaxWidth(width);
        QMUIFrameLayout.LayoutParams layoutParams = (LayoutParams) coinView.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth() / 2.5);
        coinView.setLayoutParams(layoutParams);
    }

    public void setWalletName(String walletName) {
        walletMainNameTv.setText(walletName);
    }

    public void setWalletAddress(boolean isActivate, String address, String status) {
        toActivateTv.setVisibility(isActivate ? View.GONE : View.VISIBLE);
        walletNameTv.setText(address + (isActivate ? "" : "(" + StringUtils.getString(R.string.str_inactive) + ")"));
    }

    public void setWalletType(Constants.WalletType walletType) {
        int raw;
        switch (walletType) {
            case MGP:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_10));
                raw = R.mipmap.ic_mgp_card_bg;
                break;
            case BTC:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_11));
                raw = R.mipmap.ic_btc_card_bg;
                break;
            case EOS:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_12));
                raw = R.mipmap.ic_eos_card_bg;
                break;
            case ETH:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_13));
                raw = R.mipmap.ic_eth_card_bg;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + walletType);
        }
        coinView.setBackgroundResource(raw);
    }

    public void setIsBackup(boolean isBackup) {
        moreIv.setVisibility(isBackup ? VISIBLE : GONE);
        noBackupTv.setVisibility(isBackup ? GONE : VISIBLE);
    }

    public void setTolalAssetValue(String balance) {
        tvTolalAssetValue.setText(balance);
    }

    @OnClick({R.id.moreIv, R.id.walletNameTv, R.id.toActivateTv, R.id.walletMainNameTv, R.id.noBackupTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.noBackupTv:
            case R.id.moreIv:
                if (listener != null) {
                    listener.onWalletInfo();
                }
                break;
            case R.id.walletNameTv:
            case R.id.walletMainNameTv:
                if (listener != null) {
                    listener.onWalletAddress();
                }
                break;
            case R.id.toActivateTv:
                if (listener != null) {
                    listener.onActivateWallet();
                }
                break;
        }
    }

    public void setSwitchCheckedListener(OnWalletCardClickListener listener) {
        this.listener = listener;
    }

    public interface OnWalletCardClickListener {
        void onActivateWallet();

        void onWalletInfo();

        void onWalletAddress();
    }

    public void release() {
        if (unbinder != null && unbinder != Unbinder.EMPTY) {

        }
    }
}
