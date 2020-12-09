package com.token.mangowallet.ui.adapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.bean.ProListBean;
import com.token.mangowallet.bean.StoreHomeBean;
import com.token.mangowallet.bean.entity.ImgSection;
import com.token.mangowallet.bean.entity.ProductSection;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.view.RequiredTextView;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.util.List;

import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class CommodityAdapter extends BaseSectionQuickAdapter<ProductSection, BaseViewHolder> {

    public CommodityAdapter() {
        super(R.layout.item_img_shop);
        setNormalLayout(R.layout.item_store_commodity);

    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull ProductSection productSection) {
        AppCompatImageView areaIv = baseViewHolder.getView(R.id.areaIv);
        if (productSection.object instanceof String) {
            Glide.with(areaIv)
                    .load(ObjectUtils.isEmpty(productSection.object) ? "" : productSection.object)
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(areaIv);
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ProductSection productSection) {
        QMUILinearLayout commodityItem = baseViewHolder.getView(R.id.commodityItem);
        AppCompatImageView commodityPicIv = baseViewHolder.getView(R.id.commodityPicIv);
        AppCompatTextView commodityNameTv = baseViewHolder.getView(R.id.commodityNameTv);
        AppCompatTextView commodityPriceTv = baseViewHolder.getView(R.id.commodityPriceTv);

        int mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        commodityItem.setRadius(mRadius);
        if (productSection.object instanceof ProBean) {
            ProBean listBean = (ProBean) productSection.object;
            if (ObjectUtils.isNotEmpty(listBean.getImage_url()))
                Glide.with(commodityPicIv)
                        .load(listBean.getImage_url().get(0))
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .into(commodityPicIv);
            commodityNameTv.setText(listBean.getStoreName());
            commodityPriceTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(listBean.getPrice()) ? "0" : listBean.getPrice(), 2, RoundingMode.FLOOR));
        }
    }
}
