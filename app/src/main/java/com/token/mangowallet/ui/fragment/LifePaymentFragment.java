package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.AddOrderBean;
import com.token.mangowallet.bean.AppStoreLifeBean;
import com.token.mangowallet.bean.CurrencyData;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.MarketBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.PayInitBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.ServerInfo;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.Md5Utils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.DialogHelper;
import com.token.mangowallet.view.ViewUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_LIFE_DATA;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.SHOP_ACCOUNT;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;
import static com.token.mangowallet.utils.Constants.percent;
import static java.math.BigDecimal.ROUND_DOWN;
import static java.math.RoundingMode.HALF_UP;

public class LifePaymentFragment extends BaseFragment {


    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.storeBanner)
    Banner storeBanner;
    @BindView(R.id.currencyTv)
    AppCompatTextView currencyTv;
    @BindView(R.id.fundsReceivedValueTv)
    AppCompatEditText fundsReceivedValueTv;
    @BindView(R.id.toCurrencyValueTv)
    AppCompatTextView toCurrencyValueTv;
    @BindView(R.id.onDiscountValueTv)
    AppCompatTextView onDiscountValueTv;
    @BindView(R.id.presentValueTv)
    AppCompatTextView presentValueTv;
    @BindView(R.id.receivingAccountTv)
    AppCompatTextView receivingAccountTv;
    @BindView(R.id.receivingAccountValueTv)
    AppCompatEditText receivingAccountValueTv;
    @BindView(R.id.thisPaymentTv)
    AppCompatTextView thisPaymentTv;
    @BindView(R.id.paymentValueTv)
    AppCompatTextView paymentValueTv;
    @BindView(R.id.arrowsIv)
    AppCompatImageView arrowsIv;
    @BindView(R.id.msgValueTv)
    AppCompatEditText msgValueTv;
    @BindView(R.id.serviceChargeTv)
    AppCompatTextView serviceChargeTv;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private Constants.WalletType walletType;
    private AppStoreLifeBean.DataBean dataBean;
    private BannerImageAdapter bannerImageAdapter;
    private EMWalletRepository emWalletRepository;
    private List<String> bannerList = new ArrayList<>();
    private BigDecimal bdMGPprice = BigDecimal.ZERO;
    private CurrencyPrice mCurrencyPrice;
    private BigDecimal mBuyerProDecimal = BigDecimal.ZERO;
    private BigDecimal mThreeDecimal = new BigDecimal(3);
    private BigDecimal mgpDecimal = BigDecimal.ZERO;
    private BigDecimal usDecimal = BigDecimal.ZERO;
    private AddOrderBean.DataBean addOrderData;
    private QMUIDialog qmuiDialog;
    private QMUIDialog mMsgQMUIDialog;
    private boolean IsMer;
    private List<String> buyerGainProsList;
    private QMUIBottomSheet bottomSheet;
    private CurrencyData currencyData;
    private BigDecimal mServiceChargePro;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_life_pay_bill, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        dataBean = bundle.getParcelable(EXTRA_LIFE_DATA);
        getPayInit();
        getDIGICCYPrice();
        defaultModel();
        if (dataBean != null) {
            if (ObjectUtils.isNotEmpty(dataBean.getDetailImg())) {
                bannerList.addAll(dataBean.getDetailImg());
            }
        }
        emWalletRepository = new EMWalletRepository();
        currencyData = BalanceUtils.getCurrencyData();
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.str_pay_bill);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightTextButton(R.string.str_record, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                startFragment("LifePaymentRecordFragment", bundle);
            }
        });
        bannerImageAdapter = new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.imageView)
                        .load(data)
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                        .into(holder.imageView);
            }
        };
        storeBanner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(bannerImageAdapter)
                .setIndicator(new CircleIndicator(getActivity()));

        if (currencyData != null) {
            currencyTv.setText(currencyData.getSymbol());
        }
    }

    @Override
    protected void initAction() {
        fundsReceivedValueTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mCurMoney = ObjectUtils.isEmpty(s) ? "0" : s.toString();
                usDecimal = new BigDecimal(mCurMoney).divide(new BigDecimal(currencyData.getPrice()), 2, RoundingMode.CEILING);
//                String mCurrency = BalanceUtils.currencyToBase(mCurMoney, 2, RoundingMode.FLOOR);
                toCurrencyValueTv.setText("≈$" + usDecimal.toPlainString());
                if (mCurrencyPrice == null) {
                    getDIGICCYPrice();
                }
                setPresentValue(mCurMoney);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.paymentBtn, R.id.arrowsIv, R.id.onDiscountValueTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.paymentBtn:
                if (dataBean == null) {
                    ToastUtils.showLong(R.string.str_goods_not_available_msg);
                    return;
                }
                if (ObjectUtils.isEmpty(fundsReceivedValueTv.getText()) || ObjectUtils.equals("0", fundsReceivedValueTv.getText())) {
                    ToastUtils.showLong(R.string.str_please_enter_amount);
                    return;
                }
                if (ObjectUtils.isEmpty(receivingAccountValueTv.getText())) {
                    ToastUtils.showLong(R.string.str_enter_receiving_account);
                    return;
                }

                if (mMsgQMUIDialog == null) {
                    mMsgQMUIDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_warm_prompt),
                            getString(R.string.str_confirm_payment),
                            getString(R.string.str_cancel),
                            getString(R.string.str_ok), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            }, new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    if (qmuiDialog == null) {
                                        qmuiDialog = DialogHelper.showEditTextDialog(getActivity(), getString(R.string.str_password_authentification),
                                                getString(R.string.str_enter_password), getString(android.R.string.ok), getString(android.R.string.cancel), listener, true);
                                    }
                                    qmuiDialog.show();
                                }
                            });
                }
                mMsgQMUIDialog.show();
                break;
            case R.id.onDiscountValueTv:
            case R.id.arrowsIv:
                showSimpleBottomSheetList();
                break;
        }

    }

    DialogConfirmListener listener = new DialogConfirmListener() {
        @Override
        public void onClick(QMUIDialog dialog, View view, int index) {
            EditText editText = (EditText) view;
            String contrastPassword = editText.getText().toString().trim();
            if (ObjectUtils.equals(Md5Utils.md5(contrastPassword), mangoWallet.getWalletPassword())) {
                if (mCurrencyPrice != null) {
                    if (bdMGPprice.compareTo(BigDecimal.ZERO) > 0) {//-1表示小于，0是等于，1是大于。
                        lifeAddOrder();
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

    private void setPresentValue(String money) {
        BigDecimal mCurrencyDecimal = new BigDecimal(money);
        if (mCurrencyDecimal.compareTo(BigDecimal.ZERO) > 0) {
            //-1表示小于，0是等于，1是大于。
            //赠送价值 = 金额*比例*3
            //MGP = 金额/美元价格/mgp价格  买家
            //MGP = 金额/美元价格/mgp价格*比例 商家
            //本次支付 = MGP+(MGP*手续费) 或者 MGP*（1+手续费比）
            if (IsMer) {
                mgpDecimal = mCurrencyDecimal.divide(new BigDecimal(currencyData.getPrice()), 6, HALF_UP).multiply(mBuyerProDecimal).divide(bdMGPprice, 6, RoundingMode.HALF_UP);
            } else {
                mgpDecimal = mCurrencyDecimal.divide(new BigDecimal(currencyData.getPrice()), 6, HALF_UP).divide(bdMGPprice, 6, RoundingMode.HALF_UP);
            }
            mgpDecimal = mgpDecimal.add(mgpDecimal.multiply(mServiceChargePro)).setScale(4, RoundingMode.HALF_UP);
            String rewardPro = mCurrencyDecimal.multiply(mBuyerProDecimal).multiply(mThreeDecimal).setScale(2, ROUND_DOWN).toPlainString();
            presentValueTv.setText(currencyData.getSymbol() + rewardPro);
            paymentValueTv.setText(mgpDecimal.toPlainString() + " " + walletType);
        }
    }

    private void updateView(PayInitBean payInitBean) {//IsMer: 1为商家，0为买家；买家不可编辑
        if (payInitBean != null) {
            PayInitBean.DataBean dataBean = payInitBean.getData();
            String buyerPro = ObjectUtils.isEmpty(dataBean.getBuyerPro()) ? "0" : dataBean.getBuyerPro();
            mBuyerProDecimal = new BigDecimal(buyerPro);
            String mBuyerPro = percent.multiply(mBuyerProDecimal).toPlainString();
            onDiscountValueTv.setText(mBuyerPro + "%");
            IsMer = dataBean.getIsMer() == 1;
            LogUtils.dTag(LOG_TAG, "IsMer = " + IsMer);
            ViewUtils.setEditableEditText(receivingAccountValueTv, IsMer);
            receivingAccountValueTv.setText(ObjectUtils.isEmpty(dataBean.getGainAddress()) ? "" : dataBean.getGainAddress());
            arrowsIv.setVisibility(IsMer ? View.VISIBLE : View.GONE);
            onDiscountValueTv.setEnabled(IsMer);
            //赠送价值 = 金额/币价*比例*3
            mServiceChargePro = ObjectUtils.isEmpty(dataBean.getServiceChargePro()) ? BigDecimal.ZERO : dataBean.getServiceChargePro();
            serviceChargeTv.setText(mServiceChargePro.multiply(percent).toPlainString() + "%");
        }
    }

    private void defaultModel() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("id", "2");//1 商城 2 本地化店铺
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().defaultModel(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::defaultModel, this::onError);
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
            map.put("pair", walletType + "_USDT");
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
     * 商铺列表
     */
    private void getPayInit() {
        if (dataBean == null || ObjectUtils.isEmpty(walletAddress)) {
            return;
        }
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("storeId", String.valueOf(dataBean.getId()));
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().getPayInit(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::payInitSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 买单
     */
    private void lifeAddOrder() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        String money = fundsReceivedValueTv.getText().toString().trim();
        if (IsMer) {
            BigDecimal moneyDecimal = new BigDecimal(money)
                    .multiply(mBuyerProDecimal);
            money = moneyDecimal.toPlainString();
        }
        params.put("money", money);
        params.put("gainAddress", receivingAccountValueTv.getText().toString().trim());
        params.put("address", walletAddress);
        params.put("msg", ObjectUtils.isEmpty(msgValueTv.getText()) ? "" : msgValueTv.getText().toString());
        params.put("buyerPro", mBuyerProDecimal.stripTrailingZeros().toPlainString());
        params.put("storeId", String.valueOf(dataBean.getId()));
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().lifeAddOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付订单转账
     * {"orderSn":"MGP1597654887759958804071291","currencyPrice":"0.51","dollar":"160"}
     */
    private void transferTransaction(String orderID) {
        String dollar = fundsReceivedValueTv.getText().toString().trim();
        if (IsMer) {
            BigDecimal moneyDecimal = new BigDecimal(dollar)
                    .multiply(mBuyerProDecimal);
            dollar = moneyDecimal.toPlainString();
        }
        Map memoParams = MapUtils.newHashMap();
        memoParams.put("orderSn", orderID);
        memoParams.put("currencyPrice", bdMGPprice.toPlainString());
        memoParams.put("dollar", dollar);
        String memo = GsonUtils.toJson(memoParams);

        Map params = MapUtils.newHashMap();
        params.put("memo", memo);
        params.put("from", walletAddress);
        params.put("to", SHOP_ACCOUNT);
        params.put("quantity", mgpDecimal.toPlainString() + " " + walletType);
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        LogUtils.dTag(Constants.LOG_TAG, " privatekey = " + privatekey
                + "accountName = " + walletAddress
                + "params = " + jsonData);
        emWalletRepository.sendTransaction(TRANSFER_ACTION, privatekey, walletAddress, EOSIO_TOKEN_CONTRACT_CODE, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    /**
     * 支付信息上传
     */
    private void lifePayOrder(String hash) {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("orderSn", addOrderData.getOrderSn());
        params.put("payNum", mgpDecimal.toPlainString());
        params.put("payPrice", bdMGPprice.toPlainString());
        params.put("hash", hash);
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().lifePayOrder(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::payOrderSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void defaultModel(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MarketBean marketBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MarketBean.class);
            if (marketBean.getCode() == 0) {
                MarketBean.DataBean dataBean = marketBean.getData();
                buyerGainProsList = dataBean.getBuyerGainPros();
            } else {
                ToastUtils.showLong(marketBean.getMsg());
            }
        }
    }

    private void priceSuccess(CurrencyPrice data) {
        dismissTipDialog();
        mCurrencyPrice = data;
        LogUtils.dTag("priceSuccess==", "data = " + GsonUtils.toJson(data));
        if (data.getCode() == 0) {
            bdMGPprice = data.getData().getPrice();
        } else {
            ToastUtils.showLong(data.getMsg());
        }
        String mCurMoney = ObjectUtils.isEmpty(fundsReceivedValueTv.getText()) ? "0" : fundsReceivedValueTv.getText().toString().trim();
        setPresentValue(mCurMoney);
    }

    private void payInitSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            PayInitBean payInitBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), PayInitBean.class);
            if (payInitBean.getCode() == 0) {
                updateView(payInitBean);
            } else {
                ToastUtils.showLong(payInitBean.getMsg());
            }
        }
    }

    private void addOrderSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            AddOrderBean addOrderBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), AddOrderBean.class);
            if (addOrderBean.getCode() == 0) {
                addOrderData = addOrderBean.getData();
                transferTransaction(addOrderData.getOrderSn());
            } else {
                ToastUtils.showLong(addOrderBean.getMsg());
            }
        }
    }

    private void onTransaction(TransactionBean transactionBean) {
        if (transactionBean != null) {
            if (transactionBean.isSuccess) {
                lifePayOrder(transactionBean.msg);
            } else {
                dismissTipDialog();
                ToastUtils.showLong(transactionBean.msg);
            }
        } else {
            dismissTipDialog();
            ToastUtils.showLong(R.string.str_payment_failure);
        }
    }

    private void payOrderSuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                ToastUtils.showLong(R.string.str_transaction_success);
                popBackStack();
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.dTag("error==", "e = " + e.getMessage());
    }

    private void showSimpleBottomSheetList() {
        if (bottomSheet == null) {
            LinkedHashMap<String, ServerInfo> serverInfoMap = BaseUrlUtils.getInstance().serverInfoMap;
            QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
            builder.setGravityCenter(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                    .setTitle("")
                    .setAddCancelBtn(true)
                    .setAllowDrag(false)
                    .setNeedRightMark(false)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            String buyerGainPro = buyerGainProsList.get(position);
                            mBuyerProDecimal = new BigDecimal(buyerGainPro).divide(percent);
                            onDiscountValueTv.setText(buyerGainPro + "%");
                            setPresentValue(ObjectUtils.isEmpty(fundsReceivedValueTv.getText()) ? "0" : fundsReceivedValueTv.getText().toString().trim());
                        }
                    });

            for (String string : buyerGainProsList) {
                builder.addItem(string);
            }
            bottomSheet = builder.build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
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
