package com.token.mangowallet.ui.adapter;

import android.util.SparseBooleanArray;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.token.mangowallet.R;
import com.token.mangowallet.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WalletTypeAdapter extends BaseQuickAdapter<Constants.WalletType, BaseViewHolder> {
    private SparseBooleanArray mBooleanArray;

    public WalletTypeAdapter(@Nullable List<Constants.WalletType> data) {
        super(R.layout.item_wallet_type, data);
        mBooleanArray = new SparseBooleanArray(data.size());
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Constants.WalletType walletType) {
        QMUIRadiusImageView imageView = baseViewHolder.getView(R.id.typeIconIv);
        View pitchline = baseViewHolder.getView(R.id.pitchline);
        if (mBooleanArray.get(baseViewHolder.getBindingAdapterPosition())) {
            pitchline.setVisibility(View.VISIBLE);
        } else {
            pitchline.setVisibility(View.GONE);
        }
        switch (walletType) {
            case ALL:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_wallet_all));
                break;
            case MGP:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_mgp));
                break;
            case BTC:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_btc));
                break;
            case EOS:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_eos));
                break;
            case ETH:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_eth));
                break;
        }


    }

    public SparseBooleanArray getBooleanArray() {
        return mBooleanArray;
    }
}
