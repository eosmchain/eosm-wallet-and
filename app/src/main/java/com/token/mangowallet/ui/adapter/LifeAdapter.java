package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.AppStoreLifeBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LifeAdapter extends BaseQuickAdapter<AppStoreLifeBean.DataBean, BaseViewHolder> {

    private OnPaymentListener listener;

    public LifeAdapter(@Nullable List<AppStoreLifeBean.DataBean> data) {
        super(R.layout.item_life, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AppStoreLifeBean.DataBean dataBean) {
        int mRadius = QMUIDisplayHelper.dp2px(getContext(), 10);
        QMUIConstraintLayout itemView = baseViewHolder.getView(R.id.item_view);
        itemView.setRadius(mRadius);
        QMUIRadiusImageView picIv = baseViewHolder.getView(R.id.picIv);
        AppCompatTextView storeAddressTv = baseViewHolder.getView(R.id.storeAddressTv);
        AppCompatTextView doBusinessTv = baseViewHolder.getView(R.id.doBusinessTv);
        QMUIRoundButton paymentBtn = baseViewHolder.getView(R.id.paymentBtn);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onGoPayment(dataBean);
                }
            }
        });
        if (ObjectUtils.isNotEmpty(dataBean.getImg())) {
            Glide.with(getContext())
                    .load(dataBean.getImg().get(0))
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(picIv);
        }
        storeAddressTv.setText((ObjectUtils.isEmpty(dataBean.getName()) ? "" : dataBean.getName() + " ") +
                (ObjectUtils.isEmpty(dataBean.getAddress()) ? "" : dataBean.getAddress()));
        doBusinessTv.setText(getContext().getString(R.string.str_do_business) + ":" +
                (ObjectUtils.isEmpty(dataBean.getBankTime()) ? "" : dataBean.getBankTime()));
    }

    public void setOnPaymentListener(OnPaymentListener listener) {
        this.listener = listener;
    }

    public interface OnPaymentListener {
        void onGoPayment(AppStoreLifeBean.DataBean dataBean);
    }
}
