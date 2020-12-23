package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.ui.adapter.WalletCardAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.ui.home.HomeFragment.homeFragment;
import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class WalletListFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private List<MangoWallet> walletList = new ArrayList<>();
    private MangoWallet mangoWallet;
    private WalletCardAdapter adapter;
    private QMUIDialog mPasswordQMUIDialog;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_all_wallet_list, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        walletList.clear();
        walletList.addAll(WalletDaoUtils.loadAll());
        for (int i = 0; i < walletList.size(); i++) {
            MangoWallet mangoWallet = walletList.get(i);
            Constants.WalletType walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
            if (walletType != MGP) {
                walletList.remove(i);
            }
        }
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_wallet_manage);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        adapter = new WalletCardAdapter(walletList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mPasswordQMUIDialog == null) {
            mPasswordQMUIDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
    }

    @Override
    protected void initAction() {
        adapter.setOnWalletClickListener(new WalletCardAdapter.OnWalletClickListener() {
            @Override
            public void onExportWallet(int position) {
                mangoWallet = walletList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("WalletManagementFragment", bundle);
            }

            @Override
            public void onDeleteWallet(int position) {
                mangoWallet = walletList.get(position);
                mPasswordQMUIDialog.show();
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MangoWallet mangoWallet = walletList.get(position);
                WalletDaoUtils.updateCurrent(mangoWallet);
                BusUtils.post(BUS_TO_WALLET, new ToWallet(mangoWallet, BUS_CUT_WALLET));
                popBackStack();
            }
        });
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                WalletDaoUtils.mangoWalletDao.delete(mangoWallet);
                adapter.remove(mangoWallet);
                ToastUtils.showLong(R.string.str_delete_succeed);
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
