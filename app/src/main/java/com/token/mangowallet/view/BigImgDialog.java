package com.token.mangowallet.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.token.mangowallet.R;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.List;

public class BigImgDialog extends BaseDialog implements OnBannerListener, OnPageChangeListener {


    private Banner mBanner;
    private TextView textView;
    private List<String> mUrls;
    private BannerImageAdapter bannerImageAdapter;

    @Override
    public int setUpLayoutId() {
        return R.layout.dialog_bigimg;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        dialog.setOutCancel(true);
        dialog.setDimAmout(1);

        mBanner = holder.getView(R.id.banner);
        textView = holder.getView(R.id.tv_index);
        bannerImageAdapter = new BannerImageAdapter<String>(mUrls) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                ImageView imageView = holder.imageView;
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(imageView)
                        .load(data)
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .into(imageView);
            }
        };
        mBanner.addBannerLifecycleObserver(BigImgDialog.this)//添加生命周期观察者
                .setAdapter(bannerImageAdapter)
                .setIndicator(new CircleIndicator(getActivity()));
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
//        params.height = ScreenUtils.getScreenHeight();
//        params.width = ScreenUtils.getScreenWidth();
//        mBanner.setLayoutParams(params);
//        mBanner.setImageLoader(new GlideImageLoader());
//        mBanner.setImages(mUrls);
//        mBanner.start();
        mBanner.setOnBannerListener(this);
        mBanner.addOnPageChangeListener(this);

        textView.setText(1 + "/" + mUrls.size());
    }


    public void setImgUrls(List<String> imgUrls) {
        this.mUrls = imgUrls;
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        textView.setText((i + 1) + "/" + mUrls.size());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void OnBannerClick(Object data, int position) {
        dismiss();
    }
}
