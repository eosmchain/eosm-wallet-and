package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ContactInfoBean;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class BindMailFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.emailEt)
    AppCompatEditText emailEt;
    @BindView(R.id.verificationCodeEt)
    AppCompatEditText verificationCodeEt;
    @BindView(R.id.sendCodeBtn)
    QMUIRoundButton sendCodeBtn;
    @BindView(R.id.confirmBtn)
    QMUIRoundButton confirmBtn;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private CountDownTimer timer;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bind_email, null);
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
        topbar.setTitle(R.string.str_mailbox_verification);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

    }

    @Override
    protected void initAction() {
//        addEditTextListener(emailEt);
//        addEditTextListener(verificationCodeEt);
    }

    @OnClick({R.id.sendCodeBtn, R.id.confirmBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendCodeBtn:
                sendVerificationCode();
                break;
            case R.id.confirmBtn:
                if (ObjectUtils.isEmpty(emailEt.getText())) {
                    ToastUtils.showLong(R.string.str_import_email);
                    return;
                }
                if (ObjectUtils.isEmpty(verificationCodeEt.getText())) {
                    ToastUtils.showLong(R.string.str_import_verification_code);
                    return;
                }
                if (!RegexUtils.isEmail(emailEt.getText())) {
                    ToastUtils.showLong(R.string.str_mailbox_format_incorrect);
                    return;
                }
                saveContactInfo();
                break;
        }
    }

    private void addEditTextListener(AppCompatEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (editText.getId()) {
                    case R.id.emailEt:
                        break;
                    case R.id.verificationCodeEt:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (editText.getId()) {
                    case R.id.emailEt:
                        break;
                    case R.id.verificationCodeEt:
                        break;
                }
            }
        });
    }

    /**
     * 倒计时显示
     */
    private void countDown() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendCodeBtn.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                sendCodeBtn.setText(getString(R.string.str_resend));
            }
        }.start();
    }

    /**
     * 发送验证码
     * 1、 mail: String,  0. 绑定时的邮箱 ；type=2、3不传mail；
     * 2、 mgpName: String,  type: 1当前账户； 2卖家账户 ； 3 买家账户；
     * 3、 type: Int,  0邮箱绑定；1订单支付通知；2.放行；
     * 4、money: Double?   type： 1：不传money； 2：购买的MGP； 3需要打款的MGP
     */
    private void sendVerificationCode() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("mail", emailEt.getText().toString());
            params.put("type", "0");
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().sendVerificationCode(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onVerificationCodeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存联系方式
     * mgpName
     * 邮箱 type =0； mail ；code；
     * 手机号码 type =1；phone；
     * 微信号 type =2；weixin；
     */
    private void saveContactInfo() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("mail", emailEt.getText().toString());
            params.put("code", verificationCodeEt.getText().toString());
            params.put("type", "0");
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().saveContactInfo(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onContactInfoSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onContactInfoSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            ContactInfoBean contactInfoBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), ContactInfoBean.class);
            if (contactInfoBean.getCode() == 0) {
                popBackStack();
                ToastUtils.showLong(contactInfoBean.getMsg());
            } else {
                ToastUtils.showLong(contactInfoBean.getMsg());
            }
        }
    }

    private void onVerificationCodeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean verificationCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (verificationCodeBean.getCode() == 0) {
                countDown();
            } else {
                ToastUtils.showLong(verificationCodeBean.getMsg());
            }
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
