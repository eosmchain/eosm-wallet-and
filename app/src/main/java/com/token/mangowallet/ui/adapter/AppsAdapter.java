package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.AppHomeBean;

import org.jetbrains.annotations.NotNull;

public class AppsAdapter extends BaseQuickAdapter<AppHomeBean.DataBean.AppBean, BaseViewHolder> {


    public AppsAdapter() {
        super(R.layout.item_app);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AppHomeBean.DataBean.AppBean appBean) {
        /**
         * childTitle : 链上绑定，智能识别
         * id : 5
         * isDel : false
         * lang : zh_CN
         * sort : 1
         * tab : 去中心化网络
         * title : MID身份标识
         * type : 0
         */
        AppCompatImageView smallIconIv = baseViewHolder.getView(R.id.smallIconIv);
        AppCompatImageView bigIconIv = baseViewHolder.getView(R.id.bigIconIv);
        AppCompatTextView itemSubTv = baseViewHolder.getView(R.id.itemSubTv);
        baseViewHolder.setText(R.id.itemTitleTv, appBean.getTab());
//        baseViewHolder.setImageResource(R.id.bigIconIv, getLarge(appBean.getType()));
        baseViewHolder.setText(R.id.itemThemeTv, appBean.getTitle());
        baseViewHolder.setText(R.id.itemDetailsTv, appBean.getChildTitle());
        itemSubTv.setVisibility(View.GONE);
        Glide.with(smallIconIv).asDrawable().load(appBean.getTabImg()).apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder)).into(smallIconIv);
        Glide.with(bigIconIv).asDrawable().load(appBean.getImg())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder)).into(bigIconIv);

        if (appBean.getAppTypel() == 0) {
            String subTitle = appBean.getSubTitle();
            itemSubTv.setText(subTitle);
            itemSubTv.setVisibility(View.VISIBLE);
//            if (appBean.getType() == 1) {//1持币生息
//                if (ObjectUtils.isEmpty(subTitle)) {
//                    removeAt(baseViewHolder.getLayoutPosition());
//                }
//            }
        }
    }

//    private int getThumbnail(int type) {
////    0内部1外部    当传入参数type = 0时 : 返回的type: 1持币生息 2MID身份标识3团队分享4KOL激励
////                当传入参数type = 1时: 返回type: 1POS抵押 2Mango 链商 3Mango 金融4Mango 媒体~~~~
//        switch (type) {
//            case 1://1 持币生息
//                if (isExternal) {
//                    return R.mipmap.small_hashrate_icon;
//                } else {
//                    return R.mipmap.small_finance_icon;
//                }
//            case 2://2 MID身份标识
//                if (isExternal) {
//                    return R.mipmap.small_chainstore_icon;
//                } else {
//                    return R.mipmap.column_no_center;
//                }
//            case 3://3 团队分享
//                if (isExternal) {
//                    return R.mipmap.small_finance_icon;
//                } else {
//                    return R.mipmap.column_team;
//                }
//            case 4://4 KOL激励
//                if (isExternal) {
//                    return R.mipmap.small_media_icon;
//                } else {
//                    return R.mipmap.column_kol;
//                }
//            default://
//                return R.mipmap.column_exchange;
//        }
//    }
//
//    private int getLarge(int type) {
//        //    0内部1外部    当传入参数type = 0时 : 返回的type: 1持币生息 2MID身份标识3团队分享4KOL激励
////                当传入参数type = 1时: 返回type: 1POS抵押 2Mango 链商 3Mango 金融4Mango 媒体~~~~
//        switch (type) {
//            case 1://1 持币生息
//                if (isExternal) {
//                    return R.mipmap.big_hashrate_icon;
//                } else {
//                    return R.mipmap.iv_hashrate;
//                }
//            case 2://2 MID身份标识
//                if (isExternal) {
//                    return R.mipmap.big_chainstore_icon;
//                } else {
//                    return R.mipmap.iv_mid;
//                }
//            case 3://3 团队分享
//                if (isExternal) {
//                    return R.mipmap.big_finance_icon;
//                } else {
//                    return R.mipmap.iv_team;
//                }
//            case 4://4 KOL激励
//                if (isExternal) {
//                    return R.mipmap.big_media_icon;
//                } else {
//                    return R.mipmap.iv_light_node;
//                }
//            default://
//                return R.mipmap.iv_stimulate;
//        }
//    }
}
