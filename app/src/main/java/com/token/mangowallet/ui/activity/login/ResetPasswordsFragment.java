package com.token.mangowallet.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.ui.activity.pinyin.CountryBean;
import com.token.mangowallet.ui.activity.pinyin.CountryCodeActivity;
import com.token.mangowallet.utils.DisposUtil;
import com.token.mangowallet.utils.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class ResetPasswordsFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.selectTv)
    AppCompatTextView selectTv;
    @BindView(R.id.verificationCodeTv)
    AppCompatTextView verificationCodeTv;
    @BindView(R.id.loginBtn)
    AppCompatButton loginBtn;
    @BindView(R.id.nameEt)
    AppCompatEditText nameEt;
    @BindView(R.id.verificationCodeEt)
    AppCompatEditText verificationCodeEt;

    private static final int REQUEST_CODE = 1000;
    private Unbinder unbinder;
    private boolean isEmail = false;
    private Disposable timeDis;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
//        homeViewModel = createViewModel(this, HomeViewModel.class);
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reset_passwords, null);
        unbinder = ButterKnife.bind(this, root);

        return root;
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_reset_passwords);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        selectTv.setVisibility(isEmail ? View.GONE : View.VISIBLE);
        nameEt.setHint(isEmail ? R.string.str_email : R.string.str_phonenumber);
        verificationCodeTv.setText(isEmail ? R.string.str_email_verification_code : R.string.str_phonenumber_verification_code);
    }

    @Override
    protected void initData() {
        isEmail = getArguments().getBoolean("isEmail");

    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.selectTv, R.id.verificationCodeTv, R.id.loginBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectTv:
//                startActivityForResult(new Intent(getApplicationContext(), PickActivity.class), 111);
                ActivityUtils.startActivityForResult(this, CountryCodeActivity.class, REQUEST_CODE);
                break;
            case R.id.verificationCodeTv:
                sendCheckCodeSuccess();
                break;
            case R.id.loginBtn:
                break;
        }
    }

    public void sendCheckCodeSuccess() {
        DisposUtil.close(timeDis);
        timeDis = TimeUtil.newTime()
                .subscribe(aLong -> {
                    if (aLong > 0) {
                        verificationCodeTv.setText(aLong + "s");
                    } else {
                        verificationCodeTv.setText(isEmail ? R.string.str_email_verification_code : R.string.str_phonenumber_verification_code);
                        verificationCodeTv.setEnabled(true);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CountryBean country = CountryBean.fromJson(data.getStringExtra("country"));
            selectTv.setText(country.name + " +" + country.code);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DisposUtil.close(timeDis);

    }
}
