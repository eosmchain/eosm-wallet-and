package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ContactInfoBean;
import com.token.mangowallet.bean.PayInfoBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.PayInfoAdapter;
import com.token.mangowallet.utils.NRSAUtils;

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

public class SetupPaymentFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private List<PayInfoBean.DataBean> payInfoList = new ArrayList<>();
    private PayInfoAdapter payInfoAdapter;
    private ContactInfoBean.DataBean dataBean;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_set_payment_term, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        dataBean = bundle.getParcelable("ContactInfoBean");
        getPayInfoList();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_payment_term);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        Button rightBtn = topbar.addRightTextButton(R.string.str_add, R.id.topbar_right_change_button);
        rightBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue));
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPaymentTermPop();
            }
        });
        payInfoAdapter = new PayInfoAdapter(payInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(payInfoAdapter);
    }

    @Override
    protected void initAction() {
        payInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PayInfoBean.DataBean payInfoBean = payInfoList.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
//                bundle.putParcelable("PayInfoBean", payInfoBean);
//                bundle.putInt("payId", payInfoBean.getPayId());
//                bundle.putBoolean("isEdit", true);
//                startFragment("AddPaymentFragment", bundle);
                toAddPaymentFragment(payInfoBean.getPayId(), payInfoBean, true);
            }
        });
    }

    /**
     * 获取收款方式
     */
    private void getPayInfoList() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getPayInfoList(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPayInfoSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPayInfoSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        payInfoList.clear();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            PayInfoBean payInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayInfoBean.class);
            if (payInfoBean != null) {
                if (payInfoBean.getCode() == 0) {
                    if (CollectionUtils.isNotEmpty(payInfoBean.getData())) {
                        payInfoList.addAll(payInfoBean.getData());
                    }
                } else {
                    ToastUtils.showLong(payInfoBean.getMsg());
                }
            }
        }
        payInfoAdapter.notifyDataSetChanged();
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    private void showAddPaymentTermPop() {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
        builder.setGravityCenter(false)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .setTitle(getString(R.string.str_add_payment_term))
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
//                        bundle.putParcelable("PayInfoBean", null);
//                        //1、银行卡；2、微信支付；3、支付宝
//                        bundle.putInt("payId", position + 1);
//                        startFragment("AddPaymentFragment", bundle);
                        toAddPaymentFragment(position + 1, null, false);
                    }
                });
        builder.addItem(ContextCompat.getDrawable(getContext(), R.mipmap.ic_bank_card), getString(R.string.str_bank_card));
        builder.addItem(ContextCompat.getDrawable(getContext(), R.mipmap.ic_wechat), getString(R.string.str_wechat_pay));
        builder.addItem(ContextCompat.getDrawable(getContext(), R.mipmap.ic_alipay), getString(R.string.str_alipay));
        builder.addItem(ContextCompat.getDrawable(getContext(), R.mipmap.ic_usdt_20), getString(R.string.str_usdt_erc20));
        builder.addItem(ContextCompat.getDrawable(getContext(), R.mipmap.ic_usdt_20), getString(R.string.str_usdt_trc20));
        builder.build().show();
    }

    private void toAddPaymentFragment(int payId, PayInfoBean.DataBean payInfoBean, boolean isEdit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        bundle.putParcelable("PayInfoBean", payInfoBean);
        bundle.putParcelable("ContactInfoBean", dataBean);
        //1、银行卡；2、微信支付；3、支付宝
        bundle.putInt("payId", payId);
        bundle.putBoolean("isEdit", isEdit);
        startFragment("AddPaymentFragment", bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPayInfoList();
    }
}
