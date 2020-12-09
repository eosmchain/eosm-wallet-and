package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.ui.activity.AddWalletActivity;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.MangoWalletUtils;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class WalletManagementFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;

    private QMUICommonListItemView walletNameItem;

    private Unbinder unbinder = null;
    private MangoWallet mangoWallet;
    private QMUIDialog mPasswordQMUIDialog;//密码
    private QMUIDialog mWalletNameQMUIDialog;
    private QMUIFullScreenPopup popup;
    private Bitmap qrCodeBitmap;
    private AppCompatImageView imageView;
    private Constants.WalletType walletType;

    private String hint;
    private int type = -1;
    private final int size = 200;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_wallet_management, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
    }

    @Override
    protected void initView() {
        topbar.setTitle(getString(R.string.str_wallet_manage));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        initGroupListView();
        QMUIDialog();
    }

    @Override
    protected void initAction() {

    }

    private void initGroupListView() {
        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height);
        QMUICommonListItemView walletAddressItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_wallet_address),
                getString(R.string.str_wallet_address),
                mangoWallet.getWalletAddress(),
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.AUTOFILL_TYPE_NONE, height);
        walletAddressItem.getDetailTextView().setSingleLine(true);
        walletAddressItem.getDetailTextView().setEllipsize(TextUtils.TruncateAt.MIDDLE);
        String walletName = walletType + "-Wallet";
        walletNameItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_wallet_name),
                getString(R.string.str_wallet_name),
                walletName,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView changePasswordItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_change_password),
                getString(R.string.str_change_password),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);


        QMUICommonListItemView mnemonicWordItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_mnemonic_word),
                getString(R.string.str_export_mnemonicword),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        mnemonicWordItem.setVisibility(ObjectUtils.isEmpty(mangoWallet.getMnemonicCode()) ? View.GONE : View.VISIBLE);

        QMUICommonListItemView exportPrivateKeyItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_import_privatekey),
                getString(R.string.str_export_privateKey),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView keystoreItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_keystore),
                getString(R.string.str_export_keystore),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        keystoreItem.setVisibility(ObjectUtils.isEmpty(mangoWallet.getKeystore()) ? View.GONE : View.VISIBLE);

//        QMUICommonListItemView permissionsExamineItem = groupListView.createItemView(
//                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_permissions_view),
//                getString(R.string.str_permissions_view),
//                null,
//                QMUICommonListItemView.HORIZONTAL,
//                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
//        permissionsExamineItem.setVisibility(Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == EOS ||
//                Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == MGP ? View.VISIBLE : View.GONE);

        hint = mangoWallet.getHint();
        QMUICommonListItemView promptMessageItem = groupListView.createItemView(
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_password_prompt),
                getString(R.string.str_prompt_message),
                hint,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        promptMessageItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        promptMessageItem.setOrientation(QMUICommonListItemView.HORIZONTAL);

        ClickUtils.OnDebouncingClickListener onClickListener = new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    if (ObjectUtils.equals(StringUtils.getString(R.string.str_wallet_address), text)) {
                        ClipboardUtils.copyText(mangoWallet.getWalletAddress());
                        ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_wallet_name), text)) {//修改钱包名称
//                        type = 1;
//                        mWalletNameQMUIDialog.show();
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_change_password), text)) {//修改钱包密码
                        type = 2;
                        mPasswordQMUIDialog.show();
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_export_mnemonicword), text)) {//导出助记词
                        type = 3;
                        mPasswordQMUIDialog.show();
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_export_keystore), text)) {//导出keystore
                        type = 4;
                        mPasswordQMUIDialog.show();
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_export_privateKey), text)) {//导出私钥
                        type = 5;
                        mPasswordQMUIDialog.show();
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_permissions_view), text)) {//权限查看
//                        intent = new Intent(getActivity(), BaseWalletActivity.class);
//                        intent.putExtra(EXTRA_INTERSECTION, PERMISSIONS_VIEW);
//                        intent.putExtra(EXTRA_ACCOUNT_INFO, accountInfoJson);
//                        ActivityUtils.startActivity(intent);
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_prompt_message), text)) {//密码提示
                        //1：修改钱包名称；2：修改钱包密码；3：导出助记词；4：导出keystore；5：导出私钥
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getActivity(), 20);
        QMUIGroupListView.newSection(getActivity())
                .setTitle("")
                .setDescription("")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(walletAddressItem, onClickListener)
                .addItemView(walletNameItem, onClickListener)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getActivity(), 5), 0)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getActivity())
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setTitle("")
                .addItemView(changePasswordItem, onClickListener)
                .addItemView(mnemonicWordItem, onClickListener)
                .addItemView(exportPrivateKeyItem, onClickListener)
                .addItemView(keystoreItem, onClickListener)
