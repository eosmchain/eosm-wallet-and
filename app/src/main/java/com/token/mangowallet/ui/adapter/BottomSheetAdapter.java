package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.PayConfigBean;

import org.jetbrains.annotations.NotNull;

public class BottomSheetAdapter extends BaseQuickAdapter<PayConfigBean.DataBean, BaseViewHolder> {

    private OnItemViewClickListener listener;

    public BottomSheetAdapter() {
        super(R.layout.pop_list_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PayConfigBean.DataBean dataBean) {
        AppCompatCheckBox radioButton = baseViewHolder.getView(R.id.radioButton);
        AppCompatImageView iconIv = baseViewHolder.getView(R.id.iconIv);
        radioButton.setText(dataBean.getName());
        Glide.with(iconIv).load(dataBean.getPic()).placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder).into(iconIv);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemView(radioButton, baseViewHolder.getLayoutPosition());
            }
        });

    }

    public void setOnItemViewClickListener(OnItemViewClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemViewClickListener {
        void onItemView(AppCompatCheckBox radioButton, int position);
    }
}
