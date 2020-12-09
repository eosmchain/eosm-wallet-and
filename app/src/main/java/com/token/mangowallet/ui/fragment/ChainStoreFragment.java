package com.token.mangowallet.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.util.SmartUtil;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MerchantBean;
import com.token.mangowallet.bean.StoreIncomeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.RSAUtils;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_IS_BUYER;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_EDIT;
import static com.token.mangowallet.utils.Constants.EXTRA_ORDER_TYPE;
import static com.token.mangowallet.utils.Constants.EXTRA_STORE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.percent;

public class ChainStoreFragment extends BaseFragment {

    @BindView(R.id.parallax)
    AppCompatImageView parallax;
    @BindView(R.id.userAvatarIv)
    QMUIRadiusImageView userAvatarIv;
    @BindView(R.id.userNameTv)
    AppCompatTextView userNameTv;
    @BindView(R.id.userLvTv)
    AppCompatImageView userLvTv;
    @BindView(R.id.recruitmentLayout)
    FrameLayout recruitmentLayout;
    @BindView(R.id.addAddressLayout)
    FrameLayout addAddressLayout;
    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout collapsingTopbarLayout;
    @BindView(R.id.appBarlayout)
    FrameLayout appBarlayout;
    @BindView(R.id.unpaidLayout)
    FrameLayout unpaidLayout;
    @BindView(R.id.toSendGoodsLayout)
    FrameLayout toSendGoodsLayout;
    @BindView(R.id.receivingLayout)
    FrameLayout receivingLayout;
    @BindView(R.id.returnedGoodsLayout)
    FrameLayout returnedGoodsLayout;
    @BindView(R.id.allOrdersLayout)
    FrameLayout allOrdersLayout;
    @BindView(R.id.totalMoneyTv)
    AppCompatTextView totalMoneyTv;
    @BindView(R.id.moneyUnitTv)
    AppCompatTextView moneyUnitTv;
    @BindView(R.id.totalLimitTv)
    AppCompatTextView totalLimitTv;
    @BindView(R.id.totalValueOrderTv)
    AppCompatTextView totalValueOrderTv;
    @BindView(R.id.ordeCentreTv)
    AppCompatTextView ordeCentreTv;
    @BindView(R.id.commodityManagementTv)
    AppCompatTextView commodityManagementTv;
    @BindView(R.id.totalValueOrderValueTv)
    AppCompatTextView totalValueOrderValueTv;
    @BindView(R.id.ordeCentreValueTv)
    AppCompatTextView ordeCentreValueTv;
    @BindView(R.id.commodityManagementValueTv)
    AppCompatTextView commodityManagementValueTv;
    @BindView(R.id.myEarningsTv)
    AppCompatTextView myEarningsTv;
    @BindView(R.id.hasBookedTv)
    AppCompatTextView hasBookedTv;
    @BindView(R.id.hasBookedValueTv)
    AppCompatTextView hasBookedValueTv;
    @BindView(R.id.hasBookedUnitTv)
    AppCompatTextView hasBookedUnitTv;
    @BindView(R.id.notBookedTv)
    AppCompatTextView notBookedTv;
    @BindView(R.id.refundedTv)
    AppCompatTextView refundedTv;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.merLayout1)
    ConstraintLayout merLayout1;
    @BindView(R.id.merLayout2)
    ConstraintLayout merLayout2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.redBadgeTv1)
    QMUIRoundButton redBadgeTv1;
    @BindView(R.id.redBadgeTv2)
    QMUIRoundButton redBadgeTv2;
    @BindView(R.id.redBadgeTv3)
    QMUIRoundButton redBadgeTv3;
    @BindView(R.id.redBadgeTv4)
    QMUIRoundButton redBadgeTv4;
    @BindView(R.id.redBadgeTv5)
    QMUIRoundButton redBadgeTv5;
    @BindView(R.id.myStoreTv)
    AppCompatTextView myStoreTv;
    @BindView(R.id.localLifeTv)
    AppCompatTextView localLifeTv;
    @BindView(R.id.goodsPicIv)
    QMUIRadiusImageView goodsPicIv;
    @BindView(R.id.goodsNameIv)
    AppCompatTextView goodsNameIv;
    @BindView(R.id.storeStatusTv)
    AppCompatTextView storeStatusTv;
    @BindView(R.id.stateShopsTv)
    AppCompatTextView stateShopsTv;
    @BindView(R.id.businessRatioTv)
    AppCompatTextView businessRatioTv;
    @BindView(R.id.buyerRatioTv)
    AppCompatTextView buyerRatioTv;
    @BindView(R.id.awardRatioTv)
    AppCompatTextView awardRatioTv;
    @BindView(R.id.businessRatioValueTv)
    AppCompatTextView businessRatioValueTv;
    @BindView(R.id.buyerRatioValueTv)
    AppCompatTextView buyerRatioValueTv;
    @BindView(R.id.awardRatioValueTv)
    AppCompatTextView awardRatioValueTv;
    @BindView(R.id.myMgpStoreTv)
    AppCompatTextView myMgpStoreTv;
    @BindView(R.id.merLayout3)
    ConstraintLayout merLayout3;
    @BindView(R.id.storeMsgTv)
    AppCompatTextView storeMsgTv;
    @BindView(R.id.recruitmentTv)
    AppCompatTextView recruitmentTv;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private StoreIncomeBean storeIncomeBean;
    private String walletAddress;
    private int mOffset = 0;
    private int mScrollY = 0;
    private int scrolly;
    private int lastScrollY = 0;
    private int h;// = SmartUtil.dp2px(100);
    private int color;// = ContextCompat.getColor(getActivity(), R.color.durban_White) & 0x00ffffff;
    private boolean isMer;
    private int isBindUsdt = -1;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.transparentColor));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chainstore_center, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        h = SmartUtil.dp2px(100);
        color = ContextCompat.getColor(getActivity(), R.color.durban_White) & 0x00ffffff;
        getIncome();
        getIsMer();
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(toolbar);
        userNameTv.setText(walletAddress);
    }

    @Override
    protected void initAction() {
        toolbar.setNavigationOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });
        refreshLayout.setOnMultiListener(new OnMultiListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getIncome();
                getIsMer();
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrolly = scrollY;
                updataStatusBar();
            }
        });
        toolbar.setBackgroundColor(0);
    }

    @OnClick({R.id.unpaidLayout, R.id.toSendGoodsLayout, R.id.receivingLayout, R.id.returnedGoodsLayout, R.id.allOrdersLayout,
            R.id.recruitmentLayout, R.id.addAddressLayout, R.id.commodityManagementTv, R.id.commodityManagementValueTv,
            R.id.ordeCentreTv, R.id.myMgpStoreTv, R.id.totalValueOrderTv, R.id.totalValueOrderValueTv})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        int type = 0;
        boolean isBuyer = true;
        switch (view.getId()) {
            case R.id.recruitmentLayout:
                if (storeIncomeBean != null)
                    if (isMer) {
                        if (storeIncomeBean.getData().getAppUser() == null) {
                            startFragment("RecruitmentFragment", bundle);
                        } else {
                            ToastUtils.showLong(R.string.str_has_housed);
                        }
                    } else {
                        startFragment("WalletListFragment");
                    }
                break;
            case R.id.addAddressLayout:
                startFragment("ReceivingAddressFragment", bundle);
                break;
            case R.id.unpaidLayout:
                //0 全部  1 待付款  2 待发货  3 发货中  4 已收货 5 退款中/已退款 /退款失败
                type = 1;
                isBuyer = true;
                bundle.putInt(EXTRA_ORDER_TYPE, type);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                startFragment("OrderCenterFragment", bundle);
                break;
            case R.id.toSendGoodsLayout:
                type = 3;
                isBuyer = true;
                bundle.putInt(EXTRA_ORDER_TYPE, type);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                startFragment("OrderCenterFragment", bundle);
                break;
            case R.id.receivingLayout:
                type = 4;
                isBuyer = true;
                bundle.putInt(EXTRA_ORDER_TYPE, type);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                startFragment("OrderCenterFragment", bundle);
                break;
            case R.id.returnedGoodsLayout:
                type = 6;
                isBuyer = true;
                bundle.putInt(EXTRA_ORDER_TYPE, type);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                startFragment("OrderCenterFragment", bundle);
                break;
            case R.id.allOrdersLayout:
                type = 0;
                isBuyer = true;
                bundle.putInt(EXTRA_ORDER_TYPE, type);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                startFragment("OrderCenterFragment", bundle);
                break;
            case R.id.commodityManagementValueTv:
            case R.id.commodityManagementTv:
                startFragment("GoodsManagerFragment", bundle);
                break;
            case R.id.ordeCentreValueTv:
            case R.id.ordeCentreTv:
                type = 0;
                isBuyer = false;
                bundle.putInt(EXTRA_ORDER_TYPE, type);
                bundle.putBoolean(EXTRA_IS_BUYER, isBuyer);
                startFragment("OrderCenterFragment", bundle);
                break;
            case R.id.myMgpStoreTv:
                StoreIncomeBean.DataBean.ShopBean shopBean = storeIncomeBean.getData().getShop();
                bundle.putParcelable(EXTRA_STORE, shopBean);
                startFragment("EditStoreFragment", bundle);
                break;
            case R.id.totalValueOrderTv:
            case R.id.totalValueOrderValueTv:
                bundle.putInt("type", 0);
                if (isBindUsdt == 0 || isBindUsdt == 1) {
                    bundle.putInt("isBindUsdt", isBindUsdt);
                    startFragment("MarginFragment", bundle);
                }
                break;
        }
    }

    private void getIncome() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().getIncome(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::incomeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIsMer() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().isMer(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::isMerSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isMerSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MerchantBean merchantBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MerchantBean.class);
            Drawable drawable;
            if (merchantBean.getCode() == 0) {
                if (merchantBean.getData() == 0) {//data返回0可以上传1不能上传
                    isMer = false;
                    merLayout1.setVisibility(View.VISIBLE);
                    merLayout2.setVisibility(View.VISIBLE);
                    merLayout3.setVisibility(View.VISIBLE);
                    userLvTv.setVisibility(View.VISIBLE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    recruitmentTv.setText(getString(R.string.str_wallet_manage));
                    drawable = getResources().getDrawable(R.mipmap.ic_wallet_white);
                    //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                } else if (merchantBean.getData() == 1) {
                    isMer = true;
                    merLayout1.setVisibility(View.GONE);
                    merLayout2.setVisibility(View.GONE);
                    merLayout3.setVisibility(View.GONE);
                    userLvTv.setVisibility(View.GONE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    drawable = getResources().getDrawable(R.mipmap.ic_recruitment);
                    recruitmentTv.setText(getString(R.string.str_merchant_settled));
                } else if (merchantBean.getData() == 2) {
                    isMer = false;
                    merLayout1.setVisibility(View.GONE);
                    merLayout2.setVisibility(View.GONE);
                    merLayout3.setVisibility(View.GONE);
                    userLvTv.setVisibility(View.GONE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    recruitmentTv.setText(getString(R.string.str_wallet_manage));
                    drawable = getResources().getDrawable(R.mipmap.ic_wallet_white);
                } else if (merchantBean.getData() == 3) {
                    isMer = true;
                    merLayout1.setVisibility(View.VISIBLE);
                    merLayout2.setVisibility(View.VISIBLE);
                    merLayout3.setVisibility(View.VISIBLE);
                    userLvTv.setVisibility(View.VISIBLE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    drawable = getResources().getDrawable(R.mipmap.ic_recruitment);
                    recruitmentTv.setText(getString(R.string.str_merchant_settled));
                } else if (merchantBean.getData() == 4) {
                    isMer = true;
                    merLayout1.setVisibility(View.VISIBLE);
                    merLayout2.setVisibility(View.VISIBLE);
                    merLayout3.setVisibility(View.VISIBLE);
                    userLvTv.setVisibility(View.VISIBLE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    recruitmentTv.setText(getString(R.string.str_wallet_manage));
                    drawable = getResources().getDrawable(R.mipmap.ic_wallet_white);
                } else if (merchantBean.getData() == 5) {
                    isMer = true;
                    merLayout1.setVisibility(View.VISIBLE);
                    merLayout2.setVisibility(View.VISIBLE);
                    merLayout3.setVisibility(View.VISIBLE);
                    userLvTv.setVisibility(View.VISIBLE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    drawable = getResources().getDrawable(R.mipmap.ic_recruitment);
                    recruitmentTv.setText(getString(R.string.str_merchant_settled));
                } else if (merchantBean.getData() == 6) {
                    isMer = true;
                    merLayout1.setVisibility(View.VISIBLE);
                    merLayout2.setVisibility(View.VISIBLE);
                    merLayout3.setVisibility(View.VISIBLE);
                    userLvTv.setVisibility(View.VISIBLE);
                    storeMsgTv.setText(merchantBean.getMsg());
                    recruitmentTv.setText(getString(R.string.str_wallet_manage));
                    drawable = getResources().getDrawable(R.mipmap.ic_wallet_white);
                } else {
                    isMer = false;
                    merLayout1.setVisibility(View.GONE);
                    merLayout2.setVisibility(View.GONE);
                    merLayout3.setVisibility(View.GONE);
                    userLvTv.setVisibility(View.GONE);
                    recruitmentTv.setText(getString(R.string.str_wallet_manage));
                    drawable = getResources().getDrawable(R.mipmap.ic_wallet_white);
                    storeMsgTv.setText("");
                    //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                }
            } else {
                isMer = false;
                merLayout1.setVisibility(View.GONE);
                merLayout2.setVisibility(View.GONE);
                merLayout3.setVisibility(View.GONE);
                userLvTv.setVisibility(View.GONE);
                recruitmentTv.setText(getString(R.string.str_wallet_manage));
                storeMsgTv.setText("");
                drawable = getResources().getDrawable(R.mipmap.ic_wallet_white);
                ToastUtils.showLong(merchantBean.getMsg());
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  // left, top, right, bottom
            recruitmentTv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    private void incomeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        refreshLayout.finishRefresh();
        /**
         * "top": {
         *          "proNum": 0, //商品总数
         *          "orderIncome": 0.0, //订单总收益
         *          "totalMoney": 7835.00000000, //总资产
         *          "orderNum": 0//订单数
         *          },
         * "down": {
         *          "income": 0.0, //收益
         *          "waitIncome": 0.0,//待入账
         *          "refundIncome": 0.0//已退款
         *          }
         */if (jsonObject != null) {
            storeIncomeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), StoreIncomeBean.class);
            if (storeIncomeBean.getCode() == 0) {
                StoreIncomeBean.DataBean dataBean = storeIncomeBean.getData();
                StoreIncomeBean.DataBean.DownBean downBean = dataBean.getDown();
                StoreIncomeBean.DataBean.TopBean topBean = dataBean.getTop();
                StoreIncomeBean.DataBean.OrdersBean ordersBean = dataBean.getOrders();
                StoreIncomeBean.DataBean.ShopBean shopBean = dataBean.getShop();

                if (topBean != null) {
                    isBindUsdt = topBean.getIsBindUsdt();
                    String sTotalValue = getString(R.string.str_apply_activation);
                    if (isBindUsdt == 0) {
                        sTotalValue = getString(R.string.str_apply_activation);
                    } else if (isBindUsdt == 1) {
                        sTotalValue = getString(R.string.str_total_value_order);
                    } else if (isBindUsdt == 2) {
                        sTotalValue = getString(R.string.str_cancellationing);
                    }
                    totalValueOrderTv.setText(sTotalValue);
                    totalMoneyTv.setText(topBean.getTotalMoney().stripTrailingZeros().toPlainString());
                    totalValueOrderValueTv.setText(topBean.getOrderIncome());
                    ordeCentreValueTv.setText(topBean.getOrderNum());
                    commodityManagementValueTv.setText(topBean.getProNum());
                }
                if (downBean != null) {
                    hasBookedValueTv.setText(downBean.getIncome().stripTrailingZeros().toPlainString());
                    notBookedTv.setText(String.format(getString(R.string.str_not_booked), downBean.getWaitIncome().stripTrailingZeros().toPlainString()));
                    refundedTv.setText(String.format(getString(R.string.str_refunded), downBean.getRefundIncome().stripTrailingZeros().toPlainString()));
                }
                /**
                 * "all": 14, //全部订单
                 * "prepare": 1,//待发货
                 * "goods": 1,//待收货
                 * "enter": 2,//入账中
                 * "refund": 1,//退款中
                 * "prepay": 5 //待支付
                 */
                if (ordersBean != null) {
                    String allNum = ordersBean.getAll();
                    String prepareNum = ordersBean.getPrepare();
                    String prepayNum = ordersBean.getPrepay();
                    String goodsNum = ordersBean.getGoods();
                    String enterNum = ordersBean.getEnter();
                    String refundNum = ordersBean.getRefund();

                    if (!ObjectUtils.isEmpty(prepayNum) && !ObjectUtils.equals("0", prepayNum)) {//入账中
                        redBadgeTv1.setText(prepayNum);
                        redBadgeTv1.setVisibility(View.VISIBLE);
                    } else {
                        redBadgeTv1.setVisibility(View.GONE);
                    }
//                    if (!ObjectUtils.isEmpty(allNum) && !ObjectUtils.equals("0", allNum)) {//全部订单
//                        redBadgeTv5.setText(allNum);
//                        redBadgeTv5.setVisibility(View.VISIBLE);
//                    } else {
//                        redBadgeTv5.setVisibility(View.GONE);
//                    }
                    if (!ObjectUtils.isEmpty(prepareNum) && !ObjectUtils.equals("0", prepareNum)) {//待发货
                        redBadgeTv2.setText(prepareNum);
                        redBadgeTv2.setVisibility(View.VISIBLE);
                    } else {
                        redBadgeTv2.setVisibility(View.GONE);
                    }
                    if (!ObjectUtils.isEmpty(prepayNum) && !ObjectUtils.equals("0", prepayNum)) {//待支付

                    } else {

                    }
                    if (!ObjectUtils.isEmpty(goodsNum) && !ObjectUtils.equals("0", goodsNum)) {//待收货
                        redBadgeTv3.setText(goodsNum);
                        redBadgeTv3.setVisibility(View.VISIBLE);
                    } else {
                        redBadgeTv3.setVisibility(View.GONE);
                    }

                    if (!ObjectUtils.isEmpty(refundNum) && !ObjectUtils.equals("0", refundNum)) {//退款中
                        redBadgeTv4.setText(refundNum);
                        redBadgeTv4.setVisibility(View.VISIBLE);
                    } else {
                        redBadgeTv4.setVisibility(View.GONE);
                    }
                }
                if (shopBean != null) {
                    goodsPicIv.setVisibility(View.VISIBLE);
                    goodsNameIv.setVisibility(View.VISIBLE);
                    if (ObjectUtils.isEmpty(shopBean.getImgs())) {
                        Glide.with(getActivity())
                                .load(shopBean.getImgs().get(0))
                                .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                                .into(goodsPicIv);
                    }
                    goodsNameIv.setText(ObjectUtils.isEmpty(shopBean.getName()) ? "" : shopBean.getName());
                    int status = ObjectUtils.isEmpty(shopBean.getStatus()) ? 0 : shopBean.getStatus();
                    switch (status) {//0:未开通;1:运营中;2:待审核;3:审核失败
                        case 0://未开通
                            storeStatusTv.setText(getString(R.string.str_not_available));
                            break;
                        case 1://运营中
                            storeStatusTv.setText(getString(R.string.str_operating));
                            break;
                        case 2://待审核
                            storeStatusTv.setText(getString(R.string.str_check_pending));
                            break;
                        case 3://审核失败
                            storeStatusTv.setText(getString(R.string.str_audit_failure));
                            break;
                        default:
                            break;
                    }
                    //"storePro": 0.2500,//商家比例
                    //"buyerPro": 0.3300,//买家比例
                    //"rewardPro": 0.4200,//奖励比例
                    BigDecimal storeProDecimal = ObjectUtils.isEmpty(shopBean.getStorePro()) ? BigDecimal.ZERO : shopBean.getStorePro();
                    BigDecimal buyerProDecimal = ObjectUtils.isEmpty(shopBean.getBuyerPro()) ? BigDecimal.ZERO : shopBean.getBuyerPro();
                    BigDecimal rewardProDecimal = ObjectUtils.isEmpty(shopBean.getRewardPro()) ? BigDecimal.ZERO : shopBean.getRewardPro();
                    businessRatioValueTv.setText(storeProDecimal.multiply(percent).setScale(2).toPlainString() + "%");
                    buyerRatioValueTv.setText(buyerProDecimal.multiply(percent).setScale(2).toPlainString() + "%");
                    awardRatioValueTv.setText(rewardProDecimal.multiply(percent).setScale(2).toPlainString() + "%");
                } else {
                    goodsPicIv.setVisibility(View.GONE);
                    goodsNameIv.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showLong(storeIncomeBean.getMsg());
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
        updataStatusBar();
        getIncome();
    }

    private void updataStatusBar() {
        if (lastScrollY < h) {
            scrolly = Math.min(h, scrolly);
            mScrollY = scrolly > h ? h : scrolly;
//                    buttonBarLayout.setAlpha(1f * mScrollY / h);
            toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
            BarUtils.setStatusBarColor(getBaseFragmentActivity(), ((255 * mScrollY / h) << 24) | color);
            parallax.setTranslationY(mOffset - mScrollY);
        }
        lastScrollY = scrolly;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();
    }
}
