package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.PayInfoBean;
import com.token.mangowallet.bean.UpdataFileBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.PhotoUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class AddPaymentFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.userNameValEt)
    AppCompatEditText userNameValEt;
    @BindView(R.id.accountNumberTv)
    AppCompatTextView accountNumberTv;
    @BindView(R.id.accountNumberValEt)
    AppCompatEditText accountNumberValEt;
    @BindView(R.id.collectionQRValIv)
    AppCompatImageView collectionQRValIv;
    @BindView(R.id.deleteBtn)
    AppCompatImageView deleteBtn;
    @BindView(R.id.addCollectionQRLayout)
    FrameLayout addCollectionQRLayout;
    @BindView(R.id.bankNameValEt)
    AppCompatEditText bankNameValEt;
    @BindView(R.id.bankSubbranchValEt)
    AppCompatEditText bankSubbranchValEt;
    @BindView(R.id.phonePayGroup)
    Group phonePayGroup;
    @BindView(R.id.bankPayGroup)
    Group bankPayGroup;
    @BindView(R.id.saveBtn)
    QMUIRoundButton saveBtn;
    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private int payId = 0;
    private PayInfoBean.DataBean payInfoBean;
    private boolean isAdd = true;
    private PhotoUtils photoUtils;
    private boolean isAddPic = false;
    private String picUrl = "";

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_payment, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        payId = bundle.getInt("payId");
        payInfoBean = bundle.getParcelable("PayInfoBean");
        photoUtils = new PhotoUtils();
    }

    @Override
    protected void initView() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) saveBtn.getLayoutParams();
        int title;
        switch (payId) {
            case 1://银行卡
                title = R.string.str_bank_card;
                accountNumberTv.setText(R.string.str_bank_id);
                accountNumberValEt.setHint(R.string.str_import_bank_card_account_number);
                bankPayGroup.setVisibility(View.VISIBLE);
                phonePayGroup.setVisibility(View.GONE);
                layoutParams.topToBottom = R.id.bankSubbranchValEt;
                saveBtn.setLayoutParams(layoutParams);
                break;
            case 2://微信支付
                title = R.string.str_wechat_pay;
                accountNumberTv.setText(R.string.str_account_number);
                accountNumberValEt.setHint(R.string.str_import_collection_account);
                bankPayGroup.setVisibility(View.GONE);
                phonePayGroup.setVisibility(View.VISIBLE);
                layoutParams.topToBottom = R.id.addCollectionQRLayout;
                saveBtn.setLayoutParams(layoutParams);
                break;
            case 3://支付宝
                title = R.string.str_alipay;
                accountNumberTv.setText(R.string.str_account_number);
                accountNumberValEt.setHint(R.string.str_import_collection_account);
                bankPayGroup.setVisibility(View.GONE);
                phonePayGroup.setVisibility(View.VISIBLE);
                layoutParams.topToBottom = R.id.addCollectionQRLayout;
                saveBtn.setLayoutParams(layoutParams);
                break;
            default:
                title = R.string.str_bank_card;
                bankPayGroup.setVisibility(View.VISIBLE);
                phonePayGroup.setVisibility(View.GONE);
                layoutParams.topToBottom = R.id.bankSubbranchValEt;
                saveBtn.setLayoutParams(layoutParams);
                break;
        }
        topbar.setTitle(title);
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
        photoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object result) {
                uploadFile((String) result);
            }
        });
    }

    @OnClick({R.id.addCollectionQRLayout, R.id.saveBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addCollectionQRLayout:
                if (isAddPic) {
                    deleteBtn.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(R.mipmap.ic_add_pic).error(R.drawable.placeholder).into(collectionQRValIv);
                    isAddPic = !isAddPic;
                } else {
                    photoUtils.albumSelect(getActivity(), true);
                }
                break;
            case R.id.saveBtn:
                if (ObjectUtils.isEmpty(userNameValEt.getText())) {
                    ToastUtils.showLong(R.string.str_import_collection_name);
                    return;
                }
                if (ObjectUtils.isEmpty(accountNumberValEt.getText())) {
                    ToastUtils.showLong(accountNumberValEt.getHint());
                    return;
                }
                if (payId == 1) {
                    if (ObjectUtils.isEmpty(bankNameValEt.getText())) {
                        ToastUtils.showLong(bankNameValEt.getHint());
                        return;
                    }
                    if (ObjectUtils.isEmpty(bankSubbranchValEt.getText())) {
                        ToastUtils.showLong(bankSubbranchValEt.getHint());
                        return;
                    }
                }
                savePayWay();
                break;
        }
    }

    private void updateView() {
        if (payInfoBean != null) {
            isAdd = false;
            userNameValEt.setText(ObjectUtils.isEmpty(payInfoBean.getUsername()) ? "" : payInfoBean.getUsername());
            accountNumberValEt.setText(ObjectUtils.isEmpty(payInfoBean.getCardNum()) ? "" : payInfoBean.getCardNum());
            //1、银行卡；2、微信支付；3、支付宝
            if (payId == 1) {
                bankNameValEt.setText(ObjectUtils.isEmpty(payInfoBean.getName()) ? "" : payInfoBean.getName());
                bankSubbranchValEt.setText(ObjectUtils.isEmpty(payInfoBean.getBranch()) ? "" : payInfoBean.getBranch());
            } else {
                isAddPic = true;
                deleteBtn.setVisibility(View.VISIBLE);
                picUrl = ObjectUtils.isEmpty(payInfoBean.getQrCode()) ? "" : payInfoBean.getQrCode();
                Glide.with(getActivity()).load(picUrl).error(R.drawable.placeholder).into(collectionQRValIv);
            }
        } else {
            isAdd = true;
        }
    }

    /**
     * 添加/修改收款方式
     */
    private void savePayWay() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("username", userNameValEt.getText().toString());
            params.put("cardNum", accountNumberValEt.getText().toString());
            params.put("payId", String.valueOf(payId));
            if (payId == 1) {
                params.put("name", bankNameValEt.getText().toString());
                params.put("branch", bankSubbranchValEt.getText().toString());
            } else {
                params.put("qrCode", picUrl);
            }
            if (payInfoBean != null) {
                params.put("payInfoId", String.valueOf(payInfoBean.getPayInfoId()));
            }
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().savePayWay(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSavePayWaySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     */
    private void uploadFile(String filePath) {
        showTipDialog(getString(R.string.str_loading));
        try {
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            File file = new File(filePath);
            if (!file.exists()) {
                LogUtils.d("找不到该文件");
                return;
            }
            mMultipartBodyBuilder
                    .addFormDataPart("file", URLEncoder.encode(file.getName(), "UTF-8")
                            , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("multipart/form-data")));
            MultipartBody multipartBody = mMultipartBodyBuilder.build();
            if (multipartBody.size() <= 0) {
                return;
            }
            NetWorkManager.getRequest().uploadFile(multipartBody, "otcstore.mgps.me", "MGP")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onUploadImgeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onUploadImgeSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            UpdataFileBean updataFileBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), UpdataFileBean.class);
            if (updataFileBean != null) {
                if (updataFileBean.getCode() == 0) {
                    UpdataFileBean.DataBean dataBean = updataFileBean.getData();
                    if (dataBean != null) {
                        isAddPic = true;
                        deleteBtn.setVisibility(View.VISIBLE);
                        picUrl = ObjectUtils.isEmpty(dataBean.getUrl()) ? "" : dataBean.getUrl();
                        Glide.with(getActivity()).load(picUrl).error(R.drawable.placeholder).into(collectionQRValIv);
                    }
                } else {
                    ToastUtils.showLong(updataFileBean.getMsg());
                }
            }
        }
    }

    private void onSavePayWaySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean isBindBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (isBindBean != null) {
                if (isBindBean.getCode() == 0) {
                    ToastUtils.showLong(R.string.address_msg_add_success);
                    popBackStack();
                } else {
                    ToastUtils.showLong(isBindBean.getMsg());
                }
            }
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }
}
