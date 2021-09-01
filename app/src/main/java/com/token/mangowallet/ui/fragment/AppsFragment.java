package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.ErrorEnvelope;
import com.token.mangowallet.ui.adapter.AppsAdapter;
import com.token.mangowallet.ui.viewmodel.AppsModelFactory;
import com.token.mangowallet.ui.viewmodel.AppsViewModel;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_TITLE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class AppsFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private AppsModelFactory appsModelFactory;
    private AppsViewModel appsViewModel;
    private Unbinder unbinder;
    private AppsAdapter appsAdapter;
    private BannerImageAdapter bannerImageAdapter;
    private MangoWallet mangoWallet;
    private List<String> bannerList = new ArrayList<>();
    private List<AppHomeBean.DataBean.AppBean> appBeanList;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_apps, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        appsModelFactory = new AppsModelFactory();
        appsViewModel = ViewModelProviders.of(this.getActivity(), appsModelFactory)
                .get(AppsViewModel.class);
        appsViewModel.defaultWallet().observe(this, this::showWallet);
        appsViewModel.appHomeData().observe(this, this::onAppHomeData);
        showTipDialog(getString(R.string.str_loading));
        appsViewModel.error().observe(this, this::onError);
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_apps);
        bannerImageAdapter = new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(getActivity())
                        .load(data)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(8)))
                        .into(holder.imageView);
            }
        };
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(bannerImageAdapter)
                .setIndicator(new CircleIndicator(getActivity()));

        appsAdapter = new AppsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(appsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        appsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AppHomeBean.DataBean.AppBean appBean = appBeanList.get(position);
                //    0内部1外部    当传入参数type = 0时: 返回type: 1持币生息 2MID身份标识 3团队分享   4KOL激励
                //                 当传入参数type = 1时: 返回type: 1POS抵押  2Mango 链商 3Mango 金融 4Mango 媒体~~~~
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                if (appBean.getType() == 1) {// 1 POS抵押
                    bundle.putString(EXTRA_TITLE, appBean.getTitle());
                    startFragment("AppsInteriorFragment", bundle);
                } else if (appBean.getType() == 2) {//2 Mango 链商
                    startFragment("StoreFragment", bundle);
                } else if (appBean.getType() == 3) {//3 Mango 金融

                } else if (appBean.getType() == 4) {//4 Mango 媒体

                }

            }
        });
    }


    @Override
    protected void initAction() {

    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
    }

    private void onAppHomeData(AppHomeBean appHomeBean) {
        dismissTipDialog();
        if (appHomeBean != null) {
            if (appHomeBean.getCode() == 0) {
                appBeanList = appHomeBean.getData().getApp();
                Collections.sort(appBeanList);
                appsAdapter.setList(appBeanList);
                bannerList = appHomeBean.getData().getSlider();
                bannerImageAdapter.setDatas(bannerList);
                bannerImageAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showLong(appHomeBean.getMsg());
            }
        }
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(appHomeBean));
    }


    private void onError(ErrorEnvelope e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.message+":"+e.code);
    }

    @Override
    public void onResume() {
        super.onResume();
        appsViewModel.prepare();
        appsViewModel.fetchAppHomeData("1");
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
}