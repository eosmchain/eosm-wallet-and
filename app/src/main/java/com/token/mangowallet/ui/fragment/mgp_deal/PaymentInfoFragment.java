package com.token.mangowallet.ui.fragment.mgp_deal;

import android.graphics.drawable.Drawable;
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
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ContactInfoBean;
import com.token.mangowallet.bean.PayInfoBean;
import com.token.mangowallet.bean.PayInfoUserInfoBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.BigImgDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

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
    private String amountPaid = "0";
    private PayInfoUserInfoBean.DataBean.PayInfosBean dataBean;

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
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        amountPaid = bundle.getString("amountPaid", amountPaid);
        dataBean = bundle.getParcelable("PayInfosBean");
        setPayInfo(dataBean);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {

    }

    public void setPayInfo(PayInfoUserInfoBean.DataBean.PayInfosBean dataBean) {
        this.dataBean = dataBean;
        if (dataBean != null) {
            int mipRes = R.mipmap.ic_bank_card;
            String payWayRes = "";
            String payWaypayment = "";
            if (dataBean.getPayId() == 1) {
                mipRes = R.mipmap.ic_bank_card;
                payWayRes = getString(R.string.str_bank_card);
                payWaypayment = getString(R.string.str_bank_id);
                bankCardGroup.setVisibility(View.VISIBLE);
                alipayGroup.setVisibility(View.GONE);
            } else if (dataBean.getPayId() == 2) {
                mipRes = R.mipmap.ic_wechat;
                payWayRes = getString(R.string.str_wechat_pay);
                payWaypayment = getString(R.string.str_wechat_id);
                bankCardGroup.setVisibility(View.GONE);
                alipayGroup.setVisibility(View.VISIBLE);
            } else if (dataBean.getPayId() == 3) {
                mipRes = R.mipmap.ic_alipay;
                payWayRes = getString(R.string.str_alipay);
                payWaypayment = getString(R.string.str_alipay_id);
                bankCardGroup.setVisibility(View.GONE);
                alipayGroup.setVisibility(View.VISIBLE);
            } else if (dataBean.getPayId() == 4) {
                mipRes = R.mipmap.ic_usdt_20;
                payWayRes = getString(R.string.str_usdt_erc20);
                payWaypayment = getString(R.string.str_collection_address);
                userNameTv.setText(getString(R.string.str_chain_type));
                bankCardGroup.setVisibility(View.GONE);
                alipayGroup.setVisibility(View.VISIBLE);
            } else if (dataBean.getPayId() == 5) {
                mipRes = R.mipmap.ic_usdt_20;
                payWayRes = getString(R.string.str_usdt_trc20);
                payWaypayment = getString(R.string.str_collection_address);
                userNameTv.setText(getString(R.string.str_chain_type));
                bankCardGroup.setVisibility(View.GONE);
                alipayGroup.setVisibility(View.VISIBLE);
            }
            Drawable drawable = ContextCompat.getDrawable(getContext(), mipRes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            paymentTilteTv.setCompoundDrawables(drawable, null, null, null);
            paymentTilteTv.setText(payWayRes);
            paymentIDTv.setText(payWaypayment);

            userNameValTv.setText(ObjectUtils.isEmpty(dataBean.getUsername()) ? "" : dataBean.getUsername());
            paymentIDValTv.setText(ObjectUtils.isEmpty(dataBean.getCardNum()) ? "" : dataBean.getCardNum());
            bankNameValTv.setText(ObjectUtils.isEmpty(dataBean.getName()) ? "" : dataBean.getName());
            openSubbranchValTv.setText(ObjectUtils.isEmpty(dataBean.getBranch()) ? "" : dataBean.getBranch());
        }
    }

    @OnClick({R.id.userNameValTv, R.id.paymentIDValTv, R.id.bankNameValTv, R.id.openSubbranchValTv, R.id.paymentCodeIv, R.id.seeLargerPictureTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userNameValTv:
                if (ObjectUtils.isNotEmpty(userNameValTv.getText())) {
                    ClipboardUtils.copyText(userNameValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.paymentIDValTv:
                if (ObjectUtils.isNotEmpty(paymentIDValTv.getText())) {
                    ClipboardUtils.copyText(paymentIDValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.bankNameValTv:
                if (ObjectUtils.isNotEmpty(bankNameValTv.getText())) {
                    ClipboardUtils.copyText(bankNameValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.openSubbranchValTv:
                if (ObjectUtils.isNotEmpty(openSubbranchValTv.getText())) {
                    ClipboardUtils.copyText(openSubbranchValTv.getText().toString());
                    ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                }
                break;
            case R.id.paymentCodeIv:
            case R.id.seeLargerPictureTv:
                showBigImgDialog();
                break;
        }
    }

    private void showBigImgDialog() {
        List<String> qrCodeList = new ArrayList<>();
        if (dataBean != null) {
            if (ObjectUtils.isNotEmpty(dataBean.getQrCode())) {
                qrCodeList.add(dataBean.getQrCode());
                BigImgDialog mBigImgDialog = new BigImgDialog();
                mBigImgDialog.setImgUrls(qrCodeList);
                mBigImgDialog.show(getChildFragmentManager());
            }
        }
    }

}
