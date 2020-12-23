package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.TransactionDetailsBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.ui.activity.WebActivity;
import com.token.mangowallet.ui.viewmodel.TransactionModelFactory;
import com.token.mangowallet.ui.viewmodel.TransactionViewModel;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.token.mangowallet.utils.Constants.EXTRA_TO_URL;
import static com.token.mangowallet.utils.Constants.EXTRA_TRANSACTION_ID;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.MGP_TRANSACTION_DETAILS_URL;


public class TransactionDetailsFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.transactionStateTv)
    AppCompatTextView transactionStateTv;
    @BindView(R.id.amountTv)
    AppCompatTextView amountTv;
    @BindView(R.id.fromTv)
    AppCompatTextView fromTv;
    @BindView(R.id.toTv)
    AppCompatTextView toTv;
    @BindView(R.id.memoTv)
    AppCompatTextView memoTv;
    @BindView(R.id.transactionIdTv)
    AppCompatTextView transactionIdTv;
    @BindView(R.id.blocknumTv)
    AppCompatTextView blocknumTv;
    @BindView(R.id.timeTv)
    AppCompatTextView timeTv;
    @BindView(R.id.QRcodeIv)
    AppCompatImageView QRcodeIv;
    @BindView(R.id.layout)
    QMUIRelativeLayout layout;
    @BindView(R.id.transactionStateIv)
    AppCompatImageView transactionStateIv;

    private Unbinder unbinder;
    private TransactionModelFactory transactionModelFactory;
    private TransactionViewModel transactionViewModel;

    private String transaction_id;
    private String transaction_url;
    private String walletAddress;
    private Bitmap qrCodeBitmap;
    private TransactionDetailsBean detailsBean;
    private TimeUtils timeUtils;
    private String quantity;
    private int radius = 0;
    private MangoWallet mangoWallet;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_transaction_details, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        transaction_id = bundle.getString(EXTRA_TRANSACTION_ID, "");
        quantity = bundle.getString("quantity", "0");
        walletAddress = mangoWallet.getWalletAddress();
        transactionModelFactory = new TransactionModelFactory();
        transactionViewModel = ViewModelProviders.of(this.getActivity(), transactionModelFactory)
                .get(TransactionViewModel.class);

        transactionViewModel.prepare(mangoWallet);
        transactionViewModel.fetchTransactionDetail(transaction_id);
        transactionViewModel.transactionDetail().observe(this, this::onTransactionDetails);

        transaction_url = MGP_TRANSACTION_DETAILS_URL + transaction_id;
        timeUtils = new TimeUtils();
        radius = QMUIDisplayHelper.dp2px(getContext(), 10);

    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_trading_particulars);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        layout.setRadius(radius);
        qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(transaction_url, SizeUtils.getMeasuredHeight(QRcodeIv));
        QRcodeIv.setImageBitmap(qrCodeBitmap);

    }

    @Override
    protected void initAction() {

    }

    private void onTransactionDetails(TransactionDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
        updataView();
    }

    private void updataView() {
        if (!ObjectUtils.isEmpty(detailsBean)) {
            String block_num = detailsBean.getBlock_num();
            String block_time = detailsBean.getBlock_time();

            List<TransactionDetailsBean.TracesBean> tracesBeans = detailsBean.getTraces();
            TransactionDetailsBean.TrxBeanX trx = detailsBean.getTrx();
            TransactionDetailsBean.TrxBeanX.ReceiptBean receiptBean = trx.getReceipt();
            String status = receiptBean.getStatus();
            String transactionStatus;
            int res;
            if (ObjectUtils.equals("executed", status)) {
                transactionStatus = getString(R.string.str_transaction_success);
                res = R.mipmap.transaction_success_icon;
            } else if (ObjectUtils.equals("soft_fail", status) || ObjectUtils.equals("hard_fail", status)) {
                transactionStatus = getString(R.string.str_transaction_fail);
                res = R.mipmap.transaction_fail_icon;
            } else if (ObjectUtils.equals("delayed", status)) {
                transactionStatus = getString(R.string.str_transaction_delay);
                res = R.mipmap.transaction_delayed_icon;
            } else if (ObjectUtils.equals("expired", status)) {
                transactionStatus = getString(R.string.str_transaction_overdue);
                res = R.mipmap.transaction_overdue_icon;
            } else {
                transactionStatus = getString(R.string.str_transaction_fail);
                res = R.mipmap.transaction_fail_icon;
            }
            transactionStateIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), res));
            transactionStateTv.setText(transactionStatus);
            if (tracesBeans != null) {
                if (tracesBeans.size() > 0) {
                    for (int i = 0; i < tracesBeans.size(); i++) {
                        TransactionDetailsBean.TracesBean tracesBean = tracesBeans.get(i);
                        TransactionDetailsBean.TracesBean.ActBean actBean = tracesBean.getAct();
                        TransactionDetailsBean.TracesBean.ActBean.DataBeanX dataBean = actBean.getData();
                        String quantity2 = dataBean.getQuantity();
                        if (ObjectUtils.equals(quantity, quantity2)) {
                            String trx_id = tracesBean.getTrx_id();
                            String to = dataBean.getTo();
                            String from = dataBean.getFrom();
                            String memo = dataBean.getMemo();
                            boolean isOut = false;
                            if (ObjectUtils.equals(walletAddress, to)) {
                                isOut = false;
                            } else {
                                isOut = true;
                            }
                            amountTv.setText((isOut ? "-" : "+") + quantity);
                            fromTv.setText(from);
                            toTv.setText(to);
                            memoTv.setText(memo);
                            transactionIdTv.setText(trx_id);
                            break;
                        }
                    }
                }
            }
            blocknumTv.setText(block_num);
            timeTv.setText(timeUtils.getStringTime(block_time));
        }
    }

    @OnClick({R.id.fromTv, R.id.toTv, R.id.transactionIdTv, R.id.webTransactionDetailsLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fromTv:
                ClipboardUtils.copyText(fromTv.getText().toString().trim());
                ToastUtils.showShort(R.string.str_copy_success);
                break;
            case R.id.toTv:
                ClipboardUtils.copyText(toTv.getText().toString().trim());
                ToastUtils.showShort(R.string.str_copy_success);
                break;
            case R.id.transactionIdTv:
                ClipboardUtils.copyText(transactionIdTv.getText().toString().trim());
                ToastUtils.showShort(R.string.str_copy_success);
                break;
            case R.id.webTransactionDetailsLayout:
                Intent browserIntent = new Intent(getActivity(), WebActivity.class);
                browserIntent.putExtra(EXTRA_TO_URL, transaction_url);
                startActivity(browserIntent);
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(transaction_url));
//                startActivity(browserIntent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
