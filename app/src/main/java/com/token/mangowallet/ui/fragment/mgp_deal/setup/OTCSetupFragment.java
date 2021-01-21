package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.OTCGlobalBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class OTCSetupFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    public EMWalletRepository emWalletRepository;
    private Constants.WalletType walletType;
    private QMUICommonListItemView otcArbitrationItem;
    private String walletAddress;

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
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        walletAddress = mangoWallet.getWalletAddress();
        emWalletRepository = new EMWalletRepository();

        getTableRowsGlobal();
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

        otcArbitrationItem = groupListView.createItemView(null,
                getString(R.string.str_otcarbitration_order_management),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON, height);
        otcArbitrationItem.setVisibility(View.GONE);
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
                    } else if (ObjectUtils.equals(getString(R.string.str_otcarbitration_order_management), text)) {
                        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                        startFragment("OTCArbitrationFragment", bundle);
                    }
                }
            }
        };
        QMUIGroupListView.newSection(getContext())
                .addItemView(contactWayItem, onClickListener)
                .addItemView(paymentTermItem, onClickListener)
                .addItemView(otcArbitrationItem, onClickListener)
                .addTo(groupListView);
    }

    /**
     * 投票配置表 比如：过多长时间才能撤回自己参加的投票（refund_delay_sec）；发布节点最小支付数额（min_bp_list_quantity）
     */
    private void getTableRowsGlobal() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", DEAL_CONTRACT);
            mapTableRows.put("code", DEAL_CONTRACT);
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("table", "global");
            emWalletRepository.fetchTableRowsStr(mapTableRows, walletType)
                    .subscribe(this::onGlobalSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onGlobalSuccess(Object o) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(o)) {
            OTCGlobalBean otcGlobalBean = GsonUtils.fromJson(o.toString(), OTCGlobalBean.class);
            if (otcGlobalBean != null) {
                if (CollectionUtils.isNotEmpty(otcGlobalBean.getRows())) {
                    OTCGlobalBean.RowsBean mOTCGlobalBean = otcGlobalBean.getRows().get(0);
                    if (CollectionUtils.isNotEmpty(mOTCGlobalBean.getOtc_arbiters())) {
                        for (int i = 0; i < mOTCGlobalBean.getOtc_arbiters().size(); i++) {
                            String otc_arbiters = mOTCGlobalBean.getOtc_arbiters().get(i);
                            if (ObjectUtils.equals(walletAddress, otc_arbiters)) {
                                otcArbitrationItem.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }
}
