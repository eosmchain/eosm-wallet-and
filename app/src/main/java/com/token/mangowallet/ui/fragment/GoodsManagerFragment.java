package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentEffectHandler;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.GoodsManaBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bus.AddGoodsSucEffect;
import com.token.mangowallet.bus.ShippingAddressEffect;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.GoodsManagAdapter;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_GOODS;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_EDIT;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class GoodsManagerFragment extends BaseFragment {


    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private GoodsManagAdapter managAdapter;
    private String walletAddress;
    private int mCurIndex = 0;
    private int mCurType = 1;
    private boolean isUp = false;//true上架false下架
    private List<GoodsManaBean.DataBean> dataBeanList = new ArrayList<>();

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_center, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_goods_manager);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightImageButton(R.mipmap.ic_add_black, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putBoolean(EXTRA_IS_EDIT, false);
                startFragment("AddGoodsFragment", bundle);
            }
        });
        initTabSegment();
        managAdapter = new GoodsManagAdapter(dataBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(managAdapter);
    }

    @Override
    protected void initAction() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMerPro();
            }
        });
        managAdapter.setOnOrderClickListener(new GoodsManagAdapter.OnOrderClickListener() {
            @Override
            public void onOrder(QMUIRoundButton view, int position) {
                String text = view.getText().toString();
                GoodsManaBean.DataBean dataBean = dataBeanList.get(position);
                if (ObjectUtils.equals(getString(R.string.str_undercarriage), text)) {////true上架false下架
                    isUp = false;
                    goodsUpDown(String.valueOf(dataBean.getProID()));
                } else if (ObjectUtils.equals(getString(R.string.str_ItemUpshelf), text)) {
                    isUp = true;
                    goodsUpDown(String.valueOf(dataBean.getProID()));
                }
            }

            @Override
            public void onDelete(QMUIRoundButton view, int position) {
                GoodsManaBean.DataBean dataBean = dataBeanList.get(position);
                int proID = dataBean.getProID();
                delPro(String.valueOf(proID));
            }
        });
        managAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                GoodsManaBean.DataBean dataBean = dataBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_GOODS, dataBean);
                bundle.putBoolean(EXTRA_IS_EDIT, true);
                startFragment("AddGoodsFragment", bundle);
            }
        });

        registerEffect(this, new QMUIFragmentEffectHandler<AddGoodsSucEffect>() {
            @Override
            public boolean shouldHandleEffect(@NonNull AddGoodsSucEffect effect) {
                LogUtils.dTag(LOG_TAG, "shouldHandleEffect==");
                mCurIndex = effect.type;
                mCurType = effect.type + 1;
                getMerPro();
                managAdapter.setType(mCurType);
                managAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public void handleEffect(@NonNull AddGoodsSucEffect effect) {
                LogUtils.dTag(LOG_TAG, "handleEffect==");
                mCurIndex = effect.type;
                mCurType = effect.type + 1;
                getMerPro();
                managAdapter.setType(mCurType);
                managAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initTabSegment() {
        String[] tabData = new String[]{getString(R.string.str_saleing), getString(R.string.str_outsold),
                getString(R.string.str_inwarehouse), getString(R.string.str_inreview)};
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();
        tabBuilder.setColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_black),
                ContextCompat.getColor(getActivity(), R.color.app_color_red_1));

        for (String tabText : tabData) {
            QMUITab tab = tabBuilder.setText(tabText).build(getContext());
            mTabSegment.addTab(tab);
        }
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.selectTab(mCurIndex);
        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
//               0全部默认 1销售中 2已售完 3 仓库中
                mCurIndex = index;
                mCurType = index + 1;
                getMerPro();
                managAdapter.setType(mCurType);
                managAdapter.notifyDataSetChanged();
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


    private void getMerPro() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("type", String.valueOf(mCurType));//0全部默认 1销售中 2已售完 3 仓库中
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().merPro(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::merProSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delPro(String proID) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("proID", proID);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().delPro(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::delProSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goodsUpDown(String proID) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("type", isUp);//true上架false下架
        params.put("proID", proID);//商品ID

        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().upDown(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::goodsUpDownSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goodsUpDownSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            ToastUtils.showLong(msgCodeBean.getMsg());
        }
        getMerPro();
    }

    private void merProSuccess(JsonObject jsonObject) {
        refreshLayout.finishRefresh();
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            GoodsManaBean goodsManaBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), GoodsManaBean.class);
            if (goodsManaBean.getCode() == 0) {
                dataBeanList.clear();
                List<GoodsManaBean.DataBean> goodsList = goodsManaBean.getData();
                dataBeanList.addAll(goodsList);
                managAdapter.setList(dataBeanList);
            } else {
                ToastUtils.showLong(goodsManaBean.getMsg());
            }
        }
    }

    private void delProSuccess(JsonObject jsonObject) {
        getMerPro();
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_delete_succeed);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        getMerPro();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();

    }
}
