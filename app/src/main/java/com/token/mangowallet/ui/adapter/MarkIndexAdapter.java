package com.token.mangowallet.ui.adapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.IndexMarkIndexBean;
import com.token.mangowallet.utils.BalanceUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.List;

import static com.token.mangowallet.utils.Constants.KEY_CURRENCY_DATA;
import static com.token.mangowallet.utils.Constants.SP_MangoWallet_info;

public class MarkIndexAdapter extends BaseQuickAdapter<IndexMarkIndexBean.DataBean.ListBean, BaseViewHolder> implements LoadMoreModule {

    public MarkIndexAdapter(@Nullable List<IndexMarkIndexBean.DataBean.ListBean> data) {
        super(R.layout.item_mining_mark_index, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, IndexMarkIndexBean.DataBean.ListBean markIndexItem) {
        AppCompatTextView timeTv = baseViewHolder.getView(R.id.timeTv);
        AppCompatTextView numTv = baseViewHolder.getView(R.id.numTv);
        String currencyData = SPUtils.getInstance(SP_MangoWallet_info).getString(KEY_CURRENCY_DATA, "");
        timeTv.setText(ObjectUtils.isEmpty(markIndexItem.getCreateTime()) ? "" : markIndexItem.getCreateTime());
//        if (ObjectUtils.isNotEmpty(currencyData)) {
//            CurrencyData data = GsonUtils.fromJson(currencyData, CurrencyData.class);
        numTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(markIndexItem.getNum()) ? "0" : markIndexItem.getNum(), 2, RoundingMode.FLOOR));
    }
}
