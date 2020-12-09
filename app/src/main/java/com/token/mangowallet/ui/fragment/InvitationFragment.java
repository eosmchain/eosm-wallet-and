package com.token.mangowallet.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.FindBean;
import com.token.mangowallet.ui.activity.pinyin.CountryCodeActivity;
import com.token.mangowallet.utils.ClipboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class InvitationFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.invitationCodeTv2)
    AppCompatTextView invitationCodeTv2;
    @BindView(R.id.qrCodeIv)
    AppCompatImageView qrCodeIv;
    @BindView(R.id.invitationNumTv)
    AppCompatTextView invitationNumTv;
    @BindView(R.id.copyinvitationCodeBtn)
    QMUIRoundButton copyinvitationCodeBtn;
    @BindView(R.id.copyinvitationUrlBtn)
    QMUIRoundButton copyinvitationUrlBtn;
    @BindView(R.id.shareRulesMsgTv)
    AppCompatTextView shareRulesMsgTv;
    @BindView(R.id.shareRulesTv)
    AppCompatTextView shareRulesTv;
    @BindView(R.id.invitationCodeLayout)
    QMUIConstraintLayout invitationCodeLayout;
    @BindView(R.id.qrCodeLayout)
    QMUIConstraintLayout qrCodeLayout;

    private Unbinder unbinder;
    private Bitmap qrBitmap;
    private Bitmap logoBitmap;
    private FindBean.DataBean dataBean;
    private String lnvitationCode;
    private String download;
    private int mRadius;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_invitation, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        dataBean = bundle.getParcelable("DataBean");
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 15);
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_invitation_code));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        invitationCodeLayout.setRadius(mRadius);
        qrCodeLayout.setRadius(mRadius);
        if (dataBean != null) {
            lnvitationCode = ObjectUtils.isEmpty(dataBean.getLnvitationCode()) ? "" : dataBean.getLnvitationCode();
            download = ObjectUtils.isEmpty(dataBean.getDownload()) ? "" : dataBean.getDownload();

            logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mgp, null);
            qrBitmap = QRCodeEncoder.syncEncodeQRCode(download, SizeUtils.dp2px(150),
                    ContextCompat.getColor(getActivity(), R.color.app_color_common_black), logoBitmap);
            invitationCodeTv2.setText(lnvitationCode);
            qrCodeIv.setImageBitmap(qrBitmap);
            invitationNumTv.setText(String.format(getString(R.string.str_invited_num), ObjectUtils.isEmpty(dataBean.getShareCount()) ? "" : dataBean.getShareCount() + ""));
            shareRulesTv.setText(ObjectUtils.isEmpty(dataBean.getShareTitle()) ? "" : dataBean.getShareTitle());
            shareRulesMsgTv.setText(ObjectUtils.isEmpty(dataBean.getShareContent()) ? "" : dataBean.getShareContent());
        }
    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.invitationCodeLayout, R.id.copyinvitationCodeBtn, R.id.copyinvitationUrlBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.invitationCodeLayout:
            case R.id.copyinvitationCodeBtn:
                ClipboardUtils.copyText(lnvitationCode);
                ToastUtils.showLong("\"" + lnvitationCode + "\"" + getString(R.string.str_Copied));
                break;
            case R.id.copyinvitationUrlBtn:
                ClipboardUtils.copyText(download);
                ToastUtils.showLong("\"" + download + "\"" + getString(R.string.str_Copied));
                break;
        }
    }
}