//                .addItemView(permissionsExamineItem, onClickListener)
                .addItemView(promptMessageItem, onClickListener)
                .addTo(groupListView);
    }

    @OnClick(R.id.deleteWalletBtn)
    public void onViewClicked() {
        type = 6;
        mPasswordQMUIDialog.show();
    }

    private void QMUIDialog() {
        if (mPasswordQMUIDialog == null) {
            mPasswordQMUIDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }
        if (mWalletNameQMUIDialog == null) {
            mWalletNameQMUIDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_please_walletname),
                    getString(R.string.str_please_walletname), getString(android.R.string.ok), getString(android.R.string.cancel), listener, false);
        }
        if (popup == null) {
            imageView = DialogHelper.getImageView(getActivity());
            popup = DialogHelper.showImage(getActivity(), imageView, size);
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            Intent intent;
            Bundle bundle;
            if (type != 1) {
                //1：修改钱包名称；2：修改钱包密码；3：导出助记词；4：导出keystore；5：导出私钥
                if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                    switch (type) {
                        case 2://修改密码
                            bundle = new Bundle();
                            bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                            startFragmentAndDestroyCurrent("ChangePasswordFragment", bundle, true);
                            break;
                        case 3://导出助记词
                            List<String> mnemonicCodeList = mangoWallet.getMnemonicCode();
                            if (ObjectUtils.isNotEmpty(mnemonicCodeList)) {
                                bundle = new Bundle();
                                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                                bundle.putInt("type", 2);//type 1=私钥；2=助记词；3=Keystore；
                                startFragment("ExportPrivateKeyOrMnemonicsFragment", bundle);
//                                StringBuilder mnemonicSB = new StringBuilder();
//                                for (String s : mnemonicCodeList) {
//                                    mnemonicSB.append(s).append(" ");
//                                }
//                                qrCodeBitmapRecycle();
//                                qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(mnemonicSB.toString().trim(), size);
//                                imageView.setImageBitmap(qrCodeBitmap);
//                                popup.show(groupListView);
                            }
                            break;
                        case 4://导出keystore
                            bundle = new Bundle();
                            bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                            bundle.putInt("type", 3);//type 1=私钥；2=助记词；3=Keystore；
                            startFragment("ExportPrivateKeyOrMnemonicsFragment", bundle);
//                            String keystore = MangoWalletUtils.deriveKeystore(mangoWallet);
//                            qrCodeBitmapRecycle();
//                            qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(keystore, size);
//                            imageView.setImageBitmap(qrCodeBitmap);
//                            popup.show(groupListView);
                            break;
                        case 5://导出私钥
                            bundle = new Bundle();
                            bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                            bundle.putInt("type", 1);//type 1=私钥；2=助记词；3=Keystore；
                            startFragment("ExportPrivateKeyOrMnemonicsFragment", bundle);
//                            String privateKey = mangoWallet.getPrivateKey();
//                            qrCodeBitmapRecycle();
//                            qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(privateKey, size);
//                            imageView.setImageBitmap(qrCodeBitmap);
//                            popup.show(groupListView);
                            break;
                        case 6://删除钱包
                            WalletDaoUtils.mangoWalletDao.delete(mangoWallet);
                            List<MangoWallet> allWallets = WalletDaoUtils.loadAll();
                            boolean isNull = false;
                            if (allWallets == null) {
                                isNull = true;
                            } else {
                                if (allWallets.size() <= 0) {
                                    isNull = true;
                                } else {
                                    isNull = false;
                                }
                            }
                            if (isNull) {
                                intent = new Intent(getActivity(), AddWalletActivity.class);
                                startActivity(intent);
                            } else {
                                MangoWallet wallet = allWallets.get(0);
                                WalletDaoUtils.updateCurrent(wallet);
                                BusUtils.post(BUS_TO_WALLET, new ToWallet(wallet, BUS_CUT_WALLET));
                                ToastUtils.showLong(R.string.str_delete_succeed);
                            }
                            popBackStack();
                            break;
                    }
                } else {
                    ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
                }
            }
            type = -1;
            editText.setText("");
            dialog.dismiss();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPasswordQMUIDialog != null) {
            if (mPasswordQMUIDialog.isShowing()) {
                mPasswordQMUIDialog.dismiss();
            }
        }
        mPasswordQMUIDialog = null;
        if (mWalletNameQMUIDialog != null) {
            if (mWalletNameQMUIDialog.isShowing()) {
                mWalletNameQMUIDialog.dismiss();
            }
        }
        mWalletNameQMUIDialog = null;
        if (popup != null) {
            popup.dismiss();
        }
        popup = null;
        qrCodeBitmapRecycle();

    }

    private void qrCodeBitmapRecycle() {
        if (qrCodeBitmap != null) {
            qrCodeBitmap.recycle();
        }
        qrCodeBitmap = null;
    }
}
