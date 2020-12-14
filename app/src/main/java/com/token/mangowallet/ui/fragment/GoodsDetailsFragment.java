package com.token.mangowallet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedScrollLayout;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.BigImgDialog;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_GOODS_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.percent;

public class GoodsDetailsFragment extends BaseFragment implements OnBannerListener {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.goodsNameTv)
    AppCompatTextView goodsNameTv;
    @BindView(R.id.goodsInfoTv)
    AppCompatTextView goodsInfoTv;
    @BindView(R.id.goodsPriceTv)
    AppCompatTextView goodsPriceTv;
    @BindView(R.id.sfTv)
    AppCompatTextView sfTv;
    @BindView(R.id.specificationTv)
    AppCompatTextView specificationTv;
    @BindView(R.id.arrowsIv)
    AppCompatImageView arrowsIv;
    @BindView(R.id.freightTv)
    AppCompatTextView freightTv;
    @BindView(R.id.freightPriceTv)
    AppCompatTextView freightPriceTv;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.immediatelyBuyBtn)
    AppCompatButton immediatelyBuyBtn;
    @BindView(R.id.shareTv)
    AppCompatImageView shareTv;
    @BindView(R.id.awardTv)
    AppCompatTextView awardTv;
    @BindView(R.id.awardValueTv)
    AppCompatTextView awardValueTv;
    @BindView(R.id.locationTv)
    AppCompatTextView locationTv;
    @BindView(R.id.locationValueTv)
    AppCompatTextView locationValueTv;
    @BindView(R.id.awardNumTv)
    AppCompatTextView awardNumTv;
    @BindView(R.id.locationStateTv)
    AppCompatTextView locationStateTv;

    private Unbinder unbinder;
    private ProBean proListBean;
    private BannerImageAdapter bannerImageAdapter;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private List<String> bannerList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_details, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        proListBean = bundle.getParcelable(EXTRA_GOODS_INFO);
        bannerList = proListBean.getSliderImages();
        walletAddress = mangoWallet.getWalletAddress();
        browse();
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(topBar);
        topBar.setTitle(R.string.str_goods);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        bannerImageAdapter = new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(getBaseFragmentActivity())
                        .load(data)
//                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(1)))
                        .into(holder.imageView);
            }
        };
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(bannerImageAdapter)
                .setIndicator(new CircleIndicator(getActivity()));
        banner.setOnBannerListener(this);

        goodsNameTv.setText(proListBean.getStoreName());
        goodsInfoTv.setText(proListBean.getStoreInfo());
        goodsPriceTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(proListBean.getPrice()) ? "0" : proListBean.getPrice(),
                2, RoundingMode.FLOOR));
        specificationTv.setText(ObjectUtils.isEmpty(proListBean.getStoreType()) ? "" : proListBean.getStoreType());

        awardValueTv.setText(String.format(getString(R.string.str_buyer),
                new BigDecimal(ObjectUtils.isEmpty(proListBean.getBuyerPro()) ? "0" : proListBean.getBuyerPro())
                        .multiply(percent).setScale(2).toPlainString() + "%"));
        awardNumTv.setText(String.format(getString(R.string.str_award_number), new BigDecimal(ObjectUtils.isEmpty(proListBean.getBonusPro()) ? "0" : proListBean.getBonusPro())
                .multiply(percent).setScale(2).toPlainString() + "%"));

        locationValueTv.setText(proListBean.getCountryName());
        locationStateTv.setText(String.format(getString(R.string.str_scope_state), proListBean.getCountryName()));

        String freight;
        if (ObjectUtils.isEmpty(proListBean.getPostage()) || ObjectUtils.equals("0", proListBean.getPostage())) {
            freight = getString(R.string.str_free_freight);
        } else {
            freight = getString(R.string.str_freight) + "(" + proListBean.getPostage() + ")";
        }
        freightPriceTv.setText(freight);
    }

    @Override
    protected void initAction() {

    }

    @Override
    public void OnBannerClick(Object data, int position) {
        showBigImgDialog();
    }

    private void showBigImgDialog() {
        if (ObjectUtils.isNotEmpty(bannerList)) {
            BigImgDialog mBigImgDialog = new BigImgDialog();
            mBigImgDialog.setImgUrls(bannerList);
            mBigImgDialog.show(getChildFragmentManager());
        }
    }

    @OnClick({R.id.shareTv, R.id.immediatelyBuyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shareTv:
                proShareView(view);
                break;
            case R.id.immediatelyBuyBtn:
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_GOODS_INFO, proListBean);
                startFragment("OrderConfirmFragment", bundle);
                break;
        }
    }

    /**
     * 查询所有收货地址
     */
    private void browse() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("productId", String.valueOf(proListBean.getProID()));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().browse(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::browseSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void browseSuccess(JsonObject jsonData) {
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止轮播
        banner.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁
        banner.destroy();
    }

    private void proShareView(View v) {
        String strBase64Encode = EncodeUtils.base64Encode2String(walletAddress.getBytes());
//        byte[] bytes = EncodeUtils.base64Decode(strBase64Encode);
//        String bytes2String = ConvertUtils.bytes2String(bytes);9👈覆置本段内容👉G98XPsW5gVNiO1gGFHi1EDTZ3el9G0pZ2d890ae03c20a0b5e7c11ddb2fd7ff238f675b74a4c1689ac9fcf498705b48de3e30e52848ba7df38427e2aa6ddacad5👈 打開👉MangoWallet👈【抖音新款快手老虎蜜蜂手提包公文包网红斜挎单肩包大购物袋电脑包 抖音新款快手老虎蜜蜂手提包公文包网红斜挎单肩包大购物袋电脑包】

        String copyText = String.format(getString(R.string.str_command), String.valueOf(proListBean.getProID()),
                strBase64Encode, AppUtils.getAppName(), proListBean.getStoreName(), proListBean.getStoreInfo());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_share_command, null);
        AppCompatTextView copyTv = view.findViewById(R.id.copyTv);
        AppCompatButton shareBtn = view.findViewById(R.id.shareBtn);
//        shareBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.durban_White));
//        shareBtn.setStrokeData(0, ContextCompat.getColorStateList(getContext(), R.color.app_color_green));
        copyTv.setText(copyText);
        shareBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                ClipboardUtils.copyText(copyText);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("text/*");  //设置分享内容的类型
                imageIntent.putExtra(Intent.EXTRA_TEXT, copyText);
                startActivity(Intent.createChooser(imageIntent, getString(R.string.str_share)));
            }
        });
        QMUIFrameLayout frameLayout = new QMUIFrameLayout(getActivity());
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(getActivity(), 12));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) (ScreenUtils.getScreenWidth() * 0.9f), FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.addView(view, lp);
        QMUIPopups.fullScreenPopup(getActivity())
                .addView(frameLayout)
                .closeBtn(true)
                .skinManager(QMUISkinManager.defaultInstance(getActivity()))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
//                        Toast.makeText(getActivity(), "点击到空白区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }
                })
                .show(v);
    }
}
