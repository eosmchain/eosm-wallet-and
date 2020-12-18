package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentEffectHandler;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.GetOrderIDBean;
import com.token.mangowallet.bean.OrderIDBean;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.bean.ShippingAddressBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bus.ShippingAddressEffect;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.CustomizeGoodsAddView;
import com.token.mangowallet.view.DialogHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.ui.fragment.OperatingStepsFragment.GOODS_PAYMENT_TYPE;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_GOODS_INFO;
import static com.token.mangowallet.utils.Constants.EXTRA_ISADD_ADDRESS;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;
import static com.token.mangowallet.utils.Constants.SHOP_ACCOUNT;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static java.math.RoundingMode.HALF_UP;

public class OrderConfirmFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.addressIv)
    AppCompatImageView addressIv;
    @BindView(R.id.addressNameTv)
    AppCompatTextView addressNameTv;
    @BindView(R.id.addressTv)
    AppCompatTextView addressTv;
    @BindView(R.id.addressLayout)
    ConstraintLayout addressLayout;
    @BindView(R.id.goodsPicIv)
    AppCompatImageView goodsPicIv;
    @BindView(R.id.goodsNameTv)
    AppCompatTextView goodsNameTv;
    @BindView(R.id.specificationTv)
    AppCompatTextView specificationTv;
    @BindView(R.id.priceTv)
    AppCompatTextView priceTv;
    @BindView(R.id.goodsAddView)
    CustomizeGoodsAddView goodsAddView;
    @BindView(R.id.freightTTv)
    AppCompatTextView freightTTv;
    @BindView(R.id.totalAmountTv)
    AppCompatTextView totalAmountTv;
    @BindView(R.id.freightTv)
    AppCompatTextView freightTv;
    @BindView(R.id.pieceTv)
    AppCompatTextView pieceTv;
    @BindView(R.id.actuallyTv)
    AppCompatTextView actuallyTv;
    @BindView(R.id.addressPhoneTv)
    AppCompatTextView addressPhoneTv;
    @BindView(R.id.actuallyAmountTv)
    AppCompatTextView actuallyAmountTv;
    @BindView(R.id.submitOrderBtn)
    AppCompatButton submitOrderBtn;
    @BindView(R.id.remarkEt)
    AppCompatEditText remarkEt;
    @BindView(R.id.serviceChargeValTv)
    AppCompatTextView serviceChargeValTv;

    private Unbinder unbinder;
    private int maxNumber = 999;
    private BigDecimal bdProPrice = BigDecimal.ZERO;
    private ProBean proBean;
    private MangoWallet mangoWallet;
    private int number = 1;
    private String walletAddress;
    private Constants.WalletType walletType;
    private BigDecimal bdMGPprice = BigDecimal.ZERO;
    private BigDecimal bdPostage = BigDecimal.ZERO;
    private String AHSR = "0.00";
    private EMWalletRepository emWalletRepository;
    private ShippingAddressBean shippingAddressBean;
    private ShippingAddressBean.DataBean addressDataBean;
    private List<ShippingAddressBean.DataBean> dataBeanList;
    private QMUIDialog qmuiDialog;
    private GetOrderIDBean orderIDData;
    private boolean ISADD = false;
    private List<ProBean.PayConfigsBean> payConfigList;
    private String SYMBOL = Constants.MGP_SYMBOL;
    private ProBean.PayConfigsBean payConfigsBean;
    private BigDecimal bdServiceCharge = BigDecimal.ZERO;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_confirm, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        proBean = bundle.getParcelable(EXTRA_GOODS_INFO);
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        findAddr();
        getDIGICCYPrice();
        emWalletRepository = new EMWalletRepository();
        payConfigList = proBean.getPayConfigs();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_confirm_order);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        maxNumber = proBean.getStock();
        goodsAddView.setMaxValue(ObjectUtils.isEmpty(maxNumber) ? 0 : maxNumber);
        goodsAddView.setMinValue(0);
        goodsNameTv.setText(ObjectUtils.isEmpty(proBean.getStoreName()) ? "" : proBean.getStoreName());
        specificationTv.setText(getString(R.string.str_specification) + ":" + "");
        bdProPrice = new BigDecimal(ObjectUtils.isEmpty(proBean.getPrice()) ? "0" : proBean.getPrice() + "");

        priceTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(bdProPrice.toPlainString()) ? "0" : bdProPrice.toPlainString(),
                2, RoundingMode.UP));

        if (proBean.getImage_url() != null) {
            Glide.with(getBaseFragmentActivity())
                    .load(proBean.getImage_url().get(0))
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                    .into(goodsPicIv);
        }
        freightTTv.setText(getString(R.string.str_freight) + ":");
        String postage = "0";
        if (!ObjectUtils.isEmpty(proBean.getPostage())) {
            postage = String.valueOf(proBean.getPostage());
        }
        bdPostage = new BigDecimal(postage);
        freightTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(postage) ? "0" : postage,
                2, RoundingMode.UP));

