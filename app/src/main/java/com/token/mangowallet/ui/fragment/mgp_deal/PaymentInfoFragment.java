package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.Group;
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

public class PaymentInfoFragment extends BaseFragment {

    @BindView(R.id.paymentTilteTv)
    AppCompatTextView paymentTilteTv;
    @BindView(R.id.userNameTv)
    AppCompatTextView userNameTv;
    @BindView(R.id.userNameValTv)
    AppCompatTextView userNameValTv;
    @BindView(R.id.paymentIDTv)
    AppCompatTextView paymentIDTv;
    @BindView(R.id.paymentIDValTv)
    AppCompatTextView paymentIDValTv;
    @BindView(R.id.bankNameTv)
    AppCompatTextView bankNameTv;
    @BindView(R.id.bankNameValTv)
    AppCompatTextView bankNameValTv;
    @BindView(R.id.openSubbranchTv)
    AppCompatTextView openSubbranchTv;
    @BindView(R.id.openSubbranchValTv)
    AppCompatTextView openSubbranchValTv;
    @BindView(R.id.bankCardGroup)
    Group bankCardGroup;
    @BindView(R.id.alipayGroup)
    Group alipayGroup;
    @BindView(R.id.paymentCodeIv)
    AppCompatImageView paymentCodeIv;
    @BindView(R.id.seeLargerPictureTv)
    AppCompatTextView seeLargerPictureTv;
    @BindView(R.id.barrier1)
    Barrier barrier1;
    @BindView(R.id.transferTipTv)
    AppCompatTextView transferTipTv;
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
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.view_payment_info, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        bundle = getArguments();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {

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

    @OnClick({R.id.userNameValTv, R.id.paymentIDValTv, R.id.bankNameValTv, R.id.openSubbranchValTv, R.id.paymentCodeIv, R.id.seeLargerPictureTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userNameValTv:
                break;
            case R.id.paymentIDValTv:
                break;
            case R.id.bankNameValTv:
                break;
            case R.id.openSubbranchValTv:
                break;
            case R.id.paymentCodeIv:
                break;
            case R.id.seeLargerPictureTv:
                break;
        }
    }
}
