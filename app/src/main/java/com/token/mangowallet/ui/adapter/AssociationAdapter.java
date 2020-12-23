package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.MortgageAssociationBean;
import com.token.mangowallet.utils.BalanceUtils;
import com.token.mangowallet.utils.ClipboardUtils;
import com.token.mangowallet.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.util.List;

public class AssociationAdapter extends BaseQuickAdapter<MortgageAssociationBean.DataBean.TeamListBean, BaseViewHolder> {
    private int mRadius;
    private Constants.WalletType walletType;

    public AssociationAdapter(List<MortgageAssociationBean.DataBean.TeamListBean> dataList, Constants.WalletType walletType) {
        super(R.layout.item_mid, dataList);
        this.walletType = walletType;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MortgageAssociationBean.DataBean.TeamListBean teamListBean) {
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        QMUIConstraintLayout midLayout = baseViewHolder.getView(R.id.midLayout);
        AppCompatImageView midIconIv = baseViewHolder.getView(R.id.midIconIv);
        AppCompatTextView midTv = baseViewHolder.getView(R.id.midTv);
        AppCompatTextView roleTv = baseViewHolder.getView(R.id.roleTv);
        AppCompatTextView addressTv = baseViewHolder.getView(R.id.addressTv);
        AppCompatTextView mgpTv = baseViewHolder.getView(R.id.mgpTv);
        AppCompatTextView dollarTv = baseViewHolder.getView(R.id.dollarTv);
        List<String> roleList = teamListBean.getRoleList();
        String role = "";
        if (roleList != null) {
            if (roleList.size() > 0) {
                role = roleList.get(0);
            }
        }
        midLayout.setRadius(mRadius);
        midTv.setText(ObjectUtils.isEmpty(teamListBean.getCode()) ? "" : teamListBean.getCode());
        roleTv.setText(ObjectUtils.isEmpty(role) ? "" : "(" + role + ")");
        addressTv.setText(ObjectUtils.isEmpty(teamListBean.getTeamAddress()) ? "" : teamListBean.getTeamAddress());
        mgpTv.setText((ObjectUtils.isEmpty(teamListBean.getCoinNum()) ? "0.0000" : teamListBean.getCoinNum()) + walletType);
        dollarTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(teamListBean.getCoinValue()) ? "0.00" : teamListBean.getCoinValue(), 2, RoundingMode.FLOOR));

        midTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(teamListBean.getCode());
                ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
            }
        });
        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(teamListBean.getTeamAddress());
                ToastUtils.showShort(StringUtils.getString(R.string.str_copy_success));
            }
        });
    }
}
