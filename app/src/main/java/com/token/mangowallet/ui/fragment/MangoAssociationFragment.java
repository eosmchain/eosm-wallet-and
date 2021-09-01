package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MortgageAssociationBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.AssociationAdapter;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class MangoAssociationFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.stimulateTv)
    AppCompatTextView stimulateTv;
    @BindView(R.id.layout1)
    QMUILinearLayout layout1;
    @BindView(R.id.sharedStimulateTv)
    AppCompatTextView sharedStimulateTv;
    @BindView(R.id.layout2)
    QMUILinearLayout layout2;
    @BindView(R.id.teamStimulateTv)
    AppCompatTextView teamStimulateTv;
    @BindView(R.id.layout3)
    QMUILinearLayout layout3;
    @BindView(R.id.nodeStimulateTv)
    AppCompatTextView nodeStimulateTv;
    @BindView(R.id.layout4)
    QMUILinearLayout layout4;
    @BindView(R.id.shareIndexTv)
    AppCompatTextView shareIndexTv;
    @BindView(R.id.hierarchyIndexTv)
    AppCompatTextView hierarchyIndexTv;
    @BindView(R.id.lightNodeTv)
    AppCompatTextView lightNodeTv;
    @BindView(R.id.currencyValueTv)
    AppCompatTextView currencyValueTv;
    @BindView(R.id.marketValueTv)
    AppCompatTextView marketValueTv;
    @BindView(R.id.layout5)
    QMUILinearLayout layout5;
    @BindView(R.id.midSearchView)
    SearchView midSearchView;
    @BindView(R.id.contributionListTv)
    AppCompatTextView contributionListTv;
    @BindView(R.id.mango_recycler)
    RecyclerView mangoRecycler;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String privateKey;
    private String walletAddress;
    private String code;
    private String action;
    private MortgageAssociationBean mortgageAssociationBean;
    private int mRadius;
    private AssociationAdapter associationAdapter;
    private String mQuery;
    private List<MortgageAssociationBean.DataBean.TeamListBean> originalList;
    private List<MortgageAssociationBean.DataBean.TeamListBean> newDataList = new ArrayList<>();
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mango_association, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        privateKey = mangoWallet.getPrivateKey();
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        mRadius = QMUIDisplayHelper.dp2px(getActivity(), 10);
    }

    public void lazyLoad() {
        try {
            Map mapOrderIndex = MapUtils.newHashMap();
            mapOrderIndex.put("address", walletAddress);
            String jsonData2 = GsonUtils.toJson(mapOrderIndex);
            String content = NRSAUtils.encrypt(jsonData2);
            NetWorkManager.getRequest().getMortgageAssociation(content)
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
            mortgageAssociationBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MortgageAssociationBean.class);
            if (mortgageAssociationBean.getCode() == 0) {
                updataView();
            } else {
                ToastUtils.showShort(mortgageAssociationBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    @Override
    public void initView() {
        topBar.setTitle(StringUtils.getString(R.string.str_mango_association_t));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        layout1.setRadius(mRadius);
        layout2.setRadius(mRadius);
        layout3.setRadius(mRadius);
        layout4.setRadius(mRadius);
        layout5.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        associationAdapter = new AssociationAdapter(newDataList, walletType);
        mangoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mangoRecycler.setAdapter(associationAdapter);

        // 设置搜索文本监听
        midSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!ObjectUtils.isEmpty(originalList)) {
                    for (int i = 0; i < originalList.size(); i++) {
                        MortgageAssociationBean.DataBean.TeamListBean teamListBean = originalList.get(i);
                        if (ObjectUtils.equals(mQuery, teamListBean.getCode())) {
                            newDataList.clear();
                            newDataList.add(teamListBean);
                        }
                    }
                }
                return false;
            }

            // 当搜索内容改变时触发该方法，时刻监听输入搜索框的值
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!ObjectUtils.isEmpty(newText)) {
                    mQuery = newText;//  newText输入搜索框的值
                } else {
                    if (originalList != null && newDataList != null) {
                        newDataList.clear();
                        newDataList.addAll(originalList);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void initAction() {

    }

    private void updataView() {
        if (mortgageAssociationBean != null) {
            MortgageAssociationBean.DataBean dataBean = mortgageAssociationBean.getData();
            if (dataBean != null) {
                stimulateTv.setText(ObjectUtils.isEmpty(dataBean.getYesterdayMoney()) ? "0.0000" : dataBean.getYesterdayMoney());
                sharedStimulateTv.setText(ObjectUtils.isEmpty(dataBean.getYesterdayPushMoney()) ? "0.0000" : dataBean.getYesterdayPushMoney());
                teamStimulateTv.setText(ObjectUtils.isEmpty(dataBean.getYesterdayTeamMoney()) ? "0.0000" : dataBean.getYesterdayTeamMoney());
                nodeStimulateTv.setText(ObjectUtils.isEmpty(dataBean.getYesterdayLightNodeMoney()) ? "0.0000" : dataBean.getYesterdayLightNodeMoney());

                shareIndexTv.setText(ObjectUtils.isEmpty(dataBean.getMyPushPro()) ? "0%" : dataBean.getMyPushPro());
                hierarchyIndexTv.setText(ObjectUtils.isEmpty(dataBean.getMyFloor()) ? "0" : dataBean.getMyFloor() + "");
                lightNodeTv.setText(ObjectUtils.isEmpty(dataBean.getLightNodeFlag()) ? getString(R.string.str_no) : (dataBean.getLightNodeFlag() == 1 ? getString(R.string.str_yes) : getString(R.string.str_no)));
                currencyValueTv.setText((ObjectUtils.isEmpty(dataBean.getTeamNum()) ? "0" : dataBean.getTeamNum()) + "MGP");
                marketValueTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(dataBean.getTeamValue()) ? "0" : dataBean.getTeamValue(), 2, RoundingMode.FLOOR));

                originalList = dataBean.getTeamList();
                newDataList.clear();
                newDataList.addAll(originalList);
                associationAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

}
