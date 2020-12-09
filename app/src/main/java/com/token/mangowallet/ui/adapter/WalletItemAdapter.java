package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WalletItemAdapter extends BaseQuickAdapter<MangoWallet, BaseViewHolder> {

    private OnWalletItemClickListener listener;
    private int mRadius;

    public WalletItemAdapter(@Nullable List<MangoWallet> data) {
        super(R.layout.view_wallet_item, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MangoWallet wallet) {
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 5);
        QMUIFrameLayout cardView = baseViewHolder.getView(R.id.cardView);
        AppCompatImageView coinView = baseViewHolder.getView(R.id.coinView);
        AppCompatImageView moreIv = baseViewHolder.getView(R.id.moreIv);
        AppCompatTextView walletMainNameTv = baseViewHolder.getView(R.id.walletMainNameTv);
        AppCompatTextView walletAddressTv = baseViewHolder.getView(R.id.walletAddressTv);
        cardView.setRadius(mRadius);
        Constants.WalletType walletType = Constants.WalletType.getPagerFromPositon(wallet.getWalletType());
        walletMainNameTv.setText(walletType + "-Wallet");
        walletAddressTv.setText(ObjectUtils.isEmpty(wallet.getWalletAddress()) ? StringUtils.getString(R.string.str_inactive) : wallet.getWalletAddress());
        switch (walletType) {
            case MGP://mgp
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_10));
                coinView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ic_mgp_card_bg));
                break;
            case ETH://eth
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_13));
                coinView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ic_eth_card_bg));
                break;
            case BTC://btc
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_11));
                coinView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ic_btc_card_bg));
                break;
            case EOS://eos
                cardView.setBackgroundColor(ColorUtils.getColor(R.color.app_color_theme_12));
                coinView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ic_eos_card_bg));
                break;
        }

        moreIv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onToWalletMore(wallet);
                }
            }
        });
    }

    public void setOnWalletItemClickListener(OnWalletItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnWalletItemClickListener {
        void onToWalletMore(MangoWallet wallet);
    }
}
