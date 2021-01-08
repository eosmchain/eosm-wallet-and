package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class OTCSetupFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_otc_setup, null);
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
        topbar.setTitle(R.string.str_otc_setup);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        initGroupListView();
    }

    @Override
    protected void initAction() {

    }

    private void initGroupListView() {
        int height = QMUIDisplayHelper.dp2px(getContext(), 80);
        QMUICommonListItemView contactWayItem = groupListView.createItemView(null,
                getString(R.string.str_contact_way),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON, height);
        QMUICommonListItemView paymentTermItem = groupListView.createItemView(null,
                getString(R.string.str_payment_term),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON, height);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Bundle bundle = new Bundle();
                    if (ObjectUtils.equals(getString(R.string.str_contact_way), text)) {
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        startFragment("SetupContactFragment", bundle);
                    } else if (ObjectUtils.equals(getString(R.string.str_payment_term), text)) {
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        startFragment("SetupPaymentFragment", bundle);
                    }
                }
            }
        };
        QMUIGroupListView.newSection(getContext())
                .addItemView(contactWayItem, onClickListener)
                .addItemView(paymentTermItem, onClickListener)
                .addTo(groupListView);

    }
}
