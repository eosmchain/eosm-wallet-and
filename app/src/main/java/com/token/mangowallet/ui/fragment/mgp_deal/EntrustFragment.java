package com.token.mangowallet.ui.fragment.mgp_deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.view.DialogHelper;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;
import static com.token.mangowallet.utils.Constants.OLOSE_ORDER;
import static com.token.mangowallet.utils.Constants.OPEN_ORDER;
import static com.token.mangowallet.utils.Constants.TRANSFER_ACTION;

public class EntrustFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.statusValTv)
    AppCompatTextView statusValTv;
    @BindView(R.id.entrustSellTv)
    AppCompatTextView entrustSellTv;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.transactionDetailTitleTv)
    AppCompatTextView transactionDetailTitleTv;
    @BindView(R.id.priceTv)
    AppCompatTextView priceTv;
    @BindView(R.id.priceValTv)
    AppCompatTextView priceValTv;
    @BindView(R.id.quantityTv)
    AppCompatTextView quantityTv;
    @BindView(R.id.quantityValTv)
    AppCompatTextView quantityValTv;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.haveSellTv)
    AppCompatTextView haveSellTv;
    @BindView(R.id.haveSellValTv)
    AppCompatTextView haveSellValTv;
    @BindView(R.id.haveFreezeTv)
    AppCompatTextView haveFreezeTv;
    @BindView(R.id.haveFreezeValTv)
    AppCompatTextView haveFreezeValTv;
    @BindView(R.id.repealEntrustBtn)
    QMUIRoundButton repealEntrustBtn;

    private Unbinder unbinder;
    private EMWalletRepository emWalletRepository;
    private MangoWallet mangoWallet;
    private Constants.WalletType walletType;
    private SelordersBean.RowsBean mSelordersBean;
    private int orderStatus;
    private QMUIDialog qmuiDialog;
    private BigDecimal quantity = BigDecimal.ZERO;
    private BigDecimal frozen_quantity = BigDecimal.ZERO;
    private BigDecimal fufilled_quantity = BigDecimal.ZERO;
    private BigDecimal remaining_quantity = BigDecimal.ZERO;
    private BigDecimal redeem_quantity = BigDecimal.ZERO;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_entrust, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        mSelordersBean = bundle.getParcelable("SelordersBean");

        emWalletRepository = new EMWalletRepository();
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_entrust_sell_title);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        if (ObjectUtils.isNotEmpty(mSelordersBean.getQuantity())) {// 总数
            String quantityStr = mSelordersBean.getQuantity();
            quantityStr = quantityStr.split(" ")[0];
            quantity = new BigDecimal(ObjectUtils.isEmpty(quantityStr) ? "0" : quantityStr);
        }
        if (ObjectUtils.isNotEmpty(mSelordersBean.getFrozen_quantity())) {// 冻结币
            String frozen_quantityStr = mSelordersBean.getFrozen_quantity();
            frozen_quantityStr = frozen_quantityStr.split(" ")[0];
            frozen_quantity = new BigDecimal(ObjectUtils.isEmpty(frozen_quantityStr) ? "0" : frozen_quantityStr);
        }
        if (ObjectUtils.isNotEmpty(mSelordersBean.getFufilled_quantity())) {//交易完成数量
            String fufilled_quantityStr = mSelordersBean.getFufilled_quantity();
            fufilled_quantityStr = fufilled_quantityStr.split(" ")[0];
            fufilled_quantity = new BigDecimal(ObjectUtils.isEmpty(fufilled_quantityStr) ? "0" : fufilled_quantityStr);
        }
        remaining_quantity = quantity.subtract(frozen_quantity).subtract(fufilled_quantity);
        redeem_quantity = quantity.subtract(fufilled_quantity);
        if (remaining_quantity.compareTo(BigDecimal.ZERO) <= 0) {//-1表示小于，0是等于，1是大于。
            orderStatus = 11;
        } else {
            orderStatus = mSelordersBean.getClosed() == 1 ? 12 : 10;
        }
        //订单状态：10:出售中；11:已完成；12：已撤销;
        String orderStatusStr = "";
        if (orderStatus == 10) {
            orderStatusStr = getString(R.string.str_entrust_selling);
            repealEntrustBtn.setVisibility(View.VISIBLE);
        } else if (orderStatus == 11) {
            orderStatusStr = getString(R.string.str_completed);
            repealEntrustBtn.setVisibility(View.GONE);
        } else if (orderStatus == 12) {
            orderStatusStr = getString(R.string.str_undone);
            repealEntrustBtn.setVisibility(View.GONE);
        }
        statusValTv.setText(orderStatusStr);
        entrustSellTv.setText(getString(R.string.str_entrust_sell) + (ObjectUtils.isEmpty(mSelordersBean.getQuantity()) ? "0.0000 MGP" : mSelordersBean.getQuantity()));
        priceValTv.setText(ObjectUtils.isEmpty(mSelordersBean.getPrice()) ? "￥0.00" : mSelordersBean.getPrice());
        quantityValTv.setText(ObjectUtils.isEmpty(mSelordersBean.getQuantity()) ? "0.0000 MGP" : mSelordersBean.getQuantity());
        haveSellValTv.setText(ObjectUtils.isEmpty(mSelordersBean.getFufilled_quantity()) ? "0.0000 MGP" : mSelordersBean.getFufilled_quantity());
        haveFreezeValTv.setText(ObjectUtils.isEmpty(mSelordersBean.getFrozen_quantity()) ? "0.0000 MGP" : mSelordersBean.getFrozen_quantity());
    }

    @Override
    protected void initAction() {

    }


    @OnClick(R.id.repealEntrustBtn)
    public void onViewClicked() {
        if (frozen_quantity.compareTo(BigDecimal.ZERO) <= 0) {
            qmuiDialog = DialogHelper.showMessageDialog(getActivity(), getString(R.string.str_repeal_entrust)
                    , getString(R.string.str_repeal_entrust_msg) + redeem_quantity.toPlainString() + " MGP"
                    , getString(R.string.str_cancel), getString(R.string.str_confirm), new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    }, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            transferTransaction();
                        }
                    });
            qmuiDialog.show();
        } else {
            ToastUtils.showLong(getString(R.string.str_freeze_tip));
        }
    }

    /**
     * 支付订单转账
     * {"orderSn":"MGP1597654887759958804071291","currencyPrice":"0.51","dollar":"160"}
     */
    private void transferTransaction() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("owner", mSelordersBean.getOwner());
        params.put("order_id", String.valueOf(mSelordersBean.getId()));
        String jsonData = GsonUtils.toJson(params);
        String privatekey = mangoWallet.getPrivateKey();
        //openorder
        emWalletRepository.sendTransaction(OLOSE_ORDER, privatekey, mangoWallet.getWalletAddress(), DEAL_CONTRACT, jsonData, walletType)
                .subscribe(this::onTransaction, this::onError);
    }

    private void onTransaction(TransactionBean transactionBean) {
        dismissTipDialog();
        if (transactionBean.isSuccess) {
            ToastUtils.showLong(R.string.str_order_succeed);
            popBackStack();
        } else {
            ToastUtils.showLong(transactionBean.msg);
        }
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

}
