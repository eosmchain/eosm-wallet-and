package com.token.mangowallet.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.bean.ProListBean;
import com.token.mangowallet.bean.StoreHomeBean;
import com.token.mangowallet.bean.entity.ProductSection;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.ErrorEnvelope;
import com.token.mangowallet.ui.adapter.CommodityAdapter;
import com.token.mangowallet.ui.adapter.StoreClassifyAdapter;
import com.token.mangowallet.ui.viewmodel.StoreModelFactory;
import com.token.mangowallet.ui.viewmodel.StoreViewModel;
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

import static com.token.mangowallet.utils.Constants.EXTRA_CATE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_GOODS_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class StoreFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.home_grid)
    RecyclerView homeGrid;
    @BindView(R.id.hotadIv)
    AppCompatImageView hotadIv;
    @BindView(R.id.wtoutiaoIv)
    AppCompatImageView wtoutiaoIv;
    @BindView(R.id.home_page_vf)
    ViewFlipper homePageVf;
    @BindView(R.id.commodityRecyclerView)
    RecyclerView commodityRecyclerView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private BannerImageAdapter bannerImageAdapter;
    private StoreClassifyAdapter storeClassifyAdapter;
    private CommodityAdapter commodityAdapter;
    private StoreModelFactory storeModelFactory;
    private StoreViewModel storeViewModel;
    private MangoWallet mangoWallet;
    private List<String> bannerList = new ArrayList<>();
    private List<StoreHomeBean.DataBean.CateListBean> categoryListBeanList;
    private List<ProListBean> proListBeanList = new ArrayList<>();
    private List<ProductSection> ProductSectionList = new ArrayList<>();
    private PageInfo pageInfo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        QMUIStatusBarHelper.translucent(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.transparentColor));
