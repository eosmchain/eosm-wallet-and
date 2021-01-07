package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class TransactionDetailFragment extends BaseFragment {

    @BindView(R.id.transactionDetailTitleTv)
    AppCompatTextView transactionDetailTitleTv;
    @BindView(R.id.totalPricesTv)
    AppCompatTextView totalPricesTv;
    @BindView(R.id.totalPricesValTv)
    AppCompatTextView totalPricesValTv;
    @BindView(R.id.priceTv)
    AppCompatTextView priceTv;
    @BindView(R.id.priceValTv)
    AppCompatTextView priceValTv;
    @BindView(R.id.quantityTv)
    AppCompatTextView quantityTv;
    @BindView(R.id.quantityValTv)
    AppCompatTextView quantityValTv;
    @BindView(R.id.orderNumberTv)
    AppCompatTextView orderNumberTv;
    @BindView(R.id.orderNumberValTv)
    AppCompatTextView orderNumberValTv;
    @BindView(R.id.paymentMethodTv)
    AppCompatTextView paymentMethodTv;
    @BindView(R.id.paymentMethodValTv)
    AppCompatTextView paymentMethodValTv;
    @BindView(R.id.putCoinHashTv)
    AppCompatTextView putCoinHashTv;
    @BindView(R.id.putCoinHashValTv)
    AppCompatTextView putCoinHashValTv;
    private Unbinder unbinder;
    private Bundle bundle;
    private MangoWallet mangoWallet;
    private int index = 0;
    private String num = "0";
    private String amountPaid = "0";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.view_transaction_detail, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        index = bundle.getInt("index", index);
        num = bundle.getString("num", num);
        amountPaid = bundle.getString("amountPaid", amountPaid);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {

    }


    @OnClick({R.id.totalPricesValTv, R.id.priceValTv, R.id.quantityValTv, R.id.orderNumberValTv, R.id.paymentMethodValTv, R.id.putCoinHashValTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.totalPricesValTv:
                break;
            case R.id.priceValTv:
                break;
            case R.id.quantityValTv:
                break;
            case R.id.orderNumberValTv:
                break;
            case R.id.paymentMethodValTv:
                break;
            case R.id.putCoinHashValTv:
                break;
        }
    }
}
