package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.DealsOrderBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.UpdataFileBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.Constants;
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

import static com.token.mangowallet.utils.Constants.BACKDEAL_ORDER;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class SpendConfirmFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.remarkEt)
    AppCompatEditText remarkEt;
    @BindView(R.id.collectionQRValIv)
    AppCompatImageView collectionQRValIv;
    @BindView(R.id.deleteBtn)
    AppCompatImageView deleteBtn;
    @BindView(R.id.addCollectionQRLayout)
    FrameLayout addCollectionQRLayout;
    @BindView(R.id.uploadDocumentsTv)
    AppCompatTextView uploadDocumentsTv;
    @BindView(R.id.buyerSpendConfirmBtn)
    QMUIRoundButton buyerSpendConfirmBtn;


    private Unbinder unbinder;
    private Bundle bundle;
    private MangoWallet mangoWallet;
    private DealsOrderBean.RowsBean mRowsBean;
    private PhotoUtils photoUtils;
    private String picUrl = "";
    private boolean isAddPic = true;
    private String picPath = "";
    public EMWalletRepository emWalletRepository;
    private Constants.WalletType walletType;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirm_account_paid, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        mRowsBean = bundle.getParcelable("RowsBean");
        photoUtils = new PhotoUtils();
        emWalletRepository = new EMWalletRepository();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        setArbitrationSave();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_buyer_spend_confirm);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    @Override
    protected void initAction() {
        photoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object result) {
                picPath = (String) result;
                Glide.with(getActivity()).load(picPath).error(R.drawable.placeholder).into(collectionQRValIv);
            }
        });

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

    private void setArbitrationSave() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("arbitrator", mangoWallet.getWalletAddress());
            params.put("buyer", mRowsBean.getOrder_taker());
            params.put("createTime", mRowsBean.getCreated_at());
            params.put("id", mRowsBean.getId());
            params.put("img", picUrl);
            params.put("remark", remarkEt.getText().toString());
            params.put("seller", mRowsBean.getOrder_maker());

            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().arbitrationSave(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onArbitrationSaveSuccess, this::onError);
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
                        setArbitrationSave();
                    }
                } else {
                    ToastUtils.showLong(updataFileBean.getMsg());
                }
            }
        }
    }

    /**
     * 款项异常（订单变成代付款）
     * <p>
     * user_type  0 ：重启待放行 1 重启代付款
     * //orderStatus 订单状态：0:代付款;1:超时取消;2:待放行;3:放行超时;4:交易完成;5:交易取消;
     */
    private void sendBackdealOrder() {
        if (mRowsBean != null) {
            try {
                showTipDialog(getString(R.string.str_loading));
                Map param = MapUtils.newHashMap();
                param.put("owner", mangoWallet.getWalletAddress());
                param.put("deal_id", String.valueOf(mRowsBean.getId()));//交易订单id
                String json = GsonUtils.toJson(param);
                emWalletRepository.sendTransaction(BACKDEAL_ORDER, mangoWallet.getPrivateKey(), mangoWallet.getWalletAddress(), MainActivity.deal_contract, json, walletType)
                        .subscribe(this::onBackdealOrderSuccess, this::onError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onBackdealOrderSuccess(TransactionBean transactionBean) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(transactionBean)) {
            if (transactionBean.isSuccess) {
                ToastUtils.showLong(R.string.str_operate_success);
            } else {
                ToastUtils.showLong(transactionBean.msg);
            }
        }
    }

    private void onArbitrationSaveSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            sendBackdealOrder();
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

    @OnClick({R.id.addCollectionQRLayout, R.id.buyerSpendConfirmBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addCollectionQRLayout:
                if (isAddPic) {
                    deleteBtn.setVisibility(View.VISIBLE);
                    photoUtils.albumSelect(getActivity(), true);
                } else {
                    deleteBtn.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(R.mipmap.ic_add_pic).error(R.drawable.placeholder).into(collectionQRValIv);
                    isAddPic = true;
                }
                break;
            case R.id.buyerSpendConfirmBtn:
                if (ObjectUtils.isEmpty(picPath)) {
                    ToastUtils.showLong(R.string.str_please_upload_pictures);
                    return;
                }
                if (ObjectUtils.isEmpty(remarkEt.getText())) {
                    ToastUtils.showLong(R.string.str_remark_operation_reason);
                    return;
                }
                uploadFile(picPath);
                break;
        }
    }
}
