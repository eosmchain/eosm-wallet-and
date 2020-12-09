package com.token.mangowallet.ui.adapter;

import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class WalletCardAdapter extends BaseQuickAdapter<MangoWallet, BaseViewHolder> {
    private OnWalletClickListener listener;

    public WalletCardAdapter(List<MangoWallet> data) {
        super(R.layout.item_wallet_card, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MangoWallet mangoWallet) {
        QMUIConstraintLayout walletCardView = baseViewHolder.getView(R.id.walletCardView);
        AppCompatTextView walletAddressTv = baseViewHolder.getView(R.id.walletAddressTv);
        AppCompatTextView exportTv = baseViewHolder.getView(R.id.exportTv);
        AppCompatImageView delWalletTv = baseViewHolder.getView(R.id.delWalletTv);

        int mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        String walletAddress = mangoWallet.getWalletAddress();
        Constants.WalletType walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());

        baseViewHolder.setText(R.id.walletNameTv, walletType + "-Wallet");
        walletAddressTv.setText(ObjectUtils.isEmpty(walletAddress) ? "" : walletAddress);
        walletCardView.setBackgroundColor(ContextCompat.getColor(getContext(), getBgColor(walletType)));
        walletCardView.setRadius(mRadius);
        exportTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onExportWallet(baseViewHolder.getLayoutPosition());
                }
            }
        });
        delWalletTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onDeleteWallet(baseViewHolder.getLayoutPosition());
                }
            }
        });
        walletAddressTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (ObjectUtils.isNotEmpty(walletAddress)) {
                    ClipboardUtils.copyText(walletAddress);
                    ToastUtils.showLong(R.string.str_copy_success);
                }
            }
        });
    }


    private int getBgColor(Constants.WalletType walletType) {
        if (walletType == EOS) {
            return R.color.app_color_theme_12;
        } else if (walletType == MGP) {
            return R.color.app_color_theme_10;
        } else if (walletType == ETH) {
            return R.color.app_color_theme_13;
        } else if (walletType == BTC) {
            return R.color.app_color_theme_11;
        } else {
            return R.color.app_color_theme_10;
        }

    }

    public void setOnWalletClickListener(OnWalletClickListener listener) {
        this.listener = listener;
    }

    public interface OnWalletClickListener {
        void onExportWallet(int position);

        void onDeleteWallet(int position);
    }
}
