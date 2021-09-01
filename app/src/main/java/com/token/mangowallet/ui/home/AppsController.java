package com.token.mangowallet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppHomeBean;
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

public class AppsController extends QMUIWindowInsetLayout {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.topbar)
    QMUITopBar topbar;

    private AppsModelFactory appsModelFactory;
    private AppsViewModel appsViewModel;
    private Unbinder unbinder;
    private AppsAdapter appsAdapter;
    private BannerImageAdapter bannerImageAdapter;
    private List<String> bannerList = new ArrayList<>();
    private List<AppHomeBean.DataBean.AppBean> appBeanList;
    private HomeFragment baseFragment;

    public AppsController(BaseFragment baseFragment) {
        super(baseFragment.getActivity());
        this.baseFragment = (HomeFragment) baseFragment;
        LayoutInflater.from(baseFragment.getActivity()).inflate(R.layout.fragment_apps, this);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle(R.string.str_apps);
        bannerImageAdapter = new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.imageView)
                        .load(data)
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .into(holder.imageView);
            }
        };
        banner.addBannerLifecycleObserver(baseFragment)//添加生命周期观察者
                .setAdapter(bannerImageAdapter)
                .setIndicator(new CircleIndicator(baseFragment.getActivity()));

        appsAdapter = new AppsAdapter();
        appsAdapter.setEmptyView(R.layout.qmui_empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseFragment.getActivity()));
        recyclerView.setAdapter(appsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(baseFragment.getActivity(), DividerItemDecoration.VERTICAL));
        appsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AppHomeBean.DataBean.AppBean appBean = appBeanList.get(position);
                //    0内部1外部    当传入参数type = 0时: 返回type: 1持币生息 2MID身份标识 3团队分享   4KOL激励
                //                 当传入参数type = 1时: 返回type: 1POS抵押  2Mango 链商 3Mango 金融 4Mango 媒体~~~~
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, baseFragment.mangoWallet);
                if (appBean.getType() == 1) {// 1 POS抵押
                    bundle.putString(EXTRA_TITLE, appBean.getTitle());
                    baseFragment.startFragment("AppsInteriorFragment", bundle);
                } else if (appBean.getType() == 2) {//2 Mango 链商
                    baseFragment.startFragment("StoreFragment", bundle);
                } else if (appBean.getType() == 3) {//3 Mango 金融
                    bundle.putInt("from", 0);
                    baseFragment.startFragment("LifeFragment", bundle);
                } else if (appBean.getType() == 4) {//4 Mango 媒体
                    ToastUtils.showLong(R.string.str_temporarily_opened);
                } else if (appBean.getType() == 5) {//4 Mango 投票
                    baseFragment.startFragment("VoteMainFragment", bundle);
                } else if (appBean.getType() == 6) {//4 Mango 投票
                    baseFragment.startFragment("OTCDealFragment", bundle);
                } else if (appBean.getType() == 7) {//7 生态抵押
//                    if (ObjectUtils.isEmpty(MyApplication.getInstance().mMid)) {
////                        ToastUtils.showLong(R.string.str_please_active_mid);
////                    } else {
////                        baseFragment.startFragment("MiningBigFragment", bundle);
////                    }
                    baseFragment.startFragment("MiningBigFragment", bundle);
                }

            }
        });
    }

    public void appHomeData(AppHomeBean appHomeBean) {
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

    public void release() {
        //销毁
        banner.destroy();

    }
}