//        BarUtils.transparentStatusBar(getActivity());
//        QMUIStatusBarHelper.translucent(getActivity());
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_store, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        pageInfo = new PageInfo();
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        storeModelFactory = new StoreModelFactory();
        storeViewModel = new ViewModelProvider(getActivity(), storeModelFactory).get(StoreViewModel.class);
        storeViewModel.defaultWallet().observe(this, this::showWallet);
        storeViewModel.storeHomeData().observe(this, this::onStoreHomeData);
        storeViewModel.error().observe(this, this::onError);
    }

    @Override
    protected void initView() {
        toolbar.setBackgroundColor(0);
        BarUtils.addMarginTopEqualStatusBarHeight(toolbar);
        bannerImageAdapter = new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(getBaseFragmentActivity())
                        .load(data)
//                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(8)))
                        .into(holder.imageView);
            }
        };
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(bannerImageAdapter)
                .setIndicator(new CircleIndicator(getActivity()));

        storeClassifyAdapter = new StoreClassifyAdapter();
        homeGrid.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        homeGrid.setAdapter(storeClassifyAdapter);

        commodityAdapter = new CommodityAdapter();
        commodityRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        commodityRecyclerView.setAdapter(commodityAdapter);
        commodityAdapter.setEmptyView(R.layout.view_empty);
        commodityRecyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    protected void initAction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        commodityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ProductSection productSection = ProductSectionList.get(position);
                if (!productSection.isHeader) {
                    ProBean proBean = (ProBean) productSection.object;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    bundle.putParcelable(EXTRA_GOODS_INFO, proBean);
                    startFragment("GoodsDetailsFragment", bundle);
                }
            }
        });
        storeClassifyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                StoreHomeBean.DataBean.CateListBean cateListBean = categoryListBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_CATE_DATA, cateListBean);
                startFragment("ProClassifyfragment", bundle);
            }
        });
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageInfo.nextPage();
//                storeViewModel.fetchStoreHome(pageInfo.page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageInfo.reset();
                storeViewModel.fetchStoreHome(pageInfo.page);
            }
        });
    }

    public void showWallet(MangoWallet wallet) {
        mangoWallet = wallet;
    }

    public void onStoreHomeData(StoreHomeBean data) {
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(data));
        if (data != null) {
            if (data.getCode() == 0) {
                StoreHomeBean.DataBean dataBean = data.getData();
                if (pageInfo.isFirstPage()) {
                    refreshLayout.finishRefresh();
                    bannerList = dataBean.getSysSlider();
                    bannerImageAdapter.setDatas(bannerList);
                    bannerImageAdapter.notifyDataSetChanged();

                    categoryListBeanList = dataBean.getCateList();
                    if (ObjectUtils.isNotEmpty(categoryListBeanList)) {
                        Collections.sort(categoryListBeanList);
                        storeClassifyAdapter.setList(categoryListBeanList);
                    }
                    String homeImg = dataBean.getHomeImg();
                    Glide.with(getBaseFragmentActivity())
                            .load(homeImg)
                            .into(hotadIv);
                    proListBeanList.clear();
                    proListBeanList.addAll(dataBean.getProList());
                } else {
                    refreshLayout.finishLoadMore();
                    proListBeanList.addAll(proListBeanList.size(), dataBean.getProList());
                }
                ProductSectionList.clear();
                boolean isNoData = false;
                for (int i = 0; i < proListBeanList.size(); i++) {
                    ProListBean proListBean = proListBeanList.get(i);
                    String pic = proListBean.getPic();
                    List<ProBean> list = proListBean.getList();
                    if (ObjectUtils.isNotEmpty(list)) {
                        isNoData = false;
                        ProductSection productSection1 = new ProductSection(true, pic);
                        ProductSectionList.add(productSection1);
                        for (int j = 0; j < list.size(); j++) {
                            ProBean proBean = list.get(j);
                            ProductSection productSection2 = new ProductSection(false, proBean);
                            ProductSectionList.add(productSection2);
                        }
                    } else {
                        isNoData = true;
                    }
                }
                if (isNoData) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
                StoreHomeBean.DataBean.HotBean hotBean = dataBean.getHot();
                if (hotBean != null) {
                    List<ProBean> list = hotBean.getList();
                    if (ObjectUtils.isNotEmpty(list)) {
                        ProductSection proTitHotBean = new ProductSection(true, hotBean.getPic());
                        ProductSectionList.add(0, proTitHotBean);
                        for (int i = 0; i < list.size(); i++) {
                            ProductSection proHotBean = new ProductSection(false, list.get(i));
                            ProductSectionList.add(1 + i, proHotBean);
                        }
                    }
                }
                commodityAdapter.setList(ProductSectionList);
                commodityAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showLong(data.getMsg());
            }
        }
    }

    private void onError(ErrorEnvelope e) {
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
    }
//    /**
//     * 初始化文字滚动条
//     *
//     * @param rolTagsesList
//     */
//    private void initTextBanner(List<String> rolTagsesList) {
//        homePageVf.removeAllViews();//先清空文字广告中的所有view
//        for (int i = 0; i < rolTagsesList.size(); i++) {
//            View noticeVF = getLayoutInflater().inflate(R.layout.item_notice_shift, null);
//            String rolTagses = rolTagsesList.get(i);
//            String rolTitleses = rolTitlesesList.get(i);
//            AppCompatTextView rolTagsesTv = noticeVF.findViewById(R.id.rolTagsesTv);
//            AppCompatTextView rolTitlesesTv = noticeVF.findViewById(R.id.rolTitlesesTv);
//            rolTagsesTv.setText(rolTagses);
//            rolTitlesesTv.setText(rolTitleses);
//            homePageVf.addView(noticeVF);
//            noticeVF.setOnClickListener(vfListener);
//        }
//    }
//
//    ClickUtils.OnDebouncingClickListener vfListener = new ClickUtils.OnDebouncingClickListener() {
//        @Override
//        public void onDebouncingClick(View v) {
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.transparentColor));
        storeViewModel.prepare();
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
        homePageVf.stopFlipping();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();//销毁
        banner.destroy();
    }
}