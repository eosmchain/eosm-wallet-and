package com.token.mangowallet.ui.fragment.mgp_deal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.MapUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.TimeUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class BuyerTransactionInfoFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.buyerStatusTv)
    AppCompatTextView buyerStatusTv;
    @BindView(R.id.buyerStatusValTv)
    AppCompatTextView buyerStatusValTv;
    @BindView(R.id.paymentTimeRemainingTv)
    AppCompatTextView paymentTimeRemainingTv;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.serviceTipTv)
    AppCompatTextView serviceTipTv;
    @BindView(R.id.serviceWechatNumberTv)
    AppCompatTextView serviceWechatNumberTv;
    @BindView(R.id.serviceWechatNumberValTv)
    AppCompatTextView serviceWechatNumberValTv;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.cancelOrderBtn)
    QMUIRoundButton cancelOrderBtn;
    @BindView(R.id.paidPutCoinBtn)
    QMUIRoundButton paidPutCoinBtn;
    @BindView(R.id.btnLayout)
    ConstraintLayout btnLayout;
    @BindView(R.id.paymentTimeRemainingValTv)
    AppCompatTextView paymentTimeRemainingValTv;

    private Unbinder unbinder;
    private int mCurIndex = 0;
    private AlertDialog dialog;
    private Bundle bundle;
    private MangoWallet mangoWallet;
    private int index = 0;
    private String num = "0";
    private String amountPaid = "0";
    private PaymentInfoFragment paymentInfoFragment;
    private ContactMethodFragment contactMethodFragment;
    private TransactionDetailFragment transactionDetailFragment;
    private CountDownTimer timer;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_buyer_transaction_info, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        index = bundle.getInt("index", index);
        num = bundle.getString("num", num);
        amountPaid = bundle.getString("amountPaid", amountPaid);
    }

    @Override
    protected void initView() {
        paymentInfoFragment = (PaymentInfoFragment) getParentFragmentManager().findFragmentById(R.id.paymentInfoFragment);
        contactMethodFragment = (ContactMethodFragment) getParentFragmentManager().findFragmentById(R.id.dealContactFragment);
        transactionDetailFragment = (TransactionDetailFragment) getParentFragmentManager().findFragmentById(R.id.transactionDetailFragment);
        paymentInfoFragment.setArguments(bundle);
        contactMethodFragment.setArguments(bundle);
        transactionDetailFragment.setArguments(bundle);
        buyerStatusValTv.setText(amountPaid);
        initTabSegment();
        countDown();
    }

    @Override
    protected void initAction() {

    }

    @OnClick({R.id.cancelOrderBtn, R.id.paidPutCoinBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelOrderBtn:
                showConfirmMessageDialog();
                break;
            case R.id.paidPutCoinBtn:
                break;
        }
    }


    private void initTabSegment() {
        String[] tabData = new String[]{getString(R.string.str_bank_card), getString(R.string.str_alipay), getString(R.string.str_wechat_pay)};
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();
        tabBuilder.setColor(ContextCompat.getColor(getActivity(), R.color.app_color_common_deputy),
                ContextCompat.getColor(getActivity(), R.color.app_color_orange));

        for (String tabText : tabData) {
            QMUITab tab = tabBuilder.setText(tabText).build(getContext());
            mTabSegment.addTab(tab);
        }
        int indicatorHeight = QMUIDisplayHelper.dp2px(getActivity(), 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.selectTab(mCurIndex);
        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
//               0按金额购买 1按数量购买
                mCurIndex = index;
                updateView();
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }

    private void showConfirmMessageDialog() {
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_cancellation_deal, null);
            AppCompatCheckBox checkBox = view.findViewById(R.id.checkBox);
            QMUIRoundButton actionBtn1 = view.findViewById(R.id.actionBtn1);
            QMUIRoundButton actionBtn2 = view.findViewById(R.id.actionBtn2);
            dialog = new AlertDialog.Builder(getActivity(), R.style.QMUI_Dialog).setView(view).create();
            actionBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            actionBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void updateView() {

    }

    /**
     * 倒计时显示
     */
    private void countDown() {
        timer = new CountDownTimer(15 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Map<String, Long> timeMap = TimeUtils.formatDuring(millisUntilFinished);
                paymentTimeRemainingValTv.setText(timeMap.get("minutes") + ":" + timeMap.get("seconds"));
            }

            @Override
            public void onFinish() {
//                paymentTimeRemainingValTv.setText("重新获取验证码");

            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
//        if (paymentInfoFragment != null) {
//            this.getChildFragmentManager().beginTransaction().remove(paymentInfoFragment).commit();
//        }
//        if (contactMethodFragment != null) {
//            this.getChildFragmentManager().beginTransaction().remove(contactMethodFragment).commit();
//        }
//        if (transactionDetailFragment != null) {
//            this.getChildFragmentManager().beginTransaction().remove(transactionDetailFragment).commit();
//        }
    }
}