//        updataView();
    }

    @Override
    protected void initAction() {
        goodsAddView.setOnValueChangeListene(new CustomizeGoodsAddView.OnValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                if (value > maxNumber) {
                    number = maxNumber;
                    goodsAddView.setValue(maxNumber);
                } else {
                    number = value;
                }
                updataView();
            }
        });

        registerEffect(this, new QMUIFragmentEffectHandler<ShippingAddressEffect>() {
            @Override
            public boolean shouldHandleEffect(@NonNull ShippingAddressEffect effect) {
                LogUtils.dTag(LOG_TAG, "shouldHandleEffect==");
                addressDataBean = effect.addressDataBean;
                updataViewAddr();
                return true;
            }

            @Override
            public void handleEffect(@NonNull ShippingAddressEffect effect) {
                LogUtils.dTag(LOG_TAG, "handleEffect==");
                addressDataBean = effect.addressDataBean;
                updataViewAddr();
            }
        });
    }

    @OnClick({R.id.addressLayout, R.id.submitOrderBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addressLayout:
                if (addressDataBean != null) {
                    ISADD = false;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("ReceivingAddressFragment", bundle);
                } else {
                    toAddAddress();
                }
                break;
            case R.id.submitOrderBtn:
                if (addressDataBean != null) {
                    if (addressDataBean.getCountry() != proBean.getCountryNum()) {
                        ToastUtils.showLong(R.string.str_purchase_area_restriction);
                    } else {
                        showSimpleBottomSheetList();
                    }
                } else {
                    ToastUtils.showLong(R.string.str_please_add_address);
                }
                break;
        }
    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                if (bdMGPprice != null) {
                    if (bdMGPprice.compareTo(BigDecimal.ZERO) > 0) {//-1表示小于，0是等于，1是大于。
                        transferTransaction(orderIDData.getData());
                    } else {
                        ToastUtils.showShort(StringUtils.getString(R.string.str_getmgp_price_fail));
                    }
                } else {
                    ToastUtils.showShort(StringUtils.getString(R.string.str_getmgp_price_fail));
                }
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.str_wrong_password));
            }
            editText.setText("");
            dialog.dismiss();
        }
    };

    private void updataView() {
        BigDecimal bdProTotal = bdProPrice.multiply(BigDecimal.valueOf(number));
        String amount = bdProTotal.stripTrailingZeros().toPlainString();
        pieceTv.setText(String.format(getString(R.string.str_piece), number + ""));
        BigDecimal bdPaymentMgp = BigDecimal.ZERO;
        bdServiceCharge = ObjectUtils.isEmpty(proBean.getServiceCharge()) ? BigDecimal.ZERO : new BigDecimal(proBean.getServiceCharge());
        if (bdMGPprice != BigDecimal.ZERO) {
            bdPaymentMgp = ((bdProTotal.multiply(bdServiceCharge.add(BigDecimal.ONE))).add(bdPostage)).divide(bdMGPprice, 6, HALF_UP);
        }
        //支付数量= （商品单价*件数*（1+手续费比率）+邮费）/ 币价
        serviceChargeValTv.setText(bdProTotal.multiply(bdServiceCharge).divide(bdMGPprice, 6, HALF_UP).setScale(4, RoundingMode.HALF_UP).toPlainString() + SYMBOL);
        AHSR = bdPaymentMgp.setScale(4, RoundingMode.HALF_UP).toPlainString();
        actuallyAmountTv.setText(AHSR + " " + SYMBOL);
        totalAmountTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(amount) ? "0" : amount,
                2, RoundingMode.UP));
    }

    private void updataViewAddr() {
        if (addressDataBean != null) {
            addressTv.setVisibility(View.VISIBLE);
            addressPhoneTv.setVisibility(View.VISIBLE);
            addressNameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_black));
            addressNameTv.setText(addressDataBean.getUserName());
            addressTv.setText((ObjectUtils.isEmpty(addressDataBean.getCountryName()) ? "" : addressDataBean.getCountryName()) + " " +
                    (ObjectUtils.isEmpty(addressDataBean.getCity()) ? "" : addressDataBean.getCity().replace("null", "")) + " " +
                    (ObjectUtils.isEmpty(addressDataBean.getDetailedAddress()) ? "" : addressDataBean.getDetailedAddress()));
            addressPhoneTv.setText(addressDataBean.getPhone());
        } else {
            addressPhoneTv.setVisibility(View.GONE);
            addressNameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_red));
            addressNameTv.setText(getString(R.string.address_action_create));
            addressTv.setVisibility(View.GONE);
        }
    }

    /**
     * 查询所有收货地址
     */
    private void findAddr() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().findAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::allAddressSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取MGP价格
     */
    private void getDIGICCYPrice() {
        showTipDialog(getString(R.string.str_loading));
        try {
            Map map = MapUtils.newHashMap();
            map.put("pair", SYMBOL + "_USDT");
            String json = GsonUtils.toJson(map);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCoinPrice(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::priceSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订单
     */
    private void getOrderSn() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);//用户地址或账户名
        params.put("productId", String.valueOf(proBean.getProID()));//商品ID
        params.put("detailedAddress", addressDataBean.getDetailedAddress());//收货详细地址
        params.put("userName", addressDataBean.getUserName());//用户名称
        params.put("productNum", String.valueOf(number));//购买商品数量
        params.put("city", addressDataBean.getCity());//收货城市
        params.put("productPrice", String.valueOf(proBean.getPrice()));//商品价格
        params.put("totalPostage", String.valueOf(proBean.getPostage()));//邮费
        params.put("mark", ObjectUtils.isEmpty(remarkEt.getText()) ? "" : remarkEt.getText());//用户备注
        params.put("phone", addressDataBean.getPhone());//电话
        params.put("payMgp", AHSR);//需要支付的MGP数量
        params.put("mgpPrice", bdMGPprice.toPlainString());
        params.put("country", String.valueOf(proBean.getCountryNum()));
        params.put("pay", String.valueOf(payConfigsBean.getPayId()));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().addOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付订单转账
     * {"orderSn":"MGP1597654887759958804071291","currencyPrice":"0.51","dollar":"160"}
     */
    private void transferTransaction(String orderID) {
        BigDecimal bdDollar = (bdProPrice.multiply(new BigDecimal(number))).add(bdPostage);
        Map memoParams = MapUtils.newHashMap();
        memoParams.put("orderSn", orderID);
        memoParams.put("currencyPrice", bdMGPprice.toPlainString());
        memoParams.put("dollar", bdDollar.toPlainString());
        String memo = GsonUtils.toJson(memoParams);
        AHSR = bdDollar.divide(bdMGPprice, 4, RoundingMode.UP).toPlainString();
        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", SHOP_ACCOUNT);
        params.put("quantity", AHSR + " " + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(LOG_TAG, " privatekey = " + privatekey
                + "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    /**
     * 添加订单
     */
    private void buyOrder(String transactionId) {
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);//用户地址或账户名
        params.put("productId", String.valueOf(proBean.getProID()));//商品ID
        params.put("detailedAddress", addressDataBean.getDetailedAddress());//收货详细地址
        params.put("userName", addressDataBean.getUserName());//用户名称
        params.put("productNum", String.valueOf(number));//购买商品数量
        params.put("city", addressDataBean.getCity());//收货城市
        params.put("productPrice", String.valueOf(proBean.getPrice()));//商品价格
        params.put("totalPostage", String.valueOf(proBean.getPostage()));//邮费
        params.put("mark", ObjectUtils.isEmpty(remarkEt.getText()) ? "" : remarkEt.getText());//用户备注
        params.put("phone", addressDataBean.getPhone());//电话
        params.put("payMgp", AHSR);//需要支付的MGP数量
        params.put("mgpPrice", bdMGPprice.toPlainString());
        params.put("hash", transactionId);
        params.put("orderId", orderIDData.getData());
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().buyOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allAddressSuccess(JsonObject jsonData) {
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, GsonUtils.toJson(jsonData));
        shippingAddressBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), ShippingAddressBean.class);
        dataBeanList = shippingAddressBean.getData();
        if (ObjectUtils.isNotEmpty(dataBeanList)) {
            for (ShippingAddressBean.DataBean addressBean : dataBeanList) {
                if (addressBean.isIsDefault()) {
                    addressDataBean = addressBean;
                }
            }
            if (addressDataBean == null) {
                addressDataBean = dataBeanList.get(0);
            }
        }
        updataViewAddr();
    }

    private void priceSuccess(CurrencyPrice data) {
        dismissTipDialog();
        LogUtils.dTag("priceSuccess==", "data = " + GsonUtils.toJson(data));
        if (data.getCode() == 0) {
            bdMGPprice = data.getData().getPrice();
        } else {
            ToastUtils.showLong(data.getMsg());
        }
        updataView();
    }

    private void getOrderSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            orderIDData = GsonUtils.fromJson(GsonUtils.toJson(jsonData), GetOrderIDBean.class);
            if (orderIDData.getCode() == 0) {
                if (ObjectUtils.equals(MGP_SYMBOL, SYMBOL)) {
                    if (qmuiDialog == null) {
                        qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                    }
                    qmuiDialog.show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    bundle.putInt("type", GOODS_PAYMENT_TYPE);
                    bundle.putString("num", AHSR);
                    bundle.putString("CollectionAddress", proBean.getUsdtAddr());
                    bundle.putString("SYMBOL", SYMBOL);
                    bundle.putString("OrderID", orderIDData.getData());
                    startFragment("OperatingStepsFragment", bundle);
                }
            } else {
                dismissTipDialog();
                ToastUtils.showLong(orderIDData.getMsg());
            }
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                buyOrder(transactionBean.msg);
            } else {
                dismissTipDialog();
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            dismissTipDialog();
            ToastUtils.showLong(R.string.str_payment_failure);
        }
    }

    private void addOrderSuccess(JsonObject jsonData) {
        dismissTipDialog();
        OrderIDBean orderIDBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), OrderIDBean.class);
        if (orderIDBean.getCode() == 0) {
            ToastUtils.showLong(R.string.str_transaction_success);
            popBackStack();
//            String payMoney = String.valueOf(orderIDBean.getData().getPayMoney());
//            String orderID = orderIDBean.getData().getOrderID();
//            if (ObjectUtils.isEmpty(payMoney)) {
//                payMoney = "0";
//            }
//            String payMgp = new BigDecimal(payMoney).divide(bdMGPprice, BigDecimal.ROUND_HALF_UP).toPlainString();
        } else {
            ToastUtils.showLong(orderIDBean.getMsg());
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }

    private void toAddAddress() {
        ISADD = true;
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_ISADD_ADDRESS, ISADD);
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        startFragment("EditAddressFragment", bundle);
    }

    // ================================ 生成不同类型的BottomSheet
    private void showSimpleBottomSheetList() {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
        builder.setGravityCenter(true)
                .setAddCancelBtn(true)
                .setAllowDrag(false)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        payConfigsBean = payConfigList.get(position);
                        SYMBOL = payConfigsBean.getName();
                        getDIGICCYPrice();
                        getOrderSn();
                    }
                });
        if (ObjectUtils.isNotEmpty(payConfigList)) {
            for (int i = 0; i < payConfigList.size(); i++) {
                ProBean.PayConfigsBean dataBean = payConfigList.get(i);
                if (dataBean != null) {
                    builder.addItem(dataBean.getName());
//                    Glide.with(getActivity()).load(dataBean.getPic()).into(new CustomTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            builder.addItem(resource, dataBean.getName());
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//                            builder.addItem(placeholder, dataBean.getName());
//                        }
//                    });
                }
            }
        }
        builder.build().show();
    }

//    /**
//     * 这是一个耗时的操作需要异步处理
//     *
//     * @param url 通过URL得到 Drawable
//     * @return
//     */
//    public Drawable getDrawableGlide(String url) {
//        try {
//            return Glide.with(getActivity())
//                    .load(url)
//                    .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
//                    .submit()
//                    .get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public void onResume() {
        super.onResume();
        getDIGICCYPrice();
        if (ISADD) {
            findAddr();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();
    }

}
