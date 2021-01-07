package com.token.mangowallet.ui.fragment.mgp_deal;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.IsBindBean;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.repository.EMWalletRepository;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class OTCSellFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.errorTv)
    AppCompatTextView errorTv;
    @BindView(R.id.toSetupTv)
    AppCompatTextView toSetupTv;
    @BindView(R.id.errorLayout)
    FrameLayout errorLayout;
    @BindView(R.id.mgpPriceTv)
    AppCompatTextView mgpPriceTv;
    @BindView(R.id.unitPriceTv)
    AppCompatTextView unitPriceTv;
    @BindView(R.id.subtractBtn)
    QMUIRoundButton subtractBtn;
    @BindView(R.id.numTv)
    AppCompatTextView numTv;
    @BindView(R.id.additionBtn)
    QMUIRoundButton additionBtn;
    @BindView(R.id.quantityTv)
    AppCompatTextView quantityTv;
    @BindView(R.id.quantityEt)
    AppCompatEditText quantityEt;
    @BindView(R.id.totalTv)
    AppCompatTextView totalTv;
    @BindView(R.id.balanceTv)
    AppCompatTextView balanceTv;
    @BindView(R.id.minSaleAmountTv)
    AppCompatTextView minSaleAmountTv;
    @BindView(R.id.minSaleAmountEt)
    AppCompatEditText minSaleAmountEt;
    @BindView(R.id.fiatMoneyUnitTv)
    AppCompatTextView fiatMoneyUnitTv;
    @BindView(R.id.totalMoneyTv)
    AppCompatTextView totalMoneyTv;
    @BindView(R.id.totalMoneyValueTv)
    AppCompatTextView totalMoneyValueTv;
    @BindView(R.id.fiatMoneyUnitTv2)
    AppCompatTextView fiatMoneyUnitTv2;
    @BindView(R.id.sellBtn)
    QMUIRoundButton sellBtn;

    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    public Constants.WalletType walletType;
    public EMWalletRepository emWalletRepository;
    private boolean isBind = false;
    private boolean isPledge = false;
    private String remaining;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mgp_sell, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        emWalletRepository = new EMWalletRepository();
        isBind();
        isPosPledge();
    }

    @Override
    protected void initView() {
        toSetupTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        toSetupTv.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void initAction() {

    }


    @OnClick({R.id.toSetupTv, R.id.subtractBtn, R.id.additionBtn, R.id.sellBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toSetupTv:
                Bundle bundle = new Bundle();
                if (ObjectUtils.equals(getString(R.string.str_to_add), toSetupTv.getText().toString())) {

                } else if (ObjectUtils.equals(getString(R.string.str_to_pledge), toSetupTv.getText().toString())) {
                    bundle.putString("mgpNum", "0");
                    bundle.putString("type", getString(R.string.str_mortgage_initiated));
                    bundle.putBoolean("isMortgage", true);
                    bundle.putParcelable(EXTRA_WALLET, mangoWallet);
                    startFragment("MiningMortgageFragment", bundle);
                }
                break;
            case R.id.subtractBtn:
                break;
            case R.id.additionBtn:
                break;
            case R.id.sellBtn:
                break;
        }
    }


    private void updataErrerView() {
        if (!isBind || !isPledge) {
            errorLayout.setVisibility(View.VISIBLE);
            if (!isBind) {
                errorTv.setText(R.string.str_no_contact_msg);
                toSetupTv.setText(R.string.str_to_add);
            }
            if (!isPledge) {
                errorTv.setText(R.string.str_no_pospledge_msg);
                toSetupTv.setText(R.string.str_to_pledge);
            }
            sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
        } else {
            errorLayout.setVisibility(View.GONE);
            sellBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue2));
        }
    }

    /**
     * 账号自动激活
     */
    private void isBind() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map params = MapUtils.newHashMap();
            params.put("mgpName", mangoWallet.getWalletAddress());
            params.put("type", "0");
            String json = GsonUtils.toJson(params);
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().isBind(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onIsBindSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isPosPledge() {
        try {
            showTipDialog(getString(R.string.str_loading));
            Map mapTableRows = MapUtils.newHashMap();
            mapTableRows.put("scope", Constants.contractAddress);
            mapTableRows.put("code", Constants.contractAddress);
            mapTableRows.put("table", "balances");
            mapTableRows.put("json", true);
            mapTableRows.put("table_key", "");
            mapTableRows.put("lower_bound", mangoWallet.getWalletAddress());
            mapTableRows.put("upper_bound", mangoWallet.getWalletAddress());
            emWalletRepository.fetchTableRows(mapTableRows, walletType)
                    .subscribe(this::onTableRows, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onIsBindSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            IsBindBean isBindBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), IsBindBean.class);
            if (isBindBean != null)
                isBind = isBindBean.getData() == 0 ? true : false;
        }
        updataErrerView();
    }

    private void onTableRows(Object o) {
        dismissTipDialog();
        if (!ObjectUtils.isEmpty(o)) {
            TableRowsBean tableRowsBean = GsonUtils.fromJson(GsonUtils.toJson(o), TableRowsBean.class);
            List<TableRowsBean.RowsBean> rowsBeanList = tableRowsBean.getRows();
            if (ObjectUtils.isEmpty(rowsBeanList)) {
                remaining = "0";
            } else {
                TableRowsBean.RowsBean rowsBean = rowsBeanList.get(0);
                remaining = rowsBean.getRemaining().split(" ")[0];
            }
            BigDecimal bdRemaining = new BigDecimal(remaining);
            if (bdRemaining.compareTo(BigDecimal.ZERO) > 0) {
                isPledge = true;
            } else {
                isPledge = false;
            }
        }
        updataErrerView();
    }

    private void onError(Object e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString());
    }

}
