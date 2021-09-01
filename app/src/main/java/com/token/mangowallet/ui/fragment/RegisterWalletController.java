package com.token.mangowallet.ui.fragment;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.listener.ControlListener;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.token.mangowallet.utils.Constants.TO_CREATE_WALLET;

public class RegisterWalletController extends QMUIWindowInsetLayout {

    @BindView(R.id.qrCodeIv)
    AppCompatImageView qrCodeIv;
    @BindView(R.id.accountNameTv)
    AppCompatTextView accountNameTv;
    @BindView(R.id.accountNameEt)
    AppCompatEditText accountNameEt;
    @BindView(R.id.ownerTv)
    AppCompatTextView ownerTv;
    @BindView(R.id.ownerPublickeyTv)
    AppCompatTextView ownerPublickeyTv;
    @BindView(R.id.activeTv)
    AppCompatTextView activeTv;
    @BindView(R.id.activePublickeyTv)
    AppCompatTextView activePublickeyTv;
    @BindView(R.id.shareQrCodeBtn)
    QMUIRoundButton shareQrCodeBtn;
    @BindView(R.id.errorTv)
    AppCompatTextView errorTv;

    private Bitmap qrCodeBitmap;
    private Map map = MapUtils.newHashMap();
    private String json;
    private String publicKey;
    protected ControlListener mControlListener;
    private SelectOrRegisterWalletFragment fragment;
    private EMWalletRepository emWalletRepository;

    public RegisterWalletController(SelectOrRegisterWalletFragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        LayoutInflater.from(fragment.getActivity()).inflate(R.layout.view_register_wallet, this);
        ButterKnife.bind(this);
        initView();
        emWalletRepository = new EMWalletRepository();
    }

    public void initView() {
        publicKey = fragment.mangoWallet.getPublicKey();
        map.put("active", publicKey);
        map.put("blockchain", Constants.WalletType.getPagerFromPositon(fragment.mangoWallet.getWalletType()));
        map.put("owner", publicKey);
        map.put("type", TO_CREATE_WALLET);
        accountNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String account = s.toString().trim();
                boolean isNamePass = RegexUtils.isMatch(Constants.REGEX_ACCOUNT_NAME, account);
                if (isNamePass) {
                    getAccountInfo(account);
                } else {
                    errorTv.setText(fragment.getString(R.string.str_account_name_rule));
                    errorTv.setVisibility(VISIBLE);
                }
                if (account.length() == 12 && !isNamePass) {
                    ToastUtils.showLong(R.string.str_account_name_rule);
                }
                if (errorTv.getVisibility() == GONE) {
                    qrCodeIv.setVisibility(VISIBLE);
                    shareQrCodeBtn.setEnabled(true);
                    shareQrCodeBtn.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(), R.color.blueColor));
                } else {
                    qrCodeIv.setVisibility(INVISIBLE);
                    shareQrCodeBtn.setEnabled(false);
                    shareQrCodeBtn.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(), R.color.item_divider_bg_color));
                }
            }
        });

        ownerPublickeyTv.setText(publicKey);
        activePublickeyTv.setText(publicKey);
    }

    public void getAccountInfo(String walletAddress) {
        emWalletRepository.fetchAccountInfo(walletAddress, fragment.walletType).subscribe(this::accountInfoSuccess, this::onError);
    }

    public void accountInfoSuccess(AccountInfo accountInfo) {
        errorTv.setText(fragment.getString(R.string.str_account_invalid));
        errorTv.setVisibility(VISIBLE);
        qrCodeIv.setVisibility(INVISIBLE);
        shareQrCodeBtn.setEnabled(false);
        shareQrCodeBtn.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(), R.color.item_divider_bg_color));
    }

    public void onError(Throwable e) {
        map.put("account", accountNameEt.getText().toString().trim());
        json = GsonUtils.toJson(map);
        qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(json, SizeUtils.getMeasuredHeight(qrCodeIv));
        qrCodeIv.setImageBitmap(qrCodeBitmap);
        qrCodeIv.setVisibility(VISIBLE);
        shareQrCodeBtn.setEnabled(true);
        shareQrCodeBtn.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(), R.color.blueColor));
        errorTv.setVisibility(GONE);
    }

    public void destroy() {
        if (qrCodeBitmap != null) {
            qrCodeBitmap.recycle();
        }
        qrCodeBitmap = null;
    }

    @OnClick(R.id.shareQrCodeBtn)
    public void onViewClicked() {
        if (mControlListener != null) {
            mControlListener.onShare(qrCodeBitmap);
        }
    }

    public void setControlListener(ControlListener controlListener) {
        mControlListener = controlListener;
    }
}
