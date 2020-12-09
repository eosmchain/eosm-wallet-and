package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class ChangePasswordFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.accountNameEt)
    AppCompatEditText accountNameEt;
    @BindView(R.id.passwordEt)
    AppCompatEditText passwordEt;
    @BindView(R.id.confirmPasswordEt)
    AppCompatEditText confirmPasswordEt;
    @BindView(R.id.promptMessageEt)
    AppCompatEditText promptMessageEt;


    private Unbinder unbinder;
    private MangoWallet mangoWallet;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_password, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
    }

    @Override
    protected void initView() {
        topbar.setTitle(StringUtils.getString(R.string.str_change_password));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        accountNameEt.setText(mangoWallet.getWalletAddress());
        ViewUtils.setEditableEditText(accountNameEt, false);
    }


    @Override
    protected void initAction() {

    }

    @OnClick(R.id.confirmBtn)
    public void onViewClicked() {
        String password = passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();
        String hint = promptMessageEt.getText().toString().trim();
        if (ObjectUtils.isEmpty(password)) {
            ToastUtils.showLong(R.string.str_enter_password);
            return;
        }
        if (password.getBytes().length < 8) {
            ToastUtils.showLong(R.string.str_wallet_password);
            return;
        }
        if (!ObjectUtils.equals(password, confirmPassword)) {
            ToastUtils.showLong(R.string.str_pwd_confirm_input_tips);
            return;
        }
        mangoWallet.setWalletPassword(Md5Utils.md5(password));
        if (!ObjectUtils.isEmpty(hint)) {
            mangoWallet.setHint(hint);
        }
        WalletDaoUtils.mangoWalletDao.update(mangoWallet);
        ToastUtils.showLong(R.string.str_modify_success);
        popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
