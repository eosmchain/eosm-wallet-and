package com.token.mangowallet.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.SelordersBean;
import com.token.mangowallet.utils.APPUtils;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.view.basepopup.BasePopup;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.token.mangowallet.utils.Constants.MGP_SYMBOL;
import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.FLOOR;

public class DealMGPPopup extends BasePopup {

    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.quotaTv)
    AppCompatTextView quotaTv;
    @BindView(R.id.editText)
    AppCompatEditText editText;
    @BindView(R.id.unitTv)
    AppCompatTextView unitTv;
    @BindView(R.id.numberTransactionValTv)
    AppCompatTextView numberTransactionValTv;
    @BindView(R.id.disbursementsValTv)
    AppCompatTextView disbursementsValTv;
    @BindView(R.id.content)
    QMUIConstraintLayout content;
    @BindView(R.id.priceValTv)
    AppCompatTextView priceValTv;

    private Context mContext;
    private Unbinder unbinder;
    public int mCurIndex = 0;
    private OnOrdersClickedListener listener;
    private SelordersBean.RowsBean rowsBean;
    public BigDecimal price = BigDecimal.ZERO;
    public BigDecimal quantity = BigDecimal.ZERO;
    public BigDecimal frozen_quantity = BigDecimal.ZERO;
    public BigDecimal fufilled_quantity = BigDecimal.ZERO;
    public BigDecimal min_accept_quantity = BigDecimal.ZERO;
    public BigDecimal max_accept_quantity = BigDecimal.ZERO;
    public BigDecimal min_mgp_num = BigDecimal.ZERO;
    public BigDecimal remaining_quantity = BigDecimal.ZERO;// 剩余数量
    private String purchaseNum = "0";
    private String buyNum = "0";

    public DealMGPPopup(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void initAttributes() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_buy_mgp, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);

        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口点击外部消失
        setBackgroundDimEnable(true);
    }

    @Override
    protected void initViews(View view) {
        int mRadius = QMUIDisplayHelper.dp2px(mContext, 8);
        content.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        initTabSegment();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updataBuyData(ObjectUtils.isEmpty(s) ? "0" : s.toString(), mCurIndex);
            }

            @Override
            public void afterTextChanged(Editable s) {
                updataBuyData(ObjectUtils.isEmpty(s) ? "0" : s.toString(), mCurIndex);
            }
        });
    }

    private void initTabSegment() {
        String[] tabData = new String[]{getString(R.string.str_amount_buy), getString(R.string.str_number_buy)};
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder();
        tabBuilder.setColor(ContextCompat.getColor(mContext, R.color.app_color_common_deputy),
                ContextCompat.getColor(mContext, R.color.app_color_common_dark_black));

        for (String tabText : tabData) {
            QMUITab tab = tabBuilder.setText(tabText).build(getContext());
            mTabSegment.addTab(tab);
        }
        int indicatorHeight = QMUIDisplayHelper.dp2px(mContext, 2);
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.selectTab(mCurIndex);
        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
//               0按金额购买 1按数量购买
                mCurIndex = index;
                updateView();
                updataBuyData(purchaseNum, index);
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

    private void updateView() {
        /**
         *   "id": 0, // 订单ID
         *       "owner": "mgptest11111", // 挂售人
         *       "price": "1000.00 CNY", // 价格
         *       "quantity": "1000.0000 MGP", // 总数
         *       "min_accept_quantity": "10.00 CNY", // 总价
         *       "frozen_quantity": "0 ", // 冻结币
         *       "fufilled_quantity": "0 ", // 交易完成数量
         *       "closed": 1, // 是否关闭
         *       "created_at": "2020-12-31T03:05:50",
         *       "closed_at": "2020-12-31T03:06:32"
         */
        if (rowsBean != null) {
            if (ObjectUtils.isNotEmpty(rowsBean.getPrice())) { // 价格
                String priceStr = rowsBean.getPrice();
                priceStr = priceStr.split(" ")[0];
                price = new BigDecimal(ObjectUtils.isEmpty(priceStr) ? "0" : priceStr);
            }
            if (ObjectUtils.isNotEmpty(rowsBean.getQuantity())) {// 总数
                String quantityStr = rowsBean.getQuantity();
                quantityStr = quantityStr.split(" ")[0];
                quantity = new BigDecimal(ObjectUtils.isEmpty(quantityStr) ? "0" : quantityStr);
            }
            if (ObjectUtils.isNotEmpty(rowsBean.getFrozen_quantity())) {// 冻结币
                String frozen_quantityStr = rowsBean.getFrozen_quantity();
                frozen_quantityStr = frozen_quantityStr.split(" ")[0];
                frozen_quantity = new BigDecimal(ObjectUtils.isEmpty(frozen_quantityStr) ? "0" : frozen_quantityStr);
            }
            if (ObjectUtils.isNotEmpty(rowsBean.getFufilled_quantity())) {//交易完成数量
                String fufilled_quantityStr = rowsBean.getFufilled_quantity();
                fufilled_quantityStr = fufilled_quantityStr.split(" ")[0];
                fufilled_quantity = new BigDecimal(ObjectUtils.isEmpty(fufilled_quantityStr) ? "0" : fufilled_quantityStr);
            }
            if (ObjectUtils.isNotEmpty(rowsBean.getMin_accept_quantity())) {
                String min_accept_quantityStr = rowsBean.getMin_accept_quantity();
                min_accept_quantityStr = min_accept_quantityStr.split(" ")[0];
                min_accept_quantity = new BigDecimal(ObjectUtils.isEmpty(min_accept_quantityStr) ? "0" : min_accept_quantityStr);
            }
            remaining_quantity = quantity.subtract(frozen_quantity).subtract(fufilled_quantity);
            max_accept_quantity = remaining_quantity.multiply(price);

            min_mgp_num = min_accept_quantity.divide(price, 4, BigDecimal.ROUND_HALF_UP);

//            String amountInterval = BalanceUtils.currencyToBase(min_accept_quantity.toPlainString(), 2, RoundingMode.CEILING)
//                    + "-" + BalanceUtils.currencyToBase(max_accept_quantity.toPlainString(), 2, RoundingMode.CEILING);
//            String mgpNumInterval = APPUtils.dataFormat(min_mgp_num.toPlainString())
//                    + "-" + APPUtils.dataFormat(remaining_quantity.toPlainString())
//                    + " " + MGP_SYMBOL;
            String amountInterval = min_accept_quantity.toPlainString()
                    + "-" + max_accept_quantity.toPlainString() + " CNY";
            String mgpNumInterval = APPUtils.dataFormat(min_mgp_num.toPlainString())
                    + "-" + APPUtils.dataFormat(remaining_quantity.toPlainString())
                    + " " + MGP_SYMBOL;
            editText.setFilters(new InputFilter[]{new CashierInputFilter(mCurIndex == 0 ? max_accept_quantity.setScale(2, FLOOR)
                    : remaining_quantity.setScale(4, FLOOR), mCurIndex == 0 ? 2 : 4)});
            priceValTv.setText(price.toPlainString());//BalanceUtils.currencyToBase(price.toPlainString(), 2, RoundingMode.FLOOR));
            quotaTv.setText(getString(R.string.str_quota) + " " + (mCurIndex == 0 ? amountInterval : mgpNumInterval));
            unitTv.setText(mCurIndex == 0 ? "CNY" : "MGP");
        }
    }

    private void updataBuyData(String num, int index) {
        if (ObjectUtils.isNotEmpty(num)) {
            if (!ObjectUtils.equals(num, purchaseNum) || mCurIndex == index) {
                BigDecimal bigDecimal = new BigDecimal(num);

                if (mCurIndex == 0) {
                    BigDecimal buyNum = bigDecimal.divide(price, 4, BigDecimal.ROUND_FLOOR);
                    this.buyNum = buyNum.toPlainString();
                    numberTransactionValTv.setText(buyNum + " " + MGP_SYMBOL);
                    disbursementsValTv.setText(num + " CNY");//BalanceUtils.currencyToBase(num, 2, RoundingMode.FLOOR));
                } else {
                    BigDecimal buyA = bigDecimal.multiply(price);
                    this.buyNum = bigDecimal.setScale(4, FLOOR).toPlainString();
                    numberTransactionValTv.setText(num + " " + MGP_SYMBOL);
                    disbursementsValTv.setText(buyA.setScale(2, BigDecimal.ROUND_FLOOR).toPlainString() + " CNY");//BalanceUtils.currencyToBase(buyNum.toPlainString(), 2, RoundingMode.FLOOR));
                }
            }
        }
        purchaseNum = num;
    }

    @OnClick({R.id.allBuyTv, R.id.ordersBtn})//, R.id.pullDownIv
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.allBuyTv:
                if (mCurIndex == 0) {
                    editText.setText(max_accept_quantity.setScale(2, FLOOR).toPlainString());
                } else {
                    editText.setText(remaining_quantity.setScale(4, FLOOR).toPlainString());
                }
                break;
            case R.id.ordersBtn:
                if (listener != null) {
                    if (ObjectUtils.isEmpty(editText.getText())) {
                        ToastUtils.showShort(R.string.str_import_buy_num);
                    } else {
                        listener.onOrders(rowsBean.getId(), mCurIndex, editText.getText().toString(), disbursementsValTv.getText().toString(), buyNum);
                    }
                }
                break;
//            case R.id.pullDownIv:
//                dismiss();
//                break;
        }
    }

    public void setSelordersBean(SelordersBean.RowsBean rowsBean) {
        this.rowsBean = rowsBean;
    }

    public void showPop(View playview) {
        updateView();
        this.showAtLocation(playview, Gravity.BOTTOM, 0, 0);
    }

    public void setOnOrdersClickedListener(OnOrdersClickedListener listener) {
        this.listener = listener;
    }

    public interface OnOrdersClickedListener {
        /**
         * @param index      0=按金额；1=按MGP数量
         * @param num        index=0=金额数；index=1=MGP数量
         * @param amountPaid 实付金额
         * @param buyNum     实付mgp数量
         */
        void onOrders(int ordersId, int index, String num, String amountPaid, String buyNum);
    }
}
