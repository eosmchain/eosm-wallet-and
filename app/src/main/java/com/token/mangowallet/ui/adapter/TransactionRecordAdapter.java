package com.token.mangowallet.ui.adapter;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.TransactionRecordBean;
import com.token.mangowallet.bean.TransferDataBean;
import com.token.mangowallet.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

public class TransactionRecordAdapter extends BaseQuickAdapter<TransactionRecordBean.ActionsBean, BaseViewHolder> {
    private TimeUtils timeUtils;
    private String currentAccount = "";

    public TransactionRecordAdapter(String currentAccount) {
        super(R.layout.item_history);
        this.currentAccount = currentAccount;
        timeUtils = new TimeUtils();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TransactionRecordBean.ActionsBean actionsBean) {
        View view = baseViewHolder.findView(R.id.billItem);
        QMUIRadiusImageView iconIv = baseViewHolder.findView(R.id.iconIv);
        AppCompatTextView accountsTv = baseViewHolder.findView(R.id.accountsTv);
        AppCompatTextView timeTv = baseViewHolder.findView(R.id.timeTv);
        AppCompatTextView amountTv = baseViewHolder.findView(R.id.amountTv);
        AppCompatTextView typeTv = baseViewHolder.findView(R.id.typeTv);

        TransactionRecordBean.ActionsBean.ActionTraceBean actionTraceBean = actionsBean.getAction_trace();
        TransactionRecordBean.ActionsBean.ActionTraceBean.ActBean actBean = actionTraceBean.getAct();
        try {
            TransferDataBean dataBean = GsonUtils.fromJson(GsonUtils.toJson(actBean.getData()), new TypeToken<TransferDataBean>() {
            }.getType());

            String to = dataBean.getTo();
            String from = dataBean.getFrom();
            LogUtils.dTag("BillAdapter==", " from = " + from);
            String quantity = dataBean.getQuantity();
            String name = actBean.getName();
            String block_time = actionTraceBean.getBlock_time();
            String trx_id = actionTraceBean.getTrx_id();
            int res = -1;
            int color = R.color.app_color_blue;
            boolean isOut = false;
            String account;
            if (ObjectUtils.equals(currentAccount, to)) {
                res = R.mipmap.into_icon;
                color = R.color.app_color_blue;
                isOut = false;
                account = from;
            } else {
                res = R.mipmap.out_icon;
                color = R.color.app_color_dark_blue;
                isOut = true;
                account = to;
            }
            baseViewHolder.setImageResource(R.id.iconIv,res);
            accountsTv.setText(account);
            timeTv.setText(timeUtils.getStringTime(block_time));
            amountTv.setText((isOut ? "-" : "+") + quantity);
            amountTv.setTextColor(ContextCompat.getColor(getContext(), color));
            typeTv.setText(name);
            baseViewHolder.itemView.setTag(R.id.OrderID, trx_id);
            baseViewHolder.itemView.setTag(R.id.quantity, quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
