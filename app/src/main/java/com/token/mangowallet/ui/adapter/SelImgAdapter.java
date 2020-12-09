package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.entity.ImgSection;
import com.token.mangowallet.view.RequiredTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SelImgAdapter extends BaseSectionQuickAdapter<ImgSection, BaseViewHolder> {

    public SelImgAdapter(@Nullable List<ImgSection> data) {
        super(R.layout.item_img_titile, data);
        setNormalLayout(R.layout.item_img);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull ImgSection imgSection) {
        RequiredTextView header = baseViewHolder.getView(R.id.header);
        if (imgSection.getObject() instanceof String) {
            header.setText((String) imgSection.getObject());
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ImgSection imgSection) {
        AppCompatImageView goodsPicIv = baseViewHolder.getView(R.id.goodsPicIv);
        AppCompatImageView delBtn = baseViewHolder.getView(R.id.delBtn);
        if (imgSection.isAdd) {
            delBtn.setVisibility(View.GONE);
            Glide.with(goodsPicIv)
                    .load(R.mipmap.ic_add_pic)
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(goodsPicIv);
        } else {
            if (imgSection.object != null) {
                delBtn.setVisibility(View.VISIBLE);
                Glide.with(goodsPicIv)
                        .load(imgSection.object.toString())
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .into(goodsPicIv);
            }
        }
    }
}
