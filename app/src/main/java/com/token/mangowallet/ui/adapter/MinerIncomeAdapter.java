package com.token.mangowallet.ui.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.MiningOrderIncomeItem;

import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;

public class MinerIncomeAdapter extends BaseQuickAdapter<MiningOrderIncomeItem, BaseViewHolder> implements LoadMoreModule {

    public MinerIncomeAdapter() {
        super(R.layout.item_miner_income);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MiningOrderIncomeItem incomeItem) {
        AppCompatImageView incomeTypeIv = baseViewHolder.getView(R.id.iv_income_type);
        AppCompatTextView incomeTypeTv = baseViewHolder.getView(R.id.tv_income_type);
        AppCompatTextView incomeTimeTv = baseViewHolder.getView(R.id.tv_income_time);
        AppCompatTextView incomeAmounTv = baseViewHolder.getView(R.id.tv_income_amount);

        String money = incomeItem.getMoney();
        if (ObjectUtils.isEmpty(money)) {
            money = "0";
        }
        BigDecimal bdMoney = new BigDecimal(money);

        if (bdMoney.compareTo(BigDecimal.ZERO) <= 0) {
            incomeAmounTv.setText(money + "");
            Glide.with(incomeTypeIv)
                    .load(R.mipmap.out_icon)
//                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(incomeTypeIv);
        } else {
            Glide.with(incomeTypeIv)
                    .load(R.mipmap.into_icon)
//                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(incomeTypeIv);
            incomeAmounTv.setText("+" + money);
        }
        incomeTimeTv.setText(incomeItem.getCreateTime());
        String typeName = incomeItem.getTypeName();
        String orderLevelName = incomeItem.getOrderLevelName();
        String name = ObjectUtils.isEmpty(typeName) ? incomeItem.getChannelName() : typeName + "(" + orderLevelName + ")";
        int start = 0;
        if (!ObjectUtils.isEmpty(typeName)) {
            start = typeName.length();
        }
        if (!ObjectUtils.isEmpty(name)) {
            SpannableString spannableString = getFormatItemValue(name, start, name.length());
            incomeTypeTv.setText(spannableString);
        } else {
            incomeTypeTv.setText("");
        }
    }

    private SpannableString getFormatItemValue(CharSequence value, int start, int end) {
        SpannableString result = new SpannableString(value);
        result.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }
}
