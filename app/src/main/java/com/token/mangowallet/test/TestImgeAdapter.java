package com.token.mangowallet.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.List;

import static com.token.mangowallet.test.TestFragment.addImge;

public class TestImgeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TestImgeAdapter(@Nullable List<String> data) {
        super(R.layout.item_img, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String dataBean) {
        AppCompatImageView goodsPicIv = baseViewHolder.getView(R.id.goodsPicIv);
        AppCompatImageView delBtn = baseViewHolder.getView(R.id.delBtn);
        if (ObjectUtils.equals(addImge, dataBean)) {
            delBtn.setVisibility(View.GONE);
            Glide.with(goodsPicIv).asDrawable()
                    .load(R.mipmap.ic_add_pic)
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(goodsPicIv);
        } else {
            delBtn.setVisibility(View.VISIBLE);
            Glide.with(goodsPicIv)
                    .asDrawable()
                    .load(dataBean)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(goodsPicIv);
        }
    }

    private Bitmap copressImage(String imgPath) {
        File picture = new File(imgPath);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 2;
        int outWidth = bitmapFactoryOptions.outWidth;
        int outHeight = bitmapFactoryOptions.outHeight;
        Bitmap bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(bitmapFactoryOptions.outWidth / imagew);
        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }

        }
        bitmapFactoryOptions.inJustDecodeBounds = false;//false --- allowing the caller to query the bitmap without having to allocate the memory for its pixels.
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        if (bmap != null) {
            //ivwCouponImage.setImageBitmap(bmap);
            return bmap;
        }
        return null;
    }
}
