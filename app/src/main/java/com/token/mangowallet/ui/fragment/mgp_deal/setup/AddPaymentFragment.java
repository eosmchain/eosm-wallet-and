package com.token.mangowallet.ui.fragment.mgp_deal.setup;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ContactInfoBean;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.PayInfoBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.UpdataFileBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.PhotoUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;
import com.yanzhenjie.durban.Durban;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
import static com.token.mangowallet.utils.Constants.SETSELLER;

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
    @BindView(R.id.saveBtn)
    QMUIRoundButton saveBtn;
    @BindView(R.id.mailTv)
    AppCompatTextView mailTv;
    @BindView(R.id.mailValEt)
    AppCompatEditText mailValEt;
    @BindView(R.id.verificationTv)
    AppCompatTextView verificationTv;
    @BindView(R.id.verificationValEt)
    AppCompatEditText verificationValEt;
    @BindView(R.id.sendCodeBtn)
    QMUIRoundButton sendCodeBtn;
    @BindView(R.id.userNameTv)
    AppCompatTextView userNameTv;
    @BindView(R.id.collectionQRTv)
    AppCompatTextView collectionQRTv;
    @BindView(R.id.bankNameTv)
    AppCompatTextView bankNameTv;
    @BindView(R.id.bankSubbranchTv)
    AppCompatTextView bankSubbranchTv;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    @BindView(R.id.chainTypeTv)
    AppCompatTextView chainTypeTv;
    @BindView(R.id.chainTypeValEt)
    AppCompatTextView chainTypeValEt;
    @BindView(R.id.collectionAddressTv)
    AppCompatTextView collectionAddressTv;
    @BindView(R.id.collectionAddressValEt)
    AppCompatEditText collectionAddressValEt;
    @BindView(R.id.bankIdTv)
    AppCompatTextView bankIdTv;
    @BindView(R.id.bankIdValEt)
    AppCompatEditText bankIdValEt;
    @BindView(R.id.verificationValLayout)
    LinearLayout verificationValLayout;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private int payId = 0;//1、银行卡；2、微信支付；3、支付宝；4、usdt trc20；5、usdt erc20；
    private PayInfoBean.DataBean payInfoBean;
    private boolean isAdd = true;
    private PhotoUtils photoUtils;
    private boolean isAddPic = false;
    private String picUrl = "";
    private String picPath = "";
    private boolean isEdit = false;
    private QMUIDialog delQMUIDialog;
    private ArrayList<String> mImageList;
    private ContactInfoBean.DataBean dataBean;
    private CountDownTimer timer;
    public EMWalletRepository emWalletRepository;
    private String mMail = "";
    private Constants.WalletType walletType;

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
        isEdit = bundle.getBoolean("isEdit");
        dataBean = bundle.getParcelable("ContactInfoBean");
        photoUtils = new PhotoUtils();
        mImageList = new ArrayList<>();
        emWalletRepository = new EMWalletRepository();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
    }

    @Override
    protected void initView() {
        int title;
        switch (payId) {
            case 1://银行卡
                title = R.string.str_bank_card;
                accountNumberTv.setText(R.string.str_bank_id);
                accountNumberValEt.setHint(R.string.str_import_bank_card_account_number);
                break;
            case 2://微信支付
                title = R.string.str_wechat_pay;
                accountNumberTv.setText(R.string.str_account_number);
                accountNumberValEt.setHint(R.string.str_import_collection_account);
                break;
            case 3://支付宝
                title = R.string.str_alipay;
                accountNumberTv.setText(R.string.str_account_number);
                accountNumberValEt.setHint(R.string.str_import_collection_account);
                break;
            case 4:
                title = R.string.str_usdt_erc20;
                chainTypeValEt.setText(getString(title));
                break;
            case 5:
                title = R.string.str_usdt_trc20;
                chainTypeValEt.setText(getString(title));
                break;
            default:
                title = R.string.str_bank_card;
                break;
        }
        topbar.setTitle(title);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        if (isEdit) {
            topbar.addRightTextButton(R.string.str_delete, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delQMUIDialog == null) {
                        delQMUIDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_del_pay_way_title), String.format(getString(R.string.str_del_pay_way_msg), ObjectUtils.isEmpty(payInfoBean.getName()) ? "" : payInfoBean.getName())
                                , getString(R.string.str_cancel), getString(R.string.str_ok)
                                , new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                }, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        deletePayWay();
                                    }
                                });
                    }
                    delQMUIDialog.show();
                }
            });


        }
        if (dataBean != null) {
            setMailGroup(true);
            mMail = ObjectUtils.isEmpty(dataBean.getMail()) ? "" : dataBean.getMail();
            mailValEt.setText(mMail);
        }
        setVisibilityView(payId);
        ViewUtils.setEditableEditText(mailValEt, false);
        updateView();
    }

    @Override
    protected void initAction() {
        photoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object result) {
                picPath = (String) result;
                photoUtils.cropImage(AddPaymentFragment.this, picPath, QMUIDisplayHelper.getScreenWidth(getActivity()), QMUIDisplayHelper.getScreenWidth(getActivity()));
            }
        });

    }

    @OnClick({R.id.addCollectionQRLayout, R.id.saveBtn, R.id.sendCodeBtn})
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
                if (ObjectUtils.isEmpty(accountNumberValEt.getText())) {
                    ToastUtils.showLong(accountNumberValEt.getHint());
                    return;
                }
                if (payId == 1 || payId == 2 || payId == 3) {
                    if (ObjectUtils.isEmpty(userNameValEt.getText())) {
                        ToastUtils.showLong(R.string.str_import_collection_name);
                        return;
                    }
                    if (ObjectUtils.isEmpty(bankNameValEt.getText())) {
                        ToastUtils.showLong(bankNameValEt.getHint());
                        return;
                    }
                    if (ObjectUtils.isEmpty(bankSubbranchValEt.getText())) {
                        ToastUtils.showLong(bankSubbranchValEt.getHint());
                        return;
                    }
                }
                //1、银行卡；2、微信支付；3、支付宝；4、usdt trc20；5、usdt erc20；
                if (payId == 1) {
                    savePayWay();
                } else {
                    if (ObjectUtils.isNotEmpty(picPath)) {
                        uploadFile(picPath);
                    } else {
                        savePayWay();
                    }
                }
                break;
            case R.id.sendCodeBtn:
                sendVerificationCode();
                break;
        }
    }

    private void updateView() {
        if (payInfoBean != null) {
            isAdd = false;
            userNameValEt.setText(ObjectUtils.isEmpty(payInfoBean.getUsername()) ? "" : payInfoBean.getUsername());
            //1、银行卡；2、微信支付；3、支付宝；4、usdt trc20；5、usdt erc20；
            if (payId == 1) {
                bankIdValEt.setText(ObjectUtils.isEmpty(payInfoBean.getCardNum()) ? "" : payInfoBean.getCardNum());
                bankNameValEt.setText(ObjectUtils.isEmpty(payInfoBean.getName()) ? "" : payInfoBean.getName());
                bankSubbranchValEt.setText(ObjectUtils.isEmpty(payInfoBean.getBranch()) ? "" : payInfoBean.getBranch());
            } else if (payId == 3 || payId == 2) {
                isAddPic = true;
                accountNumberValEt.setText(ObjectUtils.isEmpty(payInfoBean.getCardNum()) ? "" : payInfoBean.getCardNum());
                deleteBtn.setVisibility(View.VISIBLE);
                picUrl = ObjectUtils.isEmpty(payInfoBean.getQrCode()) ? "" : payInfoBean.getQrCode();
                Glide.with(getActivity()).load(picUrl).error(R.drawable.placeholder).into(collectionQRValIv);
            } else if (payId == 5 || payId == 4) {
                accountNumberValEt.setText(ObjectUtils.isEmpty(payInfoBean.getCardNum()) ? "" : payInfoBean.getCardNum());
            }
        } else {
            isAdd = true;
        }
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
     * 添加/修改收款方式
     * 1、银行卡；2、微信支付；3、支付宝；4、usdt trc20；5、usdt erc20；
     */
    private void savePayWay() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("payId", String.valueOf(payId));
            if (payId == 1) {
                params.put("username", userNameValEt.getText().toString());
                params.put("cardNum", bankIdValEt.getText().toString());
                params.put("name", bankNameValEt.getText().toString());
                params.put("branch", bankSubbranchValEt.getText().toString());
            } else if (payId == 2 || payId == 3) {
                params.put("username", userNameValEt.getText().toString());
                params.put("cardNum", accountNumberValEt.getText().toString());
                params.put("qrCode", picUrl);
            } else if (payId == 4 || payId == 5) {
                params.put("username", chainTypeValEt.getText().toString());
                params.put("cardNum", accountNumberValEt.getText().toString());
//                params.put("qrCode", picUrl);
            }
            if (payInfoBean != null) {
                params.put("payInfoId", String.valueOf(payInfoBean.getPayInfoId()));
            }
            params.put("code", verificationValEt.getText().toString());
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
            params.put("mail", mailValEt.getText().toString());
            params.put("type", "3");
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
     * 设置商家信息
     * owner name
     * pay_methods 支付的支付方式
     * email 邮箱
     * memo_to_buyer 商家备注
     */
    private void setSeller() {
        try {
            List list = new ArrayList();
            list.add(payId);

            Map param = MapUtils.newHashMap();
            param.put("owner", mangoWallet.getWalletAddress());
            param.put("pay_methods", list);
            param.put("email", mMail);
            param.put("memo_to_buyer", "");
            String json = GsonUtils.toJson(param);
            LogUtils.dTag(LOG_TAG, "setSeller json = " + json);
            emWalletRepository.sendTransaction(SETSELLER, mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), MainActivity.deal_contract, json, walletType)
                    .subscribe(this::onSetSellerSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSetSellerSuccess(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                ToastUtils.showLong(R.string.str_chain_success);
                popBackStack();
            } else {
                ToastUtils.showLong(R.string.str_chain_fail);
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

    /**
     * 删除收款方式
     */
    private void deletePayWay() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("payInfoId", String.valueOf(payInfoBean.getPayInfoId()));
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().delPayWay(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onDelPayWaySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     */
    private void uploadFile(String filePath) {
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
            showTipDialog(getString(R.string.str_loading));
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
                        savePayWay();
                    }
                } else {
                    ToastUtils.showLong(updataFileBean.getMsg());
                }
            }
        }
    }

    private void onSavePayWaySuccess(JsonObject jsonObject) {

        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean isBindBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (isBindBean != null) {
                if (isBindBean.getCode() == 0) {
                    setSeller();
                    return;
                } else {

                    ToastUtils.showLong(isBindBean.getMsg());
                }
            }
        }
        dismissTipDialog();
    }

    private void onDelPayWaySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean isBindBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (isBindBean != null) {
                if (isBindBean.getCode() == 0) {
                    ToastUtils.showLong(R.string.str_delete_succeed);
                    popBackStack();
                } else {
                    ToastUtils.showLong(isBindBean.getMsg());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 200) {
            mImageList = Durban.parseResult(data);
            if (CollectionUtils.isNotEmpty(mImageList)) {
                picPath = mImageList.get(0);
                isAddPic = true;
                Glide.with(getActivity()).load(picPath).error(R.drawable.placeholder).into(collectionQRValIv);
                deleteBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    //    private void setPhonePayGroup(boolean isVisible) {
//        addCollectionQRLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        collectionQRTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//    }
//
//    private void setBankPayGroup(boolean isVisible) {
//        bankNameTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        bankNameValEt.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        bankSubbranchTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        bankSubbranchValEt.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//    }
//
    private void setMailGroup(boolean isVisible) {
        mailTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mailValEt.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        verificationTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        verificationValLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * @param type 1：银行；2：微信；3：支付宝；4：USDT-ERC20；5：USDT-TRC20；
     */
    private void setVisibilityView(int type) {
        if (type == 1) {
            userNameTv.setVisibility(View.VISIBLE);
            userNameValEt.setVisibility(View.VISIBLE);
            bankIdTv.setVisibility(View.VISIBLE);
            bankIdValEt.setVisibility(View.VISIBLE);
            bankNameTv.setVisibility(View.VISIBLE);
            bankNameValEt.setVisibility(View.VISIBLE);
            bankSubbranchTv.setVisibility(View.VISIBLE);
            bankSubbranchValEt.setVisibility(View.VISIBLE);

//            mailTv.setVisibility(View.VISIBLE);
//            mailValEt.setVisibility(View.VISIBLE);
//            verificationTv.setVisibility(View.VISIBLE);
//            verificationValLayout.setVisibility(View.VISIBLE);

            accountNumberTv.setVisibility(View.GONE);
            accountNumberValEt.setVisibility(View.GONE);
            chainTypeTv.setVisibility(View.GONE);
            chainTypeValEt.setVisibility(View.GONE);
            collectionAddressTv.setVisibility(View.GONE);
            collectionAddressValEt.setVisibility(View.GONE);
            collectionQRTv.setVisibility(View.GONE);
            addCollectionQRLayout.setVisibility(View.GONE);
        } else if (type == 2) {
            userNameTv.setVisibility(View.VISIBLE);
            userNameValEt.setVisibility(View.VISIBLE);

            bankIdTv.setVisibility(View.GONE);
            bankIdValEt.setVisibility(View.GONE);
            bankNameTv.setVisibility(View.GONE);
            bankNameValEt.setVisibility(View.GONE);
            bankSubbranchTv.setVisibility(View.GONE);
            bankSubbranchValEt.setVisibility(View.GONE);

//            mailTv.setVisibility(View.VISIBLE);
//            mailValEt.setVisibility(View.VISIBLE);
//            verificationTv.setVisibility(View.VISIBLE);
//            verificationValLayout.setVisibility(View.VISIBLE);

            accountNumberTv.setVisibility(View.VISIBLE);
            accountNumberValEt.setVisibility(View.VISIBLE);

            chainTypeTv.setVisibility(View.GONE);
            chainTypeValEt.setVisibility(View.GONE);
            collectionAddressTv.setVisibility(View.GONE);
            collectionAddressValEt.setVisibility(View.GONE);

            collectionQRTv.setVisibility(View.VISIBLE);
            addCollectionQRLayout.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            userNameTv.setVisibility(View.VISIBLE);
            userNameValEt.setVisibility(View.VISIBLE);

            bankIdTv.setVisibility(View.GONE);
            bankIdValEt.setVisibility(View.GONE);
            bankNameTv.setVisibility(View.GONE);
            bankNameValEt.setVisibility(View.GONE);
            bankSubbranchTv.setVisibility(View.GONE);
            bankSubbranchValEt.setVisibility(View.GONE);

//            mailTv.setVisibility(View.VISIBLE);
//            mailValEt.setVisibility(View.VISIBLE);
//            verificationTv.setVisibility(View.VISIBLE);
//            verificationValLayout.setVisibility(View.VISIBLE);

            accountNumberTv.setVisibility(View.VISIBLE);
            accountNumberValEt.setVisibility(View.VISIBLE);

            chainTypeTv.setVisibility(View.GONE);
            chainTypeValEt.setVisibility(View.GONE);
            collectionAddressTv.setVisibility(View.GONE);
            collectionAddressValEt.setVisibility(View.GONE);

            collectionQRTv.setVisibility(View.VISIBLE);
            addCollectionQRLayout.setVisibility(View.VISIBLE);
        } else if (type == 4) {
            userNameTv.setVisibility(View.GONE);
            userNameValEt.setVisibility(View.GONE);

            bankIdTv.setVisibility(View.GONE);
            bankIdValEt.setVisibility(View.GONE);
            bankNameTv.setVisibility(View.GONE);
            bankNameValEt.setVisibility(View.GONE);
            bankSubbranchTv.setVisibility(View.GONE);
            bankSubbranchValEt.setVisibility(View.GONE);

//            mailTv.setVisibility(View.VISIBLE);
//            mailValEt.setVisibility(View.VISIBLE);
//            verificationTv.setVisibility(View.VISIBLE);
//            verificationValLayout.setVisibility(View.VISIBLE);

            accountNumberTv.setVisibility(View.VISIBLE);
            accountNumberValEt.setVisibility(View.VISIBLE);

            chainTypeTv.setVisibility(View.VISIBLE);
            chainTypeValEt.setVisibility(View.VISIBLE);
            collectionAddressTv.setVisibility(View.GONE);
            collectionAddressValEt.setVisibility(View.GONE);

            collectionQRTv.setVisibility(View.VISIBLE);
            addCollectionQRLayout.setVisibility(View.VISIBLE);
        } else if (type == 5) {
            userNameTv.setVisibility(View.GONE);
            userNameValEt.setVisibility(View.GONE);

            bankIdTv.setVisibility(View.GONE);
            bankIdValEt.setVisibility(View.GONE);
            bankNameTv.setVisibility(View.GONE);
            bankNameValEt.setVisibility(View.GONE);
            bankSubbranchTv.setVisibility(View.GONE);
            bankSubbranchValEt.setVisibility(View.GONE);

//            mailTv.setVisibility(View.VISIBLE);
//            mailValEt.setVisibility(View.VISIBLE);
//            verificationTv.setVisibility(View.VISIBLE);
//            verificationValLayout.setVisibility(View.VISIBLE);

            accountNumberTv.setVisibility(View.VISIBLE);
            accountNumberValEt.setVisibility(View.VISIBLE);


            chainTypeValEt.setVisibility(View.VISIBLE);
            collectionAddressTv.setVisibility(View.GONE);
            collectionAddressValEt.setVisibility(View.GONE);

            collectionQRTv.setVisibility(View.VISIBLE);
            addCollectionQRLayout.setVisibility(View.VISIBLE);
        }
//        chainTypeTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        chainTypeValEt.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        collectionAddressTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        verificationTv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        sendCodeBtn.setVisibility(isVisible ? View.VISIBLE : View.GONE);
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
