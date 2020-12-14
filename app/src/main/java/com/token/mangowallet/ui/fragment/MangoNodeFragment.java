package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.NodeIndexBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.NRSAUtils;

import java.math.RoundingMode;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class MangoNodeFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.text1)
    AppCompatTextView text1;
    @BindView(R.id.text2)
    AppCompatTextView text2;
    @BindView(R.id.text3)
    AppCompatTextView text3;
    @BindView(R.id.text4)
    AppCompatTextView text4;
    @BindView(R.id.text5)
    AppCompatTextView text5;
    @BindView(R.id.totalMortgageVolumeTv)
    AppCompatTextView totalMortgageVolumeTv;
    @BindView(R.id.accumulativeDestructionTv)
    AppCompatTextView accumulativeDestructionTv;
    @BindView(R.id.motivateIndexTv)
    AppCompatTextView motivateIndexTv;
    @BindView(R.id.yesterdayIncentiveTv)
    AppCompatTextView yesterdayIncentiveTv;
    @BindView(R.id.yesterdayOrderValueTv)
    AppCompatTextView yesterdayOrderValueTv;
    @BindView(R.id.layout3titleLayout)
    RelativeLayout layout3titleLayout;
    @BindView(R.id.layout3)
    QMUILinearLayout layout3;
    @BindView(R.id.layout4)
    QMUILinearLayout layout4;
    @BindView(R.id.bottomLayout)
    QMUILinearLayout bottomLayout;
    @BindView(R.id.layout1)
    QMUILinearLayout layout1;
    @BindView(R.id.layout2)
    QMUILinearLayout layout2;

    private Unbinder unbinder;
    private int mRadius;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private String privateKey;
    private NodeIndexBean nodeIndexBean;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mortgage_big, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        mRadius = QMUIDisplayHelper.dp2px(getActivity(), 10);
        privateKey = mangoWallet.getPrivateKey();
        walletAddress = mangoWallet.getWalletAddress();
    }

    @Override
    public void initView() {
        topBar.setTitle(getString(R.string.str_mango_node_t));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        layout1.setRadius(mRadius);
        layout2.setRadius(mRadius);
        text1.setText(getString(R.string.str_yesterday_incentive));
        text2.setText(getString(R.string.str_network_node));
        layout3.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        text3.setText(getString(R.string.str_yesterday_performance));
//        text4.setText(getString(R.string.str_yesterday_performance));
        text5.setText(getString(R.string.str_node_level));
        layout4.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        yesterdayOrderValueTv.setVisibility(View.GONE);

        updataView();
    }

    @Override
    protected void initAction() {

    }

    private void updataView() {
        if (nodeIndexBean != null) {
            NodeIndexBean.DataBean dataBean = nodeIndexBean.getData();
            if (dataBean != null) {
                totalMortgageVolumeTv.setText(ObjectUtils.isEmpty(dataBean.getYesterdayMoney()) ? "0.0000" : dataBean.getYesterdayMoney());
                accumulativeDestructionTv.setText(dataBean.getNodeNum() + "");
                text4.setText(ObjectUtils.isEmpty(dataBean.getTeamMoney()) ? "0.0000" : dataBean.getTeamMoney());
                yesterdayIncentiveTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(dataBean.getTeamValue()) ? "0.0000" : dataBean.getTeamValue(), 2, RoundingMode.FLOOR));

                motivateIndexTv.setText(ObjectUtils.isEmpty(dataBean.getNodeLevel()) ? "P0" : dataBean.getNodeLevel());
            }
        } else {
            totalMortgageVolumeTv.setText("0.0000");
            accumulativeDestructionTv.setText("0");
            text4.setText("0.0000");
            yesterdayIncentiveTv.setText(BalanceUtils.currencyToBase("0.00", 2, RoundingMode.FLOOR));
            motivateIndexTv.setText("P0");
        }
    }

    public void lazyLoad() {
        try {
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            String jsonData2 = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(jsonData2);

            NetWorkManager.getRequest().getNodeIndex(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::mortgageAssociationSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mortgageAssociationSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            nodeIndexBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), NodeIndexBean.class);
            if (nodeIndexBean.getCode() == 0) {
                NodeIndexBean.DataBean dataBean = nodeIndexBean.getData();
                if (dataBean != null) {
                    updataView();
                }
            } else {
                ToastUtils.showShort(nodeIndexBean.getMsg());
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
        lazyLoad();
    }
}
