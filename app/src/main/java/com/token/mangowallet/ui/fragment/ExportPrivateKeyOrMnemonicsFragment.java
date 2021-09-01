package com.token.mangowallet.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.MangoWalletUtils;
import com.token.mangowallet.view.DialogHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;


public class ExportPrivateKeyOrMnemonicsFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.publickeyTv1)
    AppCompatTextView publickeyTv1;
    @BindView(R.id.privatekeyTv1)
    AppCompatTextView privatekeyTv1;
    @BindView(R.id.publickeyTv2)
    AppCompatTextView publickeyTv2;
    @BindView(R.id.privatekeyTv2)
    AppCompatTextView privatekeyTv2;
    @BindView(R.id.ownerTv)
    AppCompatTextView ownerTv;
    @BindView(R.id.activeTv)
    AppCompatTextView activeTv;
    @BindView(R.id.copyTv1)
    AppCompatTextView copyTv1;
    @BindView(R.id.qrCodeBtn1)
    QMUIRoundButton qrCodeBtn1;
    @BindView(R.id.cardView1)
    CardView cardView1;
    @BindView(R.id.copyTv2)
    AppCompatTextView copyTv2;
    @BindView(R.id.qrCodeBtn2)
    QMUIRoundButton qrCodeBtn2;
    @BindView(R.id.cardView2)
    CardView cardView2;
    @BindView(R.id.accountNameTv)
    AppCompatTextView accountNameTv;
    @BindView(R.id.publickeyTv3)
    AppCompatTextView publickeyTv3;
    @BindView(R.id.mnemonicsTv)
    AppCompatTextView mnemonicsTv;
    @BindView(R.id.copyTv3)
    AppCompatTextView copyTv3;
    @BindView(R.id.qrCodeBtn3)
    QMUIRoundButton qrCodeBtn3;
    @BindView(R.id.cardView3)
    CardView cardView3;
    @BindView(R.id.cardView4)
    CardView cardView4;
    @BindView(R.id.titleTv)
    AppCompatTextView titleTv;
    @BindView(R.id.contentTv)
    AppCompatTextView contentTv;
    @BindView(R.id.copyTv4)
    AppCompatTextView copyTv4;
    @BindView(R.id.qrCodeBtn4)
    QMUIRoundButton qrCodeBtn4;

    private Unbinder unbinder;
    private AccountInfo accountInfo;
    private MangoWallet wallet;
    private String privateKey;
    private Bitmap qrCodeBitmap;
    //    private Map<String, Object> map = new HashMap<>();
    private String json;
    private QMUIFullScreenPopup popup;
    private int size = 200;
    private int type;
    private StringBuilder mnemonicSB;
    private AppCompatImageView imageView;
    private Constants.WalletType walletType;
    private String keystore;
    private String mTitle;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.activity_export_privatekey, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");//type 1=私钥；2=助记词；3=Keystore；
        wallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(wallet.getWalletType());
        if (walletType == Constants.WalletType.MGP || walletType == Constants.WalletType.EOS) {
            getAccount();
        }
        if (type == 1) {//type 1=私钥；2=助记词；3=Keystore；
            privateKey = wallet.getPrivateKey();
            json = privateKey;
            mTitle = StringUtils.getString(R.string.str_export_privateKey);
        } else if (type == 2) {
            mnemonicSB = new StringBuilder();
            for (String s : wallet.getMnemonicCode()) {
                mnemonicSB.append(s).append(" ");
            }
            json = mnemonicSB.toString();
            mTitle = StringUtils.getString(R.string.str_export_mnemonicword);
        } else if (type == 3) {
            keystore = MangoWalletUtils.deriveKeystore(wallet);
            json = keystore;
            mTitle = StringUtils.getString(R.string.str_export_keystore);
        } else {
            json = "";
            mTitle = StringUtils.getString(R.string.str_export_privateKey);
        }
