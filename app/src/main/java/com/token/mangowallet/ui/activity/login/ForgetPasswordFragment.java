package com.token.mangowallet.ui.activity.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgetPasswordFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.retrievePhoneBtn)
    AppCompatButton retrievePhoneBtn;
    @BindView(R.id.retrieveEmailBtn)
    QMUIRoundButton retrieveEmailBtn;

    private Unbinder unbinder;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
//        homeViewModel = createViewModel(this, HomeViewModel.class);
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_forget_password, null);
        unbinder = ButterKnife.bind(this, root);
        initData();
        initView();
        initAction();
        return root;
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_forget_password);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initAction() {

    }


    @OnClick({R.id.retrievePhoneBtn, R.id.retrieveEmailBtn})
    public void onViewClicked(View view) {
        ResetPasswordsFragment fragment = new ResetPasswordsFragment();
        Bundle bundle = new Bundle();
        boolean isEmail = false;
        switch (view.getId()) {
            case R.id.retrievePhoneBtn:
                isEmail = false;
                bundle.putBoolean("isEmail", isEmail);
                fragment.setArguments(bundle);
                this.startFragment(fragment);
                break;
            case R.id.retrieveEmailBtn:
                isEmail = true;
                bundle.putBoolean("isEmail", isEmail);
                fragment.setArguments(bundle);
                this.startFragment(fragment);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
