package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.GoodsImgeModel;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.entity.ImgSection;
import com.token.mangowallet.bean.entity.UploadImgBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.PhotoUtils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.UriToPathUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.PictureChoicePop;
import com.token.mangowallet.view.RequiredTextView;

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

import static com.token.mangowallet.bean.entity.ImgSection.GOODS_SLIDESHOW;
import static com.token.mangowallet.utils.Constants.EXTRA_STORE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.PhotoUtils.PICTURE_CROP_CODE;

public class RecruitmentFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.realNameTv)
    RequiredTextView realNameTv;
    @BindView(R.id.realNameEt)
    AppCompatEditText realNameEt;
    @BindView(R.id.phoneNumberTv)
    RequiredTextView phoneNumberTv;
    @BindView(R.id.phoneNumberEt)
    AppCompatEditText phoneNumberEt;
    @BindView(R.id.IDNumberTv)
    RequiredTextView IDNumberTv;
    @BindView(R.id.IDNumberEt)
    AppCompatEditText IDNumberEt;
    @BindView(R.id.frontIDCardTv)
    RequiredTextView frontIDCardTv;
    @BindView(R.id.frontIDCardIv)
    AppCompatImageView frontIDCardIv;
    @BindView(R.id.reverseIDCardIv)
    AppCompatImageView reverseIDCardIv;
    @BindView(R.id.handIDCardTv)
    RequiredTextView handIDCardTv;
    @BindView(R.id.handIDCardIv)
    AppCompatImageView handIDCardIv;
    @BindView(R.id.socialCreditCodeTv)
    RequiredTextView socialCreditCodeTv;
    @BindView(R.id.socialCreditCodeEt)
    AppCompatEditText socialCreditCodeEt;
    @BindView(R.id.photoBusinessLicenseTv)
    RequiredTextView photoBusinessLicenseTv;
    @BindView(R.id.photoBusinessLicenseIv)
    AppCompatImageView photoBusinessLicenseIv;
    @BindView(R.id.nextStepBtn)
    QMUIRoundButton nextStepBtn;
    @BindView(R.id.delBtn1)
    AppCompatImageView delBtn1;
    @BindView(R.id.delBtn2)
    AppCompatImageView delBtn2;
    @BindView(R.id.delBtn3)
    AppCompatImageView delBtn3;
    @BindView(R.id.delBtn4)
    AppCompatImageView delBtn4;

    private Unbinder unbinder;
    private PhotoUtils mPhotoUtils;
    private PictureChoicePop pictureChoicePop;
    private String frontIdentityPath;//身份证正面
    private String reverseIdentityPath;//身份证反面
    private String handIdentityPath;//手持身份证
    private String businessLicensePath;//营业执照证件照
    private boolean isTakePhoto;
    private boolean isFrontIdentity;
    private boolean isReverseIdentity;
    private boolean isHandIdentity;
    private boolean isBusinessLicense;
    private Uri pictureUri;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private List<String> goodsImgeList;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recruitment, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        mPhotoUtils = new PhotoUtils();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_merchant_settled);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        pictureChoicePop = new PictureChoicePop(this);
        pictureChoicePop.createPopup();
        pictureChoicePop.setAnimationStyle(R.style.dialogSlideAnim);
    }

    @Override
    protected void initAction() {
        pictureChoicePop.setOnPictureChoiceListener(new PictureChoicePop.OnPictureChoiceListener() {
            @Override
            public void onCamera(View v) {
                isTakePhoto = true;
                startQrCode();
            }

            @Override
            public void onAlbumSelect(View v) {
                isTakePhoto = false;
                startQrCode();
            }

            @Override
            public void onCancel(View v) {
                pictureChoicePop.dismiss();
            }
        });

        mPhotoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object result) {
                String filePath = (String) result;
                File file = new File(filePath);
                pictureUri = AppFilePath.getUri(getActivity(), file);
                if (isFrontIdentity) {
                    frontIdentityPath = filePath;
                    Glide.with(getActivity()).load(frontIdentityPath).into(frontIDCardIv);
                    delBtn1.setVisibility(View.VISIBLE);
                    reverseIDCardIv.setVisibility(View.VISIBLE);
                }
                if (isReverseIdentity) {
                    reverseIdentityPath = filePath;
                    Glide.with(getActivity()).load(reverseIdentityPath).into(reverseIDCardIv);
                    delBtn2.setVisibility(View.VISIBLE);
                }
                if (isHandIdentity) {
                    handIdentityPath = filePath;
                    Glide.with(getActivity()).load(handIdentityPath).into(handIDCardIv);
                    delBtn3.setVisibility(View.VISIBLE);
                }
                if (isBusinessLicense) {
                    businessLicensePath = filePath;
                    Glide.with(getActivity()).load(businessLicensePath).into(photoBusinessLicenseIv);
                    delBtn4.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.frontIDCardIv, R.id.reverseIDCardIv, R.id.handIDCardIv, R.id.photoBusinessLicenseIv, R.id.nextStepBtn, R.id.delBtn1, R.id.delBtn2, R.id.delBtn3, R.id.delBtn4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frontIDCardIv:
                isFrontIdentity = true;
                isReverseIdentity = false;
                isHandIdentity = false;
                isBusinessLicense = false;
                pictureChoicePop.showPop(view);
                break;
            case R.id.reverseIDCardIv:
                isFrontIdentity = false;
                isReverseIdentity = true;
                isHandIdentity = false;
                isBusinessLicense = false;
                pictureChoicePop.showPop(view);
                break;
            case R.id.handIDCardIv:
                isFrontIdentity = false;
                isReverseIdentity = false;
                isHandIdentity = true;
                isBusinessLicense = false;
                pictureChoicePop.showPop(view);
                break;
            case R.id.photoBusinessLicenseIv:
                isFrontIdentity = false;
                isReverseIdentity = false;
                isHandIdentity = false;
                isBusinessLicense = true;
                pictureChoicePop.showPop(view);
                break;
            case R.id.nextStepBtn:
                if (isVerify()) {
                    uploadImge();
                }
                break;
            case R.id.delBtn1:
//                 frontIdentityPath;//身份证正面
//                reverseIdentityPath;//身份证反面
//                handIdentityPath;//手持身份证
//               businessLicensePath;//营业执照证件照
                Glide.with(getActivity()).load(R.mipmap.ic_add_pic).into(frontIDCardIv);
                delBtn1.setVisibility(View.GONE);
                frontIdentityPath = "";
                break;
            case R.id.delBtn2:
                Glide.with(getActivity()).load(R.mipmap.ic_add_pic).into(reverseIDCardIv);
                delBtn2.setVisibility(View.GONE);
                reverseIdentityPath = "";
                break;
            case R.id.delBtn3:
                Glide.with(getActivity()).load(R.mipmap.ic_add_pic).into(handIDCardIv);
                delBtn3.setVisibility(View.GONE);
                handIdentityPath = "";
                break;
            case R.id.delBtn4:
                Glide.with(getActivity()).load(R.mipmap.ic_add_pic).into(photoBusinessLicenseIv);
                delBtn4.setVisibility(View.GONE);
                businessLicensePath = "";
                break;
        }

        if (delBtn1.getVisibility() == View.GONE && delBtn2.getVisibility() == View.GONE) {
            reverseIDCardIv.setVisibility(View.GONE);
        }
    }

    /**
     * 开始扫码
     */
    public void startQrCode() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(getActivity(), shouldRequest);
                    }
                }).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                if (isTakePhoto) {
                    mPhotoUtils.takePhoto(getActivity());
                } else {
                    mPhotoUtils.albumSelect(getActivity(), false);
                }
                pictureChoicePop.dismiss();
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

            }
        }).request();
    }


    private void uploadImge() {
        showTipDialog(getString(R.string.str_loading));
        try {
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
//                 frontIdentityPath;//身份证正面
//                reverseIdentityPath;//身份证反面
//                handIdentityPath;//手持身份证
//               businessLicensePath;//营业执照证件照
            List<String> imgList = new ArrayList<>();
            imgList.add(frontIdentityPath);
            imgList.add(reverseIdentityPath);
            imgList.add(handIdentityPath);
            imgList.add(businessLicensePath);
            for (int i = 0; i < imgList.size(); i++) {
                String filePath = imgList.get(i);
                File file = new File(filePath);
                if (!file.exists()) {
                    LogUtils.d("找不到该文件");
                    continue;
                }
                mMultipartBodyBuilder.addFormDataPart("file", URLEncoder.encode(file.getName(), "UTF-8")
                        , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("multipart/form-data")));
            }
            MultipartBody multipartBody = mMultipartBodyBuilder.build();
            if (multipartBody.size() <= 0) {
                return;
            }
            NetWorkManager.getRequest().upload(multipartBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::uploadImgeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * address		钱包账户
     * userName		真实姓名
     * identityCard		身份证
     * socialCode		统一社会信用代码
     * businessLicense		营业执照照片
     * identityCardPhoto		身份证正反面
     * handCardPhoto	手持身份证
     * phone	电话
     */
    private void addUserInfo() {
        String frontIdentityPath = goodsImgeList.get(0);//身份证正面
        String reverseIdentityPath = goodsImgeList.get(1);//身份证反面
        String handIdentityPath = goodsImgeList.get(2);//手持身份证
        String businessLicense = goodsImgeList.get(3);
        List list = new ArrayList();
        list.add(frontIdentityPath);
        list.add(reverseIdentityPath);
        String identityCardPhoto = GsonUtils.toJson(list);
        list.clear();
        list.add(handIdentityPath);
        handIdentityPath = GsonUtils.toJson(list);
        list.clear();
        list.add(businessLicense);
        businessLicense = GsonUtils.toJson(list);
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("userName", realNameEt.getText().toString());
        params.put("identityCard", IDNumberEt.getText().toString());
        params.put("socialCode", socialCreditCodeEt.getText().toString());
        params.put("businessLicense", businessLicense);
        params.put("identityCardPhoto", identityCardPhoto);
        params.put("handCardPhoto", handIdentityPath);
        params.put("phone", phoneNumberEt.getText().toString());
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().addUserInfo(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addUserInfoSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUserInfoSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                bundle.putParcelable(EXTRA_STORE, null);
                startFragment("EditStoreFragment", bundle);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void uploadImgeSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            UploadImgBean uploadImgBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), UploadImgBean.class);
            if (uploadImgBean.getCode() == 0) {
                goodsImgeList = uploadImgBean.getData();
                if (ObjectUtils.isNotEmpty(goodsImgeList)) {
                    addUserInfo();
                }
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    private boolean isVerify() {
        String str1 = getString(R.string.str_please_import);
        String msg = "";
        boolean isVer = true;
        if (ObjectUtils.isEmpty(realNameEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_real_name);
        } else if (ObjectUtils.isEmpty(phoneNumberEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_phone_number);
        } else if (ObjectUtils.isEmpty(IDNumberEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_ID_number);
        } else if (ObjectUtils.isEmpty(socialCreditCodeEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_social_credit_code);
        } else if (ObjectUtils.isEmpty(frontIdentityPath)) {
            isVer = false;
            msg = str1 + getString(R.string.str_identity_card_picture);
        } else if (ObjectUtils.isEmpty(reverseIdentityPath)) {
            isVer = false;
            msg = str1 + getString(R.string.str_identity_card_picture);
        } else if (ObjectUtils.isEmpty(handIdentityPath)) {
            isVer = false;
            msg = str1 + getString(R.string.str_hand_identity_card);
        } else if (ObjectUtils.isEmpty(businessLicensePath)) {
            isVer = false;
            msg = str1 + getString(R.string.str_photo_business_license);
        }
        if (ObjectUtils.isNotEmpty(msg))
            ToastUtils.showLong(msg);
        return isVer;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.dTag(LOG_TAG, "requestCode = " + requestCode + " resultCode = " + resultCode);
        if (requestCode == PICTURE_CROP_CODE) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}
