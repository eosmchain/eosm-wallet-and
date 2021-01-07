package com.token.mangowallet.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.GlobalBean;
import com.token.mangowallet.bean.GoodsImgeModel;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.entity.ImgSection;
import com.token.mangowallet.bean.entity.UploadImgBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.InputFilterMinMax;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.PhotoUtils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.CashierInputFilter;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.PictureChoicePop;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.token.mangowallet.bean.entity.ImgSection.GOODS_SLIDESHOW;
import static com.token.mangowallet.ui.fragment.StakeVoteMainFragment.mVoteContract;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.percent;

public class StakeAddVoteFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.voteNameEt)
    AppCompatEditText voteNameEt;
    @BindView(R.id.ratioEt)
    AppCompatEditText ratioEt;
    @BindView(R.id.nodeNameEt)
    AppCompatEditText nodeNameEt;
    @BindView(R.id.nodeRewardRulesEt)
    AppCompatEditText nodeRewardRulesEt;
    @BindView(R.id.importNumTv)
    AppCompatTextView importNumTv;
    @BindView(R.id.associationIntroduceEt)
    AppCompatEditText associationIntroduceEt;
    @BindView(R.id.importNumTv2)
    AppCompatTextView importNumTv2;
    @BindView(R.id.imageIv)
    AppCompatImageView imageIv;
    @BindView(R.id.paymentBtn)
    QMUIRoundButton paymentBtn;
    @BindView(R.id.delBtn)
    AppCompatImageView delBtn;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private PhotoUtils mPhotoUtils;
    private PictureChoicePop pictureChoicePop;
    private boolean isTakePhoto;
    private Uri pictureUri;
    private boolean isHasHeader = false;
    private EMWalletRepository emWalletRepository;
    private BigDecimal balance;
    private String toPayNum = "";
    private QMUIDialog passwordQmuiDialog;
    private String mHeaderImge = "";
    private String filePath = "";
    public GlobalBean.RowsBean mGlobalRowsBean;
    private BigDecimal ratioDecimal = BigDecimal.ZERO;
    private boolean isAdd;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_stake_vote, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        emWalletRepository = new EMWalletRepository();
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        isAdd = bundle.getBoolean("isAdd", false);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        if (isAdd) {
            mGlobalRowsBean = bundle.getParcelable("mGlobalRowsBean");
            paymentBtn.setText(getString(R.string.str_payment) + mGlobalRowsBean.getMin_bp_list_quantity());
        } else {
            paymentBtn.setText(getString(R.string.str_submit));
        }
        mPhotoUtils = new PhotoUtils();
        pictureChoicePop = new PictureChoicePop(this);
        pictureChoicePop.createPopup();
        pictureChoicePop.setAnimationStyle(R.style.dialogSlideAnim);
        getEOSMGPBalance();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_add_node);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        ratioEt.setFilters(new InputFilter[]{new CashierInputFilter(percent, 2)});
    }

    @Override
    protected void initAction() {
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasHeader) {
                    mHeaderImge = "";
                    filePath = "";
                    isHasHeader = false;
                    delBtn.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(R.mipmap.ic_add_pic).into(imageIv);
                } else {
                    pictureChoicePop.showPop(v);
                }
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeaderImge = "";
                filePath = "";
                isHasHeader = false;
                delBtn.setVisibility(View.GONE);
                Glide.with(getActivity()).load(R.mipmap.ic_add_pic).into(imageIv);
            }
        });
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
                isHasHeader = true;
                filePath = (String) result;
                delBtn.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(filePath).into(imageIv);
