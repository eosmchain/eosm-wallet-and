package com.token.mangowallet.ui.adapter;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.token.mangowallet.R;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.entity.TokenInfo;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.List;

import static com.token.mangowallet.utils.Constants.BTC_SYMBOL;
import static com.token.mangowallet.utils.Constants.EOS_SYMBOL;
import static com.token.mangowallet.utils.Constants.ETH_SYMBOL;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;

public class WalletAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {

    public WalletAdapter(@Nullable List<Token> data) {
        super(R.layout.item_wallet, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Token token) {
        QMUIRadiusImageView walletIconIv = baseViewHolder.findView(R.id.walletIconIv);
        AppCompatTextView coinNameTv = baseViewHolder.findView(R.id.coinNameTv);
        AppCompatTextView balanceTv = baseViewHolder.findView(R.id.balanceTv);
        AppCompatTextView valueTv = baseViewHolder.findView(R.id.valueTv);
        String symbol = "";
        int raw = 0;
        TokenInfo tokenInfo = token.tokenInfo;
        Constants.WalletType walletType = tokenInfo.walletType;
        String balance = ObjectUtils.isEmpty(token.balance) ? "0" : token.balance;
        String value = ObjectUtils.isEmpty(token.value) ? "0" : token.value;
        String price = ObjectUtils.isEmpty(token.price) ? "0" : token.price;
        switch (walletType) {
            case MGP:
                symbol = MGP_SYMBOL;
                raw = R.mipmap.ic_mgp_rounded;
                break;
            case BTC:
                symbol = BTC_SYMBOL;
                raw = R.mipmap.ic_btc;
                break;
            case EOS:
                symbol = EOS_SYMBOL;
                raw = R.mipmap.ic_eos;
                break;
            case ETH:
                symbol = ETH_SYMBOL;
                raw = R.mipmap.ic_eth;
                break;
        }
        if (raw != 0) {
            walletIconIv.setImageDrawable(ContextCompat.getDrawable(getContext(), raw));
        }
        if (ObjectUtils.isEmpty(balance)) {
            balance = "0";
        }
        if (ObjectUtils.isEmpty(value)) {
            value = "0";
        }
        coinNameTv.setText(symbol + "");
        balanceTv.setText(balance + " " + symbol);
        valueTv.setText(BalanceUtils.currencyToBase(price, 4, RoundingMode.FLOOR));
    }

    public void setTokens(List<Token> tokens) {
        setNewData(tokens);
        notifyDataSetChanged();
    }
}
