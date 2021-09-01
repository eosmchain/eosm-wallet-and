package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.PayInfoUserInfoBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.ClipboardUtils;

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
    private String amountPaid = "0";
    private PayInfoUserInfoBean.DataBean mPayInfoUserInfoBean;

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
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        amountPaid = bundle.getString("amountPaid", amountPaid);
        mPayInfoUserInfoBean = bundle.getParcelable("PayInfoUserInfoBean");
        updataView();
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
                if (ObjectUtils.isNotEmpty(mgpAccountValTv.getText())) {
                    ClipboardUtils.copyText(mgpAccountValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.emailAddressValTv:
                if (ObjectUtils.isNotEmpty(emailAddressValTv.getText())) {
                    ClipboardUtils.copyText(emailAddressValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.phoneValTv:
                if (ObjectUtils.isNotEmpty(phoneValTv.getText())) {
                    ClipboardUtils.copyText(phoneValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.wechatNumberValTv:
                if (ObjectUtils.isNotEmpty(wechatNumberValTv.getText())) {
                    ClipboardUtils.copyText(wechatNumberValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
        }
    }

    private void updataView() {
        if (mPayInfoUserInfoBean != null) {
            PayInfoUserInfoBean.DataBean.UserInfoBean userInfoBean = mPayInfoUserInfoBean.getUserInfo();
            if (userInfoBean != null) {
                mgpAccountValTv.setText(ObjectUtils.isEmpty(userInfoBean.getMgpName()) ? "" : userInfoBean.getMgpName());
                emailAddressValTv.setText(ObjectUtils.isEmpty(userInfoBean.getMail()) ? "" : userInfoBean.getMail());
                phoneValTv.setText(ObjectUtils.isEmpty(userInfoBean.getPhone()) ? "" : userInfoBean.getPhone());
                wechatNumberValTv.setText(ObjectUtils.isEmpty(userInfoBean.getWeixin()) ? "" : userInfoBean.getWeixin());
            }
        }
    }
}
