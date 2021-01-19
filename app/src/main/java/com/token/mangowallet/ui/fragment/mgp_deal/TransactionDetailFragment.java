package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.db.MangoWallet;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private String amountPaid = "0";
    private DealsOrderBean.RowsBean mRowsBean;

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
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        amountPaid = bundle.getString("amountPaid", amountPaid);
        mRowsBean = bundle.getParcelable("RowsBean");
        updataView();
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

    BigDecimal dealQuantityDecimal = BigDecimal.ZERO;// 购买MGP的单价价格
    BigDecimal orderPriceDecimal = BigDecimal.ZERO;// 本次交易数量
    BigDecimal totalPricesDecimal = BigDecimal.ZERO;// 总价

    private void updataView() {
        if (mRowsBean != null) {
            String deal_quantity = ObjectUtils.isEmpty(mRowsBean.getDeal_quantity()) ? "0.0000 MGP" : mRowsBean.getDeal_quantity();
            String order_price = ObjectUtils.isEmpty(mRowsBean.getOrder_price()) ? "0.00 CNY" : mRowsBean.getOrder_price();

            dealQuantityDecimal = new BigDecimal(deal_quantity.split(" ")[0]);
            orderPriceDecimal = new BigDecimal(order_price.split(" ")[0]);
            totalPricesDecimal = dealQuantityDecimal.multiply(orderPriceDecimal);
            totalPricesValTv.setText("¥" + totalPricesDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString());
            priceValTv.setText("¥" + orderPriceDecimal.toPlainString());
            quantityValTv.setText(deal_quantity);//pay_type
            orderNumberValTv.setText(ObjectUtils.isEmpty(mRowsBean.getOrder_sn()) ? "" : mRowsBean.getOrder_sn());

            String tabText = null;
            if (mRowsBean.getPay_type() == 1) {
                tabText = getString(R.string.str_bank_card);
            } else if (mRowsBean.getPay_type() == 2) {
                tabText = getString(R.string.str_wechat_pay);
            } else if (mRowsBean.getPay_type() == 3) {
                tabText = getString(R.string.str_alipay);
            }
            if (ObjectUtils.isNotEmpty(tabText)) {
                paymentMethodValTv.setText(tabText);
                paymentMethodTv.setVisibility(View.VISIBLE);
                paymentMethodValTv.setVisibility(View.VISIBLE);
            } else {
                paymentMethodTv.setVisibility(View.GONE);
                paymentMethodValTv.setVisibility(View.GONE);
            }
        }
    }
}
