package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.NRSAUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.ETH_TOKEN;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_FIRST;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.HMIO_TOKEN;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.USDT_TOKEN;

public class OperatingStepsFragment extends BaseFragment {


    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.tokenAddressTv)
    AppCompatTextView tokenAddressTv;
    @BindView(R.id.arrowsIv1)
    AppCompatImageView arrowsIv1;
    @BindView(R.id.collectionAddressTv)
    AppCompatTextView collectionAddressTv;
    @BindView(R.id.arrowsIv2)
    AppCompatImageView arrowsIv2;
    @BindView(R.id.hashEt)
    AppCompatEditText hashEt;
    @BindView(R.id.submitBtn)
    QMUIRoundButton submitBtn;
    @BindView(R.id.unpaidValueTv)
    AppCompatTextView unpaidValueTv;
    @BindView(R.id.unpaidUnitTv)
    AppCompatTextView unpaidUnitTv;
    @BindView(R.id.secondExplainTv1)
    AppCompatTextView secondExplainTv1;
    @BindView(R.id.secondExplain2Tv)
    AppCompatTextView secondExplain2Tv;

    public static final int FIRST_MIX_MORTGAGE_TYPE = 0;//混合抵押hash 首次上传;从抵押入口进入 first
    public static final int TWICE_MIX_MORTGAGE_TYPE = 1;//混合抵押hash二次上传;从混合抵押入口进入  twice
    public static final int GOODS_PAYMENT_TYPE = 2;//商品混合支付;从支付商品价格入口进入 payment
    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private String moneyType = "4";
    private String num;
    private String id;
    private String SYMBOL;
    private String CollectionAddress;
    private String orderID;
    private int type;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_operating_steps, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        type = bundle.getInt("type", -1);
        walletAddress = mangoWallet.getWalletAddress();
        num = bundle.getString("num", "");
        SYMBOL = bundle.getString("SYMBOL", "");
        if (type == TWICE_MIX_MORTGAGE_TYPE) {
            id = bundle.getString("id", "");
        } else if (type == GOODS_PAYMENT_TYPE) {
            orderID = bundle.getString("OrderID", "");
            CollectionAddress = bundle.getString("CollectionAddress", "");
        }
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_operating_step);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        if (type == GOODS_PAYMENT_TYPE) {
            tokenAddressTv.setText(USDT_TOKEN);
            collectionAddressTv.setText(CollectionAddress);
        } else {
            tokenAddressTv.setText(HMIO_TOKEN);
            collectionAddressTv.setText(ETH_TOKEN);
        }
        secondExplainTv1.setText(String.format(getString(R.string.str_second_explain_t1), SYMBOL));
        secondExplain2Tv.setText(String.format(getString(R.string.str_second_explain2), SYMBOL));
        unpaidValueTv.setText(num);
        unpaidUnitTv.setText(SYMBOL);
    }

    @Override
    protected void initAction() {

    }


    @OnClick({R.id.secondExplainTv1, R.id.tokenAddressTv, R.id.arrowsIv1, R.id.secondExplainTv3, R.id.collectionAddressTv, R.id.arrowsIv2, R.id.submitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.secondExplainTv1:
            case R.id.tokenAddressTv:
            case R.id.arrowsIv1:
                ClipboardUtils.copyText(ObjectUtils.isNotEmpty(tokenAddressTv.getText()) ? tokenAddressTv.getText().toString() : "");
                ToastUtils.showLong(R.string.str_copy_success);
                break;
            case R.id.secondExplainTv3:
            case R.id.collectionAddressTv:
            case R.id.arrowsIv2:
                ClipboardUtils.copyText(ObjectUtils.isNotEmpty(collectionAddressTv.getText()) ? collectionAddressTv.getText().toString() : "");
                ToastUtils.showLong(R.string.str_copy_success);
                break;
            case R.id.submitBtn:
                uploadHash();
                break;
        }
    }

    private void uploadHash() {
        if (ObjectUtils.isEmpty(hashEt.getText())) {
            ToastUtils.showLong(getString(R.string.str_please_import) + " hash");
            return;
        }
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        params.put("moneyType", moneyType);// 需支付币种 4 HMIO ，1 MGP
        params.put("hash", hashEt.getText().toString().trim());
        if (type == FIRST_MIX_MORTGAGE_TYPE) {
            params.put("num", num);
        } else if (type == TWICE_MIX_MORTGAGE_TYPE) {
            params.put("id", id);
        } else if (type == GOODS_PAYMENT_TYPE) {
            params.clear();
            params.put("mgpName", walletAddress);
            params.put("orderId", orderID);
            params.put("hash", hashEt.getText().toString().trim());
        }
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            Observable<JsonObject> observable = null;
            if (type == FIRST_MIX_MORTGAGE_TYPE) {
                observable = NetWorkManager.getRequest().uploadHash(content);
            } else if (type == TWICE_MIX_MORTGAGE_TYPE) {
                observable = NetWorkManager.getRequest().editHash(content);
            } else if (type == GOODS_PAYMENT_TYPE) {
                observable = NetWorkManager.getRequest().addRecord(content);
            }
            if (observable != null) {
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::uploadHashSuccess, this::onError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadHashSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                if (type == GOODS_PAYMENT_TYPE) {
                    popBackStack();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("MixMortgageFragment", bundle);
                }
                ToastUtils.showLong(R.string.str_submit_success);
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }
}
