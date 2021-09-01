package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CreateAccountMode;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.ui.activity.AddWalletActivity;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.view.DialogHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wendu.webviewjavascriptbridge.WVJBWebView;

import static com.token.mangowallet.utils.Constants.CREATE_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_ADD_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.EXTRA_CREATE_ACCOUNT_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_WALLET_TYPE;


public class AssistCreateFragment extends BaseFragment {
    @BindView(R.id.webview)
    WVJBWebView webview;
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.logoIv)
    AppCompatImageView logoIv;
    @BindView(R.id.creatorTv)
    AppCompatTextView creatorTv;
    @BindView(R.id.accountNameTv)
    AppCompatTextView accountNameTv;
    @BindView(R.id.activePublickeyTv)
    AppCompatTextView activePublickeyTv;
    @BindView(R.id.ownerPublickeyTv)
    AppCompatTextView ownerPublickeyTv;
    @BindView(R.id.RAMTv)
    AppCompatTextView RAMTv;
    @BindView(R.id.CPUTv)
    AppCompatTextView CPUTv;
    @BindView(R.id.NETTv)
    AppCompatTextView NETTv;
    @BindView(R.id.immediatelyCreateBtn)
    QMUIRoundButton immediatelyCreateBtn;

    private final static String url = "file:///android_asset/EOSWeb.html";//"file:///android_asset/" + "testss.html";//"EOSWeb.html";
    private String assistCreateWalletData;
    private CreateAccountMode createAccountMode;
    private String creator;
    private String private_key;
    private String account;
    private long buy_ram_bytes;
    private String stake_net_quantity;
    private String stake_cpu_quantity;
    private String active_pubkey;
    private String owner_pubkey;
    private String ep = BaseUrlUtils.getInstance().getDIGICCYBaseUrl();
    private QMUIDialog qmuiDialog;
    private QMUIDialog mQMUIDialog;
    private MangoWallet mangoWallet;
    private String json;
    private Unbinder unbinder;
    private Constants.WalletType walletType = null;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_assist_create, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        assistCreateWalletData = bundle.getString(EXTRA_CREATE_ACCOUNT_DATA, "");
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        createAccountMode = GsonUtils.fromJson(assistCreateWalletData, CreateAccountMode.class);
        if (mangoWallet != null) {
            creator = mangoWallet.getWalletAddress();
            private_key = mangoWallet.getPrivateKey();
            walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        }
        if (ObjectUtils.isEmpty(creator) || ObjectUtils.isEmpty(private_key)) {
            DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_hint),
                    getString(R.string.str_active_error_msg), "", getString(R.string.str_to_login), null, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putInt(INTENT_EXTRA_KEY_WALLET_TYPE, walletType.getType());
                            bundle.putInt(EXTRA_ADD_WALLET_TYPE, CREATE_WALLET);
                            Intent intent = new Intent(getActivity(), AddWalletActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    @Override
    public void initView() {
        topbar.setTitle(StringUtils.getString(R.string.str_assist_create));
        topbar.addLeftBackImageButton().setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        webview.loadUrl(url);
        account = createAccountMode.getAccount();
        creatorTv.setText(creator);
        accountNameTv.setText(account);
        active_pubkey = createAccountMode.getActive();
        activePublickeyTv.setText(active_pubkey);
        owner_pubkey = createAccountMode.getOwner();
        ownerPublickeyTv.setText(owner_pubkey);
        buy_ram_bytes = 4300;
        RAMTv.setText(buy_ram_bytes + "");
        stake_net_quantity = "0.2500 " + walletType;
        NETTv.setText(stake_net_quantity);
        stake_cpu_quantity = "0.0400 " + walletType;
        CPUTv.setText(stake_cpu_quantity);
        webview.setWebContentsDebuggingEnabled(true);
        if (mQMUIDialog == null) {
            mQMUIDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);//要激活的账号
        map.put("creator", creator);//给账号激活的账号
        map.put("private_key", private_key);//给账号激活的私钥
        map.put("buy_ram_bytes", buy_ram_bytes);//给激活的账号购买的内存
        map.put("stake_net_quantity", stake_net_quantity);//给要激活的账号购买的宽带
        map.put("stake_cpu_quantity", stake_cpu_quantity);//给要激活的账号购买的cpu
        map.put("active_pubkey", active_pubkey);//要激活的账号的active公钥
        map.put("owner_pubkey", owner_pubkey);//要激活的账号的owner公钥
        map.put("ep", ep);//币的节点
        json = JSON.toJSONString(map);
    }

    @Override
    protected void initAction() {

    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                createAccount(json);
            } else {
                ToastUtils.showLong(R.string.str_wrong_password);
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    @OnClick(R.id.immediatelyCreateBtn)
    public void onViewClicked() {
        mQMUIDialog.show();
    }

    private void createAccount(String json) {
        LogUtils.dTag("createAccount==", "json = " + json);
        webview.callHandler("createAccount", json, new WVJBWebView.WVJBResponseCallback() {
            @Override
            public void onResult(Object data) {
                String msg;
                if (ObjectUtils.equals("succeed", data.toString())) {
                    msg = getString(R.string.str_wallet_activate_succeed);
                } else {
                    msg = getString(R.string.str_wallet_activate_fail) + "\n" + data.toString();
                }
                dialog(msg);
            }
        });
    }

    private void dialog(String msg) {
        qmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_prompt_message), msg, "", getString(android.R.string.ok), null,
                new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        popBackStack();
                    }
                });
        qmuiDialog.show();
    }
}

