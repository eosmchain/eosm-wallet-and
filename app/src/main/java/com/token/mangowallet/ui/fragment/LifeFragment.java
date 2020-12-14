package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AppStoreLifeBean;
import com.token.mangowallet.bean.CategoryBean;
import com.token.mangowallet.bean.CountryBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.service.LocationService;
import com.token.mangowallet.ui.adapter.LifeAdapter;
import com.token.mangowallet.utils.LocationUtils;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_LIFE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class LifeFragment extends BaseFragment {

    @BindView(R.id.backupIV)
    AppCompatImageView backupIV;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.countryTv)
    AppCompatTextView countryTv;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private int mCurIndex = 0;
    private boolean isToSetting = false;
    private String mLatitude;
    private String mLongitude;
    private int size = 20;
    private PageInfo pageInfo;
    private int typeId = 0;
    private int countryId = -1;
    private String key = "";
    private boolean isSearch = false;
    private List<CategoryBean.DataBean> categoryBeanList;
    private List<CountryBean.DataBean> countryList;
    private CountryBean.DataBean countryData;
    private QMUIBottomSheet bottomSheet;
    private List<AppStoreLifeBean.DataBean> storeLifeList = new ArrayList<>();
    private LifeAdapter lifeAdapter;
    private boolean isLocation = false;
    private boolean isCategory = false;
    private boolean isCountry = false;
    private LocationService locationService;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_life, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        getCategory();
        getCountry();
        startLocation();
        pageInfo = new PageInfo();
    }

    @Override
    protected void initView() {
//        topBar.setTitle(R.string.str_MangoLife);
//        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button)
        lifeAdapter = new LifeAdapter(storeLifeList);
        lifeAdapter.setEmptyView(R.layout.view_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(lifeAdapter);
    }

    @Override
    protected void initAction() {
        backupIV.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (isSearch) {
                    key = "";
                    isSearch = false;
                    pageInfo.reset();
                    getAppStoreLife();
                    searchView.setQuery("", false);
                    mTabSegment.setVisibility(View.VISIBLE);
                } else {
                    popBackStack();
                }
            }
        });
        countryTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (ObjectUtils.isNotEmpty(countryList)) {
                    showSimpleBottomSheetList();
                } else {
                    getCountry();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //搜索提交（一般是软键盘中的搜索按钮）时触发该方法
                key = query;
                isSearch = true;
                pageInfo.reset();
                getAppStoreLife();
                mTabSegment.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //输入内容改变时触发该方法
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageInfo.reset();
                getAppStoreLife();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageInfo.nextPage();
                getAppStoreLife();
            }
        });

        lifeAdapter.setOnPaymentListener(new LifeAdapter.OnPaymentListener() {
            @Override
            public void onGoPayment(AppStoreLifeBean.DataBean dataBean) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_LIFE_DATA, dataBean);
                startFragment("LifePaymentFragment", bundle);
            }
        });
    }

    /**
     * 定位权限
     */
    public void startLocation() {
        PermissionUtils.permission(PermissionConstants.LOCATION)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
//                        DialogHelper.showRationaleDialog(getActivity(), shouldRequest);
                        // 转到手机设置界面，用户设置GPS
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                            }
                        }).launch(intent); // 设置完成后返回到原来的界面
                    }
                }).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {

            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
            }
        }).request();
    }

    private void initTabSegment(List<CategoryBean.DataBean> categoryBeanList) {
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();

        for (CategoryBean.DataBean dataBean : categoryBeanList) {
            String tabText = dataBean.getName();
            QMUITab tab = tabBuilder.setText(tabText).build(getContext());
            mTabSegment.addTab(tab);
        }
        int space = QMUIDisplayHelper.dp2px(getContext(), 50);
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space, 0, space, 0);
        mTabSegment.selectTab(mCurIndex);
        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