//        map.put("type", isPrivateKey ? TO_IMPORT_WALLET_PRIVATEKEY : TO_IMPORT_WALLET_MNEMONIC);
//        map.put(isPrivateKey ? "privateKey" : "mnemonic", isPrivateKey ? privateKey : mnemonicSB.toString());
//        json = GsonUtils.toJson(map);
        qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(json, size);
    }

    @Override
    public void initView() {
        topbar.setTitle(mTitle);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        if (walletType == Constants.WalletType.MGP || walletType == Constants.WalletType.EOS) {
            cardView1.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
            cardView2.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
            cardView3.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
            cardView4.setVisibility(View.GONE);
        } else {
            cardView1.setVisibility(View.GONE);
            cardView2.setVisibility(View.GONE);
            cardView3.setVisibility(View.GONE);
            cardView4.setVisibility(View.VISIBLE);
        }
        updataView();
    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.copyTv1, R.id.qrCodeBtn1, R.id.copyTv2, R.id.qrCodeBtn2, R.id.copyTv3, R.id.qrCodeBtn3, R.id.copyTv4, R.id.qrCodeBtn4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.copyTv1:
            case R.id.copyTv2:
            case R.id.copyTv3:
            case R.id.copyTv4:
                String copy = "";
                if (type == 1) {
                    copy = privateKey;
                } else if (type == 2) {
                    copy = mnemonicSB.toString();
                } else if (type == 3) {
                    copy = keystore;
                }
                ClipboardUtils.copyText(copy);
                ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
                break;
            case R.id.qrCodeBtn1:
            case R.id.qrCodeBtn2:
            case R.id.qrCodeBtn3:
            case R.id.qrCodeBtn4:
                if (popup == null) {
                    imageView = DialogHelper.getImageView(getActivity());
                    imageView.setImageBitmap(qrCodeBitmap);
                    popup = DialogHelper.showImage(getActivity(), imageView, size);
                }
                popup.show(view);
                break;
        }
    }

    private void updataView() {
        boolean isPrivateKey = type == 1;
        if (isPrivateKey) {
            if (walletType == Constants.WalletType.MGP || walletType == Constants.WalletType.EOS) {
                if (!ObjectUtils.isEmpty(accountInfo)) {
                    List<AccountInfo.PermissionsBean> permissionsBeanList = accountInfo.getPermissions();
                    AccountInfo.PermissionsBean activePermissionsBean = permissionsBeanList.get(0);
                    AccountInfo.PermissionsBean ownerPermissionsBean = permissionsBeanList.get(1);

                    String activeName = activePermissionsBean.getPerm_name();
                    String ownerName = ownerPermissionsBean.getPerm_name();

                    AccountInfo.PermissionsBean.RequiredAuthBean activeRequiredAuthBean = activePermissionsBean.getRequired_auth();
                    AccountInfo.PermissionsBean.RequiredAuthBean ownerRequiredAuthBean = ownerPermissionsBean.getRequired_auth();

                    List<AccountInfo.PermissionsBean.RequiredAuthBean.KeysBean> activeKeysBeanList = activeRequiredAuthBean.getKeys();
                    List<AccountInfo.PermissionsBean.RequiredAuthBean.KeysBean> ownerKeysBeanList = ownerRequiredAuthBean.getKeys();

                    AccountInfo.PermissionsBean.RequiredAuthBean.KeysBean activeKeysBean = activeKeysBeanList.get(0);
                    AccountInfo.PermissionsBean.RequiredAuthBean.KeysBean ownerKeyBean = ownerKeysBeanList.get(0);

                    String activeKey = activeKeysBean.getKey();
                    String ownerKey = ownerKeyBean.getKey();

                    ownerTv.setText(ownerName);
                    publickeyTv1.setText(ownerKey.replace("EOS", "MGP"));
                    activeTv.setText(activeName);
                    publickeyTv2.setText(activeKey.replace("EOS", "MGP"));
                }
                privatekeyTv1.setText(privateKey);
                privatekeyTv2.setText(privateKey);
            } else {
                titleTv.setText(getString(R.string.str_privatekey));
                contentTv.setText(privateKey);
            }
        } else {
            if (walletType == Constants.WalletType.MGP || walletType == Constants.WalletType.EOS) {
                accountNameTv.setText(wallet.getWalletAddress());
                publickeyTv3.setText(wallet.getPublicKey().replace("EOS","MGP"));
                mnemonicsTv.setText(mnemonicSB.toString());
            } else {
                if (type == 2) {
                    titleTv.setText(getString(R.string.str_mnemonic_word));
                    contentTv.setText(mnemonicSB.toString());
                } else if (type == 3) {
                    titleTv.setText(getString(R.string.str_keystore));
                    contentTv.setText(keystore);
                }
            }
        }
    }

    public void getAccount() {
        Observable.fromCallable(() -> MyApplication.repositoryFactory.customEosioJavaRpcProvider
                .getAccount(EOSParams.getRequestBody(EOSParams.getAccountPamars(wallet.getWalletAddress()))))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<String>(getActivity(), true) {

                    @Override
                    public void onFail(String failMsg) {
                        updataView();
                    }

                    @Override
                    public void onSuccess(String s) {
                        accountInfo = GsonUtils.fromJson(s, AccountInfo.class);
                        updataView();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (qrCodeBitmap != null) {
            qrCodeBitmap.recycle();
            qrCodeBitmap = null;
        }
    }
}
