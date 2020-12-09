package com.token.mangowallet.ui.adapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.StoreHomeBean;

import org.jetbrains.annotations.NotNull;

public class StoreClassifyAdapter extends BaseQuickAdapter<StoreHomeBean.DataBean.CateListBean, BaseViewHolder> {

    public StoreClassifyAdapter() {
        super(R.layout.item_store_classify);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreHomeBean.DataBean.CateListBean categoryListBean) {
        /**
         * cateName : 电子产品
         * sort : 1
         * pic : http://gfs9.gomein.net.cn/T15dxvBsdT1RCvBVdK.png
         * cateImg : http://172.16.11.239/img/itenTwo-Chinese.png
         * addTime : 2020-07-29
         * isDel : false
         * id : 1
         * cateID : 1
         */
        AppCompatImageView classifyIv = baseViewHolder.getView(R.id.classifyIv);
        AppCompatTextView classifyTv = baseViewHolder.getView(R.id.classifyTv);
        Glide.with(classifyIv)
                .load(categoryListBean.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                .into(classifyIv);
        baseViewHolder.itemView.setTag(R.id.categoryID, categoryListBean.getCateID());
        if (categoryListBean.isIsDel()) {
            removeAt(baseViewHolder.getLayoutPosition());
        }
        classifyTv.setText(categoryListBean.getCateName());
    }
}
