package com.token.mangowallet.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.PayConfigBean;
import com.token.mangowallet.ui.adapter.BottomSheetAdapter;
import com.token.mangowallet.view.basepopup.BasePopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BottomSheetPopupWindow extends BasePopup {

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.bottomLayout)
    QMUIFrameLayout bottomLayout;
    private AppCompatTextView cancelBtn;
    private AppCompatTextView completeBtn;

    private OnBottomSheetListener listener;
    private Unbinder unbinder;
    private Activity mContext;
    private BottomSheetAdapter mBottomSheetAdapter;
    private List<PayConfigBean.DataBean> selPositionList = new ArrayList<>();
    private List<PayConfigBean.DataBean> payConfigList;

    public BottomSheetPopupWindow(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void initAttributes() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_bottom_layout, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口点击外部消失
        setBackgroundDimEnable(false);
    }

    @Override
    protected void initViews(View view) {
        int mRadius = QMUIDisplayHelper.dp2px(mContext, 15);
        bottomLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);
        mBottomSheetAdapter = new BottomSheetAdapter();
        View mHeaderView = mContext.getLayoutInflater().inflate(R.layout.view_title_pop, null);
        cancelBtn = mHeaderView.findViewById(R.id.cancelBtn);
        completeBtn = mHeaderView.findViewById(R.id.completeBtn);
        mBottomSheetAdapter.addHeaderView(mHeaderView, 0);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mBottomSheetAdapter);

        mBottomSheetAdapter.setOnItemViewClickListener(new BottomSheetAdapter.OnItemViewClickListener() {
            @Override
            public void onItemView(AppCompatCheckBox radioButton, int position) {
                PayConfigBean.DataBean dataBean = payConfigList.get(position - 1);
                boolean isRadio = radioButton.isChecked();
                if (isRadio) {
                    selPositionList.add(dataBean);
                } else {
                    selPositionList.remove(dataBean);
                }
            }
        });
        cancelBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                selPositionList.clear();
                dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        completeBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onComplete(selPositionList);
                }
            }
        });
    }

    public List<PayConfigBean.DataBean> getSelData() {
        return selPositionList;
    }

    public void setData(List<PayConfigBean.DataBean> payConfigList) {
        this.payConfigList = payConfigList;
        mBottomSheetAdapter.setList(payConfigList);
    }

    public void addHeaderView(View view) {
        mBottomSheetAdapter.addHeaderView(view);
    }

    public void addFooterView(View view) {
        mBottomSheetAdapter.setFooterView(view);
    }

    public void setOnBottomSheetListener(OnBottomSheetListener listener) {
        this.listener = listener;
    }

    public interface OnBottomSheetListener {
        void onCancel();

        void onComplete(List<PayConfigBean.DataBean> selPositionList);
    }
}
