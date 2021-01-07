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

public class ContactMethodFragment extends BaseFragment {


    @BindView(R.id.contactTilteTv)
    AppCompatTextView contactTilteTv;
    @BindView(R.id.mgpAccountTv)
    AppCompatTextView mgpAccountTv;
    @BindView(R.id.mgpAccountValTv)
    AppCompatTextView mgpAccountValTv;
    @BindView(R.id.emailAddressTv)
    AppCompatTextView emailAddressTv;
    @BindView(R.id.emailAddressValTv)
    AppCompatTextView emailAddressValTv;
    @BindView(R.id.phoneTv)
    AppCompatTextView phoneTv;
    @BindView(R.id.phoneValTv)
    AppCompatTextView phoneValTv;
    @BindView(R.id.wechatNumberTv)
    AppCompatTextView wechatNumberTv;
    @BindView(R.id.wechatNumberValTv)
    AppCompatTextView wechatNumberValTv;
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
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.view_deal_contact, null);
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


    @OnClick({R.id.mgpAccountValTv, R.id.emailAddressValTv, R.id.phoneValTv, R.id.wechatNumberValTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mgpAccountValTv:
                break;
            case R.id.emailAddressValTv:
                break;
            case R.id.phoneValTv:
                break;
            case R.id.wechatNumberValTv:
                break;
        }
    }
}
