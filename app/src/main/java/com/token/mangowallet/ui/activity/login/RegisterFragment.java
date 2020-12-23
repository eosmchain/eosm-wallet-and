package com.token.mangowallet.ui.activity.login;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.helloTv)
    AppCompatTextView helloTv;
    @BindView(R.id.welcomeTv)
    AppCompatTextView welcomeTv;
    @BindView(R.id.usernameEt)
    AppCompatEditText usernameEt;
    @BindView(R.id.passwordEt)
    AppCompatEditText passwordEt;
    @BindView(R.id.affirmPasswordEt)
    AppCompatEditText affirmPasswordEt;
    @BindView(R.id.completeBtn)
    AppCompatButton completeBtn;
    @BindView(R.id.container)
    ConstraintLayout container;
    private Unbinder unbinder;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
//        homeViewModel = createViewModel(this, HomeViewModel.class);
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.completeBtn)
    public void onViewClicked() {
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_account_registration);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