//                0 全部  1 吃喝玩乐  2 生活服务  3 运动健身
                mCurIndex = index;
                if (ObjectUtils.isNotEmpty(categoryBeanList)) {
                    CategoryBean.DataBean dataBean = categoryBeanList.get(index);
                    typeId = dataBean.getId();
                }
                pageInfo.reset();
                getAppStoreLife();
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }

    private void getCountry() {
        showTipDialog(getString(R.string.str_loading));
        try {
            NetWorkManager.getRequest().getCountry()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::countrySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 分类
     */
    private void getCategory() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("type", "1");//1 首页显示用 2 店铺上传用
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCategory(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::categorySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商铺列表
     */
    private void getAppStoreLife() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("page", String.valueOf(pageInfo.page));
        params.put("size", String.valueOf(size));
        params.put("longitude", mLongitude);
        params.put("latitude", mLatitude);
        params.put("key", key);
        params.put("type", String.valueOf(typeId));
        params.put("countryId", countryId == -1 ? "" : String.valueOf(countryId));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getAppStoreLife(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::appStoreLifeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appStoreLifeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
        if (jsonObject != null) {
            AppStoreLifeBean appStoreLifeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), AppStoreLifeBean.class);
            if (appStoreLifeBean.getCode() == 0) {
                if (pageInfo.isFirstPage()) {
                    storeLifeList.clear();
                    storeLifeList.addAll(appStoreLifeBean.getData());
                } else {
                    storeLifeList.addAll(storeLifeList.size(), appStoreLifeBean.getData());
                }
                lifeAdapter.notifyDataSetChanged();
            }
        }
    }

    private void countrySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            CountryBean countryBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), CountryBean.class);
            if (countryBean.getCode() == 0) {
                isCountry = true;
                countryList = countryBean.getData();
                if (ObjectUtils.isNotEmpty(countryList)) {
                    CountryBean.DataBean dataBean = countryList.get(0);
                    countryId = dataBean.getId();
                    countryTv.setText(dataBean.getCountyName());
                }
                if (isCategory && isCategory && isLocation)
                    getAppStoreLife();
            }
        }
    }

    private void categorySuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            CategoryBean categoryBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), CategoryBean.class);
            if (categoryBean.getCode() == 0) {
                isCategory = true;
                categoryBeanList = categoryBean.getData();
                if (ObjectUtils.isNotEmpty(categoryBeanList)) {
                    typeId = categoryBeanList.get(0).getId();
                    initTabSegment(categoryBeanList);
                }
                if (isCategory && isCategory && isLocation)
                    getAppStoreLife();
            } else {
                ToastUtils.showLong(categoryBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        if (refreshLayout != null) {
            if (pageInfo.isFirstPage()) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
        }
        LogUtils.dTag("error==", "e = " + e.getMessage());
    }

    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void showSimpleBottomSheetList() {
        if (bottomSheet == null) {
            QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
            builder.setGravityCenter(true)

                    .setTitle(getString(R.string.str_please_choose) + getString(R.string.str_united_states))
                    .setAddCancelBtn(true)
                    .setAllowDrag(false)
                    .setNeedRightMark(false)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            countryData = countryList.get(position);
                            countryId = countryData.getId();
                            String mCountyName = countryData.getCountyName();
                            countryTv.setText(mCountyName);
                        }
                    });
            for (int i = 0; i < countryList.size(); i++) {
                CountryBean.DataBean dataBean = countryList.get(i);
                if (dataBean != null) {
                    builder.addItem(dataBean.getCountyName());
                }
            }
            bottomSheet = builder.build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                locationService.stop();
                mLatitude = String.valueOf(location.getLatitude());
                mLongitude = String.valueOf(location.getLongitude());
                isLocation = true;
                if (isCategory && isCountry && isLocation)
                    getAppStoreLife();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * @param locType 当前定位类型
         * @param diagnosticType 诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);

        }
    };


    @Override
    public void onStart() {
        super.onStart();
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getArguments().getInt("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.start();
        }
        locationService.start();// 定位SDK
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtils.getInstance(getActivity()).removeLocationUpdatesListener();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }


}
