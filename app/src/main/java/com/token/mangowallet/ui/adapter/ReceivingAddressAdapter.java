package com.token.mangowallet.ui.adapter;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.ShippingAddressBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReceivingAddressAdapter extends BaseQuickAdapter<ShippingAddressBean.DataBean, BaseViewHolder> {

    private int mRadius;
    private OnEditAddressClickListener listener;

    public ReceivingAddressAdapter(List<ShippingAddressBean.DataBean> data) {
        super(R.layout.item_receiving_address, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ShippingAddressBean.DataBean dataBean) {
        /**
         * userId : 7784
         * userName : 借口
         * phone : 15088634
         * city : 内蒙古自治区 兴安盟 扎赉特旗
         * detailedAddress : 天空
         * isDefault : false
         * isDel : false
         * createTime : 2020-08-13
         * addrID : 8
         */
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 8);
        ((QMUIConstraintLayout) baseViewHolder.getView(R.id.addressCardLayout)).setRadius(mRadius);
        baseViewHolder.setText(R.id.userNameTv, dataBean.getUserName());
        baseViewHolder.setText(R.id.telTv, dataBean.getPhone());
        baseViewHolder.setText(R.id.addressTv, dataBean.getCountryName() + " " + (ObjectUtils.isEmpty(dataBean.getCity()) ? "" : dataBean.getCity()) + " " + dataBean.getDetailedAddress());
        AppCompatCheckBox defaultAddressBox = baseViewHolder.getView(R.id.defaultAddressBox);
        boolean isDefault = dataBean.isIsDefault();
        defaultAddressBox.setChecked(isDefault);
        defaultAddressBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onCheckedChanged(buttonView, isChecked, baseViewHolder.getLayoutPosition());
                }
            }
        });
        baseViewHolder.getView(R.id.editAddressTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditAddress(baseViewHolder.getLayoutPosition());
                }
            }
        });
        baseViewHolder.getView(R.id.delAddressTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteAddress(baseViewHolder.getLayoutPosition());
                }
            }
        });
    }

    public void setOnEditAddressClickListener(OnEditAddressClickListener listener) {
        this.listener = listener;
    }

    public interface OnEditAddressClickListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position);

        void onEditAddress(int position);

        void onDeleteAddress(int position);
    }
}
