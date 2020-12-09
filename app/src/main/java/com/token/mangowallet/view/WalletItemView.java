package com.token.mangowallet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ColorUtils;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.token.mangowallet.R;
import com.token.mangowallet.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO: document your custom view class.
 */
public class WalletItemView extends FrameLayout {

    @BindView(R.id.coinView)
    AppCompatImageView coinView;
    @BindView(R.id.moreIv)
    AppCompatImageView moreIv;
    @BindView(R.id.walletMainNameTv)
    AppCompatTextView walletMainNameTv;
    @BindView(R.id.walletAddressTv)
    AppCompatTextView walletAddressTv;
    @BindView(R.id.cardView)
    QMUIFrameLayout cardView;

    private Context context;
    private Unbinder unbinder;
    private OnWalletItemClickListener listener;

    public WalletItemView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WalletItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public WalletItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public WalletItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_wallet_item, this);
        unbinder = ButterKnife.bind(this, view);
    }

    public void setWalletName(String walletName) {
        walletMainNameTv.setText(walletName);
    }

    public void setWalletAddress(String address) {
        walletAddressTv.setText(address);
    }

    public void setWalletType(Constants.WalletType walletType) {
        switch (walletType) {
            case MGP:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_10));
                coinView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_mgp_card_bg));
                break;
            case BTC:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_11));
                coinView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_btc_card_bg));
                break;
            case EOS:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_12));
                coinView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_eos_card_bg));
                break;
            case ETH:
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_13));
                coinView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_eth_card_bg));
                break;
        }
    }

    @OnClick({R.id.moreIv, R.id.cardView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.moreIv:
                if (listener != null) {
                    listener.onToWalletMore();
                }
                break;
            case R.id.cardView:
                if (listener != null) {
                    listener.onToWallet();
                }
                break;
        }
    }

    public void setOnWalletItemClickListener(OnWalletItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnWalletItemClickListener {
        void onToWalletMore();

        void onToWallet();
    }

    public void release() {
        if (unbinder != null && unbinder != Unbinder.EMPTY) {

        }
    }
}
