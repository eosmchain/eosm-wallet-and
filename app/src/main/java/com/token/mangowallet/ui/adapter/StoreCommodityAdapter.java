package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.ProListBean;
import com.token.mangowallet.bean.StoreHomeBean;
import com.token.mangowallet.listener.CommodityListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoreCommodityAdapter extends BaseQuickAdapter<ProListBean, BaseViewHolder> {

    private CommodityListener listener;

    public StoreCommodityAdapter() {
        super(R.layout.item_home_store_commodity);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ProListBean hotListBean) {
        AppCompatImageView commodityTitleIv = baseViewHolder.getView(R.id.commodityTitleIv);
        RecyclerView commodityGrid = baseViewHolder.getView(R.id.commodityGrid);
//        if (ObjectUtils.isNotEmpty(hotListBean.getCateImg())) {
//            Glide.with(commodityTitleIv)
//                    .load(hotListBean.getCateImg())
//                    .into(commodityTitleIv);
//        }
//        List<ProListBean> proListBeanList = hotListBean.getProList();
//        CommodityAdapter adapter = new CommodityAdapter(proListBeanList);
//        commodityGrid.addItemDecoration(new GridDividerItemDecoration(getContext(), QMUIDisplayHelper.dp2px(getContext(), 16), 0,
//                true, false, ContextCompat.getColor(getContext(), R.color.qmui_config_color_transparent)));
//        commodityGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        commodityGrid.setAdapter(adapter);
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                ProListBean proListBean = proListBeanList.get(position);
//                if (listener != null) {
//                    listener.onCommodity(proListBean);
//                }
//            }
//        });
//        commodityGrid.setNestedScrollingEnabled(false);
    }


    public void setCommodityListener(CommodityListener listener) {
        this.listener = listener;
    }
}
