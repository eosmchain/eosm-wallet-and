package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ContactInfoBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.ViewUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class SetupContactFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.emailValTv)
    AppCompatTextView emailValTv;
    @BindView(R.id.phoneNumberValTv)
    AppCompatEditText phoneNumberValTv;
    @BindView(R.id.wechatIDValTv)
    AppCompatEditText wechatIDValTv;


    private Unbinder unbinder;
    private View root;
    private MangoWallet mangoWallet;
    private ContactInfoBean.DataBean dataBean;
    private boolean isPhoneNumber = false;
    private String mEMail = "";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_set_contact_way, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        dataBean = bundle.getParcelable("ContactInfoBean");
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_contact_way);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        updateView();
    }

    @Override
    protected void initAction() {
        KeyboardUtils.registerSoftInputChangedListener(getActivity(), new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                boolean isEdit = false;
                if (height > 0) {
                    isEdit = true;
                } else {
                    saveContactInfo();
                    isEdit = false;
                }
                phoneNumberValTv.setCursorVisible(isEdit);
                wechatIDValTv.setCursorVisible(isEdit);
            }
        });
        addEditTextListener(phoneNumberValTv);
        addEditTextListener(wechatIDValTv);

        phoneNumberValTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                    isPhoneNumber = true;
                    saveContactInfo();
                }
            }
        });

        wechatIDValTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                    isPhoneNumber = false;
                    saveContactInfo();
                }
            }
        });
    }

    private void addEditTextListener(AppCompatEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (editText.getId()) {
                    case R.id.phoneNumberValTv:
                        isPhoneNumber = true;
                        break;
                    case R.id.wechatIDValTv:
                        isPhoneNumber = false;
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (editText.getId()) {
                    case R.id.phoneNumberValTv:
                        isPhoneNumber = true;
//                        saveContactInfo();
                        break;
                    case R.id.wechatIDValTv:
                        isPhoneNumber = false;
//                        saveContactInfo();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.emailValTv, R.id.goBindBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.emailValTv:
            case R.id.goBindBtn:
                if (ObjectUtils.isEmpty(mEMail)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    bundle.putString("EMail", mEMail);
                    startFragment("BindMailFragment", bundle);
                }
                break;
        }
    }

    private void updateView() {
        if (dataBean != null) {
            mEMail = ObjectUtils.isEmpty(dataBean.getMail()) ? "" : dataBean.getMail();
            emailValTv.setText(mEMail);
            phoneNumberValTv.setText(ObjectUtils.isEmpty(dataBean.getPhone()) ? "" : dataBean.getPhone());
            wechatIDValTv.setText(ObjectUtils.isEmpty(dataBean.getWeixin()) ? "" : dataBean.getWeixin());
        }
    }

    /**
     * 获取联系方式
     */
    private void getContactInfo() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getContactInfo(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onContactInfoSuccess, this::onError);
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
            String type = "1";
            String phone = "";
            String weixin = "";
            if (isPhoneNumber) {
                type = "1";
                phone = ObjectUtils.isEmpty(phoneNumberValTv.getText()) ? "" : phoneNumberValTv.getText().toString();
                params.put("phone", phone);
            } else {
                type = "2";
                weixin = ObjectUtils.isEmpty(wechatIDValTv.getText()) ? "" : wechatIDValTv.getText().toString();
                params.put("weixin", weixin);
            }
            params.put("type", type);

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
                dataBean = contactInfoBean.getData();
                updateView();
            } else {
                ToastUtils.showLong(contactInfoBean.getMsg());
            }
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        getContactInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardUtils.unregisterSoftInputChangedListener(getActivity().getWindow());
        if (!ObjectUtils.equals(ObjectUtils.isEmpty(phoneNumberValTv.getText()) ? "" : phoneNumberValTv.getText(), dataBean == null ? "" : dataBean.getPhone())) {
            isPhoneNumber = true;
            saveContactInfo();
        }
        if (!ObjectUtils.equals(ObjectUtils.isEmpty(wechatIDValTv.getText()) ? "" : wechatIDValTv.getText(), dataBean == null ? "" : dataBean.getWeixin())) {
            isPhoneNumber = false;
            saveContactInfo();
        }
    }
}
