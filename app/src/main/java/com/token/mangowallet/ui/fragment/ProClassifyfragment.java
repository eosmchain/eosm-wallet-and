package com.token.mangowallet.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ClassifyProBean;
import com.token.mangowallet.bean.PageInfo;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.bean.StoreHomeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.ClassifyListAdapter;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.SpinerPopWindow;
import com.token.mangowallet.view.basepopup.ItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_CATE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_GOODS_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class ProClassifyfragment extends BaseFragment {

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarlayout)
    AppBarLayout appBarlayout;
    @BindView(R.id.spinner1)
    AppCompatTextView spinner1;
    @BindView(R.id.spinner2)
    AppCompatTextView spinner2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutSwitchIv)
    AppCompatRadioButton layoutSwitchIv;
    @BindView(R.id.line2)
    View line2;

    private Unbinder unbinder;
    private int mCurPosition1 = 0;
    private int mCurPosition2 = 0;

    private MangoWallet mangoWallet;
    private String walletAddress;
    private String productName = "";
    private String price = "";
    private String cateId;
    private String sales = "";
    private static final String limit = "20";
    private String recommend = "1";
    private String partition = "0";
    private StoreHomeBean.DataBean.CateListBean cateListBean;
    private boolean isSearch = false;
    private PageInfo pageInfo;
    private ClassifyListAdapter mClassifyAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private List<ProBean> mProList = new ArrayList<>();
    private boolean isChecked = false;
    private ListPopu mSpinnerPop;
    private boolean isOneSpinner = true;
    private List<String> mSpinnerList = new ArrayList<>();

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pro_classify, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        cateListBean = bundle.getParcelable(EXTRA_CATE_DATA);
        walletAddress = mangoWallet.getWalletAddress();
        cateId = String.valueOf(cateListBean.getCateID());
        pageInfo = new PageInfo();
        getList();
    }

    @Override
    protected void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearch) {
                    productName = "";
                    proList();
                    isSearch = false;
                    pageInfo.reset();
                    searchView.setQuery("", false);
                } else {
                    popBackStack();
                }
            }
        });
        spinner1.setText(StringUtils.getStringArray(R.array.spinner_values1)[mCurPosition1]);
        spinner2.setText(StringUtils.getStringArray(R.array.spinner_values2)[mCurPosition2]);

        initRv();
        proList();
    }

    @Override
    protected void initAction() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productName = query;
                isSearch = true;
                pageInfo.reset();
                proList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageInfo.nextPage();
                proList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageInfo.reset();
                proList();
            }
        });

        layoutSwitchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                LogUtils.dTag(LOG_TAG, "layoutSwitchIv isChecked = " + isChecked);
                if (isChecked) {
                    layoutSwitchIv.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_grid_layout));
                    showGridRowRv();
                } else {
                    layoutSwitchIv.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_list_layout));
                    showListRowRv();
                }
                layoutSwitchIv.setChecked(isChecked);
            }
        });
        mSpinnerPop.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(Object obj, int position) {
                dismissSpinnerPop();
                Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                if (isOneSpinner) {
                    spinner1.setText((String) obj);
                    spinner1.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_black));
                    spinner1.setCompoundDrawables(null, null, drawable, null);
                    if (mCurPosition1 == position) {
                        return;
                    }//1价格从高到低  0价格从低到高  默认是0 price
                    //1 按销量从高到低 0从低到高 默认是0 sales
                    //推荐 1 浏览量由高到低  0由低到高 recommend
                    price = "";
                    switch (position) {
                        case 0:
                            recommend = "1";
                            price = "";
                            sales = "";
                            break;
                        case 1:
                            recommend = "";
                            price = "1";
                            sales = "";
                            break;
                        case 2:
                            recommend = "";
                            price = "0";
                            sales = "";
                            break;
                        case 3:
                            recommend = "";
                            price = "";
                            sales = "1";
                            break;
                        case 4:
                            recommend = "";
                            price = "";
                            sales = "0";
                            break;
                    }
                    mCurPosition1 = position;
                } else {
                    spinner2.setText((String) obj);
                    spinner2.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_black));
                    spinner2.setCompoundDrawables(null, null, drawable, null);
                    if (mCurPosition2 == position) {
                        return;
                    }
                    //1A 2B 3C 4D 5E partition
                    partition = String.valueOf(position);
                    mCurPosition2 = position;
                }
                proList();
            }
        });
        mSpinnerPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                if (isOneSpinner) {
                    spinner1.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_black));
                    spinner1.setCompoundDrawables(null, null, drawable, null);
                } else {
                    spinner2.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_black));
                    spinner2.setCompoundDrawables(null, null, drawable, null);
                }
            }
        });
        mClassifyAdapter.setListener(new ClassifyListAdapter.OnGoodsItemClickListener() {
            @Override
            public void onItemClick(ProBean proListBean) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_GOODS_INFO, proListBean);
                startFragment("GoodsDetailsFragment", bundle);
            }
        });

    }

    @OnClick({R.id.spinner1, R.id.spinner2})
    public void onViewClicked(View view) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (view.getId()) {
            case R.id.spinner1:
                if (mSpinnerPop.isShowing()) {
                    dismissSpinnerPop();
                } else {
                    isOneSpinner = true;
                    spinner1.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_red));
                    spinner1.setCompoundDrawables(null, null, drawable, null);
                    mSpinnerList.clear();
                    mSpinnerList.addAll(Arrays.asList(StringUtils.getStringArray(R.array.spinner_values1)));
                    mSpinnerPop.nodfiyData(mSpinnerList);
                    mSpinnerPop.showPopupWindow(spinner1);
                }
                break;
            case R.id.spinner2:
                if (mSpinnerPop.isShowing()) {
                    dismissSpinnerPop();
                } else {
                    isOneSpinner = false;
                    spinner2.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_red));
                    spinner2.setCompoundDrawables(null, null, drawable, null);
                    mSpinnerList.clear();
                    mSpinnerList.addAll(Arrays.asList(StringUtils.getStringArray(R.array.spinner_values2)));
                    mSpinnerPop.nodfiyData(mSpinnerList);
                    mSpinnerPop.showPopupWindow(spinner2);
                }
                break;
        }
    }

    private void initRv() {
        mGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mClassifyAdapter = new ClassifyListAdapter(getActivity(), mProList);
        mClassifyAdapter.setType(1);
        recyclerView.setAdapter(mClassifyAdapter);
//        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 产品网格布局
     */
    private void showGridRowRv() {
        mClassifyAdapter.setType(0);
        recyclerView.setLayoutManager(mGridLayoutManager);
        mClassifyAdapter.notifyDataSetChanged();
    }

    /**
     * 产品列表布局
     */
    private void showListRowRv() {
        mClassifyAdapter.setType(1);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mClassifyAdapter.notifyDataSetChanged();
    }

    private void proList() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("productName", productName);
        params.put("price", price);//1价格从高到低  0价格从低到高  默认是0
        params.put("cateId", cateId);//分类ID
        params.put("sales", sales);//1 按销量从高到低 0从低到高 默认是0
        params.put("page", String.valueOf(pageInfo.page));//默认为1 第一页
        params.put("limit", limit);//默认为10
        params.put("recommend", recommend);//推荐 1 浏览量由高到低  0由低到高
        params.put("partition", partition);//1A 2B 3C 4D 5E
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().proList(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::proListSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proListSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            ClassifyProBean classifyProBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), ClassifyProBean.class);
            if (classifyProBean.getCode() == 0) {
                ClassifyProBean.DataBean dataBean = classifyProBean.getData();
                List<ProBean> proList = dataBean.getProList();
                if (pageInfo.isFirstPage()) {
                    refreshLayout.finishRefresh();
                    mProList.clear();
                    mProList.addAll(proList);
                    mClassifyAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(0);
                } else {
                    refreshLayout.finishLoadMore();
                    mProList.addAll(mProList.size(), proList);
                    mClassifyAdapter.notifyDataSetChanged();
                }
            } else {
                ToastUtils.showLong(classifyProBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        if (pageInfo.isFirstPage()) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadMore();
        }
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    private void getList() {
        mSpinnerList.addAll(Arrays.asList(StringUtils.getStringArray(R.array.spinner_values1)));
        mSpinnerPop = new ListPopu(getActivity(), mSpinnerList, R.layout.item_custom_spinner_dropdown);
    }

    class ListPopu extends SpinerPopWindow<String> {
        public ListPopu(Context context, List<String> list, int resId) {
            super(context, list, resId);
        }

        @Override
        public void setData(SpinerAdapter.SpinerHolder holder, int position) {
            CheckedTextView item = holder.getView(R.id.text1);
            String s = mSpinnerList.get(position);
            item.setText(s);
            if (isOneSpinner) {
                if (position == mCurPosition1) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
            } else {
                if (position == mCurPosition2) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
            }
        }
    }

    private void dismissSpinnerPop() {
        if (mSpinnerPop != null) {
            if (mSpinnerPop.isShowing()) {
                mSpinnerPop.dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissSpinnerPop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSpinnerPop = null;
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }

    }
}
