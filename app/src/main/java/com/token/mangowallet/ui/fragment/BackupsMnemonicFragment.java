package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class BackupsMnemonicFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.mnemonicWordTv)
    AppCompatTextView mnemonicWordTv;

    private Unbinder unbinder;
    private MangoWallet wallet;
    private StringBuilder mnemonicSB;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_backups_mnemonic_word, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        wallet = bundle.getParcelable(EXTRA_WALLET);
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_backup_mnemonic));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        List<String> mnemonicCode = wallet.getMnemonicCode();
        mnemonicSB = new StringBuilder();
        for (String s : mnemonicCode) {
            mnemonicSB.append(s).append(" ");
        }

        mnemonicWordTv.setText(mnemonicSB.toString().trim());
    }

    @Override
    protected void initAction() {

    }

    @OnClick(R.id.nextStepBtn)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, wallet);
        startFragment("ConfirmMnemonicFragment", bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
