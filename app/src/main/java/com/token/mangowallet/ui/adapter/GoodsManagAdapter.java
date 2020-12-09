package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.GoodsManaBean;
import com.token.mangowallet.bean.OrderGoodsBean;
import com.token.mangowallet.utils.BalanceUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.List;

public class GoodsManagAdapter extends BaseQuickAdapter<GoodsManaBean.DataBean, BaseViewHolder> {

    private OnOrderClickListener listener;
    private int mCurType;

    public GoodsManagAdapter(@Nullable List<GoodsManaBean.DataBean> data) {
        super(R.layout.item_goods_man, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, GoodsManaBean.DataBean dataBean) {
        int mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        QMUIConstraintLayout itemGoods = baseViewHolder.getView(R.id.itemGoods);
        AppCompatTextView usernameTv = baseViewHolder.getView(R.id.usernameTv);
        AppCompatTextView statusTv = baseViewHolder.getView(R.id.statusTv);
        AppCompatImageView goodsPicIv = baseViewHolder.getView(R.id.goodsPicIv);
        AppCompatTextView goodsNameTv = baseViewHolder.getView(R.id.goodsNameTv);
        AppCompatTextView goodsSpecificationTv = baseViewHolder.getView(R.id.goodsSpecificationTv);
        AppCompatTextView goodsNumberTv = baseViewHolder.getView(R.id.goodsNumberTv);
        AppCompatTextView goodsPriceTv = baseViewHolder.getView(R.id.goodsPriceTv);
        QMUIRoundButton goodsBtn = baseViewHolder.getView(R.id.goodsBtn);
        QMUIRoundButton delBtn = baseViewHolder.getView(R.id.delBtn);

        itemGoods.setRadius(mRadius);
        String payStatus = "";
        String goodsBtnText = "";
        if (dataBean.getImage_url() != null) {
            Glide.with(goodsPicIv)
                    .load(dataBean.getImage_url().get(0))
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(goodsPicIv);
        }
        goodsNameTv.setText(dataBean.getStoreName());
        goodsSpecificationTv.setText(getContext().getString(R.string.str_goods_specification) + dataBean.getStoreName());
        goodsNumberTv.setText(getContext().getString(R.string.str_goods_quantity) + dataBean.getStock());
        goodsPriceTv.setText(getContext().getString(R.string.str_price) + dataBean.getPrice());
        goodsPriceTv.setText(getContext().getString(R.string.str_price).replace("$", "") + BalanceUtils.currencyToBase(ObjectUtils.isEmpty(dataBean.getPrice()) ? "0" : dataBean.getPrice(), 2, RoundingMode.FLOOR));
        int mStrokeWidth = QMUIDisplayHelper.dp2px(getContext(), 1);
        if (mCurType == 1) {//销售中
            goodsBtn.setText(R.string.str_undercarriage);
            goodsBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
            goodsBtn.setStrokeData(mStrokeWidth, ContextCompat.getColorStateList(getContext(), R.color.qmui_config_color_red));
            delBtn.setVisibility(View.GONE);
        } else if (mCurType == 2) {//已售空
            goodsBtn.setText(R.string.str_SOLDOUT);
            goodsBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_common_hint));
            goodsBtn.setStrokeData(0, null);
            delBtn.setVisibility(View.VISIBLE);
        } else if (mCurType == 3) {//仓库中
            goodsBtn.setText(R.string.str_ItemUpshelf);
            goodsBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
            goodsBtn.setStrokeData(mStrokeWidth, ContextCompat.getColorStateList(getContext(), R.color.qmui_config_color_red));
            delBtn.setVisibility(View.VISIBLE);
        } else if (mCurType == 4) {//审核中
            goodsBtn.setText(R.string.str_inreview);
            goodsBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
            goodsBtn.setStrokeData(0, null);
            delBtn.setVisibility(View.GONE);
        }
        goodsBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onOrder((QMUIRoundButton) v, baseViewHolder.getLayoutPosition());
                }
            }
        });
        delBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onDelete((QMUIRoundButton) v, baseViewHolder.getLayoutPosition());
                }
            }
        });
    }

    public void setType(int curType) {
        this.mCurType = curType;
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public interface OnOrderClickListener {
        void onOrder(QMUIRoundButton view, int position);

        void onDelete(QMUIRoundButton view, int position);
    }
}
