package com.token.mangowallet.ui.fragment.mgp_deal;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SellMGPFragment extends BaseFragment {

    @BindView(R.id.mgpPriceTv)
    AppCompatTextView mgpPriceTv;
    @BindView(R.id.unitPriceTv)
    AppCompatTextView unitPriceTv;
    @BindView(R.id.subtractBtn)
    QMUIRoundButton subtractBtn;
    @BindView(R.id.numTv)
    AppCompatTextView numTv;
    @BindView(R.id.additionBtn)
    QMUIRoundButton additionBtn;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.quantityTv)
    AppCompatTextView quantityTv;
    @BindView(R.id.quantityEt)
    AppCompatEditText quantityEt;
    @BindView(R.id.totalTv)
    AppCompatTextView totalTv;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.minSaleAmountTv)
    AppCompatTextView minSaleAmountTv;
    @BindView(R.id.minSaleAmountEt)
    AppCompatEditText minSaleAmountEt;
    @BindView(R.id.fiatMoneyUnitTv)
    AppCompatTextView fiatMoneyUnitTv;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.totalMoneyTv)
    AppCompatTextView totalMoneyTv;
    @BindView(R.id.totalMoneyValueTv)
    AppCompatTextView totalMoneyValueTv;
    @BindView(R.id.fiatMoneyUnitTv2)
    AppCompatTextView fiatMoneyUnitTv2;
    private Unbinder unbinder;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mgp_sell, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {

    }


}
