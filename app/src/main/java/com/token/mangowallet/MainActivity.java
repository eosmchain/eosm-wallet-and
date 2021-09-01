package com.token.mangowallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.token.mangowallet.base.BaseFragmentActivity;
import com.token.mangowallet.bean.AppVersionBean;
import com.token.mangowallet.bean.FindProBean;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.ui.fragment.GoodsDetailsFragment;
import com.token.mangowallet.ui.home.AppsController;
import com.token.mangowallet.ui.home.HomeFragment;
import com.token.mangowallet.ui.home.MyController;
import com.token.mangowallet.utils.AppDataUtils;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.math.RoundingMode;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_GOODS_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

@FirstFragments(
        value = {
                HomeFragment.class,
                MyController.class,
                AppsController.class,
        })
@DefaultFirstFragment(HomeFragment.class)
@LatestVisitRecord
public class MainActivity extends BaseFragmentActivity {

    private String userName = "";
    private QMUIFullScreenPopup qmuiPopups = null;
    private View view;
    private BannerImageAdapter bannerImageAdapter;
    private QMUIFrameLayout frameLayout;
    private Banner banner;
    private AppCompatTextView sharerTv;
    private AppCompatTextView commodityNameTv;
    private AppCompatTextView commodityPriceTv;
    private AppCompatButton viewDetailsBtn;
    private FrameLayout.LayoutParams lp;
    public static String deal_contract = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setItemIconTintList(null);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);//CORPORATION_URL、TESTURL、GUOYU_URL、AYING_URL
        getVersion();
        deal_contract = BaseUrlUtils.getInstance().getOTCContract();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    Dialog updateDialog;

    private void getVersion() {
        try {
            NetWorkManager.getRequest().getVersion("android", "mangowalletnew", String.valueOf(AppUtils.getAppVersionCode()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<JsonObject>(MainActivity.this, false) {
                        @Override
                        public void onFail(String failMsg) {
                        }

                        @Override
                        public void onSuccess(JsonObject data) {
                            if (ObjectUtils.isNotEmpty(data)) {
                                AppVersionBean appVersionBean = GsonUtils.fromJson(data.toString(), AppVersionBean.class);
                                if (appVersionBean.getCode() > 0) {
                                } else {
                                    AppVersionBean.DataBean dataBean = appVersionBean.getData();
                                    if (dataBean != null) {
                                        String versioncode = dataBean.getVersionNum();
                                        LogUtils.e(LOG_TAG, "CurrentVersionCode = " + AppUtils.getAppVersionCode());
                                        if (ObjectUtils.isNotEmpty(versioncode))
                                            if (Integer.parseInt(versioncode) > AppUtils.getAppVersionCode()) {
                                                String url = dataBean.getDownload();
                                                String versiondes = dataBean.getVersionNum();
                                                updateDialog = DialogHelper.showUpdateDialog(MainActivity.this, dataBean, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (updateDialog != null) {
                                                            updateDialog.dismiss();
                                                        }
                                                        AppDataUtils.downloadApk(MainActivity.this, url, "MangoWallet");
                                                    }
                                                });
                                                return;
                                            }
                                    }
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSharePro() {
        userName = "";
        if (ObjectUtils.isNotEmpty(ClipboardUtils.getText())) {
            String mProText = ClipboardUtils.getText().toString();
            LogUtils.dTag(LOG_TAG, mProText);
            if (mProText.contains(getString(R.string.str_aleft)) && mProText.contains(getString(R.string.str_right))) {
                ClipboardUtils.copyText("");
                //9
                // 覆置本段内容👉G9ad5
                // 打開👉MangoWallet
                // 【抖音 电脑包】
                String[] shareData = mProText.split(getString(R.string.str_aleft));
                if (ObjectUtils.isNotEmpty(shareData)) {
                    if (shareData.length >= 2) {
                        if (ObjectUtils.isNotEmpty(shareData[2])) {
                            String[] appNameArr = shareData[2].split(getString(R.string.str_right));
                            if (ObjectUtils.equals(AppUtils.getAppName(), appNameArr[1])) {
                                String userNameBase64EncodeData = shareData[1];

                                if (ObjectUtils.isNotEmpty(userNameBase64EncodeData)) {
                                    String[] userNameBase64EncodeArr = userNameBase64EncodeData.split(getString(R.string.str_right));
                                    String userNameBase64Encode = ObjectUtils.isEmpty(userNameBase64EncodeArr) ? "" : userNameBase64EncodeArr[1];
                                    if (ObjectUtils.isNotEmpty(userNameBase64Encode)) {
                                        byte[] bytes = EncodeUtils.base64Decode(userNameBase64Encode);
                                        userName = ConvertUtils.bytes2String(bytes);
                                        MangoWallet wallet = WalletDaoUtils.getCurrentWallet();
                                        if (!ObjectUtils.equals(wallet.getWalletAddress(), userName)) {
                                            String proID = ObjectUtils.isEmpty(shareData) ? "" : shareData[0];
                                            if (ObjectUtils.isNotEmpty(proID)) {
                                                proFind(proID);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void showProPop(ProBean proBean) {
        if (view == null) {
            view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_share_commodity, null);
            banner = view.findViewById(R.id.banner);
            sharerTv = view.findViewById(R.id.sharerTv);
            commodityNameTv = view.findViewById(R.id.commodityNameTv);
            commodityPriceTv = view.findViewById(R.id.commodityPriceTv);
            viewDetailsBtn = view.findViewById(R.id.viewDetailsBtn);
        }

        if (bannerImageAdapter == null) {
            bannerImageAdapter = new BannerImageAdapter<String>(proBean.getSliderImages()) {
                @Override
                public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                    Glide.with(holder.imageView)
                            .load(data)
                            .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                            .into(holder.imageView);
                }
            };

            banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                    .setAdapter(bannerImageAdapter)
                    .setIndicator(new CircleIndicator(MainActivity.this));
        }
        if (bannerImageAdapter != null) {
            bannerImageAdapter.setDatas(proBean.getSliderImages());
            bannerImageAdapter.notifyDataSetChanged();
        }
        sharerTv.setText(String.format(getString(R.string.str_sharer), userName));
        commodityNameTv.setText(proBean.getStoreName() + " " + proBean.getStoreInfo());
        commodityPriceTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(proBean.getPrice()) ? "0" : proBean.getPrice(),
                2, RoundingMode.FLOOR));

        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qmuiPopups.dismiss();
                MangoWallet wallet = WalletDaoUtils.getCurrentWallet();
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, wallet);
                bundle.putParcelable(EXTRA_GOODS_INFO, proBean);
                GoodsDetailsFragment fragment = new GoodsDetailsFragment();
                fragment.setArguments(bundle);
                startFragment(fragment);
            }
        });
        if (frameLayout == null) {
            frameLayout = new QMUIFrameLayout(MainActivity.this);
            frameLayout.setRadius(QMUIDisplayHelper.dp2px(MainActivity.this, 12));
            lp = new FrameLayout.LayoutParams((int) (ScreenUtils.getScreenWidth() * 0.9f), FrameLayout.LayoutParams.WRAP_CONTENT);
            frameLayout.addView(view, lp);
        }
        qmuiPopups = QMUIPopups.fullScreenPopup(MainActivity.this)
                .addView(frameLayout)
                .closeBtn(true)
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
//                        Toast.makeText(MainActivity.this, "点击到空白区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        banner.destroy();
                    }
                });
        qmuiPopups.show(this.getFragmentContainerView());

    }

    /**
     * 查询商品
     */
    private void proFind(String proId) {
        Map params = MapUtils.newHashMap();
        params.put("proId", proId);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().proFind(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<JsonObject>(MainActivity.this, false) {
                        @Override
                        public void onFail(String failMsg) {

                        }

                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            if (jsonObject != null) {
                                FindProBean classifyProBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), FindProBean.class);
                                if (classifyProBean.getCode() == 0) {
                                    ProBean proBean = classifyProBean.getData();
                                    showProPop(proBean);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSharePro();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}