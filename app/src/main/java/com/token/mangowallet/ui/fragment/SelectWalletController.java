package com.token.mangowallet.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.token.mangowallet.R;
import com.token.mangowallet.interact.CreateWalletInteract;
import com.token.mangowallet.listener.ControlListener;
import com.token.mangowallet.ui.adapter.AccountAdapter;
import com.token.mangowallet.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectWalletController extends QMUIWindowInsetLayout {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CreateWalletInteract createWalletInteract;
    private QMUITipDialog tipDialog;
    private List<String> accountList = CollectionUtils.newArrayList();
    private AccountAdapter accountAdapter;
    protected ControlListener mControlListener;
    private SelectOrRegisterWalletFragment fragment;

    public SelectWalletController(SelectOrRegisterWalletFragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        LayoutInflater.from(fragment.getActivity()).inflate(R.layout.view_select_wallet, this);
        ButterKnife.bind(this);
        createWalletInteract = new CreateWalletInteract();
        initView();
        initRecyclerView();
    }

    public void initView() {
        showTipDialog();
        createWalletInteract.loadWalletAccount(fragment.mangoWallet.getPrivateKey(),
                Constants.WalletType.getPagerFromPositon(fragment.mangoWallet.getWalletType()))
                .subscribe(this::loadSuccess, this::onError);
    }

    public void initRecyclerView() {
        accountAdapter = new AccountAdapter(accountList);
        recyclerView.setAdapter(accountAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getActivity()));
        accountAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mControlListener != null) {
                    String account = accountList.get(position);
                    mControlListener.onSelectWallet(account);
                }
            }
        });
    }

    public void loadSuccess(List<String> list) {
        dismissTipDialog();
        if (ObjectUtils.isEmpty(list)) {
            ToastUtils.showLong(R.string.str_no_account_found);
            return;
        }
        accountList.clear();
        accountList.addAll(list);
        accountAdapter.notifyDataSetChanged();
    }

    public void onError(Throwable e) {
        dismissTipDialog();
        ToastUtils.showLong(R.string.str_no_account_found);
    }

    private void showTipDialog() {
        if (tipDialog == null) {
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(StringUtils.getString(R.string.str_loading))
                    .create();
        }
        tipDialog.show();
    }

    private void dismissTipDialog() {
        if (tipDialog != null) {
            if (tipDialog.isShowing()) { //check if dialog is showing.
                tipDialog.dismiss();
            }
        }
    }

    public void setControlListener(ControlListener controlListener) {
        mControlListener = controlListener;
    }

    public void destroy() {

    }
}
