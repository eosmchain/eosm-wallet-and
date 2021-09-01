package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.bean.FindBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.ErrorEnvelope;
import com.token.mangowallet.ui.adapter.AppsAdapter;
import com.token.mangowallet.ui.viewmodel.AppsModelFactory;
import com.token.mangowallet.ui.viewmodel.AppsViewModel;
import com.token.mangowallet.utils.ClipboardUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_TITLE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class AppsInteriorFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_exchange)
    RelativeLayout rlExchange;
    @BindView(R.id.rl_extract)
    RelativeLayout rlExtract;
    @BindView(R.id.btnLayout)
    LinearLayout btnLayout;


    private AppsModelFactory appsModelFactory;
    private AppsViewModel appsViewModel;
    private Unbinder unbinder;
    private AppsAdapter appsAdapter;
    private BannerImageAdapter bannerImageAdapter;
    private MangoWallet mangoWallet;
    private List<String> bannerList = new ArrayList<>();
    private List<AppHomeBean.DataBean.AppBean> appBeanList;
    private String title;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_apps_interior, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        title = bundle.getString(EXTRA_TITLE, "");
        appsModelFactory = new AppsModelFactory();
        appsViewModel = ViewModelProviders.of(this.getActivity(), appsModelFactory)
                .get(AppsViewModel.class);
        appsViewModel.defaultWallet().observe(this, this::showWallet);
        appsViewModel.appHomeData().observe(this, this::onAppHomeData);
        appsViewModel.findBeanData().observe(this, this::onFindBeanData);
        showTipDialog(getString(R.string.str_loading));
        appsViewModel.error().observe(this, this::onError);
        btnLayout.setVisibility(View.VISIBLE);
    }


    @Override
    protected void initView() {
        topbar.setTitle(title);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        bannerImageAdapter = new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(getBaseFragmentActivity())
                        .load(data)
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
//                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(0)))
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
                //    0内部1外部    当传入参数type = 0时: 返回type: 1持币生息 2MID身份标识 3团队分享   4KOL激励 7MGP燃料挖矿
                //                 当传入参数type = 1时: 返回type: 1POS抵押  2Mango 链商 3Mango 金融 4Mango 媒体~~~~
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                if (appBean.getType() == 1) {// 1 持币生息
                    if (ObjectUtils.isEmpty(MyApplication.getInstance().mMid)) {
                        ToastUtils.showLong(R.string.str_please_active_mid);
                    } else {
                        startFragment("MortgageBigFragment", bundle);
                    }
                } else if (appBean.getType() == 2) {//2 MID身份标识
                    if (ObjectUtils.isEmpty(MyApplication.mMid)) {
                        startFragment("ActivateMidFragment", bundle);
                    } else {
                        ClipboardUtils.copyText(MyApplication.mMid);
                        ToastUtils.showLong(R.string.str_copy_success);
                    }
                } else if (appBean.getType() == 3) {//3 团队分享
                    startFragment("MangoAssociationFragment", bundle);
                } else if (appBean.getType() == 4) {//4 KOL激励
                    startFragment("MangoNodeFragment", bundle);
                }
            }
        });
    }


    @Override
    protected void initAction() {

    }

    @OnClick({R.id.rl_exchange, R.id.rl_extract})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        switch (view.getId()) {
            case R.id.rl_exchange:
                startFragment("NewRealTimeDateFragment", bundle);
                break;
            case R.id.rl_extract:
                if (ObjectUtils.isEmpty(MyApplication.getInstance().mMid)) {
                    ToastUtils.showShort(StringUtils.getString(R.string.str_please_active_mid));
                } else {
                    startFragment("StimulateListFragment", bundle);
                }
                break;
        }
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
        appsViewModel.fetchFindMgp();
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(appHomeBean));
    }

    private void onFindBeanData(FindBean findBean) {
        for (int i = 0; i < appBeanList.size(); i++) {
            AppHomeBean.DataBean.AppBean appBean = appBeanList.get(i);
            //    0内部1外部    当传入参数type = 0时: 返回type: 1持币生息 2MID身份标识 3团队分享   4KOL激励
            //                 当传入参数type = 1时: 返回type: 1POS抵押  2Mango 链商 3Mango 金融 4Mango 媒体~~~~
            if (appBean.getType() == 2) {
                if (findBean.getCode() == 0) {
                    FindBean.DataBean dataBean = findBean.getData();
                    MyApplication.getInstance().mMid = dataBean.getLnvitationCode();
                    appBean.setSubTitle(dataBean.getLnvitationCode());
                } else {
                    MyApplication.getInstance().mMid = "";
                    appBean.setSubTitle("");
                }
                appBeanList.set(i, appBean);
                appsAdapter.notifyDataSetChanged();
            }
        }

    }

    private void onError(ErrorEnvelope e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.message + ":" + e.code);
//        ToastUtils.showLong(R.string.str_network_error);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.durban_White));
        appsViewModel.prepare();
        appsViewModel.fetchAppHomeData("0");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (banner != null) {
            //开始轮播
            banner.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (banner != null) {
            //停止轮播
            banner.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (banner != null) {
            //销毁
            banner.destroy();
        }

    }
}