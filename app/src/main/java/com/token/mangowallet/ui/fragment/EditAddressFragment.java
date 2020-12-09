package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CountryBean;
import com.token.mangowallet.bean.GoodsTypeBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.ShippingAddressBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.exception.CompanyException;
import com.token.mangowallet.utils.RSAUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_ISADD_ADDRESS;
import static com.token.mangowallet.utils.Constants.EXTRA_RECEIVER_ADDRESS;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.percent;

/**
 * 新增/编辑 地址界面.
 */
public class EditAddressFragment extends BaseFragment {

    @BindView(R.id.edit_consignee)
    AppCompatEditText etConsignee;
    @BindView(R.id.edit_tel)
    AppCompatEditText etTel;
    @BindView(R.id.edit_detail)
    AppCompatEditText etDetail;
    @BindView(R.id.button_save)
    AppCompatButton btnSave;
    @BindView(R.id.text_region)
    AppCompatTextView tvRegion;
    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.tv_toggle)
    Switch tvToggle;
    @BindView(R.id.defaultAddressLayout)
    FrameLayout defaultAddressLayout;
    @BindView(R.id.unitedStatesTv)
    AppCompatTextView unitedStatesTv;
    @BindView(R.id.layout3)
    LinearLayout layout3;


    private Unbinder unbinder;
    private JDCityPicker cityPicker;
    private String mConsignee; // 收件人姓名
    private String mTel; // 手机号码
    private String mCity; // 省份名称 城市名称 地区名称
    private String mDetail; // 详细地址
    private boolean isDefault = false; // 邮编
    private String mProvinceName; // 省份名称
    private String mCityName; // 城市名称
    private String mDistrictName; // 地区名称
    private ShippingAddressBean.DataBean mAddressDataBean;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private boolean isAdd = true;
    private List<CountryBean.DataBean> countryList;
    private CountryBean.DataBean countryData;
    private QMUIBottomSheet bottomSheet;
    private int country;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_adress, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        isAdd = bundle.getBoolean(EXTRA_ISADD_ADDRESS, true);
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        if (!isAdd) {
            mAddressDataBean = bundle.getParcelable(EXTRA_RECEIVER_ADDRESS);
        }
        walletAddress = mangoWallet.getWalletAddress();
        getCountry();
    }

    @Override
    protected void initView() {
        topBar.setTitle(isAdd ? R.string.address_title_add : R.string.address_title_edit);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        defaultAddressLayout.setVisibility(isAdd ? View.GONE : View.VISIBLE);

        if (!isAdd) {
            mConsignee = mAddressDataBean.getUserName();
            mTel = mAddressDataBean.getPhone();
            mCity = mAddressDataBean.getCity();
            mDetail = mAddressDataBean.getDetailedAddress();
            isDefault = mAddressDataBean.isIsDefault();
            country = mAddressDataBean.getCountry();
            etConsignee.setText(mConsignee);
            etTel.setText(mTel);
            tvRegion.setText(mCity);
            etDetail.setText(mDetail);
            tvToggle.setChecked(isDefault);
            unitedStatesTv.setText(mAddressDataBean.getCountryName());
            btnSave.setEnabled(true);
        }
        MultiEditTextListening(etConsignee);
        MultiEditTextListening(etTel);
        MultiEditTextListening(etDetail);
        cityPicker = new JDCityPicker();
        cityPicker.init(getActivity());
    }

    @Override
    protected void initAction() {
        tvToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefault = isChecked;
                if (!buttonView.isPressed()) {
                    // 每次 setChecked 时会触发onCheckedChanged 监听回调，而有时我们在设置setChecked后不想去自动触发
                    // onCheckedChanged 里的具体操作, 即想屏蔽掉onCheckedChanged;加上此判断
                    return;
                }
            }
        });
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if (!ObjectUtils.isEmpty(district)) {
                    mProvinceName = province.getName();
                    mCityName = city.getName();
                    mDistrictName = district.getName();

                } else {
                    mProvinceName = province.getName();
                    mCityName = city.getName();
                }
                setRegionText();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @OnClick({R.id.text_region, R.id.button_save, R.id.unitedStatesTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_region:
                cityPicker.showCityPicker();
                break;
            case R.id.button_save:
                mConsignee = etConsignee.getText().toString().trim();
                mTel = etTel.getText().toString().trim();
                mDetail = etDetail.getText().toString().trim();
                if (isVerify()) {
                    if (isAdd) {
                        addUserAddr();
                    } else {
                        updateAddr(mAddressDataBean, mConsignee, mTel, mCity, mDetail, isDefault);
                    }
                }
                break;
            case R.id.unitedStatesTv:
                showSimpleBottomSheetList();
                break;
        }
    }

    private boolean isVerify() {
        if (ObjectUtils.isEmpty(etConsignee.getText().toString())) {
            ToastUtils.showLong(R.string.address_hint_consignee);
            return false;
        }

        if (ObjectUtils.isEmpty(etTel.getText().toString())) {
            ToastUtils.showLong(R.string.address_hint_tel);
            return false;
        }
//        if (RegexUtils.isMobileSimple(etTel.getText().toString())) {
//            ToastUtils.showLong(R.string.str_tel_format_wrong);
//            return false;
//        }
        if (ObjectUtils.isEmpty(etDetail.getText().toString())) {
            ToastUtils.showLong(R.string.address_hint_detail);
            return false;
        }
        if (layout3.getVisibility() == View.VISIBLE) {
            if (ObjectUtils.isEmpty(tvRegion.getText().toString())) {
                ToastUtils.showLong(R.string.str_please_select_area);
                return false;
            }
        }
        if (countryData == null) {
            ToastUtils.showLong(getString(R.string.str_please_choose) + getString(R.string.str_united_states));
            return false;
        }
        return true;
    }

    private void getCountry() {
        showTipDialog(getString(R.string.str_loading));
        try {
            NetWorkManager.getRequest().getCountry()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::countrySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUserAddr() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("phone", mTel);
        params.put("city", layout3.getVisibility() == View.GONE ? "" : mProvinceName + " " + mCityName + " " + mDistrictName);
        params.put("detailedAddress", mDetail);
        params.put("isDefault", true);
        params.put("userName", mConsignee);
        params.put("country", String.valueOf(country));
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().addUserAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addedSuccessfully, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新地址
     */
    private void updateAddr(ShippingAddressBean.DataBean dataBean, String userName, String phone, String city, String detailedAddress, boolean isDefault) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("id", String.valueOf(dataBean.getAddrID()));
        params.put("userId", String.valueOf(dataBean.getUserId()));
        params.put("userName", userName);
        params.put("phone", phone);
        params.put("city", city);
        params.put("detailedAddress", detailedAddress);
        params.put("isDefault", isDefault);
        params.put("country", String.valueOf(country));
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().updateAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::delOrUpdateAddrSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delOrUpdateAddrSuccess(JsonObject jsonData) {
        dismissTipDialog();
        MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
        if (msgCodeBean.getCode() == 0) {
            ToastUtils.showLong(R.string.str_modify_success);
            popBackStack();
        } else {
            ToastUtils.showLong(msgCodeBean.getMsg());
        }
    }

    private void countrySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            CountryBean countryBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), CountryBean.class);
            if (countryBean.getCode() == 0) {
                countryList = countryBean.getData();
            }
        }
    }

    private void addedSuccessfully(JsonObject jsonData) {
        dismissTipDialog();
        MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
        if (msgCodeBean.getCode() == 0) {
            ToastUtils.showLong(R.string.address_msg_add_success);
            popBackStack();
        } else {
            ToastUtils.showLong(msgCodeBean.getMsg());
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
    }

    protected void MultiEditTextListening(final AppCompatEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSave.setEnabled(isCheckAddress());
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (editText.getId()) {
                    case R.id.edit_consignee:
                        mConsignee = s.toString();
                        if (!ObjectUtils.isEmpty(mConsignee)) {

                        }
                        break;
                    case R.id.edit_tel:
                        mTel = s.toString();
                        if (!ObjectUtils.isEmpty(mTel)) {

                        }
                        break;
                    case R.id.edit_detail:
                        mDetail = s.toString();
                        if (!ObjectUtils.isEmpty(mDetail)) {

                        }
                        break;
                }
                btnSave.setEnabled(isCheckAddress());
            }
        });
    }

    private void setRegionText() {
        mCity = String.format("%s - %s%s", mProvinceName, mCityName, mDistrictName);
        tvRegion.setText(mCity);
    }

    private boolean isCheckAddress() {
        if (ObjectUtils.isEmpty(mConsignee) || ObjectUtils.isEmpty(mTel) || ObjectUtils.isEmpty(mDetail)) {
            return false;
        }
        if (layout3.getVisibility() == View.VISIBLE) {
            if (ObjectUtils.isEmpty(mCity)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();

    }

    private void showSimpleBottomSheetList() {
        if (bottomSheet == null) {
            QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
            builder.setGravityCenter(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                    .setTitle(getString(R.string.str_please_choose) + getString(R.string.str_united_states))
                    .setAddCancelBtn(true)
                    .setAllowDrag(false)
                    .setNeedRightMark(false)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            countryData = countryList.get(position);
                            country = countryData.getId();
                            String mCountyName = countryData.getCountyName();
                            unitedStatesTv.setText(mCountyName);
                            if (countryData.getId() == 1) {
                                layout3.setVisibility(View.VISIBLE);
                            } else {
                                layout3.setVisibility(View.GONE);
                            }
                        }
                    });
            for (int i = 0; i < countryList.size(); i++) {
                CountryBean.DataBean dataBean = countryList.get(i);
                if (dataBean != null) {
                    builder.addItem(dataBean.getCountyName());
                }
            }
            bottomSheet = builder.build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }
}