//                File file = new File(filePath);
//                pictureUri = AppFilePath.getUri(getActivity(), file);

            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVerify()) {
                    if (isAdd) {
                        toPayNum = ObjectUtils.isEmpty(mGlobalRowsBean.getMin_bp_list_quantity()) ? "0.0000 MGP" : mGlobalRowsBean.getMin_bp_list_quantity();
                        BigDecimal toPayNumDecimal = new BigDecimal(toPayNum.split(" ")[0]);
                        if (toPayNumDecimal.compareTo(balance) > 0) {//-1表示小于，0是等于，1是大于。
                            ToastUtils.showLong(R.string.str_cannot_greater_balance);
                            return;
                        }

                        if (passwordQmuiDialog == null) {
                            passwordQmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                    getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                        }
                        passwordQmuiDialog.show();
                    } else {
                        uploadImge(filePath);
                    }
                }
            }
        });

        nodeRewardRulesEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                importNumTv.setText(s.length() + "/150");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        associationIntroduceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                importNumTv2.setText(s.length() + "/150");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(text), mangoWallet.getWalletPassword())) {
                uploadImge(filePath);
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

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

    private void uploadImge(String filePath) {
        showTipDialog(getString(R.string.str_loading));
        try {
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            File file = new File(filePath);
            if (!file.exists()) {
                LogUtils.d("找不到该文件");
                return;
            }
            mMultipartBodyBuilder.addFormDataPart("file", URLEncoder.encode(file.getName(), "UTF-8")
                    , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("multipart/form-data")));
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

    private void uploadNodeMsg(String hash) {
        ratioDecimal = new BigDecimal(ratioEt.getText().toString()).multiply(percent);
        Map params = MapUtils.newHashMap();
        params.put("mgpAddress", walletAddress);
        params.put("nodeContent", ObjectUtils.isEmpty(associationIntroduceEt.getText()) ? "" : associationIntroduceEt.getText().toString());
        params.put("nodeHeadImg", mHeaderImge);
        params.put("nodeName", voteNameEt.getText().toString());
        params.put("nodeRewardRule", nodeRewardRulesEt.getText().toString());
        params.put("nodeShareRatio", ratioDecimal.stripTrailingZeros().toPlainString());
        params.put("hash", hash);
        params.put("nodeUrl", nodeNameEt.getText().toString());
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().uploadNodeMsg(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::uploadNodeMsgSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadNodeMsgSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_add_vote_succeed);
                popBackStack();
            } else {
                ToastUtils.showLong(R.string.str_add_vote_fail);
            }
        } else {
            ToastUtils.showLong(R.string.str_add_vote_fail);
        }
    }

    private void getEOSMGPBalance() {
        try {
            emWalletRepository.fetchBalance(walletAddress, walletType).subscribe(this::balanceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVoteTransaction() {

//        showTipDialog(getString(R.string.str_loading));
//@{@"owner":VALIDATE_STRING([MGPHttpRequest shareManager].curretWallet.address),@"vote_id":dic[@"id"]}
        ratioDecimal = new BigDecimal(ratioEt.getText().toString()).multiply(percent);

        String memo = "list:" + ratioDecimal.intValue();//1000-->10000

        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", mVoteContract);
        params.put("quantity", mGlobalRowsBean.getMin_bp_list_quantity());
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onAddVoteTransaction, this::onError);
    }

    private void balanceSuccess(BigDecimal balance) {
        this.balance = balance;
    }

    private void uploadImgeSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            UploadImgBean uploadImgBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), UploadImgBean.class);
            if (uploadImgBean.getCode() == 0) {
                List<String> goodsImgeList = uploadImgBean.getData();
                for (int i = 0; i < goodsImgeList.size(); i++) {
                    mHeaderImge = goodsImgeList.get(i);
                    if (isAdd) {
                        addVoteTransaction();
                    } else {
                        uploadNodeMsg("");
                    }
                }
            } else {
                dismissTipDialog();
                ToastUtils.showLong(R.string.str_add_vote_fail);
            }
        } else {
            dismissTipDialog();
            ToastUtils.showLong(R.string.str_add_vote_fail);
        }
    }

    private void onAddVoteTransaction(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                uploadNodeMsg(transactionBean.msg);
            } else {
                dismissTipDialog();
                ToastUtils.showLong(R.string.str_add_vote_fail);
            }
        } else {
            ToastUtils.showLong(R.string.str_add_vote_fail);
            dismissTipDialog();
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private boolean isVerify() {
        boolean isVerify = false;
        String msg = "";
        if (ObjectUtils.isEmpty(voteNameEt.getText())) {
            msg = getString(R.string.str_node_name_import);
        } else if (ObjectUtils.isEmpty(ratioEt.getText())) {
            msg = getString(R.string.str_proportion_import);
        } else if (ObjectUtils.isEmpty(nodeNameEt.getText())) {
            msg = getString(R.string.str_node_import);
        } else if (ObjectUtils.isEmpty(nodeRewardRulesEt.getText())) {
            msg = getString(R.string.str_node_reward_rules_import);
        } else if (!isHasHeader) {
            msg = getString(R.string.str_node_cover);
        } else {
            isVerify = true;
        }
        if (ObjectUtils.isNotEmpty(msg)) {
            ToastUtils.showLong(msg);
        }
        return isVerify;
    }
}
